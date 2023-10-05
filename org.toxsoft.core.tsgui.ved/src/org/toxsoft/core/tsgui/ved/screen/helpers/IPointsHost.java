package org.toxsoft.core.tsgui.ved.screen.helpers;

import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * "Хозяин" 2d-точек
 *
 * @author vs
 */
public interface IPointsHost {

  /**
   * Определяет является ли объект хозяином, переданной точки.<br>
   * Иными словами определяет принадлежит ли точка объекту.
   *
   * @param aX double - x координата точки
   * @param aY double - y координата точки
   * @return <b>true</b> - точка принадлежит объекту<br>
   *         <b>false</b> - точка не принадлежит объекту
   */
  boolean isYours( double aX, double aY );

  /**
   * Возвращает описывающий прямоугольник.<br>
   *
   * @return Rectangle - описывающий прямоугольник
   */
  ITsRectangle bounds();

}
