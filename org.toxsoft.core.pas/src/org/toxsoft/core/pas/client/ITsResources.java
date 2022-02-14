package org.toxsoft.core.pas.client;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
interface ITsResources {

  String STR_N_PAS_SERVER_ADDRESS     = Messages.getString( "ITsResources.STR_N_PAS_SERVER_ADDRESS" );     //$NON-NLS-1$
  String STR_D_PAS_SERVER_ADDRESS     = Messages.getString( "ITsResources.STR_D_PAS_SERVER_ADDRESS" );     //$NON-NLS-1$
  String STR_N_PAS_SERVER_PORT        = Messages.getString( "ITsResources.STR_N_PAS_SERVER_PORT" );        //$NON-NLS-1$
  String STR_D_PAS_SERVER_PORT        = Messages.getString( "ITsResources.STR_D_PAS_SERVER_PORT" );        //$NON-NLS-1$
  String STR_N_PAS_IN_CONN_QUEUE_SIZE = Messages.getString( "ITsResources.STR_N_PAS_IN_CONN_QUEUE_SIZE" ); //$NON-NLS-1$
  String STR_D_PAS_IN_CONN_QUEUE_SIZE = Messages.getString( "ITsResources.STR_D_PAS_IN_CONN_QUEUE_SIZE" ); //$NON-NLS-1$

  String STR_N_S5_SERVER_ADDRESS  = Messages.getString( "ITsResources.STR_N_S5_SERVER_ADDRESS" );  //$NON-NLS-1$
  String STR_D_S5_SERVER_ADDRESS  = Messages.getString( "ITsResources.STR_D_S5_SERVER_ADDRESS" );  //$NON-NLS-1$
  String STR_N_S5_SERVER_PORT     = Messages.getString( "ITsResources.STR_N_S5_SERVER_PORT" );     //$NON-NLS-1$
  String STR_D_S5_SERVER_PORT     = Messages.getString( "ITsResources.STR_D_S5_SERVER_PORT" );     //$NON-NLS-1$
  String STR_N_S5_SERVER_JMS_PORT = Messages.getString( "ITsResources.STR_N_S5_SERVER_JMS_PORT" ); //$NON-NLS-1$
  String STR_D_S5_SERVER_JMS_PORT = Messages.getString( "ITsResources.STR_D_S5_SERVER_JMS_PORT" ); //$NON-NLS-1$
  String STR_N_S5_SERVER_LOGIN    = Messages.getString( "ITsResources.STR_N_S5_SERVER_LOGIN" );    //$NON-NLS-1$
  String STR_D_S5_SERVER_LOGIN    = Messages.getString( "ITsResources.STR_D_S5_SERVER_LOGIN" );    //$NON-NLS-1$
  String STR_N_S5_SERVER_PASSWORD = Messages.getString( "ITsResources.STR_N_S5_SERVER_PASSWORD" ); //$NON-NLS-1$
  String STR_D_S5_SERVER_PASSWORD = Messages.getString( "ITsResources.STR_D_S5_SERVER_PASSWORD" ); //$NON-NLS-1$
  String STR_N_S5_MODULE_NAME     = Messages.getString( "ITsResources.STR_N_S5_MODULE_NAME" );     //$NON-NLS-1$
  String STR_D_S5_MODULE_NAME     = Messages.getString( "ITsResources.STR_D_S5_MODULE_NAME" );     //$NON-NLS-1$
  String STR_N_S5_API_INTERFACE   = Messages.getString( "ITsResources.STR_N_S5_API_INTERFACE" );   //$NON-NLS-1$
  String STR_D_S5_API_INTERFACE   = Messages.getString( "ITsResources.STR_D_S5_API_INTERFACE" );   //$NON-NLS-1$
  String STR_N_S5_API_BEAN_NAME   = Messages.getString( "ITsResources.STR_N_S5_API_BEAN_NAME" );   //$NON-NLS-1$
  String STR_D_S5_API_BEAN_NAME   = Messages.getString( "ITsResources.STR_D_S5_API_BEAN_NAME" );   //$NON-NLS-1$
  String STR_N_S5_JMS_THPOOL_SIZE = Messages.getString( "ITsResources.STR_N_S5_JMS_THPOOL_SIZE" ); //$NON-NLS-1$
  String STR_D_S5_JMS_THPOOL_SIZE = Messages.getString( "ITsResources.STR_D_S5_JMS_THPOOL_SIZE" ); //$NON-NLS-1$
  String STR_N_S5_INITER_CLASS    = Messages.getString( "ITsResources.STR_N_S5_INITER_CLASS" );    //$NON-NLS-1$
  String STR_D_S5_INITER_CLASS    = Messages.getString( "ITsResources.STR_D_S5_INITER_CLASS" );    //$NON-NLS-1$
  String STR_N_IS_CONSOLE_MSGS    = Messages.getString( "ITsResources.STR_N_IS_CONSOLE_MSGS" );    //$NON-NLS-1$
  String STR_D_IS_CONSOLE_MSGS    = Messages.getString( "ITsResources.STR_D_IS_CONSOLE_MSGS" );    //$NON-NLS-1$

  String FMT_ERR_NO_INITER_CLASS  = Messages.getString( "ITsResources.FMT_ERR_NO_INITER_CLASS" );  //$NON-NLS-1$
  String FMT_ERR_INV_INITER_CLASS = Messages.getString( "ITsResources.FMT_ERR_INV_INITER_CLASS" ); //$NON-NLS-1$
  String FMT_ERR_NO_NEW_INITER    = Messages.getString( "ITsResources.FMT_ERR_NO_NEW_INITER" );    //$NON-NLS-1$
  String FMT_INFO_CONN_TO_SERVER  = Messages.getString( "ITsResources.FMT_INFO_CONN_TO_SERVER" );  //$NON-NLS-1$
  String MSG_ERR_CANT_CONNECT_S5  = Messages.getString( "ITsResources.MSG_ERR_CANT_CONNECT_S5" );  //$NON-NLS-1$
  String MSG_ERR_CANT_INIT_PAS    = Messages.getString( "ITsResources.MSG_ERR_CANT_INIT_PAS" );    //$NON-NLS-1$
  String MSG_ERR_CANT_CONNECT     = Messages.getString( "ITsResources.MSG_ERR_CANT_CONNECT" );     //$NON-NLS-1$
  String MSG_ERR_ALREADY_EXIST    = Messages.getString( "ITsResources.MSG_ERR_ALREADY_EXIST" );    //$NON-NLS-1$

  String FMT_INFO_INIT_SERV_SOCK = Messages.getString( "ITsResources.FMT_INFO_INIT_SERV_SOCK" ); //$NON-NLS-1$
  String FMT_INFO_CONNECT        = Messages.getString( "ITsResources.FMT_INFO_CONNECT" );        //$NON-NLS-1$
  String FMT_INFO_CREATE_CHANNEL = Messages.getString( "ITsResources.FMT_INFO_CREATE_CHANNEL" ); //$NON-NLS-1$
  String FMT_ERR_EMPRY_STR_PARAM = Messages.getString( "ITsResources.FMT_ERR_EMPRY_STR_PARAM" ); //$NON-NLS-1$

  String FMT_TOO_BIG_INPUT_STRING = Messages.getString( "ITsResources.FMT_TOO_BIG_INPUT_STRING" ); //$NON-NLS-1$
}
