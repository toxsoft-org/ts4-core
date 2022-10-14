package org.toxsoft.core.tsgui.chart.impl;

import org.toxsoft.core.tsgui.chart.api.*;

public class StdGrid
    implements IGrid {

  boolean visible = true;

  IG2Params rendererParams;

  IG2GridRenderer renderer;

  public StdGrid( IG2Params aRendererParams ) {
    renderer = (IG2GridRenderer)G2ChartUtils.createObject( aRendererParams );
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
  public IG2Params rendererParams() {
    return rendererParams;
  }

}
