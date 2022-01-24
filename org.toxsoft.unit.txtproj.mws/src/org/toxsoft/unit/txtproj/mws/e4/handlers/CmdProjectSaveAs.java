package org.toxsoft.unit.txtproj.mws.e4.handlers;

import java.io.File;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.tsgui.rcp.utils.TsRcpDialogUtils;
import org.toxsoft.unit.txtproj.core.bound.ITsProjectFileBound;

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
