package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class AffTransform {

  double m00;
  double m10;
  double m01;
  double m11;
  double m02;
  double m12;

  static final double ZERO = 1E-10;

  public AffTransform() {
    m00 = m11 = 1.0;
    m01 = m10 = 0.0;
    m02 = 0.0;
    m12 = 0.0;
  }

  public AffTransform( Transform aTransform ) {
    float[] k = new float[6];
    aTransform.getElements( k );
    m00 = k[0];
    m10 = k[1];
    m01 = k[2];
    m11 = k[3];
    m02 = k[4];
    m12 = k[5];
  }

  public static AffTransform createTranslated( double aX, double aY ) {
    AffTransform at = new AffTransform();
    at.m00 = at.m11 = 1.0;
    at.m01 = at.m10 = 0.0;
    at.m02 = aX;
    at.m12 = aY;
    return at;
  }

  public static AffTransform createRotated( double aRadians ) {
    AffTransform at = new AffTransform();
    double sin = Math.sin( aRadians );
    double cos = Math.cos( aRadians );
    if( Math.abs( cos ) < ZERO ) {
      cos = 0.0;
      sin = sin > 0.0 ? 1.0 : -1.0;
    }
    else
      if( Math.abs( sin ) < ZERO ) {
        sin = 0.0;
        cos = cos > 0.0 ? 1.0 : -1.0;
      }
    at.m00 = at.m11 = cos;
    at.m01 = sin;
    at.m10 = -sin;
    at.m02 = at.m12 = 0.0;
    return at;
  }

  public static AffTransform createRotated( double aRadians, double aX, double aY ) {
    AffTransform at = new AffTransform();
    double sin = Math.sin( aRadians );
    double cos = Math.cos( aRadians );
    if( Math.abs( cos ) < ZERO ) {
      cos = 0.0;
      sin = sin > 0.0 ? 1.0 : -1.0;
    }
    else
      if( Math.abs( sin ) < ZERO ) {
        sin = 0.0;
        cos = cos > 0.0 ? 1.0 : -1.0;
      }
    at.m00 = at.m11 = cos;
    at.m01 = sin;
    at.m10 = -sin;
    at.m02 = at.m12 = 0.0;

    at.m02 = aX * (1.0 - at.m00) + aX * at.m10;
    at.m12 = aY * (1.0 - at.m00) - aY * at.m10;
    return at;
  }

  public static AffTransform createScaled( double aX, double aY ) {
    AffTransform at = new AffTransform();
    at.m00 = aX;
    at.m11 = aY;
    at.m10 = at.m01 = at.m02 = at.m12 = 0.0;
    return at;
  }

  public static AffTransform multiply( AffTransform t1, AffTransform t2 ) {
    AffTransform at = new AffTransform();
    at.m00 = t1.m00 * t2.m00 + t1.m10 * t2.m01; // m00
    at.m01 = t1.m00 * t2.m10 + t1.m10 * t2.m11; // m01
    at.m10 = t1.m01 * t2.m00 + t1.m11 * t2.m01; // m10
    at.m11 = t1.m01 * t2.m10 + t1.m11 * t2.m11; // m11
    at.m02 = t1.m02 * t2.m00 + t1.m12 * t2.m01 + t2.m02; // m02
    at.m12 = t1.m02 * t2.m10 + t1.m12 * t2.m11 + t2.m12; // m12
    return at;
  }

  public AffTransform translate( double aX, double aY ) {
    return multiply( AffTransform.createTranslated( aX, aY ), this );
  }

  public AffTransform rotate( double aRadians ) {
    return multiply( AffTransform.createRotated( aRadians ), this );
  }

  public AffTransform rotate( double aRadians, double aX, double aY ) {
    return multiply( AffTransform.createRotated( aRadians, aX, aY ), this );
  }

  public AffTransform scale( double aX, double aY ) {
    return multiply( AffTransform.createScaled( aX, aY ), this );
  }

  double calcX( ID2Point aPoint ) {
    return calcX( aPoint.x(), aPoint.y() );
  }

  double calcY( ID2Point aPoint ) {
    return calcY( aPoint.x(), aPoint.y() );
  }

  double calcX( double aX, double aY ) {
    return aX * m00 + aY * m01 + m02;
  }

  double calcY( double aX, double aY ) {
    return aX * m10 + aY * m11 + m12;
  }

  /**
   * Returns the determinant of the linear transformation matrix.
   *
   * @return double - the determinant of the linear transformation matrix.
   */
  public double determinant() {
    return m00 * m11 - m01 * m10;
  }

  public AffTransform inverse() {
    double det = determinant();
    TsIllegalStateRtException.checkTrue( Math.abs( det ) < ZERO );
    AffTransform at = new AffTransform();
    at.m00 = m11 / det; // m00
    at.m10 = -m10 / det; // m10
    at.m01 = -m01 / det; // m01
    at.m11 = m00 / det; // m11
    at.m02 = (m01 * m12 - m11 * m02) / det; // m02
    at.m12 = (m10 * m02 - m00 * m12) / det; // m12
    return at;
  }

  // void print() {
  // System.out.println( String.format( "%.2f, %.2f, %.2f, %.2f, %.2f, %.2f", m00, m10, m01, m11, m02, m12 ) );
  // }

}
