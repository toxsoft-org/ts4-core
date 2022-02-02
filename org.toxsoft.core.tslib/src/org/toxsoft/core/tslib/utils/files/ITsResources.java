package org.toxsoft.core.tslib.utils.files;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link EFsObjKind}
   */
  String STR_N_FOK_FILE = "Файл";
  String STR_D_FOK_FILE = "Только файлы";
  String STR_N_FOK_DIR  = "Папка";
  String STR_D_FOK_DIR  = "Только папки";
  String STR_N_FOK_BOTH = "Все";
  String STR_D_FOK_BOTH = "И файлы, и папки";

  // ------------------------------------------------------------------------------------
  // en.EN
  //

  String FMT_ERR_FILE_NOT_EXISTS       = "File '%s' does not exists";
  String FMT_ERR_PATH_IS_NOT_FILE      = "Path '$s' does to denotes a file";
  String FMT_ERR_FILE_NOT_READABLE     = "No read access to the file '%s'" + "";
  String FMT_ERR_FILE_NOT_WRITEABLE    = "No write access to the file '%s'";
  String FMT_ERR_DIR_NOT_EXISTS        = "Directory '%s' does not exists";
  String FMT_ERR_PATH_IS_NOT_DIRECTORY = "Path '%s' does not denotes a directory";
  String FMT_ERR_DIR_NOT_READABLE      = "No read access to the directory '%s'";
  String FMT_ERR_DIR_NOT_WRITEABLE     = "No write access to the directory '%s'";
  String FMT_ERR_CANT_UNIQUE_FILE      = "Can not create unique name for '%s', though enumerated already "
      + TsFileUtils.MAX_UNIQUE_FILE_NAME_PREFIXES + " different names!";
  String ERR_FMT_FSOBJ_NOT_EXISTS      = "File/direcotry '%s' does not exists";

  // ------------------------------------------------------------------------------------
  // ru.RU
  //

  // String FMT_ERR_FILE_NOT_EXISTS = "Файл '%s' не существует";
  // String FMT_ERR_PATH_IS_NOT_FILE = "Путь '$s' указывает не на файл";
  // String FMT_ERR_FILE_NOT_READABLE = "Нет доступа на чтение файла '%s'";
  // String FMT_ERR_FILE_NOT_WRITEABLE = "Нет доступа на запись в файл '%s'";
  // String FMT_ERR_DIR_NOT_EXISTS = "Каталог '%s' не существует";
  // String FMT_ERR_PATH_IS_NOT_DIRECTORY = "Путь '%s' указывает не на каталог";
  // String FMT_ERR_DIR_NOT_READABLE = "Нет доступа на чтение директория '%s'";
  // String FMT_ERR_DIR_NOT_WRITEABLE = "Нет доступа на запись в директорию '%s'";
  // String FMT_ERR_CANT_UNIQUE_FILE = "Не могу создать уникальное имя для '%s', а перебрал уже "
  // + FileUtils.MAX_UNIQUE_FILE_NAME_PREFIXES + " разных имен!";

}
