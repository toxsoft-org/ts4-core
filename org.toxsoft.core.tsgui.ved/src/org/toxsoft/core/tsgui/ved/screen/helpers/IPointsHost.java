package org.toxsoft.core.tsgui.ved.screen.helpers;

import org.toxsoft.core.tslib.bricks.d2.*;

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

  default boolean isYours( ID2Point aPoint ) {
    return isYours( aPoint.x(), aPoint.y() );
  }

  /**
   * Возвращает описывающий прямоугольник.<br>
   *
   * @return {@link ID2Rectangle} - описывающий прямоугольник
   */
  ID2Rectangle bounds();

}
