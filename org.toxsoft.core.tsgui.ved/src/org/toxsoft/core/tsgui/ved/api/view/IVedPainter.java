package org.toxsoft.core.tsgui.ved.api.view;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * The means to draw something VED related on the SWT {@link Canvas}.
 * <p>
 * Note: conversion while drawing does not affects component properties. Motivation to have separate method
 * {@link #setConversion(ID2Conversion)} rather than conversion parameters arguments in {@link #paint(GC, ITsRectangle)}
 * is to allow painter optimizations. Copnversion is set rarely while paint occures frequently.
 *
 * @author hazard157
 */
public interface IVedPainter {

  /**
   * Draws the component on the canvas.
   *
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region that need to be painted
   */
  void paint( GC aGc, ITsRectangle aPaintBounds );

  /**
   * Returns current conversion of normal coordinate space of component to painting coordinates space.
   *
   * @return {@link ID2Conversion} - current conversion parameters
   */
  ID2Conversion getConversion();

  /**
   * Sets the conversion parameters to be used in further paintings.
   *
   * @param aConversion {@link ID2Conversion} - conversion parameters
   */
  void setConversion( ID2Conversion aConversion );

}
