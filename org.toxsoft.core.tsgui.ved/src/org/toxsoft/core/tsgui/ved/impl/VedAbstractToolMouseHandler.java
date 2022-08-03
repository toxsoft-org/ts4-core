package org.toxsoft.core.tsgui.ved.impl;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.std.tools.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
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

    private final IList<IScreenObject> draggingObjects;

    StartDragInfo( MouseEvent aMouseEvent, IList<IScreenObject> aDraggingObjects ) {
      mouseEvent = aMouseEvent;
      draggingObjects = aDraggingObjects;
    }

    MouseEvent mouseEvent() {
      return mouseEvent;
    }

    IList<IScreenObject> draggingObjects() {
      return draggingObjects;
    }
  }

  @Override
  public void mouseUp( MouseEvent aEvent ) {
    if( dragging ) { // если идет процесс "перетаскивания", то закончим его
      dragExecutor.doFinishDrag( dragInfo.mouseEvent(), aEvent );
      screen.paintingManager().setCursor( null );
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
  public void mouseDown( MouseEvent aEvent ) {
    mouseDownInfo = new MouseDownInfo( aEvent );
    IList<IScreenObject> draggingObjects = objectsForDrag( hoveredObject, aEvent );
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
  public void mouseDoubleClick( MouseEvent aEvent ) {
    clearInternalState(); // очистим внутренние флаги и состояние
    doOnMouseDoubleClick( aEvent );
  }

  // @Override
  // public final void onClick( IScreenObject aShape, MouseEvent aEvent ) {
  // if( mouseConsumer != null ) {
  // mouseConsumer.onClick( aShape, aEvent );
  // }
  // doOnClick( aShape, aEvent );
  // }

  @Override
  public void mouseMove( MouseEvent aEvent ) {
    cursorPos.x = aEvent.x;
    cursorPos.y = aEvent.y;

    if( readyForDrag ) {
      readyForDrag = false;
      beforeDragStarted();
      Cursor cursor = dragExecutor.onStartDrag( dragInfo.mouseEvent() );
      screen.paintingManager().setCursor( cursor );
      dragging = true;
    }
    if( dragging ) {
      Cursor cursor = dragExecutor.doDrag( dragInfo.mouseEvent(), aEvent );
      screen.paintingManager().setCursor( cursor );
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
  private IVedScreen screen = null;

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
  protected IListEdit<IScreenObject> screenObjects = new ElemArrayList<>();

  private final IVedEnvironment vedEnv;

  /**
   * Потребитель семантических событий обработки мыши
   */
  private IMouseEventConsumer mouseConsumer = null;

  /**
   * Конструктор.<br>
   */
  public VedAbstractToolMouseHandler( IVedEnvironment aEnv, IVedScreen aScreen ) {
    screen = aScreen;
    vedEnv = aEnv;
  }

  public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
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
  // Реализация интерфейса {@link ITsMouseListener}
  //

  @Override
  public final ITsPoint cursorPosition() {
    return new TsPoint( cursorPos.x, cursorPos.y );
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
  public IList<IScreenObject> objectsAt( int aX, int aY ) {
    IListEdit<IScreenObject> result = new ElemArrayList<>();
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
  // Реализация интерфейса {@link IVedDisposable}
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
   * @param aCanvas IVedScreen - холст рисования
   * @param aObjects IStridablesList&lt;IScreenObject> - список объектов доступных обработчику
   */
  public void activate( IVedScreen aCanvas, IList<IScreenObject> aObjects ) {
    hoveredObject = null;
    screen = aCanvas;
    screenObjects.setAll( aObjects );
    onActivate();
  }

  /**
   * Вызывается в момент деактивации обработчика.<br>
   */
  public void deactivate() {
    if( dragging ) {
      dragExecutor.doCancelDrag( dragInfo.mouseEvent(), null );
      screen.paintingManager().setCursor( null );
    }
    onDeactivate();
    screen = null;
    clearInternalState();
  }

  /**
   * Устанавливает список объектов доступных обработчику.<br>
   *
   * @param aObjects IList&lt;IScreenObject> - список объектов доступных обработчику
   */
  public void setScreenObjects( IList<IScreenObject> aObjects ) {
    screenObjects.setAll( aObjects );
  }

  /**
   * Возвращает экран редактора.<br>
   *
   * @return VedScreen - экран редактора
   */
  public IVedScreen vedScreen() {
    return screen;
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
   * @return IList&lt;IScreenObject> - список объектов для "перетаскивания" (м.б. пустным)
   */
  protected abstract IList<IScreenObject> objectsForDrag( IScreenObject aHoveredObject, MouseEvent aEvent );

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

  @SuppressWarnings( "unused" )
  protected void doOnClick( IScreenObject aHoveredObject, MouseEvent aEvent ) {
    // nop
  }

  protected void beforeDragStarted() {
    // nop
  }

  protected void afterDragEnded() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public IVedScreen screen() {
    return screen;
  }

  public void setMouseEventConsumer( IMouseEventConsumer aMouseConsumer ) {
    mouseConsumer = aMouseConsumer;
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
