package org.toxsoft.core.tsgui.graphics.image;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tsgui.mws.services.timers.*;
import org.toxsoft.core.tsgui.utils.anim.*;
import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Helps to animate single {@link TsImage}.
 * <p>
 * Usage:
 * <ul>
 * <li>create instance and set callback or override {@link #doInvokeCallback()}. Either callback or overridden method
 * must redraw the image;</li>
 * <li>ensure that {@link #whenRealTimePassed(long)} is called from TSGUI quick timer
 * {@link ITsGuiTimersService#quickTimers()};</li>
 * <li>specify image by {@link #setImage(TsImage)} and enjoy animation :).</li>
 * </ul>
 * Note: this class does not owns the {@link TsImage} specified and does not disposes it.
 *
 * @author hazard157
 */
public class TsImageAnimator
    implements IRealTimeSensitive, IPausableAnimation {

  private ITsImageAnimatorCallback callback = null;

  private TsImage tsImage        = null;
  private boolean paused         = false;
  private long    cycleStartTime = 0L;   // time when animation cycle started last time
  private int     currIndex      = 0;

  /**
   * Constructor.
   */
  public TsImageAnimator() {
    // nop
  }

  /**
   * Constructor.
   *
   * @param aCallback {@link ITsImageAnimatorCallback} - the callback or <code>null</code>
   */
  public TsImageAnimator( ITsImageAnimatorCallback aCallback ) {
    callback = aCallback;
  }

  // ------------------------------------------------------------------------------------
  // IRealTimeSensitive
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    // first call after image was set - start animation
    if( cycleStartTime == 0L ) {
      doInvokeCallback();
      cycleStartTime = aRtTime;
      return;
    }
    if( tsImage == null ) {
      return;
    }
    if( !paused ) {
      long passed = aRtTime - cycleStartTime;
      int cInd = TsImageUtils.findIndexByTime( tsImage, passed );
      if( cInd != currIndex ) {
        currIndex = cInd;
        doInvokeCallback();
        if( cInd == 0 ) {
          cycleStartTime = aRtTime;
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the currently controlled image.
   *
   * @return {@link TsImage} - current image, may be <code>null</code>
   */
  public TsImage getImage() {
    return tsImage;
  }

  /**
   * Sets the image to control its animation.
   *
   * @param aImage {@link TsImage} - image to control, may be <code>null</code>
   */
  public void setImage( TsImage aImage ) {
    tsImage = aImage;
    cycleStartTime = 0;
    currIndex = 0;
    doInvokeCallback();
  }

  /**
   * Returns the frame to be displayed.
   * <p>
   * If animation is running, returned value changes each time when next frame is displayed.
   *
   * @return int - current index or <code>null</code> if no frame to display
   */
  public Image currentFrame() {
    if( tsImage == null || tsImage.isDisposed() ) {
      return null;
    }
    if( !tsImage.isAnimated() ) {
      return tsImage.image();
    }
    return tsImage.frames().get( currIndex );
  }

  /**
   * Returns the currently displayed frame index.
   * <p>
   * This is the index of the displayed frame in {@link TsImage#frames()}.
   * <p>
   * If image is not set then returns -1. If animation is running, value changes each time when next frame is displayed.
   *
   * @return int - frame index in the list {@link TsImage#frames()}
   */
  public int currentIndex() {
    return currIndex;
  }

  /**
   * Sets the callback invoked when image needs to be redrawn.
   *
   * @param aCallback {@link ITsImageAnimatorCallback} - the callback or <code>null</code>
   */
  public void setCallback( ITsImageAnimatorCallback aCallback ) {
    callback = aCallback;
  }

  // ------------------------------------------------------------------------------------
  // IPausableAnimation
  //

  @Override
  public boolean isPaused() {
    return paused;
  }

  @Override
  public void pause() {
    paused = true;
  }

  @Override
  public void resume() {
    paused = false;

    /**
     * TODO calculate and adjust #cycleStartTime so that animation continues from the current index, not "jumps" to the
     * index calculated by TsImageUtils.findIndexByTime()
     */

  }

  // ------------------------------------------------------------------------------------
  // To implement/override
  //

  /**
   * Implementation may override default behaviour when image redraw is needed.
   * <p>
   * By default this method invokes callback. It is up to implementation to call superclass method when overriding.
   * <p>
   * Common usage of this method is to call image redraw directly without using callback
   */
  protected void doInvokeCallback() {
    if( callback != null ) {
      try {
        callback.onNextFrame( this, currentFrame() );
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
    }
  }

}
