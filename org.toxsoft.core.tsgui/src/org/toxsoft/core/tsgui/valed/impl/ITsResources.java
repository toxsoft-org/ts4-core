package org.toxsoft.core.tsgui.valed.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ITsResources {

  /**
   * {@link AbstractValedControl}
   */
  String FMT_ERR_CANT_GET_VALUE_BEFORE_ITS_SET = "Редактор %s: - нельзя запрашивать значение до его установки";

  /**
   * {@link ValedControlFactoriesRegistry}
   */
  String FMT_WARN_CANT_CREATE_CLASS_INSTANCE = "При авто-создании фабрики невозможно создать экземпляр класса %s";
  String FMT_WARN_INSTANCE_NOT_FACTORY       = "При авто-создании фабрики не является фабрикой объект класса %s";

}
