package org.toxsoft.core.tsgui.valed;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.core.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.core.tsgui.valed.api.IValedControlFactoriesRegistry;
import org.toxsoft.core.tsgui.valed.impl.ValedControlFactoriesRegistry;

/**
 * Library initialization quant.
 *
 * @author hazard157
 */
public class QuantValed
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantValed() {
    super( QuantValed.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    ValedControlFactoriesRegistry registry = new ValedControlFactoriesRegistry();
    aAppContext.set( IValedControlFactoriesRegistry.class, registry );
    aAppContext.set( ValedControlFactoriesRegistry.class, registry );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // nop
  }

}
