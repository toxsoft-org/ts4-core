package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Y шкала для отображения действительных значений.
 *
 * @author vs
 */
public class FloatingAxisView
    extends YAxisView {

  private double startValue;
  private double endValue;
  private double valueOfDivision;

  /**
   * Конструктор.
   *
   * @param aId String - ИД шкалы
   * @param aDescr String - описание (для человека) шкалы
   * @param aAxisModel YAxisModel - модель шкалы
   */
  public FloatingAxisView( String aId, String aDescr, YAxisModel aAxisModel ) {
    super( aId, aDescr, aAxisModel );

    IYAxisDef axisDef = (IYAxisDef)aAxisModel.axisDef();

    startValue = axisDef.initialStartValue().asDouble();
    endValue = axisDef.initialEndValue().asDouble();
    valueOfDivision = axisDef.initialUnitValue().asDouble();
    // ticksQtty = (int)Math.round( (endValue - startValue) / valueOfDivision ) + 1;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов YAxisView
  //

  @Override
  protected IList<Pair<IAtomicValue, Integer>> doListAnnotationValues( int aHeight ) {
    IListEdit<Pair<IAtomicValue, Integer>> result = new ElemArrayList<>();
    double tickDelta = unitValue();
    double y = 100 - normalizeValue( axisModel().startMarkingValue() );
    double currValue = axisModel().startMarkingValue().asDouble();

    while( y >= 0 ) {
      Pair<IAtomicValue, Integer> p;
      int screenCoord = normToScreen( y );
      if( screenCoord < aHeight ) {
        p = new Pair<>( AvUtils.avFloat( currValue ), Integer.valueOf( screenCoord ) );
        result.add( p );
      }
      currValue += valueOfDivision;
      y -= tickDelta;
    }
    return result;
  }

  @Override
  double normalizeValue( IAtomicValue aValue ) {
    double value = aValue.asFloat();
    // double v1 = value - startValue;
    // double v2 = endValue - startValue;
    return ((value - startValue) / (endValue - startValue)) * 100;
  }

  @Override
  public IList<Pair<Double, ETickType>> calcTickPositions() {

    IListEdit<Pair<Double, ETickType>> tickPositions = new ElemArrayList<>();

    double tickDelta = unitValue() / axisModel.axisMarkingDef().bigTickNumber();
    IAtomicValue val = axisModel().startMarkingValue();
    double x = normalizeValue( val );

    // System.out.print( "StartMarking value = " + axisModel().startMarkingValue().asDouble() + "; " );
    // System.out.print( "Start value = " + axisModel().startValue().asDouble() + "; " );
    // System.out.print( "End value = " + axisModel().endValue().asDouble() + "; " );
    // System.out.println( "Norm Y = " + x );

    int idx = 0;
    while( x <= 100 ) {
      if( x >= 0 ) {
        Pair<Double, ETickType> pair = new Pair<>( Double.valueOf( x ), axisModel.axisMarkingDef().tickType( idx ) );
        tickPositions.add( pair );
      }
      x += tickDelta;
      idx++;
    }
    return tickPositions;
  }

  @Override
  double unitValue() {
    return (valueOfDivision * 100) / (endValue - startValue);
  }

  @Override
  int normToScreen( double aNormVal ) {
    return G2ChartUtils.normToScreen( aNormVal, markingBounds().height() ) + markingBounds().y1();
  }

  @Override
  IAtomicValue normToValue( double aNormVal ) {
    return AvUtils.avFloat( startValue + ((endValue - startValue) * aNormVal) / 100. );
  }

  @Override
  protected void onModelChanged() {
    startValue = axisModel().startValue().asDouble();
    endValue = axisModel().endValue().asDouble();
    valueOfDivision = axisModel().unitValue().asDouble();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeListener
  //

  @Override
  public void onGenericChangeEvent( Object aSource ) {
    // Sol++
    onModelChanged(); // ?????
  }

}
