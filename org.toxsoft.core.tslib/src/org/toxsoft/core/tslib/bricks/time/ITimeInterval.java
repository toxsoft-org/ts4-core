package org.toxsoft.core.tslib.bricks.time;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Поянтие "интервал времени" (реализован неизменным классом-значением {@link TimeInterval}).
 * <p>
 * В этой модели мы считаем, что все время имеет протяженность от {@link TimeUtils#MIN_TIMESTAMP} миллисекунд с 00:00:00
 * 01.01.1970 до {@link TimeUtils#MAX_TIMESTAMP} миллисекунд (обе границы включены). Поскольку это в точности
 * соответствует интервале чисел {@link Long}, то любая long метка времени всегда находится внутри "нашего времени".
 * <p>
 * This notion of a time interval includes a start time marker {@link #startTime()} and an end time marker
 * {@link #endTime()}. The end of an interval cannot be earlier than the start. The start and end time markers are
 * included in the interval. Thus, the minimum length of an interval is 1 millisecond (when {@link #startTime()} =
 * {@link #endTime()}).
 * <p>
 * Implements interface {@link Comparable}&lt;{@link ITimeInterval}&gt; sorting in ascending order first by
 * {@link #startTime()}, and then by {@link #endTime()}.
 *
 * @author hazard157
 */
public interface ITimeInterval
    extends Comparable<ITimeInterval> {

  /**
   * All time - from the beginning of time to its end.
   * <p>
   * All other time intervals are inside {@link #WHOLE}.
   */
  ITimeInterval WHOLE = new TimeInterval( TimeUtils.MIN_TIMESTAMP, TimeUtils.MAX_TIMESTAMP );

  /**
   * "Null" (absent, undefined, unspecified) time interval.
   * <p>
   * In fact, this constant can and should be used instead of <code>null</code>. All methods of the
   * {@link ITimeInterval} interface of this instance throw an exception {@link TsNullObjectErrorRtException}. If there
   * is a suspicion that the received reference to {@link ITimeInterval} may not be defined, then first you should check
   * the reference by a simple comparison <code>interval != {@link ITimeInterval#NULL}</code>.
   * <p>
   * You can safely use the Object methods of this class {@link #equals(Object)}, {@link #hashCode()},
   * {@link #toString()}, and {@link Comparable#compareTo(Object)}.
   */
  ITimeInterval NULL = new InternalNullTimeInterval();

  /**
   * Returns the interval start timestamp.
   *
   * @return long - interval start time (in milliseconds since epoch)
   */
  long startTime();

  /**
   * Returns the interval end timestamp.
   *
   * @return long - interval end time (in milliseconds since epoch)
   */
  long endTime();

  /**
   * Returns the interval duration as milliseconds.
   *
   * @return long - milliseconds duration, always >= 1
   */
  long duration();

  /**
   * Returns staring time (inclusive) as {@link LocalDateTime}.
   * <p>
   * Returned value is always presented in millisecond accuracy and equals to UTC {@link #startTime()}.
   *
   * @return {@link LocalDateTime} - start time of interval
   */
  LocalDateTime getStartDatetime();

  /**
   * Returns ending time (inclusive) as {@link LocalDateTime}.
   * <p>
   * Returned value is always presented in millisecond accuracy and equals to UTC {@link #endTime()}.
   *
   * @return {@link LocalDateTime} - end time of interval
   */
  LocalDateTime getEndDatetime();

  /**
   * Returns the interval duration as {@link Duration}.
   * <p>
   * Returned value is always presented in millisecond accuracy and equals to {@link #duration()}.
   *
   * @return {@link Duration} - the duration
   */
  Duration getDuration();

}

final class InternalNullTimeInterval
    implements ITimeInterval, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Method correctly deserializes {@link ITimeInterval#NULL} value.
   *
   * @return {@link ObjectStreamException} - {@link ITimeInterval#NULL}
   * @throws ObjectStreamException is declared by convention but newer thrown by this method
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return ITimeInterval.NULL;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return ITimeInterval.class.getSimpleName() + ".NULL"; //$NON-NLS-1$
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) { // includes also NULL check
      return true;
    }
    // GOGA 2025-0913 TO REMOVE as unneeded code
    // if( aObj instanceof ITimeInterval that ) {
    // return that.startTime() == this.startTime() && that.endTime() == this.endTime();
    // }
    // ---
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Comparable
  //

  @Override
  public int compareTo( ITimeInterval aThat ) {
    if( aThat == null ) {
      throw new NullPointerException();
    }
    // this class singleton NULL is equal to itself and is less than any other interval
    return aThat == this ? 0 : -1;
  }

  // ------------------------------------------------------------------------------------
  // ITimeInterval
  //

  @Override
  public long startTime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public long endTime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public long duration() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public LocalDateTime getStartDatetime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public LocalDateTime getEndDatetime() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

  @Override
  public Duration getDuration() {
    throw new TsNullObjectErrorRtException( InternalNullTimeInterval.class );
  }

}
