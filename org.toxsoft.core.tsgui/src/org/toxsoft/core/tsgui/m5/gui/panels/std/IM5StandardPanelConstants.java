package org.toxsoft.core.tsgui.m5.gui.panels.std;

import static org.toxsoft.core.tsgui.ITsGuiConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.actions.asp.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Defines the constants used to configure M5-panels before creation using {@link IM5PanelCreator}.
 * <p>
 * Important: constants is guaranteed to works when creating standard M5 panels. If panel creation is overridden by
 * model creator the constants may not work.
 *
 * @author hazard157
 */
public interface IM5StandardPanelConstants {

  // ------------------------------------------------------------------------------------
  // IM5CollectionPanel toolbar actions

  /**
   * ID of context reference {@link #REFDEF_M5STD_PANEL_ACTIONS_ASP}.
   */
  String REFID_M5STD_PANEL_ACTIONS_ASP = TSGUI_M5_ID + ".ctxref.ActionsAsp"; //$NON-NLS-1$

  /**
   * The context reference: actions to be added to the collection panel (toolbar and mode).<br>
   * Applicable to: {@link IM5CollectionPanel}<br>
   * Mandatory?: <code>false</code><br>
   * Default value: none<br>
   * Notes: contains actions to be added/substituted (depending on option {@link #OPDEF_M5STD_PANEL_IS_ASP_SUBSTITUTE})
   * to the toolbar actions.
   */
  ITsGuiContextRefDef<ITsActionSetProvider> REFDEF_M5STD_PANEL_ACTIONS_ASP = ///
      TsGuiContextRefDef.create( REFID_M5STD_PANEL_ACTIONS_ASP, ITsActionSetProvider.class, ///
          TSID_IS_MANDATORY, AV_FALSE );

  /**
   * ID of option {@link #OPID_M5STD_PANEL_IS_ASP_SUBSTITUTE}.
   */
  String OPID_M5STD_PANEL_IS_ASP_SUBSTITUTE = TSGUI_M5_ID + ".ctxop.ActionsAspMode"; //$NON-NLS-1$

  /**
   * The context option: determines if actions will be added or exiting action substituted by
   * {@link #REFDEF_M5STD_PANEL_ACTIONS_ASP}.<br>
   * Applicable to: {@link IM5CollectionPanel}<br>
   * Mandatory?: <code>false</code><br>
   * Default value: <code>false</code> (action will be added<br>
   * Notes: when set to <code>true</code> toolbar will contain only actions from the
   * {@link #REFDEF_M5STD_PANEL_ACTIONS_ASP}.<br>
   */
  IDataDef OPDEF_M5STD_PANEL_IS_ASP_SUBSTITUTE = DataDef.create( OPID_M5STD_PANEL_IS_ASP_SUBSTITUTE, BOOLEAN, ///
      TSID_IS_MANDATORY, AV_FALSE, ///
      TSID_DEFAULT_VALUE, AV_FALSE ///
  );

  // ------------------------------------------------------------------------------------
  // IM5CollectionPanel Tree mode

  /**
   * ID of context reference {@link #REFDEF_M5STD_PANEL_TREE_MODES_LIST}.
   */
  String REFID_M5STD_PANEL_TREE_MODES_LIST = TSGUI_M5_ID + ".ctxref.TreeModesList"; //$NON-NLS-1$

  /**
   * The context reference: the list of tree mode definitions.<br>
   * Applicable to: {@link IM5CollectionPanel}<br>
   * Mandatory?: <code>false</code><br>
   * Default value: none<br>
   * Notes: has effect only for {@link IM5CollectionPanel} created with option
   * {@link IMultiPaneComponentConstants#OPDEF_IS_SUPPORTS_TREE OPDEF_IS_SUPPORTS_TREE} set to <code>true</code><br>
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  ITsGuiContextRefDef<IStridablesList<TreeModeInfo<?>>> REFDEF_M5STD_PANEL_TREE_MODES_LIST = ///
      TsGuiContextRefDef.create( REFID_M5STD_PANEL_TREE_MODES_LIST, (Class)IStridablesList.class, ///
          TSID_IS_MANDATORY, AV_FALSE );

  /**
   * ID of option {@link #OPDEF_M5STD_PANEL_TREE_MODE_INIT_ID}.
   */
  String OPID_M5STD_PANEL_TREE_MODE_INIT_ID = TSGUI_M5_ID + ".ctxop.InitialTreeModeId"; //$NON-NLS-1$

  /**
   * The context option: the initial tree mode ID.<br>
   * Applicable to: {@link IM5CollectionPanel}<br>
   * Mandatory?: <code>false</code><br>
   * Default value: "" (an empty string)<br>
   * Notes: determines initial tree/list mode. Default value of an empty string means list mode, that is
   * {@link ITreeModeManager#currModeId()} = <code>null</code>. Other meaningful values are IDs of modes listed in
   * {@link #REFDEF_M5STD_PANEL_TREE_MODES_LIST}. <br>
   */
  IDataDef OPDEF_M5STD_PANEL_TREE_MODE_INIT_ID = DataDef.create( OPID_M5STD_PANEL_TREE_MODE_INIT_ID, STRING, ///
      TSID_IS_MANDATORY, AV_FALSE, ///
      TSID_DEFAULT_VALUE, AV_STR_EMPTY ///
  );

}
