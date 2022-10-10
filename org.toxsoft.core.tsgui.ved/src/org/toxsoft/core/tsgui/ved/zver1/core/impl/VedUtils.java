package org.toxsoft.core.tsgui.ved.zver1.core.impl;

import static org.toxsoft.core.tsgui.ved.zver1.core.impl.ITsResources.*;

import java.io.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.zver1.core.*;
import org.toxsoft.core.tsgui.ved.zver1.core.library.*;
import org.toxsoft.core.tsgui.ved.zver1.incub.lpd.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
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
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IVedEnvironment} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IVedEnvironment createEnvironment( ITsGuiContext aContext ) {
    return new VedEnvironment( aContext );
  }

  /**
   * Saves VED data to the file.
   *
   * @param aVedEnv {@link IVedEnvironment} - environment, containing the data to be saved
   * @param aSw {@link IStrioWriter} - output stream
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void saveToStream( IVedEnvironment aVedEnv, IStrioWriter aSw ) {
    TsNullArgumentRtException.checkNulls( aVedEnv, aSw );
    IVedDataModel dm = aVedEnv.dataModel();
    ILpdContainer lpd = new LpdContainer();
    lpd.panelCfg().setAll( dm.canvasConfig() );
    for( IVedComponent c : dm.listComponents() ) {
      IdChain namespace = new IdChain( c.provider().libraryId() );
      ILpdComponentInfo cinf = new LpdComponentInfo( namespace, c.provider().id(), c.id(), c.props(), c.extdata() );
      lpd.componentConfigs().add( cinf );
    }
    lpd.write( aSw );
  }

  /**
   * loads VED data from the stream.
   *
   * @param aVedEnv {@link IVedEnvironment} - environment, containing data to be updated from the file
   * @param aSr {@link IStrioReader} - input stream
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws StrioRtException format error
   */
  public static void loadFromStream( IVedEnvironment aVedEnv, IStrioReader aSr ) {
    TsNullArgumentRtException.checkNulls( aVedEnv, aSr );
    ILpdContainer lpd = new LpdContainer();
    lpd.read( aSr );
    // update model data
    IVedDataModel dm = aVedEnv.dataModel();
    dm.genericChangeEventer().pauseFiring();
    dm.listComponents().pauseFiring();
    dm.canvasConfig().pauseFiring();
    dm.clear();
    try {
      // canvas config
      dm.canvasConfig().setAll( lpd.panelCfg() );
      // create and add the components
      for( ILpdComponentInfo cinf : lpd.componentConfigs() ) {
        String libId = cinf.namespace().first();
        IVedComponentProvider p = aVedEnv.libraryManager().findProvider( libId, cinf.componentKindId() );
        if( p != null ) {
          IVedComponent c = p.createComponent( cinf.componentId(), aVedEnv, cinf.propValues(), cinf.extdata() );
          dm.addComponent( c );
        }
        else {
          LoggerUtils.errorLogger().warning( FMT_LOG_WARN_NO_COMP_PROVIDER, libId, cinf.componentKindId() );
        }
      }
    }
    finally {
      dm.canvasConfig().resumeFiring( true );
      dm.listComponents().resumeFiring( true );
      dm.genericChangeEventer().resumeFiring( true );
    }
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
    try( ICharOutputStreamCloseable chOut = new CharOutputStreamFile( aFile ) ) {
      IStrioWriter sw = new StrioWriter( chOut );
      saveToStream( aVedEnv, sw );
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
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader sr = new StrioReader( chIn );
      loadFromStream( aVedEnv, sr );
    }
  }

  /**
   * No subclassig.
   */
  private VedUtils() {
    // nop
  }

}
