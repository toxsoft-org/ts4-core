package org.toxsoft.core.tsgui.ved.editor;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.geometry.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

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
      removeViselVertexSet();
      if( viselId != null ) {
        createViselVertexSet( viselId );
      }
    }

  }

  private final IVedViselSelectionManager selectionManager;
  private final SelectionListener         selectionListener;

  private VedAbstractVertexSet viselVertexSet = null;

  /**
   * Constructor.
   *
   * @param aScreen {@link IVedScreen} - the owner VED screen
   * @param aSelectionManager {@link IVedViselSelectionManager} - selection manager for VISELs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedViselVertexSetManager( IVedScreen aScreen, IVedViselSelectionManager aSelectionManager ) {
    super( aScreen );
    TsNullArgumentRtException.checkNull( aSelectionManager );
    selectionManager = aSelectionManager;
    selectionListener = new SelectionListener();
    selectionManager.genericChangeEventer().addListener( selectionListener );
  }

  public boolean createViselVertexSet( String aViselId ) {
    VedAbstractVisel visel = vedScreen().model().visels().list().getByKey( aViselId );
    if( viselVertexSet != null ) {
      return false;
    }
    viselVertexSet = visel.createVertexSet();
    vedScreen().model().screenDecoratorsAfter().add( viselVertexSet );
    vedScreen().model().screenHandlersBefore().insert( 0, viselVertexSet.inputHandler() );
    vedScreen().view().redraw();
    return true;
  }

  public void removeViselVertexSet() {
    if( viselVertexSet != null ) {
      vedScreen().model().screenHandlersBefore().remove( viselVertexSet.inputHandler() );
      vedScreen().model().screenDecoratorsAfter().remove( viselVertexSet );
      viselVertexSet = null;
      vedScreen().view().redraw();
    }
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
      // if( viselIds.size() <= 0 && selectionManager.selectedViselIds().size() > 0 ) {
      if( viselIds.size() <= 0 || !selectionManager.selectedViselIds().hasElem( viselIds.first() ) ) {
        if( selectionManager.selectedViselIds().size() > 0 ) {
          selectionManager.deselectAll();
          return true;
        }
        return false;
      }
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // ITsKeyInputListener
  //

  @Override
  public boolean onKeyDown( Object aSource, int aCode, char aChar, int aState ) {
    if( aCode == SWT.ESC ) {
      removeViselVertexSet();
      return true;
    }
    return false;
  }
}
