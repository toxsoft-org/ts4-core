package org.toxsoft.core.tslib.bricks.d2;

import static org.toxsoft.core.tslib.bricks.d2.D2Utils.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An ediable extension of {@link ID2Vector}.
 *
 * @author hazard157
 */
public sealed interface ID2VectorEdit
    extends ID2Vector permits D2VectorEdit {

  @Override
  ID2PointEdit a();

  @Override
  ID2PointEdit b();

  /**
   * Sets vector origin to the specified location.
   * <p>
   * Vector length and angle remains intact.
   *
   * @param aX double - X coordinate of the location
   * @param aY double - Y coordinate of the location
   * @throws TsIllegalArgumentRtException invalid value
   */
  void setLocation( double aX, double aY );

  /**
   * Shifts vector origin on the specified amount.
   * <p>
   * Vector length and angle remains intact.
   *
   * @param aDeltaX double - amount to shift on X coordinate
   * @param aDeltaY double - amount to shift on Y coordinate
   * @throws TsIllegalArgumentRtException invalid value
   */
  void shiftOn( double aDeltaX, double aDeltaY );

  /**
   * Changes length of the vector.
   * <p>
   * Vector initial point and angle remains unchanged.
   *
   * @param aLength double - new length, must be positive
   * @throws TsIllegalArgumentRtException invalid value
   */
  void setLength( double aLength );

  /**
   * Sets new angle of the vector.
   * <p>
   * Vector starting point and length remains intact.
   *
   * @param aRadians double - new angle in radians
   * @throws TsIllegalArgumentRtException invalid value
   */
  void setAngleRad( double aRadians );

  /**
   * Sets vector by initial and terminal point.
   *
   * @param aInitPoint {@link ID2Point} - vector initial point
   * @param aTermPoint {@link ID2Point} - vector terminal point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setVector( ID2Point aInitPoint, ID2Point aTermPoint );

  /**
   * Sets vector by initial point, length and angle.
   *
   * @param aInitPoint {@link ID2Point} - initial point
   * @param aLength double - new length, must be positive
   * @param aRadians double - new angle in radians
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid value
   */
  void setVectorRad( ID2Point aInitPoint, double aLength, double aRadians );

  /**
   * Sets vector by initial point, length and angle.
   *
   * @param aInitPoint {@link ID2Point} - initial point
   * @param aLength double - new length, must be positive
   * @param aDegrees double - new angle in degrees
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid value
   */
  void setVectorDeg( ID2Point aInitPoint, double aLength, double aDegrees );

  /**
   * Sets vector from a source vector.
   *
   * @param aSource {@link ID2Vector} - the source
   */
  void setVector( ID2Vector aSource );

  // ------------------------------------------------------------------------------------
  // Inline methods for convinience
  //

  @SuppressWarnings( "javadoc" )
  default void setLocation( ID2Point aLocation ) {
    TsNullArgumentRtException.checkNull( aLocation );
    setLocation( aLocation.x(), aLocation.y() );
  }

  @SuppressWarnings( "javadoc" )
  default void shiftOn( ID2Point aShift ) {
    TsNullArgumentRtException.checkNull( aShift );
    shiftOn( aShift.x(), aShift.y() );
  }

  @SuppressWarnings( "javadoc" )
  default void setAngleDeg( double aDegrees ) {
    setAngleRad( deg2rad( aDegrees ) );
  }

}
