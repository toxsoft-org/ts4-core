package org.toxsoft.core.tsgui.mws.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.progargs.*;

/**
 * Addon to process {@link #CMDLINEARG_INITIAL_PERSP_ID} command line argument.
 * <p>
 *
 * @author hazard157
 */
public class AddonInitialPerspectiveChooser
    extends MwsAbstractAddon {

  /**
   * If this command line argument is specified, application will switch into the specified perspective at startup.
   */
  public static final String CMDLINEARG_INITIAL_PERSP_ID = "InitialPerspectiveId"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public AddonInitialPerspectiveChooser() {
    super( AddonInitialPerspectiveChooser.class.getSimpleName() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // nop
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // initial perspective, if specified
    ProgramArgs programArgs = aWinContext.get( ProgramArgs.class );
    String perspId = programArgs.getArgValue( CMDLINEARG_INITIAL_PERSP_ID, TsLibUtils.EMPTY_STRING );
    if( !perspId.isBlank() ) {
      ITsE4Helper e4Helper = aWinContext.get( ITsE4Helper.class );
      Display display = aWinContext.get( Display.class );
      display.asyncExec( () -> e4Helper.switchToPerspective( perspId, null ) );
    }
  }

}
