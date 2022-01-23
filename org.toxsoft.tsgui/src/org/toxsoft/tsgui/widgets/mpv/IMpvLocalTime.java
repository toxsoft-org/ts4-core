package org.toxsoft.tsgui.widgets.mpv;

import java.time.LocalTime;

import org.toxsoft.tsgui.widgets.mpv.impl.*;
import org.toxsoft.tslib.math.IntRange;
import org.toxsoft.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMultiPartValue} extension for {@link LocalTime}.
 * <p>
 * Supports time of day representation as <code>int</code> seconds and milliseconds from midnight and as
 * {@link LocalTime}.
 *
 * @author hazard157
 */
public interface IMpvLocalTime
    extends IMultiPartValue {

  /**
   * Returns stored value as <code>int</code> milliseconds from midnight.
   *
   * @return int - milliseconds from midnight in range 0..86_400_000
   */
  int getAsDayMillisecs();

  /**
   * Sets value as <code>int</code> milliseconds from midnignt.
   * <p>
   * If - time of day is outside of {@link #getRangeSecs()} then it will be fitted in range.
   *
   * @param aDayMillisecs int - milleseconds from midnignt
   */
  void setAsDayMillisecs( int aDayMillisecs );

  /**
   * Returns stored value as <code>int</code> seconds from midnight.
   *
   * @return int - seconds from midnight in range 0..86_400_000
   */
  int getAsDaySeconds();

  /**
   * Sets value as <code>int</code> seconds from midnignt.
   * <p>
   * If - time of day is outside of {@link #getRangeSecs()} then it will be fitted in range.
   *
   * @param aDaySeconds int - milleseconds from midnignt
   */
  void setAsDaySeconds( int aDaySeconds );

  /**
   * Returns stored value as {@link LocalTime}.
   *
   * @return {@link LocalTime} - time of day
   */
  LocalTime getAsLocalTime();

  /**
   * Sets value as {@link LocalTime}.
   * <p>
   * If - time of day is outside of {@link #getRangeSecs()} then it will be fitted in range.
   *
   * @param aTime {@link LocalTime} - - time of day
   */
  void setAsLocalTime( LocalTime aTime );

  /**
   * Returns the allowed range of value.
   * <p>
   * The range is specified in seconds. Returned value always has {@link IntRange#minValue()} >=0 and
   * {@link IntRange#maxValue()} < 86_400. Note, 86400 is number of seconds in day.
   *
   * @return {@link IntRange} - allowed range in seconds
   */
  IntRange getRangeSecs();

  /**
   * Sets the allowed range of value.
   * <p>
   * If argument will be fitted in range 0..(86400-1).
   * <p>
   * If current value is out of range it will be fitted in range of new interval.
   *
   * @param aRange {@link IntRange} - allowed range in seconds
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setRangeSecs( IntRange aRange );

  /**
   * Creates the instance of this interface of specified editing accuracy.
   *
   * @param aTimeLen {@link EMpvTimeLen} - desired accuracy and length of time representation
   * @return {@link IMpvLocalTime} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static IMpvLocalTime create( EMpvTimeLen aTimeLen ) {
    TsNullArgumentRtException.checkNull( aTimeLen );
    switch( aTimeLen ) {
      case HH_MM_SS_MMM:
        return new MpvLocalTimeMillisecs();
      case HH_MM_SS:
        return new MpvLocalTimeSeconds();
      case HH_MM:
        return new MpvLocalTimeMinutes();
      default:
        throw new TsNotAllEnumsUsedRtException( aTimeLen.id() );
    }
  }

}
