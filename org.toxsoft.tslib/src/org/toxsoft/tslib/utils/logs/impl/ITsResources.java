package org.toxsoft.tslib.utils.logs.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  // ------------------------------------------------------------------------------------
  // en.EN
  //

  String STR_EXCEPTION_MESSAGE_LABEL   = "Message: ";
  String STR_CAUSED_BY                 = "Caused by:";
  String STR_N_TIMESTAMP_ON_EVERY_LINE = "Timestamp?";
  String STR_D_TIMESTAMP_ON_EVERY_LINE = "Start every line with timestamp";
  String STR_N_INDENT_AS_TIMESTAMP     = "Indent as timestamp";
  String STR_D_INDENT_AS_TIMESTAMP     = "Inden all lines as if timestamp presents";
  String STR_N_INDENT_WIDTH            = "Indent width";
  String STR_D_INDENT_WIDTH            = "Indent characters count for every lebvel of nested messaged";
  String FMT_ERR_NO_LOG_FILE           = "Log file '%s' does not exists";
  String FMT_ERR_CANT_CREATE_LOG_FILE  = "Can not create log file '%s'";
  String FMT_ERR_CANT_WRITE_LOG_FILE   = "Can not write to the log file '%s'";

  // ------------------------------------------------------------------------------------
  // ru.RU
  //

  // String STR_EXCEPTION_MESSAGE_LABEL = "Сообщение: ";
  // String STR_CAUSED_BY = "Вызвано исключением:";
  // String STR_N_TIMESTAMP_ON_EVERY_LINE = "Метка времени?";
  // String STR_D_TIMESTAMP_ON_EVERY_LINE = "Признак вывода метки времени в начале каждой строки";
  // String STR_N_INDENT_AS_TIMESTAMP = "Выравнивания под время";
  // String STR_D_INDENT_AS_TIMESTAMP = "Признак выравнивания как будто есть метка времени";
  // String STR_N_INDENT_WIDTH = "Ширина отступа";
  // String STR_D_INDENT_WIDTH = "Ширина (в символах) отступа уровня при форматировании сообщений об ошибках";
  // String FMT_ERR_NO_LOG_FILE = "Лог-файл '%s' не существует";
  // String FMT_ERR_CANT_CREATE_LOG_FILE ="Невозможно создать лог-файл '%s'";
  // String FMT_ERR_CANT_WRITE_LOG_FILE ="Невозможно записать в лог-файл '%s'";

}
