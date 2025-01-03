package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.gc.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик пользовательского ввода обеспечивающий множественное выделение визелей мышью.
 * <p>
 *
 * @author vs
 */
public class VedViselMultiselectionHandler
    extends VedAbstractUserInputHandler {

  static class SelectionAreaDecorator
      extends VedAbstractDecorator {

    D2RectangleEdit bounds;

    Color colorRed = new Color( 255, 0, 0 );

    SelectionAreaDecorator( double aX, double aY, IVedScreen aVedScreen ) {
      super( aVedScreen );
      bounds = new D2RectangleEdit( aX, aY, 1, 1 );
    }

    @Override
    public void paint( ITsGraphicsContext aPaintContext ) {
      aPaintContext.gc().setLineWidth( 2 );
      aPaintContext.gc().setLineStyle( SWT.LINE_DOT );
      aPaintContext.gc().setForeground( colorRed );
      aPaintContext.gc().drawRectangle( (int)bounds.x1(), (int)bounds.y1(), (int)bounds.width(), (int)bounds.height() );
    }

    @Override
    public boolean isYours( double aX, double aY ) {
      return false;
    }

    @Override
    public ID2Rectangle bounds() {
      return null;
    }

    ID2Rectangle areaBounds() {
      return bounds;
    }

    void setEndPoint( double aX, double aY ) {
      if( aX - bounds.x1() != 0 && aY - bounds.y1() != 0 ) {
        // int x = Math.min( aX, bounds.x1() );
        // int y = Math.min( aY, bounds.y1() );
        // int width = Math.abs( aX - bounds.x2() );
        // int height = Math.abs( aY - bounds.y2() );
        // bounds.setRect( x, y, width, height );
        bounds.setSize( aX - bounds.x1(), aY - bounds.y1() );
      }
    }
  }

  /**
   * Признак того, что идет
   */
  private boolean dragging = false;

  private final IVedViselSelectionManager selectionManager;

  private SelectionAreaDecorator areaDecorator = null;

  /**
   * Constructor.
   *
   * @param aScreen {@link VedScreen} - the owner VED screen
   * @param aSelectionManager {@link IVedViselSelectionManager} - selection manager for VISELs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselMultiselectionHandler( IVedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
    super( aScreen );
    selectionManager = TsNullArgumentRtException.checkNull( aSelectionManager );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT && aState == SWT.CTRL ) {
      VedAbstractVisel visel = VedScreenUtils.itemByPoint( aCoors.x(), aCoors.y(), vedScreen(), false );
      if( visel != null ) {
        selectionManager.toggleSelection( visel.id() );
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
    if( aDragInfo.button() == ETsMouseButton.LEFT && (aDragInfo.startingState() & SWT.CTRL) != 0 ) {
      ITsPoint p = aDragInfo.startingPoint();
      ID2Point d2p = vedScreen().view().coorsConverter().swt2Screen( p );
      // System.out.println( "Start drag: x = " + p.x() + "; y = " + p.y() );
      // areaDecorator = new SelectionAreaDecorator( p.x(), p.y(), vedScreen() );
      areaDecorator = new SelectionAreaDecorator( d2p.x(), d2p.y(), vedScreen() );
      vedScreen().model().screenDecoratorsAfter().add( areaDecorator );
      vedScreen().view().redraw();
      dragging = true;
      return true;
    }
    return false;
  }

  @Override
  public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    if( dragging ) {
      ID2Point d2p = vedScreen().view().coorsConverter().swt2Screen( aCoors );
      // areaDecorator.setEndPoint( aCoors.x(), aCoors.y() );
      areaDecorator.setEndPoint( d2p.x(), d2p.y() );
      vedScreen().view().redraw();
      return true;
    }
    return false;
  }

  @Override
  public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    dragging = false;
    if( areaDecorator != null && vedScreen().model().screenDecoratorsAfter().list().hasElem( areaDecorator ) ) {
      vedScreen().model().screenDecoratorsAfter().remove( areaDecorator );
      IStridablesList<VedAbstractVisel> visels = listBoundedVisels( areaDecorator.areaBounds() );
      selectionManager.setSelectedVisels( visels.ids() );
      areaDecorator = null;
      vedScreen().view().redraw();
    }
    return false;
  }

  @Override
  public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
    dragging = false;
    if( areaDecorator != null ) {
      vedScreen().model().screenDecoratorsAfter().remove( areaDecorator );
      areaDecorator = null;
      vedScreen().view().redraw();
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private IStridablesList<VedAbstractVisel> listBoundedVisels( ID2Rectangle aBounds ) {
    StridablesList<VedAbstractVisel> result = new StridablesList<>();
    IVedCoorsConverter conv = vedScreen().view().coorsConverter();
    for( VedAbstractVisel visel : vedScreen().model().visels().list() ) {
      ID2Rectangle r = visel.bounds();
      ID2Rectangle br = conv.screen2Visel( aBounds, visel );
      if( br.contains( r.a() ) && br.contains( r.b() ) ) {
        result.add( visel );
      }
    }
    return result;
  }

  // IVedCoorsConverter converter = vedScreen().view().coorsConverter();
  // for( VedAbstractVisel item : vedScreen().model().visels().list() ) {
  // ID2Point d2p = converter.swt2Visel( aSwtX, aSwtY, item );
  // if( item.isYours( d2p.x(), d2p.y() ) ) {
  // return item;
  // }

}
