package org.toxsoft.core.tslib.bricks.filebound;

import static org.toxsoft.core.tslib.ITsHardConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.bricks.filebound.ITsResources.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;

/**
 * {@link IKeepedContentFileBound} parameters.
 *
 * @author hazard157
 */
public interface IKeepedContentFileBoundConstants {

  /**
   * ID of option {@link #OPDEF_IS_AUTO_BACKUPS_ENABLED}.
   */
  String OPID_IS_AUTO_BACKUPS_ENABLED = TS_ID + ".filebound.IsAutoBackupsEnabled"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_BACKUP_FILES_SUBDIR_NAME}.
   */
  String OPID_BACKUP_FILES_SUBDIR_NAME = TS_ID + ".filebound.BakupFilesSubdirName"; //$NON-NLS-1$

  /**
   * ID of option {@link #OPDEF_BACKUP_FILE_EXT}.
   */
  String OPID_BACKUP_FILE_EXT = TS_ID + ".filebound.BackupFileExt"; //$NON-NLS-1$

  /**
   * This flag determines if backup files should be created.
   */
  IDataDef OPDEF_IS_AUTO_BACKUPS_ENABLED = DataDef.create( OPID_IS_AUTO_BACKUPS_ENABLED, BOOLEAN, //
      TSID_NAME, STR_N_IS_AUTO_BACKUPS_ENABLED, //
      TSID_DESCRIPTION, STR_D_IS_AUTO_BACKUPS_ENABLED, //
      TSID_DEFAULT_VALUE, AV_TRUE //
  );

  /**
   * Name of subdirectory to place subsequent backup files.
   */
  IDataDef OPDEF_BACKUP_FILES_SUBDIR_NAME = DataDef.create( OPID_BACKUP_FILES_SUBDIR_NAME, STRING, //
      TSID_NAME, STR_N_BACKUP_FILES_SUBDIR_NAME, //
      TSID_DESCRIPTION, STR_D_BACKUP_FILES_SUBDIR_NAME, //
      TSID_DEFAULT_VALUE, avStr( "backups" ) //$NON-NLS-1$
  );

  /**
   * Backup file extention.
   */
  IDataDef OPDEF_BACKUP_FILE_EXT = DataDef.create( OPID_BACKUP_FILE_EXT, STRING, //
      TSID_NAME, STR_N_BACKUP_FILE_EXT, //
      TSID_DESCRIPTION, STR_D_BACKUP_FILE_EXT, //
      TSID_DEFAULT_VALUE, avStr( "~" ) //$NON-NLS-1$
  );

  /**
   * All paremeters in a single list.
   */
  IStridablesList<IDataDef> ALL_PARAMS = new StridablesList<>( //
      OPDEF_IS_AUTO_BACKUPS_ENABLED, //
      OPDEF_BACKUP_FILES_SUBDIR_NAME, //
      OPDEF_BACKUP_FILE_EXT //
  );

}
