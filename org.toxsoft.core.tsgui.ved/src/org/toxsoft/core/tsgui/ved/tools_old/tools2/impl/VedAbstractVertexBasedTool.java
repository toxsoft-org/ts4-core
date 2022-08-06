package org.toxsoft.core.tsgui.ved.tools_old.tools2.impl;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.swtevents.*;
import org.toxsoft.core.tsgui.graphics.colors.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.incub.props.*;
import org.toxsoft.core.tsgui.ved.tools_old.tools2.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Базовый класс для всех инструментов, которые используют "активную" границу для редактирования свойств компоненты.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertexBasedTool
    extends VedAbstractEditorTool {

  /**
   * Стратегия обработки изменения выделения.
   * <p>
   *
   * @author vs
   */
  interface ISelectionChangedStrategy {

    void onSelectionChanged();
  }

  /**
   * Реализация сратегии обработки изменения выделения по-умолчанию.
   * <p>
   * Реализует следующее поведение:
   * <ul>
   * <li>При отсутствии выделенных элементов - скрывает набор вершин</li>
   * <li>При наличии одного или более выделенных элементов - инциализирует и отображает набор вершин</li>
   * </ul>
   *
   * @author vs
   */
  class DefaultSelectionChangedStrategy
      implements ISelectionChangedStrategy {

    @Override
    public void onSelectionChanged() {
      IStridablesList<IVedComponentView> selComps = selectedViews();
      if( selComps.size() <= 0 ) { // нет выделенных компонент
        hideVertexSet( vertexSet );
      }
      else {
        vertexSet.init( selComps );
        showVertexSet( vertexSet ); // есть выделенные компоненты
      }
    }

  }

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

  // /**
  // * Список выделенных компонет, с которыми работает данный инструмент
  // */
  // private final IStridablesListEdit<IVedComponentView> selectedViews = new StridablesList<>();

  /**
   * Список экранных объектов доступных инструменту
   */
  protected IListEdit<IScreenObject> screenObjects = new ElemArrayList<>();

  private IVedVertexSetView vertexSet = null;

  private IScreenObject activeObject = null;

  private IVedComponentView activeView = null;

  IVedScreenSelectionManager selectionManager;

  DefaultSelectionChangedStrategy selectionStrategy = new DefaultSelectionChangedStrategy();

  IGenericChangeListener selectionListener = aSource -> {
    selectionStrategy.onSelectionChanged();
  };

  private final IVedScreenSelectionDecorator selectionDecorator;

  protected VedAbstractVertexBasedTool( IVedEditorToolProvider aProvider, IVedEnvironment aEnv, IVedScreen aScreen ) {
    super( aProvider, aEnv, aScreen );
    selectionManager = vedScreen().selectionManager();
    Color c = colorManager().getColor( ETsColor.BLUE );
    selectionDecorator = new VedDefaultSelectionDecorator( aScreen, aEnv, c );
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
    selectionManager.genericChangeEventer().addListener( selectionListener );
    vedScreen().paintingManager().addViewsDecorator( selectionDecorator );
    vertexSet = vertexSet();
    // if( mouseListener() != null ) {
    // mouseListener().setMouseEventConsumer( this );
    // }

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
    selectionManager.genericChangeEventer().removeListener( selectionListener );
    vedScreen().paintingManager().removeViewsDecorator( selectionDecorator );
    hideVertexSet( vertexSet );
    vertexSet = null;
    onDeactivated();
  }

  // ------------------------------------------------------------------------------------
  // IMouseEventConsumer
  //

  // @Override
  // public void onClick( IScreenObject aHoveredObject, MouseEvent aEvent ) {
  // if( aHoveredObject == null ) {
  // if( activeView != null ) {
  // selectedViews.remove( activeView );
  // screenObjects.add( activeObject );
  // System.out.println( "+++ addScreenObject+++ " + activeObject );
  // // activeView.component().genericChangeEventer().removeListener( activeComponentListener );
  // activeView.component().props().propsEventer().removeListener( propertyChangeListener );
  // activeView = null;
  // activeObject = null;
  // }
  // hideVertexSet( vertexSet );
  // return;
  // }
  // if( aHoveredObject != null && aHoveredObject.entity() != null ) {
  // if( activeObject != null ) {
  // screenObjects.add( activeObject );
  // System.out.println( "+++ addScreenObject+++ " + activeObject );
  // }
  // activeObject = aHoveredObject;
  // activeView = aHoveredObject.entity();
  // // activeView.component().genericChangeEventer().addListener( activeComponentListener );
  // activeView.component().props().propsEventer().addListener( propertyChangeListener );
  // screenObjects.remove( activeObject );
  // selectedViews.remove( activeView );
  // System.out.println( "--- removeScreenObject--- " + activeObject );
  // if( aHoveredObject.kind() == EScreenObjectKind.COMPONENT
  // && !selectedViews.hasKey( ((IVedComponentView)aHoveredObject.entity()).id() ) ) {
  // selectedViews.add( activeView );
  // ITsRectangle tsRect = vedScreen().coorsConvertor().rectBounds( activeView.outline().bounds() );
  // vertexSet.init( tsRect );
  // showVertexSet( vertexSet );
  // }
  // return;
  // }
  // }

  // ------------------------------------------------------------------------------------
  // API
  //

  public IVedComponentView activeView() {
    return activeView;
  }

  public IVedScreenSelectionManager selectionManager() {
    return selectionManager;
  }

  public IVedScreenSelectionDecorator selectionDecorator() {
    return selectionDecorator;
  }

  public IStridablesList<IVedComponentView> selectedViews() {
    IStridablesListEdit<IVedComponentView> result = new StridablesList<>();
    for( IVedComponentView view : selectionManager.selectedViews() ) {
      if( accept( view ) ) {
        result.add( view );
      }
    }
    return result;
  }

  // public void addToSelected( IVedComponentView aView ) {
  // if( !selectedViews.hasKey( aView.id() ) ) {
  // selectedViews.add( aView );
  // onSelectionChanged();
  // }
  // }
  //
  // public void removeFromSelected( String aId ) {
  // if( selectedViews.hasKey( aId ) ) {
  // selectedViews.removeById( aId );
  // onSelectionChanged();
  // }
  // }

  /**
   * Скрывает набор вершин.
   *
   * @param aVertexSet IVedVertexSetView - набор вершин
   */
  public void hideVertexSet( IVedVertexSetView aVertexSet ) {
    aVertexSet.setVisible( false );
    for( IVedVertex v : aVertexSet.listVertexes() ) {
      removeVertexScreenObject( v.id() );
    }
    if( mouseListener() != null ) {
      mouseListener().setScreenObjects( screenObjects );
    }
    vedScreen().paintingManager().redraw();
  }

  /**
   * Отображает набор вершин.
   *
   * @param aVertexSet IVedVertexSetView - набор вершин
   */
  public void showVertexSet( IVedVertexSetView aVertexSet ) {
    aVertexSet.setVisible( true );

    for( IVedVertex v : aVertexSet.listVertexes() ) {
      if( !hasVertexScreenObject( v.id() ) ) { // если нет screenObject соответствующего вершине
        screenObjects.add( new VedVertexScreenObject( v ) );
      }
    }

    if( mouseListener() != null ) {
      mouseListener().setScreenObjects( screenObjects );
    }
    vedScreen().paintingManager().redraw();
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  protected void onChangeLocationByKeyRequest( double aDeltaX, double aDeltaY ) {
    for( IVedComponentView view : selectedViews() ) {
      view.porter().shiftOn( aDeltaX, aDeltaY );
    }
    updateVertexSet();
  }

  protected void onChangeSizeByKeyRequest( double aDeltaWidth, double aDeltaHeight ) {
    if( selectedViews().size() == 0 ) {
      return;
    }
    if( selectedViews().size() == 1 ) {
      IVedComponentView view = selectedViews().first();
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

  protected abstract boolean accept( IVedComponentView aView );

  protected abstract void onVertexDragged( double aDx, double aDy, IVedVertex aVertex, ETsDragState aState );

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

  private void removeVertexScreenObject( String aVertexId ) {
    for( int i = 0; i < screenObjects.size(); i++ ) {
      IScreenObject scrObj = screenObjects.get( i );
      if( scrObj.kind() == EScreenObjectKind.VERTEX ) {
        if( ((IVedVertex)scrObj.entity()).id().equals( aVertexId ) ) {
          screenObjects.removeByIndex( i );
          break;
        }
      }
    }
  }

  private boolean hasVertexScreenObject( String aVertexId ) {
    for( int i = 0; i < screenObjects.size(); i++ ) {
      IScreenObject scrObj = screenObjects.get( i );
      if( scrObj.kind() == EScreenObjectKind.VERTEX ) {
        if( ((IVedVertex)scrObj.entity()).id().equals( aVertexId ) ) {
          return true;
        }
      }
    }
    return false;
  }

  private void updateVertexSet() {
    if( activeView != null ) {
      // ID2Rectangle d2r = activeView.outline().bounds();
      // ITsRectangle tsRect = vedScreen().coorsConvertor().rectBounds( d2r );
      // vertexSet.init( tsRect );
      vertexSet.init( selectedViews() );
      vedScreen().paintingManager().redraw();
      vedScreen().paintingManager().update();
    }
  }
}
