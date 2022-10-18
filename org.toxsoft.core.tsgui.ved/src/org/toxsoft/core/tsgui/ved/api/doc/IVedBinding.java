package org.toxsoft.core.tsgui.ved.api.doc;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * The link to the component.
 *
 * @author hazard157
 */
public interface IVedBinding
    extends IStridable, IPropertable {

  /**
   * Returns the binding definition.
   *
   * @return {@link IVedBindingProvider} - the binding definition
   */
  IVedBindingProvider bindInfo();

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

  /**
   * Returns the information about entity properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

}
