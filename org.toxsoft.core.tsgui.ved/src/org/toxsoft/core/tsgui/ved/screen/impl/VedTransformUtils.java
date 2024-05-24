package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Набор вспомогательных методов преобразования координат для {@link IVedScreen}.
 * <p>
 *
 * @author vs
 */
public class VedTransformUtils {

  /**
   * Возвращает X-координату точки, вокруг которой осуществляется вращение визуального элемента.
   *
   * @param aProps {@link IOptionSet} - набор свойств визуального элемента
   * @return double - X-координату точки, вокруг которой осуществляется вращение
   */
  public static double viselRotationX( IOptionSet aProps ) {
    double zoom = aProps.getDouble( PROP_ZOOM );
    TsFulcrum tsf = aProps.getValobj( PROP_TS_FULCRUM );
    double width = aProps.getDouble( PROP_WIDTH );
    return zoom * (tsf.xPerc() * width) / 100.;
  }

  /**
   * Возвращает Y-координату точки, вокруг которой осуществляется вращение визуального элемента.
   *
   * @param aProps {@link IOptionSet} - набор свойств визуального элемента
   * @return double - Y-координату точки, вокруг которой осуществляется вращение
   */
  public static double viselRotationY( IOptionSet aProps ) {
    double zoom = aProps.getDouble( PROP_ZOOM );
    TsFulcrum tsf = aProps.getValobj( PROP_TS_FULCRUM );
    double height = aProps.getDouble( PROP_HEIGHT );
    return zoom * (tsf.yPerc() * height) / 100.;
  }

  /**
   * Возвращает параметры преобразования координат визуального элемента из его свойств.
   *
   * @param aProps {@link IOptionSet} - свойства визуального элемента
   * @return ID2Conversion - параметры преобразования 2D-координат
   */
  public static ID2Conversion getViselConversion( IOptionSet aProps ) {
    double zoom = aProps.getDouble( PROP_ZOOM );
    ID2Angle angle = aProps.getValobj( PROP_ANGLE );
    TsFulcrum tsf = aProps.getValobj( PROP_TS_FULCRUM );

    double x = aProps.getDouble( PROP_X );
    double y = aProps.getDouble( PROP_Y );
    double width = aProps.getDouble( PROP_WIDTH );
    double height = aProps.getDouble( PROP_HEIGHT );
    double dx = (tsf.xPerc() * width) / 100.;
    double dy = (tsf.yPerc() * height) / 100.;

    D2ConversionEdit d2Conversion = new D2ConversionEdit( angle, zoom, new D2Point( x - dx * zoom, y - dy * zoom ) );
    return d2Conversion;
  }

  public static ID2Point visel2Screen( double aX, double aY, IOptionSet aViselProps ) {
    // T refreshConversions( aVisel );
    ID2Conversion d2conv = getViselConversion( aViselProps );
    double rotationX = viselRotationX( aViselProps );
    double rotationY = viselRotationY( aViselProps );
    VedAffineTransform at = D2MatrixConverterNew.d2convToTransform( d2conv, rotationX, rotationY );
    return at.transform( aX, aY );
  }

  /**
   * Возвращает границы визуального элемента по его свойствам.
   *
   * @param aViselProps {@link IOptionSet} - свойства визуального элемента
   * @return {@link ID2Rectangle} - границы визуального элемента
   */
  public static ID2Rectangle viselBounds( IOptionSet aViselProps ) {
    TsFulcrum tsf = aViselProps.getValobj( PROPID_TS_FULCRUM );
    double x = aViselProps.getFloat( PROP_X );
    double y = aViselProps.getFloat( PROP_Y );
    double w = aViselProps.getFloat( PROP_WIDTH );
    double h = aViselProps.getFloat( PROP_HEIGHT );

    double dx = (tsf.xPerc() * w) / 100.;
    double dy = (tsf.yPerc() * h) / 100.;

    return new D2Rectangle( x - dx, y - dy, w, h );
  }

  /**
   * Запрет на создание экземпляров.
   */
  private VedTransformUtils() {
    // nop
  }
}
