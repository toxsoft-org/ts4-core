package org.toxsoft.core.tsgui.mws.services.timers;

import org.toxsoft.core.tslib.bricks.*;
import org.toxsoft.core.tslib.bricks.events.*;

/**
 * Timers service allows clients to be called periodically from the main GUI thread.
 *
 * @author hazard157
 */
public sealed interface ITsGuiTimersService
    permits TsGuiTimersService {

  /**
   * Returns the period of quick timer invocation.
   * <p>
   * Default quick timer period is specified in {@link ITsGuiTimersServiceConstants#OPDEF_QUICK_TIMER_PERIOD}.
   *
   * @return long - quick timer period in milliseconds
   */
  long getQuickTimerPeriod();

  /**
   * Returns the period of slow timer invocation.
   * <p>
   * Default quick timer period is specified in {@link ITsGuiTimersServiceConstants#OPDEF_SLOW_TIMER_PERIOD}.
   *
   * @return long - slow timer period in milliseconds
   */
  long getSlowTimerPeriod();

  /**
   * Returns the quick timer eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IRealTimeSensitive}&lt; - the quick timer eventer
   */
  ITsEventer<IRealTimeSensitive> quickTimers();

  /**
   * Returns the slow timer eventer.
   *
   * @return {@link ITsEventer}&lt;{@link IRealTimeSensitive}&lt; - the slow timer eventer
   */
  ITsEventer<IRealTimeSensitive> slowTimers();

}
