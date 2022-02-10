package org.toxsoft.core.unit.txtproj.mws.e4.handlers;

import java.io.File;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.tsgui.rcp.utils.TsRcpDialogUtils;
import org.toxsoft.core.unit.txtproj.lib.bound.ITsProjectFileBound;
import org.toxsoft.core.unit.txtproj.mws.e4.addons.AddonUnitTxtprojMws;

/**
 * Command "Open an existing project file".
 *
 * @author hazard157
 */
public class CmdProjectOpen {

  @Inject
  ITsProjectFileBound projectHolder;

  @Execute
  void execute( Shell aShell, ITsE4Helper aE4Helper ) {
    if( AddonUnitTxtprojMws.askAndSaveEdits( projectHolder, aShell ) ) {
      File f = TsRcpDialogUtils.askFileOpen( aShell );
      if( f != null ) {
        projectHolder.open( f );
      }
    }
    aE4Helper.updateHandlersCanExecuteState();
  }

}
