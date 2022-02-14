package org.toxsoft.core.pas.server;

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

  String FMT_ERR_NO_INITER_CLASS  = Messages.getString( "ITsResources.FMT_ERR_NO_INITER_CLASS" );  //$NON-NLS-1$
  String FMT_ERR_INV_INITER_CLASS = Messages.getString( "ITsResources.FMT_ERR_INV_INITER_CLASS" ); //$NON-NLS-1$
  String FMT_ERR_NO_NEW_INITER    = Messages.getString( "ITsResources.FMT_ERR_NO_NEW_INITER" );    //$NON-NLS-1$
  String FMT_INFO_CONN_TO_SERVER  = Messages.getString( "ITsResources.FMT_INFO_CONN_TO_SERVER" );  //$NON-NLS-1$
  String MSG_ERR_CANT_CONNECT_S5  = Messages.getString( "ITsResources.MSG_ERR_CANT_CONNECT_S5" );  //$NON-NLS-1$
  String MSG_ERR_CANT_INIT_PAS    = Messages.getString( "ITsResources.MSG_ERR_CANT_INIT_PAS" );    //$NON-NLS-1$
  String MSG_ERR_RECREATE_CHANNEL = Messages.getString( "ITsResources.MSG_ERR_RECREATE_CHANNEL" ); //$NON-NLS-1$

  String FMT_INFO_INIT_SERV_SOCK  = Messages.getString( "ITsResources.FMT_INFO_INIT_SERV_SOCK" );  //$NON-NLS-1$
  String FMT_INFO_CLIENT_ACCEPTED = Messages.getString( "ITsResources.FMT_INFO_CLIENT_ACCEPTED" ); //$NON-NLS-1$
  String FMT_ERR_EMPRY_STR_PARAM  = Messages.getString( "ITsResources.FMT_ERR_EMPRY_STR_PARAM" );  //$NON-NLS-1$

  String FMT_TOO_BIG_INPUT_STRING = Messages.getString( "ITsResources.FMT_TOO_BIG_INPUT_STRING" ); //$NON-NLS-1$

}
