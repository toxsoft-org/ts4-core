package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.incub.geom.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Базовый класс обработчиков мыши инструментов редатора.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractToolMouseHandler
    implements IVedMouseHandler {

  class MouseDownInfo {

    /**
     * Событие мыши
     */
    private final MouseEvent mouseEvent;

    /**
     * Двух-мерный объект под курсором мыши в момент нажатия
     */
    private final IScreenObject hovObject;

    /**
     * Момент времени в ms когда была нажата кнопка мыши
     */
    private final long time;

    MouseDownInfo( MouseEvent aEvent ) {
      mouseEvent = aEvent;
      hovObject = objectAt( aEvent.x, aEvent.y );
      time = System.currentTimeMillis();
    }

    MouseEvent mouseEvent() {
      return mouseEvent;
    }

    IScreenObject hoveredObject() {
      return hovObject;
    }

    long time() {
      return time;
    }
  }

  static class StartDragInfo {

    private final MouseEvent mouseEvent;

    private final IStridablesList<IScreenObject> draggingObjects;

    StartDragInfo( MouseEvent aMouseEvent, IStridablesList<IScreenObject> aDraggingObjects ) {
      mouseEvent = aMouseEvent;
      draggingObjects = aDraggingObjects;
    }

    MouseEvent mouseEvent() {
      return mouseEvent;
    }

    IStridablesList<IScreenObject> draggingObjects() {
      return draggingObjects;
    }
  }

  @Override
  public void onMouseUp( MouseEvent aEvent ) {
    if( dragging ) { // если идет процесс "перетаскивания", то закончим его
      dragExecutor.doFinishDrag( dragInfo.mouseEvent(), aEvent );
      canvas.setCursor( null );
      afterDragEnded();
      clearInternalState();
      return;
    }

    if( mouseDownInfo != null ) { // проверим не случился ли click
      hoveredObject = objectAt( aEvent.x, aEvent.y );
      if( hoveredObject == mouseDownInfo.hoveredObject() && aEvent.button == mouseDownInfo.mouseEvent().button ) {
        clearInternalState();
        onClick( hoveredObject, aEvent );
        return;
      }
    }
    clearInternalState();
    doOnMouseUp( aEvent );
  }

  @Override
  public void onMouseDown( MouseEvent aEvent ) {
    mouseDownInfo = new MouseDownInfo( aEvent );
    IStridablesList<IScreenObject> draggingObjects = objectsForDrag( hoveredObject, aEvent );
    dragExecutor = dragExecutor( hoveredObject );
    if( dragExecutor != IVedDragExecutor.NULL ) {
      readyForDrag = true;
      dragInfo = new StartDragInfo( aEvent, draggingObjects );
      return;
    }
    hoveredObject = objectAt( aEvent.x, aEvent.y );
    doOnMouseDown( aEvent );
  }

  @Override
  public void onMouseDoubleClick( MouseEvent aEvent ) {
    clearInternalState(); // очистим внутренние флаги и состояние
    doOnMouseDoubleClick( aEvent );
  }

  @Override
  public void onMouseMove( MouseEvent aEvent ) {
    cursorPos.x = aEvent.x;
    cursorPos.y = aEvent.y;

    if( readyForDrag ) {
      readyForDrag = false;
      beforeDragStarted();
      Cursor cursor = dragExecutor.onStartDrag( dragInfo.mouseEvent() );
      canvas.setCursor( cursor );
      dragging = true;
    }
    if( dragging ) {
      Cursor cursor = dragExecutor.doDrag( dragInfo.mouseEvent(), aEvent );
      canvas.setCursor( cursor );
      return;
    }

    IScreenObject hovObj = objectAt( aEvent.x, aEvent.y );
    if( hovObj != hoveredObject ) {
      onObjectOut( hoveredObject );
      if( hovObj != null ) {
        onObjectIn( hovObj );
      }
    }
    hoveredObject = hovObj;
  }

  /**
   * Признак того, что все ресурсы были освобождены
   */
  boolean disposed = false;

  /**
   * Признак того, что следующее перемещение мыши должно трактоваться как "перетаскивание"
   */
  boolean readyForDrag = false;

  /**
   * Признак того, что идет "перетаскивание"
   */
  boolean dragging = false;

  /**
   * Информация о начале "перетаскивания"
   */
  StartDragInfo dragInfo = null;

  /**
   * Информация о последнем нажатии кнопки мыши или null после отпускания кнопки мыши нажатия не было
   */
  MouseDownInfo mouseDownInfo = null;

  /**
   * Объект над которым находится курсор мыши
   */
  IScreenObject hoveredObject = null;

  /**
   * Холст рисования
   */
  protected VedScreen canvas = null;

  // /**
  // * "Инструмент" редактора
  // */
  // protected final VedAbstractEditorTool tool;

  /**
   * Обработчик событий перетаскивания
   */
  private IVedDragExecutor dragExecutor = IVedDragExecutor.NULL;

  /**
   * Положение курсора мыши
   */
  Point cursorPos = new Point( 0, 0 );

  /**
   * Список экранных объектов доступных обработчику мыши
   */
  IStridablesListEdit<IScreenObject> screenObjects = new StridablesList<>();

  // /**
  // * Конструктор.<br>
  // *
  // * @param aTool VedAbstractEditorTool - "инструмент" редактора
  // */
  // public VedAbstractToolMouseHandler( VedAbstractEditorTool aTool ) {
  // tool = aTool;
  // }

  /**
   * Конструктор.<br>
   */
  public VedAbstractToolMouseHandler() {
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IDragEventProducer}
  //

  @Override
  public void addDragListener( IVedDragListener aListener ) {
    throw new TsUnsupportedFeatureRtException(); // не допускается добавление слушателей "перетаскивания"
  }

  @Override
  public void removeDragListener( IVedDragListener aListener ) {
    throw new TsUnsupportedFeatureRtException(); // не допускается удаление слушателей "перетаскивания"
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IVedMouseHandler}
  //

  @Override
  public final ID2Point cursorPosition() {
    return new D2Point( cursorPos.x, cursorPos.y );
  }

  @Override
  public IScreenObject objectAt( int aX, int aY ) {
    for( IScreenObject obj : screenObjects ) {
      if( obj.containsScreenPoint( aX, aY ) ) {
        return obj;
      }
    }
    return null;
  }

  @Override
  public IStridablesList<IScreenObject> objectsAt( int aX, int aY ) {
    IStridablesListEdit<IScreenObject> result = new StridablesList<>();
    for( IScreenObject obj : screenObjects ) {
      if( obj.containsScreenPoint( aX, aY ) ) {
        result.add( obj );
      }
    }
    return result;
  }

  @Override
  public final IScreenObject hoveredObject() {
    return hoveredObject;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IDisposed}
  //

  @Override
  public boolean isDisposed() {
    return disposed;
  }

  @Override
  public void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Вызывается в момент активации обработчика.<br>
   *
   * @param aCanvas IEditingCanvas - холст рисования
   * @param aObjects IStridablesList&lt;IScreenObject> - список объектов доступных обработчику
   */
  public void activate( VedScreen aCanvas, IStridablesList<IScreenObject> aObjects ) {
    hoveredObject = null;
    canvas = aCanvas;
    screenObjects.clear();
    screenObjects.addAll( aObjects );
    onActivate();
  }

  /**
   * Вызывается в момент деактивации обработчика.<br>
   */
  public void deactivate() {
    if( dragging ) {
      dragExecutor.doCancelDrag( dragInfo.mouseEvent(), null );
      canvas.setCursor( null );
    }
    onDeactivate();
    canvas = null;
    clearInternalState();
  }

  /**
   * Устанавливает список объектов доступных обработчику.<br>
   *
   * @param aObjects IStridablesList&lt;IScreenObject> - список объектов доступных обработчику
   */
  public void setScreenObjects( IStridablesList<IScreenObject> aObjects ) {
    screenObjects.clear();
    screenObjects.addAll( aObjects );
  }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //

  /**
   * Возвращает обработчик событий перетаскивания.<br>
   *
   * @param aHoveredObject IShape2dView - объект под курсором мыши
   * @return IDragExecutor - обработчик событий перетаскивания
   */
  protected abstract IVedDragExecutor dragExecutor( IScreenObject aHoveredObject );

  /**
   * Возвращает список объектов для "перетаскивания".<br>
   *
   * @param aHoveredObject IObject2d - объект под курсором мыши
   * @param aEvent MouseEvent - информация о положении курсора мыши и состоянии кнопок
   * @return IStridablesList&lt;IScreenObject> - список объектов для "перетаскивания" (м.б. пустным)
   */
  protected abstract IStridablesList<IScreenObject> objectsForDrag( IScreenObject aHoveredObject, MouseEvent aEvent );

  /**
   * Освобождает все системные ресурсы
   */
  protected abstract void doDispose();

  // ------------------------------------------------------------------------------------
  // Методы для возможного переопределения в наследниках
  //

  protected void onActivate() {
    // nop
  }

  protected void onDeactivate() {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doOnMouseDown( MouseEvent aEvent ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doOnMouseUp( MouseEvent aEvent ) {
    // nop
  }

  @SuppressWarnings( "unused" )
  protected void doOnMouseDoubleClick( MouseEvent aEvent ) {
    // nop
  }

  protected void beforeDragStarted() {
    // nop
  }

  protected void afterDragEnded() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void clearInternalState() {
    readyForDrag = false;
    dragging = false;
    dragInfo = null;
    mouseDownInfo = null;
  }

}
