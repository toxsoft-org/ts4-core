package org.toxsoft.core.tsgui.ved.olds.incub;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;

/**
 * Mixin interface of entities with properties.
 *
 * @author hazard157
 */
public interface IPropertableDef
    extends IStridableParameterized {

  /**
   * Returns the options definitions for the {@link IPropertable#props()}.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - properties info
   */
  IStridablesList<IDataDef> propDefs();

}
