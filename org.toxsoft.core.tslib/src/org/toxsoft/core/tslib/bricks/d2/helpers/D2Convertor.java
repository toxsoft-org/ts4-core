package org.toxsoft.core.tslib.bricks.d2.helpers;

import static java.lang.Math.*;
import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;

/**
 * Con
 *
 * @author hazard157
 */
public final class D2Convertor
    implements ID2Convertor, ID2Conversionable {

  private final ID2ConversionEdit d2Conv = new D2ConversionEdit();

  /**
   * Constructor.
   */
  public D2Convertor() {
    // nop
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
    // zoom from origin
    double x1 = aX * d2Conv.zoomFactor();
    double y1 = aY * d2Conv.zoomFactor();
    // rotate around origin
    double beta = d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    // change origin
    return x2 + d2Conv.origin().x();
  }

  @Override
  public double convertY( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    // zoom from origin
    double x1 = aX * d2Conv.zoomFactor();
    double y1 = aY * d2Conv.zoomFactor();
    // rotate around origin
    double beta = d2Conv.rotation().radians();
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // change origin
    return y2 + d2Conv.origin().y();
  }

  @Override
  public double reverseX( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    // reverse origin
    double x1 = aX - d2Conv.origin().x();
    double y1 = aY - d2Conv.origin().y();
    // reverse rotation (just change angle sign)
    double beta = -d2Conv.rotation().radians();
    double x2 = x1 * cos( beta ) - y1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    return x2 / d2Conv.zoomFactor();
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
  public double reverseY( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    // reverse origin
    double x1 = aX - d2Conv.origin().x();
    double y1 = aY - d2Conv.origin().y();
    // reverse rotation (just change angle sign)
    double beta = -d2Conv.rotation().radians();
    double y2 = y1 * cos( beta ) + x1 * sin( beta );
    // reverse zoom (divide rather than multiply on zoom factor)
    return y2 / d2Conv.zoomFactor();
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

}
