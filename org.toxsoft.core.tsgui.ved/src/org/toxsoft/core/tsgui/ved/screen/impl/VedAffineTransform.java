package org.toxsoft.core.tsgui.ved.screen.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Аффинное преобразование координат экрана редактирования.
 * <p>
 *
 * @author vs
 */
public class VedAffineTransform
    implements Serializable {

  /**
   * The Constant serialVersionUID.
   */
  private static final long serialVersionUID = 1330973210523860834L;

  /**
   * The Constant TYPE_IDENTITY.
   */
  public static final int TYPE_IDENTITY          = 0;
  /**
   * The Constant TYPE_TRANSLATION.
   */
  public static final int TYPE_TRANSLATION       = 1;
  /**
   * The Constant TYPE_UNIFORM_SCALE.
   */
  public static final int TYPE_UNIFORM_SCALE     = 2;
  /**
   * The Constant TYPE_GENERAL_SCALE.
   */
  public static final int TYPE_GENERAL_SCALE     = 4;
  /**
   * The Constant TYPE_QUADRANT_ROTATION.
   */
  public static final int TYPE_QUADRANT_ROTATION = 8;
  /**
   * The Constant TYPE_GENERAL_ROTATION.
   */
  public static final int TYPE_GENERAL_ROTATION  = 16;
  /**
   * The Constant TYPE_GENERAL_TRANSFORM.
   */
  public static final int TYPE_GENERAL_TRANSFORM = 32;
  /**
   * The Constant TYPE_FLIP.
   */
  public static final int TYPE_FLIP              = 64;
  /**
   * The Constant TYPE_MASK_SCALE.
   */
  public static final int TYPE_MASK_SCALE        = TYPE_UNIFORM_SCALE | TYPE_GENERAL_SCALE;
  /**
   * The Constant TYPE_MASK_ROTATION.
   */
  public static final int TYPE_MASK_ROTATION     = TYPE_QUADRANT_ROTATION | TYPE_GENERAL_ROTATION;
  /**
   * The <code>TYPE_UNKNOWN</code> is an initial type value.
   */
  static final int        TYPE_UNKNOWN           = -1;
  /**
   * The min value equivalent to zero. If absolute value less then ZERO it considered as zero.
   */
  static final double     ZERO                   = 1E-10;
  /**
   * The values of transformation matrix.
   */
  double                  m00;
  /**
   * The m10.
   */
  double                  m10;
  /**
   * The m01.
   */
  double                  m01;
  /**
   * The m11.
   */
  double                  m11;
  /**
   * The m02.
   */
  double                  m02;
  /**
   * The m12.
   */
  double                  m12;
  /**
   * The transformation <code>type</code>.
   */
  transient int           type;

  /**
   * Instantiates a new affine transform of type <code>TYPE_IDENTITY</code> (which leaves coordinates unchanged).
   */
  public VedAffineTransform() {
    type = TYPE_IDENTITY;
    m00 = m11 = 1.0;
    m10 = m01 = m02 = m12 = 0.0;
  }

  /**
   * Instantiates a new affine transform that has the same data as the given VedAffineTransform.
   *
   * @param t the transform to copy.
   */
  public VedAffineTransform( VedAffineTransform t ) {
    this.type = t.type;
    this.m00 = t.m00;
    this.m10 = t.m10;
    this.m01 = t.m01;
    this.m11 = t.m11;
    this.m02 = t.m02;
    this.m12 = t.m12;
  }

  /**
   * Instantiates a new affine transform by specifying the values of the 2x3 transformation matrix as floats. The type
   * is set to the default type: <code>TYPE_UNKNOWN</code>
   *
   * @param aM00 the m00 entry in the transformation matrix.
   * @param aM10 the m10 entry in the transformation matrix.
   * @param aM01 the m01 entry in the transformation matrix.
   * @param aMm11 the m11 entry in the transformation matrix.
   * @param aM02 the m02 entry in the transformation matrix.
   * @param aM12 the m12 entry in the transformation matrix.
   */
  public VedAffineTransform( float aM00, float aM10, float aM01, float aMm11, float aM02, float aM12 ) {
    this.type = TYPE_UNKNOWN;
    this.m00 = aM00;
    this.m10 = aM10;
    this.m01 = aM01;
    this.m11 = aMm11;
    this.m02 = aM02;
    this.m12 = aM12;
  }

  /**
   * Instantiates a new affine transform by specifying the values of the 2x3 transformation matrix as doubles. The type
   * is set to the default type: <code>TYPE_UNKNOWN</code>
   *
   * @param aM00 the m00 entry in the transformation matrix.
   * @param aM10 the m10 entry in the transformation matrix.
   * @param aM01 the m01 entry in the transformation matrix.
   * @param aM11 the m11 entry in the transformation matrix.
   * @param aM02 the m02 entry in the transformation matrix.
   * @param aM12 the m12 entry in the transformation matrix.
   */
  public VedAffineTransform( double aM00, double aM10, double aM01, double aM11, double aM02, double aM12 ) {
    this.type = TYPE_UNKNOWN;
    this.m00 = aM00;
    this.m10 = aM10;
    this.m01 = aM01;
    this.m11 = aM11;
    this.m02 = aM02;
    this.m12 = aM12;
  }

  /**
   * Instantiates a new affine transform by reading the values of the transformation matrix from an array of floats. The
   * mapping from the array to the matrix starts with <code>matrix[0]</code> giving the top-left entry of the matrix and
   * proceeds with the usual left-to-right and top-down ordering.
   * <p>
   * If the array has only four entries, then the two entries of the last row of the transformation matrix default to
   * zero.
   *
   * @param matrix the array of four or six floats giving the values of the matrix.
   * @throws ArrayIndexOutOfBoundsException if the size of the array is 0, 1, 2, 3, or 5.
   */
  public VedAffineTransform( float[] matrix ) {
    this.type = TYPE_UNKNOWN;
    m00 = matrix[0];
    m10 = matrix[1];
    m01 = matrix[2];
    m11 = matrix[3];
    if( matrix.length > 4 ) {
      m02 = matrix[4];
      m12 = matrix[5];
    }
  }

  /**
   * Instantiates a new affine transform by reading the values of the transformation matrix from an array of doubles.
   * The mapping from the array to the matrix starts with <code>matrix[0]</code> giving the top-left entry of the matrix
   * and proceeds with the usual left-to-right and top-down ordering.
   * <p>
   * If the array has only four entries, then the two entries of the last row of the transformation matrix default to
   * zero.
   *
   * @param matrix the array of four or six doubles giving the values of the matrix.
   * @throws ArrayIndexOutOfBoundsException if the size of the array is 0, 1, 2, 3, or 5.
   */
  public VedAffineTransform( double[] matrix ) {
    this.type = TYPE_UNKNOWN;
    m00 = matrix[0];
    m10 = matrix[1];
    m01 = matrix[2];
    m11 = matrix[3];
    if( matrix.length > 4 ) {
      m02 = matrix[4];
      m12 = matrix[5];
    }
  }

  /**
   * Returns type of the affine transformation.
   * <p>
   * The type is computed as follows: Label the entries of the transformation matrix as three rows (m00, m01), (m10,
   * m11), and (m02, m12). Then if the original basis vectors are (1, 0) and (0, 1), the new basis vectors after
   * transformation are given by (m00, m01) and (m10, m11), and the translation vector is (m02, m12).
   * <p>
   * The types are classified as follows: <br/>
   * TYPE_IDENTITY - no change<br/>
   * TYPE_TRANSLATION - The translation vector isn't zero<br/>
   * TYPE_UNIFORM_SCALE - The new basis vectors have equal length<br/>
   * TYPE_GENERAL_SCALE - The new basis vectors dont' have equal length<br/>
   * TYPE_FLIP - The new basis vector orientation differs from the original one<br/>
   * TYPE_QUADRANT_ROTATION - The new basis is a rotation of the original by 90, 180, 270, or 360 degrees<br/>
   * TYPE_GENERAL_ROTATION - The new basis is a rotation of the original by an arbitrary angle<br/>
   * TYPE_GENERAL_TRANSFORM - The transformation can't be inverted.<br/>
   * <p>
   * Note that multiple types are possible, thus the types can be combined using bitwise combinations.
   *
   * @return the type of the Affine Transform.
   */
  public int getType() {
    if( type != TYPE_UNKNOWN ) {
      return type;
    }
    int newType = 0;
    if( m00 * m01 + m10 * m11 != 0.0 ) {
      newType |= TYPE_GENERAL_TRANSFORM;
      return newType;
    }
    if( m02 != 0.0 || m12 != 0.0 ) {
      newType |= TYPE_TRANSLATION;
    }
    else
      if( m00 == 1.0 && m11 == 1.0 && m01 == 0.0 && m10 == 0.0 ) {
        newType = TYPE_IDENTITY;
        return newType;
      }
    if( m00 * m11 - m01 * m10 < 0.0 ) {
      newType |= TYPE_FLIP;
    }
    double dx = m00 * m00 + m10 * m10;
    double dy = m01 * m01 + m11 * m11;
    if( dx != dy ) {
      newType |= TYPE_GENERAL_SCALE;
    }
    else
      if( dx != 1.0 ) {
        newType |= TYPE_UNIFORM_SCALE;
      }
    if( (m00 == 0.0 && m11 == 0.0) || (m10 == 0.0 && m01 == 0.0 && (m00 < 0.0 || m11 < 0.0)) ) {
      newType |= TYPE_QUADRANT_ROTATION;
    }
    else
      if( m01 != 0.0 || m10 != 0.0 ) {
        newType |= TYPE_GENERAL_ROTATION;
      }
    return newType;
  }

  /**
   * Gets the scale x entry of the transformation matrix (the upper left matrix entry).
   *
   * @return the scale x value.
   */
  public double getScaleX() {
    return m00;
  }

  /**
   * Gets the scale y entry of the transformation matrix (the lower right entry of the linear transformation).
   *
   * @return the scale y value.
   */
  public double getScaleY() {
    return m11;
  }

  /**
   * Gets the shear x entry of the transformation matrix (the upper right entry of the linear transformation).
   *
   * @return the shear x value.
   */
  public double getShearX() {
    return m01;
  }

  /**
   * Gets the shear y entry of the transformation matrix (the lower left entry of the linear transformation).
   *
   * @return the shear y value.
   */
  public double getShearY() {
    return m10;
  }

  /**
   * Gets the x coordinate of the translation vector.
   *
   * @return the x coordinate of the translation vector.
   */
  public double getTranslateX() {
    return m02;
  }

  /**
   * Gets the y coordinate of the translation vector.
   *
   * @return the y coordinate of the translation vector.
   */
  public double getTranslateY() {
    return m12;
  }

  /**
   * Checks if the AffineTransformation is the identity.
   *
   * @return true, if the AffineTransformation is the identity.
   */
  public boolean isIdentity() {
    return getType() == TYPE_IDENTITY;
  }

  /**
   * Writes the values of the transformation matrix into the given array of doubles. If the array has length 4, only the
   * linear transformation part will be written into it. If it has length greater than 4, the translation vector will be
   * included as well.
   *
   * @param matrix the array to fill with the values of the matrix.
   * @throws ArrayIndexOutOfBoundsException if the size of the array is 0, 1, 2, 3, or 5.
   */
  public void getMatrix( double[] matrix ) {
    matrix[0] = m00;
    matrix[1] = m10;
    matrix[2] = m01;
    matrix[3] = m11;
    if( matrix.length > 4 ) {
      matrix[4] = m02;
      matrix[5] = m12;
    }
  }

  /**
   * Gets the determinant of the linear transformation matrix.
   *
   * @return the determinant of the linear transformation matrix.
   */
  public double getDeterminant() {
    return m00 * m11 - m01 * m10;
  }

  /**
   * Sets the transform in terms of a list of double values.
   *
   * @param aM0 the m00 coordinate of the transformation matrix.
   * @param aM10 the m10 coordinate of the transformation matrix.
   * @param aM01 the m01 coordinate of the transformation matrix.
   * @param aM11 the m11 coordinate of the transformation matrix.
   * @param aM02 the m02 coordinate of the transformation matrix.
   * @param aM12 the m12 coordinate of the transformation matrix.
   */
  public void setTransform( double aM0, double aM10, double aM01, double aM11, double aM02, double aM12 ) {
    this.type = TYPE_UNKNOWN;
    this.m00 = aM0;
    this.m10 = aM10;
    this.m01 = aM01;
    this.m11 = aM11;
    this.m02 = aM02;
    this.m12 = aM12;
  }

  /**
   * Sets the transform's data to match the data of the transform sent as a parameter.
   *
   * @param t the transform that gives the new values.
   */
  public void setTransform( VedAffineTransform t ) {
    type = t.type;
    setTransform( t.m00, t.m10, t.m01, t.m11, t.m02, t.m12 );
  }

  /**
   * Sets the transform to the identity transform.
   */
  public void setToIdentity() {
    type = TYPE_IDENTITY;
    m00 = m11 = 1.0;
    m10 = m01 = m02 = m12 = 0.0;
  }

  /**
   * Sets the transformation to a translation alone. Sets the linear part of the transformation to identity and the
   * translation vector to the values sent as parameters. Sets the type to <code>TYPE_IDENTITY</code> if the resulting
   * AffineTransformation is the identity transformation, otherwise sets it to <code>TYPE_TRANSLATION</code>.
   *
   * @param mx the distance to translate in the x direction.
   * @param my the distance to translate in the y direction.
   */
  public void setToTranslation( double mx, double my ) {
    m00 = m11 = 1.0;
    m01 = m10 = 0.0;
    m02 = mx;
    m12 = my;
    if( mx == 0.0 && my == 0.0 ) {
      type = TYPE_IDENTITY;
    }
    else {
      type = TYPE_TRANSLATION;
    }
  }

  /**
   * Sets the transformation to being a scale alone, eliminating rotation, shear, and translation elements. Sets the
   * type to <code>TYPE_IDENTITY</code> if the resulting AffineTransformation is the identity transformation, otherwise
   * sets it to <code>TYPE_UNKNOWN</code>.
   *
   * @param scx the scaling factor in the x direction.
   * @param scy the scaling factor in the y direction.
   */
  public void setToScale( double scx, double scy ) {
    m00 = scx;
    m11 = scy;
    m10 = m01 = m02 = m12 = 0.0;
    if( scx != 1.0 || scy != 1.0 ) {
      type = TYPE_UNKNOWN;
    }
    else {
      type = TYPE_IDENTITY;
    }
  }

  /**
   * Sets the transformation to being a shear alone, eliminating rotation, scaling, and translation elements. Sets the
   * type to <code>TYPE_IDENTITY</code> if the resulting AffineTransformation is the identity transformation, otherwise
   * sets it to <code>TYPE_UNKNOWN</code>.
   *
   * @param shx the shearing factor in the x direction.
   * @param shy the shearing factor in the y direction.
   */
  public void setToShear( double shx, double shy ) {
    m00 = m11 = 1.0;
    m02 = m12 = 0.0;
    m01 = shx;
    m10 = shy;
    if( shx != 0.0 || shy != 0.0 ) {
      type = TYPE_UNKNOWN;
    }
    else {
      type = TYPE_IDENTITY;
    }
  }

  /**
   * Sets the transformation to being a rotation alone, eliminating shearing, scaling, and translation elements. Sets
   * the type to <code>TYPE_IDENTITY</code> if the resulting AffineTransformation is the identity transformation,
   * otherwise sets it to <code>TYPE_UNKNOWN</code>.
   *
   * @param angle the angle of rotation in radians.
   */
  public void setToRotation( double angle ) {
    double sin = Math.sin( angle );
    double cos = Math.cos( angle );
    if( Math.abs( cos ) < ZERO ) {
      cos = 0.0;
      sin = sin > 0.0 ? 1.0 : -1.0;
    }
    else
      if( Math.abs( sin ) < ZERO ) {
        sin = 0.0;
        cos = cos > 0.0 ? 1.0 : -1.0;
      }
    m00 = m11 = cos;
    m01 = -sin;
    m10 = sin;
    m02 = m12 = 0.0;
    type = TYPE_UNKNOWN;
  }

  /**
   * Sets the transformation to being a rotation followed by a translation. Sets the type to <code>TYPE_UNKNOWN</code>.
   *
   * @param angle the angle of rotation in radians.
   * @param px the distance to translate in the x direction.
   * @param py the distance to translate in the y direction.
   */
  public void setToRotation( double angle, double px, double py ) {
    setToRotation( angle );
    m02 = px * (1.0 - m00) + py * m10;
    m12 = py * (1.0 - m00) - px * m10;
    type = TYPE_UNKNOWN;
  }

  /**
   * Creates a new AffineTransformation that is a translation alone with the translation vector given by the values sent
   * as parameters. The new transformation's type is <code>TYPE_IDENTITY</code> if the AffineTransformation is the
   * identity transformation, otherwise it's <code>TYPE_TRANSLATION</code>.
   *
   * @param mx the distance to translate in the x direction.
   * @param my the distance to translate in the y direction.
   * @return the new AffineTransformation.
   */
  public static VedAffineTransform getTranslateInstance( double mx, double my ) {
    VedAffineTransform t = new VedAffineTransform();
    t.setToTranslation( mx, my );
    return t;
  }

  /**
   * Creates a new AffineTransformation that is a scale alone. The new transformation's type is
   * <code>TYPE_IDENTITY</code> if the AffineTransformation is the identity transformation, otherwise it's
   * <code>TYPE_UNKNOWN</code>.
   *
   * @param scx the scaling factor in the x direction.
   * @param scY the scaling factor in the y direction.
   * @return the new AffineTransformation.
   */
  public static VedAffineTransform getScaleInstance( double scx, double scY ) {
    VedAffineTransform t = new VedAffineTransform();
    t.setToScale( scx, scY );
    return t;
  }

  /**
   * Creates a new AffineTransformation that is a shear alone. The new transformation's type is
   * <code>TYPE_IDENTITY</code> if the AffineTransformation is the identity transformation, otherwise it's
   * <code>TYPE_UNKNOWN</code>.
   *
   * @param shx the shearing factor in the x direction.
   * @param shy the shearing factor in the y direction.
   * @return the new AffineTransformation.
   */
  public static VedAffineTransform getShearInstance( double shx, double shy ) {
    VedAffineTransform m = new VedAffineTransform();
    m.setToShear( shx, shy );
    return m;
  }

  /**
   * Creates a new AffineTransformation that is a rotation alone. The new transformation's type is
   * <code>TYPE_IDENTITY</code> if the AffineTransformation is the identity transformation, otherwise it's
   * <code>TYPE_UNKNOWN</code>.
   *
   * @param angle the angle of rotation in radians.
   * @return the new AffineTransformation.
   */
  public static VedAffineTransform getRotateInstance( double angle ) {
    VedAffineTransform t = new VedAffineTransform();
    t.setToRotation( angle );
    return t;
  }

  /**
   * Creates a new AffineTransformation that is a rotation followed by a translation. Sets the type to
   * <code>TYPE_UNKNOWN</code>.
   *
   * @param angle the angle of rotation in radians.
   * @param x the distance to translate in the x direction.
   * @param y the distance to translate in the y direction.
   * @return the new AffineTransformation.
   */
  public static VedAffineTransform getRotateInstance( double angle, double x, double y ) {
    VedAffineTransform t = new VedAffineTransform();
    t.setToRotation( angle, x, y );
    return t;
  }

  /**
   * Applies a translation to this AffineTransformation.
   *
   * @param mx the distance to translate in the x direction.
   * @param my the distance to translate in the y direction.
   */
  public void translate( double mx, double my ) {
    concatenate( VedAffineTransform.getTranslateInstance( mx, my ) );
  }

  /**
   * Applies a scaling transformation to this AffineTransformation.
   *
   * @param scx the scaling factor in the x direction.
   * @param scy the scaling factor in the y direction.
   */
  public void scale( double scx, double scy ) {
    concatenate( VedAffineTransform.getScaleInstance( scx, scy ) );
  }

  /**
   * Applies a shearing transformation to this AffineTransformation.
   *
   * @param shx the shearing factor in the x direction.
   * @param shy the shearing factor in the y direction.
   */
  public void shear( double shx, double shy ) {
    concatenate( VedAffineTransform.getShearInstance( shx, shy ) );
  }

  /**
   * Applies a rotation transformation to this AffineTransformation.
   *
   * @param angle the angle of rotation in radians.
   */
  public void rotate( double angle ) {
    concatenate( VedAffineTransform.getRotateInstance( angle ) );
  }

  /**
   * Applies a rotation and translation transformation to this AffineTransformation.
   *
   * @param angle the angle of rotation in radians.
   * @param px the distance to translate in the x direction.
   * @param py the distance to translate in the y direction.
   */
  public void rotate( double angle, double px, double py ) {
    concatenate( VedAffineTransform.getRotateInstance( angle, px, py ) );
  }

  /**
   * Multiplies the matrix representations of two VedAffineTransform objects.
   *
   * @param t1 - the VedAffineTransform object is a multiplicand
   * @param t2 - the VedAffineTransform object is a multiplier
   * @return an VedAffineTransform object that is the result of t1 multiplied by the matrix t2.
   */
  VedAffineTransform multiply( VedAffineTransform t1, VedAffineTransform t2 ) {
    return new VedAffineTransform( t1.m00 * t2.m00 + t1.m10 * t2.m01, // m00
        t1.m00 * t2.m10 + t1.m10 * t2.m11, // m01
        t1.m01 * t2.m00 + t1.m11 * t2.m01, // m10
        t1.m01 * t2.m10 + t1.m11 * t2.m11, // m11
        t1.m02 * t2.m00 + t1.m12 * t2.m01 + t2.m02, // m02
        t1.m02 * t2.m10 + t1.m12 * t2.m11 + t2.m12 );// m12
  }

  /**
   * Applies the given VedAffineTransform to this VedAffineTransform via matrix multiplication.
   *
   * @param t the VedAffineTransform to apply to this VedAffineTransform.
   */
  public void concatenate( VedAffineTransform t ) {
    setTransform( multiply( t, this ) );
  }

  /**
   * Changes the current VedAffineTransform the one obtained by taking the transform t and applying this
   * VedAffineTransform to it.
   *
   * @param t the VedAffineTransform that this VedAffineTransform is multiplied by.
   */
  public void preConcatenate( VedAffineTransform t ) {
    setTransform( multiply( this, t ) );
  }

  /**
   * Creates an VedAffineTransform that is the inverse of this transform.
   *
   * @return the affine transform that is the inverse of this VedAffineTransform.
   * @throws TsIllegalArgumentRtException if this VedAffineTransform cannot be inverted (the determinant of the linear
   *           transformation part is zero).
   */
  public VedAffineTransform createInverse() {
    double det = getDeterminant();
    if( Math.abs( det ) < ZERO ) {
      throw new TsIllegalArgumentRtException( "Determinant is zero" ); //$NON-NLS-1$
    }
    return new VedAffineTransform( m11 / det, // m00
        -m10 / det, // m10
        -m01 / det, // m01
        m00 / det, // m11
        (m01 * m12 - m11 * m02) / det, // m02
        (m10 * m02 - m00 * m12) / det // m12
    );
  }

  /**
   * Apply the current VedAffineTransform to the point.
   *
   * @param aSrc the original point.
   * @return the point in the VedAffineTransform's image space where the original point is sent.
   */
  public ID2Point transform( ID2Point aSrc ) {
    double x = aSrc.x();
    double y = aSrc.y();
    return new D2Point( x * m00 + y * m01 + m02, x * m10 + y * m11 + m12 );
  }

  /**
   * Apply the current VedAffineTransform to the point.
   *
   * @param aX double - X coordinate of the point
   * @param aY double - Y coordinate of the point
   * @return ID2Point - transformed point
   */
  public ID2Point transform( double aX, double aY ) {
    return new D2Point( aX * m00 + aY * m01 + m02, aX * m10 + aY * m11 + m12 );
  }

  /**
   * Transforms the point according to the linear transformation part of this AffineTransformation (without applying the
   * translation).
   *
   * @param src the original point.
   * @param dst the point object where the result of the delta transform is written.
   * @return the result of applying the delta transform (linear part only) to the original point.
   */
  public ID2Point deltaTransform( ID2Point src, ID2Point dst ) {
    double x = src.x();
    double y = src.y();
    return new D2Point( x * m00 + y * m01, x * m10 + y * m11 );
  }

  /**
   * Applies the linear transformation part of this VedAffineTransform (ignoring the translation part) to a set of
   * points given as an array of double values where every two values in the array give the coordinates of a point; the
   * even-indexed values giving the x coordinates and the odd-indexed values giving the y coordinates.
   *
   * @param src the array of points to be transformed.
   * @param srcOff the offset in the source point array of the first point to be transformed.
   * @param dst the point array where the images of the points (after applying the delta transformation) should be
   *          placed.
   * @param dstOff the offset in the destination array where the new values should be written.
   * @param aLength the number of points to transform.
   * @throws ArrayIndexOutOfBoundsException if <code>srcOff + length*2 > src.length</code> or
   *           <code>dstOff + length*2 > dst.length</code>.
   */
  public void deltaTransform( double[] src, int srcOff, double[] dst, int dstOff, int aLength ) {
    int srcIdx = srcOff;
    int dstIdx = dstOff;
    int length = aLength;
    while( --length >= 0 ) {
      double x = src[srcIdx++];
      double y = src[srcIdx++];
      dst[dstIdx++] = x * m00 + y * m01;
      dst[dstIdx++] = x * m10 + y * m11;
    }
  }

  /**
   * Transforms the point according to the inverse of this AffineTransformation.
   *
   * @param src the original point.
   * @return the result of applying the inverse transform. Inverse transform.
   * @throws TsIllegalStateRtException if this VedAffineTransform cannot be inverted (the determinant of the linear
   *           transformation part is zero).
   */
  public ID2Point inverseTransform( ID2Point src )
      throws TsIllegalStateRtException {
    double det = getDeterminant();
    if( Math.abs( det ) < ZERO ) {
      throw new TsIllegalStateRtException( "Determinant is zero" ); //$NON-NLS-1$
    }
    double x = src.x() - m02;
    double y = src.y() - m12;
    return new D2Point( (x * m11 - y * m01) / det, (y * m00 - x * m10) / det );
  }

  /**
   * Transforms the point according to the inverse of this AffineTransformation.
   *
   * @param aX double - X coordinate of the point
   * @param aY double - Y coordinate of the point
   * @return the result of applying the inverse transform. Inverse transform.
   * @throws TsIllegalStateRtException if this VedAffineTransform cannot be inverted (the determinant of the linear
   *           transformation part is zero).
   */
  public ID2Point inverseTransform( double aX, double aY )
      throws TsIllegalStateRtException {
    double det = getDeterminant();
    if( Math.abs( det ) < ZERO ) {
      throw new TsIllegalStateRtException( "Determinant is zero" ); //$NON-NLS-1$
    }
    double x = aX - m02;
    double y = aY - m12;
    return new D2Point( (x * m11 - y * m01) / det, (y * m00 - x * m10) / det );
  }

  @Override
  public String toString() {
    return getClass().getName() + "[[" + m00 + ", " + m01 + ", " + m02 + "], [" //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
        + m10 + ", " + m11 + ", " + m12 + "]]"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + (int)m00;
    result = TsLibUtils.PRIME * result + (int)m01;
    result = TsLibUtils.PRIME * result + (int)m02;
    result = TsLibUtils.PRIME * result + (int)m10;
    result = TsLibUtils.PRIME * result + (int)m11;
    result = TsLibUtils.PRIME * result + (int)m12;
    return result;
  }

  @Override
  public boolean equals( Object obj ) {
    if( obj == this ) {
      return true;
    }
    if( obj instanceof VedAffineTransform t ) {
      return m00 == t.m00 && m01 == t.m01 && m02 == t.m02 && m10 == t.m10 && m11 == t.m11 && m12 == t.m12;
    }
    return false;
  }

}
