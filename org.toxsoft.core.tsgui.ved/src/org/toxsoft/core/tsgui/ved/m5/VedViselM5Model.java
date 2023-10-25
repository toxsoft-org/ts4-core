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
 * M5-model of {@link IVedVisel},
 *
 * @author hazard157
 */
public class VedViselM5Model
    extends VedItemM5ModelBase<IVedVisel> {

  /**
   * Constructor.
   */
  public VedViselM5Model() {
    super( MID_VED_VISEL, IVedVisel.class );
    setPanelCreator( new M5DefaultPanelCreator<>() {

      protected IM5CollectionPanel<IVedVisel> doCreateCollEditPanel( ITsGuiContext aContext,
          IM5ItemsProvider<IVedVisel> aItemsProvider, IM5LifecycleManager<IVedVisel> aLifecycleManager ) {
        OPDEF_IS_ACTIONS_CRUD.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_REORDER.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_ACTIONS_TREE_MODES.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_SUPPORTS_TREE.setValue( aContext.params(), AV_FALSE );
        OPDEF_IS_ACTIONS_HIDE_PANES.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_DETAILS_PANE_HIDDEN.setValue( aContext.params(), AV_TRUE );
        OPDEF_IS_SUMMARY_PANE_HIDDEN.setValue( aContext.params(), AV_FALSE );
        MultiPaneComponentModown<IVedVisel> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, aLifecycleManager );
        return new M5CollectionPanelMpcModownWrapper<>( mpc, false );
      }
    } );
  }

  @Override
  protected IM5LifecycleManager<IVedVisel> doCreateLifecycleManager( Object aMaster ) {
    return new VedViselM5LifecycleManager( this, IVedScreen.class.cast( aMaster ) );
  }

}
