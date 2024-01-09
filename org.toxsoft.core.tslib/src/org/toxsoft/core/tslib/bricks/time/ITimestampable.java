package org.toxsoft.core.tslib.bricks.time;

/**
 * Mix-in interface with timestamp.
 * <p>
 * This interface introduces timestamp {@link #timestamp()}. Some entities (like iterators) timestamp may vary from call
 * to call, while others (like measurement values) will have the same returned value during instance lifetime.
 * <p>
 * Note: {@link #timestamp()} method does not specifies which time is used. At runtime
 * {@link System#currentTimeMillis()} returns time of current time zone. Implementers must specify timestamp time zone.
 *
 * @author hazard157
 */
public interface ITimestampable {

  /**
   * Returns moment of time.
   *
   * @return long - time in milliseconds after epoch start (00:00:00 01/01/1970)
   */
  long timestamp();

}
