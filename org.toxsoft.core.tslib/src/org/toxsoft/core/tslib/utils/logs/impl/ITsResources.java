package org.toxsoft.core.tslib.utils.logs.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */

@SuppressWarnings( "nls" )
interface ITsResources {

  String STR_EXCEPTION_MESSAGE_LABEL      = Messages.getString( "STR_EXCEPTION_MESSAGE_LABEL" );
  String STR_CAUSED_BY                    = Messages.getString( "STR_CAUSED_BY" );
  String FMT_ERR_NO_LOG_FILE              = Messages.getString( "FMT_ERR_NO_LOG_FILE" );
  String FMT_ERR_CANT_CREATE_LOG_FILE     = Messages.getString( "FMT_ERR_CANT_CREATE_LOG_FILE" );
  String FMT_ERR_CANT_WRITE_LOG_FILE      = Messages.getString( "FMT_ERR_CANT_WRITE_LOG_FILE" );
  String FMT_MSG_LOGGER_FACTORY_INSTALLED = "setLoggerFactory(...): the factory was installed. aFactory = %s";
}
