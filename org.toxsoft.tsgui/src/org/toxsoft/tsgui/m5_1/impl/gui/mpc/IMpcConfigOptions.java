package org.toxsoft.tsgui.m5_1.impl.gui.mpc;

import static org.toxsoft.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.tsgui.m5_1.impl.gui.mpc.ITsResources.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.tsgui.bricks.actions.ITsStdActionDefs;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.m5.gui.viewers.impl.M5AbstractCollectionViewer;
import org.toxsoft.tsgui.m5_1.api.IM5ItemsProvider;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl.BasicMultiPaneComponent;
import org.toxsoft.tsgui.utils.layout.EBorderLayoutPlacement;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;

/**
 * MPC configuration options.
 *
 * @author hazard157
 */
public interface IMpcConfigOptions {

  /**
   * Option IDs prefix.
   */
  String MPC_ID = "ts.m5.mpc"; //$NON-NLS-1$

  /**
   * Details pane usage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - details pane will be created alongside to the tree viewer, <code>false</code> -
   * no details pane will be created at all.<br>
   * <i>Default value:</i> <code>true</code>
   */
  IDataDef USE_DETAILS_PANE = DataDef.create( MPC_ID + "UseDetailsPane", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_USE_DETAILS_PANE, //
      TSID_DESCRIPTION, STR_D_MPT_USE_DETAILS_PANE, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Toolbar usage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - toolbar will be created and placed {@link EBorderLayoutPlacement#NORTH} to the
   * tree viewer, <code>false</code> - no toolbar will be created at all.<br>
   * <i>Default value:</i> <code>true</code>
   */
  IDataDef USE_TOOL_BAR = DataDef.create( MPC_ID + "UseToolBar", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_USE_TOOL_BAR, //
      TSID_DESCRIPTION, STR_D_MPT_USE_TOOL_BAR, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Datail pane placement relative to the tree viewer.<br>
   * <i>Type:</i> {@link EAtomicType#VALOBJ} - {@link EBorderLayoutPlacement}<br>
   * <i>Usage:</i> allowed values are {@link EBorderLayoutPlacement#WEST}, {@link EBorderLayoutPlacement#EAST} and
   * {@link EBorderLayoutPlacement#SOUTH}. Other values will be set to default value.<br>
   * <i>Default value:</i> {@link EBorderLayoutPlacement#EAST}
   */
  IDataDef DETAILS_PANE_PLACEMENT = DataDef.create( MPC_ID + "DetailsPanePlacement", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_DETAILS_PANE_PLACEMENT, //
      TSID_DESCRIPTION, STR_D_MPT_DETAILS_PANE_PLACEMENT, //
      TSID_KEEPER_ID, EBorderLayoutPlacement.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EBorderLayoutPlacement.SOUTH ) //
  );

  /**
   * Summary pane usage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - summary pane will be created and placed {@link EBorderLayoutPlacement#SOUTH} to
   * the tree viewer, <code>false</code> - no summary pane will be created at all.<br>
   * <i>Default value:</i> <code>false</code>
   */
  IDataDef USE_SUMMARY_PANE = DataDef.create( MPC_ID + "UseSummaryPane", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_USE_SUMMARY_PANE, //
      TSID_DESCRIPTION, STR_D_MPT_USE_SUMMARY_PANE, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Panes hide/show buttons uasage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - on the toolbar will be placed buttons to hide/show for each of used panes
   * (details, summary, filter), <code>false</code> - no such buttons will be created at all;<br>
   * <i>Default value:</i> <code>true</code>
   */
  IDataDef USE_HIDE_BUTTONS = DataDef.create( MPC_ID + "UseHideButtons", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_USE_HIDE_BUTTONS, //
      TSID_DESCRIPTION, STR_D_MPT_USE_HIDE_BUTTONS, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Filter pane usage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - filter pane will be created and placed {@link EBorderLayoutPlacement#NORTH} to
   * the tree viewer below the toolbar, <code>false</code> - no filter pane will be created at all.<br>
   * <i>Default value:</i> <code>false</code>
   */
  IDataDef USE_FILTER_PANE = DataDef.create( MPC_ID + "UseFilterPane", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_USE_FILTER_PANE, //
      TSID_DESCRIPTION, STR_D_MPT_USE_FILTER_PANE, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Tree mode control buttons usage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - toolbar will contain tree control buttons (tree/table selector, tree grouping
   * mode selector, expand/collapse all), <code>false</code> - no tree control buttons (however programming API is
   * working anyway).<br>
   * <i>Default value:</i> <code>false</code>
   */
  IDataDef HAS_TREE_MODE = DataDef.create( MPC_ID + "HasTreeMode", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_HAS_TREE_MODE, //
      TSID_DESCRIPTION, STR_D_MPT_HAS_TREE_MODE, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Items editing buttons usage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - toolbar will contain editing buttons (add/edit/remove) andcorresponding mouse and
   * keyboard actions will work, <code>false</code> - no edit buttons (however programming API is working anyway).<br>
   * <i>Default value:</i> <code>true</code>
   */
  IDataDef HAS_EDIT_ACTIONS = DataDef.create( MPC_ID + "HasEditActions", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_D_MPT_HAS_EDIT_ACTIONS, //
      TSID_DESCRIPTION, STR_N_MPT_HAS_EDIT_ACTIONS, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Controls if tree widget has header with column names.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - widget will have header with column names and sorting change capability by mouse
   * click on column header, <code>false</code> - no header in widget, useful for trees with single column ho increase
   * vertical space.<br>
   * <i>Default value:</i> <code>true</code>
   */
  IDataDef HAS_TABLE_HEADER = DataDef.create( MPC_ID + "HasTableHeader", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_HAS_TABLE_HEADER, //
      TSID_DESCRIPTION, STR_D_MPT_HAS_TABLE_HEADER, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Items reorder actions usage flag.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - toolbar will contain reordering buttons (move item up/down/first/last) and
   * corresponding mouse and keyboard actions will work, <code>false</code> - no reoderingbuttons (however programming
   * API is working anyway).<br>
   * Note that visible reodering buttons will be permanently disabled if {@link IM5ItemsProvider#reorderer()} =
   * <code>null</code>.<br>
   * <i>Default value:</i> <code>true</code>
   */
  IDataDef HAS_REORDER_ACTIONS = DataDef.create( MPC_ID + "HasReorderActions", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_HAS_REORDER_ACTIONS, //
      TSID_DESCRIPTION, STR_D_MPT_HAS_REORDER_ACTIONS, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * FIXME TRANSLATE
   * <p>
   * FIXME CHECK Actions must be determined automatically !
   * <p>
   * Признак наличия действий по пометке/разметке объектов в списке.<br>
   * <i>Type:</i> {@link EAtomicType#BOOLEAN}<br>
   * <i>Usage:</i> <code>true</code> - явно указывает, что в панели инструментов должны быть кнопки изменения состояния
   * пометки (пометить все/убрать все пометки), <code>false</code> - таких кнопок нет на панели инструментов. При
   * нажатии кнокпо вызваются соответствющие методы-заглушки для наследника.<br>
   * <i>Default value:</i> <code>true</code>
   */
  IDataDef HAS_CHECK_ACTIONS = DataDef.create( MPC_ID + "HasCheckActions", BOOLEAN, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_HAS_CHECK_ACTIONS, //
      TSID_DESCRIPTION, STR_D_MPT_HAS_CHECK_ACTIONS, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * размер значков узлов дерева.<br>
   * <i>Type:</i> {@link EIconSize}<br>
   * <i>Usage:</i> задает размер значков узлов дерева методом
   * {@link M5AbstractCollectionViewer#setIconSize(EIconSize)}.<br>
   * <i>Default value:</i> {@link EIconSize#IS_16X16} - размер 16x16
   */
  IDataDef NODE_ICON_SIZE = DataDef.create( MPC_ID + "NodeIconSize", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_NODE_ICON_SIZE, //
      TSID_DESCRIPTION, STR_D_MPT_NODE_ICON_SIZE, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_16X16 ) //
  );

  /**
   * Целочисленный идентификатор действия при двойном щелчке на элементе.<br>
   * <i>Type:</i> {@link EAtomicType#STRING}<br>
   * <i>Usage:</i> значение используется для выполнения действия методом
   * {@link BasicMultiPaneComponent#processAction(int)} в том случае, когда наследник не обработал двойной щелчок в
   * методе {@link BasicMultiPaneComponent}<code>.doProcessTreeDoubleClick()</code>.<br>
   * <i>Default value:</i> {@link ITsStdActionDefs#ACTID_EDIT} (edit selected item)
   */
  IDataDef DOUBLE_CLICK_ACTION_ID = DataDef.create( MPC_ID + "DoubleClickActionId", STRING, //$NON-NLS-1$
      TSID_NAME, STR_N_MPT_DOUBLE_CLICK_ACTION_ID, //
      TSID_DESCRIPTION, STR_D_MPT_DOUBLE_CLICK_ACTION_ID, //
      TSID_DEFAULT_VALUE, avStr( ACTID_EDIT ) //
  );

  // FIXME HAS_REODER_ABILITY

}
