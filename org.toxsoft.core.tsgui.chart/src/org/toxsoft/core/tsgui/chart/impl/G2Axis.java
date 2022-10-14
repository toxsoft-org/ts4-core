package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.api.*;
import ru.toxsoft.tsgui.chart.legaсy.*;

/**
 * Шкала компоненты графиков.
 *
 * @author vs
 */
public abstract class G2Axis
    extends AbstractG2ChartArea {

  // protected final Composite canvas;
  protected final IXAxisDef xAxisDef;
  protected final IYAxisDef yAxisDef;

  protected final AxisMarkingDef axisMarking;

  protected final IG2AxisRenderer renderer;

  protected int ticksQtty = 10;// FIXME определить количество тиков

  // private TsRectangle markingBounds = null;
  //
  // private boolean markingBoundsChanged = false;

  private Margins markingMargins = new Margins( 0, 0, 0, 0 );

  EBorderLayoutPlacement place;

  // IAtomicValue startValue;
  // IAtomicValue endValue;
  // IAtomicValue valueOfDivision;

  protected G2Axis( Composite aCanvas, IXAxisDef aAxisDef ) {
    super( "timeAxis", TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_STRING ); //$NON-NLS-1$
    // canvas = aCanvas;
    xAxisDef = aAxisDef;
    yAxisDef = null;
    axisMarking = aAxisDef.axisMarkingDef();
    renderer = createRenderer( xAxisDef.rendererParams() );
    // ((AbstractAxisPartRenderer)renderer).setAxis( this );
    place = EBorderLayoutPlacement.SOUTH;
  }

  protected G2Axis( Composite aCanvas, IYAxisDef aAxisDef ) {
    super( aAxisDef.id(), aAxisDef.nmName(), aAxisDef.description() );
    // canvas = aCanvas;
    xAxisDef = null;
    yAxisDef = aAxisDef;
    axisMarking = aAxisDef.axisMarkingDef();
    renderer = createRenderer( yAxisDef.rendererParams() );
    // ((AbstractAxisPartRenderer)renderer).setAxis( this );
    place = EBorderLayoutPlacement.WEST;
  }

  public static TimeAxis createTimeAxis( Composite aCanvas, IXAxisDef aAxisDef ) {
    TimeAxis axis = new TimeAxis( aCanvas, aAxisDef );
    return axis;
  }

  public static G2Axis createYAxis( Composite aCanvas, IYAxisDef aAxisDef ) {
    if( aAxisDef.initialUnitValue().atomicType() == EAtomicType.FLOATING ) {
      FloatingAxis axis = new FloatingAxis( aCanvas, aAxisDef );
      return axis;
    }
    // FIXME сделать Int шкалу
    throw new TsUnderDevelopmentRtException( "Другие типы шкал кроме Floating - в разработке" );
  }

  public void setCardinalPoint( EBorderLayoutPlacement aPlace ) {
    place = aPlace;
  }

  public EBorderLayoutPlacement cardinalPoint() {
    return place;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractG2ChartArea
  //

  @Override
  public void drawBackground( GC aGc ) {
    renderer.drawBackground( aGc, bounds, place );
  }

  @Override
  public void draw( GC aGc ) {
    renderer.drawAxis( aGc, bounds, place );
  }

  @Override
  public ITsPoint prefSize( GC aGc, int aHorHint, int aVertHint ) {
    return renderer.prefSize( aGc, aHorHint, aVertHint );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Вычисляет координаты засечек в нормализованных координатах
   *
   * @return
   */
  public IList<Pair<Double, ETickType>> calcTickPositions() {
    IListEdit<Pair<Double, ETickType>> tickPositions = new ElemArrayList<>();
    int ticksPerBig = axisMarking.midTickQtty() + axisMarking.littTickQtty() + 1;
    // double tickDelta = 100. / ((ticksQtty - 1) * ticksPerBig);
    double tickDelta = 100. / ((ticksQtty - 1) * axisMarking.bigTickNumber());
    double x = 0;
    int idx = 0;
    while( x <= 100 ) {
      Pair<Double, ETickType> pair = new Pair<>( x, axisMarking.tickType( idx ) );
      tickPositions.add( pair );
      x += tickDelta;
      idx++;
    }
    return tickPositions;
  }

  public IList<Pair<IAtomicValue, Integer>> listAnnotationValues() {
    return doListAnnotationValues();
  }

  AxisMarkingDef axisMarkingDef() {
    return axisMarking;
  }

  // public void setMarkingBounds( TsRectangle aMarkingBounds ) {
  // markingBoundsChanged = true;
  // markingBounds = aMarkingBounds;
  // }

  public void setMarkingMargins( Margins aMargings ) {
    markingMargins = aMargings;
  }

  TsRectangle markingBounds() {
    return new TsRectangle( bounds.x1() + markingMargins.left(), bounds.y1() + markingMargins.top(), //
        bounds.width() - markingMargins.left() - markingMargins.right(), //
        bounds.height() - markingMargins.top() - markingMargins.bottom() );
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //

  abstract String startValueToString( String aFormat );

  abstract String endValueToString( String aFormat );

  protected abstract IList<Pair<IAtomicValue, Integer>> doListAnnotationValues();

  abstract double normalizeValue( IAtomicValue aValue );

  // // ------------------------------------------------------------------------------------
  // // Внутренняя реализация
  // //
  //
  // IG2AxisRenderer createRenderer( IG2Params aRendererParams ) {
  // try {
  // Class rendererClass = Class.forName( aRendererParams.consumerName() );
  // Constructor constructor = rendererClass.getDeclaredConstructor( new Class[] { IOptionSet.class } );
  // return (IG2AxisRenderer)constructor.newInstance( aRendererParams.params() );
  // }
  // catch( Throwable ex ) {
  // ex.printStackTrace();
  // }
  // return null;
  // }

  IG2AxisRenderer createRenderer( IG2Params aRendererParams ) {
    return (IG2AxisRenderer)G2ChartUtils.createObject( aRendererParams );
  }

  // public static Integer normToScreen( Double aCoord, int aLength ) {
  // return Integer.valueOf( (int)Math.round( (aCoord.doubleValue() * aLength) / 100. ) );
  // }

  // @Override
  // protected void onBoundsChanged() {
  // if( markingBoundsChanged == false ) {
  // markingBounds = bounds;
  // }
  // }

}
