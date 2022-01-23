package org.toxsoft.unit.txtproj.mws;

import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.unit.txtproj.core.IUnitTxtprojCoreConstants.*;
import static org.toxsoft.unit.txtproj.mws.ITsResources.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.icons.ITsIconManager;
import org.toxsoft.tsgui.graphics.image.EThumbSize;
import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.impl.ElemArrayList;

/**
 * Константы GUI приложения.
 *
 * @author goga
 */
@SuppressWarnings( "javadoc" )
public interface IUnitTxtprojMwsConstants {

  // ------------------------------------------------------------------------------------
  // e4

  String TOOLBARID_TXTPROJ  = "org.toxsoft.unit.txtproj.toolbar.main";  //$NON-NLS-1$
  String MENUID_TXTPROJ     = "org.toxsoft.unit.txtproj.menu.main";     //$NON-NLS-1$
  String CMDCATEGID_TXTPROJ = "org.toxsoft.unit.txtproj.cmdcateg.main"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Icons
  String PREFIX_OF_ICON_FIELD_NAME = "ICON_"; //$NON-NLS-1$
  // String ICON_WELCOME_01 = "welcome-01"; //$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Preferences

  String PREF_BUNDLE_ID_WELCOME = TXTPROJ_ID;

  String APPRMID_IS_BACKUPS_CREATED = "IsBackupsCreated"; //$NON-NLS-1$
  String APPRMID_BACKUP_SUBDIR_NAME = "BackupSubdirName"; //$NON-NLS-1$

  String APPRMID_THUMB_SIZE_FILMS     = "FilmsThumbSize";    //$NON-NLS-1$
  String APPRMID_THUMB_SIZE_PLEPS     = "PlepsThumbSize";    //$NON-NLS-1$
  String APPRMID_ICON_SIZE_GAMES      = "GamesThumbSize";    //$NON-NLS-1$
  String APPRMID_PLAY_FULL_SCREEN     = "PlayFullScreen";    //$NON-NLS-1$
  String APPRMID_IS_STARTUP_GIF_SHOWN = "IsStartupGifShown"; //$NON-NLS-1$

  IDataDef APPRM_THUMB_SIZE_EPISODES = DataDef.create( APPRMID_IS_BACKUPS_CREATED, VALOBJ, //
      TSID_NAME, STR_N_THUMB_SIZE_EPISODES, //
      TSID_DESCRIPTION, STR_D_THUMB_SIZE_EPISODES, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 ) //
  );

  IDataDef APPRM_THUMB_SIZE_FILMS = DataDef.create( APPRMID_THUMB_SIZE_FILMS, VALOBJ, //
      TSID_NAME, STR_N_THUMB_SIZE_FILMS, //
      TSID_DESCRIPTION, STR_D_THUMB_SIZE_FILMS, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 ) //
  );

  IDataDef APPRM_THUMB_SIZE_PLEPS = DataDef.create( APPRMID_THUMB_SIZE_PLEPS, VALOBJ, //
      TSID_NAME, STR_N_THUMB_SIZE_PLEPS, //
      TSID_DESCRIPTION, STR_D_THUMB_SIZE_PLEPS, //
      TSID_KEEPER_ID, EThumbSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EThumbSize.SZ128 ) //
  );

  IDataDef APPRM_ICON_SIZE_GAMES = DataDef.create( APPRMID_ICON_SIZE_GAMES, VALOBJ, //
      TSID_NAME, STR_N_ICON_SIZE_GAMES, //
      TSID_DESCRIPTION, STR_D_ICON_SIZE_GAMES, //
      TSID_KEEPER_ID, EIconSize.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( EIconSize.IS_64X64 ) //
  );

  IDataDef APPRM_PLAY_FULL_SCREEN = DataDef.create( APPRMID_PLAY_FULL_SCREEN, BOOLEAN, //
      TSID_NAME, STR_N_PLAY_FULL_SCREEN, //
      TSID_DESCRIPTION, STR_D_PLAY_FULL_SCREEN, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  IDataDef APPRM_IS_STARTUP_GIF_SHOWN = DataDef.create( APPRMID_IS_STARTUP_GIF_SHOWN, //
      BOOLEAN, //
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_NAME, STR_N_IS_STARTUP_GIF_SHOWN, //
      TSID_DESCRIPTION, STR_D_IS_STARTUP_GIF_SHOWN //
  );

  IList<IDataDef> ALL_APPRMS = new ElemArrayList<>( //
      APPRM_THUMB_SIZE_EPISODES, //
      APPRM_THUMB_SIZE_FILMS, //
      APPRM_THUMB_SIZE_PLEPS, //
      APPRM_ICON_SIZE_GAMES, //
      APPRM_PLAY_FULL_SCREEN, //
      APPRM_IS_STARTUP_GIF_SHOWN //
  );

  static void init( IEclipseContext aWinContext ) {
    // icons
    ITsIconManager iconManager = aWinContext.get( ITsIconManager.class );
    iconManager.registerStdIconByIds( Activator.PLUGIN_ID, IUnitTxtprojMwsConstants.class, PREFIX_OF_ICON_FIELD_NAME );
    // FIXME app params
  }

}
