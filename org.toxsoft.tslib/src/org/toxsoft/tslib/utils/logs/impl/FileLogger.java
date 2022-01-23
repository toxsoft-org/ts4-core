package org.toxsoft.tslib.utils.logs.impl;

import static org.toxsoft.tslib.utils.logs.impl.ITsResources.*;

import java.io.*;

import org.toxsoft.tslib.coll.primtypes.IStringList;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.logs.ELogSeverity;
import org.toxsoft.tslib.utils.logs.ICloseableLogger;

// TRANSLATE

/**
 * Log messages writer to the text file.
 * <p>
 * This is the package-private class. Instances are created by the {@link LoggerUtils} static methods.
 *
 * @author hazard157
 */
class FileLogger
    extends AbstractLogger
    implements ICloseableLogger {

  /**
   * Output stream.
   * <p>
   * Stream is initialized in constructor, and becames <code>null</code> when it closes in {@link #close()} method.
   */
  private PrintStream out = null;

  /**
   * Constructor,
   *
   * @param aLogFile {@link File} - output file
   * @param aAppend boolean - append to the existing file sign
   * @param aAutoCreate boolean - create unexisting file sign, otherwise exception will be thrown
   * @param aFormatter {@link LogMessageFormatter} - log messages formatter
   * @throws TsNullArgumentRtException aLogFile = null
   * @throws TsIoRtException I/O exception
   */
  FileLogger( File aLogFile, boolean aAppend, boolean aAutoCreate, LogMessageFormatter aFormatter ) {
    super( aFormatter );
    TsNullArgumentRtException.checkNull( aLogFile );
    // check and create log file if needed
    if( !aLogFile.exists() ) {
      if( !aAutoCreate ) {
        throw new TsIoRtException( FMT_ERR_NO_LOG_FILE, aLogFile.getPath() );
      }
      try {
        aLogFile.createNewFile();
      }
      catch( IOException ex ) {
        throw new TsIoRtException( ex, FMT_ERR_CANT_CREATE_LOG_FILE, aLogFile.getPath() );
      }
    }
    // output stream creation
    try {
      OutputStream outs = new FileOutputStream( aLogFile, aAppend );
      out = new PrintStream( outs );
    }
    catch( Throwable ex ) {
      throw new TsIoRtException( ex, FMT_ERR_CANT_CREATE_LOG_FILE, aLogFile.getPath() );
    }
  }

  // ------------------------------------------------------------------------------------
  // ISkLogger
  //

  @Override
  public boolean isSeverityOn( ELogSeverity aSeverity ) {
    TsNullArgumentRtException.checkNull( aSeverity );
    return true;
  }

  @Override
  public void log( ELogSeverity aLogSeverity, String aMessage, Object... aArgs ) {
    if( out == null ) {
      throw new TsIllegalStateRtException();
    }
    IStringList sl = msgFormatter().format( System.currentTimeMillis(), aLogSeverity, aMessage, aArgs );
    for( int i = 0, n = sl.size(); i < n; i++ ) {
      out.println( sl.get( i ) );
    }
    out.flush();
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException, String aMessage, Object... aArgs ) {
    if( out == null ) {
      throw new TsIllegalStateRtException();
    }
    IStringList sl = msgFormatter().format( System.currentTimeMillis(), aLogSeverity, aException, aMessage, aArgs );
    for( int i = 0, n = sl.size(); i < n; i++ ) {
      out.println( sl.get( i ) );
    }
    out.flush();
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException ) {
    if( out == null ) {
      throw new TsIllegalStateRtException();
    }
    IStringList sl = msgFormatter().format( System.currentTimeMillis(), aLogSeverity, aException,
        TsLibUtils.EMPTY_STRING, TsLibUtils.EMPTY_ARRAY_OF_OBJECTS );
    for( int i = 0, n = sl.size(); i < n; i++ ) {
      out.println( sl.get( i ) );
    }
    out.flush();
  }

  // ------------------------------------------------------------------------------------
  // ICloseableLogger
  //

  @Override
  public void close() {
    if( out == null ) {
      throw new TsIllegalStateRtException();
    }
    try {
      out.flush();
      out.close();
    }
    finally {
      out = null;
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractLogger
  //

  @Override
  protected boolean beforeLog( ELogSeverity aLogSeverity, Throwable aException, String aMessage, Object[] aArgs ) {
    if( out == null ) {
      throw new TsIllegalStateRtException();
    }
    return true;
  }

  @Override
  protected void afterLog() {
    out.flush();
  }

  @Override
  protected void printLine( String aLine ) {
    out.println( aLine );
  }

}
