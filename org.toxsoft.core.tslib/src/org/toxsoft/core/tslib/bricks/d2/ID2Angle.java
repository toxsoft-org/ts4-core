package org.toxsoft.core.tslib.bricks.d2;

/**
 * The angle in 2D world.
 *
 * @author hazard157
 */
public sealed interface ID2Angle permits ID2AngleEdit,D2Angle {

  /**
   * FIXME instead radians/degrees use unit(EAngleUnit)/value pair
   */

  /**
   * The 0.0 angle singleton.
   */
  ID2Angle ZERO = D2Angle.ofDegrees( 0.0 );

  /**
   * Returns angle in radians.
   *
   * @return double - the radians
   */
  double radians();

  /**
   * Returns angle in degrees.
   *
   * @return double - the degrees
   */
  double degrees();

}
