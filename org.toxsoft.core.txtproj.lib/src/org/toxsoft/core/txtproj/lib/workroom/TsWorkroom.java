package org.toxsoft.core.txtproj.lib.workroom;

import static org.toxsoft.core.txtproj.lib.workroom.ITsWorkroomConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.apprefs.*;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsWorkroom} implementation.
 *
 * @author hazard157
 */
public final class TsWorkroom
    implements ITsWorkroom {

  private final File              workroomDir;
  private final IAppPreferences   workroomPerferences;
  private final TsWorkroomStorage appStorage;

  private final IStringMapEdit<ITsWorkroomStorage> ssMap = new StringMap<>();

  /**
   * Constructor.
   *
   * @param aWrDir {@link File} - valid and initialized workroom directory
   */
  TsWorkroom( File aWrDir ) {
    workroomDir = aWrDir;
    File prefInitFile = new File( workroomDir, TS_WORKROOM_PREFS_FILE_NAME );
    AbstractAppPreferencesStorage ps = new AppPreferencesConfigIniStorage( prefInitFile, false );
    workroomPerferences = new AppPreferences( ps );
    IPrefBundle pb = workroomPerferences.getBundle( TS_WORKROOM_APP_PREFS_BUNDLE_ID );
    appStorage = new TsWorkroomStorage( TsLibUtils.EMPTY_STRING, aWrDir, pb );
  }

  /**
   * Opens the workroom of the specified flavor.
   *
   * @param aWrDir {@link File} - workroom directory
   * @param aFlavor {@link WorkroomFlavor} - expected workroom flavor
   * @return {@link ITsWorkroom} - open workroom
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsWorkroomRtException an error occurred during opening the workroom
   */
  public static ITsWorkroom openWorkroom( File aWrDir, WorkroomFlavor aFlavor ) {
    TsWorkroomOpener opener = new TsWorkroomOpener( aWrDir, aFlavor );
    return opener.openWorkroom();
  }

  // ------------------------------------------------------------------------------------
  // ITsWorkroom
  //

  @Override
  public File wsDir() {
    return workroomDir;
  }

  @Override
  public ITsWorkroomStorage getApplicationStorage() {
    return appStorage;
  }

  @Override
  public ITsWorkroomStorage getStorage( String aSubsysId ) {
    StridUtils.checkValidIdPath( aSubsysId );
    ITsWorkroomStorage storage = ssMap.findByKey( aSubsysId );
    if( storage == null ) {
      File ssDir = new File( workroomDir, aSubsysId );
      if( !ssDir.exists() ) {
        ssDir.mkdir();
      }
      IPrefBundle ssPrefsBundle = workroomPerferences.getBundle( aSubsysId );
      storage = new TsWorkroomStorage( aSubsysId, ssDir, ssPrefsBundle );
      ssMap.put( aSubsysId, storage );
    }
    return storage;
  }

}
