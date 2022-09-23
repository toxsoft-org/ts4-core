package org.toxsoft.core.tsgui.utils;

/**
 * Mixin interface of the time-sensitive GUI entities.
 * <p>
 * TODO describe<br>
 * FIXME make some utility class where many {@link IGuiThreadWorker} may be registered
 *
 * @author hazard157
 */
public interface IGuiThreadWorker {

  /**
   * Called periodically when the main GUI thread is idle.
   * <p>
   * Implememntation may preform any actions however it is recommended to keep method body execution time as short as
   * possible.
   * <p>
   * Timestamp argument is the time returned by {@link System#currentTimeMillis()}. Because this method may be called
   * for many entities sequentally same argument allows to avoid many {@link System#currentTimeMillis()} calls and many
   * entities can act in sync with each other.
   *
   * @param aTimestamp long - the current time at the time the method was called
   */
  void onGuiThreadIdle( long aTimestamp );

}
