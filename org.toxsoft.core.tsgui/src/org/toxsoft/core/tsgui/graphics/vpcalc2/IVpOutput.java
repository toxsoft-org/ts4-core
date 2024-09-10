package org.toxsoft.core.tsgui.graphics.vpcalc2;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Calculated output values.
 *
 * @author hazard157
 */
public interface IVpOutput
    extends IGenericChangeEventCapable {

  /**
   * Returns the conversion (transformation) to be applied to the drawing content.
   *
   * @return {@link ID2Conversion} - transformation to apply to the content before painting
   */
  ID2Conversion d2Conv();

  /**
   * Returns the horizontal scroll bar parameters.
   *
   * @return {@link IScrollBarCfg} - the horizontal scroll bar parameters
   */
  IScrollBarCfg horBar();

  /**
   * Returns the vertical scroll bar parameters.
   *
   * @return {@link IScrollBarCfg} - the vertical scroll bar parameters
   */
  IScrollBarCfg verBar();

  /**
   * Returns the rectangle bounds around content to draw.
   * <p>
   * When the rotation angle is zero, bounds rectangle is the same as the content rectangle. This data is not needed for
   * content drawing, rather may be useful for debug and special drawing modes.
   *
   * @return {@link ITsRectangle} - drawn content bounds in screen (viewport) coordinates
   */
  ITsRectangle getContentDrawingBounds();

}
