package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import org.eclipse.osgi.util.NLS;

public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "ru.toxsoft.tsgui.m5.gui.multipane.impl.messages"; //$NON-NLS-1$

  public static String ITsResources_BTN_P_CLEAR;
  public static String ITsResources_BTN_P_FILTER;
  public static String ITsResources_FMT_ASK_REMOVE_ITEM;
  public static String ITsResources_FMT_ERR_NULL_LIFECYCLE_MANAGER;
  public static String ITsResources_FMT_MSG_ITEMS_COUNT;
  public static String ITsResources_FMT_MSG_ITEMS_FILTERED_COUNT;
  public static String ITsResources_MSG_ERR_NO_ADD_ITEM_CODE;
  public static String ITsResources_MSG_ERR_NO_EDIT_ITEM_CODE;
  public static String ITsResources_MSG_ERR_NO_REMOVE_ITEM_CODE;
  public static String ITsResources_TXT_M_TEXT;
  public static String ITsResources_TXT_P_TEXT;

  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
