package org.toxsoft.core.txtproj.lib.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.txtproj.lib.*;

/**
 * Utility methods to work with {@link ITsProject}.
 *
 * @author hazard157
 */
public class TsProjectUtils {

  /**
   * Reads project content from file.
   *
   * @param aFile {@link File} - file to read content from
   * @param aProject {@link ITsProject} - the project
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error accessing file
   * @throws StrioRtException error reading content
   */
  public static void readContentFromFile( File aFile, ITsProject aProject ) {
    TsNullArgumentRtException.checkNulls( aFile, aProject );
    try( ICharInputStreamCloseable chIn = new CharInputStreamFile( aFile ) ) {
      IStrioReader dr = new StrioReader( chIn );
      aProject.read( dr );
    }
    catch( Exception e ) {
      throw new StrioRtException( e );
    }
  }

  /**
   * Writes project content to the file.
   *
   * @param aFile {@link File} - file to write content to
   * @param aProject {@link ITsProject} - the project
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException error writing file
   */
  public static void writeContentToFile( File aFile, ITsProject aProject ) {
    try( FileWriter fw = new FileWriter( aFile ) ) {
      ICharOutputStream chOut = new CharOutputStreamWriter( fw );
      IStrioWriter sw = new StrioWriter( chOut );
      aProject.write( sw );
    }
    catch( IOException e ) {
      throw new TsIoRtException( e );
    }

  }

  /**
   * No subclassing.
   */
  private TsProjectUtils() {
    // nop
  }

}
