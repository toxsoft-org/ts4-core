package org.toxsoft.core.tsgui.ved.extra.tools;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.swtevents.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tsgui.ved.extra.decors.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.basis.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Базовый класс для всех инструментов, которые используют "активную" границу для редактирования свойств компоненты.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertexBasedTool
    extends VedAbstractEditorTool
    implements IVedViewDecorator, IVedScreenDecorator {

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
   * Реализация стратегии обработки изменения выделения по-умолчанию.
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
        hideVertexSet( vertexSet() );
      }
      else {
        vertexSet().init( selComps );
        showVertexSet( vertexSet() ); // есть выделенные компоненты
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

  // private final IPropertyChangeListener propertyChangeListener =
  // ( aSource, aPropId, aOldValue, aNewValue ) -> updateVertexSet();

  /**
   * Список экранных объектов доступных инструменту
   */
  protected IListEdit<IScreenObject> screenObjects = new ElemArrayList<>();

  IVedScreenSelectionManager selectionManager;

  DefaultSelectionChangedStrategy selectionStrategy = new DefaultSelectionChangedStrategy();

  IGenericChangeListener selectionListener = aSource -> {
    selectionStrategy.onSelectionChanged();
  };

  VedVertexBasedToolUIListener uiListener;

  protected VedAbstractVertexBasedTool( String aId, IOptionSet aParams, IVedScreen aScreen, IVedEnvironment aEnv ) {
    super( aId, aParams, aScreen, aEnv );
    selectionManager = vedScreen().selectionManager();
    uiListener = new VedVertexBasedToolUIListener( this );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractEditorTool
  //

  @Override
  public void activate() {
    screenObjects.clear();
    for( IVedComponentView view : vedScreen().listViews() ) {
      screenObjects.add( new VedComponentViewScreenObject( view ) );
    }
    selectionManager.genericChangeEventer().addListener( selectionListener );
  }

  @Override
  public void deactivate() {
    selectionManager.genericChangeEventer().removeListener( selectionListener );
  }

  @Override
  public VedVertexBasedToolUIListener screenInputListener() {
    return uiListener;
  }

  @Override
  public ITsCollectionChangeListener componentsListListener() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedScreenDecorator screenDecorator() {
    return this;
  }

  @Override
  public IVedViewDecorator viewDecorator() {
    return this;
  }

  // ------------------------------------------------------------------------------------
  // IVedScreenDecorator
  //

  @Override
  public void paintBefore( GC aGc, ITsRectangle aPaintBounds ) {
    // nop
  }

  @Override
  public void paintAfter( GC aGc, ITsRectangle aPaintBounds ) {
    // Если кол-во компонент внутри набора вершин > 1, то декорируем экран (рисуем 1 раз)
    if( vertexSet().visible() && vertexSet().componentViews().size() > 1 ) {
      vertexSet().paint( aGc );
    }
  }

  // ------------------------------------------------------------------------------------
  // IVedViewDecorator
  //

  @Override
  public void paintBefore( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // nop
  }

  @Override
  public void paintAfter( IVedComponentView aView, GC aGc, ITsRectangle aPaintBounds ) {
    // Если кол-во компонент внутри набора вершин = 1, то декорируем только этот view (рисуем 1 раз)
    if( vertexSet().visible() && vertexSet().componentViews().size() == 1 ) {
      if( vertexSet().componentViews().first().id().equals( aView.id() ) ) {
        vertexSet().paint( aGc );
      }
    }
  }

  @Override
  public void updateOnScreenConversionChange() {
    updateVertexSet();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает признак того, может ли инструмент работать с данным оъектом.<br>
   *
   * @param aScreenObj IScreenObject - экранный объект не null
   * @return <b>true</b> - подходящий объект (могу с ним работать)<br>
   *         <b>false</b> - неизвестный объект (работать не могу)
   */
  public boolean accept( IScreenObject aScreenObj ) {
    if( aScreenObj.kind() == EScreenObjectKind.VERTEX ) {
      return true;
    }
    return accept( (IVedComponentView)aScreenObj.entity() );
  }

  public IVedScreenSelectionDecorator selectionDecorator() {
    return vedEnv().tsContext().eclipseContext().get( IVedScreenSelectionDecorator.class );
  }

  public IVedScreenSelectionManager selectionManager() {
    return selectionManager;
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
    vedScreen().paintingManager().redraw();
  }

  /**
   * Обновляет курсор мыши в зависимости от ее положения.<br>
   *
   * @param aX int - экранная X координата курсора
   * @param aY int - экранная Y координата курсора
   */
  public void updateCursor( int aX, int aY ) {
    IScreenObject scrObj = objectAt( aX, aY );
    if( scrObj != null ) {
      if( scrObj.kind() == EScreenObjectKind.VERTEX ) {
        IVedVertex vertex = scrObj.entity();
        ECursorType ct = vertex.cursorType();
        vedScreen().paintingManager().setCursor( cursorManager().getCursor( ct ) );
        return;
      }
    }
    doUpdateCursor( scrObj );
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

  protected abstract void doUpdateCursor( IScreenObject aScrObj );

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
  // to use
  //

  /**
   * Возвращает эранный объект находящийся по курсором мыши (содержащий точку aX, aY).<br>
   * Если несколько объектов нахожятся под курсором мыши, то возвращается первый в порядке z-order.
   *
   * @param aX int - экранная X координата
   * @param aY int - экранная Y координата
   * @return IScreenObject - экранныq объект находящийся по курсором мыши
   */
  protected IScreenObject objectAt( int aX, int aY ) {
    for( IScreenObject scrObj : screenObjects ) {
      if( scrObj.containsScreenPoint( aX, aY ) ) {
        return scrObj;
      }
    }
    return null;
  }

  /**
   * Возвращает список экранных объектов находящихся по курсором мыши (содержащих точку aX, aY).
   *
   * @param aX int - экранная X координата
   * @param aY int - экранная Y координата
   * @return IList&ltIScreenObject> - список экранных объектов находящихся по курсором мыши
   */
  protected IList<IScreenObject> objectsAt( int aX, int aY ) {
    IListEdit<IScreenObject> result = new ElemArrayList<>();
    for( IScreenObject scrObj : screenObjects ) {
      if( scrObj.containsScreenPoint( aX, aY ) ) {
        result.add( scrObj );
      }
    }
    return result;
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
    if( selectedViews().size() > 0 ) {
      vertexSet().init( selectedViews() );
      vedScreen().paintingManager().redraw();
      vedScreen().paintingManager().update();
    }
  }
}
