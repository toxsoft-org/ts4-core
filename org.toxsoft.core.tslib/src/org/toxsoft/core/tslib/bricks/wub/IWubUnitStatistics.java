package org.toxsoft.core.tslib.bricks.wub;

import org.toxsoft.core.tslib.av.opset.*;

/**
 * Cumulative information about WUB unit working statistics in a running WUB box.
 * <p>
 * Note: diagnostics information {@link IWubUnitDiagnostics} is created and maintained by the WUB unit itself while
 * statistics information {@link IWubUnitStatistics} are created and maintained by the WUB box.
 *
 * @author hazard157
 */
public interface IWubUnitStatistics {

  /**
   * Returns the moment of time when {@link IWubUnit#start()} was called.
   *
   * @return long - start time in milliseconds after epoch or 0L if unit was not started yet
   */
  long startTime();

  /**
   * Returns the moment of time when {@link IWubUnit#start()} was called.
   * <p>
   * Returned value is obtained by {@link System#nanoTime()}. This value used as base for nanosecond accuracy statistics
   * values.
   *
   * @return long - start time in nanoseconds or {@link Long#MIN_VALUE}
   */
  long startTimeNanos();

  /**
   * Returns the current values of the cumulative and runtime statistics variables.
   * <p>
   * The examples of the statistics variables are:
   * <ul>
   * <li>total number of times the unit doJob() calls after start, total duration of unit execution;</li>
   * <li>average % percentage of the unit doJob() execution time in the box doJob() execution for last call, last
   * minute, hour etc.;</li>
   * </ul>
   * <p>
   * Note: if {@link IWubUnitStatistics} instance is received before unit is started returned set may be epmty and/or
   * contain senseless values.
   *
   * @return {@link IOptionSet} - statistics variable values
   */
  IOptionSet statisticsVariablesValues();

}
