package org.toxsoft.core.tsgui.ved.zver1.core.base;

import org.toxsoft.core.tsgui.ved.zver1.core.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base in terface of VED entities providers.
 *
 * @author hazard157
 */
public interface IVedEntityProviderBase
    extends IParameterized {

  /**
   * Returns the provider ID as pair of IDpaths.
   *
   * @return {@link IdPair} - the provider ID
   */
  IdPair pid();

  /**
   * Returns the information about entity properties.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties definitions
   */
  IStridablesList<IDataDef> propDefs();

  /**
   * Creates the entity instance with default values of fields.
   *
   * @param <T> - expected type of created entity
   * @param aId String - the ID of entity to be created
   * @param aVedEnv {@link IVedEnvironment} the VED environment
   * @return &lt;T&gt; - created component
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException any property value is not compatible to the property definition
   */
  <T extends IVedEntity> T create( String aId, IVedEnvironment aVedEnv );

}
