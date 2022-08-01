package org.toxsoft.core.tsgui.ved.utils.drag;

import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Стандартный обработчик события о перемещении "фигур".
 * <p>
 *
 * @author vs
 */
public class StdDragVedCompViewsListener
    implements IVedDragObjectsListener {

  /**
   * Холст рисования
   */
  private final IVedScreen canvas;

  /**
   * Конструктор.<br>
   *
   * @param aCanvas Canvas - холст рисования
   */
  public StdDragVedCompViewsListener( IVedScreen aCanvas ) {
    canvas = aCanvas;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса {@link IMoveShapesEventListener}
  //

  @Override
  public void onShapesDrag( double aDx, double aDy, IList<IScreenObject> aShapes, ETsDragState aState ) {
    if( aState != ETsDragState.START && aState != ETsDragState.FINISH ) {
      moveShapes( aDx, aDy, aShapes );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  private void moveShapes( double aDx, double aDy, IList<IScreenObject> aObjects ) {
    // Rectangle r = new Rectangle( 0, 0, 0, 0 );
    for( IScreenObject obj : aObjects ) {
      IVedComponentView view = (IVedComponentView)obj.entity();
      // r = r.union( VedScreen.boundsToScreen( view ) );
      view.porter().shiftOn( aDx, aDy );
      // r = r.union( VedScreen.boundsToScreen( view ) );
    }
    // canvas.redraw( r.x, r.y, r.width + 1, r.height + 1, false );
    canvas.paintingManager().redraw();
    canvas.paintingManager().update();
  }

}
