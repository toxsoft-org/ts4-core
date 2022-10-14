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
public class IntegerAxisView
    extends YAxisView {

  private int startValue;
  private int endValue;
  double      valueOfDivision;

  public IntegerAxisView( String aId, String aDescr, YAxisModel aAxisModel ) {
    super( aId, aDescr, aAxisModel );

    IYAxisDef axisDef = (IYAxisDef)aAxisModel.axisDef();

    startValue = (int)axisDef.initialStartValue().asDouble();
    endValue = (int)axisDef.initialEndValue().asDouble();
    valueOfDivision = axisDef.initialUnitValue().asDouble();
    // ticksQtty = Math.round( (endValue - startValue) / valueOfDivision ) + 1;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов YAxisView
  //

  @Override
  protected IList<Pair<IAtomicValue, Integer>> doListAnnotationValues( int aHeight ) {
    IListEdit<Pair<IAtomicValue, Integer>> result = new ElemArrayList<>();
    double tickDelta = unitValue();
    double y = 100 - firstTickIndent;

    // int currValue = startValue;
    int currValue = endValue;

    while( y >= 0 ) {
      Pair<IAtomicValue, Integer> p;
      int screenCoord = normToScreen( y );
      if( screenCoord < bounds.y2() ) {
        // if( screenCoord < aHeight ) {
        p = new Pair<>( AvUtils.avInt( currValue ), Integer.valueOf( screenCoord ) );
        result.add( p );
      }
      // currValue += valueOfDivision;
      // y -= tickDelta;

      currValue -= valueOfDivision;
      y = Math.round( normalizeValue( AvUtils.avFloat( currValue ) ) );
    }
    return result;
  }

  @Override
  double normalizeValue( IAtomicValue aValue ) {
    float value = aValue.asFloat();
    return ((value - startValue) / (endValue - startValue)) * 100;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeListener
  //
  @Override
  public void onGenericChangeEvent( Object aSource ) {
    // FIXME реализовать
    startValue = (int)axisModel().startValue().asDouble();
    endValue = (int)axisModel().endValue().asDouble();
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
    // FIXME реализовать
    return null;
  }

  @Override
  protected void onModelChanged() {
    // FIXME реализовать IntegerAxisView#onModelChanged()
  }

}
