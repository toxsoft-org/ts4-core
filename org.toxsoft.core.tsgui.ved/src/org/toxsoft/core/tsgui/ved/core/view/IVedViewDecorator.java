package org.toxsoft.core.tsgui.ved.core.view;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * The means to draw something (the decoration) assosiated to view..
 *
 * @author hazard157
 */
public interface IVedViewDecorator
    extends ID2Conversionable, IDisposable {

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

}
