package org.toxsoft.core.pas.client;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String STR_N_PAS_SERVER_ADDRESS     = Messages.getString( "ITsResources.STR_N_PAS_SERVER_ADDRESS" );
  String STR_D_PAS_SERVER_ADDRESS     = Messages.getString( "ITsResources.STR_D_PAS_SERVER_ADDRESS" );
  String STR_N_PAS_SERVER_PORT        = Messages.getString( "ITsResources.STR_N_PAS_SERVER_PORT" );
  String STR_D_PAS_SERVER_PORT        = Messages.getString( "ITsResources.STR_D_PAS_SERVER_PORT" );
  String STR_N_PAS_IN_CONN_QUEUE_SIZE = Messages.getString( "ITsResources.STR_N_PAS_IN_CONN_QUEUE_SIZE" );
  String STR_D_PAS_IN_CONN_QUEUE_SIZE = Messages.getString( "ITsResources.STR_D_PAS_IN_CONN_QUEUE_SIZE" );

  String STR_N_S5_SERVER_ADDRESS  = Messages.getString( "ITsResources.STR_N_S5_SERVER_ADDRESS" );
  String STR_D_S5_SERVER_ADDRESS  = Messages.getString( "ITsResources.STR_D_S5_SERVER_ADDRESS" );
  String STR_N_S5_SERVER_PORT     = Messages.getString( "ITsResources.STR_N_S5_SERVER_PORT" );
  String STR_D_S5_SERVER_PORT     = Messages.getString( "ITsResources.STR_D_S5_SERVER_PORT" );
  String STR_N_S5_SERVER_JMS_PORT = Messages.getString( "ITsResources.STR_N_S5_SERVER_JMS_PORT" );
  String STR_D_S5_SERVER_JMS_PORT = Messages.getString( "ITsResources.STR_D_S5_SERVER_JMS_PORT" );
  String STR_N_S5_SERVER_LOGIN    = Messages.getString( "ITsResources.STR_N_S5_SERVER_LOGIN" );
  String STR_D_S5_SERVER_LOGIN    = Messages.getString( "ITsResources.STR_D_S5_SERVER_LOGIN" );
  String STR_N_S5_SERVER_PASSWORD = Messages.getString( "ITsResources.STR_N_S5_SERVER_PASSWORD" );
  String STR_D_S5_SERVER_PASSWORD = Messages.getString( "ITsResources.STR_D_S5_SERVER_PASSWORD" );
  String STR_N_S5_MODULE_NAME     = Messages.getString( "ITsResources.STR_N_S5_MODULE_NAME" );
  String STR_D_S5_MODULE_NAME     = Messages.getString( "ITsResources.STR_D_S5_MODULE_NAME" );
  String STR_N_S5_API_INTERFACE   = Messages.getString( "ITsResources.STR_N_S5_API_INTERFACE" );
  String STR_D_S5_API_INTERFACE   = Messages.getString( "ITsResources.STR_D_S5_API_INTERFACE" );
  String STR_N_S5_API_BEAN_NAME   = Messages.getString( "ITsResources.STR_N_S5_API_BEAN_NAME" );
  String STR_D_S5_API_BEAN_NAME   = Messages.getString( "ITsResources.STR_D_S5_API_BEAN_NAME" );
  String STR_N_S5_JMS_THPOOL_SIZE = Messages.getString( "ITsResources.STR_N_S5_JMS_THPOOL_SIZE" );
  String STR_D_S5_JMS_THPOOL_SIZE = Messages.getString( "ITsResources.STR_D_S5_JMS_THPOOL_SIZE" );
  String STR_N_S5_INITER_CLASS    = Messages.getString( "ITsResources.STR_N_S5_INITER_CLASS" );
  String STR_D_S5_INITER_CLASS    = Messages.getString( "ITsResources.STR_D_S5_INITER_CLASS" );
  String STR_N_IS_CONSOLE_MSGS    = Messages.getString( "ITsResources.STR_N_IS_CONSOLE_MSGS" );
  String STR_D_IS_CONSOLE_MSGS    = Messages.getString( "ITsResources.STR_D_IS_CONSOLE_MSGS" );

  String FMT_ERR_NO_INITER_CLASS  = Messages.getString( "ITsResources.FMT_ERR_NO_INITER_CLASS" );
  String FMT_ERR_INV_INITER_CLASS = Messages.getString( "ITsResources.FMT_ERR_INV_INITER_CLASS" );
  String FMT_ERR_NO_NEW_INITER    = Messages.getString( "ITsResources.FMT_ERR_NO_NEW_INITER" );
  String FMT_INFO_CONN_TO_SERVER  = Messages.getString( "ITsResources.FMT_INFO_CONN_TO_SERVER" );
  String MSG_ERR_CANT_CONNECT_S5  = Messages.getString( "ITsResources.MSG_ERR_CANT_CONNECT_S5" );
  String MSG_ERR_CANT_INIT_PAS    = Messages.getString( "ITsResources.MSG_ERR_CANT_INIT_PAS" );
  String MSG_ERR_CANT_CONNECT     = Messages.getString( "ITsResources.MSG_ERR_CANT_CONNECT" );
  String MSG_ERR_ALREADY_EXIST    = Messages.getString( "ITsResources.MSG_ERR_ALREADY_EXIST" );

  String FMT_INFO_INIT_SERV_SOCK = Messages.getString( "ITsResources.FMT_INFO_INIT_SERV_SOCK" );
  String FMT_INFO_CONNECT        = Messages.getString( "ITsResources.FMT_INFO_CONNECT" );
  String FMT_INFO_CREATE_CHANNEL = Messages.getString( "ITsResources.FMT_INFO_CREATE_CHANNEL" );
  String FMT_ERR_EMPRY_STR_PARAM = Messages.getString( "ITsResources.FMT_ERR_EMPRY_STR_PARAM" );

  String FMT_TOO_BIG_INPUT_STRING = Messages.getString( "ITsResources.FMT_TOO_BIG_INPUT_STRING" );
}
