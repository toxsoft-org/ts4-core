package org.toxsoft.core.tslib.av.validators;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link AbstractAvValidator}.
   */
  String FMT_ERR_VALUE_WRONG_AT = "Вместо %s обнаружен тип %s у значения '%s'";

  /**
   * {@link NameStringAvValidator}
   */
  String MSG_WARN_NULL_AV_NAME = "Имя (название) не задано (имеет значение IAtomicValue.NULL)";

}
