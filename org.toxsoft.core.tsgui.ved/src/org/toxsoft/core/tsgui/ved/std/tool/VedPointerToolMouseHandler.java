package org.toxsoft.core.tsgui.ved.std.tool;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

public class VedPointerToolMouseHandler
    extends VedAbstractToolMouseHandler {

  ITsCursorManager cursorManager;

  Cursor cursorHand;

  IVedDragExecutor dragExecutor = IVedDragExecutor.NULL;

  StdDragVedCompViewsListener stdDragListener = null;

  Rectangle sssR;

  IVedDragCompViewsListener moveListener = ( aDx, aDy, aShapes, aState ) -> {
    // FIXME раскомментировать
    // if( vertexSet != null ) {
    // if( aShapes.size() > 0 ) {
    // if( aState == ETsDragState.START ) {
    // sssR = createRect( slaveShape.bounds() );
    // }
    //
    // IVedComponentView view = aShapes.first();
    //
    // Rectangle r1 = vertexSet.bounds();
    // r1 = new Rectangle( r1.x, r1.y, r1.width, r1.height );
    // vertexSet.update( aDx, aDy, view.id() );
    // Rectangle r2 = vertexSet.bounds();
    // Rectangle rr = substract( r2, r1 );
    // slaveShape.shiftOn( rr.x, rr.y );
    // slaveShape.setSize( slaveShape.bounds().width + rr.width, slaveShape.bounds().height + rr.height );
    // if( aState == ETsDragState.FINISH ) {
    // slaveShape.onDragFinished();
    // IUndoManager um = appContext.get( IImedService.class ).undoManager();
    // Rectangle r = slaveShape.bounds();
    // um.addUndoredoItem( new UndoSizeLocationChangePerformer( r.x - sssR.x, r.y - sssR.y, r.width - sssR.width,
    // r.height - sssR.height, slaveShape ) );
    // }
    // }
    // canvas.redraw();
    // }
    // else {
    // if( aState == ETsDragState.START ) {
    // sssR = createRect( aShapes.first().bounds() );
    // }
    // stdDragListener.onShapesDrag( aDx, aDy, aShapes, aState );
    // if( aState == ETsDragState.FINISH ) {
    // IUndoManager um = appContext.get( IImedService.class ).undoManager();
    // Rectangle r = aShapes.first().bounds();
    // um.addUndoredoItem( new UndoMoveShapesPerformer( r.x - sssR.x, r.y - sssR.y, aShapes ) );
    // }
    // }
    stdDragListener.onShapesDrag( aDx, aDy, aShapes, aState );
  };

  VedMoveCompViewsDragExecutor moveExecutor;

  private final IEclipseContext appContext;

  // FIXME раскомментировать
  // private RectVertexSetView vertexSet = null;

  private IVedComponentView slaveShape = null;

  public VedPointerToolMouseHandler( VedAbstractEditorTool aTool, IEclipseContext aAppContext ) {
    super( aTool );
    appContext = aAppContext;
    cursorManager = new TsCursorManager( aAppContext );
    cursorHand = cursorManager.getCursor( ECursorType.HAND );
  }

  @Override
  protected void onActivate() {
    stdDragListener = new StdDragVedCompViewsListener( canvas );
  }

  @Override
  protected void onDeactivate() {
    clearVertexSet();
  }

  @Override
  protected IVedDragExecutor dragExecutor( IVedComponentView aHoveredObject ) {
    if( aHoveredObject == null ) {
      return IVedDragExecutor.NULL;
    }
    moveExecutor = new VedMoveCompViewsDragExecutor( new StridablesList<>( aHoveredObject ), appContext );
    moveExecutor.addVedDragCompViewsEventListener( moveListener );
    return moveExecutor;
  }

  @Override
  protected IStridablesList<IVedComponentView> objectsForDrag( IVedComponentView aHoveredObject, MouseEvent aEvent ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doDispose() {
    // TODO Auto-generated method stub
  }

  @Override
  public void onClick( IVedComponentView aShape ) {
    // FIXME раскомментировать
    // if( aShape != null ) {
    // slaveShape = aShape;
    // vertexSet = new RectVertexSetView( aShape.bounds(), appContext );
    // canvas.add( vertexSet );
    // for( IVedComponentView view : vertexSet.listShapes( IShape2dFilter.TRUE ) ) {
    // canvas.add( view );
    // }
    // canvas.redraw();
    // }
    // else {
    // if( vertexSet != null ) {
    // clearVertexSet();
    // }
    // }
  }

  @Override
  public void onObjectIn( IVedComponentView aShape ) {
    // FIXME раскомментировать
    // Cursor cursor = aShape.cursor();
    // if( cursor == null ) {
    // cursor = cursorHand;
    // }
    // canvas.setCursor( cursor );
    canvas.setCursor( cursorHand );
  }

  @Override
  public void onObjectOut( IVedComponentView aShape ) {
    canvas.setCursor( null );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  IStridablesList<IVedComponentView> listShapes() {
    // FIXME раскомментировать
    // if( vertexSet != null ) {
    // return vertexSet.listShapes( IShape2dFilter.TRUE );
    // }
    // return canvas.listShapes( tool );
    return canvas.listViews( ITsFilter.ALL );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  @Override
  protected void beforeDragStarted() {
    // FIXME раскомментировать
    // if( vertexSet != null ) {
    // vertexSet.setVisible( false );
    // }
  }

  @Override
  protected void afterDragEnded() {
    super.afterDragEnded();
    // FIXME раскомментировать
    // if( vertexSet != null ) {
    // vertexSet.setVisible( true );
    // canvas.redraw();
    // }
  }

  void clearVertexSet() {
    // FIXME раскомментировать
    // if( vertexSet != null ) {
    // canvas.remove( vertexSet.id() );
    // for( IShape2dView view : vertexSet.listShapes( IShape2dFilter.TRUE ) ) {
    // canvas.remove( view.id() );
    // }
    // vertexSet = null;
    // slaveShape = null;
    // canvas.redraw();
    // }
  }

  private static Rectangle createRect( Rectangle aRect ) {
    Rectangle r = new Rectangle( 0, 0, 0, 0 );
    r.x = aRect.x;
    r.y = aRect.y;
    r.width = aRect.width;
    r.height = aRect.height;
    return r;
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
