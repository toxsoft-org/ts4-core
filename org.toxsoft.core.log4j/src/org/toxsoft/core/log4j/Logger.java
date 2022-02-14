package org.toxsoft.core.log4j;

import java.io.File;

import org.apache.log4j.Level;
import org.apache.log4j.PropertyConfigurator;
import org.apache.log4j.xml.DOMConfigurator;
import org.toxsoft.core.tslib.bricks.validator.IValResList;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.logs.ELogSeverity;
import org.toxsoft.core.tslib.utils.logs.ILogger;
import org.toxsoft.core.tslib.utils.logs.impl.AbstractBasicLogger;

/**
 * Оболочка над log4j-логером {@link Logger}, реализующий ТоксСофт-овский интерфейс {@link ILogger}.
 *
 * @author mvk
 */
public class Logger
    extends AbstractBasicLogger {

  private final org.apache.log4j.Logger source;

  /**
   * Создает оболочку над указанным log4j-логером.
   *
   * @param aSource {@link org.apache.log4j.Logger} - log4j-логер
   * @return ILogger {@link ILogger} журнал
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ILogger getLogger( org.apache.log4j.Logger aSource ) {
    return new Logger( aSource );
  }

  /**
   * Создает оболочку для указанной категории.
   *
   * @param aName String - имя категории
   * @return ILogger {@link ILogger} журнал
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ILogger getLogger( String aName ) {
    return new Logger( aName );
  }

  /**
   * Создает оболочку для указанного класса.
   *
   * @param aClass Class - класс
   * @return ILogger {@link ILogger} журнал
   * @throws TsNullArgumentRtException аргумент = null
   */
  public static ILogger getLogger( Class<?> aClass ) {
    return new Logger( aClass );
  }

  /**
   * Запись в журнал результов
   *
   * @param aLogger {@link ILogger} журнал
   * @param aResults {@link IValResList} результат выполнения
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void resultsToLog( ILogger aLogger, IValResList aResults ) {
    TsNullArgumentRtException.checkNulls( aLogger, aResults );
    for( ValidationResult result : aResults.results() ) {
      resultToLog( aLogger, result );
    }
  }

  /**
   * Запись в журнал результа.
   *
   * @param aLogger {@link ILogger} журнал
   * @param aResult {@link ValidationResult} результат выполнения
   * @throws TsNullArgumentRtException любой аргумент = null
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
        // Переполнение очереди команд ожидающих выполнение. Старая команда удалена
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
  // Закрытые конструкторы
  //
  /**
   * Создает оболочку над указанным log4j-логером.
   *
   * @param aSource {@link org.apache.log4j.Logger} - log4j-логер
   * @throws TsNullArgumentRtException аргумент = null
   */
  private Logger( org.apache.log4j.Logger aSource ) {
    source = TsNullArgumentRtException.checkNull( aSource );
  }

  /**
   * Создает оболочку для указанной категории.
   *
   * @param aName String - имя категории
   * @throws TsNullArgumentRtException аргумент = null
   */
  private Logger( String aName ) {
    source = org.apache.log4j.Logger.getLogger( TsNullArgumentRtException.checkNull( aName ) );
  }

  /**
   * Создает оболочку для указанного класса.
   *
   * @param aClass Class - класс
   * @throws TsNullArgumentRtException аргумент = null
   */
  private Logger( Class<?> aClass ) {
    source = org.apache.log4j.Logger.getLogger( TsNullArgumentRtException.checkNull( aClass ) );
  }

  // ------------------------------------------------------------------------------------
  // Переопределение методов базового класса AbstractBasicLogger
  //
  @Override
  public boolean isSeverityOn( ELogSeverity aSeverity ) {
    TsNullArgumentRtException.checkNull( aSeverity );
    return source.isEnabledFor( ts2j( aSeverity ) );
  }

  // ------------------------------------------------------------------------------------
  // Реализация недостающих методов интерфейса ILogger
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
  // Внутренние методы
  //
  private static Level ts2j( ELogSeverity aLogSeverity ) {
    switch( aLogSeverity ) {
      case DEBUG:
        return Level.TRACE;
      case ERROR:
        return Level.ERROR;
      case WARNING:
        return Level.WARN;
      case INFO:
        return Level.INFO;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }

  }

  /**
   * Настроить период сканирования свойств журналирования
   *
   * @param aDelay long время (мсек) между проверками изменения свойств
   */
  public static void setScanPropertiesTimeout( long aDelay ) {
    String log4jfilename = System.getProperty( "log4j.configuration" ); //$NON-NLS-1$
    if( log4jfilename == null ) {
      System.err.println(
          "Logger.setScanPropertiesTimeout(): в системных свойствах (JVM) не установлен параметр : -Dlog4j.configuration" ); //$NON-NLS-1$
      return;
    }
    log4jfilename = log4jfilename.substring( 5 );
    if( new File( log4jfilename ).exists() ) {
      if( log4jfilename.endsWith( ".properties" ) ) { //$NON-NLS-1$
        PropertyConfigurator.configureAndWatch( log4jfilename, aDelay );
        return;
      }
      if( log4jfilename.endsWith( ".xml" ) ) { //$NON-NLS-1$
        DOMConfigurator.configureAndWatch( log4jfilename, aDelay );
        return;
      }
      System.err.println( "Logger.setScanPropertiesTimeout(): недопустимое расширение файла для настроек log4j: " //$NON-NLS-1$
          + log4jfilename );
    }
    System.err.println(
        "Logger.setScanPropertiesTimeout(): не найден файл свойств указанный через параметр : -Dlog4j.configuration. filename = " //$NON-NLS-1$
            + log4jfilename );
  }

}
