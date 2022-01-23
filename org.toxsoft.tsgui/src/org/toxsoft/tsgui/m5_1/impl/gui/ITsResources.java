package org.toxsoft.tsgui.m5_1.impl.gui;

import org.toxsoft.tsgui.valed.impl.ValedControlFactoriesRegistry;

/**
 * Локализуемые ресурсы.
 *
 * @author goga
 */
interface ITsResources {

  /**
   * {@link M5EntityPanelWithValeds}
   */
  String MSG_ERR_NO_FACTORIES_REGISTRY_IN_CONTEXT = Messages.ITsResources_MSG_ERR_NO_FACTORIES_REGISTRY_IN_CONTEXT
      + ValedControlFactoriesRegistry.class.getSimpleName();
  String FMT_ERR_NO_FACTORY_IN_PARAMS             = Messages.ITsResources_FMT_ERR_NO_FACTORY_IN_PARAMS;
  String FMT_CANT_SET_FIELD_VALUE                 = Messages.ITsResources_FMT_CANT_SET_FIELD_VALUE;
  String FMT_ERR_FIELD_VALIDATION_FAIL            = Messages.ITsResources_FMT_ERR_FIELD_VALIDATION_FAIL;

}
