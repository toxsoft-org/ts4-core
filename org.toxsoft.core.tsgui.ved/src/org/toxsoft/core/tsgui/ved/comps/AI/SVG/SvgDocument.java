package org.toxsoft.core.tsgui.ved.comps.AI.SVG;

import java.util.ArrayList;
import java.util.List;

/**
 * Результат парсинга SVG-файла.
 * Содержит метаданные документа и список всех распознанных фигур.
 */
public class SvgDocument {

    private double width;
    private double height;
    private String viewBox;
    private String version;

    private final List<SvgShape> shapes = new ArrayList<>();

    public double getWidth()     { return width; }
    public double getHeight()    { return height; }
    public String getViewBox()   { return viewBox; }
    public String getVersion()   { return version; }
    public List<SvgShape> getShapes() { return shapes; }

    public void setWidth(double width)    { this.width = width; }
    public void setHeight(double height)  { this.height = height; }
    public void setViewBox(String vb)     { this.viewBox = vb; }
    public void setVersion(String ver)    { this.version = ver; }

    public void addShape(SvgShape shape)  { shapes.add(shape); }

    /** Вернуть только фигуры заданного типа */
    public <T extends SvgShape> List<T> getShapesOfType(Class<T> clazz) {
        List<T> result = new ArrayList<>();
        for (SvgShape s : shapes) {
            if (clazz.isInstance(s)) result.add(clazz.cast(s));
        }
        return result;
    }

    @Override
    public String toString() {
        return String.format("SvgDocument [%.0fx%.0f viewBox=%s shapes=%d]",
            width, height, viewBox, shapes.size());
    }
}
