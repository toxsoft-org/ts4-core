package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.std.tool.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;

public class VedScreen
    extends TsPanel
    implements IVedScreen, IVedContextable {

  IGenericChangeListener selectionListener = aSource -> {
    redraw();
  };

  private VedAbstractEditorTool activeTool = null;

  private final VedScreenMouseDelegator mouseDelegator;

  private Cursor cursor = null;

  private double zoomFactor = 1.0;

  /**
   * Список представлений компонент
   */
  private final IStridablesListEdit<IVedComponentView> views = new StridablesList<>();

  /**
   * Список экранных объектов
   */
  private final IStridablesListEdit<IScreenObject> screenObjects = new StridablesList<>();

  private final IVedDataModel dataModel;

  private VedSelectedComponentManager selectionManager;

  VedPointerTool pointerTool;

  Color selectionColor;

  public VedScreen( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    setBackground( colorManager().getColor( ETsColor.WHITE ) );

    mouseDelegator = new VedScreenMouseDelegator( this );

    selectionManager = new VedSelectedComponentManager( selectionListener );
    selectionManager.genericChangeEventer().addListener( selectionListener );

    selectionColor = colorManager().getColor( ETsColor.RED );

    addPaintListener( this::paint );

    dataModel = aContext.eclipseContext().get( IVedEnvironment.class ).dataModel();
    dataModel.listComponents().addCollectionChangeListener( ( aSource, aOp, aItem ) -> {
      if( aOp == ECrudOp.CREATE ) {
        views.clear();
        for( IVedComponent comp : dataModel.listComponents() ) {
          views.add( comp.createView( VedScreen.this ) );
        }
        // setActiveTool( pointerTool );
        redraw();
      }
    } );

    pointerTool = new VedPointerTool( aContext );
    setActiveTool( pointerTool );
  }

  // ------------------------------------------------------------------------------------
  // IVedScreen
  //

  @Override
  public void addScreenObject( IScreenObject aObject ) {
    screenObjects.add( aObject );
  }

  @Override
  public void removeScreenObject( String aId ) {
    screenObjects.removeById( aId );
  }

  @Override
  public IStridablesList<IScreenObject> listScreenObjects() {
    return screenObjects;
  }

  @Override
  public IStridablesList<IVedComponentView> listViews() {
    return views;
  }

  public IVedEditorTool activeTool() {
    return activeTool;
  }

  public void setActiveTool( IVedEditorTool aTool ) {
    activeTool = (VedAbstractEditorTool)aTool;
    activeTool.activate( this );
    VedAbstractToolMouseHandler mh = (VedAbstractToolMouseHandler)activeTool.mouseHandler();

    IStridablesListEdit<IScreenObject> scrObjs = new StridablesList<>();
    for( IVedComponentView view : views ) {
      scrObjs.add( new VedComponentViewScreenObject( view ) );
    }

    mh.activate( this, scrObjs );
    mouseDelegator.setMouseHandler( mh );
  }

  @Override
  public IVedSelectedComponentManager selectionManager() {
    return selectionManager;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает найденное представление компоненты или <b>null</b>.<br>
   *
   * @param aId String - ИД компоненты
   * @return IVedComponentView - найденное представление компоненты или <b>null</b>
   */
  public IVedComponentView findComponentView( String aId ) {
    return views.findByKey( aId );
  }

  @Override
  public void setCursor( Cursor aCursor ) {
    if( aCursor != null && !aCursor.equals( cursor ) ) {
      cursor = aCursor;
      super.setCursor( aCursor );
      return;
    }
    if( aCursor == null && cursor != null ) {
      cursor = aCursor;
      super.setCursor( aCursor );
      return;
    }
  }

  /**
   * Возвращает коэффициент масштабирования.<br>
   *
   * @return double - коэффициент масштабирования
   */
  public double zoomFactor() {
    return zoomFactor;
  }

  /**
   * Задает коэффициент масштабирования.<br>
   *
   * @param aZoomFactor double - коэффициент масштабирования
   */
  public void setZoomFactor( double aZoomFactor ) {
    if( Double.compare( zoomFactor, aZoomFactor ) != 0 ) {
      zoomFactor = aZoomFactor;
      for( IVedComponentView view : views ) {
        view.painter().setZoomFactor( aZoomFactor );
      }
      // for( IScreenObject obj : screenObjects ) {
      // obj.setZoomFactor( aZoomFactor );
      // }
      activeTool.onZoomFactorChanged( zoomFactor );
      redraw();
    }
  }

  /**
   * Возвращает фильтрованный список "фигур".<br>
   *
   * @param aFilter ITsFilter&lt;IVedComponentView> - фильтр
   * @return IStridablesList&lt;IVedComponentView> - фильтрованный список "фигур"
   */
  public IStridablesList<IVedComponentView> listViews( ITsFilter<IVedComponentView> aFilter ) {
    IStridablesListEdit<IVedComponentView> result = new StridablesList<>();
    for( IVedComponentView view : views ) {
      if( aFilter.accept( view ) ) {
        result.add( view );
      }
    }
    return result;
  }

  /**
   * Преобразует описывающий прямоугольник "представления" в прямоугольник в экранных координатах.<br>
   *
   * @param aView IVedComponentView - "представление" компоненты
   * @return Rectangle - прямоугольникв экранных координатах
   */
  public static Rectangle boundsToScreen( IVedComponentView aView ) {
    ID2Rectangle d2r = aView.outline().bounds();
    int x = (int)Math.round( d2r.x1() * aView.painter().zoomFactor() );
    int y = (int)Math.round( d2r.y1() * aView.painter().zoomFactor() );
    int width = (int)Math.round( d2r.width() * aView.painter().zoomFactor() );
    int height = (int)Math.round( d2r.height() * aView.painter().zoomFactor() );
    return new Rectangle( x, y, width, height );
  }

  /**
   * Преобразует экранную координату в нормализованную координату.<br>
   *
   * @param aCoord int - экранная координата
   * @return double - нормализованная координата
   */
  public double screenToNorm( int aCoord ) {
    return aCoord / zoomFactor;
  }

  /**
   * Преобразует нормализованную координату в экранную координату.<br>
   *
   * @param aCoord double - нормализованная координата
   * @return int - экранная координата
   */
  public double normToScreen( double aCoord ) {
    return aCoord / zoomFactor;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  interface IScreenPainter {

    void setTransform( double aZoomFactor, ID2Point aOrigin, double aAngle, ID2Point aPivotPoint );

    void paint( GC aGc );

  }

  interface IComponentViewDecorator {

    void paint( IVedComponentView aView, GC aGc );
  }

  private IListEdit<IScreenPainter>          backgroundPainters = new ElemArrayList<>();
  private IListEdit<IScreenPainter>          foregroundPainters = new ElemArrayList<>();
  private IListEdit<IComponentViewDecorator> viewDecorators     = new ElemArrayList<>();

  void paint( PaintEvent aEvent ) {

    for( IScreenPainter p : backgroundPainters ) {
      p.paint( aEvent.gc );
    }

    for( IVedComponentView view : views ) {
      view.painter().paint( aEvent.gc );
      //
      for( IComponentViewDecorator d : viewDecorators ) {
        d.paint( view, aEvent.gc );
      }
    }

    for( IScreenObject obj : screenObjects ) {
      obj.paint( aEvent.gc );
    }

    for( IScreenPainter p : foregroundPainters ) {
      p.paint( aEvent.gc );
    }

    // отрисуем границы выделенных элементов
    aEvent.gc.setForeground( selectionColor );
    aEvent.gc.setLineWidth( 2 );
    for( IVedComponentView view : views ) {
      if( selectionManager.selectedComponents().hasKey( view.id() ) ) {
        Rectangle r = boundsToScreen( view );
        r.x -= 2;
        r.y -= 2;
        r.width += 4;
        r.height += 4;
        aEvent.gc.drawRectangle( r );
      }
    }

    // for( IShape2dView shape : shapes ) {
    // if( shape.visible() ) {
    // shape.paint( aEvent.gc );
    // }
    // }
  }

}
