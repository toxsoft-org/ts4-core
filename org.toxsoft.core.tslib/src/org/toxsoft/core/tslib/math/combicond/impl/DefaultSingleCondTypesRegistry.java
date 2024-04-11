package org.toxsoft.core.tslib.math.combicond.impl;

import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.math.combicond.*;

/**
 * {@link ISingleCondTypesRegistry} implementation.
 *
 * @author hazard157
 */
public class DefaultSingleCondTypesRegistry
    extends StridablesRegisrty<ISingleCondType>
    implements ISingleCondTypesRegistry<ISingleCondType> {

  /**
   * Singleton instance of the registry.
   */
  public static final ISingleCondTypesRegistry<ISingleCondType> INSTANCE = new DefaultSingleCondTypesRegistry();

  /**
   * Constructor.
   */
  public DefaultSingleCondTypesRegistry() {
    super( ISingleCondType.class );
  }

}
