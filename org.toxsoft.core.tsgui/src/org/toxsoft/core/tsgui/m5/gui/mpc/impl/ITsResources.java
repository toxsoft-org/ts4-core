package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link MpcFilterPaneWrapper}
   */
  String BTN_P_FILTER = "Turn filter on/off";
  String BTN_P_CLEAR  = "Reset filter";

  /**
   * {@link MultiPaneComponent}
   */
  String MSG_ERR_NO_ADD_ITEM_CODE    = "No addItem() code";
  String MSG_ERR_NO_EDIT_ITEM_CODE   = "No editItem() code";
  String MSG_ERR_NO_REMOVE_ITEM_CODE = "No removeItem() code";

  /**
   * {@link MpcSummaryPaneMessage}
   */
  String FMT_MSG_ITEMS_COUNT          = "Total count: %d";
  String FMT_MSG_ITEMS_FILTERED_COUNT = "Total count: %d (shown: %d)";

}
