package org.toxsoft.core.tslib.bricks.d2;

/**
 * The rectangle on a plane with <code>double</code> coordinates.
 *
 * @author hazard157
 */
public sealed interface ID2Rectangle permits D2Rectangle,D2RectangleEdit {

  // TODO ID2RectangleEdit

  /**
   * Rectangle of 0 size at (0.0,0.0).
   */
  ID2Rectangle ZERO = new D2Rectangle( 0.0, 0.0, 0.0, 0.0 );

  /**
   * Returns the top-left corner X coordinate.
   *
   * @return double - the top-left corner X coordinate
   */
  double x1();

  /**
   * Returns the top-left corner Y coordinate.
   *
   * @return double - the top-left corner Y coordinate
   */
  double y1();

  /**
   * Returns the bottom-right corner X coordinate.
   *
   * @return double - the bottom-right corner X coordinate
   */
  double x2();

  /**
   * Returns the bottom-right corner Y coordinate.
   *
   * @return double - the bottom-right corner Y coordinate
   */
  double y2();

  /**
   * Returns the top-left corner coordinates.
   *
   * @return {@link ID2Point} - the top-left corner coordinates
   */
  ID2Point a();

  /**
   * Returns the bottom-right corner coordinates.
   *
   * @return {@link ID2Point} - the bottom-right corner coordinates
   */
  ID2Point b();

  /**
   * Returns the width of the rectangle.
   *
   * @return double - the width
   */
  double width();

  /**
   * Returns the height of the rectangle.
   *
   * @return double - the height
   */
  double height();

  /**
   * Returns the size of rectangle.
   *
   * @return {@link ID2Size} - the size
   */
  ID2Size size();

  boolean contains( double aX, double aY );

  boolean contains( ID2Point aPoint );

}
