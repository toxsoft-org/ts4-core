package org.toxsoft.core.tsgui.utils.anim;

/**
 * General mix-in implementation of the entities capable of "playing" some kind of content.
 * <p>
 * Interface introduces the concept of "pausing" the runtime activity like media play, animation, etc.
 *
 * @author hazard157
 */
public interface IPausableAnimation {

  /**
   * Determines the current state of the runtime activity.
   *
   * @return boolean - current state of pause<br>
   *         <b>true</b> - runtime activity is <b>off</b>, the entity is "still";<br>
   *         <b>false</b> - runtime activity is <b>on</b>, the entity is "alive".
   */
  boolean isPaused();

  /**
   * Pauses (temporarily stops) the runtime activity.
   * <p>
   * On already paused entities, when {@link #isPaused()}=<code>true</code>, method does nothing.
   */
  void pause();

  /**
   * Resumes previously paused runtime activity.
   * <p>
   * On already active entities, when {@link #isPaused()}=<code>false</code>, method does nothing.
   */
  void resume();

}
