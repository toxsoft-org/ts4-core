package org.toxsoft.core.txtproj.lib.workroom;

import static org.toxsoft.core.txtproj.lib.workroom.ITsWorkroomConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Helpers to work with the workroom.
 *
 * @author hazard157
 */
public class WorkroomUtils {

  // TODO check that directory is not parent or child of other workroom directory

  private static final String ECLIPSE_PROJ_FILE_NAME  = ".project";             //$NON-NLS-1$
  private static final String ECLIPSE_PROJ_FILE_SIGN1 = "<?xml";                //$NON-NLS-1$
  private static final String ECLIPSE_PROJ_FILE_SIGN2 = "<projectDescription>"; //$NON-NLS-1$

  /**
   * Checks the the specified directory as the workroom.
   *
   * @param aWorkroomDirectory {@link File} - the path to the directory
   * @param aWorkroomFlavor {@link WorkroomFlavor} - expected workroom flavor and version
   * @return {@link EWorkroomDirectoryCheckStatus} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static EWorkroomDirectoryCheckStatus checkWorkroomDirectory( File aWorkroomDirectory,
      WorkroomFlavor aWorkroomFlavor ) {
    TsNullArgumentRtException.checkNull( aWorkroomDirectory );
    // checking directory in file system
    if( !aWorkroomDirectory.exists() ) {
      return EWorkroomDirectoryCheckStatus.NOT_EXISTS;
    }
    if( !aWorkroomDirectory.isDirectory() ) {
      return EWorkroomDirectoryCheckStatus.DIR_INACSSABLE;
    }
    if( !aWorkroomDirectory.canRead() || !aWorkroomDirectory.canWrite() ) {
      return EWorkroomDirectoryCheckStatus.DIR_INACSSABLE;
    }
    // empty directory?
    File[] ff = aWorkroomDirectory.listFiles( TsFileFilter.FF_ALL_HIDDEN );
    if( ff == null || ff.length == 0 ) {
      return EWorkroomDirectoryCheckStatus.EMPTY_DIR;
    }
    // check for EMPTY_ECLIPSE_PROJ
    if( ff.length == 1 ) {
      if( ff[0].getName().equals( ECLIPSE_PROJ_FILE_NAME ) ) {
        if( ff[0].length() < 10 * 1024 ) { // big file means non-empty project
          String content = readSmallTextFile( ff[0] );
          if( content.contains( ECLIPSE_PROJ_FILE_SIGN1 ) && content.contains( ECLIPSE_PROJ_FILE_SIGN2 ) ) {
            return EWorkroomDirectoryCheckStatus.EMPTY_ECLIPSE_PROJ;
          }
        }
      }
    }
    // checking a potential workroom (i.e. if there is a workspace info file)
    File wrinfFile = new File( aWorkroomDirectory, TS_WORKROOM_FLAVOR_FILE_NAME );
    if( wrinfFile.exists() ) {
      try {
        // check the flavor
        WorkroomFlavor wrFlavor = WorkroomFlavor.KEEPER.read( wrinfFile );
        if( !wrFlavor.flavorId().equalsIgnoreCase( aWorkroomFlavor.flavorId() ) ) {
          return EWorkroomDirectoryCheckStatus.BAD_FLAVOUR;
        }
        int formatVersion = wrFlavor.fomatVersion();
        if( formatVersion < aWorkroomFlavor.fomatVersion() ) {
          return EWorkroomDirectoryCheckStatus.OLDER_VERSION;
        }
        if( formatVersion > aWorkroomFlavor.fomatVersion() ) {
          return EWorkroomDirectoryCheckStatus.NEWER_VERSION;
        }
        // check workroom is already opened
        File lockFile = new File( aWorkroomDirectory, TS_WORKROOM_LOCK_FILE_NAME );
        if( lockFile.exists() ) {
          return EWorkroomDirectoryCheckStatus.IS_BUSY;
        }
        return EWorkroomDirectoryCheckStatus.OK;
      }
      catch( Exception ex ) {
        // error reading workroom-specific files - assume corrupted workroom
        LoggerUtils.errorLogger().error( ex );
        return EWorkroomDirectoryCheckStatus.CORRUPTED;
      }
    }
    return EWorkroomDirectoryCheckStatus.NON_WR;
  }

  private static final String readSmallTextFile( File aFile ) {
    String s = TsLibUtils.EMPTY_STRING;
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      s = sr.readUntilChar( IStrioHardConstants.CHAR_EOF );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    return s;
  }

  /**
   * No subclasses.
   */
  private WorkroomUtils() {
    // nop
  }

}
