package org.toxsoft.core.tsgui.ved.m5;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tsgui.ved.m5.IVedM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;

/**
 * M5-model of the {@link IVedItem}.
 * <p>
 * This is a simple model just for both actors and VISELs listing.
 *
 * @author hazard157
 */
public class VedItemM5Model
    extends VedItemM5ModelBase<IVedItem> {

  /**
   * Constructor.
   */
  public VedItemM5Model() {
    super( MID_VED_ITEM, IVedItem.class );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5CollectionPanel<IVedItem> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IVedItem> aItemsProvider, IM5LifecycleManager<IVedItem> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_REORDER.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_TREE_MODES.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_TRUE );
        MultiPaneComponentModown<IVedItem> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        VedItemM5TreeMakeByKind treeMakerByKind = new VedItemM5TreeMakeByKind();
        TreeModeInfo<IVedItem> tmiByKind = new TreeModeInfo<>( "ByKind", //$NON-NLS-1$
            STR_TMI_ITEM_BY_KIND, STR_TMI_ITEM_BY_KIND_D, null, treeMakerByKind );
        mpc.treeModeManager().setHasTreeMode( true );
        mpc.treeModeManager().addTreeMode( tmiByKind );
        mpc.treeModeManager().setCurrentMode( tmiByKind.id() );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<IVedItem> doCreateLifecycleManager( Object aMaster ) {
    return new VedItemM5LifecycleManager( this, IVedScreen.class.cast( aMaster ) );
  }

}
