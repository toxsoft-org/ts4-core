package org.toxsoft.templates;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;

/**
 * The library quant.
 *
 * @author hazard157
 */
public class QuantXxxYyyGui
    extends AbstractQuant {

  /**
   * Constructor.
   */
  public QuantXxxYyyGui() {
    super( QuantXxxYyyGui.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQuant
  //

  @Override
  protected void doInitApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void doInitWin( IEclipseContext aWinContext ) {
    IXxxYyyConstants.init( aWinContext );
  }

}
