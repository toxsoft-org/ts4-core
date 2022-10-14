package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Модель временной шкалы.
 * <p>
 * Изменяемый класс, который содержит текущие значения параметров шкалы.
 *
 * @author vs
 */
public class XAxisModel
    extends AxisModelBase {

  private final IXAxisDef xAxisDef;
  private TimeInterval    timeInterval;
  private ETimeUnit       timeUnit;

  private long startMarkingValue;

  public XAxisModel( IXAxisDef aAxisDef ) {
    super( aAxisDef );
    xAxisDef = aAxisDef;
    ITimeInterval ti = xAxisDef.initialTimeInterval();
    timeInterval = new TimeInterval( ti.startTime(), ti.endTime() );
    timeUnit = xAxisDef.initialTimeUnit();
    startMarkingValue = computeStartMarkingValue();
    doShiftAndScale( (ti.startTime() - startMarkingValue) * 100 / (ti.endTime() - ti.startTime()), 1 );
    // Sol++ print date
    // G2ChartUtils.printTimeInterval( timeInterval, "XAxisModel конструктор: " );
  }

  IXAxisDef xAxisDef() {
    return xAxisDef;
  }

  public ITimeInterval timeInterval() {
    return timeInterval;
  }

  ETimeUnit timeUnit() {
    return timeUnit;
  }

  void setTimeInterval( ITimeInterval aTimeInterval ) {
    timeInterval = new TimeInterval( aTimeInterval.startTime(), aTimeInterval.endTime() );
    startMarkingValue = computeStartMarkingValue();
  }

  void setTimeUnit( ETimeUnit aTimeUnit ) {
    double koeff;
    if( aTimeUnit.timeInMills() > timeUnit.timeInMills() ) {
      koeff = aTimeUnit.timeInMills() / timeUnit.timeInMills();
    }
    else {
      koeff = 1. / (timeUnit.timeInMills() / aTimeUnit.timeInMills());
    }
    timeUnit = aTimeUnit;
    // long duration = Math.round( timeInterval.duration() * koeff );
    long duration = Math.round( 10 * timeUnit.timeInMills() );
    timeInterval = new TimeInterval( timeInterval.startTime(), timeInterval.startTime() + duration );
    startMarkingValue = computeStartMarkingValue();
    markingDef = new AxisMarkingDef( markingDef, timeUnit.midTicksQtty(), timeUnit.littTicksQtty() );
    fireChangeEvent();
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AxisModelBase
  //

  @Override
  protected void doLocate( IAtomicValue aStartValue ) {
    setTimeInterval( new TimeInterval( aStartValue.asLong(), aStartValue.asLong() + timeInterval.duration() ) );
  }

  @Override
  protected void doSetRange( IAtomicValue aStartValue, IAtomicValue aEndValue ) {
    setTimeInterval( new TimeInterval( aStartValue.asLong(), aEndValue.asLong() ) );
  }

  @Override
  protected void doShiftAndScale( double aShift, double aScale ) {
    long shiftValue = normalToValue( aShift ).asLong();

    long startValue = timeInterval.startTime() + shiftValue;
    long endValue = timeInterval.endTime() + shiftValue;
    timeInterval = new TimeInterval( startValue, endValue );

    // int ticksCount = (int)(startValue / timeUnit.timeInMills());
    // startMarkingValue = ticksCount * timeUnit.timeInMills();
    // if( startMarkingValue > startValue ) {
    // startMarkingValue = startMarkingValue - timeUnit.timeInMills();
    // }

    startMarkingValue = computeStartMarkingValue();
    System.out.println( String.format( "Start marking value =  %1$tF %1$tT", startMarkingValue ) );
  }

  @Override
  IAtomicValue normalToValue( double aNormalValue ) {
    long value = Math.round( ((timeInterval.endTime() - timeInterval.startTime()) / 100) * aNormalValue );
    return AvUtils.avTimestamp( value );
  }

  @Override
  IAtomicValue startMarkingValue() {
    return AvUtils.avTimestamp( startMarkingValue );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  long computeStartMarkingValue() {
    long ticksCount = timeInterval.startTime() / timeUnit.timeInMills();
    long smValue = ticksCount * timeUnit.timeInMills();
    // if( smValue > timeInterval.startTime() ) {
    while( smValue > timeInterval.startTime() ) {
      smValue = smValue - timeUnit.timeInMills();
    }
    return smValue;
  }

}
