package org.toxsoft.core.tsgui.ved.api.view;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
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
    extends ID2Conversionable, IVedDisposable {

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

}
