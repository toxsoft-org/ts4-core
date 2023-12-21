package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;

public class D2MatrixConverter {

  public static AffTransform d2convToTransform( ID2Conversion aD2Conv, double aRotX, double aRotY ) {
    AffTransform at = new AffTransform();
    at = transform( at, aD2Conv, aRotX, aRotY );
    return at;
  }

  public static AffTransform transform( AffTransform aTransform, ID2Conversion aD2Conv, double aRotX, double aRotY ) {
    AffTransform at = aTransform;
    at = at.translate( aD2Conv.origin().x(), aD2Conv.origin().y() );
    at = at.translate( aRotX, aRotY );
    at = at.rotate( aD2Conv.rotation().radians() );
    at = at.translate( -aRotX, -aRotY );
    at = at.scale( aD2Conv.zoomFactor(), aD2Conv.zoomFactor() );
    return at;
  }

  public AffTransform getItemTransfrom( Transform aScreenTransform, ID2Conversion aD2Conv, double aRotX,
      double aRotY ) {
    float[] tEl = new float[6];
    aScreenTransform.getElements( tEl );

    AffTransform at = new AffTransform( aScreenTransform );
    transform( at, aD2Conv, aRotX, aRotY );

    return at;
  }

  // public D2Matrix getItemTransfrom( ID2Conversion aD2Conv, double aRotX, double aRotY ) {
  // D2Matrix d2m = D2Matrix.translated( aD2Conv.origin().x(), aD2Conv.origin().y() );
  // d2m.translate( d2m.calcX( aRotX, aRotY ), d2m.calcY( aRotX, aRotY ) );
  // d2m.rotate( (float)aD2Conv.rotation().radians() );
  // d2m.translate( d2m.calcX( -aRotX, -aRotY ), d2m.calcY( -aRotX, -aRotY ) );
  // d2m.scale( (float)aD2Conv.zoomFactor(), (float)aD2Conv.zoomFactor() );
  // return d2m;
  // }

  // static class D2Matrix {
  //
  // double[][] elems = new double[3][3];
  //
  // D2Matrix() {
  // for( int i = 0; i < 3; i++ ) {
  // for( int j = 0; j < 3; j++ ) {
  // elems[i][j] = 0;
  // if( i == j ) {
  // elems[i][j] = 1;
  // }
  // }
  // }
  // }
  //
  // D2Matrix( double[][] aElems ) {
  // for( int i = 0; i < 3; i++ ) {
  // for( int j = 0; j < 3; j++ ) {
  // elems[i][j] = aElems[i][j];
  // }
  // }
  // }
  //
  // void translate( double aX, double aY ) {
  // elems[2][0] = aX;
  // elems[2][1] = aY;
  // }
  //
  // void rotate( double aRadians ) {
  // double angle = Math.asin( elems[0][1] ) + aRadians;
  // elems[0][0] *= Math.cos( angle );
  // elems[0][1] = Math.sin( angle );
  // elems[1][0] = -Math.sin( angle );
  // elems[1][1] *= Math.cos( angle );
  // }
  //
  // void scale( double aX, double aY ) {
  // D2Matrix m = new D2Matrix();
  // m.elems[0][0] *= aX;
  // m.elems[1][1] *= aY;
  // }
  //
  // static D2Matrix translated( double aX, double aY ) {
  // D2Matrix m = new D2Matrix();
  // m.elems[2][0] = aX;
  // m.elems[2][1] = aY;
  // return m;
  // }
  //
  // static D2Matrix rotated( double aRadians ) {
  // D2Matrix m = new D2Matrix();
  // m.elems[0][0] = Math.cos( aRadians );
  // m.elems[0][1] = Math.sin( aRadians );
  // m.elems[1][0] = -Math.sin( aRadians );
  // m.elems[1][1] = Math.cos( aRadians );
  // return m;
  // }
  //
  // static D2Matrix scaled( double aX, double aY ) {
  // D2Matrix m = new D2Matrix();
  // m.elems[0][0] = aX;
  // m.elems[1][1] = aY;
  // return m;
  // }
  //
  // public D2Matrix multiply( double[][] aElems ) {
  // double[][] newElems = new double[3][3];
  // for( var i = 0; i < 3; i++ ) {
  // for( var j = 0; j < 3; j++ ) {
  // newElems[i][j] = elems[i][j];
  // }
  // }
  // for( var i = 0; i < 2; i++ ) {
  // for( var j = 0; j < 2; j++ ) {
  // newElems[i][j] = 0;
  // for( var k = 0; k < 2; k++ ) {
  // newElems[i][j] += elems[i][k] * aElems[k][j];
  // }
  // }
  // }
  // return new D2Matrix( newElems );
  // }
  //
  // double calcX( double aX, double aY ) {
  // return aX * elems[0][0] + aY * elems[1][0] + elems[2][0];
  // }
  //
  // double calcY( double aX, double aY ) {
  // return aX * elems[0][1] + aY * elems[1][1] + elems[2][1];
  // }
  //
  // }

}
