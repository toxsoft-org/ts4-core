package org.toxsoft.core.tsgui.widgets.mpv;

import java.time.LocalDate;

import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMultiPartValue} extension for {@link LocalDate}.
 *
 * @author hazard157
 */
public interface IMpvLocalDate
    extends IMultiPartValue {

  /**
   * Minimal allowed date of {@link #getMinDate()}.
   */
  LocalDate MIN_MIN_DATE = LocalDate.of( 0, 1, 1 );

  /**
   * Maximal allowed date of {@link #getMaxDate()}.
   */
  LocalDate MAX_MAX_DATE = LocalDate.of( 9999, 12, 31 );

  /**
   * Returns stored value as {@link LocalDate}.
   *
   * @return {@link LocalDate} - the date
   */
  LocalDate getAsLocalDate();

  /**
   * Sets value as {@link LocalDate}.
   *
   * @param aDate {@link LocalDate} - the date
   */
  void setAsLocalDate( LocalDate aDate );

  /**
   * Returns the minimal allowed date of value.
   *
   * @return {@link LocalDate} - minimal value
   */
  LocalDate getMinDate();

  /**
   * Returns the maximal allowed date of value.
   *
   * @return {@link LocalDate} - maximal value
   */
  LocalDate getMaxDate();

  /**
   * Sets the allowed range for value.
   * <p>
   * If current value is out of range it will be fitted in range of new interval.
   * <p>
   * Anyway allowed range will remain in interval {@link #MIN_MIN_DATE} .. {@link #MAX_MAX_DATE}.
   *
   * @param aMinDate {@link LocalDate} - starting date of interval, inclusive
   * @param aMaxDate {@link LocalDate} - ending date of interval, inclusive
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException start date is after end date
   */
  void setDateRange( LocalDate aMinDate, LocalDate aMaxDate );

}
