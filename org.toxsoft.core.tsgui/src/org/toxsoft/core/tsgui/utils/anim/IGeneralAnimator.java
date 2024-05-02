package org.toxsoft.core.tsgui.utils.anim;

/**
 * Аниматор общего назначения.
 *
 * @author hazard157
 * @param <T> - user data type
 * @deprecated use {@link TsAnimator} instead
 */
@Deprecated
public interface IGeneralAnimator<T>
    extends IPausableAnimation {

  /**
   * Вовзращает интервал между вызовами метода
   * {@link IGeneralAnimationCallback#onNextStep(IGeneralAnimator, long, Object)} .
   *
   * @return long - интервал между сменами состояния мигания в миллисекундах
   */
  long interval();

  /**
   * Возвращает интерфейс для вызова пользовательской функции перерисовки.
   *
   * @return {@link IGeneralAnimationCallback} - интерфейс метода обратного вызова, не null
   */
  IGeneralAnimationCallback<T> callback();

  /**
   * An arbitrary reference used only to return in the method
   * {@link IGeneralAnimationCallback#onNextStep(IGeneralAnimator, long, Object)}.
   *
   * @return &lt;T&gt; - произвольная ссылка на пользовательский данные
   */
  T userData();

}
