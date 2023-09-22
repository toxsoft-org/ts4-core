package org.toxsoft.core.tsgui.ved.zver2.api.comp;

import org.eclipse.swt.graphics.*;
import org.eclipse.ui.services.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * The means to draw some decorations on the entire VED screen.
 * <p>
 * Common usage is to fill screen background in {@link #paintBefore(GC, ITsRectangle)} or grid in
 * {@link #paintAfter(GC, ITsRectangle)}.
 *
 * @author hazard157
 */
public interface IVedScreenDecorator
    extends IDisposable {

  /**
   * Implementation may draw on screen <b>before</b> the views are painted.
   *
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region that need to be painted
   */
  void paintBefore( GC aGc, ITsRectangle aPaintBounds );

  /**
   * Implementation may draw on screen <b>after</b> the views are painted.
   *
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region that need to be painted
   */
  void paintAfter( GC aGc, ITsRectangle aPaintBounds );

  /**
   * Called by VED screen when {@link IVedScreen#getConversion()} changes.
   * <p>
   * Implementation may need this update to recalculate internal caches, resources, etc.
   */
  default void updateOnScreenConversionChange() {
    // nop
  }

}
