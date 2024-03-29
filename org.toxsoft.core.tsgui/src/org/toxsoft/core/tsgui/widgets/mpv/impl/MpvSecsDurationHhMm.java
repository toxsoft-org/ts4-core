package org.toxsoft.core.tsgui.widgets.mpv.impl;

import org.toxsoft.core.tsgui.widgets.mpv.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.*;

/**
 * {@link IMpvLocalTime} implementation in form "HH:MM:SS.mmm".
 *
 * @author hazard157
 */
public class MpvSecsDurationHhMm
    extends MultiPartValueBase
    implements IMpvSecsDuration {

  protected static final int IDX_HOUR = 0;
  protected static final int IDX_MIN  = 1;

  private static final IntRange WIDEST_RANGE = new IntRange( 0, 99 * 3600 + 59 * 60 + 0 );

  private IntRange range = WIDEST_RANGE;

  /**
   * Constructor.
   */
  public MpvSecsDurationHhMm() {
    addPart( new Part( "HH", 2, new IntRange( 0, 99 ), -1, ':' ) ); //$NON-NLS-1$
    addPart( new Part( "MM", 2, new IntRange( 0, 59 ), -1, -1 ) ); //$NON-NLS-1$
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
  protected void doProcessOverflow( int aPartIndex, int aNewValue, int aOverflow ) {
    super.doProcessOverflow( aPartIndex, aNewValue, aOverflow );
  }

  @Override
  protected ValidationResult doValidateValuesSet( IIntList aPartValues ) {
    return super.doValidateValuesSet( aPartValues );
  }

  // ------------------------------------------------------------------------------------
  // IMpvSecsDuration
  //

  @Override
  public int getValueSecs() {
    return pval( IDX_HOUR ) * 3600 + pval( IDX_MIN ) * 60;
  }

  @Override
  public void setValueSecs( int aDuration ) {
    int dur = range.inRange( aDuration );
    int hh = dur / 3600;
    dur -= hh * 3600;
    int mm = dur / 60;
    IIntList oldVals = getPartsValues();
    sval( IDX_HOUR, hh );
    sval( IDX_MIN, mm );
    IIntList newVals = getPartsValues();
    if( !newVals.equals( oldVals ) ) {
      eventer().fireChangeEvent();
    }
  }

  @Override
  public IntRange getWidestRange() {
    return WIDEST_RANGE;
  }

  @Override
  public IntRange getRange() {
    return range;
  }

  @Override
  public void setRange( IntRange aRange ) {
    range = WIDEST_RANGE.inRange( aRange );
    int val = getValueSecs();
    if( !range.isInRange( val ) ) {
      setValueSecs( val );
    }
  }

}
