package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.temporal.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.time.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Стандартный график типа линии
 *
 * @author hazard157
 */
public class StdG2Graphic
    implements IG2Graphic {

  private static final int         SET_POINT_LINE_WIDTH = 1;
  private final IG2DataSet         dataSet;
  private final IG2GraphicRenderer renderer;
  private final XAxisView          xAxisView;
  private YAxisView                yAxisView;
  private final IPlotDef           plotDef;

  private boolean visible = true;
  // FIXME добавить аппроксиматор как параметр
  IApproximator approximator = new LinearApproximator();

  /**
   * Конструктор
   *
   * @param aDataSet даннные графика
   * @param aXAxis описание шкалы X
   * @param aYAxis описание шкалы Y
   * @param aPlotDef описание поля отображения
   * @param aContext контекст приложения
   */
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
    // TODO need to check
    // dima 04.06.25 отработаем то что теперь запрос типа OSOE, то есть в ответе может придти левая точка раньше начала
    // шкалы или правая точка позже окончания видимой части шкалы
    if( !values.isEmpty() ) {
      TemporalAtomicValue newFirstValue = null;
      TemporalAtomicValue newLastValue = null;
      boolean tunedHead = false;
      boolean tunedTail = false;
      if( needTune( xAxisView.axisModel().timeInterval(), values )
          && values.first().timestamp() < xAxisView.axisModel().timeInterval().startTime() ) {
        ITemporalAtomicValue nearestFromLeft =
            getNearestFromLeft( xAxisView.axisModel().timeInterval().startTime(), values );
        // делаем первую точку в начале видимой части шкалы
        newFirstValue =
            new TemporalAtomicValue( xAxisView.axisModel().timeInterval().startTime(), nearestFromLeft.value() );
        tunedHead = true;
      }
      if( needTune( xAxisView.axisModel().timeInterval(), values )
          && values.last().timestamp() > xAxisView.axisModel().timeInterval().endTime() ) {
        ITemporalAtomicValue nearestFromRight =
            getNearestFromRight( xAxisView.axisModel().timeInterval().endTime(), values );
        // делаем последнюю точку в конце видимой части шкалы
        newLastValue =
            new TemporalAtomicValue( xAxisView.axisModel().timeInterval().endTime(), nearestFromRight.value() );
        tunedTail = true;
      }
      if( tunedHead || tunedTail ) {
        IListEdit<ITemporalAtomicValue> tunedValues = new ElemArrayList<>( values );
        if( newFirstValue != null ) {
          tunedValues.add( newFirstValue );
        }

        if( newLastValue != null ) {
          tunedValues.add( newLastValue );
        }
        // sort by time
        IListReorderer<ITemporalAtomicValue> reorderer = new ListReorderer<>( tunedValues );
        reorderer.sort( ( aO1, aO2 ) -> (int)(aO1.timestamp() - aO2.timestamp()) );
        values = new ElemArrayList<>( reorderer.list() );
        values = tunedValues;
      }
    }

    // Sol++ print date
    // G2ChartUtils.printTimeInterval( xAxisView.axisModel().timeInterval(), "Draw graphic: " );

    // dima 20.01.23 draw set point lines
    // проходим по списку уставок и отрисовываем каждую
    if( plotDef.rendererParams().params().findByKey( IStdG2GraphicRendererOptions.СHART_SET_POINTS.id() ) != null ) {
      IStringList setPointList =
          IStdG2GraphicRendererOptions.СHART_SET_POINTS.getValue( plotDef.rendererParams().params() ).asValobj();
      aGc.setForeground( Display.getCurrent().getSystemColor( SWT.COLOR_BLACK ) );
      // запоминаем текущие настройки
      int currWidth = aGc.getLineWidth();
      aGc.setLineWidth( SET_POINT_LINE_WIDTH );
      int currStyle = aGc.getLineStyle();
      aGc.setLineStyle( SWT.LINE_SOLID );

      aGc.setLineDash( new int[] { 3, 6 } );

      for( String setPointStr : setPointList ) {
        try {
          float spValue = floatVal( setPointStr );
          double nv = yAxisView.normalizeValue( AvUtils.avFloat( spValue ) );
          int spY = cr.y2() - G2ChartUtils.normToScreen( nv, cr.height() );
          aGc.drawLine( cr.x1(), spY, cr.x2(), spY );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
      // восстанавливаем текущие настройки
      aGc.setLineWidth( currWidth );
      aGc.setLineStyle( currStyle );
      aGc.setForeground( renderer.graphicColor() );

    }
    IListEdit<IList<Pair<Integer, Integer>>> polylines = new ElemArrayList<>();
    IListEdit<Pair<Integer, Integer>> polyline = null;

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

  private static ITemporalAtomicValue getNearestFromRight( long aTime, IList<ITemporalAtomicValue> aValues ) {
    ITemporalAtomicValue retVal = aValues.last();
    for( int i = aValues.size() - 1; i >= 0; i-- ) {
      ITemporalAtomicValue value = aValues.get( i );
      if( value.timestamp() <= aTime ) {
        break;
      }
      retVal = value;
    }
    return retVal;
  }

  private static ITemporalAtomicValue getNearestFromLeft( long aTime, IList<ITemporalAtomicValue> aValues ) {
    ITemporalAtomicValue retVal = aValues.first();
    for( ITemporalAtomicValue value : aValues ) {
      if( value.timestamp() <= aTime ) {
        retVal = value;
      }
      break;
    }
    return retVal;
  }

  private static boolean needTune( ITimeInterval aTimeInterval, IList<ITemporalAtomicValue> aValues ) {
    int counter = 0;
    for( ITemporalAtomicValue value : aValues ) {
      if( value.timestamp() >= aTimeInterval.startTime() && value.timestamp() <= aTimeInterval.endTime() ) {
        counter++;
      }
    }
    return counter < 2;
  }

  @SuppressWarnings( "nls" )
  private static float floatVal( String aSetPointStr ) {
    float retVal = Float.NaN;
    // перестрахуемся на счет использования запятой в качестве разделителя
    String spValStr = aSetPointStr.replaceAll( ",", "." );
    try {
      retVal = Float.parseFloat( spValStr );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    return retVal;
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
    String valStr = "null"; //$NON-NLS-1$
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

  /**
   * dima 24.01.23
   *
   * @return список значений уставок
   */
  public IStringList setPoints() {
    IStringList retVal = new StringArrayList();
    if( plotDef.rendererParams().params().findByKey( IStdG2GraphicRendererOptions.СHART_SET_POINTS.id() ) != null ) {
      retVal = IStdG2GraphicRendererOptions.СHART_SET_POINTS.getValue( plotDef.rendererParams().params() ).asValobj();
    }
    return retVal;
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
