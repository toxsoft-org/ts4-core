package org.toxsoft.core.tsgui.widgets.mpv.impl;

import java.time.LocalDate;

import org.toxsoft.core.tsgui.widgets.mpv.IMpvLocalDate;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.math.IntRange;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpvLocalDate} implementation in form "YYYY-MM-DD".
 *
 * @author goga
 */
public class MpvLocatDate
    extends MultiPartValueBase
    implements IMpvLocalDate {

  private static final int IDX_YEAR  = 0;
  private static final int IDX_MONTH = 1;
  private static final int IDX_DAY   = 2;

  private LocalDate minDate = MIN_MIN_DATE;
  private LocalDate maxDate = MAX_MAX_DATE;

  /**
   * Constructor.
   */
  public MpvLocatDate() {
    addPart( new Part( "YYYY", 4, new IntRange( 0, 9999 ), -1, '-' ) ); //$NON-NLS-1$
    addPart( new Part( "MM months", 2, new IntRange( 1, 12 ), -1, '-' ) ); //$NON-NLS-1$
    addPart( new Part( "DD", 2, new IntRange( 1, 31 ), -1, -1 ) ); //$NON-NLS-1$
    setAsLocalDate( LocalDate.now() );
  }

  // ------------------------------------------------------------------------------------
  // IMpvLocalDate
  //

  @Override
  public LocalDate getAsLocalDate() {
    return LocalDate.of( pval( IDX_YEAR ), pval( IDX_MONTH ), pval( IDX_DAY ) );
  }

  @Override
  public void setAsLocalDate( LocalDate aDate ) {
    TsNullArgumentRtException.checkNull( aDate );
    LocalDate d = aDate;
    if( d.isBefore( minDate ) ) {
      d = minDate;
    }
    if( d.isAfter( maxDate ) ) {
      d = maxDate;
    }
    IIntList oldVals = getPartsValues();
    sval( IDX_YEAR, d.getYear() );
    sval( IDX_MONTH, d.getMonthValue() );
    sval( IDX_DAY, d.getDayOfMonth() );
    IIntList newVals = getPartsValues();
    if( !newVals.equals( oldVals ) ) {
      eventer().fireChangeEvent();
    }
  }

  @Override
  public LocalDate getMinDate() {
    return minDate;
  }

  @Override
  public LocalDate getMaxDate() {
    return maxDate;
  }

  @Override
  public void setDateRange( LocalDate aMinDate, LocalDate aMaxDate ) {
    TsNullArgumentRtException.checkNulls( aMinDate, aMaxDate );
    LocalDate dmin = aMinDate.isBefore( MIN_MIN_DATE ) ? MIN_MIN_DATE : aMinDate;
    LocalDate dmax = aMaxDate.isAfter( MAX_MAX_DATE ) ? MAX_MAX_DATE : aMaxDate;
    if( !dmin.equals( minDate ) && !dmax.equals( maxDate ) ) {
      minDate = dmin;
      maxDate = dmax;
      LocalDate d = getAsLocalDate();
      if( d.isBefore( minDate ) ) {
        setAsLocalDate( minDate );
      }
      else {
        if( d.isAfter( maxDate ) ) {
          setAsLocalDate( maxDate );
        }
      }
    }
  }

}
