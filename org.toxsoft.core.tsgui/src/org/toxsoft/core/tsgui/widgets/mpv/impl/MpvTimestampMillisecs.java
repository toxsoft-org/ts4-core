package org.toxsoft.core.tsgui.widgets.mpv.impl;

import java.time.*;

import org.toxsoft.core.tsgui.widgets.mpv.IMpvTimestamp;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.math.IntRange;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpvTimestamp} implementation in form "YYYY-MM-DD HH:MM:SS.mmm".
 *
 * @author goga
 */
public class MpvTimestampMillisecs
    extends AbstractMpvTimestamp {

  /**
   * Constructor.
   */
  public MpvTimestampMillisecs() {
    addPart( new Part( "HH", 2, new IntRange( 0, 23 ), ' ', ':' ) ); //$NON-NLS-1$
    addPart( new Part( "MM mins", 2, new IntRange( 0, 59 ), -1, ':' ) ); //$NON-NLS-1$
    addPart( new Part( "SS", 2, new IntRange( 0, 59 ), -1, -1 ) ); //$NON-NLS-1$
    addPart( new Part( "MMM μsecs", 3, new IntRange( 0, 999 ), '.', -1 ) ); //$NON-NLS-1$
    setAsDatetime( LocalDateTime.of( 2023, 7, 1, 12, 0, 0, 0 ) );
  }

  // ------------------------------------------------------------------------------------
  // IMpvTimestamp
  //

  @Override
  public LocalDateTime getAsDatetime() {
    LocalDate ld = LocalDate.of( pval( IDX_YEAR ), pval( IDX_MONTH ), pval( IDX_DAY ) );
    LocalTime lt = LocalTime.of( pval( IDX_HOUR ), pval( IDX_MIN ), pval( IDX_SEC ), //
        NANOS_PER_MSEC * pval( IDX_MSEC ) );
    return LocalDateTime.of( ld, lt );
  }

  @Override
  public void setAsDatetime( LocalDateTime aTimestamp ) {
    TsNullArgumentRtException.checkNull( aTimestamp );
    LocalDateTime ldt = aTimestamp;
    if( ldt.isBefore( MIN_LDT_OF_WIDEST_RANGE ) ) {
      ldt = MIN_LDT_OF_WIDEST_RANGE;
    }
    if( ldt.isAfter( MAX_LDT_OF_WIDEST_RANGE ) ) {
      ldt = MAX_LDT_OF_WIDEST_RANGE;
    }
    IIntList oldVals = getPartsValues();
    sval( IDX_YEAR, ldt.getYear() );
    sval( IDX_MONTH, ldt.getMonthValue() );
    sval( IDX_DAY, ldt.getDayOfMonth() );
    sval( IDX_HOUR, ldt.getHour() );
    sval( IDX_MIN, ldt.getMinute() );
    sval( IDX_SEC, ldt.getSecond() );
    sval( IDX_MSEC, ldt.getNano() / NANOS_PER_MSEC );
    IIntList newVals = getPartsValues();
    if( !newVals.equals( oldVals ) ) {
      eventer().fireChangeEvent();
    }
  }

}
