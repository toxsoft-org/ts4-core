package org.toxsoft.core.tsgui.ved.olds.incub;

import org.toxsoft.core.tsgui.ved.olds.incub.geom.*;

/**
 * Контур геометрической фигуры.
 * <p>
 *
 * @author vs
 */
public interface IShapeOutline {

  /**
   * Прямоугольник, ограничивающий область в экранных координатах (с учетом масштаба).
   *
   * @return Rectangle2D - прямоугольник, ограничивающий область
   */
  ID2Rectangle bounds();

  /**
   * Определяет находится ли точка внутри геометрической фигуры.<br>
   *
   * @param aX double - координата точки по оси X
   * @param aY double - координата точки по оси Y
   * @return <b>true</b><br>
   *         <b>false</b>
   */
  boolean contains( double aX, double aY );

  /**
   * Возвращает точку, соответствующую центру фигуры.
   * <p>
   *
   * @return ID2Point - точка, соответствующая центру фигуры
   */
  ID2Point center();

  /**
   * Находит точку на границе фигуры ближайшую к указанной.
   *
   * @param aPoint ID2Point - точка
   * @return ID2Point - точка на границе фигуры ближайшая к указанной
   */
  ID2Point nearestPoint( ID2Point aPoint );

}
