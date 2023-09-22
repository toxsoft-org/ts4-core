package org.toxsoft.core.tsgui.ved.zver2.api.comp;

import org.eclipse.swt.graphics.*;
import org.eclipse.ui.services.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * The means to draw something (the decoration) assosiated to view..
 *
 * @author hazard157
 */
public interface IVedViewDecorator
    extends IDisposable {

  /**
   * Performs decorator drawing <b>after</b> the view is drawn.
   *
   * @param aView {@link IVedComponentView} - the view to be decorated
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region that need to be painted
   */
  void paintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds );

  /**
   * Performs decorator drawing <b>before</b> the view is drawn.
   *
   * @param aView {@link IVedComponentView} - the view to be decorated
   * @param aGc {@link GC} - the graphics context
   * @param aPaintBounds {@link ITsRectangle} - rectangle region that need to be painted
   */
  default void paintBefore( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // nop
  }

  /**
   * Called by VED screen when {@link IVedScreen#getConversion()} changes.
   * <p>
   * Implementation may need this update to recalculate internal caches, resources, etc.
   */
  default void updateOnScreenConversionChange() {
    // nop
  }

}
