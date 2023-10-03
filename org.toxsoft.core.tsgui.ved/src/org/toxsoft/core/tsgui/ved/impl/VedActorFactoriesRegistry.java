package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;

/**
 * {@link IVedActorFactoriesRegistry} implementation.
 *
 * @author hazard157
 */
public class VedActorFactoriesRegistry
    extends StridablesRegisrty<IVedActorFactory>
    implements IVedActorFactoriesRegistry {

  /**
   * Constructor.
   */
  public VedActorFactoriesRegistry() {
    super( IVedActorFactory.class );
  }

}
