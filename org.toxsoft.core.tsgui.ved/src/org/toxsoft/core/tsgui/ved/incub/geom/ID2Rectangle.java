package org.toxsoft.core.tsgui.ved.incub.geom;

/**
 * The rectangleon a plane with <code>double</code> coordinates.
 * <p>
 * TODO clarify if {@link #width()} / {@link #height()} may be 0 ???
 *
 * @author hazard157
 */
public sealed interface ID2Rectangle permits D2Rectangle {

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
   * returns the size of rectangle.
   *
   * @return {@link ID2Point} - size ({@link ID2Point#x()} is witdh, {@link ID2Point#y() - height})
   */
  ID2Point size();

}
