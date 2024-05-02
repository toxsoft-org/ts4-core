package org.toxsoft.core.tsgui.utils.anim;

/**
 * Callback of the {@link TsAnimator} animation.
 *
 * @author hazard157
 * @param <T> - user data type
 */
public interface ITsAnimatorCallback<T> {

  /**
   * Called when it's time to perform animation next step.
   * <p>
   * Timestamp values are as defined by the method {@link System#currentTimeMillis()}.
   *
   * @param aSource {@link TsAnimator} - the source of the animation
   * @param aCounter long - steps counter, changes after each call of this method
   * @param aCurrTime long - timestamp of current animation support internal cycle start
   * @return boolean - counter reset flag<br>
   *         <b>true</b> - reset counting, on next call <code>aCounter</code> will have value 0 ;<br>
   *         <b>false</b> - continue counting, on next call <code>aCounter</code> will increase by 1.
   */
  boolean onNextStep( TsAnimator<T> aSource, long aCounter, long aCurrTime );

}
