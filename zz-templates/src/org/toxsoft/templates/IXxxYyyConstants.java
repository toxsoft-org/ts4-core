package org.toxsoft.templates;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.templates.l10n.ITsXxxYyySharedResources.*;

import org.eclipse.e4.core.contexts.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

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

  // ------------------------------------------------------------------------------------
  // Application preferences

  String PREFBUNDLEID_XXX = XXX_FULL_ID; // recommendation: use dedicated perspective ID if any

  String APREFID_FOO_1 = "Foo1"; //$NON-NLS-1$
  String APREFID_FOO_2 = "Foo2"; //$NON-NLS-1$

  IDataDef APPREF_FOO_1 = DataDef.create( APREFID_FOO_1, BOOLEAN, ///
      TSID_NAME, STR_APPREF_FOO_1, ///
      TSID_DESCRIPTION, STR_APPREF_FOO_1_D, ///
      TSID_DEFAULT_VALUE, AV_TRUE ///
  );

  IDataDef APPREF_FOO_2 = DataDef.create( APREFID_FOO_2, BOOLEAN, ///
      TSID_NAME, STR_APPREF_FOO_2, ///
      TSID_DESCRIPTION, STR_APPREF_FOO_2_D, ///
      TSID_DEFAULT_VALUE, AV_TRUE ///
  );

  IStridablesList<IDataDef> SHOWN_APPREFS_LIST = new StridablesList<>( ///
      // APPREF_FOO_1, /// option Foo1 will be present in perferences but NOT shown in the user dialog
      APPREF_FOO_2 ///
  );

  /**
   * Constants registration.
   *
   * @param aWinContext {@link IEclipseContext} - windows level context
   */
  static void init( IEclipseContext aWinContext ) {
    // register plug-in built-in icons
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IXxxYyyConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    // register application preference option available for user to edit via preferences GUI dialog
    IAppPreferences aprefs = aWinContext.get( IAppPreferences.class );
    IPrefBundle pb = aprefs.defineBundle( PREFBUNDLEID_XXX, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_PREF_BINDLE_XXX, //
        TSID_DESCRIPTION, STR_PREF_BINDLE_XXX_D, //
        TSID_ICON_ID, ICONID_APP_ICON //
    ) );
    for( IDataDef dd : SHOWN_APPREFS_LIST ) {
      pb.defineOption( dd );
    }
  }

}
