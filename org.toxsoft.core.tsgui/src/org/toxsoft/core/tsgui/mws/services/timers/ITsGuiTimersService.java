package org.toxsoft.core.tsgui.mws.services.timers;

import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.events.*;

/**
 * Timers service allows clients to be called periodically from the main GUI thread.
 *
 * @author hazard157
 */
public sealed interface ITsGuiTimersService permits TsGuiTimersService {

  /**
   * Returns the period of quick timer invockation.
   * <p>
   * By default quick timer period is 40 milliseconds.
   *
   * @return long - quick timer period in milliseconds
   */
  long getQuickTimerPeriod();

  /**
   * Returns the period of slow timer invockation.
   * <p>
   * By default quick timer period is 960 milliseconds.
   *
   * @return long - slowtimer period in milliseconds
   */
  long getSlowTimerPeriod();

  /**
   * Returns the quick timer eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IRealTimeSensitive}&lgt - the quick timer eventer
   */
  ITsEventer<IRealTimeSensitive> quickTimers();

  /**
   * Returns the slow timer eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IRealTimeSensitive}&lgt - the slow timer eventer
   */
  ITsEventer<IRealTimeSensitive> slowTimers();

}
