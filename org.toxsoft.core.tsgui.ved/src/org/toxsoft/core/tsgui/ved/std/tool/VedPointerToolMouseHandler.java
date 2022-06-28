package org.toxsoft.core.tsgui.ved.std.tool;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.incub.undoman.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.incub.ved.undoman.*;
import org.toxsoft.test.ved.exe.e4.services.*;
import org.toxsoft.test.ved.exe.imed.api.*;
import org.toxsoft.test.ved.exe.imed.api.drag.*;
import org.toxsoft.test.ved.exe.imed.api.tools.*;
import org.toxsoft.test.ved.exe.imed.common.*;
import org.toxsoft.test.ved.exe.imed.common.drag.*;
import org.toxsoft.test.ved.exe.imed.common.shapes.*;
import org.toxsoft.test.ved.exe.imed.common.undo.*;

public class VedPointerToolMouseHandler
    extends AbstractToolMouseHandler {

  ITsCursorManager cursorManager;

  Cursor cursorHand;

  IVedDragExecutor dragExecutor = IVedDragExecutor.NULL;

  Rectangle sssR;

  IDragShapesEventListener moveListener = new IDragShapesEventListener() {

    @Override
    public void onShapesDrag( double aDx, double aDy, IStridablesList<IShape2dView> aShapes, ETsDragState aState ) {
      if( vertexSet != null ) {
        if( aShapes.size() > 0 ) {
          if( aState == ETsDragState.START ) {
            sssR = createRect( slaveShape.bounds() );
          }

          IShape2dView view = aShapes.first();

          Rectangle r1 = vertexSet.bounds();
          r1 = new Rectangle( r1.x, r1.y, r1.width, r1.height );
          vertexSet.update( aDx, aDy, view.id() );
          Rectangle r2 = vertexSet.bounds();
          Rectangle rr = substract( r2, r1 );
          slaveShape.shiftOn( rr.x, rr.y );
          slaveShape.setSize( slaveShape.bounds().width + rr.width, slaveShape.bounds().height + rr.height );
          if( aState == ETsDragState.FINISH ) {
            slaveShape.onDragFinished();
            IUndoManager um = appContext.get( IImedService.class ).undoManager();
            Rectangle r = slaveShape.bounds();
            um.addUndoredoItem( new UndoSizeLocationChangePerformer( r.x - sssR.x, r.y - sssR.y, r.width - sssR.width,
                r.height - sssR.height, slaveShape ) );
          }
        }
        canvas.redraw();
      }
      else {
        if( aState == ETsDragState.START ) {
          sssR = createRect( aShapes.first().bounds() );
        }
        stdDragListener.onShapesDrag( aDx, aDy, aShapes, aState );
        if( aState == ETsDragState.FINISH ) {
          IUndoManager um = appContext.get( IImedService.class ).undoManager();
          Rectangle r = aShapes.first().bounds();
          um.addUndoredoItem( new UndoMoveShapesPerformer( r.x - sssR.x, r.y - sssR.y, aShapes ) );
        }
      }
    }

  };

  VedMoveCompViewsDragExecutor moveExecutor;

  private final IEclipseContext appContext;

  private RectVertexSetView vertexSet = null;

  private IShape2dView slaveShape = null;

  StdDragShapesEventListener stdDragListener = null;

  public VedPointerToolMouseHandler( IEditorTool aTool, IEclipseContext aAppContext ) {
    super( aTool );
    appContext = aAppContext;
    cursorManager = new TsCursorManager( aAppContext );
    cursorHand = cursorManager.getCursor( ECursorType.HAND );
  }

  @Override
  protected void onActivate() {
    stdDragListener = new StdDragShapesEventListener( (Canvas)canvas );
  }

  @Override
  protected void onDeactivate() {
    clearVertexSet();
  }

  @Override
  protected IVedDragExecutor dragExecutor( IShape2dView aHoveredObject ) {
    if( aHoveredObject == null ) {
      return IVedDragExecutor.NULL;
    }
    moveExecutor = new MoveShapesDragExecutor( new StridablesList<>( aHoveredObject ), appContext );
    moveExecutor.addVedDragCompViewsEventListener( moveListener );
    return moveExecutor;
  }

  @Override
  protected IStridablesList<IShape2dView> objectsForDrag( IShape2dView aHoveredObject, MouseEvent aEvent ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  protected void doDispose() {
    // TODO Auto-generated method stub
  }

  @Override
  public void onClick( IShape2dView aShape ) {
    if( aShape != null ) {
      slaveShape = aShape;
      vertexSet = new RectVertexSetView( aShape.bounds(), appContext );
      canvas.add( vertexSet );
      for( IShape2dView view : vertexSet.listShapes( IShape2dFilter.TRUE ) ) {
        canvas.add( view );
      }
      canvas.redraw();
    }
    else {
      if( vertexSet != null ) {
        clearVertexSet();
      }
    }
  }

  @Override
  public void onObjectIn( IShape2dView aShape ) {
    Cursor cursor = aShape.cursor();
    if( cursor == null ) {
      cursor = cursorHand;
    }
    canvas.setCursor( cursor );
  }

  @Override
  public void onObjectOut( IShape2dView aShape ) {
    canvas.setCursor( null );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  IStridablesList<IShape2dView> listShapes() {
    if( vertexSet != null ) {
      return vertexSet.listShapes( IShape2dFilter.TRUE );
    }
    return canvas.listShapes( tool );
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  @Override
  protected void beforeDragStarted() {
    if( vertexSet != null ) {
      vertexSet.setVisible( false );
    }
  }

  @Override
  protected void afterDragEnded() {
    super.afterDragEnded();
    if( vertexSet != null ) {
      vertexSet.setVisible( true );
      canvas.redraw();
    }
  }

  void clearVertexSet() {
    if( vertexSet != null ) {
      canvas.remove( vertexSet.id() );
      for( IShape2dView view : vertexSet.listShapes( IShape2dFilter.TRUE ) ) {
        canvas.remove( view.id() );
      }
      vertexSet = null;
      slaveShape = null;
      canvas.redraw();
    }
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
