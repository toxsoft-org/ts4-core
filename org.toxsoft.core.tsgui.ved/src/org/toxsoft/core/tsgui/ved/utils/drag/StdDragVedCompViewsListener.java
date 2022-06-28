package org.toxsoft.core.tsgui.ved.utils.drag;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Стандартный обработчик события о перемещении "фигур".
 * <p>
 *
 * @author vs
 */
public class StdDragVedCompViewsListener
    implements IVedDragCompViewsListener {

  /**
   * Холст рисования
   */
  private final VedScreen canvas;

  /**
   * Конструктор.<br>
   *
   * @param aCanvas Canvas - холст рисования
   */
  public StdDragVedCompViewsListener( VedScreen aCanvas ) {
    canvas = aCanvas;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IMoveShapesEventListener}
  //

  @Override
  public void onShapesDrag( double aDx, double aDy, IStridablesList<IVedComponentView> aShapes, ETsDragState aState ) {
    if( aState != ETsDragState.START && aState != ETsDragState.FINISH ) {
      moveShapes( aDx, aDy, aShapes );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private void moveShapes( double aDx, double aDy, IStridablesList<IVedComponentView> aViews ) {
    Rectangle r = new Rectangle( 0, 0, 0, 0 );
    for( IVedComponentView view : aViews ) {
      r = r.union( VedScreen.boundsToScreen( view ) );
      view.porter().shiftOn( aDx, aDy );
      r = r.union( VedScreen.boundsToScreen( view ) );
    }
    canvas.redraw( r.x, r.y, r.width + 1, r.height + 1, false );
  }

}
