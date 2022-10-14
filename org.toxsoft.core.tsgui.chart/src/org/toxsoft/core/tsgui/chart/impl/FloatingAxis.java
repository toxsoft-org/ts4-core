package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

import ru.toxsoft.tsgui.chart.api.*;

/**
 * Шкала действительных значений.
 * <p>
 *
 * @author vs
 */
public class FloatingAxis
    extends G2Axis {

  private final IYAxisDef axisDef;
  double                  startValue;
  double                  endValue;
  double                  valueOfDivision;

  FloatingAxis( Composite aCanvas, IYAxisDef aAxisDef ) {
    super( aCanvas, aAxisDef );
    axisDef = aAxisDef;
    startValue = axisDef.initialStartValue().asDouble();
    endValue = axisDef.initialEndValue().asDouble();
    valueOfDivision = axisDef.initialUnitValue().asDouble();
    ticksQtty = (int)Math.round( (endValue - startValue) / valueOfDivision ) + 1;
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
    return EG2ChartElementKind.Y_AXIS;
  }

  @Override
  protected IList<Pair<IAtomicValue, Integer>> doListAnnotationValues() {
    IListEdit<Pair<IAtomicValue, Integer>> result = new ElemArrayList<>();
    // int ticksPerBig = axisMarking.midTickQtty() + axisMarking.littTickQtty() + 1;
    // double tickDelta = 100. / axisMarking.bigTickNumber();
    double tickDelta = 100. / (ticksQtty - 1);
    double y = 100;

    double currValue = startValue;

    while( y > -tickDelta ) {
      Pair<IAtomicValue, Integer> p;
      int screenCoord = G2ChartUtils.normToScreen( y, markingBounds().height() ) + markingBounds().y1();
      p = new Pair<>( AvUtils.avFloat( currValue ), Integer.valueOf( screenCoord ) );
      result.add( p );
      currValue += valueOfDivision;
      y -= tickDelta;
    }
    return result;
  }

  @Override
  String startValueToString( String aFormat ) {
    String format = null;
    if( !aFormat.isEmpty() ) {
      format = aFormat;
    }
    return AvUtils.printAv( format, AvUtils.avFloat( startValue ) );
  }

  @Override
  String endValueToString( String aFormat ) {
    String format = null;
    if( !aFormat.isEmpty() ) {
      format = aFormat;
    }
    return AvUtils.printAv( format, AvUtils.avFloat( endValue ) );
  }

  @Override
  double normalizeValue( IAtomicValue aValue ) {
    float value = aValue.asFloat();
    return ((value - startValue) / (endValue - startValue)) * 100;
  }

}
