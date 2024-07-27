package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The rectangle on a plane with <code>double</code> coordinates.
 *
 * @author hazard157
 */
public sealed interface ID2Rectangle
    permits D2Rectangle, D2RectangleEdit {

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
   * Returns the center point.
   *
   * @return {@link ID2Point} - the center point
   */
  default ID2Point center() {
    return new D2Point( x1() + width() / 2., y1() + height() / 2. );
  }

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

  /**
   * Determines if the point is inside or on the edges of this rectangle.
   *
   * @param aX double - the X coordinate of the point
   * @param aY double - the Y coordinate of the point
   * @return boolean - <code>true</code> if this rectangle contains the point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean contains( double aX, double aY );

  /**
   * Determines if the point is inside or on the edges of this rectangle.
   *
   * @param aPoint {@link ID2Point} - the point
   * @return boolean - <code>true</code> if this rectangle contains the point
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean contains( ID2Point aPoint );

}
