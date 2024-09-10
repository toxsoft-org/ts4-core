package org.toxsoft.core.tslib.bricks.d2;

/**
 * Point in 2D world.
 *
 * @author hazard157
 */
public sealed interface ID2Point
    permits D2Point, ID2PointEdit {

  /**
   * Point with (0.0,0.0) coordinates.
   */
  ID2Point ZERO = new D2Point( 0.0, 0.0 );

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

  @SuppressWarnings( "javadoc" )
  default int intX() {
    return (int)x();
  }

  @SuppressWarnings( "javadoc" )
  default int intY() {
    return (int)y();
  }

}
