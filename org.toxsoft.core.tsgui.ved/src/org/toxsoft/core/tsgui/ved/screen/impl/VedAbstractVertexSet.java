package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.incub.tsg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.d2.helpers.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * {@link IVedVertexSet} basic abstract implementation.
 *
 * @author vs
 */
public abstract class VedAbstractVertexSet
    extends VedAbstractDecorator
    implements IVedVertexSet, ITsGuiContextable {

  /**
   * FIXME подчистить код!!!
   */

  private final IStridablesList<? extends IVedVertex> vertexes;

  private D2Convertor convertor = new D2Convertor();

  DragOperationInfo dragInfo;

  class DragCargo {

    final IVedVertex vertex;

    final ID2Rectangle vBounds;

    D2PointEdit prevPoint = new D2PointEdit();

    DragCargo( IVedVertex aVertex, DragOperationInfo aInfo ) {
      vertex = aVertex;
      vBounds = new D2Rectangle( visel.bounds() );
      ITsPoint p = aInfo.startingPoint();

      ID2Point ctrlP = screenView.coorsConverter().swt2Visel( p.x(), p.y(), visel );
      // ID2Point ctrlP = screenView.coorsConverter().swt2Screen( p.x(), p.y() );

      // prevPoint.setPoint( p.x(), p.y() );
      prevPoint.setPoint( ctrlP.x(), ctrlP.y() );
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
      if( !(aDragInfo.cargo() instanceof DragCargo) ) {
        return false;
      }
      DragCargo dc = aDragInfo.cargo();
      if( dc == null ) {
        return false;
      }
      ID2Point currP = screenView.coorsConverter().swt2Visel( aCoors.x(), aCoors.y(), visel );

      double dx = currP.x() - dc.prevPoint.x();
      double dy = currP.y() - dc.prevPoint.y();

      boolean result = doOnVertexDrag( dc.vertex, dx, dy, EVedDragState.DRAGGING );

      dc.prevPoint.setPoint( currP.x(), currP.y() );

      screenView.redraw();
      screenView.update();
      return result;
    }

    @Override
    public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
      if( !(aDragInfo.cargo() instanceof DragCargo) ) {
        return false;
      }
      DragCargo dc = aDragInfo.cargo();
      if( dc == null ) {
        return false;
      }
      ID2Point currP = screenView.coorsConverter().swt2Visel( aCoors.x(), aCoors.y(), visel );

      double dx = currP.x() - dc.prevPoint.x();
      double dy = currP.y() - dc.prevPoint.y();

      boolean result = doOnVertexDrag( dc.vertex, dx, dy, EVedDragState.FINISH );

      visible = true;
      screenView.redraw();
      return result;
    }

    @Override
    public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
      // ID2Rectangle rectBefore = bounds();
      // convertor.setConversion( screenView.getConversion() );
      // double startX = aDragInfo.startingPoint().x();
      // double startY = aDragInfo.startingPoint().y();
      // double dx = convertor.reverseX( rectBefore.x1(), rectBefore.y1() ) - convertor.reverseX( startX, startY );
      // double dy = convertor.reverseY( rectBefore.x1(), rectBefore.y1() ) - convertor.reverseY( startX, startY );
      if( !(aDragInfo.cargo() instanceof DragCargo) ) {
        return false;
      }
      DragCargo dc = aDragInfo.cargo();
      if( dc == null ) {
        return false;
      }
      // boolean result = doOnVertexDrag( dc.vertex, dx, dy, EVedDragState.CANCEL );
      visel.props().setPropPairs( PROP_X, avFloat( dc.vBounds.x1() ), //
          PROP_Y, avFloat( dc.vBounds.y1() ), //
          PROP_WIDTH, avFloat( dc.vBounds.width() ), //
          PROP_HEIGHT, avFloat( dc.vBounds.height() ) );

      visible = true;
      screenView.redraw();
      return true;
    }

    // ------------------------------------------------------------------------------------
    // ITsKeyInputListener
    //

    @Override
    public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
      boolean result = false;
      double zf = vedScreen.view().getConversion().zoomFactor() * visel.getConversion().zoomFactor();
      if( aCode == SWT.ARROW_LEFT ) {
        if( aState == 0 ) {
          double x = visel.props().getDouble( PROPID_X ) - 1 / zf;
          visel.props().setDouble( PROPID_X, x );
        }
        if( aState == SWT.CTRL ) {
          double x = visel.props().getDouble( PROPID_X ) - 10. / zf;
          visel.props().setDouble( PROPID_X, x );
        }
        if( aState == SWT.SHIFT ) {
          double w = visel.props().getDouble( PROPID_WIDTH ) - 1 / zf;
          visel.props().setDouble( PROPID_WIDTH, w );
        }
        if( aState == (SWT.CTRL | SWT.SHIFT) ) {
          double w = visel.props().getDouble( PROPID_WIDTH ) - 10 / zf;
          visel.props().setDouble( PROPID_WIDTH, w );
        }
        result = true;
      }
      if( aCode == SWT.ARROW_RIGHT ) {
        if( aState == 0 ) {
          double x = visel.props().getDouble( PROPID_X ) + 1 / zf;
          visel.props().setDouble( PROPID_X, x );
        }
        if( aState == SWT.CTRL ) {
          double x = visel.props().getDouble( PROPID_X ) + 10. / zf;
          visel.props().setDouble( PROPID_X, x );
        }
        if( aState == SWT.SHIFT ) {
          double w = visel.props().getDouble( PROPID_WIDTH ) + 1 / zf;
          visel.props().setDouble( PROPID_WIDTH, w );
        }
        if( aState == (SWT.CTRL | SWT.SHIFT) ) {
          double w = visel.props().getDouble( PROPID_WIDTH ) + 10 / zf;
          visel.props().setDouble( PROPID_WIDTH, w );
        }
        result = true;
      }
      if( aCode == SWT.ARROW_UP ) {
        if( aState == 0 ) {
          double y = visel.props().getDouble( PROPID_Y ) - 1 / zf;
          visel.props().setDouble( PROPID_Y, y );
        }
        if( aState == SWT.CTRL ) {
          double y = visel.props().getDouble( PROPID_Y ) - 10. / zf;
          visel.props().setDouble( PROPID_Y, y );
        }
        if( aState == SWT.SHIFT ) {
          double h = visel.props().getDouble( PROPID_HEIGHT ) - 1 / zf;
          visel.props().setDouble( PROPID_HEIGHT, h );
        }
        if( aState == (SWT.CTRL | SWT.SHIFT) ) {
          double h = visel.props().getDouble( PROPID_HEIGHT ) - 10 / zf;
          visel.props().setDouble( PROPID_HEIGHT, h );
        }
        result = true;
      }
      if( aCode == SWT.ARROW_DOWN ) {
        if( aState == 0 ) {
          double y = visel.props().getDouble( PROPID_Y ) + 1 / zf;
          visel.props().setDouble( PROPID_Y, y );
        }
        if( aState == SWT.CTRL ) {
          double y = visel.props().getDouble( PROPID_Y ) + 10. / zf;
          visel.props().setDouble( PROPID_Y, y );
        }
        if( aState == SWT.SHIFT ) {
          double h = visel.props().getDouble( PROPID_HEIGHT ) + 1 / zf;
          visel.props().setDouble( PROPID_HEIGHT, h );
        }
        if( aState == (SWT.CTRL | SWT.SHIFT) ) {
          double h = visel.props().getDouble( PROPID_HEIGHT ) + 10 / zf;
          visel.props().setDouble( PROPID_HEIGHT, h );
        }
        result = true;
      }
      if( result ) {
        screenView.redraw();
        screenView.update();
      }
      return result;
    }

  }

  class ViselListener
      implements IPropertyChangeListener<IVedItem> {

    @Override
    public void onPropsChanged( IVedItem aSource, IOptionSet aNewValues, IOptionSet aOldValues ) {
      doOnViselPropsChanged( aSource, aNewValues, aOldValues );
    }

  }

  private final VedAbstractVisel visel;

  VedScreen     vedScreen;
  VedScreenView screenView;

  private boolean visible = true;

  private final IStringListEdit hiddenVerts = new StringArrayList();

  private final InputHandler inputHandler;

  private final ViselListener viselListener;

  /**
   * Конструктор.
   *
   * @param aVisel {@link VedAbstractVisel} - визуальный элемент
   * @param aVertexes IStridablesList&lt? extends IVedVertex> - список вершин
   * @param aVedScreen {@link VedScreen} - экран
   */
  public VedAbstractVertexSet( VedAbstractVisel aVisel, IStridablesList<? extends IVedVertex> aVertexes,
      VedScreen aVedScreen ) {
    super( aVedScreen );
    visel = aVisel;
    vedScreen = aVedScreen;
    screenView = vedScreen.view();
    vertexes = new StridablesList<>( aVertexes );
    inputHandler = new InputHandler( aVedScreen );
    convertor.setConversion( screenView.getConversion() );
    viselListener = new ViselListener();
    visel.props().propsEventer().addListener( viselListener );
    vedScreen.view().configChangeEventer().addListener( aSource -> {
      updateZoomFactor();
    } );
    updateZoomFactor();
  }

  void updateZoomFactor() {
    ID2Conversion d2conv = vedScreen.view().getConversion();
    if( d2conv.zoomFactor() < 1 ) {
      for( IVedVertex v : vertexes ) {
        ((VedAbstractVertex)v).setZoomFactor( d2conv.zoomFactor() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedScreen.tsContext();
  }

  @Override
  protected void doDispose() {
    // nop
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
        bounds = D2GeometryUtils.union( bounds, vertex.bounds() );
      }
    }
    return bounds;
  }

  @Override
  public final void paint( ITsGraphicsContext aGc ) {
    if( visible ) {
      doPaint( aGc );
    }
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

  /**
   * Возвращает ИД визеля, с которым связан набор вершин.
   *
   * @return String - ИД визеля, с которым связан набор вершин
   */
  public String viselId() {
    return visel.id();
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

  protected abstract void doPaint( ITsGraphicsContext aGc );

  protected abstract void doOnViselPropsChanged( IVedItem aSource, IOptionSet aNewVals, IOptionSet aOldVals );

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
