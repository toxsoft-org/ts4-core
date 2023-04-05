package org.toxsoft.core.tslib.bricks;

/**
 * Mix-in interface of time-dependent entities.
 * <p>
 * Interface assumes that it will be called periodically for all time-dependent entities.
 *
 * @author hazard157
 */
public interface IRealTimeSensitive {

  /**
   * Informs entity that time has passed.
   * <p>
   * The argument value is the same as {@link System#currentTimeMillis()} and remains the same for all time-dependent
   * entities called in one sweep.
   *
   * @param aRtTime long - current time in milliseconds from epoch start
   */
  void whenRealTimePassed( long aRtTime );

}
