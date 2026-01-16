package org.toxsoft.core.tsgui.m5.gui.mpc;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Multi pane component configuration constants.
 * <p>
 * Options must be specified as {@link ITsGuiContext#params()} in constructor.
 *
 * @author hazard157
 */
public interface IMultiPaneComponentConstants {

  /**
   * Prefix of option IDs.
   */
  String MPC_OP_ID = TS_ID + ".m5.gui.mpc"; //$NON-NLS-1$

  /**
   * Specifies if toolbar is present.
   */
  IDataDef OPDEF_IS_TOOLBAR = bdd( "IsToolbar", true ); //$NON-NLS-1$

  /**
   * Specifies text displayed as a leftmost control on toolbar.
   */
  IDataDef OPDEF_IS_TOOLBAR_NAME = bdd( "IsToolbarName", false ); //$NON-NLS-1$

  /**
   * Specifies if filter pane is present.
   */
  IDataDef OPDEF_IS_FILTER_PANE = bdd( "IsFilterPane", false ); //$NON-NLS-1$

  /**
   * Specifies initial state of the filter pane: <code>true</code> - hidden, <code>false</code> - shown.
   */
  IDataDef OPDEF_IS_FILTER_PANE_HIDDEN = bdd( "IsFilterPaneHidden", false ); //$NON-NLS-1$

  /**
   * Specifies if details pane is present.
   * <p>
   * Note: event if specified <code>true</code>, details pane may not be present if no fields are marked with hint
   * {@link IM5Constants#M5FF_DETAIL}.
   */
  IDataDef OPDEF_IS_DETAILS_PANE = bdd( "IsDetailsPane", true ); //$NON-NLS-1$

  /**
   * Specifies initial state of the details pane: <code>true</code> - hidden, <code>false</code> - shown.
   */
  IDataDef OPDEF_IS_DETAILS_PANE_HIDDEN = bdd( "IsDetailsPaneHidden", false );//$NON-NLS-1$

  /**
   * Specifies if summary pane is present.
   */
  IDataDef OPDEF_IS_SUMMARY_PANE = bdd( "IsSummaryPane", false ); //$NON-NLS-1$

  /**
   * Specifies initial state of the summary pane: <code>true</code> - hidden, <code>false</code> - shown.
   */
  IDataDef OPDEF_IS_SUMMARY_PANE_HIDDEN = bdd( "IsSummaryPaneHidden", false ); //$NON-NLS-1$

  /**
   * Specifies if tree control will have check boxes for each item.
   */
  IDataDef OPDEF_IS_SUPPORTS_CHECKS = bdd( "IsSupportsChecks", false ); //$NON-NLS-1$

  /**
   * Specifies if MPC has a tree mode support, not only default list mode.
   * <p>
   * Option must be defined before calling to constructor.
   */
  IDataDef OPDEF_IS_SUPPORTS_TREE = bdd( "IsSupportsTree", false ); //$NON-NLS-1$

  /**
   * Specifies if toolbar contains "tree mode" and "list mode" buttons.
   * <p>
   * Has no sense if {@link #OPDEF_IS_SUPPORTS_TREE} is set to <code>false</code>.
   */
  IDataDef OPDEF_IS_ACTIONS_TREE_MODES = bdd( "IsActionsTreeMODES", true ); //$NON-NLS-1$

  /**
   * Specifies if CRUD action buttons (Add, Edit, Remove) are present on toolbar.
   */
  IDataDef OPDEF_IS_ACTIONS_CRUD = bdd( "IsActionsCrud", false ); //$NON-NLS-1$

  /**
   * Specifies if "Add copy" button is present on toolbar.
   * <p>
   * Ignored if {@link #OPDEF_IS_ACTIONS_CRUD} = <code>false</code>.
   */
  IDataDef OPDEF_IS_ADD_COPY_ACTION = bdd( "IsAddCopyAction", false ); //$NON-NLS-1$

  /**
   * Specifies if action "Refresh" is present on toolbar.
   */
  IDataDef OPDEF_IS_ACTIONS_REFRESH = bdd( "IsActionsRefresh", false ); //$NON-NLS-1$

  /**
   * Specifies if reorder actions ("Move selected item first/previous/next/last") are present on toolbar.
   */
  IDataDef OPDEF_IS_ACTIONS_REORDER = bdd( "IsActionsReorder", false ); //$NON-NLS-1$

  /**
   * Specifies if toggle buttons to hide/show panel (details/filter/summary) are present on toolbar.
   */
  IDataDef OPDEF_IS_ACTIONS_HIDE_PANES = bdd( "IsActionsHidePanes", false ); //$NON-NLS-1$

  /**
   * Specifies column headers are present in the table tree.
   */
  IDataDef OPDEF_IS_COLUMN_HEADER = bdd( "IsColumnHeader", true ); //$NON-NLS-1$

  /**
   * Specifies the size of the icons used in the tree nodes.<br>
   * Type: {@link EAtomicType#VALOBJ} - {@link EIconSize}
   */
  IDataDef OPDEF_NODE_ICON_SIZE = DataDef.create( MPC_OP_ID + ".NodeIconSize", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_16X16 ) //
  );

  /**
   * Specifies the size of the thumbnail images used in the tree nodes.<br>
   * Type: {@link EAtomicType#VALOBJ} - {@link EThumbSize}
   */
  IDataDef OPDEF_NODE_THUMB_SIZE = DataDef.create( MPC_OP_ID + ".NodeThumbSize", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ96 ) //
  );

  /**
   * Specifies action ID (usually one of the toolbar button action IDs) executed on mouse double click on item.<br>
   * Type: {@link EAtomicType#STRING}
   */
  IDataDef OPDEF_DBLCLICK_ACTION_ID = DataDef.create( MPC_OP_ID + ".DblclickActionId", STRING, //$NON-NLS-1$
      TSID_DEFAULT_VALUE, avStr( ACTID_EDIT ) //
  );

  /**
   * Specifies the location of the details pane relative to the table tree.<br>
   * Type: {@link EAtomicType#VALOBJ} - {@link EBorderLayoutPlacement}
   */
  IDataDef OPDEF_DETAILS_PANE_PLACE = DataDef.create( MPC_OP_ID + ".DetailsPanePlace", VALOBJ, //$NON-NLS-1$
      TSID_KEEPER_ID, EBorderLayoutPlacement.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EBorderLayoutPlacement.SOUTH ) //
  );

  // ------------------------------------------------------------------------------------
  // Helpers

  /**
   * Creates boolean option definition.
   *
   * @param aSuffix String - suffix of action ID
   * @param aDefaultValue boolean - default value of the option
   * @return {@link IDataDef} - created option definition
   */
  static IDataDef bdd( String aSuffix, boolean aDefaultValue ) {
    return DataDef.create( MPC_OP_ID + '.' + aSuffix, EAtomicType.BOOLEAN, //
        TSID_FORMAT_STRING, FMT_BOOL_CHECK_AV, //
        TSID_DEFAULT_VALUE, Boolean.valueOf( aDefaultValue ) //
    );
  }

}
