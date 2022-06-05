package org.toxsoft.core.tsgui.utils.anim;

/**
 * Обратный вызов аниматором {@link AnimationSupport} для перерисовки очередного состояния мигания.
 * 
 * @author hazard157
 */
public interface IBlinkerAnimator
    extends IPausableAnimation {

  /**
   * Вовзращает интервал между сменами состояния мигания.
   * 
   * @return long - интервал между сменами состояния мигания в миллисекундах
   */
  long interval();

  /**
   * Возвращает интерфейс для вызова пользовательской функции перерисовки мигающего объекта.
   * 
   * @return {@link IBlinkerAnimationCallback} - интерфейс метода обратного вызова, не null
   */
  IBlinkerAnimationCallback callback();

  /**
   * Возвращает ссылку, переданную в методе
   * {@link IAnimationSupport#registerBlinker(long, IBlinkerAnimationCallback, Object)}
   * 
   * @return Object - пользовательсякая произвольная ссылка
   */
  Object userData();

}
