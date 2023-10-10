package org.toxsoft.core.tsgui.ved.screen.impl;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Абстрактный "Набор вершин", от которого должны наследоваться конкретные.
 * <p>
 *
 * @author vs
 */
public abstract class VedAbstractVertexSet
    extends VedAbstractDecorator
    implements IVedVertexSet, ITsGuiContextable {

  private final IStridablesList<? extends IVedVertex> vertexes;

  D2Convertor convertor = new D2Convertor();

  DragOperationInfo dragInfo;

  class DragCargo {

    final IVedVertex vertex;

    final double startDx;

    final double startDy;

    D2PointEdit prevPoint = new D2PointEdit();

    DragCargo( IVedVertex aVertex, DragOperationInfo aInfo ) {
      vertex = aVertex;
      ITsPoint p = aInfo.startingPoint();

      ID2Point ctrlP = screenView.coorsConverter().swt2Visel( p.x(), p.y(), visel );

      startDx = ctrlP.x() - bounds().x1();
      startDy = ctrlP.y() - bounds().x2();

      prevPoint.setPoint( p.x(), p.y() );
    }

  }

  class InputHandler
      extends VedAbstractUserInputHandler {

    public InputHandler( VedScreen aScreen ) {
      super( aScreen );
    }

    // ------------------------------------------------------------------------------------
    // ITsMouseInputListener
    //

    @Override
    public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
      if( aButton == ETsMouseButton.RIGHT ) {
        // TsDialogInfo dlgInfo = new TsDialogInfo( tsContext(), "Edit", "Edit options" );
        // IStridablesList<IDataDef> dataDefs = OptionDefUtils.allDefs( visel );
        // IOptionSet opSet = OptionDefUtils.options( visel, tsContext() );
        // DialogOptionsEdit.editOpset( dlgInfo, dataDefs, opSet );
      }
      if( aButton == ETsMouseButton.LEFT ) {
        IVedVertex vertex = vertexByPoint( aCoors );
        if( vertex != null ) {
          return true;
        }
      }
      return false;
    }

    @Override
    public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
      IVedVertex vertex = vertexByPoint( aCoors );
      if( vertex != null ) {
        screenView.setCursor( cursorManager().getCursor( vertex.cursorType() ) );
        return true;
      }
      screenView.setCursor( null );
      return false;
    }

    @Override
    public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
      IVedVertex vertex = vertexByPoint( aDragInfo.startingPoint() );
      if( vertex != null ) {
        dragInfo = aDragInfo;
        dragInfo.setCargo( new DragCargo( vertex, aDragInfo ) );
        visible = false;
        return true;
      }
      return false;
    }

    @Override
    public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
      ID2Rectangle rectBefore = bounds();

      DragCargo dc = aDragInfo.cargo();
      if( dc == null ) {
        return false;
      }
      ID2Point prevP = screenView.coorsConverter().swt2Visel( (int)dc.prevPoint.x(), (int)dc.prevPoint.y(), visel );
      ID2Point currP = screenView.coorsConverter().swt2Visel( aCoors.x(), aCoors.y(), visel );
      int dx = (int)(currP.x() - prevP.x());
      int dy = (int)(currP.y() - prevP.y());

      boolean result = doOnVertexDrag( dc.vertex, dx, dy, EVedDragState.DRAGGING );

      dc.prevPoint.setPoint( aCoors.x(), aCoors.y() );

      ID2Rectangle rectAfter = bounds();

      // ITsRectangle ur = VedVertexSetUtils.union( rectBefore, rectAfter );
      // TsRectangleEdit ure = new TsRectangleEdit( ur );
      // ure.setRect( ur.x1() - 5, ur.y1() - 5, ur.width() + 30, ur.height() + 30 );
      // screen.redrawRect( ure );
      screenView.redraw();
      screenView.update();
      // screen.redraw();
      // System.out.println( "Rect before: " + rectBefore.toString() );
      // System.out.println( "Rect after: " + rectAfter.toString() );
      // System.out.println( "Union rect: " + ur.toString() );
      // System.out.println();
      return result;
    }

    @Override
    public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
      ID2Rectangle rectBefore = bounds();

      DragCargo dc = aDragInfo.cargo();
      if( dc == null ) {
        return false;
      }
      ID2Point prevP = screenView.coorsConverter().swt2Visel( (int)dc.prevPoint.x(), (int)dc.prevPoint.y(), visel );
      ID2Point currP = screenView.coorsConverter().swt2Visel( aCoors.x(), aCoors.y(), visel );
      int dx = (int)(currP.x() - prevP.x());
      int dy = (int)(currP.y() - prevP.y());

      boolean result = doOnVertexDrag( dc.vertex, dx, dy, EVedDragState.FINISH );

      ID2Rectangle rectAfter = bounds();
      visible = true;
      // screen.redrawRect( VedVertexSetUtils.union( rectBefore, rectAfter ) );
      screenView.redraw();
      return result;
    }

    @Override
    public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
      ID2Rectangle rectBefore = bounds();
      convertor.setConversion( screenView.getConversion() );
      double startX = aDragInfo.startingPoint().x();
      double startY = aDragInfo.startingPoint().y();
      double dx = convertor.reverseX( rectBefore.x1(), rectBefore.y1() ) - convertor.reverseX( startX, startY );
      double dy = convertor.reverseY( rectBefore.x1(), rectBefore.y1() ) - convertor.reverseY( startX, startY );
      DragCargo dc = aDragInfo.cargo();
      if( dc == null ) {
        return false;
      }
      boolean result = doOnVertexDrag( dc.vertex, dx, dy, EVedDragState.CANCEL );
      ID2Rectangle rectAfter = bounds();
      visible = true;
      // screen.redrawRect( VedVertexSetUtils.union( rectBefore, rectAfter ) );
      screenView.redraw();
      return result;
    }

  }

  private final VedAbstractVisel visel;

  VedScreen     vedScreen;
  VedScreenView screenView;

  private boolean visible = true;

  private final IStringListEdit hiddenVerts = new StringArrayList();

  private final InputHandler inputHandler;

  public VedAbstractVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
      VedScreen aVedScreen ) {
    super( aVedScreen );
    visel = aVisel;
    vedScreen = aVedScreen;
    screenView = vedScreen.view();
    vertexes = new StridablesList<>( aVertexes );
    inputHandler = new InputHandler( aVedScreen );
    convertor.setConversion( screenView.getConversion() );
    // screen.genericChangeEventer().addListener( aSource -> convertor.setConversion( screen.getConversion() ) );
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IDisplayable
  //

  @Override
  public final ID2Rectangle bounds() {
    ID2Rectangle bounds = null;
    for( IVedVertex vertex : vertexes ) {
      if( bounds == null ) {
        bounds = vertex.bounds();
      }
      else {
        bounds = VedVertexSetUtils.union( bounds, vertex.bounds() );
      }
    }
    return bounds;
  }

  @Override
  public final void paint( ITsGraphicsContext aGc ) {
    if( !visible ) {
      return;
    }
    doPaint( aGc.gc() );
  }

  // ------------------------------------------------------------------------------------
  // IVedDecorator
  //

  @Override
  public final String getViselIdOfDrawingTransform() {
    return visel.id();
  }

  @Override
  public boolean isYours( double aX, double aY ) {
    for( IVedVertex v : vertexes ) {
      if( v.isYours( aX, aY ) ) {
        return true;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // IViVertexSet
  //

  @Override
  public IList<? extends IVedVertex> vertexes() {
    return vertexes;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает обработчик пользовательского ввода. (клавиатура, мышь)
   *
   * @return {@link VedAbstractUserInputHandler} - обработчик пользовательского ввода
   */
  public VedAbstractUserInputHandler inputHandler() {
    return inputHandler;
  }

  // ------------------------------------------------------------------------------------
  // For use
  //

  protected VedAbstractVisel visel() {
    return visel;
  }

  protected boolean isVertexHidden( String aVertexId ) {
    return hiddenVerts.hasElem( aVertexId );
  }

  protected void clearHiddenIds() {
    hiddenVerts.clear();
  }

  protected void setVertexVisible( String aId, boolean aVisible ) {
    if( !aVisible && !hiddenVerts.hasElem( aId ) ) {
      hiddenVerts.add( aId );
    }
    if( aVisible ) {
      hiddenVerts.remove( aId );
    }
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract boolean doOnVertexDrag( IVedVertex aVertex, double aDx, double aDy, EVedDragState aDragState );

  protected abstract void doPaint( GC aGc );

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  IVedVertex vertexByPoint( ITsPoint aPoint ) {
    if( visel == null ) {
      return null;
    }

    ID2Point p = vedScreen.view().coorsConverter().swt2Visel( aPoint, visel );
    for( IVedVertex vertex : vertexes ) {
      if( !hiddenVerts.hasElem( vertex.id() ) && vertex.isYours( p.x(), p.y() ) ) {
        return vertex;
      }
    }
    return null;
  }

}
