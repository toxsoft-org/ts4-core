package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.tsgui.chart.api.*;

public class G2HistoryDataSet
    extends Stridable
    implements IG2DataSet {

  IList<ITemporalAtomicValue> values = IList.EMPTY;

  public G2HistoryDataSet( String aId ) {
    super( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING, true );
  }

  public void setValues( IList<ITemporalAtomicValue> aValues ) {
    values = aValues;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //

  @Override
  public IList<ITemporalAtomicValue> getValues( ITimeInterval aInterval ) {
    return values;
  }

  @Override
  public void prepare( ITimeInterval aInterval ) {
    // TODO Auto-generated method stub

  }

  @Override
  public Pair<ITemporalAtomicValue, ITemporalAtomicValue> locate( long aTimeStamp ) {
    ITemporalAtomicValue leftVal = ITemporalAtomicValue.NULL;
    ITemporalAtomicValue rightVal = ITemporalAtomicValue.NULL;
    for( ITemporalAtomicValue value : values ) {
      if( value.timestamp() <= aTimeStamp ) {
        leftVal = value;
      }
      else {
        rightVal = value;
        break;
      }
    }
    return new Pair<>( leftVal, rightVal );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

}
