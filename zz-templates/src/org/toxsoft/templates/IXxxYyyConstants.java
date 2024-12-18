package org.toxsoft.templates;

import static org.toxsoft.templates.l10n.ITsXxxYyySharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;

/**
 * Plugin common constants.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface IXxxYyyConstants {

  // ------------------------------------------------------------------------------------
  // plugin constants prefixes

  String XXX_FULL_ID = "com.acme.xxx";  //$NON-NLS-1$ general full ID prefix (IDpath)
  String XXX_ID      = "xxx";           //$NON-NLS-1$ general short ID prefix (IDname)
  String XXX_ACT_ID  = XXX_ID + ".act"; //$NON-NLS-1$ prefix of the ITsActionDef IDs
  String XXX_M5_ID   = XXX_ID + ".m5";  //$NON-NLS-1$ perfix of M5-model IDs

  // ------------------------------------------------------------------------------------
  // Icons

  String PREFIX_OF_ICON_FIELD_NAME = "ICONID_";  //$NON-NLS-1$
  String ICONID_APP_ICON           = "app-icon"; //$NON-NLS-1$ "app-icon" replaces default application windows icon

  // ------------------------------------------------------------------------------------
  // Actions

  String ACTID_DO_IT = XXX_ACT_ID + ".do_it"; //$NON-NLS-1$

  ITsActionDef ACDEF_DO_IT = TsActionDef.ofPush2( ACTID_DO_IT, //
      STR_DO_IT, STR_DO_IT_D, ICONID_APP_ICON );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IXxxYyyConstants.class, PREFIX_OF_ICON_FIELD_NAME );
  }

}
