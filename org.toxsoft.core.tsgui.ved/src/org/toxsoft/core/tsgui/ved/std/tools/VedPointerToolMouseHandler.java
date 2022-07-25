package org.toxsoft.core.tsgui.ved.std.tools;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * Обработчик мыши для инструмента "Указатель".
 * <p>
 *
 * @author vs
 */
public class VedPointerToolMouseHandler
    extends VedAbstractToolMouseHandler
    implements IVedContextable {

  Cursor cursorHand;

  IVedDragExecutor dragExecutor = IVedDragExecutor.NULL;

  StdDragVedCompViewsListener stdDragListener = null;

  /**
   * Набор вершин прямоугольника
   */
  private VedRectVertexSetView vertexSet = null;

  private IVedComponentView slaveShape = null;

  IVedDragObjectsListener moveListener = ( aDx, aDy, aShapes, aState ) -> {
    if( vertexSet != null ) {
      if( aShapes.size() > 0 ) {

        IScreenObject view = aShapes.first();

        Rectangle r1 = vertexSet.bounds();
        r1 = new Rectangle( r1.x, r1.y, r1.width, r1.height );
        vertexSet.update( aDx, aDy, view.id() );
        Rectangle r2 = vertexSet.bounds();
        Rectangle rr = substract( r2, r1 );

        double zf = screen().getConversion().zoomFactor();

        ID2Point d2p = screen().coorsConvertor().convertPoint( rr.x, rr.y );

        slaveShape.porter().shiftOn( d2p.x(), d2p.y() );
        double w = slaveShape.outline().bounds().width() + rr.width / zf;
        double h = slaveShape.outline().bounds().height() + rr.height / zf;
        slaveShape.porter().setSize( w, h );
        screen().paintingManager().redraw();
      }
    }
    else {
      double zf = screen().getConversion().zoomFactor();
      stdDragListener.onShapesDrag( aDx / zf, aDy / zf, aShapes, aState );
    }
  };

  VedMoveObjectsDragExecutor moveExecutor;

  public VedPointerToolMouseHandler( IVedEnvironment aEnv, IVedScreen aScreen ) {
    super( aEnv, aScreen );
    cursorHand = cursorManager().getCursor( ECursorType.HAND );
    stdDragListener = new StdDragVedCompViewsListener( screen() );
  }

  @Override
  protected void onActivate() {
    // stdDragListener = new StdDragVedCompViewsListener( screen() );
    for( IVedComponentView view : screen().listViews() ) {
      screenObjects.add( new VedComponentViewScreenObject( view ) );
    }
  }

  @Override
  protected void onDeactivate() {
    clearVertexSet();
    screenObjects.clear();
  }

  @Override
  protected IVedDragExecutor dragExecutor( IScreenObject aHoveredObject ) {
    if( aHoveredObject == null ) {
      return IVedDragExecutor.NULL;
    }

    moveExecutor = new VedMoveObjectsDragExecutor( new StridablesList<>( aHoveredObject ), vedEnv() );
    moveExecutor.addVedDragObjectsListener( moveListener );
    return moveExecutor;
  }

  @Override
  protected IStridablesList<IScreenObject> objectsForDrag( IScreenObject aHoveredObject, MouseEvent aEvent ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doDispose() {
    // TODO Auto-generated method stub
  }

  @Override
  public void onClick( IScreenObject aObj, MouseEvent aEvent ) {
    if( aObj != null && vertexSet == null ) { // клик на объекте при отсутствии активной границы
      if( (aEvent.stateMask & SWT.CTRL) != 0 ) {
        IVedComponentView view = aObj.entity();
        if( view != null ) {
          screen().selectionManager().toggleSelection( view.component() );
        }
        return;
      }
    }

    if( aObj != null ) {
      IStridablesListEdit<IScreenObject> scrObjs = new StridablesList<>();
      slaveShape = aObj.entity();
      vertexSet = new VedRectVertexSetView( aObj.bounds(), tsContext() );
      screenObjects.add( vertexSet );
      for( IScreenObject vertex : vertexSet.listVertexes() ) {
        screenObjects.add( vertex );
        scrObjs.add( vertex );
      }
      screen().paintingManager().redraw();
      setScreenObjects( scrObjs );
    }
    else {
      if( vertexSet != null ) {
        clearVertexSet();
      }
      IStridablesListEdit<IScreenObject> scrObjs = new StridablesList<>();
      for( IVedComponentView view : screen().listViews() ) {
        IScreenObject obj = new VedComponentViewScreenObject( view );
        scrObjs.add( obj );
      }
      setScreenObjects( scrObjs );
    }
  }

  @Override
  public void onObjectIn( IScreenObject aObj ) {
    Cursor cursor = cursorManager().findCursor( aObj.cursorType().id() );
    if( cursor == null ) {
      cursor = cursorHand;
    }
    screen().paintingManager().setCursor( cursor );
  }

  @Override
  public void onObjectOut( IScreenObject aShape ) {
    screen().paintingManager().setCursor( null );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  /**
   * Вызывается при измнениии коэффициента масштабирования.
   *
   * @param aZoomFactor double - кэффициент масштабирования
   */
  public void onZoomFactorChanged( double aZoomFactor ) {
    if( vertexSet != null ) {
      ID2Rectangle d2r = slaveShape.outline().bounds();
      int x = (int)Math.round( d2r.x1() * aZoomFactor );
      int y = (int)Math.round( d2r.y1() * aZoomFactor );
      int w = (int)Math.round( d2r.width() * aZoomFactor );
      int h = (int)Math.round( d2r.height() * aZoomFactor );

      vertexSet.setRect( new Rectangle( x, y, w, h ) );
    }
  }

  VedRectVertexSetView vertexSet() {
    return vertexSet;
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  @Override
  protected void beforeDragStarted() {
    if( vertexSet != null ) {
      vertexSet.setVisible( false );
      for( IVedVertex vertex : vertexSet.listVertexes() ) {
        vertex.setVisible( false );
      }
    }
  }

  @Override
  protected void afterDragEnded() {
    super.afterDragEnded();
    if( vertexSet != null ) {
      vertexSet.setVisible( true );
      for( IVedVertex vertex : vertexSet.listVertexes() ) {
        vertex.setVisible( true );
      }
      screen().paintingManager().redraw();
    }
  }

  void clearVertexSet() {
    if( vertexSet != null ) {
      screenObjects.removeById( vertexSet.id() );
      for( IVedVertex vertex : vertexSet.listVertexes() ) {
        screenObjects.removeById( vertex.id() );
      }
      vertexSet = null;
      slaveShape = null;
      screen().paintingManager().redraw();
    }
  }

  private static Rectangle substract( Rectangle aRect1, Rectangle aRect2 ) {
    Rectangle r = new Rectangle( 0, 0, 0, 0 );
    r.x = aRect1.x - aRect2.x;
    r.y = aRect1.y - aRect2.y;
    r.width = aRect1.width - aRect2.width;
    r.height = aRect1.height - aRect2.height;
    return r;
  }
}
