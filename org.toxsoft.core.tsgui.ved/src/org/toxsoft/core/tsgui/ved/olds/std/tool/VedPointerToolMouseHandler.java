package org.toxsoft.core.tsgui.ved.olds.std.tool;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.olds.drag.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

public class VedPointerToolMouseHandler
    extends VedAbstractToolMouseHandler {

  ITsCursorManager cursorManager;

  Cursor cursorHand;

  IVedDragExecutor dragExecutor = IVedDragExecutor.NULL;

  StdDragVedCompViewsListener stdDragListener = null;

  /**
   * Набор вершин прямоугольника
   */
  private VedRectVertexSetView vertexSet = null;

  private IVedComponentView slaveShape = null;

  // Rectangle sssR;

  IVedDragObjectsListener moveListener = ( aDx, aDy, aShapes, aState ) -> {
    if( vertexSet != null ) {
      if( aShapes.size() > 0 ) {
        // if( aState == ETsDragState.START ) {
        // sssR = createRect( slaveShape.bounds() );
        // }

        IScreenObject view = aShapes.first();

        Rectangle r1 = vertexSet.bounds();
        r1 = new Rectangle( r1.x, r1.y, r1.width, r1.height );
        vertexSet.update( aDx, aDy, view.id() );
        Rectangle r2 = vertexSet.bounds();
        Rectangle rr = substract( r2, r1 );
        slaveShape.porter().shiftOn( canvas.screenToNorm( rr.x ), canvas.screenToNorm( rr.y ) );
        double w = slaveShape.outline().bounds().width() + rr.width / canvas.zoomFactor();
        double h = slaveShape.outline().bounds().height() + rr.height / canvas.zoomFactor();
        slaveShape.porter().setSize( w, h );
        // if( aState == ETsDragState.FINISH ) {
        // slaveShape.onDragFinished();
        // IUndoManager um = appContext.get( IImedService.class ).undoManager();
        // Rectangle r = slaveShape.bounds();
        // um.addUndoredoItem( new UndoSizeLocationChangePerformer( r.x - sssR.x, r.y - sssR.y, r.width - sssR.width,
        // r.height - sssR.height, slaveShape ) );
        // }
        canvas.redraw();
      }
    }
    else {
      // if( aState == ETsDragState.START ) {
      // sssR = createRect( aShapes.first().bounds() );
      // }

      stdDragListener.onShapesDrag( aDx / canvas.zoomFactor(), aDy / canvas.zoomFactor(), aShapes, aState );

      // if( aState == ETsDragState.FINISH ) {
      // IUndoManager um = appContext.get( IImedService.class ).undoManager();
      // Rectangle r = aShapes.first().bounds();
      // um.addUndoredoItem( new UndoMoveShapesPerformer( r.x - sssR.x, r.y - sssR.y, aShapes ) );
      // }
    }
    // stdDragListener.onShapesDrag( aDx, aDy, aShapes, aState );
  };

  VedMoveObjectsDragExecutor moveExecutor;

  private final IEclipseContext appContext;

  public VedPointerToolMouseHandler( VedAbstractEditorTool aTool, IEclipseContext aAppContext ) {
    super();
    // super( aTool );
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
  protected IVedDragExecutor dragExecutor( IScreenObject aHoveredObject ) {
    if( aHoveredObject == null ) {
      return IVedDragExecutor.NULL;
    }

    moveExecutor = new VedMoveObjectsDragExecutor( new StridablesList<>( aHoveredObject ), appContext );
    moveExecutor.addVedDragCompViewsEventListener( moveListener );
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
          canvas.selectionManager().toggleSelection( view.component() );
          // System.out.println( "Toggle selection reuqired!" );
        }
        return;
      }
    }

    if( aObj != null ) {
      IStridablesListEdit<IScreenObject> scrObjs = new StridablesList<>();
      slaveShape = aObj.entity();
      vertexSet = new VedRectVertexSetView( aObj.bounds(), appContext );
      canvas.addScreenObject( vertexSet );
      for( IScreenObject vertex : vertexSet.listVertexes() ) {
        canvas.addScreenObject( vertex );
        scrObjs.add( vertex );
      }
      canvas.redraw();
      setScreenObjects( scrObjs );
    }
    else {
      if( vertexSet != null ) {
        clearVertexSet();
      }
      IStridablesListEdit<IScreenObject> scrObjs = new StridablesList<>();
      for( IVedComponentView view : canvas.listViews() ) {
        IScreenObject obj = new VedComponentViewScreenObject( view );
        scrObjs.add( obj );
      }
      setScreenObjects( scrObjs );
    }
  }

  @Override
  public void onObjectIn( IScreenObject aObj ) {
    Cursor cursor = cursorManager.findCursor( aObj.cursorType().id() );
    if( cursor == null ) {
      cursor = cursorHand;
    }
    canvas.setCursor( cursor );
  }

  @Override
  public void onObjectOut( IScreenObject aShape ) {
    canvas.setCursor( null );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  IStridablesList<? extends IScreenObject> listShapes() {
    if( vertexSet != null ) {
      return vertexSet.listVertexes();
    }
    // return canvas.listShapes( tool );
    return canvas.listViews( ITsFilter.ALL );
  }

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
      canvas.redraw();
    }
  }

  void clearVertexSet() {
    if( vertexSet != null ) {
      canvas.removeScreenObject( vertexSet.id() );
      for( IVedVertex vertex : vertexSet.listVertexes() ) {
        canvas.removeScreenObject( vertex.id() );
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
