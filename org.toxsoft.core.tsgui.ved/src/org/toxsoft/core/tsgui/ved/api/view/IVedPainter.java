package org.toxsoft.core.tsgui.ved.api.view;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

/**
 * The means to draw the view on the SWT {@link Canvas}.
 * <p>
 * Note: zooming while drawing does not affects component properties. Motivation to have separate method
 * {@link #setZoomFactor(double)} rather than zoom argument in {@link #paint(GC)} is to allow paointer optimizations.
 * Zoom factor is set rarely while paint occures frequently.
 *
 * @author hazard157
 */
public interface IVedPainter {

  /**
   * Draws the component on the canvas.
   *
   * @param aGc {@link GC} - the drawing canvas
   */
  void paint( GC aGc );

  /**
   * Returns current zoom factor
   *
   * @return double - the zoom factor (1.0 is the original size)
   */
  double zoomFactor();

  /**
   * Sets the zoom factor for next paint operation.
   *
   * @param aZoomFactor double - коэффициент масштабирования
   */
  void setZoomFactor( double aZoomFactor );

}
