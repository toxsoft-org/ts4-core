package org.toxsoft.core.tsgui.ved.incub.undoman;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.ved.incub.undoman.tsgui.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The UNDO/REDO item for the VED screen editor.
 * <p>
 * On each edit operations the whole edited content is remembered and restored when UNDO is performed. Content are
 * stored as {@link IVedScreenCfg} instances.
 *
 * @author hazard157
 */
public class VedUndoItem
    extends AbstractUndoRedoItem {

  private final IVedScreenCfg cfgBefore;
  private final IVedScreenCfg cfgAfter;

  /**
   * Constructor.
   *
   * @param aManager {@link VedUndoManager} - the owner manager
   * @param aCfgBefore {@link IVedScreenCfg} - editor content <b>before</b> editing
   * @param aCfgAfter {@link IVedScreenCfg} - editor content <b>after</b> editing
   * @param aName String - displayable name of the UNDO/REDO item
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedUndoItem( VedUndoManager aManager, IVedScreenCfg aCfgBefore, IVedScreenCfg aCfgAfter, String aName ) {
    super( aManager, TSID_NAME, aName );
    TsNullArgumentRtException.checkNulls( aManager, aCfgBefore, aCfgAfter );
    cfgBefore = aCfgBefore;
    cfgAfter = aCfgAfter;
  }

  // ------------------------------------------------------------------------------------
  // AbstractUndoRedoItem
  //

  @SuppressWarnings( "unchecked" )
  @Override
  public VedUndoManager manager() {
    return VedUndoManager.class.cast( super.manager() );
  }

  @Override
  public void undo() {
    VedScreenUtils.setVedScreenConfig( manager().vedScreen(), cfgBefore );
  }

  @Override
  public void redo() {
    VedScreenUtils.setVedScreenConfig( manager().vedScreen(), cfgAfter );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "boxing" )
  @Override
  public String toString() {
    return String.format( "%s Op: %s, Visels: %d/%d, Actors: %s/%d", //$NON-NLS-1$
        this.getClass().getSimpleName(), DDEF_NAME.getValue( params() ).asString(), //
        cfgAfter.viselCfgs().size(), cfgBefore.viselCfgs().size(), //
        cfgAfter.actorCfgs().size(), cfgBefore.actorCfgs().size() //
    );
  }

}
