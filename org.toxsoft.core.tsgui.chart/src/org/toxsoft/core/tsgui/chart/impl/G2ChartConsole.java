package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.time.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Консоль управления графиками.
 *
 * @author vs
 */
public class G2ChartConsole
    implements IG2Console {

  private final XAxisModel       xAxisModel;
  private final G2Chart          g2chart;
  private final AbstractG2Layout chartLayout;

  public G2ChartConsole( G2Chart aG2Chart ) {
    g2chart = TsNullArgumentRtException.checkNull( aG2Chart );
    chartLayout = (AbstractG2Layout)g2chart.chartLayout();
    xAxisModel = g2chart.xAxisModel();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2Console
  //

  @Override
  public long getX1() {
    return xAxisModel.timeInterval().startTime();
  }

  @Override
  public long getX2() {
    return xAxisModel.timeInterval().endTime();
  }

  @Override
  public IAtomicValue getY1( String aPlotId ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IAtomicValue getY2( String aPlotId ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void setTimeUnit( ETimeUnit aTimeUnit ) {
    xAxisModel.setTimeUnit( aTimeUnit );
    // g2chart.xAxisView().setT
  }

  @Override
  public void changeXScale( double aShift, double aScale ) {
    xAxisModel.shiftAndScale( aShift, aScale );
  }

  @Override
  public void changeYScale( double aShift, double aScale ) {
    // TODO Auto-generated method stub
  }

  @Override
  public void shiftYAxis( String aAxisId, double aShift ) {
    for( YAxisModel model : chartLayout.yAxisModels ) {
      if( model.yAxisDef().id().equals( aAxisId ) ) {
        model.shiftAndScale( aShift, 1.0 );
      }
    }
  }

  @Override
  public void scaleYAxis( String aAxisId, double aScale ) {
    for( YAxisModel model : chartLayout.yAxisModels ) {
      if( model.yAxisDef().id().equals( aAxisId ) ) {
        model.shiftAndScale( 0, aScale );
      }
    }
  }

  @Override
  public void locateX( long aT1 ) {
    long duration = xAxisModel.timeInterval().duration();
    xAxisModel.setTimeInterval( new TimeInterval( aT1, aT1 + duration ) );
  }

  @Override
  public void setXRange( long aT1, long aT2 ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void locateY( String aPlotId, IAtomicValue aV1 ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setYRange( String aPlotId, IAtomicValue aV1, IAtomicValue aV2 ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void addListener( IChartAxisListener aListener ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void removeListener( IChartAxisListener aListener ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void selectPlot( String aPlotId ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setYAxisVisible( String aAxisId, boolean aVisible ) {
    g2chart.setYAxisVisible( aAxisId, aVisible );
  }

  @Override
  public void setVizirVisible( boolean aVisible ) {
    g2chart.setVizirVisible( aVisible );
  }

  @Override
  public void setGridVisible( boolean aVisible ) {
    // TODO Auto-generated method stub

  }

  @Override
  public void setPlotVisible( String aPlotId, boolean aVisible ) {
    g2chart.setPlotVisible( aPlotId, aVisible );
  }

}
