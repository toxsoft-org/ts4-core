package org.toxsoft.core.tsgui.graphics.vpcalc2;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.utils.rectfit.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Calculates content positioning and transformation parameters depending on various parameters.
 * <p>
 * TODO describe what the viewport / content is
 *
 * @author hazard157, vs
 */
public interface IVpCalc {

  /**
   * Returns the calculation strategy settings as specified at calculator creation.
   *
   * @return {@link IVpCalcCfg} - the calculation strategy parameters
   */
  IVpCalcCfg getCfg();

  /**
   * Returns the fit mode.
   *
   * @return {@link ERectFitMode} - fit mode
   */
  ERectFitMode getFitMode();

  /**
   * Determines if small objects are expanded to fit viewport.
   *
   * @return boolean - <code>true</code> to expand small images in {@link ERectFitMode#isAdaptiveScale()} modes
   */
  boolean isExpandToFit();

  /**
   * Sets the content fitiing parameters.
   *
   * @param aFitMode {@link ERectFitMode} - fit mode
   * @param aExpandToFit boolean - <code>true</code> to expand small images in adaptive scale modes
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean setFitParams( ERectFitMode aFitMode, boolean aExpandToFit );

  ID2Angle getAngle();

  boolean setAngle( ID2Angle aAngle );

  /**
   * Informs calculator about size of the un-transformed content.
   * <p>
   * This method must be called every time when content size changes, eg when new image is loaded.
   *
   * @param aContentSize {@link ID2Size} - the content size in pixels
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean setContentSize( ID2Size aContentSize );

  /**
   * Informs calculator about viewport change.
   * <p>
   * Method must be called every time when viewport changes, eg when control is resized.
   * <p>
   * Coordinates of the viewport rectangle is given in the painting {@link GC} coordinates space.
   *
   * @param aViewportBounds {@link ITsRectangle} - viewport bounds rectangle in pixels
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean setViewportBounds( ITsRectangle aViewportBounds );

  double getParamZoom();

  boolean setParamZoom( double aZoom );

  ITsPoint getParamOrigin();

  boolean setParamOrigin( ITsPoint aOrigin );

  /**
   * Returns the output (calculated) parameters of the content painting.
   * <p>
   * Note: calculation results has an eventer to observe any changes in drawing paremeters.
   *
   * @return {@link IVpOutput} - calculation results
   */
  IVpOutput output();

}
