package org.toxsoft.core.tsgui.ved.utils;

import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Стандартная реализация прямоугольной рамки.
 * <p>
 * <b>Мотивация:</b><br>
 * Наследование от {@link D2RectOutline} вызвано необходимостью переопределения метода {@link #contains(double, double)}
 *
 * @author vs
 */
public class D2BorderOutline
    extends D2RectOutline {

  private final int lineWidth;

  /**
   * Конструктор.
   *
   * @param aRect ID2Rectangle - прямоугольник
   * @param aLineWidth - толщина линии границы
   */
  public D2BorderOutline( ID2Rectangle aRect, int aLineWidth ) {
    super( aRect );
    lineWidth = aLineWidth;
  }

  /**
   * Конструктор.
   *
   * @param aX double - x координата левого верхнего угла
   * @param aY double - y координата левого верхнего угла
   * @param aWidth double - ширина прямоугольника
   * @param aHeight double - высота прямоугольника
   * @param aLineWidth - толщина линии границы
   */
  public D2BorderOutline( double aX, double aY, double aWidth, double aHeight, int aLineWidth ) {
    super( aX, aY, aWidth, aHeight );
    lineWidth = aLineWidth;
  }

  // ------------------------------------------------------------------------------------
  // {@link D2RectOutline}
  //

  @Override
  public boolean contains( double aX, double aY ) {
    if( super.contains( aX, aY ) ) {
      return distanceTo( aX, aY ) < 3. + lineWidth;
    }
    return false;
  }

}
