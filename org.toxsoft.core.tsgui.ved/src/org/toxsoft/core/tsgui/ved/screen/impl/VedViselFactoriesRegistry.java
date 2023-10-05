package org.toxsoft.core.tsgui.ved.screen.impl;

import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;

/**
 * {@link IVedViselFactoriesRegistry} implementation.
 *
 * @author hazard157
 */
public class VedViselFactoriesRegistry
    extends StridablesRegisrty<IVedViselFactory>
    implements IVedViselFactoriesRegistry {

  /**
   * Constructor.
   */
  public VedViselFactoriesRegistry() {
    super( IVedViselFactory.class );
  }

}
