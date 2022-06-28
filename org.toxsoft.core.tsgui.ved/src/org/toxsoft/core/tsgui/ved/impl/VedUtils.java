package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tsgui.ved.impl.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.incub.lpd.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Visual EDitor framework helper methods and entry point.
 *
 * @author hazard157
 */
public class VedUtils {

  /**
   * Creates and return new {@link IVedEnvironment}.
   *
   * @return {@link IVedEnvironment} - created instance
   */
  public static IVedEnvironment createEnvironment() {
    return new VedEnvironment();
  }

  /**
   * Saves VED data to the file.
   *
   * @param aVedEnv {@link IVedEnvironment} - environment, containing the data to be saved
   * @param aFile {@link File} - the file to write to
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error accessing and writing to the file
   */
  public static void saveToFile( IVedEnvironment aVedEnv, File aFile ) {
    TsNullArgumentRtException.checkNull( aVedEnv );
    TsFileUtils.checkFileAppendable( aFile );
    IVedDataModel dm = aVedEnv.dataModel();
    ILpdContainer lpd = new LpdContainer();
    lpd.panelCfg().setAll( dm.canvasConfig() );
    for( IVedComponent c : dm.comps() ) {
      IdChain namespace = new IdChain( c.libraryId() );
      ILpdComponentInfo cinf = new LpdComponentInfo( namespace, c.componentKindId(), c.props(), c.extdata() );
      lpd.componentConfigs().add( cinf );
    }
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( aFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      lpd.write( sw );
    }
  }

  /**
   * loads VED data from the file.
   *
   * @param aVedEnv {@link IVedEnvironment} - environment, containing data to be updated from the file
   * @param aFile {@link File} - the file to read from
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error accessing and reading from the file
   * @throws StrioRtException file format error
   */
  public static void loadFromFile( IVedEnvironment aVedEnv, File aFile ) {
    TsNullArgumentRtException.checkNull( aVedEnv );
    TsFileUtils.checkFileReadable( aFile );
    ILpdContainer lpd = new LpdContainer();
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      lpd.read( sr );
    }
    // update model data
    IVedDataModel dm = aVedEnv.dataModel();
    dm.genericChangeEventer().pauseFiring();
    dm.comps().pauseFiring();
    dm.canvasConfig().pauseFiring();
    try {
      // canvas config
      dm.canvasConfig().setAll( lpd.panelCfg() );
      // create and add the components
      for( ILpdComponentInfo cinf : lpd.componentConfigs() ) {
        String libId = cinf.namespace().first();
        IVedComponentProvider p = aVedEnv.libraryManager().findProvider( libId, cinf.componentKindId() );
        if( p != null ) {
          IVedComponent c = p.createComponent( aVedEnv, cinf.propValues(), cinf.extdata() );
          dm.comps().add( c );
        }
        else {
          LoggerUtils.errorLogger().warning( FMT_LOG_WARN_NO_COMP_PROVIDER, libId, cinf.componentKindId() );
        }
      }
    }
    finally {
      dm.canvasConfig().resumeFiring( true );
      dm.comps().resumeFiring( true );
      dm.genericChangeEventer().resumeFiring( true );
    }
  }

  /**
   * No subclassig.
   */
  private VedUtils() {
    // nop
  }

}
