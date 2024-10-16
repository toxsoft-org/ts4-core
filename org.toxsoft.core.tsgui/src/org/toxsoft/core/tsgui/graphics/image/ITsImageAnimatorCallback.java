package org.toxsoft.core.tsgui.graphics.image;

import org.eclipse.swt.graphics.*;

/**
 * Callback informs about displayed frame change so redraw is needed for animation display.
 *
 * @author hazard157
 */
public interface ITsImageAnimatorCallback {

  /**
   * Called when displayed frame is changed so redraw is needed.
   * <p>
   * Method is called from main GUI thread. Argument <code>aFrame</code> is the same as
   * {@link TsImageAnimator#currentFrame() aSource.currentFrame()}.
   *
   * @param aSource {@link TsImageAnimator} - the callback caller
   * @param aFrame {@link Image} - the image to draw or <code>null</code>
   */
  void onNextFrame( TsImageAnimator aSource, Image aFrame );

}
