package org.toxsoft.core.tslib.bricks.d2;

/**
 * The angle in 2D world.
 *
 * @author hazard157
 */
public sealed interface ID2Angle permits ID2AngleEdit,D2Angle {

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
