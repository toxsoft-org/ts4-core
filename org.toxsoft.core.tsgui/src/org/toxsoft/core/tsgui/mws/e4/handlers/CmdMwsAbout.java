package org.toxsoft.core.tsgui.mws.e4.handlers;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.mws.*;

/**
 * E4 command handler: show "About program" information dialog.
 * <p>
 * Command ID: {@link IMwsCoreConstants#MWSID_CMD_ABOUT}.
 *
 * @author hazard157
 */
public class CmdMwsAbout {

  @Execute
  void execute( Shell aShell ) {

    // TODO CmdMwsAbout.execute()
    TsDialogUtils.underDevelopment( aShell );

  }

}
