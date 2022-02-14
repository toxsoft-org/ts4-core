package org.toxsoft.core.pas.common;

/**
 * Локализуемые ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String STR_N_PAS_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_N_PAS_FAILURE_TIMEOUT" ); //$NON-NLS-1$
  String STR_D_PAS_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_D_PAS_FAILURE_TIMEOUT" ); //$NON-NLS-1$

  String STR_N_PAS_REMOTE_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_N_PAS_REMOTE_FAILURE_TIMEOUT" ); //$NON-NLS-1$
  String STR_D_PAS_REMOTE_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_D_PAS_REMOTE_FAILURE_TIMEOUT" ); //$NON-NLS-1$

  String STR_N_PAS_SEND_PING = Messages.getString( "ITsResources.STR_N_PAS_SEND_PING" ); //$NON-NLS-1$
  String STR_D_PAS_SEND_PING = Messages.getString( "ITsResources.STR_D_PAS_SEND_PING" ); //$NON-NLS-1$

  String STR_N_PAS_WRITE_TIMEOUT = "Таймаут записи";                                                                                    //$NON-NLS-1$
  String STR_D_PAS_WRITE_TIMEOUT =
      "Таймаут (мсек), в течении которого, поток записи должен завершить запись данных в канал. В противном случае, канал будет закрыт";

  String JSON_REQUEST_TOSTRING_FORMAT      = Messages.getString( "ITsResources.JSON_REQUEST_TOSTRING_FORMAT" );      //$NON-NLS-1$
  String JSON_NOTIFICATION_TOSTRING_FORMAT = Messages.getString( "ITsResources.JSON_NOTIFICATION_TOSTRING_FORMAT" ); //$NON-NLS-1$
  String JSON_RESULT_TOSTRING_FORMAT       = Messages.getString( "ITsResources.JSON_RESULT_TOSTRING_FORMAT" );       //$NON-NLS-1$
  String JSON_ERROR_TOSTRING_FORMAT        = Messages.getString( "ITsResources.JSON_ERROR_TOSTRING_FORMAT" );        //$NON-NLS-1$

  String MSG_RECEIVE_MESSAGE          = Messages.getString( "ITsResources.MSG_RECEIVE_MESSAGE" );          //$NON-NLS-1$
  String MSG_SEND_MESSAGE             = Messages.getString( "ITsResources.MSG_SEND_MESSAGE" );             //$NON-NLS-1$
  String MSG_RECEIVE_ALIVE            = Messages.getString( "ITsResources.MSG_RECEIVE_ALIVE" );            //$NON-NLS-1$
  String MSG_UNDEF                    = Messages.getString( "ITsResources.MSG_UNDEF" );                    //$NON-NLS-1$
  String MSG_SEND_ALIVE               = Messages.getString( "ITsResources.MSG_SEND_ALIVE" );               //$NON-NLS-1$
  String MSG_SET_CHANNEL_HANDLER      = Messages.getString( "ITsResources.MSG_SET_CHANNEL_HANDLER" );      //$NON-NLS-1$
  String MSG_SET_NOTIFICATION_HANDLER = Messages.getString( "ITsResources.MSG_SET_NOTIFICATION_HANDLER" ); //$NON-NLS-1$
  String MSG_SET_REQUEST_HANDLER      = Messages.getString( "ITsResources.MSG_SET_REQUEST_HANDLER" );      //$NON-NLS-1$
  String MSG_SET_RESULT_HANDLER       = Messages.getString( "ITsResources.MSG_SET_RESULT_HANDLER" );       //$NON-NLS-1$
  String MSG_SET_ERROR_HANDLER        = Messages.getString( "ITsResources.MSG_SET_ERROR_HANDLER" );        //$NON-NLS-1$
  String MSG_SET_FAILURE_TIMEOUT      = Messages.getString( "ITsResources.MSG_SET_FAILURE_TIMEOUT" );      //$NON-NLS-1$
  String MSG_SET_WRITE_TIMEOUT        =
      "%s. Установка таймаута записи в канал. prevWriteTimeout = %s, writeTimeout = %s";
  String MSG_SET_SEND_PING            = Messages.getString( "ITsResources.MSG_SET_SEND_PING" );            //$NON-NLS-1$
  String MSG_START_DOJOB              = Messages.getString( "ITsResources.MSG_START_DOJOB" );              //$NON-NLS-1$
  String MSG_FINISH_DOJOB             = Messages.getString( "ITsResources.MSG_FINISH_DOJOB" );             //$NON-NLS-1$
  String MSG_START_WRITER             = "Начало работы писателя данных канала: thread = %s";
  String MSG_FINISH_WRITER            = "Завершение работы писателя данных канала: thread = %s";

  String ERR_INTERRUPT_WRITER                 = "Завершение работы (interrupt) писателя данных канала: thread = %s";
  String ERR_UNDEF_FAILURE_TIMEOUT            = Messages.getString( "ITsResources.MSG_ERR_UNDEF_FAILURE_TIMEOUT" );    //$NON-NLS-1$
  String ERR_WRITE_TIMEOUT                    = "checkAlive(...): %s. interrupt write thread %s by timeout (%d msec)"; //$NON-NLS-1$
  String ERR_CLOSE_CHANNEL_BY_FAILURE_TIMEOUT =
      Messages.getString( "ITsResources.MSG_ERR_CLOSE_CHANNEL_BY_FAILURE_TIMEOUT" );                                   //$NON-NLS-1$
  String ERR_READING_CHAR_TIMEOUT             = Messages.getString( "ITsResources.MSG_ERR_READING_CHAR_TIMEOUT" );     //$NON-NLS-1$

  String ERR_CREATE_READER           = Messages.getString( "ITsResources.MSG_ERR_CREATE_READER" );           //$NON-NLS-1$
  String ERR_CREATE_WRITER           = Messages.getString( "ITsResources.MSG_ERR_CREATE_WRITER" );           //$NON-NLS-1$
  String ERR_WRITE                   = Messages.getString( "ITsResources.MSG_ERR_WRITE" );                   //$NON-NLS-1$
  String ERR_READ                    = Messages.getString( "ITsResources.MSG_ERR_READ" );                    //$NON-NLS-1$
  String ERR_WRITE_CHANNEL           = Messages.getString( "ITsResources.MSG_ERR_WRITE_CHANNEL" );           //$NON-NLS-1$
  String ERR_READ_CHANNEL            = Messages.getString( "ITsResources.MSG_ERR_READ_CHANNEL" );            //$NON-NLS-1$
  String ERR_BREAK_CONNECTION        = Messages.getString( "ITsResources.MSG_ERR_BREAK_CONNECTION" );        //$NON-NLS-1$
  String ERR_REQUEST_UNEXPECTED      = Messages.getString( "ITsResources.MSG_ERR_REQUEST_UNEXPECTED" );      //$NON-NLS-1$
  String ERR_NOTIFICATION_UNEXPECTED = Messages.getString( "ITsResources.MSG_ERR_NOTIFICATION_UNEXPECTED" ); //$NON-NLS-1$
  String ERR_REMOTE_FORMAT           = Messages.getString( "ITsResources.MS_ERR_REMOTE_FORMAT" );            //$NON-NLS-1$
  String ERR_REMOTE_UNEXPECTED       = Messages.getString( "ITsResources.MS_ERR_REMOTE_UNEXPECTED" );        //$NON-NLS-1$

  String ERR_NO_LEFT_BRACE   = Messages.getString( "ITsResources.MSG_ERR_NO_LEFT_BRACE" );   //$NON-NLS-1$
  String ERR_UNEXPECTED_EOF  = Messages.getString( "ITsResources.MSG_ERR_UNEXPECTED_EOF" );  //$NON-NLS-1$
  String ERR_EMPRY_STR_PARAM = Messages.getString( "ITsResources.MSG_ERR_EMPRY_STR_PARAM" ); //$NON-NLS-1$

  String ERR_NO_CFG_FILE        = Messages.getString( "ITsResources.MSG_ERR_NO_CFG_FILE" );         //$NON-NLS-1$
  String ERR_IGNORED_INV_ARG_ID = Messages.getString( "ITsResources.MSG_WARN_IGNORED_INV_ARG_ID" ); //$NON-NLS-1$
  String ERR_ARG_VAL_AS_STRING  = Messages.getString( "ITsResources.MSG_WARN_ARG_VAL_AS_STRING" );  //$NON-NLS-1$

  String ERR_REQUEST_IMPL = Messages.getString( "ITsResources.MSG_WRONG_REQUEST_IMPL" ); //$NON-NLS-1$

  String ERR_METHOD_NOT_FOUND         = Messages.getString( "ITsResources.MSG_ERR_METHOD_NOT_FOUND" );         //$NON-NLS-1$
  String ERR_UNEXPECTED               = Messages.getString( "ITsResources.MSG_ERR_UNEXPECTED" );               //$NON-NLS-1$
  String ERR_RESULT_HANDLER_NOT_FOUND = Messages.getString( "ITsResources.MSG_ERR_RESULT_HANDLER_NOT_FOUND" ); //$NON-NLS-1$
  String ERR_SELF_ADDR_CONNECTION     = Messages.getString( "ITsResources.MSG_ERR_SELF_ADDR_CONNECTION" );     //$NON-NLS-1$

  String ERR_DOJOB_INTERRUPT = Messages.getString( "ITsResources.MSG_ERR_DOJOB_INTERRUPT" ); //$NON-NLS-1$
}
