package org.toxsoft.core.tsgui.ved.editor;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.cursors.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
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
public class VedViselPositionManager
    extends VedAbstractUserInputHandler {

  DragOperationInfo dragInfo;

  class DragCargo {

    final VedAbstractVisel item;

    final IListEdit<VedAbstractVisel> visels;

    TsPointEdit prevPoint = new TsPointEdit( 0, 0 );

    DragCargo( VedAbstractVisel aVisel, DragOperationInfo aInfo ) {
      item = TsNullArgumentRtException.checkNull( aVisel );
      if( selectionManager.isSelected( aVisel.id() ) ) {
        IStringList idsList = selectionManager.selectedViselIds();
        visels = new ElemArrayList<>();
        for( String id : idsList ) {
          visels.add( vedScreen().model().visels().list().getByKey( id ) );
        }
      }
      else {
        visels = new ElemArrayList<>( aVisel );
      }
      ITsPoint p = aInfo.startingPoint();
      prevPoint.setPoint( p.x(), p.y() );
    }

  }

  private final IVedViselSelectionManager selectionManager;

  /**
   * Constructor.
   *
   * @param aScreen {@link IVedScreen} - the owner VED screen
   * @param aSelectionManager {@link IVedViselSelectionManager} - selection manager for VISELs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselPositionManager( IVedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
    super( aScreen );
    selectionManager = TsNullArgumentRtException.checkNull( aSelectionManager );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseMove( Object aSource, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aState == 0 ) {
      VedAbstractVisel item = itemByPoint( aCoors.x(), aCoors.y() );
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
      VedAbstractVisel visel = itemByPoint( aDragInfo.startingPoint().x(), aDragInfo.startingPoint().y() );
      if( visel != null ) {
        dragInfo = aDragInfo;
        dragInfo.setCargo( new DragCargo( visel, aDragInfo ) );
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

    IVedCoorsConverter converter = vedScreen().view().coorsConverter();

    int dx = aCoors.x() - dc.prevPoint.x();
    int dy = aCoors.y() - dc.prevPoint.y();

    for( VedAbstractVisel v : dc.visels ) {
      ID2Point d2p = converter.swt2Visel( dx, dy, v );
      v.setLocation( v.originX() + d2p.x(), v.originY() + d2p.y() );
    }

    dc.prevPoint.setPoint( aCoors.x(), aCoors.y() );

    vedScreen().view().redraw();
    vedScreen().view().update();
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

  private VedAbstractVisel itemByPoint( int aSwtX, int aSwtY ) {
    IVedCoorsConverter converter = vedScreen().view().coorsConverter();
    for( VedAbstractVisel item : vedScreen().model().visels().list() ) {
      ID2Point d2p = converter.swt2Visel( aSwtX, aSwtY, item );
      if( item.isYours( d2p.x(), d2p.y() ) ) {
        return item;
      }
    }
    return null;
  }

  private void modifyViselsX( int aDelta ) {
    IVedCoorsConverter converter = vedScreen().view().coorsConverter();
    for( String id : selectionManager.selectedViselIds() ) {
      VedAbstractVisel v = vedScreen().model().visels().list().getByKey( id );
      ID2Point d2p = converter.swt2Visel( aDelta, 0, v );

      double xVal = v.props().getDouble( PROPID_X ) + d2p.x();
      double yVal = v.props().getDouble( PROPID_Y ) + d2p.y();
      v.props().setPropPairs( PROPID_X, avFloat( xVal ), PROPID_Y, avFloat( yVal ) );
    }
  }

  private void modifyViselsY( int aDelta ) {
    IVedCoorsConverter converter = vedScreen().view().coorsConverter();
    for( String id : selectionManager.selectedViselIds() ) {
      VedAbstractVisel v = vedScreen().model().visels().list().getByKey( id );
      ID2Point d2p = converter.swt2Visel( 0, aDelta, v );

      double xVal = v.props().getDouble( PROPID_X ) + d2p.x();
      double yVal = v.props().getDouble( PROPID_Y ) + d2p.y();
      v.props().setPropPairs( PROPID_X, avFloat( xVal ), PROPID_Y, avFloat( yVal ) );
    }
  }

}
