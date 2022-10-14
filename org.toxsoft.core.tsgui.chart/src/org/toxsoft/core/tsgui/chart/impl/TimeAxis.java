package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.tsgui.chart.api.*;

public class TimeAxis
    extends G2Axis {

  private final IXAxisDef axisDef;

  long startTime;
  long endTime;
  long deltaTime;

  TimeAxis( Composite aCanvas, IXAxisDef aAxisDef ) {
    super( aCanvas, aAxisDef );
    axisDef = aAxisDef;
    startTime = axisDef.initialTimeInterval().startTime();
    endTime = axisDef.initialTimeInterval().endTime();
    ticksQtty++;
    deltaTime = (endTime - startTime) / ticksQtty;
    endTime = startTime + deltaTime * ticksQtty;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов G2Axis
  //

  @Override
  public String elementId() {
    return id();
  }

  @Override
  public EG2ChartElementKind elementKind() {
    return EG2ChartElementKind.X_AXIS;
  }

  @Override
  protected IList<Pair<IAtomicValue, Integer>> doListAnnotationValues() {
    IListEdit<Pair<IAtomicValue, Integer>> result = new ElemArrayList<>();
    double tickDelta = 100. / axisMarking.bigTickNumber();
    double x = 0;

    long currValue = startTime;

    while( x < 100 + tickDelta ) {
      Pair<IAtomicValue, Integer> p;
      int screenX = G2ChartUtils.normToScreen( x, markingBounds().width() ) + markingBounds().x1();
      p = new Pair<>( AvUtils.avTimestamp( currValue ), Integer.valueOf( screenX ) );
      result.add( p );
      currValue += deltaTime;
      x += tickDelta;
    }
    return result;
  }

  @Override
  String startValueToString( String aFormat ) {
    String format = aFormat;
    if( aFormat.isEmpty() ) {
      format = null;
    }
    // FIXME return DvUtils.printDv( format, DvUtils.avTimestamp( startTime ) );
    return "February Февраль";
  }

  @Override
  String endValueToString( String aFormat ) {
    String format = aFormat;
    if( aFormat.isEmpty() ) {
      format = null;
    }
    // FIXME return DvUtils.printDv( format, DvUtils.avTimestamp( endTime ) );
    return "February Февраль";
  }

  // ------------------------------------------------------------------------------------
  // Публичный API
  //

  public TimeInterval timeInterval() {
    return new TimeInterval( startTime, endTime );
  }

  @Override
  double normalizeValue( IAtomicValue aValue ) {
    long value = aValue.asLong();
    // long dt = (endTime - startTime);// / 1000;
    // long dv = (value - startTime);// / 1000;
    // long result = (dv * 100) / dt;
    return ((value - startTime) * 100) / (endTime - startTime);
  }

}
