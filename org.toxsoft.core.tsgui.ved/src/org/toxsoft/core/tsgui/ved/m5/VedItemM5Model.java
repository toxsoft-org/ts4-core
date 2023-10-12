package org.toxsoft.core.tsgui.ved.m5;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.ved.ITsguiVedConstants.*;
import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.std.fields.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;

/**
 * M5-model of the {@link IVedItem}.
 * <p>
 * This is a simple model just for both actors and VISELs listing.
 *
 * @author hazard157
 */
public class VedItemM5Model
    extends M5Model<IVedItem> {

  /**
   * The model ID.
   */
  public static final String MODEL_ID = VED_ID + ".VedItem"; //$NON-NLS-1$

  /**
   * ID of the field {@link #FACTORY_ID}.
   */
  public static final String FID_FACTORY_ID = "factoryId"; //$NON-NLS-1$

  /**
   * Field {@link IVedItem#id()}
   */
  public final IM5AttributeFieldDef<IVedItem> ID = new M5StdFieldDefId<>( //
      TSID_NAME, STR_ITEM_ID, //
      TSID_DESCRIPTION, STR_ITEM_ID_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  ) {

    protected Image doGetFieldValueIcon( IVedItem aEntity, EIconSize aIconSize ) {
      String iconId = aEntity.iconId();
      if( iconId != null ) {
        return iconManager().loadStdIcon( iconId, aIconSize );
      }
      return null;
    }
  };

  /**
   * Field {@link IVedItem#factoryId()}
   */
  public final IM5AttributeFieldDef<IVedItem> FACTORY_ID = new M5AttributeFieldDef<>( FID_FACTORY_ID, DDEF_IDPATH, //
      TSID_NAME, STR_ITEM_FACTORY_ID, //
      TSID_DESCRIPTION, STR_ITEM_FACTORY_ID_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL ) //
  ) {

    protected IAtomicValue doGetFieldValue( IVedItem aEntity ) {
      return avStr( aEntity.factoryId() );
    }

  };

  /**
   * Field {@link IVedItem#nmName()}
   */
  public final IM5AttributeFieldDef<IVedItem> NAME = new M5StdFieldDefName<>( //
      TSID_NAME, STR_ITEM_NAME, //
      TSID_DESCRIPTION, STR_ITEM_NAME_D, //
      M5_OPID_FLAGS, avInt( M5FF_COLUMN ) //
  );

  /**
   * Field {@link IVedItem#description()}
   */
  public final IM5AttributeFieldDef<IVedItem> DESCRIPTION = new M5StdFieldDefDescription<>( //
      TSID_NAME, STR_ITEM_DESCRIPTION, //
      TSID_DESCRIPTION, STR_ITEM_DESCRIPTION_D, //
      M5_OPID_FLAGS, avInt( M5FF_DETAIL ) //
  );

  /**
   * Constructor.
   */
  public VedItemM5Model() {
    super( MODEL_ID, IVedItem.class );
    addFieldDefs( ID, FACTORY_ID, NAME, DESCRIPTION );
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
