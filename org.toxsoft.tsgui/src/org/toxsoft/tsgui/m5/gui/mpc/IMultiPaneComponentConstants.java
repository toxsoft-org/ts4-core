package org.toxsoft.tsgui.m5.gui.mpc;

import static org.toxsoft.tsgui.m5.gui.mpc.ITsResources.*;
import static org.toxsoft.tslib.ITsHardConstants.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;

/**
 * Multi pane component configuration constants.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc" } )
public interface IMultiPaneComponentConstants {

  String MPC_OP_ID = TS_ID + ".m5.gui.mpc"; //$NON-NLS-1$

  IDataDef OPDEF_IS_TOOLBAR      = bdd( "IsToolbar", true, STR_N_IS_TOOLBAR, STR_D_IS_TOOLBAR ); //$NON-NLS-1$
  IDataDef OPDEF_IS_FILTER_PANE  = bdd( "IsFilterPane", false, STR_N_IS_, STR_D_IS_ );           //$NON-NLS-1$
  IDataDef OPDEF_IS_DETAILS_PANE = bdd( "IsDetailsPane", false, STR_N_IS_, STR_D_IS_ );          //$NON-NLS-1$
  IDataDef OPDEF_IS_SUMMARY_PANE = bdd( "IsSummaryPane", false, STR_N_IS_, STR_D_IS_ );          //$NON-NLS-1$

  IDataDef OPDEF_IS_SUPPORTS_CHECKS    = bdd( "IsSupportsChecks", false, STR_N_IS_, STR_D_IS_ );   //$NON-NLS-1$
  IDataDef OPDEF_IS_SUPPORTS_TREE      = bdd( "IsSupportsTree", false, STR_N_IS_, STR_D_IS_ );     //$NON-NLS-1$
  IDataDef OPDEF_IS_ACTIONS_CRUD       = bdd( "IsActionsCrud", false, STR_N_IS_, STR_D_IS_ );      //$NON-NLS-1$
  IDataDef OPDEF_IS_ACTIONS_MAKE_COPY  = bdd( "IsActionsMakeCopy", false, STR_N_IS_, STR_D_IS_ );  //$NON-NLS-1$
  IDataDef OPDEF_IS_ACTIONS_REFRESH    = bdd( "IsActionsRefresh", false, STR_N_IS_, STR_D_IS_ );   //$NON-NLS-1$
  IDataDef OPDEF_IS_ACTIONS_REORDER    = bdd( "IsActionsReorder", false, STR_N_IS_, STR_D_IS_ );   //$NON-NLS-1$
  IDataDef OPDEF_IS_ACTIONS_HIDE_PANES = bdd( "IsActionsHidePanes", false, STR_N_IS_, STR_D_IS_ ); //$NON-NLS-1$

  IDataDef OPDEF_IS_COLUMN_HEADER = null;

  IDataDef OPDEF_NODE_ICON_SIZE = DataDef.create( MPC_OP_ID + ".NodeIconSize", EAtomicType.VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_NODE_ICON_SIZE, //
      TSID_DESCRIPTION, STR_D_NODE_ICON_SIZE, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_16X16 ) //
  );

  IDataDef OPDEF_DBLCLICK_ACTION_ID = null;
  IDataDef OPDEF_DETAILS_PANE_PLACE = null;

  // ------------------------------------------------------------------------------------
  // Helpers

  static IDataDef bdd( String aSuffix, boolean aDefaultValue, String aName, String aDescription ) {
    return DataDef.create( MPC_OP_ID + '.' + aSuffix, EAtomicType.BOOLEAN, //
        TSID_NAME, aName, //
        TSID_DESCRIPTION, aDescription, //
        TSID_FORMAT_STRING, FMT_BOOL_CHECK_AV, //
        TSID_DEFAULT_VALUE, Boolean.valueOf( aDefaultValue ) //
    );
  }

}
