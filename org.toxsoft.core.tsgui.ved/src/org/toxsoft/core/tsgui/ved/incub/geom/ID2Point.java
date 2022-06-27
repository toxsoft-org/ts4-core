package org.toxsoft.core.tsgui.ved.incub.geom;

/**
 * The point on a plane with <code>double</code> coordinates.
 *
 * @author hazard157
 */
public sealed interface ID2Point permits D2Point {

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
