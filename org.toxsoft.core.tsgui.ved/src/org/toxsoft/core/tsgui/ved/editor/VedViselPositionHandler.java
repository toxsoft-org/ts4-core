package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик пользовательского ввода обеспечивающий перемещение визелей мышью.
 * <p>
 *
 * @author vs
 */
public class VedViselPositionHandler
    extends VedAbstractUserInputHandler {

  DragOperationInfo dragInfo;

  class DragCargo {

    final VedAbstractVisel item;

    final IListEdit<VedAbstractVisel> visels;

    TsPointEdit prevPoint = new TsPointEdit( 0, 0 );

    DragCargo( VedAbstractVisel aVisel, DragOperationInfo aInfo ) {
      item = TsNullArgumentRtException.checkNull( aVisel );
      IStringList idsList = positionManager.listViselIds2Move( aVisel.id() );
      visels = new ElemArrayList<>();
      for( String id : idsList ) {
        visels.add( vedScreen().model().visels().list().getByKey( id ) );
      }

      ITsPoint p = aInfo.startingPoint();
      prevPoint.setPoint( p.x(), p.y() );
    }

  }

  private final IVedViselsPositionManager positionManager;

  /**
   * Constructor.
   *
   * @param aScreen {@link IVedScreen} - the owner VED screen
   * @param aPositionManager {@link IVedViselsPositionManager} - selection manager for VISELs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselPositionHandler( IVedScreen aScreen, IVedViselsPositionManager aPositionManager ) {
    super( aScreen );
    positionManager = TsNullArgumentRtException.checkNull( aPositionManager );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aState == 0 ) {
      VedAbstractVisel item = VedScreenUtils.itemByPoint( aCoors.x(), aCoors.y(), vedScreen(), false );
      if( item == null ) {
        vedScreen().view().setCursor( null );
      }
      else {
        vedScreen().view().setCursor( vedScreen().cursorManager().getCursor( ECursorType.HAND ) );
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragStart( Object aSource, DragOperationInfo aDragInfo ) {
    if( aDragInfo.button() == ETsMouseButton.LEFT && (aDragInfo.startingState() & SWT.MODIFIER_MASK) == 0 ) {
      ITsPoint p = aDragInfo.startingPoint();
      VedAbstractVisel visel = VedScreenUtils.itemByPoint( p.x(), p.y(), vedScreen(), false );
      if( visel != null ) {
        dragInfo = aDragInfo;
        dragInfo.setCargo( new DragCargo( visel, aDragInfo ) );
        vedScreen().model().visels().eventer().pauseFiring();
        vedScreen().model().actors().eventer().pauseFiring();
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean onMouseDragMove( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    DragCargo dc = aDragInfo.cargo();
    if( dc == null ) {
      return false;
    }

    int dx = aCoors.x() - dc.prevPoint.x();
    int dy = aCoors.y() - dc.prevPoint.y();

    for( VedAbstractVisel v : dc.visels ) {
      ID2Point d2pZero = v.coorsConverter().swt2Screen( 0, 0 );
      ID2Point d2p = v.coorsConverter().swt2Screen( dx, dy );
      v.setLocation( v.originX() + d2p.x() - d2pZero.x(), v.originY() + d2p.y() - d2pZero.y() );
    }

    dc.prevPoint.setPoint( aCoors.x(), aCoors.y() );

    vedScreen().view().redraw();
    vedScreen().view().update();
    return true;
  }

  @Override
  public boolean onMouseDragFinish( Object aSource, DragOperationInfo aDragInfo, int aState, ITsPoint aCoors ) {
    DragCargo dc = aDragInfo.cargo();
    if( dc == null ) {
      return false;
    }

    int dx = aCoors.x() - dc.prevPoint.x();
    int dy = aCoors.y() - dc.prevPoint.y();

    for( VedAbstractVisel v : dc.visels ) {
      ID2Point d2pZero = v.coorsConverter().swt2Screen( 0, 0 );
      ID2Point d2p = v.coorsConverter().swt2Screen( dx, dy );
      v.setLocation( v.originX() + d2p.x() - d2pZero.x(), v.originY() + d2p.y() - d2pZero.y() );
    }

    vedScreen().model().visels().eventer().resumeFiring( true );
    vedScreen().model().actors().eventer().resumeFiring( true );

    dc.prevPoint.setPoint( aCoors.x(), aCoors.y() );

    vedScreen().view().redraw();
    vedScreen().view().update();
    return true;
  }

  @Override
  public boolean onMouseDragCancel( Object aSource, DragOperationInfo aDragInfo ) {
    DragCargo dc = aDragInfo.cargo();
    if( dc == null ) {
      return false;
    }
    if( dc.visels.size() > 0 ) {
      vedScreen().model().visels().eventer().resumeFiring( false );
      vedScreen().model().actors().eventer().resumeFiring( false );
    }
    return true;
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    boolean result = false;
    if( aCode == SWT.ARROW_LEFT ) {
      if( aState == 0 ) {
        modifyViselsX( -1 );
      }
      if( aState == SWT.CTRL ) {
        modifyViselsX( -10 );
      }
      result = true;
    }
    if( aCode == SWT.ARROW_RIGHT ) {
      if( aState == 0 ) {
        modifyViselsX( 1 );
      }
      if( aState == SWT.CTRL ) {
        modifyViselsX( 10 );
      }
      result = true;
    }
    if( aCode == SWT.ARROW_UP ) {
      if( aState == 0 ) {
        modifyViselsY( -1 );
      }
      if( aState == SWT.CTRL ) {
        modifyViselsY( -10 );
      }
      result = true;
    }
    if( aCode == SWT.ARROW_DOWN ) {
      if( aState == 0 ) {
        modifyViselsY( 1 );
      }
      if( aState == SWT.CTRL ) {
        modifyViselsY( 10 );
      }
      result = true;
    }
    return result;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private void modifyViselsX( int aDelta ) {
    IStridablesList<VedAbstractVisel> visels = vedScreen().model().visels().list();
    for( String id : positionManager.listViselIds2Move( null ) ) {
      VedAbstractVisel v = visels.getByKey( id );
      double xVal = v.props().getDouble( PROPID_X ) + aDelta;
      v.props().setDouble( PROPID_X, xVal );
    }
  }

  private void modifyViselsY( int aDelta ) {
    IStridablesList<VedAbstractVisel> visels = vedScreen().model().visels().list();
    for( String id : positionManager.listViselIds2Move( null ) ) {
      VedAbstractVisel v = visels.getByKey( id );

      double yVal = v.props().getDouble( PROPID_Y ) + aDelta;
      v.props().setDouble( PROPID_Y, yVal );
    }
  }

}
