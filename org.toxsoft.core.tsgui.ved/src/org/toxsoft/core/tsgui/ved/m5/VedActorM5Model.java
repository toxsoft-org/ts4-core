package org.toxsoft.core.tsgui.ved.m5;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.ved.m5.IVedM5Constants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;

/**
 * M5-model of {@link IVedActor},
 *
 * @author hazard157
 */
public class VedActorM5Model
    extends VedItemM5ModelBase<IVedActor> {

  /**
   * Constructor.
   */
  public VedActorM5Model() {
    super( MID_VED_ACTOR, IVedActor.class );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5CollectionPanel<IVedActor> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IVedActor> aItemsProvider, IM5LifecycleManager<IVedActor> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_REORDER.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_TREE_MODES.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_ACTIONS_HIDE_PANES.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_DETAILS_PANE_HIDDEN.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUMMARY_PANE_HIDDEN.setValue( aContext.params(), AV_FALSE );
        MultiPaneComponentModown<IVedActor> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<IVedActor> doCreateLifecycleManager( Object aMaster ) {
    return new VedActorM5LifecycleManager( this, IVedScreen.class.cast( aMaster ) );
  }

}
