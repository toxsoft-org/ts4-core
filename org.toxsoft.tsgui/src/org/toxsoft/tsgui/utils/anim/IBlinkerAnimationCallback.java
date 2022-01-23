package org.toxsoft.tsgui.utils.anim;

/**
 * Обратный вызов аниматором {@link AnimationSupport} для отрисовки другого состояния мигания.
 * 
 * @author goga
 */
public interface IBlinkerAnimationCallback {

  /**
   * Вызывается когда настала пора отрисовать другое состояние мигающего объекта.
   * <p>
   * При каждой смене состояние мигания, меняется значение аргумента aState.
   * <p>
   * Этот метод вызывается из UI-потока выполнения, что позволяет рисовать прямо из тела метода.
   * 
   * @param aBlinkerAnimator {@link IBlinkerAnimator} - зарегистрированный аниматор мигания
   * @param aState boolean - индикатор состояния мигание
   * @param aUserData Object - произвольная ссылка, ранее переданная в
   *          {@link IAnimationSupport#registerBlinker(long, IBlinkerAnimationCallback, Object)}
   */
  void onDrawBlink( IBlinkerAnimator aBlinkerAnimator, boolean aState, Object aUserData );

}
