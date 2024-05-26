package org.toxsoft.core.tslib.gw.time;

import org.toxsoft.core.tslib.bricks.*;

/**
 * Mix-in interface of Green World time-dependent entities.
 * <p>
 * The time of the Green World (TODO describe correctly):
 * <ul>
 * <li>time is measured in milliseconds from epoch start (UTC time starting from 01/01/1970);</li>
 * <li>there is nothing like clock to ask time for, instead Green World is informed from outside when some amount of
 * time has passed by method {@link #whenGwTimePassed(long)};</li>
 * <li>there is no ability to calculate passed time between method calls. In fact, time may flow at any "speed" and in
 * any direction - next call to {@link #whenGwTimePassed(long)} may have argument value less than previous call;</li>
 * <li>thus the Green World time is flexible and may flow in any direction at any "speed".</li>
 * </ul>
 * <p>
 * This interface has same meaning for Green World time as {@link IRealTimeSensitive} has for the Red World.
 *
 * @author hazard157
 */
public interface IGwTimeFleetable {

  /**
   * Informs entity that Green World time has passed.
   * <p>
   * Attention: entities should <b>not</b> get the current time themselves (in particular, they should <b>not</b> call
   * {@link System#currentTimeMillis()} themselves), instead, they should use the argument as a timestamp.
   *
   * @param aGwTime long - Green World time in milliseconds from epoch start
   */
  void whenGwTimePassed( long aGwTime );

}
