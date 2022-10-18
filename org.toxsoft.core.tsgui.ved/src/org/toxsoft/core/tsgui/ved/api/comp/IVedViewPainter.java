package org.toxsoft.core.tsgui.ved.api.comp;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * The means to draw the view on the SWT {@link Canvas}.
 *
 * @author hazard157
 */
public interface IVedViewPainter {

  /**
   * Draws the component on the canvas.
   *
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region in pixels that need to be painted
   */
  void paint( GC aGc, ITsRectangle aPaintBounds );

}
