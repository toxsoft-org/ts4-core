package org.toxsoft.core.tsgui.widgets.mpv.impl;

import java.time.LocalTime;

import org.toxsoft.core.tsgui.widgets.mpv.IMpvLocalTime;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IMpvLocalTime} implementation in form "HH:MM".
 *
 * @author hazard157
 */
public class MpvLocalTimeMinutes
    extends AbstractMpvLocalTime {

  /**
   * Constructor.
   */
  public MpvLocalTimeMinutes() {
    setAsLocalTime( LocalTime.of( 0, 0, 0, 0 ) );
  }

  // ------------------------------------------------------------------------------------
  // IMpvTimestamp
  //

  @Override
  public LocalTime getAsLocalTime() {
    return LocalTime.of( pval( IDX_HOUR ), pval( IDX_MIN ), 0, 0 );
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
    IIntList newVals = getPartsValues();
    if( !newVals.equals( oldVals ) ) {
      eventer().fireChangeEvent();
    }
  }

}
