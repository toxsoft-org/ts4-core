package org.toxsoft.core.tsgui.widgets.mpv.impl;

import java.time.LocalTime;

import org.toxsoft.core.tsgui.widgets.mpv.IMpvLocalTime;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.math.IntRange;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpvLocalTime} implementation in form "HH:MM:SS.mmm".
 *
 * @author goga
 */
public abstract class AbstractMpvLocalTime
    extends MultiPartValueBase
    implements IMpvLocalTime {

  protected static final int IDX_HOUR = 0;
  protected static final int IDX_MIN  = 1;
  protected static final int IDX_SEC  = 2;
  protected static final int IDX_MSEC = 3;

  protected static final int NANOS_PER_MSEC = 1_000_000;

  protected static final LocalTime MIN_TIME      = LocalTime.of( 0, 0, 0, 0 );
  protected static final LocalTime MAX_TIME      = LocalTime.of( 23, 59, 59, 999 );
  protected static final int       MIN_SECS      = 0;
  protected static final int       MAX_SECS      = 86_400 - 1;
  protected static final int       MIN_MILLISECS = 0;
  protected static final int       MAX_MILLISECS = 86_400_000 - 1;

  private static final IntRange WIDEST_RANGE_SECS      = new IntRange( MIN_SECS, MAX_SECS );
  private static final IntRange WIDEST_RANGE_MILLISECS = new IntRange( MIN_MILLISECS, MAX_MILLISECS );

  private IntRange rangeSecs      = WIDEST_RANGE_SECS;
  private IntRange rangeMillisecs = WIDEST_RANGE_MILLISECS;

  /**
   * Constructor.
   * <p>
   * Adds parts for "HH:MM:DD" with indexes {@link #IDX_HOUR} and {@link #IDX_MIN}.
   */
  public AbstractMpvLocalTime() {
    addPart( new Part( "HH", 2, new IntRange( 0, 23 ), -1, ':' ) ); //$NON-NLS-1$
    addPart( new Part( "MM", 2, new IntRange( 0, 59 ), -1, ':' ) ); //$NON-NLS-1$
  }

  // ------------------------------------------------------------------------------------
  // MultiPartValueBase
  //

  @Override
  protected int doGetPartMaxValue( int aPartIndex ) {
    return super.doGetPartMaxValue( aPartIndex );
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
  // IMpvLocalTime
  //

  @Override
  public int getAsDayMillisecs() {
    LocalTime lt = getAsLocalTime();
    return lt.toSecondOfDay() + (lt.getNano() / NANOS_PER_MSEC);
  }

  @Override
  public void setAsDayMillisecs( int aDayMillisecs ) {
    int val = rangeSecs.inRange( aDayMillisecs );
    setAsLocalTime( LocalTime.ofNanoOfDay( val * NANOS_PER_MSEC ) );
  }

  @Override
  public int getAsDaySeconds() {
    LocalTime lt = getAsLocalTime();
    return lt.toSecondOfDay();
  }

  @Override
  public void setAsDaySeconds( int aDaySeconds ) {
    int val = rangeMillisecs.inRange( aDaySeconds );
    setAsLocalTime( LocalTime.ofSecondOfDay( val ) );
  }

  @Override
  public IntRange getRangeSecs() {
    return rangeSecs;
  }

  @Override
  public void setRangeSecs( IntRange aRange ) {
    TsNullArgumentRtException.checkNull( aRange );
    rangeSecs = WIDEST_RANGE_SECS.inRange( aRange );
    rangeMillisecs = new IntRange( rangeSecs.minValue() * 1000, rangeSecs.maxValue() * 1000 );
    int oldMillisecs = getAsDayMillisecs();
    int newMillisecs = rangeMillisecs.inRange( oldMillisecs );
    if( newMillisecs != oldMillisecs ) {
      setAsDayMillisecs( newMillisecs );
    }
  }

  @Override
  public abstract LocalTime getAsLocalTime();

  @Override
  public abstract void setAsLocalTime( LocalTime aLocalTime );

}
