package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.asp.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

public class VedCanvasMenuManager
    extends VedAbstractUserInputHandler {

  private final VedAspFileImpex aspFileImpex;

  public VedCanvasMenuManager( IVedScreen aScreen ) {
    super( aScreen );
    aspFileImpex = new VedAspFileImpex( aScreen );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.RIGHT && (aState & SWT.MODIFIER_MASK) == 0 ) {
      if( vedScreen().view().listViselIdsAtPoint( aCoors ).size() == 0 ) {
        // MenuManager mm = new MenuManager();
        // mm.add
        // Menu ctxMenu = mm.createContextMenu( vedScreen().view().getControl() );
        // AspMenuCreator mc = new AspMenuCreator();
        // Menu ctxMenu = vedScreen().view().getControl().setMenu( ctxMenu );
        return true;
      }
    }
    return false;
  }
}
