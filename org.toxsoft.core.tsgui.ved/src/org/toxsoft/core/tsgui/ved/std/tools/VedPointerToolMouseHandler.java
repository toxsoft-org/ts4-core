package org.toxsoft.core.tsgui.ved.std.tools;

import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.view.*;
import org.toxsoft.core.tsgui.ved.impl.*;
import org.toxsoft.core.tsgui.ved.utils.*;
import org.toxsoft.core.tsgui.ved.utils.drag.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
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

  private VedStdPointerTool tool = null;

  IVedDragObjectsListener moveListener = ( aDx, aDy, aShapes, aState ) -> {
    IVedComponentView slaveShape = tool.activeView();
    if( slaveShape != null ) {
      slaveShape.component().genericChangeEventer().muteListener( tool.activeComponentListener() );
      if( aShapes.size() > 0 ) {

        IScreenObject view = aShapes.first();

        Rectangle r1 = tool.vertexSet().bounds();
        r1 = new Rectangle( r1.x, r1.y, r1.width, r1.height );
        tool.vertexSet().update( aDx, aDy, view.id() );
        Rectangle r2 = tool.vertexSet().bounds();
        Rectangle rr = substract( r2, r1 );

        double zf = screen().getConversion().zoomFactor();

        ID2Point d2p = screen().coorsConvertor().convertPoint( rr.x, rr.y );

        slaveShape.porter().shiftOn( d2p.x(), d2p.y() );
        double w = slaveShape.outline().bounds().width() + rr.width / zf;
        double h = slaveShape.outline().bounds().height() + rr.height / zf;
        slaveShape.porter().setSize( w, h );
        screen().paintingManager().redraw();
        screen().paintingManager().update();
      }
      slaveShape.component().genericChangeEventer().unmuteListener( tool.activeComponentListener() );
    }
    else {
      double zf = screen().getConversion().zoomFactor();
      stdDragListener.onShapesDrag( aDx / zf, aDy / zf, aShapes, aState );
    }
  };

  VedMoveObjectsDragExecutor moveExecutor;

  /**
   * Конструктор.<br>
   *
   * @param aTool VedStdPointerTool - инструмент - указатель
   * @param aEnv IVedEnvironment - окружение редактора
   * @param aScreen IVedScreen - экран отображения
   */
  public VedPointerToolMouseHandler( VedStdPointerTool aTool, IVedEnvironment aEnv, IVedScreen aScreen ) {
    super( aEnv, aScreen );
    tool = aTool;
    cursorHand = cursorManager().getCursor( ECursorType.HAND );
    stdDragListener = new StdDragVedCompViewsListener( screen() );
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
   * Вызывается при изменении коэффициента масштабирования.
   *
   * @param aZoomFactor double - кэффициент масштабирования
   */
  public void onZoomFactorChanged( double aZoomFactor ) {
    // if( vertexSet != null ) {
    if( tool.activeView() != null ) {
      ID2Rectangle d2r = tool.activeView().outline().bounds();
      ITsRectangle tsRect = vedScreen().coorsConvertor().rectBounds( d2r );

      tool.vertexSet().init( tsRect );

      // int x = (int)Math.round( d2r.x1() * aZoomFactor );
      // int y = (int)Math.round( d2r.y1() * aZoomFactor );
      // int w = (int)Math.round( d2r.width() * aZoomFactor );
      // int h = (int)Math.round( d2r.height() * aZoomFactor );
      //
      // vertexSet.init( new TsRectangle( x, y, w, h ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Внутренняя реализация
  //

  @Override
  protected void beforeDragStarted() {
    if( tool.activeView() != null ) {
      tool.vertexSet().setVisible( false );
    }
  }

  @Override
  protected void afterDragEnded() {
    super.afterDragEnded();
    if( tool.activeView() != null ) {
      tool.vertexSet().setVisible( true );
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
