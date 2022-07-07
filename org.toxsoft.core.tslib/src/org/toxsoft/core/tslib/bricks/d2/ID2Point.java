package org.toxsoft.core.tslib.bricks.d2;

/**
 * The point on a plane with <code>double</code> coordinates.
 *
 * @author hazard157
 */
public sealed interface ID2Point permits D2Point,D2PointEdit {

  /**
   * Returns the X coordinate.
   *
   * @return double - the X coordinate
   */
  double x();

  /**
   * Returns the Y coordinate.
   *
   * @return double - the Y coordinate
   */
  double y();

}
