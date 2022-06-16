package org.toxsoft.core.tslib.bricks.time;

/**
 * The time interval used to query time-related data.
 * <p>
 * This is {@link ITimeInterval} expanded with the rules how to process data at interval boundaries.
 *
 * @author mvk
 */
public interface IQueryInterval
    extends ITimeInterval {

  /**
   * Returns the query type determining the rules how to process data at interval boundaries.
   *
   * @return {@link EQueryIntervalType} - the query interval type
   */
  EQueryIntervalType type();

}
