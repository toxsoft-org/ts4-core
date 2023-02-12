package org.toxsoft.core.txtproj.lib.workroom;

import static org.toxsoft.core.txtproj.lib.workroom.ITsResources.*;
import static org.toxsoft.core.txtproj.lib.workroom.ITsWorkroomConstants.*;

import java.io.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Internal implementation of the {@link ITsWorkroom} opener.
 *
 * @author hazard157
 */
public class TsWorkroomOpener {

  private final File           workroomDir;
  private final WorkroomFlavor flavor;

  /**
   * Constructor.
   *
   * @param aWrDir {@link File} - workroom directory
   * @param aFlavor {@link WorkroomFlavor} - expected workroom flavor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsWorkroomOpener( File aWrDir, WorkroomFlavor aFlavor ) {
    TsNullArgumentRtException.checkNulls( aWrDir, aFlavor );
    workroomDir = aWrDir;
    flavor = aFlavor;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private TsWorkroom initializeEmptyWorkroom( File aWrDir ) {
    File wrinfFile = new File( aWrDir, TS_WORKROOM_FLAVOR_FILE_NAME );
    WorkroomFlavor.KEEPER.write( wrinfFile, flavor );
    return new TsWorkroom( aWrDir );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  @SuppressWarnings( "boxing" )
  ITsWorkroom openWorkroom() {
    // check and perform appropriate action
    EWorkroomDirectoryCheckStatus checkStatus = WorkroomUtils.checkWorkroomDirectory( workroomDir, flavor );
    switch( checkStatus ) {
      case OK: {
        return new TsWorkroom( workroomDir );
      }
      case EMPTY_DIR: {
        return initializeEmptyWorkroom( workroomDir );
      }
      case EMPTY_ECLIPSE_PROJ: {
        return initializeEmptyWorkroom( workroomDir );
      }
      case NON_WR: {
        throw new TsWorkroomRtException( workroomDir, MSG_ERR_WR_NON_WS );
      }
      case CORRUPTED: {
        throw new TsWorkroomRtException( workroomDir, MSG_ERR_WR_CORRUPTED );
      }
      case IS_BUSY: {
        throw new TsWorkroomRtException( workroomDir, MSG_ERR_WR_IS_BUSY );
      }
      case NOT_EXISTS: {
        throw new TsWorkroomRtException( workroomDir, MSG_ERR_WR_NOT_EXISTS );
      }
      case BAD_FLAVOUR: {
        throw new TsWorkroomRtException( workroomDir, FMT_ERR_WR_BAD_FLAVOUR, flavor.flavorId() );
      }
      case OLDER_VERSION: {
        throw new TsWorkroomRtException( workroomDir, FMT_ERR_WR_OLDER_VERSION, flavor.fomatVersion() );
      }
      case NEWER_VERSION: {
        throw new TsWorkroomRtException( workroomDir, FMT_ERR_WR_NEWER_VERSION, flavor.fomatVersion() );
      }
      case DIR_INACSSABLE: {
        throw new TsWorkroomRtException( workroomDir, MSG_ERR_WR_DIR_INACSSABLE );
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

}
