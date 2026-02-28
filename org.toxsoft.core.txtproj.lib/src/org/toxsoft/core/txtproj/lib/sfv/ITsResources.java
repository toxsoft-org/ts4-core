package org.toxsoft.core.txtproj.lib.sfv;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  String FMT_ERR_SECT_ID_BLANK      = "Пустой идентификатор раздела '%s'";
  String FMT_ERR_SECT_ID_NON_IDPATH = "Идентификатор раздела '%s' должен быть ИД-путем";
  String FMT_WARN_BIG_CONTENT       = "Очень большое содержимое у раздела %s";
  String FMT_WARN_SECT_ID_EXIST     = "В файле уже есть раздел %s";
  String FMT_WARN_DUP_SECT_ID_EXIST = "В файле есть другой раздел %s";
  String FMT_ERR_SECT_NOT_EXIST     = "В файле нет раздела %s";

}
