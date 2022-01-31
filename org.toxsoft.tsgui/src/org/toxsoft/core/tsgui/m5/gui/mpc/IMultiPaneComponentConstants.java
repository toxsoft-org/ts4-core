package org.toxsoft.core.tsgui.m5.gui.mpc;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.ITsResources.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;

/**
 * Multi pane component configuration constants.
 *
 * @author hazard157
 */
@SuppressWarnings( { "javadoc" } )
public interface IMultiPaneComponentConstants {

  String MPC_OP_ID = TS_ID + ".m5.gui.mpc"; //$NON-NLS-1$

  IDataDef OPDEF_IS_TOOLBAR = bdd( "IsToolbar", true, //$NON-NLS-1$
      STR_N_IS_TOOLBAR, STR_D_IS_TOOLBAR );

  IDataDef OPDEF_IS_FILTER_PANE = bdd( "IsFilterPane", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_DETAILS_PANE = bdd( "IsDetailsPane", true, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_SUMMARY_PANE = bdd( "IsSummaryPane", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_SUPPORTS_CHECKS = bdd( "IsSupportsChecks", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_SUPPORTS_TREE = bdd( "IsSupportsTree", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_ACTIONS_CRUD = bdd( "IsActionsCrud", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_ACTIONS_MAKE_COPY = bdd( "IsActionsMakeCopy", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_ACTIONS_REFRESH = bdd( "IsActionsRefresh", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_ACTIONS_REORDER = bdd( "IsActionsReorder", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_ACTIONS_HIDE_PANES = bdd( "IsActionsHidePanes", false, //$NON-NLS-1$
      STR_N_IS_, STR_D_IS_ );

  IDataDef OPDEF_IS_COLUMN_HEADER = bdd( "IsColumnHeader", false, //$NON-NLS-1$
      STR_N_IS_COLUMN_HEADER, STR_D_IS_COLUMN_HEADER );

  IDataDef OPDEF_NODE_ICON_SIZE = DataDef.create( MPC_OP_ID + ".NodeIconSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_NODE_ICON_SIZE, //
      TSID_DESCRIPTION, STR_D_NODE_ICON_SIZE, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_16X16 ) //
  );

  IDataDef OPDEF_DBLCLICK_ACTION_ID = DataDef.create( MPC_OP_ID + ".DblclickActionId", STRING, //$NON-NLS-1$
      TSID_NAME, STR_N_DBLCLICK_ACTION_ID, //
      TSID_DESCRIPTION, STR_D_DBLCLICK_ACTION_ID, //
      TSID_FORMAT_STRING, FMT_BOOL_CHECK_AV, //
      TSID_DEFAULT_VALUE, avStr( ACTID_EDIT ) //
  );

  IDataDef OPDEF_DETAILS_PANE_PLACE = DataDef.create( MPC_OP_ID + ".DetailsPanePlace", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_DETAILS_PANE_PLACE, //
      TSID_DESCRIPTION, STR_D_DETAILS_PANE_PLACE, //
      TSID_KEEPER_ID, EBorderLayoutPlacement.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EBorderLayoutPlacement.SOUTH ) //
  );

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
