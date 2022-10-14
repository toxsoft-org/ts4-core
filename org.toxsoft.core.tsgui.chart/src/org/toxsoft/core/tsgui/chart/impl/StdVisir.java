package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

import ru.toxsoft.tsgui.chart.api.*;

public class StdVisir
    implements IG2Visir {

  private boolean visible = true;

  IG2Params rendererParams;

  private IG2VisirRenderer renderer;

  private final GenericChangeEventer eventer;

  double position  = 50;
  long   visirTime = 0;

  final G2Chart g2Chart;

  public StdVisir( IG2Params aRendererParams, G2Chart aChart ) {
    TsNullArgumentRtException.checkNulls( aRendererParams, aChart );
    g2Chart = aChart;
    renderer = (IG2VisirRenderer)G2ChartUtils.createObject( aRendererParams );
    eventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IVisir
  //

  @Override
  public boolean isVisible() {
    return visible;
  }

  @Override
  public void setVisible( boolean aVisible ) {
    visible = aVisible;
  }

  @Override
  public IG2Params rendererParams() {
    return rendererParams;
  }

  @Override
  public IAtomicValue getPlotValue( String aPlotId ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public double getPosition() {
    return position;
  }

  @Override
  public void setPosition( double aXCoord ) {
    position = aXCoord;
    visirTime = ((AbstractG2Layout)g2Chart.chartLayout()).getXAxisView().normToValue( position ).asLong();
  }

  @Override
  public long getVisirTime() {
    return visirTime;
  }

  @Override
  public void setVisirTime( long aTimeStamp ) {
    visirTime = aTimeStamp;
    position =
        ((AbstractG2Layout)g2Chart.chartLayout()).getXAxisView().normalizeValue( AvUtils.avTimestamp( visirTime ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IGenericChangeEventProducer
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2Visir
  //

  @Override
  public IG2VisirRenderer renderer() {
    return renderer;
  }

}
