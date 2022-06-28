package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.incub.geom.*;
import org.toxsoft.core.tsgui.ved.std.tool.*;
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
    private final IVedComponentView hovObject;

    /**
     * Момент времени в ms когда была нажата кнопка мыши
     */
    private final long time;

    MouseDownInfo( MouseEvent aEvent ) {
      mouseEvent = aEvent;
      // hovObject = AbstractToolMouseHandler.this.hoveredObject();
      hovObject = objectAt( aEvent.x, aEvent.y );
      time = System.currentTimeMillis();
    }

    MouseEvent mouseEvent() {
      return mouseEvent;
    }

    IVedComponentView hoveredObject() {
      return hovObject;
    }

    long time() {
      return time;
    }
  }

  static class StartDragInfo {

    private final MouseEvent mouseEvent;

    private final IStridablesList<IVedComponentView> draggingObjects;

    StartDragInfo( MouseEvent aMouseEvent, IStridablesList<IVedComponentView> aDraggingObjects ) {
      mouseEvent = aMouseEvent;
      draggingObjects = aDraggingObjects;
    }

    MouseEvent mouseEvent() {
      return mouseEvent;
    }

    IStridablesList<IVedComponentView> draggingObjects() {
      return draggingObjects;
    }
  }

  // MouseListener mouseListener = new MouseListener() {

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
        onClick( hoveredObject );
        return;
      }
    }
    clearInternalState();
    doOnMouseUp( aEvent );
  }

  @Override
  public void onMouseDown( MouseEvent aEvent ) {
    mouseDownInfo = new MouseDownInfo( aEvent );
    IStridablesList<IVedComponentView> draggingObjects = objectsForDrag( hoveredObject, aEvent );
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
  // };

  // MouseMoveListener mouseMoveListener = new MouseMoveListener() {

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

    IVedComponentView hovObj = objectAt( aEvent.x, aEvent.y );
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
  IVedComponentView hoveredObject = null;

  /**
   * Холст рисования
   */
  protected VedScreen canvas = null;

  /**
   * "Инструмент" редактора
   */
  protected final VedAbstractEditorTool tool;

  /**
   * Обработчик событий перетаскивания
   */
  private IVedDragExecutor dragExecutor = IVedDragExecutor.NULL;

  /**
   * Положение курсора мыши
   */
  Point cursorPos = new Point( 0, 0 );

  /**
   * Конструктор.<br>
   *
   * @param aTool VedAbstractEditorTool - "инструмент" редактора
   */
  public VedAbstractToolMouseHandler( VedAbstractEditorTool aTool ) {
    tool = aTool;
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
  public IVedComponentView objectAt( int aX, int aY ) {
    IVedComponentView result = null;
    for( IVedComponentView shape : tool.listViews() ) {
      if( shape.outline().contains( aX, aY ) ) {
        result = shape;
      }
    }
    return result;
  }

  @Override
  public IStridablesList<IVedComponentView> objectsAt( int aX, int aY ) {
    IStridablesListEdit<IVedComponentView> result = new StridablesList<>();
    for( IVedComponentView obj : tool.listViews() ) {
      if( obj.outline().contains( aX, aY ) ) {
        result.add( obj );
      }
    }
    return result;
  }

  @Override
  public final IVedComponentView hoveredObject() {
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
   */
  public void activate( VedScreen aCanvas ) {
    hoveredObject = null;
    canvas = aCanvas;
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

  // public IEditingCanvas canvas() {
  // return canvas;
  // }

  // ------------------------------------------------------------------------------------
  // Методы для переопределения в наследниках
  //

  /**
   * Возвращает обработчик событий перетаскивания.<br>
   *
   * @param aHoveredObject IShape2dView - объект под курсором мыши
   * @return IDragExecutor - обработчик событий перетаскивания
   */
  protected abstract IVedDragExecutor dragExecutor( IVedComponentView aHoveredObject );

  /**
   * Возвращает список объектов для "перетаскивания".<br>
   *
   * @param aHoveredObject IObject2d - объект под курсором мыши
   * @param aEvent MouseEvent - информация о положении курсора мыши и состоянии кнопок
   * @return IStridablesList&lt;IVedComponentView> - список объектов для "перетаскивания" (м.б. пустным)
   */
  protected abstract IStridablesList<IVedComponentView> objectsForDrag( IVedComponentView aHoveredObject,
      MouseEvent aEvent );

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
