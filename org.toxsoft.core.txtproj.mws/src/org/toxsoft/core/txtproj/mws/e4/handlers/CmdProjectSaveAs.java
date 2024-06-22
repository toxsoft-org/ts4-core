package org.toxsoft.core.txtproj.mws.e4.handlers;

import java.io.File;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.tsgui.rcp.utils.TsRcpDialogUtils;
import org.toxsoft.core.txtproj.lib.bound.ITsProjectFileBound;

import jakarta.inject.Inject;

/**
 * Command "Save project as other file".
 *
 * @author hazard157
 */
public class CmdProjectSaveAs {

  @Inject
  ITsProjectFileBound projectHolder;

  @Execute
  void execute( Shell aShell, @Optional ITsE4Helper aE4Helper ) {
    File f = TsRcpDialogUtils.askFileSave( aShell );
    if( f != null ) {
      projectHolder.saveAs( f );
    }
    aE4Helper.updateHandlersCanExecuteState();
  }

}
