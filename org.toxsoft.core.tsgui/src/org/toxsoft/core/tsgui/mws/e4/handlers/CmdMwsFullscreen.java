package org.toxsoft.core.tsgui.mws.e4.handlers;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.e4.ui.model.application.ui.basic.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.osgi.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;

import jakarta.inject.*;

/**
 * E4 command handler: toggle or set/reset main window full-screen mode.
 * <p>
 * Command ID: {@link IMwsCoreConstants#MWSID_CMD_FULLSCREEN}.
 * <p>
 * Respects following options in {@link IMwsOsgiService#context() IMwsOsgiService.context().params()}:
 * <ul>
 * <li>{@link IMwsCoreConstants#MWSOP_USE_FULLSCREEN_CMD} - enables/disables this command;</li>
 * <li>{@link IMwsCoreConstants#MWSOP_FULLSCREEN_HIDE_MENU} - enables/disables hiding main menu in the full-screen
 * mode;</li>
 * <li>{@link IMwsCoreConstants#MWSOP_FULLSCREEN_HIDE_TRIMBAR_TOP} - enables/disables hiding top trim bar (with all tool
 * bars) in the full-screen mode;</li>
 * <li>{@link IMwsCoreConstants#MWSOP_FULLSCREEN_HIDE_TRIMBAR_BOTTOM} - enables/disables hiding bottom trim bar (with
 * all status line items) in the full-screen mode.</li>
 * </ul>
 * <p>
 * If argument {@link IMwsCoreConstants#MWSID_CMDARG_FS_ONOFF} is specified, full-screen mode is determined by the
 * argument, not current the state of the shell.
 * <p>
 * Optional command arguments with the same IDs have precedence over application options.
 * <p>
 * Command is bind to hot key F11.
 *
 * @author hazard157
 */
public class CmdMwsFullscreen {

  @Execute
  void exec( Shell aShell, ITsE4Helper aE4Helper, MTrimmedWindow aWindow, IMwsOsgiService aMwsService, //
      @Named( MWSID_CMDARG_FS_ONOFF ) @Optional String aOnOffStr, //
      @Named( MWSID_CMDARG_FS_HIDE_MENU ) @Optional String aHideMenuStr, //
      @Named( MWSID_CMDARG_FS_HIDE_TOP ) @Optional String aHideTopStr, //
      @Named( MWSID_CMDARG_FS_HIDE_BOTTOM ) @Optional String aHideBottomStr //
  ) {
    // full-screen mode is determined by either from argument or from toggled current state
    boolean fullscreenOn;
    if( aOnOffStr != null ) {
      fullscreenOn = aOnOffStr.equalsIgnoreCase( Boolean.TRUE.toString() );
    }
    else {
      fullscreenOn = !aShell.getFullScreen();
    }
    // resize shell
    aShell.setFullScreen( fullscreenOn );
    // main menu
    boolean argHideMainMenu = aHideMenuStr != null && aHideMenuStr.equalsIgnoreCase( Boolean.TRUE.toString() );
    boolean showMainMenu = !fullscreenOn || isMainMenuAlwaysShown( aMwsService ) || !argHideMainMenu;
    MMenu mainMenu = aE4Helper.findElement( aWindow, MWSID_MENU_MAIN, MMenu.class );
    mainMenu.setVisible( showMainMenu );
    mainMenu.setToBeRendered( showMainMenu );
    // top trim bar
    boolean argHideTopTrimBar = aHideTopStr != null && aHideTopStr.equalsIgnoreCase( Boolean.TRUE.toString() );
    boolean showTopTrimBar = !fullscreenOn || isTopTrimBarAlwaysShown( aMwsService ) || !argHideTopTrimBar;
    MTrimBar trimBarTop = aE4Helper.findElement( aWindow, MWSID_TRIMBAR_TOP, MTrimBar.class );
    trimBarTop.setVisible( showTopTrimBar );
    trimBarTop.setToBeRendered( showTopTrimBar );
    // bottom trim bar
    boolean argHideBottomTrimBar = aHideBottomStr != null && aHideBottomStr.equalsIgnoreCase( Boolean.TRUE.toString() );
    boolean showBottomTrimBar = !fullscreenOn || isBottomTrimBarAlwaysShown( aMwsService ) || !argHideBottomTrimBar;
    MTrimBar trimBarBottom = aE4Helper.findElement( aWindow, MWSID_TRIMBAR_BOTTOM, MTrimBar.class );
    trimBarBottom.setVisible( showBottomTrimBar );
    trimBarBottom.setToBeRendered( showBottomTrimBar );
  }

  @CanExecute
  boolean canExec( IMwsOsgiService aMwsService ) {
    return MWSOP_USE_FULLSCREEN_CMD.getValue( aMwsService.context().params() ).asBool();
  }

  private static boolean isMainMenuAlwaysShown( IMwsOsgiService aMwsService ) {
    return !MWSOP_FULLSCREEN_HIDE_MENU.getValue( aMwsService.context().params() ).asBool();
  }

  private static boolean isTopTrimBarAlwaysShown( IMwsOsgiService aMwsService ) {
    return !MWSOP_FULLSCREEN_HIDE_TRIMBAR_TOP.getValue( aMwsService.context().params() ).asBool();
  }

  private static boolean isBottomTrimBarAlwaysShown( IMwsOsgiService aMwsService ) {
    return !MWSOP_FULLSCREEN_HIDE_TRIMBAR_BOTTOM.getValue( aMwsService.context().params() ).asBool();
  }

}
