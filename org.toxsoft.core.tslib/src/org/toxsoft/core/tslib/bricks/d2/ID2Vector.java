package org.toxsoft.core.tslib.bricks.d2;

/**
 * The vector in 2D world is a directed straight line segment.
 *
 * @author hazard157
 */
public sealed interface ID2Vector permits ID2VectorEdit,D2Vector {

  /**
   * Zero length vector at origin.
   */
  ID2Vector ZERO = D2Vector.ofXyLenDeg( 0.0, 0.0, 0.0, 0.0 );

  /**
   * Returns the initial point of the vector.
   *
   * @return {@link ID2Point} - the initial point coordinates
   */
  ID2Point a();

  /**
   * Returns the terminal point of the vector.
   *
   * @return {@link ID2Point} - the terminal point coordinates
   */
  ID2Point b();

  /**
   * Returns the length of the vector.
   *
   * @return double - the length
   */
  double length();

  /**
   * Returns the angle between vector and X coordinates axis.
   *
   * @return {@link ID2Angle} - the angle of vector
   */
  ID2Angle angle();

}
