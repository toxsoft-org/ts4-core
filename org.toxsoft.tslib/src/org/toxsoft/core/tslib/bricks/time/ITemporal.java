package org.toxsoft.core.tslib.bricks.time;

/**
 * Temporal entities are those that have a timestamp.
 * <p>
 * Obviously those entities ane naturalLy {@link Comparable} by the moment in time.
 *
 * @author hazard157
 * @param <T> - concrete type of the entity
 */
public interface ITemporal<T extends ITemporal<T>>
    extends ITimestampable, Comparable<T> {

  // nop

}
