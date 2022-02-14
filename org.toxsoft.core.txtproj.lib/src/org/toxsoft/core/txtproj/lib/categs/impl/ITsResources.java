package org.toxsoft.core.txtproj.lib.categs.impl;

/**
 * Локализуемые ресурсы.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link Catalogue}
   */
  String FMT_ERR_CATEG_ALREADY_EXISTS = "Категория '%s' уже существует";
  String FMT_ERR_NO_PARENT            = "Родительская категория '%s' не существует";
  String FMT_ERR_INV_LOCAL_ID         = "Локальный идентификатор '%s' ложен быть ИД-именем";
  String FMT_ERR_NO_CATEG             = "Категории '%s' не существует";
  String MSG_ERR_CANT_EDIT_ROOT       = "Нельзя редактировать свойства каталога (корневого узла)";
  String FMT_WARN_REMOVING_SUBTREE    = "Вместе с '%s' произойдет удаление еще %d потомков-категории";
  String FMT_WARN_NO_NAME             = "Рекомендуется задать название категории '%s'";
  String MSG_ERR_READING_CATALOGUE    = "Загружаемое содержимое каталога является невалидным";

}
