package org.toxsoft.core.tsgui.mws.e4.addons;

import org.eclipse.e4.core.contexts.*;
import org.eclipse.e4.ui.model.application.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.workbench.modeling.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.bases.*;
import org.toxsoft.core.tslib.utils.progargs.*;

/**
 * Addon to process {@link #CMDLINEARG_MAIN_WINDOW_DEBUG_SIZE} command line argument.
 *
 * @author hazard157
 */
public class AddonMainWindowDebugSize
    extends MwsAbstractAddon {

  /**
   * If this command line argument is specified, the main window will have small size for debugging convenience.
   */
  public static final String CMDLINEARG_MAIN_WINDOW_DEBUG_SIZE = "MainWindowDebugSize"; //$NON-NLS-1$

  /**
   * Constructor.
   */
  public AddonMainWindowDebugSize() {
    super( AddonMainWindowDebugSize.class.getSimpleName() );
  }

  @Override
  protected void initApp( IEclipseContext aAppContext ) {
    // window and application icon
    MApplication app = aAppContext.get( MApplication.class );
    EModelService modelService = aAppContext.get( EModelService.class );
    MTrimmedWindow mainWindow = (MTrimmedWindow)modelService.find( IMwsCoreConstants.MWSID_WINDOW_MAIN, app );
    ProgramArgs programArgs = aAppContext.get( ProgramArgs.class );
    if( programArgs.hasArg( CMDLINEARG_MAIN_WINDOW_DEBUG_SIZE ) ) { // initial size of the window SMALL
      Display display = aAppContext.get( Display.class );
      Rectangle dBounds = display.getBounds();
      int dx = dBounds.width / 8;
      int dy = dBounds.height / 8;
      mainWindow.setX( 4 * dx );
      mainWindow.setY( 2 * dy );
      mainWindow.setWidth( 3 * dx );
      mainWindow.setHeight( 5 * dy );
    }
    else { // initial size of the window BIG
      Display display = aAppContext.get( Display.class );
      Rectangle dBounds = display.getBounds();
      mainWindow.setX( dBounds.x + 8 );
      mainWindow.setY( 0 );
      mainWindow.setWidth( dBounds.width - 4 * 8 );
      mainWindow.setHeight( dBounds.height );
    }
  }

  @Override
  protected void initWin( IEclipseContext aWinContext ) {
    // nop
  }

}
