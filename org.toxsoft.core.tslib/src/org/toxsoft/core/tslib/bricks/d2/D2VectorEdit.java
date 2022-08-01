package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ID2VectorEdit} implementation.
 *
 * @author hazard157
 */
public final class D2VectorEdit
    implements ID2VectorEdit, Serializable {

  private static final long serialVersionUID = -3232710855232107664L;

  private final ID2PointEdit a     = new D2PointEdit( 0.0, 0.0 );
  private final ID2PointEdit b     = new D2PointEdit( 1.0, 0.0 );
  private final ID2AngleEdit angle = D2AngleEdit.ofDegrees( 0.0 );

  private double length;

  /**
   * Constructor.
   *
   * @param aInitPoint {@link ID2Point} - initial point
   * @param aTermPoint {@link ID2Point} - terminal point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public D2VectorEdit( ID2Point aInitPoint, ID2Point aTermPoint ) {
    TsNullArgumentRtException.checkNulls( aInitPoint, aTermPoint );
    a.setPoint( aInitPoint );
    b.setPoint( aTermPoint );
    recalcLengthAndAngle();
  }

  /**
   * Creates vector basted on initial coordinates, length and angle.
   *
   * @param aX double - X coordintae of initial point
   * @param aY double - L coordintae of initial point
   * @param aLength souble - the length of the vector
   * @param aDegrees double angle in degrees
   * @return {@link D2VectorEdit} - created instence
   * @throws TsIllegalArgumentRtException any value is invalid as defined by {@link D2Utils}<code>.checkXxx()</code>
   */
  public static D2VectorEdit ofXyLenDeg( double aX, double aY, double aLength, double aDegrees ) {
    ID2Point initPoint = new D2Point( aX, aY );
    checkLength( aLength );
    checkAngleDegrees( aDegrees );
    double radians = deg2rad( aDegrees );
    double dx = Math.cos( radians );
    double dy = Math.sin( radians );
    ID2Point termPoint = new D2Point( aX + dx, aY + dy );
    return new D2VectorEdit( initPoint, termPoint );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Updates {@link #length} and {@link #angle} values when {@link #a} and {@link #b} are specified.
   */
  private void recalcLengthAndAngle() {
    double dx = b.x() - a.x();
    double dy = b.y() - a.y();
    length = duck( Math.sqrt( dx * dx + dy * dy ) );
    angle.setRad( Math.acos( dx / length ) );
  }

  /**
   * Updates {@link #b} value when {@link #a}, {@link #length} and {@link #angle} are specified.
   */
  private void recalcTerminalPoint() {
    double dx = length * Math.cos( angle.radians() );
    double dy = length * Math.sin( angle.radians() );
    b.setPoint( a.x() + dx, a.y() + dy );
  }

  // ------------------------------------------------------------------------------------
  // ID2Vector
  //

  @Override
  public ID2PointEdit a() {
    return a;
  }

  @Override
  public ID2PointEdit b() {
    return b;
  }

  @Override
  public double length() {
    return length;
  }

  @Override
  public ID2Angle angle() {
    return angle;
  }

  // ------------------------------------------------------------------------------------
  // ID2VectorEdit
  //

  @Override
  public void setLocation( double aX, double aY ) {
    checkCoor( aX );
    checkCoor( aY );
    double dx = b.x() - a.x();
    double dy = b.y() - a.y();
    a.setPoint( aX, aY );
    b.setPoint( aX + dx, aY + dy );
  }

  @Override
  public void shiftOn( double aDeltaX, double aDeltaY ) {
    checkCoor( aDeltaX );
    checkCoor( aDeltaY );
    a.shiftOn( aDeltaX, aDeltaY );
    b.shiftOn( aDeltaX, aDeltaY );
  }

  @Override
  public void setLength( double aLength ) {
    checkLength( aLength );
    length = aLength;
    recalcTerminalPoint();
  }

  @Override
  public void setAngleRad( double aRadians ) {
    angle.setRad( aRadians );
    recalcTerminalPoint();
  }

  @Override
  public void setVector( ID2Point aInitPoint, ID2Point aTermPoint ) {
    TsNullArgumentRtException.checkNulls( aInitPoint, aTermPoint );
    a.setPoint( aInitPoint );
    b.setPoint( aTermPoint );
    recalcLengthAndAngle();
  }

  @Override
  public void setVectorRad( ID2Point aInitPoint, double aLength, double aRadians ) {
    TsNullArgumentRtException.checkNull( aInitPoint );
    checkLength( aLength );
    a.setPoint( aInitPoint );
    length = aLength;
    angle.setRad( aRadians );
    recalcTerminalPoint();
  }

  @Override
  public void setVectorDeg( ID2Point aInitPoint, double aLength, double aDegrees ) {
    TsNullArgumentRtException.checkNull( aInitPoint );
    checkLength( aLength );
    a.setPoint( aInitPoint );
    length = aLength;
    angle.setDeg( aDegrees );
    recalcTerminalPoint();
  }

  @Override
  public void setVector( ID2Vector aSource ) {
    TsNullArgumentRtException.checkNull( aSource );
    setVector( aSource.a(), aSource.b() );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return String.format( "{%s,%s,l=%.4f,%s}", //$NON-NLS-1$
        a.toString(), b.toString(), Double.valueOf( length ), angle.toString() );
  }

  @Override
  public boolean equals( Object object ) {
    if( object == this ) {
      return true;
    }
    if( !(object instanceof ID2Vector that) ) {
      return false;
    }
    return this.a.equals( that.a() ) && this.b.equals( that.b() );
  }

  @Override
  public int hashCode() {
    int result = INITIAL_HASH_CODE;
    result = PRIME * result + a.hashCode();
    result = PRIME * result + b.hashCode();
    return result;
  }

}
