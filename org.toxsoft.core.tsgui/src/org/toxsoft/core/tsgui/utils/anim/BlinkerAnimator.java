package org.toxsoft.core.tsgui.utils.anim;

import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Простая реализация {@link IBlinkerAnimator}.
 *
 * @author hazard157
 */
class BlinkerAnimator
    implements IBlinkerAnimator {

  private final long interval;
  private final IBlinkerAnimationCallback callback;
  private final Object userData;

  private boolean state = false;
  private boolean paused = true;

  private long lastDrawTimespamp = 0;

  BlinkerAnimator( long aInterval, IBlinkerAnimationCallback aCallback, Object aUserData ) {
    TsIllegalArgumentRtException.checkTrue( aInterval <= 0 );
    interval = aInterval;
    callback = TsNullArgumentRtException.checkNull( aCallback );
    userData = aUserData;
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Возвращает текущего состояни мигания и меняет это состояние.
   *
   * @return boolean - текущее среди одно из двух состоянии мигания
   */
  boolean nextState() {
    state = !state;
    return !state;
  }

  /**
   * Возвращает метку времени, когда в последный раз вызывалась перерисовка.
   *
   * @return long - время последней перерисовки в миллисекундах с начала эпохи
   */
  long lastDrawTimespamp() {
    return lastDrawTimespamp;
  }

  /**
   * Задает метку времени, когда в последный раз вызывалась перерисовка.
   *
   * @param aLastDrawTimespamp long - время последней перерисовки в миллисекундах с начала эпохи
   */
  void setLastDrawTimespamp( long aLastDrawTimespamp ) {
    lastDrawTimespamp = aLastDrawTimespamp;
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
  // IBlinkerAnimator
  //

  @Override
  public long interval() {
    return interval;
  }

  @Override
  public IBlinkerAnimationCallback callback() {
    return callback;
  }

  @Override
  public Object userData() {
    return userData;
  }

}
