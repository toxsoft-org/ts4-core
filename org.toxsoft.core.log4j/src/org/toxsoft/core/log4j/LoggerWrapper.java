package org.toxsoft.core.log4j;

import static org.toxsoft.core.log4j.ITsResources.*;

import java.io.*;

import org.apache.log4j.*;
import org.apache.log4j.xml.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.vrl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Wraps over Log4j {@link Logger} to implementa tslib interface {@link ILogger}.
 *
 * @author mvk
 */
public class LoggerWrapper
    extends AbstractBasicLogger {

  /**
   * Log4j library expects in this system property the configuration XML file name.
   * <p>
   * Used in static method {@link #setScanPropertiesTimeout(long)}.
   */
  public static final String SYSPROP_LOG4J_CONFIG_FILE_NAME = "-Dlog4j.configuratio"; //$NON-NLS-1$

  /**
   * Valid extensions (without dot) and hence the formats of the Log4j configuration file.
   * <p>
   * Used in static method {@link #setScanPropertiesTimeout(long)}.
   */
  public static final String[] LOG4J_CONFIG_FILE_EXTS = { "properties", "xml" }; //$NON-NLS-1$//$NON-NLS-2$

  private final Logger source;

  /**
   * Creates wrapper over the specified Log4j logger.
   *
   * @param aSource {@link org.apache.log4j.Logger} - Log4j-logger
   * @return ILogger {@link ILogger} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ILogger getLogger( org.apache.log4j.Logger aSource ) {
    return new LoggerWrapper( aSource );
  }

  /**
   * Creates wrapper for the category specified by the name.
   *
   * @param aName String - the category name
   * @return ILogger {@link ILogger} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ILogger getLogger( String aName ) {
    return new LoggerWrapper( aName );
  }

  /**
   * Creates wrapper to log messages from the specified class.
   *
   * @param aClass Class - the class
   * @return ILogger {@link ILogger} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static ILogger getLogger( Class<?> aClass ) {
    return new LoggerWrapper( aClass );
  }

  // GOGA 2025-10-27: as i found, this method is not used, anyway, it is deprecated!
  // /**
  // * Writes validation check results to the logger.
  // *
  // * @param aLogger {@link ILogger} - the logger
  // * @param aResults {@link IValResList} - validation results
  // * @throws TsNullArgumentRtException any argument = <code>null</code>
  // */
  // @Deprecated
  // public static void resultsToLog( ILogger aLogger, IValResList aResults ) {
  // TsNullArgumentRtException.checkNulls( aLogger, aResults );
  // for( ValidationResult result : aResults.results() ) {
  // resultToLog( aLogger, result );
  // }
  // }
  // ---

  /**
   * Writes validation check results to the logger.
   *
   * @param aLogger {@link ILogger} - the logger
   * @param aResults {@link IVrList} - validation results
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @Deprecated
  public static void resultsToLog( ILogger aLogger, IVrList aResults ) {
    TsNullArgumentRtException.checkNulls( aLogger, aResults );
    for( VrlItem item : aResults.items() ) {
      resultToLog( aLogger, item.vr() );
    }
  }

  /**
   * Writes single validation check result to the logger.
   *
   * @param aLogger {@link ILogger} - the logger
   * @param aResult {@link ValidationResult} - the result to be written
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void resultToLog( ILogger aLogger, ValidationResult aResult ) {
    TsNullArgumentRtException.checkNulls( aLogger, aResult );
    switch( aResult.type() ) {
      case OK:
        if( !aResult.message().equals( TsLibUtils.EMPTY_STRING ) ) {
          aLogger.info( aResult.message() );
        }
        break;
      case WARNING:
        aLogger.warning( aResult.message() );
        break;
      case ERROR:
        aLogger.error( aResult.message() );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // Private constructors
  //

  private LoggerWrapper( org.apache.log4j.Logger aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  private LoggerWrapper( String aName ) {
    source = org.apache.log4j.Logger.getLogger( TsNullArgumentRtException.checkNull( aName ) );
  }

  private LoggerWrapper( Class<?> aClass ) {
    source = org.apache.log4j.Logger.getLogger( TsNullArgumentRtException.checkNull( aClass ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractBasicLogger
  //

  @Override
  public boolean isSeverityOn( ELogSeverity aSeverity ) {
    TsNullArgumentRtException.checkNull( aSeverity );
    return source.isEnabledFor( ts2j( aSeverity ) );
  }

  // ------------------------------------------------------------------------------------
  // ILogger
  //

  @Override
  public void log( ELogSeverity aLogSeverity, String aMessage, Object... aArgs ) {
    Level level = ts2j( aLogSeverity );
    String msg = String.format( aMessage, aArgs );
    source.log( level, msg );
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException, String aMessage, Object... aArgs ) {
    Level level = ts2j( aLogSeverity );
    String msg = String.format( aMessage, aArgs );
    source.log( level, msg, aException );
  }

  @Override
  public void log( ELogSeverity aLogSeverity, Throwable aException ) {
    Level level = ts2j( aLogSeverity );
    source.log( level, TsLibUtils.EMPTY_STRING, aException );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static Level ts2j( ELogSeverity aLogSeverity ) {
    return switch( aLogSeverity ) {
      case DEBUG -> Level.TRACE;
      case ERROR -> Level.ERROR;
      case WARNING -> Level.WARN;
      case INFO -> Level.INFO;
      default -> throw new TsNotAllEnumsUsedRtException();
    };

  }

  /**
   * Sets the logging properties configuration file rescan intervals.
   * <p>
   * Every <code>aIntervalMsecs</code> milliseconds application checks logger configuration file and applies changes.
   * <p>
   * Log4j library expects in this system property the configuration XML file name. This method requires
   * {@link #SYSPROP_LOG4J_CONFIG_FILE_NAME} to contain valid file name with extension from
   * {@link #LOG4J_CONFIG_FILE_EXTS} to be specified. Otherwise method write warning in {@link System#err} and does
   * nothing.
   *
   * @param aIntervalMsecs long - rescan interval in milliseconds
   */
  public static void setScanPropertiesTimeout( long aIntervalMsecs ) {
    String log4jfilename = System.getProperty( "log4j.configuration" ); //$NON-NLS-1$
    if( log4jfilename == null ) {
      System.err.println( LOG_NO_LOG4J_CFG_FILE_IN_SYS_PROPS );
      return;
    }
    log4jfilename = log4jfilename.substring( 5 );
    if( new File( log4jfilename ).exists() ) {
      if( log4jfilename.endsWith( ".properties" ) ) { //$NON-NLS-1$
        PropertyConfigurator.configureAndWatch( log4jfilename, aIntervalMsecs );
        return;
      }
      if( log4jfilename.endsWith( ".xml" ) ) { //$NON-NLS-1$
        DOMConfigurator.configureAndWatch( log4jfilename, aIntervalMsecs );
        return;
      }
      System.err.println( LOG_NO_LOG4J_CFG_FILE_FOUND + log4jfilename );
    }
    System.err.println( LOG_LOG4J_CFG_FILE_INV_EXT + log4jfilename );
  }

}
