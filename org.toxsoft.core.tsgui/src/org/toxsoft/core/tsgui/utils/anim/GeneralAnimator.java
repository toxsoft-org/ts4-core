package org.toxsoft.core.tsgui.utils.anim;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IGeneralAnimator}.
 *
 * @author hazard157
 * @param <T> - тип пользовательских данных
 */
class GeneralAnimator<T>
    implements IGeneralAnimator<T> {

  private final long                         interval;
  private final IGeneralAnimationCallback<T> callback;
  private final T                            userData;

  private boolean paused            = true;
  private long    counter           = 0;
  private long    lastCallTimestamp = 0;

  GeneralAnimator( long aInterval, IGeneralAnimationCallback<T> aCallback, T aUserData ) {
    TsIllegalArgumentRtException.checkTrue( aInterval <= 0 );
    interval = aInterval;
    callback = TsNullArgumentRtException.checkNull( aCallback );
    userData = aUserData;
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Возвращает значение счетчика и заодно сдвигает его дальше.
   *
   * @return long - очередное значение счетчика
   */
  long nextCounter() {
    return counter++;
  }

  /**
   * Resets internal counter to 0.
   */
  void resetCounter() {
    resetCounter( 0L );
  }

  /**
   * Resets internal counter to the specified value.
   *
   * @param aCounter long - initial counter value
   */
  void resetCounter( long aCounter ) {
    counter = aCounter;
  }

  /**
   * Возвращает метку времени, когда в последный раз вызывалась перерисовка.
   *
   * @return long - время последней перерисовки в миллисекундах с начала эпохи
   */
  long lastCallTimestamp() {
    return lastCallTimestamp;
  }

  /**
   * Задает метку времени, когда в последный раз вызывалась перерисовка.
   *
   * @param aLastCallTimestamp long - время последней перерисовки в миллисекундах с начала эпохи
   */
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
  // IGeneralAnimator
  //

  @Override
  public long interval() {
    return interval;
  }

  @Override
  public IGeneralAnimationCallback<T> callback() {
    return callback;
  }

  @Override
  public T userData() {
    return userData;
  }

}
