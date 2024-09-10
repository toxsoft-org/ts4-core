package org.toxsoft.core.tsgui.graphics.vpcalc2;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

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
  IVpCalcCfg cfg();

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

  /**
   * Returns the content rotation angle.
   * <p>
   * Content is rotated around the actual origin point.
   * <p>
   * This is the same value as an actual angle returned by {@link #output() output().d2Conv().rotation()}.
   *
   * @return {@link ID2Angle} - the rotation angle
   */
  ID2Angle getAngle();

  /**
   * Sets the content rotation angle {@link #getAngle()}.
   *
   * @param aAngle {@link ID2Angle} - the rotation angle
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean setAngle( ID2Angle aAngle );

  /**
   * Returns the desired (not actual) zoom factor previously set by {@link #setDesiredZoom(double)}.
   * <p>
   * Actual origin value is returned by {@link #output() output().d2Conv().zoomFactor()}.
   *
   * @return double - the desired zoom factor (1.0 is the original size)
   */
  double getDesiredZoom();

  /**
   * Sets the desired zoom factor {@link #getDesiredZoom()}.
   * <p>
   * The real zoom depends on configuration, and may not the same as the desired one. For example, in adaptive fit modes
   * zoom factor is determined by the viewport size.
   *
   * @param aZoom double - the desired zoom factor (1.0 is the original size)
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean setDesiredZoom( double aZoom );

  /**
   * Returns the desired (not actual) origin coordinates previously set by {@link #setDesiredOrigin(ITsPoint)}.
   * <p>
   * Actual origin value is returned by {@link #output() output().d2Conv().origin()}.
   *
   * @return {@link ITsPoint} - the desired origin coordinates
   */
  ITsPoint getDesiredOrigin();

  /**
   * Sets the desired origin coordinates {@link #getDesiredOrigin()}.
   * <p>
   * The real coordinates of origin depends on configuration, and may not the same as the desired coordinates. For
   * example, if {@link IVpCalcCfg#fulcrumStartegy()} is {@link EVpFulcrumStartegy#ALWAYS}, desired value is ignored and
   * read origin is calculated depending on viewport, content size, fulcrum point, margins, etc.
   *
   * @param aOrigin {@link ITsPoint} - the desired origin coordinates
   * @return boolean - <code>true</code> if {@link #output()} has been changed
   */
  boolean setDesiredOrigin( ITsPoint aOrigin );

  /**
   * Returns the output (calculated) parameters of the content painting.
   * <p>
   * Note: calculation results has an eventer to observe any changes in drawing paremeters.
   *
   * @return {@link IVpOutput} - calculation results
   */
  IVpOutput output();

}
