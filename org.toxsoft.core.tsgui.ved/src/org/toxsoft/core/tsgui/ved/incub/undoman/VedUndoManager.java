package org.toxsoft.core.tsgui.ved.incub.undoman;

import org.toxsoft.core.tsgui.ved.incub.undoman.tsgui.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IUndoManager} implementation for a {@link IVedScreen} editing operations,
 * <p>
 * Usage:
 * <ul>
 * <li>create the instance of the manager;</li>
 * <li>create {@link AspUndoManager} instance and add actions to the GUI;</li>
 * <li>that's all - undo manager will became active.</li>
 * </ul>
 *
 * @author hazard157
 */
public class VedUndoManager
    extends UndoManager {

  private final IVedScreen vedScreen;

  private IVedScreenCfg cfgBefore = IVedScreenCfg.NONE;

  /**
   * Constructor.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen to manage UNDO/REDO operations for
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedUndoManager( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    vedScreen = aVedScreen;
    vedScreen.model().visels().eventer().addListener( ( sec, op, id ) -> whenVedItemsEdited( op, id ) );
    vedScreen.model().actors().eventer().addListener( ( sec, op, id ) -> whenVedItemsEdited( op, id ) );
    cfgBefore = VedScreenUtils.getVedScreenConfig( vedScreen );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenVedItemsEdited( ECrudOp aOp, String aId ) {
    if( isPerformingUndoRedo() ) { // ignore editing when UNDO/REDO is performing
      return;
    }
    IVedScreenCfg cfgAfter = VedScreenUtils.getVedScreenConfig( vedScreen );
    String opName = aOp.name();
    if( aId != null ) {
      opName = String.format( "%s[%s]", aOp.nmName(), aId ); //$NON-NLS-1$
    }
    AbstractUndoRedoItem undoItem = new VedUndoItem( this, cfgBefore, cfgAfter, opName );
    addUndoredoItem( undoItem );
    cfgBefore = cfgAfter;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the managed VED screen.
   *
   * @return {@link IVedScreen} - the VED screen
   */
  public IVedScreen vedScreen() {
    return vedScreen;
  }

}
