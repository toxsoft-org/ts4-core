package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.incub.geom.*;
import org.toxsoft.core.tsgui.ved.std.tool.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class VedScreen
    extends TsPanel
    implements IVedScreen {

  private IVedEditorTool activeTool = null;

  private final VedScreenMouseDelegator mouseDelegator;

  private Cursor cursor = null;

  private double zoomFactor = 1.0;

  /**
   * Список представлений компонент
   */
  private final IStridablesListEdit<IVedComponentView> views = new StridablesList<>();

  private final IVedDataModel dataModel;

  VedPointerTool pointerTool;

  public VedScreen( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    mouseDelegator = new VedScreenMouseDelegator( this );

    addDisposeListener( aE -> onDispose() );

    addPaintListener( this::paint );

    dataModel = aContext.eclipseContext().get( IVedEnvironment.class ).dataModel();
    dataModel.comps().addCollectionChangeListener( ( aSource, aOp, aItem ) -> {
      views.clear();
      for( IVedComponent comp : dataModel.comps() ) {
        views.add( comp.createView( VedScreen.this ) );
      }
      setActiveTool( pointerTool );
      redraw();
    } );

    pointerTool = new VedPointerTool( aContext );
    setActiveTool( pointerTool );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedScreen}
  //

  @Override
  public IVedEditorTool activeTool() {
    return activeTool;
  }

  @Override
  public void setActiveTool( IVedEditorTool aTool ) {
    // activeTool = aTool;
    // mouseDelegator.setMouseHandler( ((AbstractVedTool)aTool).mouseHandler() );
    activeTool = aTool;
    ((VedAbstractEditorTool)activeTool).activate( this );
    VedAbstractToolMouseHandler mh = (VedAbstractToolMouseHandler)((VedAbstractEditorTool)activeTool).mouseHandler();
    mh.activate( this );
    mouseDelegator.setMouseHandler( mh );
  }

  @Override
  public IStridablesList<IVedEditorTool> tools() {
    throw new TsUnderDevelopmentRtException();
  }

  @Override
  public IVedSelectedComponentManager selectionManager() {
    throw new TsUnderDevelopmentRtException();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

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
      for( IVedComponentView view : views ) {
        view.painter().setZoomFactor( aZoomFactor );
      }
    }
  }

  /**
   * Возвращает фильтрованный список "фигур".<br>
   *
   * @param aFilter ITsFilter&lt;IVedComponentView> - фильтр
   * @return IStridablesList&lt;IVedComponentView> - фильтрованный список "фигур"
   */
  // public IStridablesList<IVedComponentView> listViews( ITsFilter<IVedComponentView> aFilter ) {
  // IStridablesListEdit<IVedComponentView> result = new StridablesList<>();
  // for( IVedComponentView view : views ) {
  // if( aFilter.accept( view ) ) {
  // result.add( view );
  // }
  // }
  // return result;
  // }

  public IStridablesList<IVedComponentView> listViews() {
    return views;
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

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void onDispose() {
    mouseDelegator.dispose();
  }

  void paint( PaintEvent aEvent ) {
    for( IVedComponentView view : views ) {
      view.painter().paint( aEvent.gc );
    }

    // for( IShape2dView shape : shapes ) {
    // if( shape.visible() ) {
    // shape.paint( aEvent.gc );
    // }
    // }
  }

}
