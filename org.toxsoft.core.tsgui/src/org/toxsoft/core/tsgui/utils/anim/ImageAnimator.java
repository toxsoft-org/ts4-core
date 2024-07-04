package org.toxsoft.core.tsgui.utils.anim;

import org.toxsoft.core.tsgui.graphics.image.impl.*;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Простая реализация {@link IImageAnimator}.
 * <p>
 * Внимание: эта реализация не владеет ресурсом {@link TsImage} - то есть, она не освобождает ресурсы и не вызывает
 * {@link TsImage#dispose()}.
 *
 * @author hazard157
 */
class ImageAnimator
    implements IImageAnimator {

  private final TsImage                 multiImage;
  private final IImageAnimationCallback callback;
  private final Object                  userData;

  private int     currIndex = 0;
  private boolean paused    = false;

  private long lastDrawTimespamp = 0;

  ImageAnimator( TsImage aTsImage, IImageAnimationCallback aCallback, Object aUserData ) {
    multiImage = TsNullArgumentRtException.checkNull( aTsImage );
    callback = TsNullArgumentRtException.checkNull( aCallback );
    userData = aUserData;
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Возвращает значение индекса текущего кадра и смещает индекс на следующий кадр.
   *
   * @return int - индекс кадра, который должен быть сейчас отображен
   */
  int nextIndex() {
    int result = currIndex;
    if( ++currIndex >= multiImage.count() ) {
      currIndex = 0;
    }
    return result;
  }

  /**
   * Возвращает значение индекса текущего кадра.
   *
   * @return int - индекс кадра, который должен быть сейчас отображен
   */
  int getIndex() {
    return currIndex;
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
  // IImageAnimator
  //

  @Override
  public IImageAnimationCallback callback() {
    return callback;
  }

  @Override
  public TsImage multiImage() {
    return multiImage;
  }

  @Override
  public Object userData() {
    return userData;
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

}
