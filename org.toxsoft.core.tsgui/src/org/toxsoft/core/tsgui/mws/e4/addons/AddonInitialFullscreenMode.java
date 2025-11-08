package org.toxsoft.core.tsgui.mws.e4.addons;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.progargs.*;

/**
 * Addon to process {@link #CMDLINEARG_INITIAL_FULLSCREEN} command line argument.
 *
 * @author hazard157
 */
public class AddonInitialFullscreenMode
    extends MwsAbstractAddon {

  /**
   * If this boolean command line argument is <code>true</code>, application will start in full-screen mode.
   * <p>
   * Absence of the option is interpreted as <code>false</code>.
   */
  public static final String CMDLINEARG_INITIAL_FULLSCREEN = "InitialFullscreenMode"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public AddonInitialFullscreenMode() {
    super( AddonInitialFullscreenMode.class.getSimpleName() );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static boolean getInitialFullscreenModeArgValue( ProgramArgs aProgramArgs ) {
    if( aProgramArgs.hasArg( CMDLINEARG_INITIAL_FULLSCREEN ) ) {
      String argValue = aProgramArgs.getArgValue( CMDLINEARG_INITIAL_FULLSCREEN, Boolean.FALSE.toString() );
      return argValue.equalsIgnoreCase( Boolean.TRUE.toString() );
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    ProgramArgs programArgs = aWinContext.get( ProgramArgs.class );
    if( getInitialFullscreenModeArgValue( programArgs ) ) {
      Display display = aWinContext.get( Display.class );
      ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );
      display.asyncExec( () -> {
        IStringMapEdit<String> args = new StringMap<>();
        args.put( MWSID_CMDARG_FS_ONOFF, Boolean.TRUE.toString() );
        e4Helper.execCmd( MWSID_CMD_FULLSCREEN, args );
      } );
    }
  }

}
