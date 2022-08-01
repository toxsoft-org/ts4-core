package org.toxsoft.core.tsgui.ved.std.tools;

import static java.lang.Math.*;

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
      // slaveShape.component().genericChangeEventer().muteListener( tool.activeComponentListener() );
      if( aShapes.size() > 0 ) {

        IScreenObject view = aShapes.first();

        Rectangle r1 = tool.vertexSet().bounds();
        // screen().paintingManager().redrawRect( new TsRectangle( r1.x, r1.y, r1.width, r1.height ) );

        r1 = new Rectangle( r1.x, r1.y, r1.width, r1.height );
        tool.vertexSet().update( aDx, aDy, view.id() );
        Rectangle r2 = tool.vertexSet().bounds();
        Rectangle rr = substract( r2, r1 );

        double alpha = vedScreen().getConversion().rotation().radians();
        double dx = rr.x * cos( -alpha ) - rr.y * sin( -alpha );
        double dy = rr.y * cos( -alpha ) + rr.x * sin( -alpha );
        double zf = vedScreen().getConversion().zoomFactor();

        slaveShape.porter().shiftOn( dx / zf, dy / zf );

        double w = rr.width * cos( -alpha ) - rr.height * sin( -alpha );
        double h = rr.height * cos( -alpha ) + rr.width * sin( -alpha );

        w = rr.width * cos( -alpha );// - rr.height * sin( -alpha );
        h = rr.height * cos( -alpha );// + rr.width * sin( -alpha );

        slaveShape.porter().setSize( slaveShape.outline().bounds().width() + w / zf,
            slaveShape.outline().bounds().height() + h / zf );

        // r1 = tool.vertexSet().bounds();
        // screen().paintingManager().redrawRect( new TsRectangle( r1.x, r1.y, r1.width, r1.height ) );
        screen().paintingManager().redraw();
        screen().paintingManager().update();
      }
      // slaveShape.component().genericChangeEventer().unmuteListener( tool.activeComponentListener() );
    }
    else {
      double alpha = vedScreen().getConversion().rotation().radians();
      double dx = aDx * cos( -alpha ) - aDy * sin( -alpha );
      double dy = aDy * cos( -alpha ) + aDx * sin( -alpha );
      double zf = vedScreen().getConversion().zoomFactor();

      stdDragListener.onShapesDrag( dx / zf, dy / zf, aShapes, aState );
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
      ITsRectangle tsRect = vedScreen().coorsConvertor().rectBounds( tool.activeView().outline().bounds() );
      tool.vertexSet().init( tsRect );
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
