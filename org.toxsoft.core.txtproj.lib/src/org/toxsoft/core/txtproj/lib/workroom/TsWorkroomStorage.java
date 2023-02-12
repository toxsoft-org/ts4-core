package org.toxsoft.core.txtproj.lib.workroom;

import static org.toxsoft.core.txtproj.lib.workroom.ITsWorkroomConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * {@link ITsWorkroomStorage} implementation.
 *
 * @author hazard157
 */
final class TsWorkroomStorage
    implements ITsWorkroomStorage {

  private final String            subsysId;
  private final File              rootDir;
  private final IPrefBundle       prefBundle;
  private final IKeepablesStorage ktorStorage;

  public TsWorkroomStorage( String aSubsysId, File aDir, IPrefBundle aPrefBundle ) {
    TsNullArgumentRtException.checkNulls( aPrefBundle, aPrefBundle, aPrefBundle );
    if( !aSubsysId.isEmpty() ) {
      StridUtils.checkValidIdPath( aSubsysId );
    }
    subsysId = aSubsysId;
    rootDir = aDir;
    prefBundle = aPrefBundle;
    File ktorStorageFile = new File( rootDir, TS_WORKROOM_KEEPABLE_STORAGE_FILE_NAME );
    ktorStorage = new KeepablesStorageInFile( ktorStorageFile );
  }

  // ------------------------------------------------------------------------------------
  // ITsWorkroomStorage
  //

  @Override
  public String subsysId() {
    return subsysId;
  }

  @Override
  public File rootDir() {
    return rootDir;
  }

  @Override
  public IPrefBundle prefBundle() {
    return prefBundle;
  }

  @Override
  public IKeepablesStorage ktorStorage() {
    return ktorStorage;
  }

}
