package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.editor.IVedViselSelectionManager.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class VedViselContextMenuManager
    extends VedAbstractUserInputHandler {

  private final IVedViselSelectionManager selectionManager;

  private final VedAspViselsAlignment aspAlignment;

  private final VedAspCommonContextMenu aspCommon;

  private final AspMenuCreator commonMenuCreator;

  private final AspMenuCreator alignmentMenuCreator;

  /**
   * Constructor.
   *
   * @param aScreen {@link IVedScreen} - the owner VED screen
   * @param aSelectionManager {@link IVedViselSelectionManager} - selection manager for VISELs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselContextMenuManager( IVedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
    super( aScreen );
    TsNullArgumentRtException.checkNull( aSelectionManager );
    selectionManager = aSelectionManager;
    aspAlignment = new VedAspViselsAlignment( aScreen, selectionManager );
    aspCommon = new VedAspCommonContextMenu( aScreen, selectionManager );
    commonMenuCreator = new AspMenuCreator( aspCommon, aScreen.tsContext() );
    alignmentMenuCreator = new AspMenuCreator( aspAlignment, aScreen.tsContext() );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    vedScreen().view().getControl().setMenu( null );
    if( aButton == ETsMouseButton.RIGHT && (aState & SWT.MODIFIER_MASK) == 0 ) { // pure right click
      IStringList viselIds = vedScreen().view().listViselIdsAtPoint( aCoors );
      VedAbstractVisel visel = null;
      if( viselIds.size() > 0 ) {
        visel = vedScreen().model().visels().list().getByKey( viselIds.first() );
      }
      aspCommon.setActiveVisel( visel );
      if( visel != null ) { // click was on the visel
        if( selectionManager.selectionKind() == ESelectionKind.MULTI ) { // multiselection is present
          aspAlignment.setAnchorVisel( visel );

          Menu cmnMenu = commonMenuCreator.getMenu( vedScreen().view().getControl() );

          // Menu m = new Menu( vedScreen().view().getControl() );
          MenuItem alignItem = new MenuItem( cmnMenu, SWT.CASCADE );
          alignItem.setText( "Выравнивание" );

          Menu ctxMenu = alignmentMenuCreator.getMenu( cmnMenu );

          alignItem.setMenu( ctxMenu );

          vedScreen().view().getControl().setMenu( cmnMenu );
          return true;
        }
      }
      Menu cmnMenu = commonMenuCreator.getMenu( vedScreen().view().getControl() );
      vedScreen().view().getControl().setMenu( cmnMenu );
      return true;
    }
    return false;
  }

}
