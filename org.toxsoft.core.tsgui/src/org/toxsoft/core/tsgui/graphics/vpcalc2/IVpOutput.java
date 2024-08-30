package org.toxsoft.core.tsgui.graphics.vpcalc2;

import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

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

}
