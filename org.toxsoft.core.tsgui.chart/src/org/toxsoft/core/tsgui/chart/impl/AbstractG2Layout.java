package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.layouts.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Базовый класс, от которого должны наследоваться конкретные менеджеры размещения.
 *
 * @author vs
 */
public abstract class AbstractG2Layout
    implements IG2Layout {

  protected Canvas panel = null;

  protected IXAxisDef xAxisDef = null;

  protected IListEdit<IG2ChartArea>         areas     = new ElemArrayList<>();
  protected IStridablesListEdit<IYAxisDef>  yAxisDefs = new StridablesList<>();
  protected IStridablesListEdit<IPlotDef>   plotDefs  = new StridablesList<>();
  protected IStridablesListEdit<IG2DataSet> dataSets  = new StridablesList<>();
  protected IListEdit<IG2Graphic>           graphics  = new ElemArrayList<>();

  protected XAxisModel                 xAxisModel;
  protected XAxisView                  xAxisView;
  protected IStringMapEdit<YAxisModel> yAxisModels = new StringMap<>();
  protected IListEdit<YAxisView>       yAxisViews  = new ElemArrayList<>();

  protected AbstractG2Layout() {
    // nop
  }

  public final Canvas createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( panel );

    xAxisModel = new XAxisModel( xAxisDef );
    for( IYAxisDef yAxisDef : yAxisDefs ) {
      yAxisModels.put( yAxisDef.id(), new YAxisModel( yAxisDef ) );
    }

    panel = doCreateControl( aParent );
    layout();
    panel.addControlListener( new ControlListener() {

      @Override
      public void controlResized( ControlEvent aE ) {
        layout();
      }

      @Override
      public void controlMoved( ControlEvent aE ) {
        // nop
      }
    } );

    return panel;
  }

  public final void layout() {
    TsIllegalStateRtException.checkNull( panel );
    if( panel.getClientArea().width <= 0 || panel.getClientArea().height <= 0 ) {
      return;
    }
    // dima 31.05.23 в этом месте может прилететь исключение
    try {
      doLayout();
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
  }

  public final void setXAxisDef( IXAxisDef aXAxisDef ) {
    xAxisDef = aXAxisDef;
  }

  public final void addYAxisDef( IYAxisDef aYAxisDef ) {
    yAxisDefs.add( aYAxisDef );
    yAxisModels.put( aYAxisDef.id(), new YAxisModel( aYAxisDef ) );
    onYAxisDefAdded( aYAxisDef );
  }

  public final void removeYAxisDef( String aAxisId ) {
    yAxisDefs.removeById( aAxisId );
    yAxisModels.removeByKey( aAxisId );
    onYAxisDefRemoved( aAxisId );
  }

  // public final void addYAxis( IYAxisDef aAxisDef ) {
  // onYAxisAdded( aAxisDef );
  // }
  //
  // public final void removeYAxis( String aAxisId ) {
  // onYAxisRemoved( aAxisId );
  // }

  public final void addPlotDef( IPlotDef aPlotDef ) {
    plotDefs.add( aPlotDef );
    onPlotDefAdded( aPlotDef );
  }

  public final void addDataSet( IG2DataSet aDataSet ) {
    dataSets.add( aDataSet );
    onDataSetAdded( aDataSet );
  }

  public final IList<IG2ChartArea> listAreas() {
    return areas;
  }

  public final Rectangle bounds() {
    return panel.getClientArea();
  }

  public IListEdit<IG2Graphic> listGaphics() {
    return graphics;
  }

  @Override
  public IG2ChartArea areaUnderCursor( int aX, int aY ) {
    for( IG2ChartArea area : areas ) {
      if( area.bounds().contains( aX, aY ) ) {
        return area;
      }
    }
    return null;
  }

  public YAxisView getYAxisView( String aId ) {
    for( int i = 0; i < yAxisViews.size(); i++ ) {
      YAxisView axisView = yAxisViews.get( i );
      if( axisView.id().equals( aId ) ) {
        return axisView;
      }
    }
    return null;
  }

  public XAxisView getXAxisView() {
    return xAxisView;
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //

  protected abstract void doLayout();

  protected abstract Canvas doCreateControl( Composite aParent );

  protected abstract void onYAxisDefAdded( IYAxisDef aAxisDef );

  protected abstract void onYAxisDefRemoved( String aAxisId );

  // protected abstract void onYAxisAdded( IYAxisDef aAxisDef );
  //
  // protected abstract void onYAxisRemoved( String aAxisId );

  protected abstract void onPlotDefAdded( IPlotDef aPlotDef );

  protected abstract void onPlotDefRemoved( IPlotDef aPlotDef );

  protected abstract void onDataSetAdded( IG2DataSet aDataSet );

  /**
   * Перечисляет идентификаторы всех полотен для рисования графиков.
   * <p>
   * <b>Мотивация:</b><br>
   * Данный метод сделан абстрактым, так как только конкретная релизация менеджера расположений знает сколько и какие
   * полотна будут использованы.<br>
   * Сам метод введен для того, чтобы дать пользователю возможность добавить график на конкретное полотно.
   *
   * @return
   */
  protected abstract IStringList listCanvasIds();

  protected abstract IList<G2Canvas> listG2Canvases();

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  // XAxisModel xAxisModel() {
  // return xAxisModel;
  // }
  //
  // XAxisView xAxisView() {
  // return xAxisView;
  // }

}
