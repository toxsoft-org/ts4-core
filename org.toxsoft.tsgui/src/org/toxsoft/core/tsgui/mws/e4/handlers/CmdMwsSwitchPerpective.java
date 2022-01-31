package org.toxsoft.core.tsgui.mws.e4.handlers;

import static org.toxsoft.core.tsgui.mws.IMwsCoreConstants.*;
import static org.toxsoft.core.tsgui.mws.e4.handlers.ITsResources.*;

import java.util.List;

import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.*;
import org.eclipse.e4.ui.model.application.commands.MParameter;
import org.eclipse.e4.ui.model.application.ui.menu.ItemType;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledItem;
import org.toxsoft.core.tsgui.mws.IMwsCoreConstants;
import org.toxsoft.core.tsgui.mws.services.e4helper.ITsE4Helper;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;

/**
 * Команда "Переключить в перспективу %s": String aPrespId.
 * <p>
 * Идентификатор команды {@link IMwsCoreConstants#MWSID_CMD_SWITCH_PERSP}.
 *
 * @author hazard157
 */
public class CmdMwsSwitchPerpective {

  @Execute
  void execute( @Named( MWSID_CMDARG_PERSP_ID ) @Optional String aPerspId, @Optional ITsE4Helper aHelper ) {
    if( aHelper != null ) {
      aHelper.switchToPerspective( aPerspId, null );
      aHelper.updateHandlersCanExecuteState();
    }
    else {
      LoggerUtils.errorLogger().error( MSG_LOG_ERR_NO_E4_HELPER );
    }
  }

  // Метод всегда возвращает true TODO если сделать запрет перспектив, то метод должен возвращаеть false
  // В этом методе выставляются состояния RADIO пунктов меню и кнопок тулбара выбора перспектив
  @CanExecute
  boolean canExec( MHandledItem aItem, ITsE4Helper aHelper ) {
    List<MParameter> params = aItem.getParameters();
    boolean isCurrPersp = false;
    String currPespId = aHelper.currentPerspId();
    for( MParameter p : params ) {
      if( p.getName().equals( MWSID_CMDARG_PERSP_ID ) ) {
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
