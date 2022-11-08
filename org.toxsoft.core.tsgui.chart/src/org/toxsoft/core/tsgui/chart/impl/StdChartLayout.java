package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.layouts.*;
import org.toxsoft.core.tsgui.chart.legaсy.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;

class StdChartLayout
    extends AbstractG2Layout
    implements IStdChartLayout {

  private static final String MAIN_CANVAS_ID = "mainCanvas"; //$NON-NLS-1$

  static class YAxisContainer {

    IListEdit<YAxisView>                 axises = new ElemArrayList<>();
    private final EBorderLayoutPlacement place;

    YAxisContainer( EBorderLayoutPlacement aPlace ) {
      place = aPlace;
    }

    Point location;
    int   height;

    int width() {
      int width = 0;
      for( int i = 0; i < axises.size(); i++ ) {
        width += axises.get( i ).bounds().width();
      }
      return width;
    }

    Point location() {
      return location;
    }

    void setLocation( Point aLocation ) {
      location = aLocation;
      int x = location.x;
      for( int i = 0; i < axises.size(); i++ ) {
        YAxisView axis = axises.get( i );
        ITsRectangle r = axis.bounds();
        r = new TsRectangle( x, location.y, r.width(), r.height() );
        axis.setBounds( r );
        x += r.width();
      }
    }

    void setHeight( int aHeight, int aTopMargin, int aBottomMargin ) {
      for( int i = 0; i < axises.size(); i++ ) {
        YAxisView axis = axises.get( i );
        ITsRectangle r = axis.bounds();
        r = new TsRectangle( r.x1(), r.y1(), r.width(), aHeight );
        axis.setBounds( r );
        axis.setMarkingMargins( new Margins( 0, aTopMargin, 0, aBottomMargin ) );
      }
    }

    void addAxis( YAxisView aAxis ) {
      aAxis.setCardinalPoint( place );
      axises.add( aAxis );
    }

    void insertAxis( int aIdx, YAxisView aAxis ) {
      aAxis.setCardinalPoint( place );
      axises.insert( aIdx, aAxis );
    }

    void removeAxis( YAxisView aAxis ) {
      axises.remove( aAxis );
    }

    void clear() {
      axises.clear();
    }
  }

  Canvas              canvas;
  YAxisContainer      leftContainer  = new YAxisContainer( EBorderLayoutPlacement.WEST );
  YAxisContainer      rightContainer = new YAxisContainer( EBorderLayoutPlacement.EAST );
  G2Canvas            chartCanvas;
  IListEdit<G2Canvas> canvases       = new ElemArrayList<>();

  private final ITsGuiContext guiContext;

  StdChartLayout( ITsGuiContext aContext ) {
    guiContext = aContext;
  }

  // TimeAxis xAxis;
  // XAxisView xAxisView;

  // IListEdit<G2Axis> yAxises = new ElemArrayList<>();
  // IListEdit<YAxisView> yAxises = new ElemArrayList<>();

  // ------------------------------------------------------------------------------------
  // Реализация методов AbstractG2Layout
  //

  @Override
  protected Canvas doCreateControl( Composite aParent ) {
    canvas = new Canvas( aParent, SWT.NONE | SWT.DOUBLE_BUFFERED );
    createContent();
    return canvas;
  }

  @Override
  protected void doLayout() {
    GC gc = null;
    try {
      gc = new GC( Display.getCurrent() );
      Rectangle clientArea = canvas.getClientArea();
      // Обработаем X шкалу
      ITsPoint p = xAxisView.prefSize( gc, SWT.DEFAULT, SWT.DEFAULT );
      ITsRectangle xRect = new TsRectangle( clientArea.x, clientArea.height - p.y(), canvas.getSize().x, p.y() );
      xAxisView.setBounds( xRect );

      // Обработаем Y шкалы
      int height = clientArea.height - xAxisView.bounds().height();
      ITsPoint titleSize = ITsPoint.ZERO;
      int titleHeight = 0;
      for( YAxisView axis : yAxisViews ) {
        p = axis.prefSize( gc, SWT.DEFAULT, height );
        int w = p.x();
        if( w <= 0 ) {
          w = 1;
        }
        int h = p.y();
        if( h <= 0 ) {
          h = 1;
        }
        TsRectangle yRect = new TsRectangle( 0, 0, w, h );
        axis.setBounds( yRect );
        if( axis.axisModel().axisDef().title() != TsLibUtils.EMPTY_STRING ) {
          titleSize = axis.titleSize( gc );
          if( axis.titleOrientation() == ETsOrientation.HORIZONTAL ) {
            if( titleSize.y() > titleHeight ) {
              titleHeight = titleSize.y();
            }
          }
        }
      }

      leftContainer.setHeight( height, chartCanvas.margins().top() + titleHeight, chartCanvas.margins().bottom() );
      rightContainer.setHeight( height, chartCanvas.margins().top() + titleHeight, chartCanvas.margins().bottom() );
      leftContainer.setLocation( new Point( clientArea.x, clientArea.y ) );
      rightContainer.setLocation( new Point( clientArea.width - rightContainer.width(), clientArea.y ) );

      int xMarkingLeft = leftContainer.width() + chartCanvas.margins().left();
      int xMarkingRight = rightContainer.width() + chartCanvas.margins().right();
      xAxisView.setMarkingMargins( new Margins( xMarkingLeft, 0, xMarkingRight, 0 ) );

      TsRectangle canvasRect = new TsRectangle( leftContainer.width(), titleHeight,
          clientArea.width - leftContainer.width() - rightContainer.width(),
          clientArea.height - xAxisView.bounds().height() - titleHeight );
      chartCanvas.setBounds( canvasRect );

      // System.out.println( "Layout" ); //$NON-NLS-1$
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  @Override
  protected IList<G2Canvas> listG2Canvases() {
    return canvases;
  }

  @Override
  protected IStringList listCanvasIds() {
    return new StringArrayList( MAIN_CANVAS_ID );
  }

  @Override
  protected void onYAxisDefAdded( IYAxisDef aAxisDef ) {
    if( canvas == null ) {
      return;
    }
    YAxisModel axisModel;
    if( yAxisModels.hasKey( aAxisDef.id() ) ) {
      axisModel = yAxisModels.getByKey( aAxisDef.id() );
    }
    else {
      axisModel = new YAxisModel( aAxisDef );
    }
    YAxisView axis = new FloatingAxisView( aAxisDef.id(), aAxisDef.description(), axisModel );
    yAxisViews.add( axis );
    boolean left = true;
    if( left ) {
      left = false;
      leftContainer.insertAxis( 0, axis );
    }
    else {
      left = true;
      rightContainer.addAxis( axis );
    }
    // 2020-12-27 mvk
    for( IG2Graphic graphic : graphics ) {
      if( aAxisDef.id().equals( graphic.plotDef().id() ) ) {
        graphic.setYAxisView( axis );
        break;
      }
    }

    areas.add( axis );
  }

  @Override
  protected void onYAxisDefRemoved( String aAxisId ) {
    if( canvas == null ) {
      return;
    }
    YAxisView axis = getYAxisView( aAxisId );
    if( axis != null ) {
      areas.remove( axis );
      yAxisViews.remove( axis );
      leftContainer.removeAxis( axis );
      rightContainer.removeAxis( axis );
    }
  }

  // @Override
  // protected void onYAxisAdded( IYAxisDef aAxisDef ) {
  // YAxisView axis = getYAxisView( aAxisDef.id() );
  // leftContainer.insertAxis( 0, axis );
  // }
  //
  // @Override
  // protected void onYAxisRemoved( String aAxisId ) {
  // YAxisView axis = getYAxisView( aAxisId );
  // leftContainer.removeAxis( axis );
  // }

  @Override
  protected void onPlotDefAdded( IPlotDef aPlotDef ) {
    YAxisView yAxis = getYAxisView( aPlotDef.yAxisId() );
    IG2DataSet ds = dataSets.getByKey( aPlotDef.dataSetid() );
    IG2Graphic graphic = new StdG2Graphic( ds, xAxisView, yAxis, aPlotDef, guiContext );
    ds.prepare( xAxisView.axisModel().timeInterval() );
    graphics.add( graphic );
    chartCanvas.addGraphic( graphic );
  }

  @Override
  protected void onPlotDefRemoved( IPlotDef aPlotDef ) {
    // FIXME to implemens onPlotDefRemoved
  }

  @Override
  protected void onDataSetAdded( IG2DataSet aDataSet ) {
    if( !dataSets.hasElem( aDataSet ) ) {
      dataSets.add( aDataSet );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void createContent() {

    GC gc = null;
    try {
      gc = new GC( Display.getCurrent() );
      // создадим полотно для рисования графиков
      IG2Params g2p =
          G2ChartUtils.createParams( IStdG2CanvasRendererOptions.CONSUMER_NAME, IOptionSet.NULL, guiContext );
      chartCanvas = new G2Canvas( MAIN_CANVAS_ID, TsLibUtils.EMPTY_STRING, g2p, guiContext );
      canvases.add( chartCanvas );
      areas.add( chartCanvas );

      // Обработаем X шкалу
      // XAxisModel xAxisModel = new XAxisModel( xAxisDef );
      xAxisModel = new XAxisModel( xAxisDef );
      // xAxis = G2Axis.createTimeAxis( canvas, xAxisDef );
      xAxisView = new XAxisView( "timeAxis", "Временная шкала", xAxisModel, EBorderLayoutPlacement.SOUTH ); //$NON-NLS-1$ //$NON-NLS-2$
      areas.add( xAxisView );

      // Обработаем Y шкалы
      boolean left = true;
      for( IYAxisDef axisDef : yAxisDefs ) {
        YAxisModel yAxisModel = new YAxisModel( axisDef );
        if( !yAxisModels.hasKey( axisDef.id() ) ) {
          yAxisModels.put( axisDef.id(), yAxisModel );
        }

        YAxisView axis = new FloatingAxisView( axisDef.id(), axisDef.description(), yAxisModel );
        yAxisViews.add( axis );
        if( left ) {
          left = false;
          leftContainer.insertAxis( 0, axis );
        }
        else {
          left = true;
          rightContainer.addAxis( axis );
        }
        areas.add( axis );
      }

      chartCanvas.setXAxisView( xAxisView );
      if( yAxisViews.size() > 0 ) {
        chartCanvas.setYAxis( yAxisViews.get( 0 ) );
      }

      // создадим графики
      for( IPlotDef plotDef : plotDefs ) {
        YAxisView yAxis = getYAxisView( plotDef.yAxisId() );
        IG2DataSet ds = dataSets.getByKey( plotDef.dataSetid() );
        IG2Graphic graphic = new StdG2Graphic( ds, xAxisView, yAxis, plotDef, guiContext );
        ds.prepare( xAxisView.axisModel().timeInterval() );
        graphics.add( graphic );
        chartCanvas.addGraphic( graphic );
      }
    }
    finally {
      if( gc != null ) {
        gc.dispose();
      }
    }
  }

  // for( IG2DataSet ds : dataSets ) {
  // if( ds.id().equals( aId ) ) {
  // return ds;
  // }
  // }
  // return null;
  // }

  // @Override
  // public G2Axis xAixs() {
  // return xAxis;
  // }
  //
  // @Override
  // public YAxisView findYAxisView( String aId ) {
  // for( YAxisView axisView : yAxises ) {
  // if( axisView.id().equals( aId ) ) {
  // return axisView;
  // }
  // }
  // return null;
  // }

}
