package org.toxsoft.core.tslib.bricks.d2.helpers;

import static java.lang.Math.*;
import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Conversion} implementation.
 *
 * @author hazard157
 */
public class D2Convertor
    implements ID2Convertor, ID2Conversionable {

  private final ID2ConversionEdit d2Conv = new D2ConversionEdit();

  /**
   * Constructor.
   */
  public D2Convertor() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aConversion {@link ID2Conversion} - initial converion settings
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Convertor( ID2Conversion aConversion ) {
    d2Conv.setConversion( aConversion );
  }

  // ------------------------------------------------------------------------------------
  // ID2Conversionable
  //

  @Override
  public ID2Conversion getConversion() {
    return d2Conv;
  }

  @Override
  public void setConversion( ID2Conversion aConversion ) {
    d2Conv.setConversion( aConversion );
  }

  // ------------------------------------------------------------------------------------
  // ID2Convertor
  //

  @Override
  public double convertX( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );

    // Аналог Transform.translate
    double x1 = aX - d2Conv.origin().x();// x координата вектора радиуса поворота
    double y1 = aY - d2Conv.origin().y();// y координата вектора радиуса поворота
    // rotate around origin
    double beta = d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    // zoom
    // точка поворота остается на месте, а расстояние до искомой масштабируется
    return d2Conv.origin().x() + x2 * d2Conv.zoomFactor();
  }

  @Override
  public double reverseX( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    // reverse origin
    double x1 = aX - d2Conv.origin().x();
    double y1 = aY - d2Conv.origin().y();
    // double x1 = (aX - d2Conv.origin().x()) / d2Conv.zoomFactor();
    // double y1 = (aY - d2Conv.origin().y()) / d2Conv.zoomFactor();
    // reverse rotation (just change angle sign)

    x1 = x1 / d2Conv.zoomFactor();
    y1 = y1 / d2Conv.zoomFactor();

    double beta = -d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    // return d2Conv.origin().x() + x2 / d2Conv.zoomFactor();
    return d2Conv.origin().x() + x2;
    // return x2;
  }

  @Override
  public double convertY( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    double x1 = aX - d2Conv.origin().x();// x координата вектора радиуса поворота
    double y1 = aY - d2Conv.origin().y();// y координата вектора радиуса поворота
    // rotate around origin
    double beta = d2Conv.rotation().radians();
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // zoom
    // точка поворота остается на месте, а расстояние до искомой масштабируется
    return d2Conv.origin().y() + y2 * d2Conv.zoomFactor();
  }

  @Override
  public double reverseY( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    // reverse origin
    double x1 = aX - d2Conv.origin().x();
    double y1 = aY - d2Conv.origin().y();

    x1 = x1 / d2Conv.zoomFactor();
    y1 = y1 / d2Conv.zoomFactor();

    // reverse rotation (just change angle sign)
    double beta = -d2Conv.rotation().radians();
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    // return d2Conv.origin().y() + y2 / d2Conv.zoomFactor();
    return d2Conv.origin().y() + y2;
  }

  @Override
  public ID2Point convertPoint( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    // zoom from origin
    double x1 = aX * d2Conv.zoomFactor();
    double y1 = aY * d2Conv.zoomFactor();
    // rotate around origin
    double beta = d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // change origin
    double x3 = x2 + d2Conv.origin().x();
    double y3 = y2 + d2Conv.origin().y();
    return new D2Point( x3, y3 );
  }

  @Override
  public ID2Point reversePoint( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    // reverse origin
    double x1 = aX - d2Conv.origin().x();
    double y1 = aY - d2Conv.origin().y();
    // reverse rotation (just change angle sign)
    double beta = -d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    double x3 = x2 / d2Conv.zoomFactor();
    double y3 = y2 / d2Conv.zoomFactor();
    return new D2Point( x3, y3 );
  }

  @Override
  public ID2Size convertSize( double aWidth, double aHeight ) {
    checkLength( aWidth );
    checkLength( aHeight );
    // calculate like point coordinates from (0,0) origin
    // zoom from origin (0,0)
    double x1 = aWidth * d2Conv.zoomFactor();
    double y1 = aHeight * d2Conv.zoomFactor();
    // rotate around origin
    double beta = d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    return new D2Size( Math.abs( x2 ), Math.abs( y2 ) );
  }

  @Override
  public ID2Size reverseSize( double aWidth, double aHeight ) {
    checkLength( aWidth );
    checkLength( aHeight );
    // calculate like point coordinates from (0,0) origin
    double x1 = aWidth;
    double y1 = aHeight;
    // reverse rotation (just change angle sign)
    double beta = -d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    double x3 = x2 / d2Conv.zoomFactor();
    double y3 = y2 / d2Conv.zoomFactor();
    return new D2Size( Math.abs( x3 ), Math.abs( y3 ) );
  }

  @Override
  public ITsRectangle rectBounds( ID2Rectangle aRect ) {
    double x1 = convertX( aRect.a() );
    double x2 = convertX( aRect.a().x(), aRect.b().y() );
    double x3 = convertX( aRect.b() );
    double x4 = convertX( aRect.b().x(), aRect.a().y() );
    double y1 = convertY( aRect.a() );
    double y2 = convertY( aRect.a().x(), aRect.b().y() );
    double y3 = convertY( aRect.b() );
    double y4 = convertY( aRect.b().x(), aRect.a().y() );
    int minx = (int)floor( (min( x1, min( x2, min( x3, x4 ) ) )) );
    int maxx = (int)ceil( (max( x1, max( x2, max( x3, x4 ) ) )) );
    int miny = (int)floor( (min( y1, min( y2, min( y3, y4 ) ) )) );
    int maxy = (int)ceil( (max( y1, max( y2, max( y3, y4 ) ) )) );
    return new TsRectangle( minx, miny, maxx - minx + 1, maxy - miny + 1 );
  }

  // ------------------------------------------------------------------------------------
  // Tmp
  //

  @Override
  public double reverseItemX( double aX, double aY, double aItemX, double aItemY ) {
    checkCoor( aX );
    checkCoor( aY );
    // reverse origin
    double x1 = aX - d2Conv.origin().x() - aItemX;
    double y1 = aY - d2Conv.origin().y() - aItemY;
    double beta = -d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    return d2Conv.origin().x() + aItemX + x2 / d2Conv.zoomFactor();
  }

  @Override
  public double reverseItemY( double aX, double aY, double aItemX, double aItemY ) {
    checkCoor( aX );
    checkCoor( aY );
    // reverse origin
    double x1 = aX - d2Conv.origin().x() - aItemX;
    double y1 = aY - d2Conv.origin().y() - aItemY;
    // reverse rotation (just change angle sign)
    double beta = -d2Conv.rotation().radians();
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    return d2Conv.origin().y() + aItemY + y2 / d2Conv.zoomFactor();
  }

}
