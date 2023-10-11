package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Менеджер по созданию и удалению набора вершин {@link IVedVertexSet}.
 * <p>
 *
 * @author vs
 */
public class VedViselVertexSetManager
    extends VedAbstractUserInputHandler {

  /**
   * Конструктор.
   *
   * @param aScreen {@link VedScreen} - экран
   */
  public VedViselVertexSetManager( VedScreen aScreen ) {
    super( aScreen );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT ) {
      vedScreen().view().removeViselVertexSet();
      IStringList viselIds = vedScreen().view().listViselIdsAtPoint( aCoors );
      if( viselIds.size() > 0 ) {
        vedScreen().view().createViselVertexSet( viselIds.first() );
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT ) {
      vedScreen().view().removeViselVertexSet();
      return true;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( aCode == SWT.ESC ) {
      vedScreen().view().removeViselVertexSet();
      return true;
    }
    return false;
  }
}
