package org.toxsoft.core.txtproj.lib.workroom;

import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * The workroom constants.
 *
 * @author hazard157
 */
public interface ITsWorkroomConstants {

  /**
   * The name of the file identifying directory as a workroom.
   */
  String TS_WORKROOM_FLAVOR_FILE_NAME = "ts-workroom.flavor"; //$NON-NLS-1$

  /**
   * The presence of this file indicates that the workroom is already open.
   */
  String TS_WORKROOM_LOCK_FILE_NAME = "ts-workroom.lock"; //$NON-NLS-1$

  /**
   * Workroom preferences storage file name.
   * <p>
   * This file contains {@link IAppPreferences} via {@link AppPreferencesConfigIniStorage} backend.
   */
  String TS_WORKROOM_PREFS_FILE_NAME = "ts-workroom.prefs"; //$NON-NLS-1$

  /**
   * The {@link IPrefBundle} ID in {@link #TS_WORKROOM_PREFS_FILE_NAME} with application-wide preferences.
   */
  String TS_WORKROOM_APP_PREFS_BUNDLE_ID = "ts.workroom.application"; //$NON-NLS-1$

  /**
   * Extension used by {@link ITsWorkroom} to store {@link IKeepablesStorage} format files.
   * <p>
   * Also is recommended extension for application specific files of the same storage format.
   */
  String TS_WORKROOM_KEEPABLE_STORAGE_FILES_EXT = "ktor"; //$NON-NLS-1$

  /**
   * {@link IKeepablesStorage} file name in workroom and subsystem directories.
   * <p>
   * Storage implements {@link ITsWorkroomStorage#ktorStorage()} via {@link KeepablesStorageInFile} backend.
   */
  String TS_WORKROOM_KEEPABLE_STORAGE_FILE_NAME = "data-storage." + TS_WORKROOM_KEEPABLE_STORAGE_FILES_EXT; //$NON-NLS-1$

}
