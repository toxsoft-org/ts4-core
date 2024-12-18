package org.toxsoft.templates.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.quant.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.templates.*;

/**
 * Plugin addon - initializes all subsystems and modules..
 *
 * @author hazard157
 */
public class AddonXxxYyy
    extends MwsAbstractAddon {

  /**
   * Constructor.
   */
  public AddonXxxYyy() {
    super( Activator.PLUGIN_ID );
    // HERE register keepers
  }

  // ------------------------------------------------------------------------------------
  // MwsAbstractAddon
  //

  @Override
  protected void doRegisterQuants( IQuantRegistrator aQuantRegistrator ) {
    // HERE register quants
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    IXxxYyyConstants.init( aWinContext );
  }

}
