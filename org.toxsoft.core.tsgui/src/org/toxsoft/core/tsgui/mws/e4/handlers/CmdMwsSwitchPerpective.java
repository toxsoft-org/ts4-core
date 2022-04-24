package org.toxsoft.core.tsgui.mws.e4.handlers;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.core.tsgui.mws.e4.handlers.ITsResources.*;

import java.util.*;

import javax.inject.*;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.commands.*;
import org.eclipse.e4.ui.model.application.ui.menu.*;
import org.toxsoft.core.tsgui.mws.*;
import org.toxsoft.core.tsgui.mws.services.e4helper.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * E4 command handler: switch to specified perspective.
 * <p>
 * Command ID: {@link IMwsCoreConstants#MWSID_CMD_SWITCH_PERSP}.<br>
 * Arguments (E4 command parameters):<br>
 * {@link IMwsCoreConstants#MWSID_CMDARG_SP_PERSP_ID} - (mandatory arg) ID of the perspective to switch to
 *
 * @author hazard157
 */
public class CmdMwsSwitchPerpective {

  @Execute
  void execute( @Named( MWSID_CMDARG_SP_PERSP_ID ) @Optional String aPerspId, @Optional ITsE4Helper aHelper ) {
    if( aHelper != null ) {
      aHelper.switchToPerspective( aPerspId, null );
      aHelper.updateHandlersCanExecuteState();
    }
    else {
      LoggerUtils.errorLogger().error( MSG_LOG_ERR_NO_E4_HELPER );
    }
  }

  // Method always returns true - motivation is to update RADIO group of perspective buttons in menu and toolbar
  @CanExecute
  boolean canExec( MHandledItem aItem, ITsE4Helper aHelper ) {
    List<MParameter> params = aItem.getParameters();
    boolean isCurrPersp = false;
    String currPespId = aHelper.currentPerspId();
    for( MParameter p : params ) {
      if( p.getName().equals( MWSID_CMDARG_SP_PERSP_ID ) ) {
        isCurrPersp = p.getValue().equals( currPespId );
        break;
      }
    }
    if( aItem.getType() == ItemType.CHECK || aItem.getType() == ItemType.RADIO ) {
      aItem.setSelected( isCurrPersp );
    }
    return true;
  }

}
