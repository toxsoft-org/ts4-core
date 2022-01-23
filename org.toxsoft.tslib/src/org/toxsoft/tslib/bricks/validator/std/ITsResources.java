package org.toxsoft.tslib.bricks.validator.std;

/**
 * Локадлизуемые ресрсы валидаторов.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link EmptyStringValidator}
   */
  String MSG_STRING_VALUE_IS_EMPTY = "Значение - пустая строка";

  /**
   * {@link FileObjectValidator}
   */
  String FMT_ERR_NOT_A_FILE          = "Объект %s не является файлом";
  String FMT_ERR_NOT_A_DIR           = "Объект %s не является директорией";
  String FMT_ERR_NON_EXSISTANT_FILE  = "Файл %s не существует";
  String FMT_WARN_NON_EXSISTANT_FILE = "Должен существовать файл %s";
  String FMT_ERR_NON_EXSISTANT_DIR   = "Директория %s не существует";
  String FMT_WARN_NON_EXSISTANT_DIR  = "Должна существовать директория %s";

  /**
   * {@link IdPathStringValidator}
   */
  String FMT_ERR_INV_ID_PATH = "Идентификатор '%s' должен быть ИД-путем";
  String FMT_ERR_INV_ID_NAME = "Идентификатор '%s' должен быть ИД-именем";
  String MSG_WARN_NONE_ID    = "Нежелательный идентификатор %s";

  /**
   * {@link NameStringValidator}
   */
  String MSG_WARN_EMPTY_NAME   = "Имя (название) не должно быть пустой строкой";
  String MSG_WARN_DEFAULT_NAME = "Не следует использовать название умолчанию";

}
