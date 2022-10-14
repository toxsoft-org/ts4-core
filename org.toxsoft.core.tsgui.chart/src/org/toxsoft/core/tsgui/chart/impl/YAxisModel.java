package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;

/**
 * Модель Y шкалы.
 * <p>
 * Изменямый класс, который хранит все текущие параметры шкалы.
 *
 * @author vs
 */
public class YAxisModel
    extends AxisModelBase {

  private final IYAxisDef yAxisDef;

  private IAtomicValue startValue;
  private IAtomicValue endValue;
  private IAtomicValue unitValue;

  private IAtomicValue startMarkingValue;

  /**
   * Конструктор.
   *
   * @param aAxisDef IYAxisDef - описание шкалы
   */
  public YAxisModel( IYAxisDef aAxisDef ) {
    super( aAxisDef );
    yAxisDef = aAxisDef;
    startValue = yAxisDef.initialStartValue();
    endValue = yAxisDef.initialEndValue();
    unitValue = yAxisDef.initialUnitValue();
    startMarkingValue = startValue;
  }

  IYAxisDef yAxisDef() {
    return yAxisDef;
  }

  IAtomicValue startValue() {
    return startValue;
  }

  IAtomicValue endValue() {
    return endValue;
  }

  IAtomicValue unitValue() {
    return unitValue;
  }

  @Override
  IAtomicValue startMarkingValue() {
    return startMarkingValue;
  }

  void setStartValue( IAtomicValue aStartValue ) {
    startValue = aStartValue;
  }

  void setEndValue( IAtomicValue aEndValue ) {
    endValue = aEndValue;
  }

  void setUnitValue( IAtomicValue aUnitValue ) {
    double scaleFactor = aUnitValue.asDouble() / unitValue.asDouble();
    double delta = (endValue.asDouble() - startValue.asDouble()) * scaleFactor;
    unitValue = aUnitValue;
    endValue = AvUtils.avFloat( startValue.asDouble() + delta );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AxisModelBase
  //

  @Override
  protected void doLocate( IAtomicValue aStartValue ) {
    setStartValue( aStartValue );
  }

  @Override
  protected void doSetRange( IAtomicValue aStartValue, IAtomicValue aEndValue ) {
    setStartValue( aStartValue );
    setEndValue( aEndValue );
  }

  @Override
  protected void doShiftAndScale( double aShift, double aScale ) {
    double shiftValue = normalToValue( aShift ).asDouble();

    startValue = AvUtils.avFloat( startValue.asDouble() + shiftValue );
    endValue = AvUtils.avFloat( endValue.asDouble() + shiftValue );
    setUnitValue( AvUtils.avFloat( unitValue.asDouble() * aScale ) );

    int ticksCount = (int)(startValue.asDouble() / unitValue.asDouble());
    startMarkingValue = AvUtils.avFloat( ticksCount * unitValue.asDouble() );
    if( startMarkingValue.asDouble() > startValue.asDouble() ) {
      startMarkingValue = AvUtils.avFloat( startMarkingValue.asDouble() - unitValue.asDouble() );
    }
    System.out.println( "shift = " + aShift ); //$NON-NLS-1$
    System.out.println( "shiftValue = " + shiftValue ); //$NON-NLS-1$
    System.out.println( "StartValue = " + startValue.asFloat() ); //$NON-NLS-1$
    System.out.println( "StartMarkingValue = " + startMarkingValue.asFloat() ); //$NON-NLS-1$
  }

  @Override
  IAtomicValue normalToValue( double aNormalValue ) {
    if( ((YAxisDef)axisDef()).valueType() == EAtomicType.FLOATING ) {
      double sVal = startValue.asDouble();
      double eVal = endValue.asDouble();
      double value = ((eVal - sVal) / 100) * aNormalValue;
      return AvUtils.avFloat( value );
    }
    return null;
  }

  // // ------------------------------------------------------------------------------------
  // // Внутренняя реализация
  // //
  //
  // IAtomicValue normalValueToValue(double aNormalValue) {
  // double sVal = startValue.asDouble();
  // double eVal = endValue.asDouble();
  // return AvUtils.avFloat( aNormalValue );
  // }
}
