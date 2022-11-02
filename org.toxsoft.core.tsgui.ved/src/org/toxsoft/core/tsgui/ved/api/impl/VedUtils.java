package org.toxsoft.core.tsgui.ved.api.impl;

import java.io.*;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Visual EDitor framework helper methods and entry point.
 *
 * @author hazard157
 */
public class VedUtils {

  /**
   * Creates and return new {@link IVedFramework}.
   *
   * @return {@link IVedFramework} - created instance
   */
  public static IVedFramework createFramework() {
    return new VedFramework();
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
    IVedDocumentData dd = aVedEnv.getDocumentData();
    VedDocumentData.KEEPER.write( aSw, dd );
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
    IVedDocumentData dd = VedDocumentData.KEEPER.read( aSr );
    aVedEnv.setDocumentData( dd );
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
    IVedDocumentData dd = aVedEnv.getDocumentData();
    VedDocumentData.KEEPER.write( aFile, dd );
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
    IVedDocumentData dd = VedDocumentData.KEEPER.read( aFile );
    aVedEnv.setDocumentData( dd );
  }

  /**
   * No subclassig.
   */
  private VedUtils() {
    // nop
  }

}
