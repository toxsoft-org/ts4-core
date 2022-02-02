package org.toxsoft.core.tslib.bricks.ctx.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link TsContextRefDef}
   */
  String FMT_ERR_NO_MANDATORY_REF = "В контексте отсутстввует обязательная ссылка '%s' с ключом '%s'";
  String FMT_ERR_INV_CLASS_REF    = "Ссылка '%s' в контексте имеет тип '%s', а ожидался '%s'";

  /**
   * {@link TsContextBase}
   */
  String FMT_ERR_NO_REF_TO_CLASS_IN_CTX = "В контексте нет ссылки на экземпляр типа %s";
  String FMT_ERR_NO_REF_OF_KEY_IN_CTX   = "В контексте нет ссылки с ключом %s";

}
