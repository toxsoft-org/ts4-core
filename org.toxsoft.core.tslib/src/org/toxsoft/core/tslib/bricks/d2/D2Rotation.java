package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2Rotation} implementation.
 *
 * @author hazard157
 */
public final class D2Rotation
    implements ID2Rotation {

  private ID2Point pivotPoint;
  private ID2Angle rotationAngle;

  /**
   * Constructor.
   */
  public D2Rotation() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aPivotPoint {@link ID2Point} - pivot point
   * @param aRotationAngle {@link ID2Angle} - rotation angle
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Rotation( ID2Point aPivotPoint, ID2Angle aRotationAngle ) {
    TsNullArgumentRtException.checkNulls( aPivotPoint, aRotationAngle );
    pivotPoint = new D2Point( aPivotPoint );
    rotationAngle = new D2Angle( aRotationAngle );
  }

  /**
   * Copy constructor.
   *
   * @param aSource {@link ID2Rotation} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2Rotation( ID2Rotation aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    pivotPoint = new D2Point( aSource.pivotPoint() );
    rotationAngle = new D2Angle( aSource.rotationAngle() );
  }

  D2Rotation( double aPivotX, double aPivotY, double aAngle, boolean aIsRadians ) {
    pivotPoint = new D2Point( aPivotX, aPivotY );
    rotationAngle = new D2Angle( aAngle, aIsRadians );
  }

  /**
   * Creates instance with angle specified in radians.
   *
   * @param aPivotX double - pivot pint X coordinate
   * @param aPivotY double - pivot pint Y coordinate
   * @param aRadians double - rotation angle in radians
   * @return {@link D2Rectangle} - created instance
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static D2Rotation ofCoorRad( double aPivotX, double aPivotY, double aRadians ) {
    return new D2Rotation( aPivotX, aPivotY, aRadians, true );
  }

  /**
   * Creates instance with angle specified in degrees.
   *
   * @param aPivotX double - pivot pint X coordinate
   * @param aPivotY double - pivot pint Y coordinate
   * @param aDegrees double - rotation angle in degrees
   * @return {@link D2Rectangle} - created instance
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static D2Rotation ofCoorDeg( double aPivotX, double aPivotY, double aDegrees ) {
    return new D2Rotation( aPivotX, aPivotY, aDegrees, false );
  }

  // ------------------------------------------------------------------------------------
  // ID2Rotation
  //

  @Override
  public ID2Point pivotPoint() {
    return pivotPoint;
  }

  @Override
  public ID2Angle rotationAngle() {
    return rotationAngle;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return '{' + pivotPoint.toString() + ' ' + rotationAngle.toString() + '}';
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2Rotation that) ) {
      return false;
    }
    return this.pivotPoint.equals( that.pivotPoint() ) && this.rotationAngle.equals( that.rotationAngle() );
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + pivotPoint.hashCode();
    result = PRIME * result + rotationAngle.hashCode();
    return result;
  }

}
