package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.items.*;
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
