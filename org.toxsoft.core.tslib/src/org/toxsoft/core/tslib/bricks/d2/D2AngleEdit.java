package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2AngleEdit} implementation.
 *
 * @author hazard157
 */
public final class D2AngleEdit
    implements ID2AngleEdit, Serializable {

  private static final long serialVersionUID = -1574866026368637608L;

  private double radians = 0.0;
  private double degrees = 0.0;

  /**
   * Creates angle of 0.0 (both radians and degrees).
   */
  public D2AngleEdit() {
    // nop
  }

  /**
   * Copy constructor.
   *
   * @param aAngle {@link ID2Angle} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2AngleEdit( ID2Angle aAngle ) {
    TsNullArgumentRtException.checkNull( aAngle );
    radians = aAngle.radians();
    degrees = aAngle.degrees();
  }

  /**
   * Creates angle of specified degrees.
   *
   * @param aDegrees double - angle value in degrees
   * @return {@link D2AngleEdit} - created instance
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static D2AngleEdit ofDegrees( double aDegrees ) {
    D2AngleEdit a = new D2AngleEdit();
    a.degrees = duck( aDegrees );
    a.radians = deg2rad( a.degrees );
    return a;
  }

  /**
   * Creates angle of specified radians.
   *
   * @param aRadians double - angle value in radians
   * @return {@link D2AngleEdit} - created instance
   * @throws TsIllegalArgumentRtException invalid value
   */
  public static D2AngleEdit ofRadians( double aRadians ) {
    D2AngleEdit a = new D2AngleEdit();
    a.radians = aRadians;
    a.degrees = duck( rad2deg( aRadians ) );
    return a;
  }

  // ------------------------------------------------------------------------------------
  // ID2Angle
  //

  @Override
  public double radians() {
    return radians;
  }

  @Override
  public double degrees() {
    return degrees;
  }

  // ------------------------------------------------------------------------------------
  // ID2AngleEdit
  //

  @Override
  public void setRad( double aRadians ) {
    radians = checkAngleRadians( aRadians );
    degrees = duck( rad2deg( radians ) );
  }

  @Override
  public void changeRad( double aDeltaRadians ) {
    radians += checkAngleRadians( aDeltaRadians );
    degrees = duck( rad2deg( radians ) );
  }

  @Override
  public void setDeg( double aDegrees ) {
    degrees = duck( checkAngleDegrees( aDegrees ) );
    radians = deg2rad( degrees );
  }

  @Override
  public void changeDeg( double aDeltaDegrees ) {
    degrees += duck( checkAngleDegrees( aDeltaDegrees ) );
    radians = deg2rad( degrees );
  }

  @Override
  public void setAngle( ID2Angle aAngle ) {
    TsNullArgumentRtException.checkNull( aAngle );
    radians = aAngle.radians();
    degrees = aAngle.degrees();
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return "∠" + radians + " (" + degrees + "°)"; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2AngleEdit that) ) {
      return false;
    }
    return this.radians == that.radians() && this.degrees == that.degrees();
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    long dblval = Double.doubleToRawLongBits( radians );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    dblval = Double.doubleToRawLongBits( degrees );
    result = PRIME * result + (int)(dblval ^ (dblval >>> 32));
    return result;
  }

}
