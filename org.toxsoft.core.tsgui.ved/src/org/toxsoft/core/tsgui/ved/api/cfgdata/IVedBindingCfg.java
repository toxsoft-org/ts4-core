package org.toxsoft.core.tsgui.ved.api.cfgdata;

import org.toxsoft.core.tsgui.ved.api.doc.*;

/**
 * The tailor to component binding config data.
 *
 * @author hazard157
 */
public interface IVedBindingCfg
    extends IVedConfigBase {

  /**
   * Returns the ID of {@link IVedBindingProvider} to create the binding.
   *
   * @return String - binding provider ID
   */
  String bindingProviderId();

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
