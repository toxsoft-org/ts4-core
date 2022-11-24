package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class StdG2Graphic
    implements IG2Graphic {

  private final IG2DataSet         dataSet;
  private final IG2GraphicRenderer renderer;
  private final XAxisView          xAxisView;
  private YAxisView                yAxisView;
  private final IPlotDef           plotDef;

  private boolean visible = true;
  // FIXME добавить аппроксиматор как параметр
  IApproximator approximator = new LinearApproximator();

  // public StdG2Graphic( IG2DataSet aDataSet, XAxisView aXAxis, YAxisView aYAxis, IG2Params aRendererParams ) {
  public StdG2Graphic( IG2DataSet aDataSet, XAxisView aXAxis, YAxisView aYAxis, IPlotDef aPlotDef,
      ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aDataSet, aXAxis, aYAxis );
    dataSet = aDataSet;
    renderer = new StdG2GraphicRenderer( aPlotDef.rendererParams(), aContext );
    xAxisView = aXAxis;
    yAxisView = aYAxis;
    plotDef = aPlotDef;
    // Sol++ print date
    // G2ChartUtils.printTimeInterval( xAxisView.axisModel().timeInterval(), "StdG2Graphic: " );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2Graphic
  //

  @Override
  public IPlotDef plotDef() {
    return plotDef;
  }

  @Override
  public void draw( GC aGc, ITsRectangle aClientRect ) {
    ITsRectangle cr = aClientRect;

    IList<ITemporalAtomicValue> values = dataSet.getValues( xAxisView.axisModel().timeInterval() );

    // Sol++ print date
    // G2ChartUtils.printTimeInterval( xAxisView.axisModel().timeInterval(), "Draw graphic: " );

    IListEdit<IList<Pair<Integer, Integer>>> polylines = new ElemArrayList<>();
    IListEdit<Pair<Integer, Integer>> polyline = null;

    if( values == null ) {
      return;
    }

    double ntMax = Double.NEGATIVE_INFINITY;
    double nvMax = Double.NEGATIVE_INFINITY;

    double prevNt = Double.NEGATIVE_INFINITY;
    double prevNv = Double.NEGATIVE_INFINITY;

    for( int i = 0; i < values.size(); i++ ) {
      long time = values.get( i ).timestamp();
      IAtomicValue value = values.get( i ).value();
      if( value == IAtomicValue.NULL ) {
        if( polyline != null ) {
          polylines.add( polyline );
        }
        polyline = null;
      }
      else {
        if( polyline == null ) {
          polyline = new ElemArrayList<>();
        }
        double nt = xAxisView.normalizeValue( AvUtils.avTimestamp( time ) );
        double nv = yAxisView.normalizeValue( value );
        if( nt >= 0 && nt <= 100 ) {
          if( prevNt != Double.NEGATIVE_INFINITY ) {
            int scrX = cr.x1() + G2ChartUtils.normToScreen( prevNt, cr.width() );
            int scrY = cr.y2() - G2ChartUtils.normToScreen( prevNv, cr.height() );
            polyline.add( new Pair<>( Integer.valueOf( scrX ), Integer.valueOf( scrY ) ) );
            prevNt = Double.NEGATIVE_INFINITY;
          }
          int scrX = cr.x1() + G2ChartUtils.normToScreen( nt, cr.width() );
          int scrY = cr.y2() - G2ChartUtils.normToScreen( nv, cr.height() );
          polyline.add( new Pair<>( Integer.valueOf( scrX ), Integer.valueOf( scrY ) ) );
        }
        else {
          if( nt < 0 ) {
            prevNt = nt;
            prevNv = nv;
          }
          else {
            ntMax = nt;
            nvMax = nv;
            break;
          }
        }
      }
    }
    if( polyline != null ) {
      if( ntMax != Double.NEGATIVE_INFINITY ) {
        int scrX = cr.x1() + G2ChartUtils.normToScreen( ntMax, cr.width() );
        int scrY = cr.y2() - G2ChartUtils.normToScreen( nvMax, cr.height() );
        polyline.add( new Pair<>( Integer.valueOf( scrX ), Integer.valueOf( scrY ) ) );
      }
      polylines.add( polyline );
    }

    renderer.drawGraphic( aGc, polylines, cr );
  }

  @Override
  public void drawRepresentation( GC aGc, ITsRectangle aBounds ) {
    renderer.drawRepresentation( aGc, aBounds );
  }

  @Override
  public ITemporalAtomicValue valueAt( long aTimestamp ) {
    Pair<ITemporalAtomicValue, ITemporalAtomicValue> pair = dataSet.locate( aTimestamp );
    if( pair == null ) {
      return new TemporalAtomicValue( aTimestamp, IAtomicValue.NULL );
    }

    if( pair.left() == ITemporalAtomicValue.NULL ) {
      return pair.right();
    }
    if( pair.right() == ITemporalAtomicValue.NULL ) {
      return pair.left();
    }

    if( pair.left().timestamp() == pair.right().timestamp() ) {
      return pair.left();
    }

    // double v = (aTimestamp - pair.left().timestamp()) / (pair.right().timestamp() - pair.left().timestamp());
    //
    // double leftVal = pair.left().value().asDouble();
    // double rightVal = pair.right().value().asDouble();
    // v = leftVal + (rightVal - leftVal) * v;

    // dima, 15.11.22 если график "ступени", то берем просто левое значение
    EGraphicRenderingKind renderingKind =
        IStdG2GraphicRendererOptions.RENDERING_KIND.getValue( plotDef.rendererParams().params() ).asValobj();
    if( renderingKind.equals( EGraphicRenderingKind.LADDER ) ) {
      return pair.left();
    }

    IAtomicValue v = approximator.approximate( pair.left(), pair.right(), aTimestamp );
    return new TemporalAtomicValue( aTimestamp, v );
  }

  @Override
  public String valueToString( ITemporalAtomicValue aValue ) {
    String valStr = "null";
    if( aValue != ITemporalAtomicValue.NULL ) {
      IAtomicValue value = aValue.value();
      if( value != IAtomicValue.NULL ) {
        // dima, 11.11.22 используем формат из параметра
        // return AvUtils.printAv( "%4.2f", value );
        EDisplayFormat displayFormat =
            IStdG2GraphicRendererOptions.VALUES_DISPLAY_FORMAT.getValue( plotDef.rendererParams().params() ).asValobj();
        return AvUtils.printAv( displayFormat.format(), value );
      }
    }
    return valStr;
  }

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  @Override
  public void setYAxisView( YAxisView aAxis ) {
    yAxisView = aAxis;
  }

}
