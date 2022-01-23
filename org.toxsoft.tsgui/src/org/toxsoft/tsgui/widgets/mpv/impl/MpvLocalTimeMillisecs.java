package org.toxsoft.tsgui.widgets.mpv.impl;

import java.time.LocalTime;

import org.toxsoft.tsgui.widgets.mpv.IMpvLocalTime;
import org.toxsoft.tslib.coll.primtypes.IIntList;
import org.toxsoft.tslib.math.IntRange;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpvLocalTime} implementation in form "HH:MM:SS.mmm".
 *
 * @author goga
 */
public class MpvLocalTimeMillisecs
    extends AbstractMpvLocalTime {

  /**
   * Constructor.
   */
  public MpvLocalTimeMillisecs() {
    addPart( new Part( "SS", 2, new IntRange( 0, 59 ), -1, -1 ) ); //$NON-NLS-1$
    addPart( new Part( "mmm", 3, new IntRange( 0, 999 ), '.', -1 ) ); //$NON-NLS-1$
    setAsLocalTime( LocalTime.of( 0, 0, 0, 0 ) );
  }

  // ------------------------------------------------------------------------------------
  // IMpvTimestamp
  //

  @Override
  public LocalTime getAsLocalTime() {
    return LocalTime.of( pval( IDX_HOUR ), pval( IDX_MIN ), pval( IDX_SEC ), pval( IDX_MSEC ) );
  }

  @Override
  public void setAsLocalTime( LocalTime aTimestamp ) {
    TsNullArgumentRtException.checkNull( aTimestamp );
    LocalTime lt = aTimestamp;
    if( lt.isBefore( MIN_TIME ) ) {
      lt = MIN_TIME;
    }
    if( lt.isAfter( MAX_TIME ) ) {
      lt = MAX_TIME;
    }
    IIntList oldVals = getPartsValues();
    sval( IDX_HOUR, lt.getHour() );
    sval( IDX_MIN, lt.getMinute() );
    sval( IDX_SEC, lt.getMinute() );
    sval( IDX_MSEC, lt.getMinute() );
    IIntList newVals = getPartsValues();
    if( !newVals.equals( oldVals ) ) {
      eventer().fireChangeEvent();
    }
  }

}
