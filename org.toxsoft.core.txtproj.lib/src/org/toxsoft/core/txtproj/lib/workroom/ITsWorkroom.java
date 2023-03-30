package org.toxsoft.core.txtproj.lib.workroom;

import java.io.*;

import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The workroom is the root directory of the directory-base development tool.
 * <p>
 * It is assumed that the workroom provides data and file storage services to subsystems that have unique identifiers.
 *
 * @author hazard157
 */
public sealed interface ITsWorkroom permits TsWorkroom {

  /**
   * Returns the workroom directory.
   *
   * @return {@link File} - the workroom directory
   */
  File wrDir();

  /**
   * Returns the storage for application-wide data.
   * <p>
   * The storage has an empty string as the {@link ITsWorkroomStorage#subsysId()}.
   *
   * @return {@link ITsWorkroomStorage} - application-wide data storage
   */
  ITsWorkroomStorage getApplicationStorage();

  /**
   * Returns the storage for the specified subsystem.
   *
   * @param aSubsysId String - the ID (an IDpath) of the subsystem
   * @return {@link IPrefBundle} - preferences storage
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  ITsWorkroomStorage getStorage( String aSubsysId );

}
