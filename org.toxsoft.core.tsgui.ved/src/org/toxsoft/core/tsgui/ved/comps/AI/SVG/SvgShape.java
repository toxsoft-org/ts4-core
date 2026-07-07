package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Базовый класс для любой SVG-фигуры.
 * Все атрибуты хранятся в map; типовые (fill, stroke, opacity)
 * вынесены в отдельные поля для удобного доступа.
 */
public abstract class SvgShape {

    /** Тип фигуры: rect, circle, ellipse, line, polyline, polygon, path, text */
    private final String type;

    /** id атрибут элемента (может быть null) */
    private String id;

    /** Цвет заливки, например "#D85A30" или "none" */
    private String fill;

    /** Цвет обводки */
    private String stroke;

    /** Толщина обводки */
    private String strokeWidth;

    /** Прозрачность 0.0 – 1.0 */
    private double opacity = 1.0;

    /** transform атрибут в исходном виде */
    private String transform;

    /** Все остальные атрибуты элемента */
    private final Map<String, String> attributes = new LinkedHashMap<>();

    protected SvgShape(String type) {
        this.type = type;
    }

    public String getType()        { return type; }
    public String getId()          { return id; }
    public String getFill()        { return fill; }
    public String getStroke()      { return stroke; }
    public String getStrokeWidth() { return strokeWidth; }
    public double getOpacity()     { return opacity; }
    public String getTransform()   { return transform; }
    public Map<String, String> getAttributes() { return attributes; }

    public void setId(String id)               { this.id = id; }
    public void setFill(String fill)           { this.fill = fill; }
    public void setStroke(String stroke)       { this.stroke = stroke; }
    public void setStrokeWidth(String sw)      { this.strokeWidth = sw; }
    public void setOpacity(double opacity)     { this.opacity = opacity; }
    public void setTransform(String transform) { this.transform = transform; }
    public void setAttribute(String k, String v) { attributes.put(k, v); }
    public String getAttribute(String k)       { return attributes.get(k); }

    @Override
    public String toString() {
        return String.format("[%s] id=%s fill=%s stroke=%s attrs=%s",
            type, id, fill, stroke, attributes);
    }
}
