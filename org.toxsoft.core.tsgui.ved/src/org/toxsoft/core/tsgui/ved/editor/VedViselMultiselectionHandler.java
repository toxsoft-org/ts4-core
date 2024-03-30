package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Обработчик пользовательского ввода обеспечивающий множественное выделение визелей мышью.
 * <p>
 *
 * @author vs
 */
public class VedViselMultiselectionHandler
    extends VedAbstractUserInputHandler {

  private final IVedViselSelectionManager selectionManager;

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
      }
      return true;
    }
    return false;
  }

  // // ------------------------------------------------------------------------------------
  // // Implementation
  // //
  //
  // private VedAbstractVisel itemByPoint( int aSwtX, int aSwtY ) {
  // IVedCoorsConverter converter = vedScreen().view().coorsConverter();
  // for( VedAbstractVisel item : vedScreen().model().visels().list() ) {
  // ID2Point d2p = converter.swt2Visel( aSwtX, aSwtY, item );
  // if( item.isYours( d2p.x(), d2p.y() ) ) {
  // return item;
  // }
  // }
  // return null;
  // }

}
