package org.toxsoft.core.tsgui.utils.margins;

/**
 * Options to customize the borders and intervals of drawing a grid of cells in a rectangular panel.
 * <p>
 * Implemented as a read-only interface and editable class pattern.
 *
 * @author hazard157
 */
public sealed interface ITsGridMargins
    extends ITsMargins permits TsGridMargins {

  /**
   * Returns the horizontal distance between grid cells.
   *
   * @return int - horizontal distance between grid cells in pixels
   */
  int horGap();

  /**
   * Returns the vertical distance between grid cells.
   *
   * @return int - vertical distance between grid cells in pixels
   */
  int verGap();

  /**
   * Returns the thickness of the border of the panel.
   *
   * @return int - the width of the border of the panel in pixels
   */
  int borderWidth();

}
