package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Обработчик событий перетаскивания.
 * <p>
 * Генерирует сообщения о перемещении представлений компонент.
 *
 * @author vs
 */
public class VedMoveCompViewsDragExecutor
    implements IVedDragExecutor, IVedDragCompViewsEventProducer {

  private final IListEdit<IVedDragCompViewsListener> listeners = new ElemLinkedBundleList<>();

  private boolean paused   = false;
  private boolean wasEvent = false;

  IStridablesList<IVedComponentView> shapes;

  int startX = -1;
  int startY = -1;
  int prevX  = -1;
  int prevY  = -1;

  Cursor handCursor;

  /**
   * Конструктор.<br>
   *
   * @param aShapes IStridablesList&lt;IShape2dView> - список "перетаскиваемых" фигур
   * @param aAppContext IEclipseContext - контекст окна
   */
  public VedMoveCompViewsDragExecutor( IStridablesList<IVedComponentView> aShapes, IEclipseContext aAppContext ) {
    shapes = aShapes;
    ITsCursorManager cursorManager = new TsCursorManager( aAppContext );
    handCursor = cursorManager.getCursor( ECursorType.HAND );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IDragExecutor}
  //

  @Override
  public Cursor onStartDrag( MouseEvent aStartEvent ) {
    startX = aStartEvent.x;
    startY = aStartEvent.y;
    prevX = startX;
    prevY = startY;
    fireShapesDragEvent( 0, 0, ETsDragState.START );
    return handCursor;
  }

  @Override
  public Cursor doDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    int dx = aCurrEvent.x - prevX;
    int dy = aCurrEvent.y - prevY;
    fireShapesDragEvent( dx, dy, ETsDragState.DRAGGING );
    prevX = aCurrEvent.x;
    prevY = aCurrEvent.y;
    return handCursor;
  }

  @Override
  public boolean doFinishDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    int dx = aCurrEvent.x - prevX;
    int dy = aCurrEvent.y - prevY;
    fireShapesDragEvent( dx, dy, ETsDragState.FINISH );
    return true;
  }

  @Override
  public boolean doCancelDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    fireShapesDragEvent( aCurrEvent.x - aStartEvent.x, aCurrEvent.y - aStartEvent.y, ETsDragState.CANCEL );
    return true;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IMoveShapesEventProducer}
  //

  @Override
  public void addVedDragCompViewsEventListener( IVedDragCompViewsListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeVedDragCompViewsEventListener( IVedDragCompViewsListener aListener ) {
    listeners.remove( aListener );
  }

  @Override
  public void pauseFiring() {
    paused = true;
  }

  @Override
  public void resumeFiring( boolean aFireDelayed ) {
    paused = false;
    if( wasEvent ) {
      wasEvent = false;
      if( aFireDelayed ) {
        // fireChangeEvent( lastX, lastY );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void fireShapesDragEvent( int aDx, int aDy, ETsDragState aDragState ) {
    if( !paused ) {
      for( IVedDragCompViewsListener l : listeners ) {
        l.onShapesDrag( aDx, aDy, shapes, aDragState );
      }
    }
  }

}
