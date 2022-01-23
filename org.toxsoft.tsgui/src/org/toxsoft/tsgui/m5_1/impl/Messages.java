package org.toxsoft.tsgui.m5_1.impl;

import org.eclipse.osgi.util.NLS;

@SuppressWarnings( "javadoc" )
public class Messages
    extends NLS {

  private static final String BUNDLE_NAME = "org.toxsoft.tsgui.m5.impl.messages"; //$NON-NLS-1$

  public static String FMT_ERR_AV_IS_NULL;
  public static String FMT_ERR_CANT_GET_FIELD_VALUE_AS_CLASS;
  public static String FMT_ERR_CANT_USE_MODEL_FOR_ENTITY_CLASS;
  public static String FMT_ERR_INV_VALUE_CLASS;
  public static String FMT_ERR_LC_OBJECT_NOT_OF_MODEL;
  public static String FMT_ERR_LC_VALUES_NOT_OF_MODEL;
  public static String FMT_ERR_MASTER_OBJ_IS_NULL;
  public static String FMT_ERR_MASTER_OBJ_OF_INV_CLASS;
  public static String FMT_ERR_MODEL_CANT_CREATE_OBJS;
  public static String FMT_ERR_MODEL_CANT_EDIT_OBJS;
  public static String FMT_ERR_MODEL_CANT_REMOVE_OBJS;
  public static String FMT_ERR_MODEL_ID_ALREADY_IN_DOMAIN;
  public static String FMT_ERR_NO_CREATION_CODE;
  public static String FMT_ERR_NO_CTX_REF;
  public static String FMT_ERR_NO_DEF_MANAGER_FOR_NULL;
  public static String FMT_ERR_NO_EDIT_CODE;
  public static String FMT_ERR_NO_LM_CREATOR_CODE;
  public static String FMT_ERR_NO_MODEL_ID_IN_DOMAIN;
  public static String FMT_ERR_NO_OWNER_MODEL;
  public static String FMT_ERR_NO_REMOVE_CODE;
  public static String MSG_ERR_NO_TEXT_GETTER_CODE;
  public static String FMT_ERR_NO_TO_AV_CODE;
  public static String FMT_ERR_NO_VALUE_GETTER_CODE;
  public static String FMT_ERR_MODEL_ALREADY_INITED;
  public static String STR_D_ROOT_DOMAIN;

  public static String STR_N_ROOT_DOMAIN;
  static {
    // initialize resource bundle
    NLS.initializeMessages( BUNDLE_NAME, Messages.class );
  }

  private Messages() {
  }
}
