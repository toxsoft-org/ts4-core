package org.toxsoft.core.tsgui.graphics.vpcalc2;

import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Configuration parameters for {@link IVpCalc}.
 *
 * @author hazard157
 */
public interface IVpCalcCfg
    extends IGenericChangeEventCapable, IKeepableEntity {

  /**
   * Returns the fulcrum point of content placing in the viewport rectangle.
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
   * Returns the margins of the visible area inside the viewport rectangle.
   *
   * @return {@link ITsMargins} - visible (usable) area margins in the viewport rectangle
   */
  ITsMargins margins();

  /**
   * Returns the fit mode.
   *
   * @return {@link ERectFitMode} - fit mode
   */
  ERectFitMode fitMode();

  /**
   * Determines if small objects are expanded to fit viewport.
   *
   * @return boolean - <code>true</code> to expand small images in {@link ERectFitMode#isAdaptiveScale()} modes
   */
  boolean isExpandToFit();

  // ------------------------------------------------------------------------------------

  /**
   * Sets the fulcrum {@link #fulcrum()}.
   *
   * @param aFulcrum - the fulcrum
   */
  void setFulcrum( ETsFulcrum aFulcrum );

  /**
   * Sets the fulcrum usage strategy {@link #fulcrumStartegy()}.
   *
   * @param aStrategy {@link EVpFulcrumStartegy} - the bounding strategy
   */
  void setFulcrumStartegy( EVpFulcrumStartegy aStrategy );

  /**
   * Sets the bounding strategy {@link #boundsStrategy()}.
   *
   * @param aStrategy {@link EVpBoundingStrategy} - the bounding strategy
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setBoundingStrategy( EVpBoundingStrategy aStrategy );

  /**
   * Sets the margins {@link #margins()}.
   *
   * @param aMargins {@link ITsMargins} - the margins
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setMargins( ITsMargins aMargins );

  /**
   * Sets the content image fit mode.
   *
   * @param aFitMode {@link ERectFitMode} - fit mode
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setFitMode( ERectFitMode aFitMode );

  /**
   * Sets if small content will be expanded to fit the viewport.
   *
   * @param aExpandToFit boolean - <code>true</code> to expand small images in adaptive scale modes
   */
  void setExpandToFit( boolean aExpandToFit );

  /**
   * Copies settings from the specified source.
   *
   * @param aSource {@link IVpCalcCfg} - the source
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void copyFrom( IVpCalcCfg aSource );

}
