package org.toxsoft.core.tsgui.utils.anim;

/**
 * Обратный вызов зарегистрированного аниматора {@link IGeneralAnimator}.
 *
 * @author hazard157
 * @param <T> - тип пользовательских данных
 */
public interface IGeneralAnimationCallback<T> {

  /**
   * Вызывается с заданным {@link IGeneralAnimator#interval()} интервалом для очередного шага анимации.
   * <p>
   * Этот метод вызывается из UI-потока выполнения, что позволяет рисовать прямо из тела метода.
   *
   * @param aAnimator {@link IGeneralAnimator} - информация об анимации
   * @param aCounter long - счетчик вызовов, начинает счет с 0
   * @param aUserData &lt;T&gt; - произвольная ссылка {@link IGeneralAnimator#userData()}
   * @return boolean - признак сброса счетчика<br>
   *         <b>true</b> - при следующем вызове aCounter будет 0;<br>
   *         <b>false</b> - aCounter продолжит считать вызовы.
   */
  boolean onNextStep( IGeneralAnimator<T> aAnimator, long aCounter, T aUserData );

}
