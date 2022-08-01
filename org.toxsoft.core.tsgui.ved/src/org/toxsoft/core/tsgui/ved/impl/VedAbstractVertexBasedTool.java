package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.toxsoft.core.tsgui.bricks.swtevents.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.incub.props.*;
import org.toxsoft.core.tsgui.ved.std.tools.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Базовый класс для всех инструментов, которые используют "активную" границу для редактирования свойств компоненты.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertexBasedTool
    extends VedAbstractEditorTool
    implements IMouseEventConsumer {

  ISwtKeyListener keyListener = new ISwtKeyListener() {

    @Override
    public void keyPressed( KeyEvent aEvent ) {
      switch( aEvent.keyCode ) {
        case SWT.ARROW_LEFT:
          if( (aEvent.stateMask & SWT.SHIFT) == 0 ) {
            double dx = -1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dx = -10.0;
            }
            dx = vedScreen().coorsConvertor().convertX( dx, 0.0 );
            onChangeLocationByKeyRequest( dx, 0 );
            return;
          }
          if( (aEvent.stateMask & SWT.SHIFT) != 0 ) {
            double dw = -1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dw = -10.0;
            }
            dw = vedScreen().coorsConvertor().convertX( dw, 0.0 );
            onChangeSizeByKeyRequest( dw, 0 );
          }
          break;
        case SWT.ARROW_RIGHT:
          if( (aEvent.stateMask & SWT.SHIFT) == 0 ) {
            double dx = 1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dx = 10.0;
            }
            dx = vedScreen().coorsConvertor().convertX( dx, 0.0 );
            onChangeLocationByKeyRequest( dx, 0 );
            return;
          }
          if( (aEvent.stateMask & SWT.SHIFT) != 0 ) {
            double dw = 1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dw = 10.0;
            }
            dw = vedScreen().coorsConvertor().convertX( dw, 0.0 );
            onChangeSizeByKeyRequest( dw, 0 );
          }
          break;
        case SWT.ARROW_UP:
          if( (aEvent.stateMask & SWT.SHIFT) == 0 ) {
            double dy = -1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dy = -10.0;
            }
            dy = vedScreen().coorsConvertor().convertY( 0.0, dy );
            onChangeLocationByKeyRequest( 0, dy );
            return;
          }
          if( (aEvent.stateMask & SWT.SHIFT) != 0 ) {
            double dh = -1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dh = -10.0;
            }
            dh = vedScreen().coorsConvertor().convertY( 0.0, dh );
            onChangeSizeByKeyRequest( 0, dh );
          }
          break;
        case SWT.ARROW_DOWN:
          if( (aEvent.stateMask & SWT.SHIFT) == 0 ) {
            double dy = 1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dy = 10.0;
            }
            dy = vedScreen().coorsConvertor().convertY( 0.0, dy );
            onChangeLocationByKeyRequest( 0, dy );
            return;
          }
          if( (aEvent.stateMask & SWT.SHIFT) != 0 ) {
            double dh = 1.0;
            if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
              dh = 10.0;
            }
            dh = vedScreen().coorsConvertor().convertY( 0.0, dh );
            onChangeSizeByKeyRequest( 0, dh );
          }
          break;
        default:
          break;
      }
    }
  };

  private final IPropertyChangeListener propertyChangeListener =
      ( aSource, aPropId, aOldValue, aNewValue ) -> updateVertexSet();

  // GOGA
  // IGenericChangeListener activeComponentListener = aSource -> {
  // updateVertexSet();
  // };

  /**
   * Список выделенных компонет, с которыми работает данный инструмент
   */
  private final IStridablesListEdit<IVedComponentView> selectedViews = new StridablesList<>();

  /**
   * Список экранных объектов доступных инструменту
   */
  protected IStridablesListEdit<IScreenObject> screenObjects = new StridablesList<>();

  private IVedVertexSetView vertexSet = null;

  private IScreenObject activeObject = null;

  private IVedComponentView activeView = null;

  protected VedAbstractVertexBasedTool( IVedEditorToolProvider aProvider, IVedEnvironment aEnv, IVedScreen aScreen ) {
    super( aProvider, aEnv, aScreen );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractEditorTool
  //

  @Override
  public ISwtKeyListener keyListener() {
    return keyListener;
  }

  @Override
  public VedAbstractToolMouseHandler mouseListener() {
    return null;
  }

  @Override
  void papiToolActivated() {
    vertexSet = vertexSet();
    if( mouseListener() != null ) {
      mouseListener().setMouseEventConsumer( this );
    }

    screenObjects.clear();
    for( IVedComponentView view : listComponentViews() ) {
      screenObjects.add( new VedComponentViewScreenObject( view ) );
    }
    if( mouseListener() != null ) {
      mouseListener().setScreenObjects( screenObjects );
    }

    onActivated();
  }

  @Override
  void papiToolDeactivated() {
    vertexSet = null;
    if( mouseListener() != null ) {
      mouseListener().setMouseEventConsumer( null );
    }
    hideVertexSet( vertexSet );
    onDeactivated();
  }

  // ------------------------------------------------------------------------------------
  // IMouseEventConsumer
  //

  @Override
  public void onClick( IScreenObject aHoveredObject, MouseEvent aEvent ) {
    if( aHoveredObject == null ) {
      if( activeView != null ) {
        selectedViews.remove( activeView );
        screenObjects.add( activeObject );
        // activeView.component().genericChangeEventer().removeListener( activeComponentListener );
        activeView.component().props().propsEventer().removeListener( propertyChangeListener );
        activeView = null;
        activeObject = null;
      }
      hideVertexSet( vertexSet );
    }
    if( aHoveredObject != null && aHoveredObject.entity() != null ) {
      activeObject = aHoveredObject;
      activeView = aHoveredObject.entity();
      // activeView.component().genericChangeEventer().addListener( activeComponentListener );
      activeView.component().props().propsEventer().addListener( propertyChangeListener );
      screenObjects.remove( aHoveredObject );
      if( !selectedViews.hasKey( aHoveredObject.id() ) ) {
        selectedViews.add( activeView );
        ITsRectangle tsRect = vedScreen().coorsConvertor().rectBounds( activeView.outline().bounds() );
        vertexSet.init( tsRect );
        showVertexSet( vertexSet );
      }
      return;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public IVedComponentView activeView() {
    return activeView;
  }

  public void addToSelected( IVedComponentView aView ) {
    if( !selectedViews.hasKey( aView.id() ) ) {
      selectedViews.add( aView );
      onSelectionChanged();
    }
  }

  public void removeFromSelected( String aId ) {
    if( selectedViews.hasKey( aId ) ) {
      selectedViews.removeById( aId );
      onSelectionChanged();
    }
  }

  // public IGenericChangeListener activeComponentListener() {
  // return activeComponentListener;
  // }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  protected void onChangeLocationByKeyRequest( double aDeltaX, double aDeltaY ) {
    for( IVedComponentView view : selectedViews ) {
      view.porter().shiftOn( aDeltaX, aDeltaY );
    }
    updateVertexSet();
  }

  protected void onChangeSizeByKeyRequest( double aDeltaWidth, double aDeltaHeight ) {
    if( selectedViews.size() == 0 ) {
      return;
    }
    if( selectedViews.size() == 1 ) {
      IVedComponentView view = selectedViews.first();
      double width = view.outline().bounds().width() + aDeltaWidth;
      double height = view.outline().bounds().height() + aDeltaHeight;
      view.porter().setSize( width, height );
      updateVertexSet();
    }
  }

  // ------------------------------------------------------------------------------------
  // Методы для обязательного переопределения в наследниках
  //

  protected abstract IVedVertexSetView vertexSet();

  protected abstract IStridablesList<IVedComponentView> listComponentViews();

  // ------------------------------------------------------------------------------------
  // Методы для возможного переопределения в наследниках
  //

  @SuppressWarnings( "unused" )
  protected String cursorId( IVedComponentView aView ) {
    return TsLibUtils.EMPTY_STRING;
  }

  protected void onSelectionChanged() {
    // nop
  }

  protected void onActivated() {
    // nop
  }

  protected void onDeactivated() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private void hideVertexSet( IVedVertexSetView aView ) {
    aView.setVisible( false );
    for( IVedVertex v : aView.listVertexes() ) {
      screenObjects.remove( v );
    }
    screenObjects.remove( aView );
    if( mouseListener() != null ) {
      mouseListener().setScreenObjects( screenObjects );
    }
    vedScreen().paintingManager().redraw();
  }

  private void showVertexSet( IVedVertexSetView aView ) {
    aView.setVisible( true );
    if( !screenObjects.hasKey( aView.id() ) ) {
      screenObjects.add( aView );
    }
    for( IVedVertex v : aView.listVertexes() ) {
      if( !screenObjects.hasKey( v.id() ) ) {
        screenObjects.add( v );
      }
    }
    if( mouseListener() != null ) {
      mouseListener().setScreenObjects( screenObjects );
    }
    vedScreen().paintingManager().redraw();
  }

  private void updateVertexSet() {
    ID2Rectangle d2r = this.activeView.outline().bounds();
    ITsRectangle tsRect = vedScreen().coorsConvertor().rectBounds( d2r );
    this.vertexSet.init( tsRect );
    vedScreen().paintingManager().redraw();
    vedScreen().paintingManager().update();
  }
}
