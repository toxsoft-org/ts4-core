package org.toxsoft.core.tsgui.utils.margins;

import org.toxsoft.core.tslib.math.*;

/**
 * Options to customize the borders when drawing internals in a rectangular panel.
 * <p>
 * Implemented as a read-only interface and editable class pattern.
 * <p>
 * Value of any option is always "fitted" in allowed range {@link ITsMargins#VALUES_RANGE}.
 *
 * @author hazard157
 */
public sealed interface ITsMargins
    permits ITsGridMargins, TsMargins {

  /**
   * The allowed range of any parameter value.
   * <p>
   * Options outside the range will be forced to "fit" the range.
   */
  IntRange VALUES_RANGE = new IntRange( 0, 10 * 1024 );

  /**
   * Returns the distance between the internals and the left edge of the panel.
   *
   * @return int - distance between the internals and the left edge of the panel in pixels
   */
  int left();

  /**
   * Returns the distance between the internals and the right edge of the panel.
   *
   * @return int - distance between the internals and the right edge of the panel in pixels
   */
  int right();

  /**
   * Returns the distance between the internals and the top edge of the panel.
   *
   * @return int - distance between the internals and the top edge of the panel in pixels
   */
  int top();

  /**
   * Returns the distance between the internals and the bottom edge of the panel.
   *
   * @return int - distance between the internals and the bottom edge of the panel in pixels
   */
  int bottom();

  // ------------------------------------------------------------------------------------
  // Convenience inline methods
  //

  @SuppressWarnings( "javadoc" )
  default int horMargin() {
    return left() + right();
  }

  @SuppressWarnings( "javadoc" )
  default int verMargin() {
    return top() + bottom();
  }

}
