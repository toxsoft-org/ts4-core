package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;

/**
 * Фракция радиального градиента.
 * <p>
 *
 * @author vs
 */
public class LinearGradientFraction
    extends AbstractGradientFraction {

  /**
   * Конструктор.
   * <p>
   *
   * @param aStartValue double - начальное значение
   * @param aStartRgb RGBA - компоненты цвета начальной точки
   * @param aEndValue double - конечное значение
   * @param aEndRgb RGBA - компоненты цвета конечной точки
   */
  public LinearGradientFraction( double aStartValue, RGBA aStartRgb, double aEndValue, RGBA aEndRgb ) {
    super( aStartValue / 100., aEndValue / 100., aStartRgb, aEndRgb );
    // nop
  }

  @Override
  public RGBA calcRgb( double aValue ) {
    double k = Math.abs( (aValue - startValue()) / delta() );
    int red = (int)Math.round( startRgba().rgb.red + redLength() * k );
    int g = (int)Math.round( startRgba().rgb.green + greenLength() * k );
    int b = (int)Math.round( startRgba().rgb.blue + blueLength() * k );
    int alpha = (int)Math.round( startRgba().alpha + alphaLength() * k );
    return new RGBA( Math.abs( red ), Math.abs( g ), Math.abs( b ), Math.abs( alpha ) );
  }

  @Override
  protected IGradientFraction create( double aStartValue, double aEndValue, RGBA aStartRGB, RGBA aEndRGB ) {
    return new LinearGradientFraction( aStartValue, aStartRGB, aEndValue, aEndRGB );
  }

}
