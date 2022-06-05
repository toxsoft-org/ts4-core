package org.toxsoft.core.tsgui.widgets.mpv.impl;

import java.time.*;

import org.toxsoft.core.tsgui.widgets.mpv.IMpvTimestamp;
import org.toxsoft.core.tslib.bricks.time.ITimeInterval;
import org.toxsoft.core.tslib.bricks.time.impl.TimeInterval;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.math.IntRange;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpvTimestamp} implementation in form "YYYY-MM-DD HH:MM:SS.uuu".
 *
 * @author hazard157
 */
public abstract class AbstractMpvTimestamp
    extends MultiPartValueBase
    implements IMpvTimestamp {

  protected static final int IDX_YEAR  = 0;
  protected static final int IDX_MONTH = 1;
  protected static final int IDX_DAY   = 2;
  protected static final int IDX_HOUR  = 3;
  protected static final int IDX_MIN   = 4;
  protected static final int IDX_SEC   = 5;
  protected static final int IDX_MSEC  = 6;

  protected static final int NANOS_PER_MSEC = 1_000_000;

  protected static final LocalDateTime MIN_LDT_OF_WIDEST_RANGE = LocalDateTime.of( //
      LocalDate.of( 0, 1, 1 ), //
      LocalTime.of( 0, 0, 0, 0 ) //
  );

  protected static final LocalDateTime MAX_LDT_OF_WIDEST_RANGE = LocalDateTime.of( //
      LocalDate.of( 9999, 12, 31 ), //
      LocalTime.of( 23, 59, 59, 999 ) //
  );

  protected static final ITimeInterval WIDEST_RANGE =
      new TimeInterval( MIN_LDT_OF_WIDEST_RANGE, MAX_LDT_OF_WIDEST_RANGE );

  private ITimeInterval range = WIDEST_RANGE;

  /**
   * Constructor.
   * <p>
   * Adds parts for "YYYY-MM-DD" with indexes {@link #IDX_YEAR}, {@link #IDX_MONTH}, {@link #IDX_DAY}.
   */
  public AbstractMpvTimestamp() {
    addPart( new Part( "YYYY", 4, new IntRange( 0, 9999 ), -1, '-' ) ); //$NON-NLS-1$
    addPart( new Part( "MM months", 2, new IntRange( 1, 12 ), -1, '-' ) ); //$NON-NLS-1$
    addPart( new Part( "DD", 2, new IntRange( 1, 31 ), -1, -1 ) ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // MultiPartValueBase
  //

  @Override
  protected int doGetPartMaxValue( int aPartIndex ) {
    switch( aPartIndex ) {
      case IDX_DAY: { // вернем кол-во дней в месяце YYYY-MM
        YearMonth yearMonthObject = YearMonth.of( pval( IDX_YEAR ), pval( IDX_MONTH ) );
        return yearMonthObject.lengthOfMonth();
      }
      default:
        return super.doGetPartMaxValue( aPartIndex );
    }
  }

  @Override
  protected int doGetPartMinValue( int aPartIndex ) {
    return super.doGetPartMinValue( aPartIndex );
  }

  @Override
  void doProcessOverflow( int aPartIndex, int aNewValue, int aOverflow ) {
    super.doProcessOverflow( aPartIndex, aNewValue, aOverflow );
  }

  @Override
  protected ValidationResult doValidateValuesSet( IIntList aPartValues ) {
    return super.doValidateValuesSet( aPartValues );
  }

  // ------------------------------------------------------------------------------------
  // IMpvTimestamp
  //

  @Override
  final public long getAsTimestamp() {
    LocalDateTime ldt = getAsDatetime();
    Instant instant = ldt.toInstant( ZoneOffset.UTC );
    return instant.toEpochMilli();
  }

  @Override
  final public void setAsTimestamp( long aTimestamp ) {
    long val = aTimestamp;
    if( val < range.startTime() ) {
      val = range.startTime();
    }
    if( val > range.endTime() ) {
      val = range.endTime();
    }
    Instant instant = Instant.ofEpochMilli( val );
    LocalDateTime ldt = LocalDateTime.ofInstant( instant, ZoneOffset.UTC );
    setAsDatetime( ldt );
  }

  @Override
  public abstract LocalDateTime getAsDatetime();

  @Override
  public abstract void setAsDatetime( LocalDateTime aTimestamp );

  @Override
  final public ITimeInterval getInterval() {
    return range;
  }

  @Override
  final public void setInterval( ITimeInterval aInterval ) {
    TsNullArgumentRtException.checkNull( aInterval );
    long t1 = aInterval.startTime();
    if( t1 < WIDEST_RANGE.startTime() ) {
      t1 = WIDEST_RANGE.startTime();
    }
    long t2 = aInterval.endTime();
    if( t2 > WIDEST_RANGE.endTime() ) {
      t2 = WIDEST_RANGE.endTime();
    }
    range = new TimeInterval( t1, t2 );
    long val = getAsTimestamp();
    if( val < range.startTime() ) {
      setAsDatetime( range.getStartDatetime() );
      return;
    }
    if( val > range.startTime() ) {
      setAsDatetime( range.getEndDatetime() );
    }
  }

  @Override
  public ITimeInterval getWidestInterval() {
    return WIDEST_RANGE;
  }

}
