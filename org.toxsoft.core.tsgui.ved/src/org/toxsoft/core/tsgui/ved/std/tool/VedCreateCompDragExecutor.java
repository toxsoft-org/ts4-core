package org.toxsoft.core.tsgui.ved.std.tool;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * Обработчик перетаскивания начального создания и задания размеров компоненты.
 * <p>
 *
 * @author vs
 */
public class VedCreateCompDragExecutor
    implements IVedDragExecutor, IGenericChangeEventCapable {

  int startX;
  int startY;

  GenericChangeEventer gcEventer;

  private final Rectangle currRect = new Rectangle( 0, 0, 0, 0 );

  private boolean finished = false;

  VedCreateCompDragExecutor() {
    gcEventer = new GenericChangeEventer( this );
  }

  // ------------------------------------------------------------------------------------
  // {@link IVedDragExecutor}
  //

  @Override
  public Cursor onStartDrag( MouseEvent aStartEvent ) {
    startX = aStartEvent.x;
    startY = aStartEvent.y;
    finished = false;
    return null;
  }

  @Override
  public Cursor doDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {

    currRect.x = Math.min( startX, aCurrEvent.x );
    currRect.y = Math.min( startY, aCurrEvent.y );
    currRect.width = Math.abs( startX - aCurrEvent.x );
    currRect.height = Math.abs( startY - aCurrEvent.y );

    System.out.println( "doDrag" );
    gcEventer.fireChangeEvent();
    return null;
  }

  @Override
  public boolean doFinishDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    finished = true;
    gcEventer.fireChangeEvent();
    return false;
  }

  @Override
  public boolean doCancelDrag( MouseEvent aStartEvent, MouseEvent aCurrEvent ) {
    finished = true;
    gcEventer.fireChangeEvent();
    return false;
  }

  // ------------------------------------------------------------------------------------
  // {@link IGenericChangeEventCapable}
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return gcEventer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает текущий прямоугольник, полученный в результате перетаскивания одной из его вершин.<br>
   *
   * @return Rectangle - текущий прямоугольник, полученный в результате перетаскивания одной из его вершин
   */
  public Rectangle currRect() {
    if( !finished ) {
      return currRect;
    }
    return null;
  }
}
