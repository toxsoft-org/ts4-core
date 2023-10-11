package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
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

  class SelectionListener
      implements IGenericChangeListener {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      String viselId = selectionManager.singleSelectedViselId();
      vedScreen().view().removeViselVertexSet();
      if( viselId != null ) {
        vedScreen().view().createViselVertexSet( viselId );
      }
    }

  }

  private final IVedViselSelectionManager selectionManager;

  private final SelectionListener selectionListener;

  /**
   * Конструктор.
   *
   * @param aScreen {@link VedScreen} - экран
   */
  public VedViselVertexSetManager( VedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
    super( aScreen );
    selectionManager = aSelectionManager;
    selectionListener = new SelectionListener();
    selectionManager.genericChangeEventer().addListener( selectionListener );
  }

  // ------------------------------------------------------------------------------------
  // ITsMouseInputListener
  //

  @Override
  public boolean onMouseDoubleClick( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors,
      Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT ) {
      IStringList viselIds = vedScreen().view().listViselIdsAtPoint( aCoors );
      if( viselIds.size() > 0 ) {
        selectionManager.setSingleSelectedViselId( viselIds.first() );
      }
      return true;
    }
    return false;
  }

  @Override
  public boolean onMouseDown( Object aSource, ETsMouseButton aButton, int aState, ITsPoint aCoors, Control aWidget ) {
    if( aButton == ETsMouseButton.LEFT ) {
      IStringList viselIds = vedScreen().view().listViselIdsAtPoint( aCoors );
      if( viselIds.size() <= 0 ) {
        selectionManager.deselectAll();
      }
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
