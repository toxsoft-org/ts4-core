package org.toxsoft.core.tsgui.chart.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.chart.api.*;
import org.toxsoft.core.tsgui.chart.layouts.*;
import org.toxsoft.core.tsgui.chart.renderers.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.notifier.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IG2Chart}.
 * <p>
 * Единственная реализация интерфейса {@link IG2Chart}. Любая "кастомизация" графической компоненты осуществляется за
 * счет использования соотвествующих элементов компоненты (шкалы, отрисовщики, поведение и т.д.) и их настроечных
 * параметров. Например, для особого расположения частей компоненты (шкалы, холсты с графиками и т.д.) необходимо
 * использовать соответствующий менеджер расположения, при отсутсвии подходящего разработать его.
 *
 * @author goga, vs
 */
public final class G2Chart
    implements IG2Chart {

  IXAxisDef xAxisInfo = new XAxisDef();
  Canvas    backplane = null;

  private final AbstractG2Layout        layout;
  final G2ChartRenderer                 renderer;
  INotifierStridablesListEdit<IPlotDef> plotDefs =
      new NotifierStridablesListEditWrapper<>( new StridablesList<IPlotDef>() );

  IListEdit<StdG2Graphic> graphics = new ElemArrayList<>();

  private final INotifierStridablesListEdit<IYAxisDef> yAxisDefs =
      new NotifierStridablesListEditWrapper<>( new StridablesList<IYAxisDef>() );

  INotifierStridablesListEdit<IG2DataSet> dataSets =
      new NotifierStridablesListEditWrapper<>( new StridablesList<IG2DataSet>() );

  private final IG2Visir      visir;
  private G2ChartConsole      console      = null;
  private G2ChartMouseHandler mouseHandler = null;

  /**
   * Конструктор.
   *
   * @param aLayout IG2Layout - менеджер размещения
   * @param aContext ITsGuiContext - соответствующий контекст
   */
  public G2Chart( IG2Layout aLayout, ITsGuiContext aContext ) {
    layout = (AbstractG2Layout)aLayout;
    renderer = new G2ChartRenderer( layout, aContext );

    G2Params g2p = new G2Params( IStdG2VisirRendererOptions.CONSUMER_NAME );
    visir = new StdVisir( g2p, this );
    visir.setVisible( false );

    yAxisDefs.addCollectionChangeListener( ( aSource, aOp, aItem ) -> {
      switch( aOp ) {
        case CREATE:
          // IYAxisDef axisDef = (IYAxisDef)((INotifierStridablesListEdit)aSource).getItem( (String)aItem );
          IYAxisDef axisDef = yAxisDefs.getByKey( (String)aItem );
          layout.addYAxisDef( axisDef );
          // layout.layout();
          break;
        case EDIT:
          break;
        case LIST:
          break;
        case REMOVE:
          layout.removeYAxisDef( (String)aItem );
          // layout.layout();
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    } );

    plotDefs.addCollectionChangeListener( ( aSource, aOp, aItem ) -> {
      switch( aOp ) {
        case CREATE:
          // IPlotDef plotDef = (IPlotDef)((INotifierStridablesListEdit)aSource).getItem( (String)aItem );
          IPlotDef plotDef = plotDefs.getByKey( (String)aItem );
          layout.addPlotDef( plotDef );
          break;
        case EDIT:
          break;
        case LIST:
          break;
        case REMOVE:
          // layout.removePlDef( (String)aItem );
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    } );

    dataSets.addCollectionChangeListener( ( aSource, aOp, aItem ) -> {
      switch( aOp ) {
        case CREATE:
          // IG2DataSet dataSet = (IG2DataSet)((INotifierStridablesListEdit)aSource).getItem( (String)aItem );
          IG2DataSet dataSet = dataSets.getByKey( (String)aItem );
          layout.addDataSet( dataSet );
          break;
        case EDIT:
          break;
        case LIST:
          break;
        case REMOVE:
          // layout.removePlDef( (String)aItem );
          break;
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    } );

  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ILazyControl
  //

  @Override
  public Composite createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( backplane );

    // добавим X шкалу
    layout.setXAxisDef( xAxisInfo );

    // добавим Y шкалы
    for( IYAxisDef axisDef : yAxisDefs ) {
      layout.addYAxisDef( axisDef );
    }

    // Получим идентификатор полтна для рисования (мы знаем, что оно оно и только одно)
    // String canvasId = layout.listCanvasIds().get( 0 );

    // Добавим описания графикиков
    for( IPlotDef plotDef : plotDefs ) {
      layout.addPlotDef( plotDef );
    }

    // Добавим наборы данных
    for( IG2DataSet dataSet : dataSets ) {
      layout.addDataSet( dataSet );
    }

    // if( backplane != null ) {
    // backplane.dispose();
    // }
    backplane = layout.createControl( aParent );

    // visir.setVisible( true );
    // visir.setPosition( 50 );
    for( G2Canvas g2canvas : layout.listG2Canvases() ) {
      g2canvas.setVisir( visir );
    }

    backplane.addPaintListener( aE -> renderer.draw( aE.gc ) );

    backplane.addMouseListener( new MouseAdapter() {

      @Override
      public void mouseDown( MouseEvent e ) {
        backplane.setFocus();
        IG2ChartArea area = layout.areaUnderCursor( e.x, e.y );
        // if( area != null ) {
        // TsDialogUtils.info( null, "Найдена область: " );
        // }
        // else {
        // TsDialogUtils.error( null, "Область не найдена" );
        // }

        if( e.button == 1 ) {
          switch( area.elementKind() ) {
            case CANVAS:
              G2Canvas g2canvas = (G2Canvas)area;
              double visirPos =
                  G2ChartUtils.screenToNorm( e.x - g2canvas.clientRect.x1(), g2canvas.clientRect.width() );
              visir.setPosition( visirPos );
              backplane.redraw();
              break;
            case LABEL:
              break;
            case LEGEND:
              break;
            case TITLE:
              break;
            case X_AXIS:
              break;
            case Y_AXIS:
              break;
            default:
              break;
          }
        }

        if( e.button == 3 ) {
          // Sol++ Здесь обрабатывается нажатие правой кнопки мышки
          switch( area.elementKind() ) {
            case CANVAS:
              G2Canvas g2canvas = (G2Canvas)area;
              double visirPos =
                  G2ChartUtils.screenToNorm( e.x - g2canvas.clientRect.x1(), g2canvas.clientRect.width() );
              visir.setPosition( visirPos );
              backplane.redraw();
              // G2Chart.this.refresh();
              break;
            case LABEL:
              break;
            case LEGEND:
              break;
            case TITLE:
              break;
            case X_AXIS: {
              // XAxisView xAxisView = layout.getXAxisView();
              // final XAxisModel xAxisModel = xAxisView.axisModel();
              //
              // MenuManager mm = new MenuManager( "Меню" );
              //
              // mm.add( new Action( "Сдвиг..." ) {
              //
              // @Override
              // public void run() {
              // InputDialog dlg = new InputDialog( null, "Сдвинуть шкалу", "Сдвиг (-100 - 100):", "", null );
              // if( dlg.open() == IDialogConstants.OK_ID ) {
              // String str = dlg.getValue();
              // if( str.isEmpty() == false ) {
              // double shift = Double.parseDouble( str );
              // if( shift < -100 ) {
              // shift = -100.;
              // }
              // if( shift > 100 ) {
              // shift = 100.;
              // }
              //
              // xAxisModel.shiftAndScale( shift, 1 );
              // layout.doLayout();
              // backplane.redraw();
              // }
              // }
              // }
              //
              // } );
              //
              // MenuManager menuShift = new MenuManager( "Сдвиг" );
              // mm.add( menuShift );
              //
              // Menu popupMenu = mm.createContextMenu( backplane );
              // Point p = backplane.toDisplay( e.x, e.y );
              // popupMenu.setLocation( p.x, p.y );
              // popupMenu.setVisible( true );
            }
              break;
            case Y_AXIS:
              // String axisViewId = area.elementId();
              // YAxisView yAxisView = layout.getYAxisView( axisViewId );
              // if( yAxisView == null ) {
              // // TODO залогировать
              // return;
              // }
              // final YAxisModel yAxisModel = yAxisView.axisModel();
              // MenuManager mm = new MenuManager( "Меню" );
              //
              // mm.add( new Action( "Locate..." ) {
              //
              // @Override
              // public void run() {
              // InputDialog dlg =
              // new InputDialog( null, "Установить начальное значение", "Начальное значение:", "", null );
              // if( dlg.open() == IDialogConstants.OK_ID ) {
              // String str = dlg.getValue();
              // if( str.isEmpty() == false ) {
              // ((IYAxisDef)yAxisModel.axisDef()).valueType(); // FIXME учесть тип значения
              // yAxisModel.locate( DvUtils.avFloat( Double.parseDouble( str ) ) );
              // layout.doLayout();
              // backplane.redraw();
              // }
              // }
              // }
              //
              // } );
              //
              // mm.add( new Action( "Сдвиг..." ) {
              //
              // @Override
              // public void run() {
              // InputDialog dlg = new InputDialog( null, "Сдвинуть шкалу", "Сдвиг (-100 - 100):", "", null );
              // if( dlg.open() == IDialogConstants.OK_ID ) {
              // String str = dlg.getValue();
              // if( str.isEmpty() == false ) {
              // ((IYAxisDef)yAxisModel.axisDef()).valueType(); // FIXME учесть тип значения
              // double shift = Double.parseDouble( str );
              // if( shift < -100 ) {
              // shift = -100.;
              // }
              // if( shift > 100 ) {
              // shift = 100.;
              // }
              //
              // yAxisModel.shiftAndScale( shift, 1 );
              // layout.doLayout();
              // backplane.redraw();
              // }
              // }
              // }
              //
              // } );
              //
              // MenuManager menuScale = new MenuManager( "Масштаб" );
              // MenuManager menuShift = new MenuManager( "Сдвиг" );
              // mm.add( menuScale );
              // mm.add( menuShift );
              //
              // menuScale.add( new Action( "100%" ) {} );
              // menuScale.add( new Action( "200%" ) {} );
              // menuScale.add( new Action( "400%" ) {} );
              // menuShift.add( new Action( "10" ) {} );
              // menuShift.add( new Action( "20" ) {} );
              // menuShift.add( new Action( "50" ) {} );
              // Menu popupMenu = mm.createContextMenu( backplane );
              // Point p = backplane.toDisplay( e.x, e.y );
              // popupMenu.setLocation( p.x, p.y );
              // popupMenu.setVisible( true );
              break;
            default:
              throw new TsNotAllEnumsUsedRtException();
          }
        }
      }

    } );

    // // Добавим графики
    // for( IPlotDef plotDef : plotDefs ) {
    // G2Axis yAxis = layout.yAxis( plotDef.yAxisId() );
    // IG2DataSet ds = dataSet( plotDef.dataSetid() );
    // StdG2Graphic graphic = new StdG2Graphic( ds, layout.xAixs(), yAxis );
    // graphics.add( graphic );
    // ds.prepare( xAxisDef().initialTimeInterval() );
    // layout.addGraphic( graphic );
    // }

    ((AbstractG2Layout)chartLayout()).layout();
    return backplane;
  }

  @Override
  public Composite getControl() {
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IG2Chart
  //

  @Override
  public IXAxisDef xAxisDef() {
    return xAxisInfo;
  }

  @Override
  public void setXAxisDef( IXAxisDef aInfo ) {
    TsNullArgumentRtException.checkNull( aInfo );
    if( xAxisInfo.equals( aInfo ) ) {
      return;
    }
    xAxisInfo = aInfo;
    // TODO обновить отображение шкалы, если нужно
  }

  @Override
  public INotifierStridablesListEdit<IG2DataSet> dataSets() {
    return dataSets;
  }

  @Override
  public INotifierStridablesListEdit<IPlotDef> plotDefs() {
    return plotDefs;
  }

  @Override
  public IG2Layout chartLayout() {
    return layout;
  }

  @Override
  public void refresh() {
    if( !backplane.isDisposed() ) {
      layout.layout();
      backplane.redraw();
      if( visir != null && visir.isVisible() ) {
        visir.setPosition( visir.getPosition() );
      }
    }
  }

  @Override
  public INotifierStridablesListEdit<IYAxisDef> yAxisDefs() {
    return yAxisDefs;
  }

  @Override
  public IVisir visir() {
    return visir;
  }

  @Override
  public IZone zone() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IGrid grid() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IG2Console console() {
    if( console == null ) {
      console = new G2ChartConsole( this );
    }
    return console;
  }

  @Override
  public void setMouseHandler( G2ChartMouseHandler aHandler ) {
    // TODO решить вопрос с null
    if( mouseHandler != null ) {
      mouseHandler.dispose();
    }
    if( aHandler != null ) {
      mouseHandler = aHandler;
      mouseHandler.init( this );
    }
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  XAxisView xAxisView() {
    return layout.xAxisView;
  }

  XAxisModel xAxisModel() {
    return layout.xAxisModel;
  }

  /**
   * Возвращает признак видимости шкалы указанного графика
   *
   * @param aAxisId String идентификатор графика
   * @return boolean <b>true</b> - шкала видима<br>
   *         <b>false</b> - шкала не видима
   */
  public boolean isYAxisVisible( String aAxisId ) {
    return layout.yAxisDefs.hasKey( aAxisId );
  }

  /**
   * Задает видимость Y шкалы. vs
   *
   * @param aAxisId String идентификатор графика
   * @param aVisible boolean <b>true</b> - шкала должна быть видима<br>
   *          <b>false</b> - шкала не должна быть видима
   */
  void setYAxisVisible( String aAxisId, boolean aVisible ) {
    IYAxisDef yAxisDef = yAxisDefs.getByKey( aAxisId );
    if( aVisible ) {
      layout.addYAxisDef( yAxisDef );
      return;
    }
    layout.removeYAxisDef( aAxisId );
  }

  void setVizirVisible( boolean aVisible ) {
    visir.setVisible( aVisible );
    visir.setPosition( visir.getPosition() );
  }

  void setPlotVisible( String aPlotId, boolean aVisible ) {
    for( G2Canvas g2canvas : layout.listG2Canvases() ) {
      for( IG2Graphic graphic : g2canvas.graphics ) {
        if( graphic.plotDef().id().equals( aPlotId ) ) {
          graphic.setVisible( aVisible );
        }
      }
    }
  }

}
