package org.toxsoft.tsgui.m5_2;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.bricks.quant.AbstractQuant;

/**
 * Library initialization quant.
 *
 * @author hazard157
 */
public class QuantM5
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantM5() {
    super( QuantM5.class.getSimpleName() );
  }

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // FIXME M5Utils.initAppContext( aAppContext );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    // FIXME M5Utils.initWinContext( aWinContext );
  }

}
