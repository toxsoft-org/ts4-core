package org.toxsoft.core.tsgui.widgets.mpv;

import org.toxsoft.core.tsgui.widgets.mpv.impl.*;
import org.toxsoft.core.tslib.math.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMultiPartValue} extension for non-negative durations in seconds.
 * <p>
 * Maximal value {@link #MAX_DURATION} is defined to fit in "HH:MM:SS" representation "99:59:59". Obviously minimal
 * value is 0.
 *
 * @author hazard157
 */
public interface IMpvSecsDuration
    extends IMultiPartValue {

  /**
   * The maximal allowed duration in sconds corresponds to "<b>9</b>9:59:59" representation, almost 100 hours.
   */
  int MAX_DURATION = 99 * 3600 + 59 * 60 + 59;

  /**
   * Returns stored value as <code>int</code>.
   *
   * @return int - duration seconds
   */
  int getValueSecs();

  /**
   * Sets value value as <code>int</code>.
   * <p>
   * The argument will be fitted in {@link #getRange()}.
   *
   * @param aSecs int - duration seconds
   */
  void setValueSecs( int aSecs );

  /**
   * Returns the widest possible range of the value.
   * <p>
   * {@link IntRange#minValue()} is always 0. {@link IntRange#maxValue()} depends on implementation parts. For
   * implementations "HH:MM:SS" it is 99:59:59, for "HH:MM" - 99:59:00, for "MM:SS" - 0:<b>9</b>9:59.
   *
   * @return {@link IntRange} - the widest possible range
   */
  IntRange getWidestRange();

  /**
   * Returns the current restriction on value.
   * <p>
   * The returned range always is subrange of {@link #getWidestRange()}.
   *
   * @return {@link IntRange} - allowed range of value
   */
  IntRange getRange();

  /**
   * Sets the allowed range for value.
   * <p>
   * If current value is out of range it will be fitted in range of new interval.
   * <p>
   * Argument will be "fitted" in {@link #getWidestRange()}.
   *
   * @param aRange {@link IntRange} - allowed range of value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setRange( IntRange aRange );

  /**
   * Creates the instance of this interface of specified parts.
   *
   * @param aIsHoursPart boolean - HH part exists in "HH:MM:SS"
   * @param aIsSecondsPart boolean - SS part exists in "HH:MM:SS"
   * @return {@link IMpvSecsDuration} - created instance
   */
  static IMpvSecsDuration create( boolean aIsHoursPart, boolean aIsSecondsPart ) {
    return switch( (aIsHoursPart ? 0x10 : 0x00) | (aIsSecondsPart ? 0x01 : 0x00) ) {
      case 0x00 -> new MpvSecsDurationMm();
      case 0x01 -> new MpvSecsDurationMmSs();
      case 0x10 -> new MpvSecsDurationHhMm();
      case 0x11 -> new MpvSecsDurationHhMmSs();
      default -> throw new TsInternalErrorRtException();
    };

  }

}
