package org.toxsoft.core.pas.common;

/**
 * Локализуемые ресурсы.
 *
 * @author mvk
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String STR_N_PAS_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_N_PAS_FAILURE_TIMEOUT" );
  String STR_D_PAS_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_D_PAS_FAILURE_TIMEOUT" );

  String STR_N_PAS_REMOTE_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_N_PAS_REMOTE_FAILURE_TIMEOUT" );
  String STR_D_PAS_REMOTE_FAILURE_TIMEOUT = Messages.getString( "ITsResources.STR_D_PAS_REMOTE_FAILURE_TIMEOUT" );

  String STR_N_PAS_SEND_PING = Messages.getString( "ITsResources.STR_N_PAS_SEND_PING" );
  String STR_D_PAS_SEND_PING = Messages.getString( "ITsResources.STR_D_PAS_SEND_PING" );

  String STR_N_PAS_WRITE_TIMEOUT = "Таймаут записи";
  String STR_D_PAS_WRITE_TIMEOUT =
      "Таймаут (мсек), в течении которого, поток записи должен завершить запись данных в канал. В противном случае, канал будет закрыт";

  String FMT_CHANNEL = "remote(%s:%d), local(%s:%d), time =%s";

  String JSON_REQUEST_TOSTRING_FORMAT      = Messages.getString( "ITsResources.JSON_REQUEST_TOSTRING_FORMAT" );
  String JSON_NOTIFICATION_TOSTRING_FORMAT = Messages.getString( "ITsResources.JSON_NOTIFICATION_TOSTRING_FORMAT" );
  String JSON_RESULT_TOSTRING_FORMAT       = Messages.getString( "ITsResources.JSON_RESULT_TOSTRING_FORMAT" );
  String JSON_ERROR_TOSTRING_FORMAT        = Messages.getString( "ITsResources.JSON_ERROR_TOSTRING_FORMAT" );

  String MSG_RECEIVE_MESSAGE          = "%s. message received. json = %s, lastReadTimestamp = %s";
  String MSG_SEND_MESSAGE             = Messages.getString( "ITsResources.MSG_SEND_MESSAGE" );
  String MSG_RECEIVE_ALIVE            = Messages.getString( "ITsResources.MSG_RECEIVE_ALIVE" );
  String MSG_UNDEF                    = Messages.getString( "ITsResources.MSG_UNDEF" );
  String MSG_SEND_ALIVE               = Messages.getString( "ITsResources.MSG_SEND_ALIVE" );
  String MSG_SET_CHANNEL_HANDLER      = Messages.getString( "ITsResources.MSG_SET_CHANNEL_HANDLER" );
  String MSG_SET_NOTIFICATION_HANDLER = Messages.getString( "ITsResources.MSG_SET_NOTIFICATION_HANDLER" );
  String MSG_SET_REQUEST_HANDLER      = Messages.getString( "ITsResources.MSG_SET_REQUEST_HANDLER" );
  String MSG_SET_RESULT_HANDLER       = Messages.getString( "ITsResources.MSG_SET_RESULT_HANDLER" );
  String MSG_SET_ERROR_HANDLER        = Messages.getString( "ITsResources.MSG_SET_ERROR_HANDLER" );
  String MSG_SET_FAILURE_TIMEOUT      = Messages.getString( "ITsResources.MSG_SET_FAILURE_TIMEOUT" );
  String MSG_SET_WRITE_TIMEOUT        =
      "%s. Установка таймаута записи в канал. prevWriteTimeout = %s, writeTimeout = %s";
  String MSG_SET_SEND_PING            = Messages.getString( "ITsResources.MSG_SET_SEND_PING" );
  String MSG_START_DOJOB              = Messages.getString( "ITsResources.MSG_START_DOJOB" );
  String MSG_FINISH_DOJOB             = Messages.getString( "ITsResources.MSG_FINISH_DOJOB" );
  String MSG_START_WRITER             = "Начало работы писателя данных канала: thread = %s";
  String MSG_FINISH_WRITER            = "Завершение работы писателя данных канала: thread = %s";

  String ERR_INTERRUPT_WRITER                 = "Завершение работы (interrupt) писателя данных канала: thread = %s";
  String ERR_UNDEF_FAILURE_TIMEOUT            = Messages.getString( "ITsResources.MSG_ERR_UNDEF_FAILURE_TIMEOUT" );
  String ERR_WRITE_TIMEOUT                    = "checkAlive(...): %s. interrupt write thread %s by timeout (%d msec)";
  String ERR_CLOSE_CHANNEL_BY_FAILURE_TIMEOUT =
      "%s. close channel by timeout. failureTimeout = %d, time = %d, lastReadTimestamp = %s";
  String ERR_READING_CHAR_TIMEOUT             = Messages.getString( "ITsResources.MSG_ERR_READING_CHAR_TIMEOUT" );

  String ERR_CREATE_READER           = Messages.getString( "ITsResources.MSG_ERR_CREATE_READER" );
  String ERR_CREATE_WRITER           = Messages.getString( "ITsResources.MSG_ERR_CREATE_WRITER" );
  String ERR_WRITE                   = Messages.getString( "ITsResources.MSG_ERR_WRITE" );
  String ERR_READ                    = Messages.getString( "ITsResources.MSG_ERR_READ" );
  String ERR_WRITE_CHANNEL           = Messages.getString( "ITsResources.MSG_ERR_WRITE_CHANNEL" );
  String ERR_READ_CHANNEL            = Messages.getString( "ITsResources.MSG_ERR_READ_CHANNEL" );
  String ERR_BREAK_CONNECTION        = Messages.getString( "ITsResources.MSG_ERR_BREAK_CONNECTION" );
  String ERR_REQUEST_UNEXPECTED      = Messages.getString( "ITsResources.MSG_ERR_REQUEST_UNEXPECTED" );
  String ERR_NOTIFICATION_UNEXPECTED = Messages.getString( "ITsResources.MSG_ERR_NOTIFICATION_UNEXPECTED" );
  String ERR_REMOTE_FORMAT           = Messages.getString( "ITsResources.MS_ERR_REMOTE_FORMAT" );
  String ERR_REMOTE_UNEXPECTED       = Messages.getString( "ITsResources.MS_ERR_REMOTE_UNEXPECTED" );

  String ERR_NO_LEFT_BRACE   = Messages.getString( "ITsResources.MSG_ERR_NO_LEFT_BRACE" );
  String ERR_UNEXPECTED_EOF  = Messages.getString( "ITsResources.MSG_ERR_UNEXPECTED_EOF" );
  String ERR_EMPRY_STR_PARAM = Messages.getString( "ITsResources.MSG_ERR_EMPRY_STR_PARAM" );

  String ERR_NO_CFG_FILE        = Messages.getString( "ITsResources.MSG_ERR_NO_CFG_FILE" );
  String ERR_IGNORED_INV_ARG_ID = Messages.getString( "ITsResources.MSG_WARN_IGNORED_INV_ARG_ID" );
  String ERR_ARG_VAL_AS_STRING  = Messages.getString( "ITsResources.MSG_WARN_ARG_VAL_AS_STRING" );

  String ERR_REQUEST_IMPL = Messages.getString( "ITsResources.MSG_WRONG_REQUEST_IMPL" );

  String ERR_METHOD_NOT_FOUND         = Messages.getString( "ITsResources.MSG_ERR_METHOD_NOT_FOUND" );
  String ERR_UNEXPECTED               = Messages.getString( "ITsResources.MSG_ERR_UNEXPECTED" );
  String ERR_RESULT_HANDLER_NOT_FOUND = Messages.getString( "ITsResources.MSG_ERR_RESULT_HANDLER_NOT_FOUND" );
  String ERR_SELF_ADDR_CONNECTION     = Messages.getString( "ITsResources.MSG_ERR_SELF_ADDR_CONNECTION" );

  String ERR_DOJOB_INTERRUPT = Messages.getString( "ITsResources.MSG_ERR_DOJOB_INTERRUPT" );
}
