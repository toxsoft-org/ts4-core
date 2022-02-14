package org.toxsoft.core.txtproj.mws.e4.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.txtproj.lib.bound.ITsProjectFileBound;

/**
 * Command "Save project to the current file".
 *
 * @author hazard157
 */
public class CmdProjectSave {

  @Inject
  ITsProjectFileBound projectHolder;

  @Execute
  void execute( ITsE4Helper aE4Helper ) {
    projectHolder.save();
    aE4Helper.updateHandlersCanExecuteState();
  }

  @CanExecute
  boolean hasUnsavedData() {
    return projectHolder.isAltered() && projectHolder.hasFileBound();
  }

}
