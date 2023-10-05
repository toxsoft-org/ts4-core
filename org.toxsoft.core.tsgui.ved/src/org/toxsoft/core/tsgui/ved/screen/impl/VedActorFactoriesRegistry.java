package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.items.*;
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
