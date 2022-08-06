package org.toxsoft.core.tsgui.ved.tools_old.drag;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.impl.*;
import org.toxsoft.core.tsgui.ved.tools_old.tools2.impl.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

/**
 * Обработчик событий перетаскивания.
 * <p>
 * Генерирует сообщения о перемещении экранных объектов.
 *
 * @author vs
 */
public class VedMoveObjectsDragExecutor
    implements IVedDragExecutor, IVedDragObjectsEventProducer, IVedContextable {

  private final IListEdit<IVedDragObjectsListener> listeners = new ElemLinkedBundleList<>();

  private boolean paused = false;

  IList<IScreenObject> shapes;

  int startX = -1;
  int startY = -1;
  int prevX  = -1;
  int prevY  = -1;

  Cursor handCursor;

  private final IVedEnvironment vedEnv;

  /**
   * Конструктор.<br>
   *
   * @param aShapes IList&lt;IScreenObject> - список "перетаскиваемых" фигур
   * @param aEnv IVedEnvironment - окружение редактора
   */
  public VedMoveObjectsDragExecutor( IList<IScreenObject> aShapes, IVedEnvironment aEnv ) {
    shapes = aShapes;
    vedEnv = aEnv;
    handCursor = cursorManager().getCursor( ECursorType.HAND );
  }

  @Override
  final public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
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
  public void addVedDragObjectsListener( IVedDragObjectsListener aListener ) {
    if( !listeners.hasElem( aListener ) ) {
      listeners.add( aListener );
    }
  }

  @Override
  public void removeVedDragObjectsListener( IVedDragObjectsListener aListener ) {
    listeners.remove( aListener );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  void fireShapesDragEvent( int aDx, int aDy, ETsDragState aDragState ) {
    if( !paused ) {
      for( IVedDragObjectsListener l : listeners ) {
        l.onShapesDrag( aDx, aDy, shapes, aDragState );
      }
    }
  }

}
