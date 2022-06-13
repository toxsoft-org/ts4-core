package org.toxsoft.core.tsgui.mws.e4.handlers;

import org.toxsoft.core.tsgui.mws.services.e4helper.*;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ITsResources {

  String MSG_LOG_ERR_NO_E4_HELPER = "No context reference to " + ITsE4Helper.class.getSimpleName(); //$NON-NLS-1$

  /**
   * {@link CmdMwsAbout}
   */
  String DLG_C_ABOUT = Messages.getString( "DLG_C_ABOUT" ); //$NON-NLS-1$ ;
  String DLG_T_ABOUT = Messages.getString( "DLG_T_ABOUT" ); //$NON-NLS-1$ ;

  /**
   * {@link CmdMwsEditAppPrefs}
   */
  String MSG_WARN_NO_KNOWN_PREF = Messages.getString( "MSG_WARN_NO_KNOWN_PREF" ); //$NON-NLS-1$
  String DLG_C_APP_PREFS        = Messages.getString( "DLG_C_APP_PREFS" );        //$NON-NLS-1$
  String DLG_T_APP_PREFS        = Messages.getString( "DLG_T_APP_PREFS" );        //$NON-NLS-1$

}
