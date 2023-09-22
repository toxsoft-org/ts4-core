package org.toxsoft.core.tsgui.ved.zver2.api.cfgdata;

/**
 * The tailor to component binding config data.
 *
 * @author hazard157
 */
public interface IVedBindingCfg
    extends IVedConfigBase {

  /**
   * Returns the bind component ID.
   *
   * @return String - the bind component ID
   */
  String componentId();

  /**
   * Returns the bind component property ID.
   *
   * @return String - component property ID
   */
  String propertyId();

}
