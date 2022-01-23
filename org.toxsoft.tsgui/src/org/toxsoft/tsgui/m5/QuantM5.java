package org.toxsoft.tsgui.m5;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.bricks.quant.AbstractQuant;
import org.toxsoft.tsgui.m5.model.impl.M5Utils;

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
    M5Utils.initAppContext( aAppContext );
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    M5Utils.initWinContext( aWinContext );
  }

}
