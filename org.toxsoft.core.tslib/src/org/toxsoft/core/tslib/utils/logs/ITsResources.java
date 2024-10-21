package org.toxsoft.core.tslib.utils.logs;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String STR_LOG_SEVERITY_DEBUG   = "Debug";
  String STR_LOG_SEVERITY_DEBUG_D   = "Message for developers must not be present in fincal code";
  String STR_LOG_SEVERITY_ERROR   = "Error";
  String STR_LOG_SEVERITY_ERROR_D   = "Error occured, program may not work partially or crash completely";
  String STR_LOG_SEVERITY_WARNING = "Warrning";
  String STR_LOG_SEVERITY_WARNING_D = "A recoverable problem occured, note that it may lead to errors";
  String STR_LOG_SEVERITY_INFO    = "Info";
  String STR_LOG_SEVERITY_INFO_D    = "Informational message, program is working nomally";

}
