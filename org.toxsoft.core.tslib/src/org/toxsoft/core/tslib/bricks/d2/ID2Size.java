package org.toxsoft.core.tslib.bricks.d2;

import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * The dimensions of a something.
 * <p>
 * Dimensions (a width and a height) are always >= 0.0.
 *
 * @author hazard157
 */
public sealed interface ID2Size
    permits D2Size, ID2SizeEdit {

  /**
   * Size with (0.0,0.0) dimensions.
   */
  ID2Size ZERO = new D2Size( 0.0, 0.0 );

  /**
   * Size with (1.0,1.0) dimensions.
   */
  ID2Size ONE = new D2Size( 1.0, 1.0 );

  /**
   * Returns the width.
   *
   * @return double - the width, always >= 0.0
   */
  double width();

  /**
   * Returns the height.
   *
   * @return double - the height, always >= 0.0
   */
  double height();

  /**
   * Returns the size as a {@link ITsDims}.
   * <p>
   * Important note: the integer dimension follows the rules of {@link ITsDims} so returned value never is 0.
   *
   * @return {@link ITsDims} - the size as a integer dimensions
   */
  ITsDims dims();

  /**
   * Returns the width as an integer.
   * <p>
   * Simply return {@link #dims() dims().width()}.
   *
   * @return int - the width as an integer, always >= 1
   */
  default int intW() {
    return dims().width();
  }

  /**
   * Returns the height as an integer.
   * <p>
   * Simply return {@link #dims() dims().height()}.
   *
   * @return int - the height as an integer, always >= 1
   */
  default int intH() {
    return dims().height();
  }

}
