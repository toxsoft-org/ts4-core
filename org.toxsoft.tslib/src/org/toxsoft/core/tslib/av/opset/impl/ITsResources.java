package org.toxsoft.core.tslib.av.opset.impl;

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

  String FMT_ERR_CANT_CAST_OPSET_VALUE           = "Option %s: value type %s is not convertable to %s";
  String FMT_ERR_INV_OPSET_CREATION_NAME_VARARG  = "Parameter #%d must be String or IStridable";
  String FMT_ERR_INV_OPSET_CREATION_VALUE_VARARG = "Parameter #%d must be convertable to IAtomicValue";

  // ------------------------------------------------------------------------------------
  // ru.RU
  //

  // String FMT_ERR_CANT_CAST_OPSET_VALUE = "Опция %s: тип значения %s и тип опции %s несовместимы";
  // String FMT_ERR_INV_OPSET_CREATION_NAME_VARARG = "Параметр #%d должен быть String или IStridable";
  // String FMT_ERR_INV_OPSET_CREATION_VALUE_VARARG = "Параметр #%d должен быть IAtomicValue";

}
