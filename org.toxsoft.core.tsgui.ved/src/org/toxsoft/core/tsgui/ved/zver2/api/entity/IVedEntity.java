package org.toxsoft.core.tsgui.ved.zver2.api.entity;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Base interface of all VED entities.
 *
 * @author hazard157
 */
public interface IVedEntity
    extends IStridable, IPropertable, IGenericChangeEventCapable {

  /**
   * Returns the kind of provider entities.
   *
   * @return {@link EVedEntityKind} - the kind of entities
   */
  default EVedEntityKind entityKind() {
    return provider().entityKind();
  }

  /**
   * Returns the provider that created this entity.
   *
   * @return {@link IVedEntityProvider} - the provider (the factory that created entity)
   */
  IVedEntityProvider provider();

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
