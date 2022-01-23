package org.toxsoft.tslib.av.misc;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link AvTextParser}
   */
  String FMT_CANT_PARSE_ANY_TYPE       = "Невозможно интепретировать текст '%s' как значение одного из типов '%s'";
  String FMT_CANT_PARSE_AS_TYPE        = "Невозможно интепретировать текст '%s' как значение типа '%s'";
  String FMT_ERR_EMPTY_STRING_NOT_TYPE = "Пустая строка не может интерпретироваться как значение типа '%s'";

}
