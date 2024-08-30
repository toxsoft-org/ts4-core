package org.toxsoft.core.tsgui.graphics.vpcalc2;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.margins.*;

/**
 * Invariant configuration parameters for {@link IVpCalc}.
 *
 * @author hazard157
 */
public interface IVpCalcCfg {

  /**
   * Returns the fulcrum point of content placing in the vieweport recangle.
   *
   * @return {@link ETsFulcrum} - placement fulcrum
   */
  ETsFulcrum fulcrum();

  /**
   * Determines in which cases {@link #fulcrum()} has to be applied to drawing.
   *
   * @return {@link EVpFulcrumStartegy} - how to use fulcrum
   */
  EVpFulcrumStartegy fulcrumStartegy();

  /**
   * Determines how the content location will be limited by the viewport.
   * <p>
   * Applied when {@link #fulcrum()} is <b>not</b> used.
   *
   * @return {@link EVpBoundingStrategy} - how to restrict content location
   */
  EVpBoundingStrategy boundsStrategy();

  /**
   * Retruns the margins of the visible area inside the viewport rectangle.
   *
   * @return {@link ITsMargins} - visible (usable) area margins in the viewport rectangle
   */
  ITsMargins margins();

}
