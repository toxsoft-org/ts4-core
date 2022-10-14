package org.toxsoft.core.tsgui.ved.core.base;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Base interface of VED entities.
 *
 * @author hazard157
 */
public interface IVedEntity
    extends IStridable, IPropertable, IGenericChangeEventCapable {

  /**
   * Returns the provider that created this entity.
   *
   * @param <P> - expected type of the provider
   * @return {@link IVedEntityProviderBase} - the provider (the factory that created entity)
   */
  <P extends IVedEntityProviderBase> P provider();

  /**
   * Returns the capbilites constants.
   *
   * @return {@link IOptionSetEdit} - capabilities
   */
  IOptionSet capabilities();

  /**
   * Returns the values of the external data.
   *
   * @return {@link INotifierOptionSetEdit} - the external data
   */
  INotifierOptionSetEdit extdata();

}
