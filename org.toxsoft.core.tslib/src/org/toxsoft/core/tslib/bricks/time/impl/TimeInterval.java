package org.toxsoft.core.tslib.bricks.time.impl;

import java.io.Serializable;
import java.time.*;
import java.time.temporal.ChronoUnit;

import org.toxsoft.core.tslib.bricks.time.ITimeInterval;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Неизменяемая реализация интервала времени {@link ITimeInterval}.
 *
 * @author hazard157
 */
public final class TimeInterval
    implements ITimeInterval, Serializable {

  private static final long serialVersionUID = 157157L;

  private final long start;
  private final long end;

  private transient LocalDateTime ldtStart = null;
  private transient LocalDateTime ldtEnd   = null;
  private transient Duration      duration = null;

  /**
   * Constructor.
   *
   * @param aStart long - starting time in epoch millisecons
   * @param aEnd long - ending time in epoch millisecons
   * @throws TsIllegalArgumentRtException aEnd < aStart
   */
  public TimeInterval( long aStart, long aEnd ) {
    TimeUtils.checkIntervalArgs( aStart, aEnd );
    start = aStart;
    end = aEnd;
  }

  /**
   * Constructor.
   * <p>
   * Arguments are not stored in instance - {@link #startTime()} and {@link #endTime()} <code>long</code> values are
   * stored instead.
   *
   * @param aStart {@link LocalDateTime} - starting time
   * @param aEnd {@link LocalDateTime} - ending time
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aEnd is before aStart
   */
  public TimeInterval( LocalDateTime aStart, LocalDateTime aEnd ) {
    TsNullArgumentRtException.checkNulls( aStart, aEnd );
    TsIllegalArgumentRtException.checkTrue( aStart.isAfter( aEnd ) );
    start = aStart.toInstant( ZoneOffset.UTC ).toEpochMilli();
    end = aEnd.toInstant( ZoneOffset.UTC ).toEpochMilli();
  }

  // ------------------------------------------------------------------------------------
  // ITimeInterval
  //

  @Override
  public long startTime() {
    return start;
  }

  @Override
  public long endTime() {
    return end;
  }

  @Override
  public long duration() {
    return end - start + 1;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return TimeUtils.intervalToString( start, end );
  }

  @Override
  public boolean equals( Object aObj ) {
    if( aObj == this ) {
      return true;
    }
    if( aObj instanceof TimeInterval ) {
      TimeInterval obj = (TimeInterval)aObj;
      return start == obj.start && end == obj.end;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + (int)(start ^ (start >>> 32));
    result = TsLibUtils.PRIME * result + (int)(end ^ (end >>> 32));
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса Comparable
  //

  @Override
  public int compareTo( ITimeInterval o ) {
    if( o == null ) {
      throw new NullPointerException();
    }
    int c = Long.compare( start, o.startTime() );
    if( c == 0 ) {
      c = Long.compare( end, o.endTime() );
    }
    return c;
  }

  @Override
  public LocalDateTime getStartDatetime() {
    if( ldtStart == null ) {
      ldtStart = LocalDateTime.ofInstant( Instant.ofEpochMilli( start ), ZoneOffset.UTC );
    }
    return ldtStart;
  }

  @Override
  public LocalDateTime getEndDatetime() {
    if( ldtEnd == null ) {
      ldtEnd = LocalDateTime.ofInstant( Instant.ofEpochMilli( end ), ZoneOffset.UTC );
    }
    return ldtEnd;
  }

  @Override
  public Duration getDuration() {
    if( duration == null ) {
      duration = Duration.of( duration(), ChronoUnit.MILLIS );
    }
    return duration;
  }

}
