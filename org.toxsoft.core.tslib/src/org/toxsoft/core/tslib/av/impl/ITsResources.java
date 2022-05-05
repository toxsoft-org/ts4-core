package org.toxsoft.core.tslib.av.impl;

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

  String MSG_ERR_INT_VALUE_OVER_MAX_LONG      = "Integer value is greater than max allowed value " + Long.MAX_VALUE;
  String MSG_ERR_INV_NUMERICAL_AV_TEXT_FORMAT = "Numerical or timestamp value was expected";
  String MSG_ERR_INV_HEX_NUL_AV_TEXT_FORMAT   = "Format vialoation of hexadecimal number";
  String FMT_ERR_INV_AV_TEXT                  = "Format vialoation of the atomic value, tocken '%s'";
  String FMT_ERR_ENTITY_AV_NOT_STRING_TYPE    = "Type must be " + AvUtils.DDID_STRING + " rather than %s";

  // String FMT_ERR_INV_DATA_DEF_VALIDATOR_CLASS =
  // "DataDef validator class %s must implement " + ITsValidator.class.getSimpleName();

  String FMT_ERR_INV_DATA_DEF_HELPER_CLASS = "Class %s must extend %s";
  String FMT_ERR_INV_DATADEF_CONSTRUCTOR   = "Error invoking constructor with argument IDataDef on class %s";
  String FMT_ERR_INV_DATATYPE_CONSTRUCTOR  = "Error invoking constructor with argument IDataType on class %s";
  String FMT_ERR_NO_SUITABLE_CONSTRUCTORS  = "No suitable constructors to create instance of class %s";
  String FMT_ERR_INV_EMPTY_CONSTRUCTOR     = "Error invoking constructor with no arguments on class %s";

  String FMT_ERR_NO_MANDATORY_OP = "Set does not has mandatory option with ID %s";

  // ------------------------------------------------------------------------------------
  // ru.RU
  //

  // String MSG_ERR_INT_VALUE_OVER_MAX_LONG = "Целое больше поддерживаемого значения " + Long.MAX_VALUE;
  // String MSG_ERR_INV_NUMERICAL_AV_TEXT_FORMAT = "Ожидалось численное значение или метка времени";
  // String MSG_ERR_INV_HEX_NUL_AV_TEXT_FORMAT = "Неверный формат 16-ричного целого числа";
  // String FMT_ERR_INV_AV_TEXT = "Неверный формат атомарного значения '%s'";
  // String FMT_ERR_ENTITY_AV_NOT_STRING_TYPE = "Тип данного дожен быть " + AvUtils.DDID_STRING + " а не %s";

}
