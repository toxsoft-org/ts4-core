package org.toxsoft.core.tsgui.ved.olds.incub;

import org.eclipse.swt.graphics.*;

/**
 * Mixin interface of objects that can be drawn on SWT {@link GC} canvas.
 *
 * @author vs
 */
public interface IPaintable {

  /**
   * Implementation must draw itself on the canvas.
   *
   * @param aGc {@link GC} - the graphics context, the canvas
   */
  void paint( GC aGc );

}
