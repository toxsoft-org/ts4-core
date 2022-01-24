package org.toxsoft.unit.txtproj.core.bound;

import static org.toxsoft.tslib.ITsHardConstants.*;
import static org.toxsoft.tslib.av.EAtomicType.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.unit.txtproj.core.bound.ITsResources.*;

import org.toxsoft.tslib.av.impl.DataDef;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;

/**
 * Описания параметров настройки {@link ITsProjectFileBound}.
 *
 * @author hazard157
 */
public interface ITsProjectFileBoundParams {

  /**
   * Идентификатор опции {@link #OPDEF_IS_AUTO_BACKUPS_ENABLED}.
   */
  String OPID_IS_AUTO_BACKUPS_ENABLED = TS_FULL_ID + ".tsproject.param.IsAutoBackupsEnabled"; //$NON-NLS-1$

  /**
   * Идентификатор опции {@link #OPDEF_BACKUP_FILES_SUBDIR_NAME}.
   */
  String OPID_BACKUP_FILES_SUBDIR_NAME = TS_FULL_ID + ".tsproject.param.BakupFilesSubdirName"; //$NON-NLS-1$

  /**
   * Идентификатор опции {@link #OPDEF_BACKUP_FILE_EXT}.
   */
  String OPID_BACKUP_FILE_EXT = TS_FULL_ID + ".tsproject.param.BackupFileExt"; //$NON-NLS-1$

  /**
   * Идентификатор опции {@link #OPDEF_IS_LISTENER_ERRORS_THROWN}.
   */
  String OPID_IS_LISTENER_ERRORS_THROWN = TS_FULL_ID + ".tsproject.param.IsListenerErrorsThrown"; //$NON-NLS-1$

  /**
   * Признак разрешения автоматического создания резервных копии.
   */
  IDataDef OPDEF_IS_AUTO_BACKUPS_ENABLED = DataDef.create( OPID_IS_AUTO_BACKUPS_ENABLED, BOOLEAN, //
      TSID_NAME, STR_N_IS_AUTO_BACKUPS_ENABLED, //
      TSID_DESCRIPTION, STR_D_IS_AUTO_BACKUPS_ENABLED, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Имя подкаталога, в котором сохраняются резервные копии.
   */
  IDataDef OPDEF_BACKUP_FILES_SUBDIR_NAME = DataDef.create( OPID_BACKUP_FILES_SUBDIR_NAME, STRING, //
      TSID_NAME, STR_N_BACKUP_FILES_SUBDIR_NAME, //
      TSID_DESCRIPTION, STR_D_BACKUP_FILES_SUBDIR_NAME, //
      TSID_DEFAULT_VALUE, avStr( "backups" ) //$NON-NLS-1$
  );

  /**
   * Расширение имени резервной копии файла.
   */
  IDataDef OPDEF_BACKUP_FILE_EXT = DataDef.create( OPID_BACKUP_FILE_EXT, STRING, //
      TSID_NAME, STR_N_BACKUP_FILE_EXT, //
      TSID_DESCRIPTION, STR_D_BACKUP_FILE_EXT, //
      TSID_DEFAULT_VALUE, avStr( "~" ) //$NON-NLS-1$
  );

  /**
   * Признак выбрасывания исключений, врозникших в слушателях.
   */
  IDataDef OPDEF_IS_LISTENER_ERRORS_THROWN = DataDef.create( OPID_IS_LISTENER_ERRORS_THROWN, BOOLEAN, //
      TSID_NAME, STR_N_IS_LISTENER_ERRORS_THROWN, //
      TSID_DESCRIPTION, STR_D_IS_LISTENER_ERRORS_THROWN, //
      TSID_DEFAULT_VALUE, AV_FALSE //
  );

  /**
   * Все вышеприведенные параметры в виде списка.
   */
  IStridablesList<IDataDef> ALL_PARAMS = new StridablesList<>( //
      OPDEF_IS_AUTO_BACKUPS_ENABLED, //
      OPDEF_BACKUP_FILES_SUBDIR_NAME, //
      OPDEF_BACKUP_FILE_EXT, //
      OPDEF_IS_LISTENER_ERRORS_THROWN //
  );

}
