package org.toxsoft.core.tsgui.chart.impl;

import java.util.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.tsgui.chart.api.*;

public class RandomFloatingDataSet
    extends Stridable
    implements IG2DataSet {

  IListEdit<ITemporalAtomicValue> values = new ElemArrayList<>();

  int    valuesCount = 10;
  Double minValue    = 0.;
  Double maxValue    = 100.;

  public RandomFloatingDataSet( String aId ) {
    super( aId, TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING, true );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2DataSet
  //

  @Override
  public IList<ITemporalAtomicValue> getValues( ITimeInterval aInterval ) {
    return values;
  }

  @Override
  public void prepare( ITimeInterval aInterval ) {
    values.clear();
    long dt = aInterval.endTime() - aInterval.startTime();
    long step = dt / valuesCount;
    // FIXME long currTime = aInterval.startTime() + 25000;
    long currTime = aInterval.startTime();
    Random rnd = new Random();
    for( int i = 0; i < valuesCount; i++ ) {
      Double dv = minValue + rnd.nextFloat() * (maxValue - minValue);
      ITemporalAtomicValue value = new TemporalAtomicValue( currTime, AvUtils.avFloat( dv ) );
      values.add( value );
      currTime += step;
      // currTime = aInterval.startTime() + (i + 1) * step;
    }

    values.add( new TemporalAtomicValue( aInterval.endTime(), AvUtils.avFloat( 20 ) ) );
  }

  @Override
  public Pair<ITemporalAtomicValue, ITemporalAtomicValue> locate( long aTimeStamp ) {
    // FIXME locate
    return new Pair<>( ITemporalAtomicValue.NULL, ITemporalAtomicValue.NULL );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ICloseable
  //

  @Override
  public void close() {
    // TODO Auto-generated method stub

  }

}
