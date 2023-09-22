package org.toxsoft.core.tsgui.ved.zver2.api.doc;

import org.toxsoft.core.tsgui.ved.zver2.api.entity.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The VED tailor, depending on his own, subject-specific properties, "dresses" the components.
 * <p>
 * Tailor "translates" domain-specific property values to the drawing properties of the components. For example, when
 * boolean property "enabled/disabled" changes tailor will change the lamp (circular component) property "color" from
 * green to red.
 *
 * @author hazard157
 */
public interface IVedTailor
    extends IVedEntity {

  /**
   * Returns binding providers available for this tailor.
   *
   * @return {@link IStridablesList}&lt;{@link IVedBindingProvider}&gt; - available bindings providers
   */
  IStridablesList<IVedBindingProvider> bindingProviders();

  /**
   * Returns the currently existing bindings to the components.
   *
   * @return {@link IStridablesList}&lt;{@link IVedBinding}&gt; - existing bindings
   */
  IStridablesList<IVedBinding> bindings();

}
