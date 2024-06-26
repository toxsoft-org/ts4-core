package org.toxsoft.core.txtproj.mws.e4.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.txtproj.lib.bound.ITsProjectFileBound;
import org.toxsoft.core.txtproj.mws.e4.addons.AddonUnitTxtprojMws;

import jakarta.inject.Inject;

/**
 * Command "Create new project".
 *
 * @author hazard157
 */
public class CmdProjectNew {

  @Inject
  ITsProjectFileBound projectHolder;

  @Execute
  void execute( Shell aShell, ITsE4Helper aE4Helper ) {
    if( AddonUnitTxtprojMws.askAndSaveEdits( projectHolder, aShell ) ) {
      projectHolder.project().clear();
      projectHolder.resetFileBound();
    }
    aE4Helper.updateHandlersCanExecuteState();
  }

}
