package org.toxsoft.tslib.bricks.time;

/**
 * Mixin interface with timestamp.
 * <p>
 * This interface introduces timestamp {@link #timestamp()}. Some entities (like iterators) timestamp may vary from call
 * to call, while others (like measurement values) will have the same returned value during instance lifetime.
 *
 * @author hazard157
 */
public interface ITimestampable {

  /**
   * Returns moment of time.
   *
   * @return long - time in milliseconds after epoche start (00:00:00 01/01/1970)
   */
  long timestamp();

}
