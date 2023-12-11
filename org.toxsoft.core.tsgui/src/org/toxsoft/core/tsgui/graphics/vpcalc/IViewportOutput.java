package org.toxsoft.core.tsgui.graphics.vpcalc;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * Calculated output values.
 *
 * @author hazard157
 */
public interface IViewportOutput
    extends IGenericChangeEventCapable {

  /**
   * Returns the conversion (transformation) to be applied to the drawing content.
   *
   * @return {@link ID2Conversion} - transformation to apply to the content before painting
   */
  ID2Conversion conversion();

  // /**
  // * Returns the 2D coordinates converter always converting {@link #conversion()} says.
  // *
  // * @return {@link ID2Convertor} - the coordinates converter
  // */
  // ID2Convertor converter();

  /**
   * Returns the horizontal scroll bar parameters.
   *
   * @return {@link ScrollBarSettings} - the horizontal scroll bar parameters
   */
  ScrollBarSettings horBarSettings();

  /**
   * Returns the vertical scroll bar parameters.
   *
   * @return {@link ScrollBarSettings} - the vertical scroll bar parameters
   */
  ScrollBarSettings verBarSettings();

}
