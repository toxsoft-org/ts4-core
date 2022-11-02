package org.toxsoft.core.tsgui.ved.api.entity;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base in terface of VED entities providers.
 *
 * @author hazard157
 */
public interface IVedEntityProvider
    extends IStridableParameterized {

  /**
   * Returns the kind of provider entities.
   *
   * @return {@link EVedEntityKind} - the kind of entities
   */
  EVedEntityKind entityKind();

  /**
   * Returns the information about entity properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Creates the entity instance with default values of fields.
   *
   * @param <T> - expected type of created entity must match {@link EVedEntityKind#entityClass()}
   * @param aCfg {@link IVedEntityConfig} - entity config data
   * @param aVedEnv {@link IVedEnvironment} the VED environment
   * @return &lt;T&gt; - created entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException config entity kind does not matches provided entity kind
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  <T extends IVedEntity> T create( IVedEntityConfig aCfg, IVedEnvironment aVedEnv );

}
