package org.toxsoft.tsgui.mws.e4.handlers;

import org.eclipse.e4.core.di.annotations.Execute;
import org.toxsoft.tsgui.mws.services.e4helper.ITsE4Helper;

/**
 * Команда "Выход".
 *
 * @author hazard157
 */
public class CmdMwsQuit {

  @Execute
  void execute( ITsE4Helper aHelper ) {
    aHelper.quitApplication();
  }

}
