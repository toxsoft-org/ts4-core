package org.toxsoft.core.txtproj.lib.workroom;

import java.io.*;

import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * The storage in workroom provided for the subsystem.
 *
 * @author hazard157
 */
public sealed interface ITsWorkroomStorage permits TsWorkroomStorage {

  /**
   * Returns the ID of the subsystem.
   * <p>
   * For application-wide storage returns an empty string.
   *
   * @return String - the ID (an IDpath) of the subsystem or an empty string
   */
  String subsysId();

  /**
   * Returns the root directory of the subsystem-specific files storage.
   *
   * @return {@link File} - an existing subsystem files directory
   */
  File rootDir();

  /**
   * Returns the preferences storage for subsystem settings.
   *
   * @return {@link IPrefBundle} - preferences storage
   */
  IPrefBundle prefBundle();

  /**
   * Returns the storage of the any subsystem data.
   *
   * @return {@link IKeepablesStorage} - arbitrary data storage
   */
  IKeepablesStorage ktorStorage();

}
