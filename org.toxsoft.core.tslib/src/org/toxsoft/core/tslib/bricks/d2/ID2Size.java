package org.toxsoft.core.tslib.bricks.d2;

/**
 * The dimensions of a something.
 * <p>
 * Dimensions (a width and a height) are always >= 0.0.
 *
 * @author hazard157
 */
public sealed interface ID2Size permits D2Size,ID2SizeEdit {

  /**
   * Size with (0.0,0.0) coordinates.
   */
  ID2Size ZERO = new D2Size( 0.0, 0.0 );

  /**
   * Returns the width.
   *
   * @return double - the width, always >= 0
   */
  double width();

  /**
   * Returns the height.
   *
   * @return double - the height, always >= 0
   */
  double height();

}
