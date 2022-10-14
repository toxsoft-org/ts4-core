package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class XAxisView
    extends G2AxisViewBase {

  XAxisModel xAxisModel;
  int        ticksQtty = 11;
  ETimeUnit  timeUnit;

  public XAxisView( String aId, String aDescription, XAxisModel aAxisModel, EBorderLayoutPlacement aPlace ) {
    super( aId, TsLibUtils.EMPTY_STRING, aDescription, EG2ChartElementKind.X_AXIS, aAxisModel );
    xAxisModel = aAxisModel;
    timeUnit = xAxisModel.timeUnit();

    place = aPlace;
    long startTime = xAxisModel.timeInterval().startTime();
    long endTime = xAxisModel.timeInterval().endTime();

    long deltaTime = ((IXAxisDef)aAxisModel.axisDef()).initialTimeUnit().timeInMills();

    ticksQtty = Math.round( (endTime - startTime) / deltaTime ) + 1;
  }

  @Override
  public XAxisModel axisModel() {
    return xAxisModel;
  }

  @Override
  public void onGenericChangeEvent( Object aSource ) {
    if( timeUnit != xAxisModel.timeUnit() ) {
      timeUnit = xAxisModel.timeUnit();
      System.out.println( "TimeUnit changed" );
    }
  }

  // @Override
  // public void onGenericChanged( Object aSource ) {
  // if( timeUnit != xAxisModel.timeUnit() ) {
  // timeUnit = xAxisModel.timeUnit();
  // System.out.println( "TimeUnit changed" );
  // }
  // }

  @Override
  protected IList<Pair<IAtomicValue, Integer>> doListAnnotationValues( int aLenth ) {
    IListEdit<Pair<IAtomicValue, Integer>> result = new ElemArrayList<>();

    IList<Pair<Double, ETickType>> ticks = calcTickPositions();
    double x = Double.NaN;
    long currValue = xAxisModel.startMarkingValue().asLong();
    long deltaTime = xAxisModel.timeUnit().timeInMills();
    for( Pair<Double, ETickType> p : ticks ) {
      switch( p.right() ) {
        case BIG:
          if( x == Double.NaN ) {
            x = normalizeValue( xAxisModel.startMarkingValue() );
          }
          else {
            currValue += deltaTime;
            x += deltaTime;
          }
          Pair<IAtomicValue, Integer> pair;
          int screenX = normToScreen( p.left() );
          pair = new Pair<>( AvUtils.avTimestamp( currValue ), Integer.valueOf( screenX ) );
          result.add( pair );
          break;
        case LITTLE:
          break;
        case MIDDLE:
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }

    // // double tickDelta = 100. / axisModel.axisMarkingDef().bigTickNumber();
    // double tickDelta = 100. / (ticksQtty - 1);
    // // double x = firstTickIndent;
    // double x = normalizeValue( xAxisModel.startMarkingValue() );
    //
    // // long currValue = xAxisModel.timeInterval().startTime();
    // long currValue = xAxisModel.startMarkingValue().asLong();
    // long deltaTime = xAxisModel.timeUnit().timeInMills();
    //
    // while( x < 100 + tickDelta ) {
    // if( x >= 0 ) {
    // Pair<IAtomicValue, Integer> p;
    // // int screenX = G2ChartUtils.normToScreen( x, markingBounds().width() ) + markingBounds().x1();
    // int screenX = normToScreen( x );
    // p = new Pair<>( AvUtils.avTimestamp( currValue ), Integer.valueOf( screenX ) );
    // result.add( p );
    // }
    // currValue += deltaTime;
    // x += tickDelta;
    // }
    return result;
  }

  @Override
  double normalizeValue( IAtomicValue aValue ) {
    long value = aValue.asLong();
    long sTime = xAxisModel.timeInterval().startTime();
    long eTime = xAxisModel.timeInterval().endTime();
    return ((value - sTime) * 100.) / (eTime - sTime);
  }

  @Override
  double unitValue() {
    long startTime = xAxisModel.timeInterval().startTime();
    long endTime = xAxisModel.timeInterval().endTime();
    long deltaTime = xAxisModel.timeUnit().timeInMills();
    return (deltaTime * 100D) / (endTime - startTime);
  }

  @Override
  int normToScreen( double aNormVal ) {
    return G2ChartUtils.normToScreen( aNormVal, markingBounds().width() ) + markingBounds().x1();
  }

  @Override
  IAtomicValue normToValue( double aNormVal ) {
    long startTime = xAxisModel.timeInterval().startTime();
    long endTime = xAxisModel.timeInterval().endTime();
    return AvUtils.avTimestamp( Math.round( startTime + ((endTime - startTime) * aNormVal) / 100. ) );
  }

  @Override
  protected void onModelChanged() {
    long startTime = xAxisModel.timeInterval().startTime();
    long endTime = xAxisModel.timeInterval().endTime();
    long deltaTime = xAxisModel.timeUnit().timeInMills();
    ticksQtty = Math.round( (endTime - startTime) / deltaTime ) + 1;
  }

}
