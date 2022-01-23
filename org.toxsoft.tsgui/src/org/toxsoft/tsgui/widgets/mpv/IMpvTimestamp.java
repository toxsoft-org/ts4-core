package org.toxsoft.tsgui.widgets.mpv;

import java.time.LocalDateTime;

import org.toxsoft.tsgui.widgets.mpv.impl.*;
import org.toxsoft.tslib.bricks.time.ITimeInterval;
import org.toxsoft.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMultiPartValue} extension for timestamp.
 * <p>
 * Supports timestamp representation both as <code>long</code> milliseconds from epoche start and {@link LocalDateTime}.
 *
 * @author hazard157
 */
public interface IMpvTimestamp
    extends IMultiPartValue {

  /**
   * Returns stored value as <code>long</code> epoch millisecons.
   *
   * @return long - milleseconds from epoch start
   */
  long getAsTimestamp();

  /**
   * Sets value as <code>long</code> epoch millisecons.
   * <p>
   * If timestamp is outside of interval {@link #getInterval()} then it will be fitted in range.
   *
   * @param aTimestamp long - milleseconds from epoch start
   */
  void setAsTimestamp( long aTimestamp );

  /**
   * Returns stored value as {@link LocalDateTime}.
   *
   * @return {@link LocalDateTime} - timestamp
   */
  LocalDateTime getAsDatetime();

  /**
   * Sets value as {@link LocalDateTime}.
   * <p>
   * If timestamp is outside of interval {@link #getInterval()} then it will be fitted in range.
   *
   * @param aTimestamp {@link LocalDateTime} - timestamp
   */
  void setAsDatetime( LocalDateTime aTimestamp );

  /**
   * Returns the current interval allowed for value in this class.
   *
   * @return {@link ITimeInterval} - the allowed range of values
   */
  ITimeInterval getInterval();

  /**
   * Sets the allowed interval of timestamp.
   * <p>
   * If current value is out of range it will be fitted in range of new interval.
   * <p>
   * Not that even if argument is big enough, only dates in 0000...9999 will be represented by this class, so resulting
   * range will be smaller.
   *
   * @param aInterval {@link ITimeInterval} - the new interval
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setInterval( ITimeInterval aInterval );

  /**
   * Returns the widest interval of the value.
   * <p>
   * Widest interval is determined by the parts number and type. This is the possible minimal and maximal values that
   * may be held in this class.
   *
   * @return {@link ITimeInterval} - widest interval
   */
  ITimeInterval getWidestInterval();

  /**
   * Creates the instance of this interface of specified editing accuracy.
   *
   * @param aTimeLen {@link EMpvTimeLen} - desired accuracy and length of time representation
   * @return {@link IMpvTimestamp} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  static IMpvTimestamp create( EMpvTimeLen aTimeLen ) {
    TsNullArgumentRtException.checkNull( aTimeLen );
    switch( aTimeLen ) {
      case HH_MM_SS_MMM:
        return new MpvTimestampMillisecs();
      case HH_MM_SS:
        return new MpvTimestampSeconds();
      case HH_MM:
        return new MpvTimestampMinutes();
      default:
        throw new TsNotAllEnumsUsedRtException( aTimeLen.id() );
    }
  }

}
