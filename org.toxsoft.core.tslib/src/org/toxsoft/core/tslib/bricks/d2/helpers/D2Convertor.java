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

  // @Override
  // public double convertX( double aX, double aY ) {
  // checkCoor( aX );
  // checkCoor( aY );
  // // zoom from origin
  // double x1 = aX * d2Conv.zoomFactor();
  // double y1 = aY * d2Conv.zoomFactor();
  // // change origin
  // double x2 = x1 + d2Conv.origin().x();
  // double y2 = y1 + d2Conv.origin().y();
  // // rotate around pivot: shift to (0,0), rotate and bring back
  // double x3 = x2 - d2Conv.rotation().pivotPoint().x();
  // double y3 = y2 - d2Conv.rotation().pivotPoint().y();
  // double beta = d2Conv.rotation().rotationAngle().radians();
  // double x4 = x3 * cos( beta ) - y3 * sin( beta );
  // double x5 = x4 + d2Conv.rotation().pivotPoint().x();
  // return x5;
  // }

  @Override
  public double convertX( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    double zf = d2Conv.zoomFactor();
    // zoom from origin
    double x1 = aX * zf;
    double y1 = aY * zf;
    // change origin
    double x2 = x1 + d2Conv.origin().x() * zf;
    double y2 = y1 + d2Conv.origin().y() * zf;
    // rotate around pivot: shift to (0,0), rotate and bring back
    double x3 = x2 - (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    double y3 = y2 - (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().y()) * zf;
    double beta = d2Conv.rotation().rotationAngle().radians();
    double x4 = x3 * cos( beta ) - y3 * sin( beta );
    double x5 = x4 + (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    return x5;
  }

  @Override
  public double reverseX( double aX, double aY ) {
    double zf = d2Conv.zoomFactor();
    // rotate around pivot: shift to (0,0), rotate and bring back
    double x3 = aX - (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    double y3 = aY - (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().y()) * zf;
    double beta = d2Conv.rotation().rotationAngle().radians();
    double x4 = x3 * cos( -beta ) - y3 * sin( -beta );
    double x5 = x4 + (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    // change origin
    double x2 = x5 - d2Conv.origin().x() * zf;
    // zoom from origin
    double x1 = x2 / d2Conv.zoomFactor();
    return x1;
  }

  // @Override
  // public double convertY( double aX, double aY ) {
  // // zoom from origin
  // double x1 = aX * d2Conv.zoomFactor();
  // double y1 = aY * d2Conv.zoomFactor();
  // // change origin
  // double x2 = x1 + d2Conv.origin().x();
  // double y2 = y1 + d2Conv.origin().y();
  // // rotate around pivot: shift to (0,0), rotate and bring back
  // double x3 = x2 - d2Conv.rotation().pivotPoint().x();
  // double y3 = y2 - d2Conv.rotation().pivotPoint().y();
  // double beta = d2Conv.rotation().rotationAngle().radians();
  // double y4 = y3 * cos( beta ) + x3 * sin( beta );
  // double y5 = y4 + d2Conv.rotation().pivotPoint().y();
  // return y5;
  // }
  //
  // @Override
  // public double reverseY( double aX, double aY ) {
  // // rotate around pivot: shift to (0,0), rotate and bring back
  // double x3 = aX - d2Conv.rotation().pivotPoint().x();
  // double y3 = aY - d2Conv.rotation().pivotPoint().y();
  // double beta = d2Conv.rotation().rotationAngle().radians();
  // double y4 = y3 * cos( -beta ) + x3 * sin( -beta );
  // double y5 = y4 + d2Conv.rotation().pivotPoint().y();
  // // change origin
  // double y2 = y5 - d2Conv.origin().y();
  // double y1 = y2 / d2Conv.zoomFactor();
  // return y1;
  // }

  @Override
  public double convertY( double aX, double aY ) {
    double zf = d2Conv.zoomFactor();
    // zoom from origin
    double x1 = aX * zf;
    double y1 = aY * zf;
    // change origin
    double x2 = x1 + d2Conv.origin().x() * zf;
    double y2 = y1 + d2Conv.origin().y() * zf;
    // rotate around pivot: shift to (0,0), rotate and bring back
    double x3 = x2 - (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    double y3 = y2 - (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().x()) * zf;
    double beta = d2Conv.rotation().rotationAngle().radians();
    double y4 = y3 * cos( beta ) + x3 * sin( beta );
    double y5 = y4 + (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().x()) * zf;
    return y5;
  }

  @Override
  public double reverseY( double aX, double aY ) {
    double zf = d2Conv.zoomFactor();
    // rotate around pivot: shift to (0,0), rotate and bring back
    double x3 = aX - (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    double y3 = aY - (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().x()) * zf;
    double beta = d2Conv.rotation().rotationAngle().radians();
    double y4 = y3 * cos( -beta ) + x3 * sin( -beta );
    double y5 = y4 + (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().x()) * zf;
    // change origin
    double y2 = y5 - d2Conv.origin().y() * zf;
    double y1 = y2 / d2Conv.zoomFactor();
    return y1;
  }

  // @Override
  // public ID2Point convertPoint( double aX, double aY ) {
  // // zoom from origin
  // double x1 = aX * d2Conv.zoomFactor();
  // double y1 = aY * d2Conv.zoomFactor();
  // // change origin
  // double x2 = x1 + d2Conv.origin().x();
  // double y2 = y1 + d2Conv.origin().y();
  // // rotate around pivot: shift to (0,0), rotate and bring back
  // double x3 = x2 - d2Conv.rotation().pivotPoint().x();
  // double y3 = y2 - d2Conv.rotation().pivotPoint().y();
  // double beta = d2Conv.rotation().rotationAngle().radians();
  // double x4 = x3 * cos( beta ) - y3 * sin( beta );
  // double y4 = y3 * cos( beta ) + x3 * sin( beta );
  // double x5 = x4 + d2Conv.rotation().pivotPoint().x();
  // double y5 = y4 + d2Conv.rotation().pivotPoint().y();
  // return new D2Point( x5, y5 );
  // }

  @Override
  public ID2Point convertPoint( double aX, double aY ) {
    double zf = d2Conv.zoomFactor();
    // zoom from origin
    double x1 = aX * d2Conv.zoomFactor() * zf;
    double y1 = aY * d2Conv.zoomFactor() * zf;
    // change origin
    double x2 = x1 + d2Conv.origin().x() * zf;
    double y2 = y1 + d2Conv.origin().y() * zf;
    // rotate around pivot: shift to (0,0), rotate and bring back
    double x3 = x2 - (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    double y3 = y2 - (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().y()) * zf;
    double beta = d2Conv.rotation().rotationAngle().radians();
    double x4 = x3 * cos( beta ) - y3 * sin( beta );
    double y4 = y3 * cos( beta ) + x3 * sin( beta );
    double x5 = x4 + (d2Conv.rotation().pivotPoint().x() + d2Conv.origin().x()) * zf;
    double y5 = y4 + (d2Conv.rotation().pivotPoint().y() + d2Conv.origin().y()) * zf;
    return new D2Point( x5, y5 );
  }

  @Override
  public ID2Point reversePoint( double aX, double aY ) {
    double x = reverseX( aX, aY );
    double y = reverseY( aX, aY );
    return new D2Point( x, y );
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
    int minx = (int)(min( x1, min( x2, min( x3, x4 ) ) ));
    int maxx = (int)(max( x1, max( x2, max( x3, x4 ) ) ));
    int miny = (int)(min( y1, min( y2, min( y3, y4 ) ) ));
    int maxy = (int)(max( y1, max( y2, max( y3, y4 ) ) ));
    return new TsRectangle( minx, miny, maxx - minx + 1, maxy - miny + 1 );
  }

}
