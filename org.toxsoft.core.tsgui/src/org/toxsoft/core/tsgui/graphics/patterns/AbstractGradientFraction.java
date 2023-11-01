package org.toxsoft.core.tsgui.graphics.patterns;

import org.eclipse.swt.graphics.*;

/**
 * Базовый класс для создания фракций градиентов.
 * <p>
 *
 * @author vs
 */
public abstract class AbstractGradientFraction
    implements IGradientFraction {

  private final GradientFractionInfo fi;

  private final int redLength;

  private final int blueLength;

  private final int greenLength;

  private final int alphaLength;

  private final double delta;

  AbstractGradientFraction( double aStartValue, double aEndValue, RGBA aStartRGB, RGBA aEndRGB ) {
    fi = new GradientFractionInfo( aStartValue, aEndValue, aStartRGB, aEndRGB );
    delta = fi.endValue() - fi.startValue();

    redLength = fi.endRGBA().rgb.red - fi.startRGBA().rgb.red;
    blueLength = fi.endRGBA().rgb.blue - fi.startRGBA().rgb.blue;
    greenLength = fi.endRGBA().rgb.green - fi.startRGBA().rgb.green;
    alphaLength = fi.endRGBA().alpha - fi.startRGBA().alpha;
  }

  AbstractGradientFraction( GradientFractionInfo aInfo ) {
    fi = aInfo;
    delta = fi.endValue() - fi.startValue();

    redLength = fi.endRGBA().rgb.red - fi.startRGBA().rgb.red;
    blueLength = fi.endRGBA().rgb.blue - fi.startRGBA().rgb.blue;
    greenLength = fi.endRGBA().rgb.green - fi.startRGBA().rgb.green;
    alphaLength = fi.endRGBA().alpha - fi.startRGBA().alpha;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IGradientFraction}
  //

  @Override
  public boolean isMine( double aValue ) {
    if( aValue >= fi.startValue() && aValue <= fi.endValue() ) {
      return true;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает параметры фракции градиента.<br>
   *
   * @return GradientFractionInfo - параметры фракции
   */
  public GradientFractionInfo fractionInfo() {
    return fi;
  }

  // ------------------------------------------------------------------------------------
  // Методы для использования в наследниках
  //

  protected double startValue() {
    return fi.startValue();
  }

  protected RGBA startRgba() {
    return fi.startRGBA();
  }

  protected double endValue() {
    return fi.endValue();
  }

  protected RGBA endRgba() {
    return fi.endRGBA();
  }

  protected double delta() {
    return delta;
  }

  protected int redLength() {
    return redLength;
  }

  protected int blueLength() {
    return blueLength;
  }

  protected int greenLength() {
    return greenLength;
  }

  protected int alphaLength() {
    return alphaLength;
  }

  // ------------------------------------------------------------------------------------
  // Методы для обязательного переопределения в наследниках
  //

  @Override
  public abstract RGBA calcRgb( double aValue );

  protected abstract IGradientFraction create( double aStartValue, double aEndValue, RGBA aStartRGB, RGBA aEndRGB );

}
