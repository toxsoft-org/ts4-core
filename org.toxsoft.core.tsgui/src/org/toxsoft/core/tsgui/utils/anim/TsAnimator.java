package org.toxsoft.core.tsgui.utils.anim;

import static org.toxsoft.core.tsgui.utils.anim.AnimationSupport.*;

import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * General purpose animator.
 *
 * @author hazard157
 * @param <T> - user data type
 */
public final class TsAnimator<T>
    implements IPausableAnimation {

  private static final LongRange GRAMULARITY_RANGE = new LongRange( MIN_GRANULARITY, 86_400_000 );

  private final ITsAnimatorCallback<T> callback;
  private final T                      userData;
  private final long                   granularity;

  private boolean paused             = true;
  private long    counter            = 0;
  private long    lastResetTimestamp = 0;
  private long    lastCallTimestamp  = 0;

  TsAnimator( T aUserData, ITsAnimatorCallback<T> aCallback, long aGranularity ) {
    callback = TsNullArgumentRtException.checkNull( aCallback );
    userData = aUserData;
    granularity = GRAMULARITY_RANGE.inRange( aGranularity );
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  ITsAnimatorCallback<T> callback() {
    return callback;
  }

  long granularity() {
    return granularity;
  }

  long nextCounter() {
    return counter++;
  }

  void resetCounter( long aTime ) {
    counter = 0L;
    lastResetTimestamp = aTime;
  }

  void setCounter( long aCounter ) {
    counter = aCounter;
  }

  long lastCallTimestamp() {
    return lastCallTimestamp;
  }

  long lastResetTimestamp() {
    return lastResetTimestamp;
  }

  void setLastCallTimestamp( long aLastCallTimestamp ) {
    lastCallTimestamp = aLastCallTimestamp;
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
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the user data specified at animator creation.
   *
   * @return &lt;T&gt; - the user data
   */
  public T userData() {
    return userData;
  }

  /**
   * Returns timestamp when callback was last called with <code>aCounter</code> = 0.
   *
   * @return long - time of last counter reset
   */
  public long lastResetTime() {
    return lastResetTimestamp;
  }

}
