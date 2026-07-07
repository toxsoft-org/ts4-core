package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.File;
import java.io.InputStream;

/**
 * Парсер SVG-файлов на основе стандартного Java DOM (без внешних зависимостей).
 *
 * Поддерживаемые элементы:
 *   rect, circle, ellipse, line, polyline, polygon, path, text
 *
 * Использование:
 *   SvgParser parser = new SvgParser();
 *   SvgDocument doc  = parser.parse(new File("ring.svg"));
 *   for (SvgShape shape : doc.getShapes()) { ... }
 */
public class SvgParser {

    // ---------------------------------------------------------------
    // Публичное API
    // ---------------------------------------------------------------

    public SvgDocument parse(File file) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xml = builder.parse(file);
        return buildDocument(xml);
    }

    public SvgDocument parse(InputStream stream) throws Exception {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document xml = builder.parse(stream);
        return buildDocument(xml);
    }

    // ---------------------------------------------------------------
    // Внутренняя логика
    // ---------------------------------------------------------------

    private SvgDocument buildDocument(Document xml) {
        xml.getDocumentElement().normalize();
        Element root = xml.getDocumentElement();

        SvgDocument doc = new SvgDocument();
        doc.setWidth(parseDouble(root.getAttribute("width"), 0));
        doc.setHeight(parseDouble(root.getAttribute("height"), 0));
        doc.setViewBox(root.getAttribute("viewBox"));
        doc.setVersion(root.getAttribute("version"));

        // Обходим всё дерево рекурсивно
        parseChildren(root, doc);

        return doc;
    }

    /** Рекурсивный обход дочерних узлов (поддерживает <g> группы) */
    private void parseChildren(Element parent, SvgDocument doc) {
        NodeList children = parent.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node node = children.item(i);
            if (node.getNodeType() != Node.ELEMENT_NODE) continue;

            Element el = (Element) node;
            String tag = el.getLocalName() != null
                ? el.getLocalName()
                : el.getTagName().replaceAll(".*:", "");  // убрать префикс пространства имён

            SvgShape shape = null;

            switch (tag) {
                case "rect":     shape = parseRect(el);     break;
                case "circle":   shape = parseCircle(el);   break;
                case "ellipse":  shape = parseEllipse(el);  break;
                case "line":     shape = parseLine(el);      break;
                case "polyline": shape = parsePolyline(el); break;
                case "polygon":  shape = parsePolygon(el);  break;
                case "path":     shape = parsePath(el);     break;
                case "text":     shape = parseText(el);     break;
                case "g":
                case "svg":
                    // Группа или вложенный SVG — спускаемся глубже
                    parseChildren(el, doc);
                    break;
                default:
                    // Неизвестный тег — игнорируем
                    break;
            }

            if (shape != null) {
                applyCommonAttributes(el, shape);
                doc.addShape(shape);
            }
        }
    }

    // ---------------------------------------------------------------
    // Парсинг отдельных фигур
    // ---------------------------------------------------------------

    private SvgRect parseRect(Element el) {
        SvgRect r = new SvgRect();
        r.setX(attr(el, "x"));
        r.setY(attr(el, "y"));
        r.setWidth(attr(el, "width"));
        r.setHeight(attr(el, "height"));
        r.setRx(attr(el, "rx"));
        r.setRy(attr(el, "ry"));
        return r;
    }

    private SvgCircle parseCircle(Element el) {
        SvgCircle c = new SvgCircle();
        c.setCx(attr(el, "cx"));
        c.setCy(attr(el, "cy"));
        c.setR(attr(el, "r"));
        return c;
    }

    private SvgEllipse parseEllipse(Element el) {
        SvgEllipse e = new SvgEllipse();
        e.setCx(attr(el, "cx"));
        e.setCy(attr(el, "cy"));
        e.setRx(attr(el, "rx"));
        e.setRy(attr(el, "ry"));
        return e;
    }

    private SvgLine parseLine(Element el) {
        SvgLine l = new SvgLine();
        l.setX1(attr(el, "x1"));
        l.setY1(attr(el, "y1"));
        l.setX2(attr(el, "x2"));
        l.setY2(attr(el, "y2"));
        return l;
    }

    private SvgPolyline parsePolyline(Element el) {
        SvgPolyline p = new SvgPolyline();
        p.setPoints(el.getAttribute("points"));
        return p;
    }

    private SvgPolygon parsePolygon(Element el) {
        SvgPolygon p = new SvgPolygon();
        p.setPoints(el.getAttribute("points"));
        return p;
    }

    private SvgPath parsePath(Element el) {
        SvgPath p = new SvgPath();
        p.setD(el.getAttribute("d"));
        return p;
    }

    private SvgText parseText(Element el) {
        SvgText t = new SvgText();
        t.setX(attr(el, "x"));
        t.setY(attr(el, "y"));
        t.setContent(el.getTextContent());
        t.setFontFamily(el.getAttribute("font-family"));
        t.setFontSize(el.getAttribute("font-size"));
        return t;
    }

    // ---------------------------------------------------------------
    // Общие атрибуты: id, fill, stroke, opacity, transform, style
    // ---------------------------------------------------------------

    private void applyCommonAttributes(Element el, SvgShape shape) {
        shape.setId(el.getAttribute("id"));
        shape.setTransform(el.getAttribute("transform"));

        // Атрибуты могут быть заданы напрямую или через style="fill:...;stroke:..."
        String fill        = el.getAttribute("fill");
        String stroke      = el.getAttribute("stroke");
        String strokeWidth = el.getAttribute("stroke-width");
        String opacityStr  = el.getAttribute("opacity");

        // Разбор inline style (переопределяет отдельные атрибуты)
        String styleAttr = el.getAttribute("style");
        if (!styleAttr.isEmpty()) {
            for (String declaration : styleAttr.split(";")) {
                String[] kv = declaration.split(":", 2);
                if (kv.length != 2) continue;
                String key = kv[0].trim();
                String val = kv[1].trim();
                switch (key) {
                    case "fill":         fill        = val; break;
                    case "stroke":       stroke      = val; break;
                    case "stroke-width": strokeWidth = val; break;
                    case "opacity":      opacityStr  = val; break;
                    default:
                        shape.setAttribute(key, val);
                }
            }
        }

        if (!fill.isEmpty())        shape.setFill(fill);
        if (!stroke.isEmpty())      shape.setStroke(stroke);
        if (!strokeWidth.isEmpty()) shape.setStrokeWidth(strokeWidth);
        if (!opacityStr.isEmpty()) {
            try { shape.setOpacity(Double.parseDouble(opacityStr)); }
            catch (NumberFormatException ignored) {}
        }

        // Сохраняем все прочие атрибуты
        NamedNodeMap attrs = el.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            Node a = attrs.item(i);
            String name = a.getNodeName();
            // Пропускаем уже обработанные
            if (!name.matches("id|fill|stroke|stroke-width|opacity|transform|style|" +
                              "x|y|width|height|rx|ry|cx|cy|r|x1|y1|x2|y2|d|points|" +
                              "font-family|font-size")) {
                shape.setAttribute(name, a.getNodeValue());
            }
        }
    }

    // ---------------------------------------------------------------
    // Вспомогательные методы
    // ---------------------------------------------------------------

    /** Читает числовой атрибут, убирая единицы (px, pt, em...) */
    private double attr(Element el, String name) {
        String val = el.getAttribute(name);
        return parseDouble(val, 0.0);
    }

    private double parseDouble(String val, double def) {
        if (val == null || val.isEmpty()) return def;
        try {
            // Убрать единицы: px, pt, em, rem, %
            return Double.parseDouble(val.replaceAll("[a-zA-Z%]+$", "").trim());
        } catch (NumberFormatException e) {
            return def;
        }
    }
}
