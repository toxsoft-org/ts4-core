package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class VedScreen
    extends TsPanel
    implements IVedScreen {

  private IVedEditorTool activeTool = null;

  private final VedScreenMouseDelegator mouseDelegator;

  private Cursor cursor = null;

  /**
   * Список представлений компонент
   */
  private final IStridablesListEdit<IVedComponentView> views = new StridablesList<>();

  private final IVedDataModel dataModel;

  public VedScreen( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    mouseDelegator = new VedScreenMouseDelegator( this );

    addDisposeListener( aE -> onDispose() );

    addPaintListener( this::paint );

    dataModel = aContext.eclipseContext().get( IVedEnvironment.class ).dataModel();
    dataModel.genericChangeEventer().addListener( aSource -> {
      views.clear();
      for( IVedComponent comp : dataModel.comps() ) {
        views.add( comp.createView( this ) );
      }
      redraw();
    } );
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
    activeTool = aTool;
    mouseDelegator.setMouseHandler( ((AbstractVedTool)aTool).mouseHandler() );
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
