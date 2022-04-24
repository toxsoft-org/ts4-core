package org.toxsoft.core.tsgui.mws.e4.handlers;

import org.eclipse.e4.core.di.annotations.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;

/**
 * E4 command handler: quit an application.
 * <p>
 * Command ID: {@link IMwsCoreConstants#MWSID_CMD_SWITCH_PERSP}.
 *
 * @author hazard157
 */
public class CmdMwsQuit {

  @Execute
  void execute( ITsE4Helper aHelper ) {
    aHelper.quitApplication();
  }

}
