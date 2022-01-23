package org.toxsoft.tsgui.utils.anim;

/**
 * Аниматор общего назначения.
 *
 * @author goga
 * @param <T> - тип пользовательских данных
 */
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
   * Произвольная ссылка, используемая только для того, чтобы вернуть в методе
   * {@link IGeneralAnimationCallback#onNextStep(IGeneralAnimator, long, Object)}.
   *
   * @return &lt;T&gt; - произвольная ссылка на пользовательский данные
   */
  T userData();

}
