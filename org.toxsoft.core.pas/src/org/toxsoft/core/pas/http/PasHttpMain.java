package org.toxsoft.core.pas.http;

import static org.toxsoft.core.pas.http.ITsResources.*;

import java.io.File;

import org.toxsoft.core.log4j.Logger;
import org.toxsoft.core.pas.http.server.PasHttpServer;
import org.toxsoft.core.tslib.bricks.apprefs.IAppPreferences;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;
import org.toxsoft.core.tslib.utils.logs.ILogger;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.tslib.utils.progargs.ProgramArgs;

/**
 * Запуск моста - реализацующий публичное API АС ФГДП.
 *
 * @author mvk
 */
public class PasHttpMain {

  private static final String CMD_LINE_ARG_HELP_1  = "h";      //$NON-NLS-1$
  private static final String CMD_LINE_ARG_HELP_2  = "-help";  //$NON-NLS-1$
  private static final String CMD_LINE_ARG_CFG_INI = "config"; //$NON-NLS-1$

  private static final String DEFAULT_CFG_INI_FILE_NAME = "pubapi-http.ini"; //$NON-NLS-1$

  private static final int RETCODE_CANT_START    = 1;
  private static final int RETCODE_RUNTIME_ERROR = 1;

  private static ILogger logger = Logger.getLogger( PasHttpMain.class );

  /**
   * Начало работы программы
   *
   * @param aArgs String[] - аргументы командной строки
   */
  public static void main( String[] aArgs ) {
    logger.info( MSG_HELLO1 );
    logger.info( MSG_HELLO2 );
    try {
      IAppPreferences appPrefs = prepareProgram( aArgs );
      PasHttpServer server = null;
      try {
        server = new PasHttpServer( appPrefs );
        server.start();
      }
      finally {
        if( server != null ) {
          server.close();
        }
      }
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      logger.error( FMT_FAILED, ex.getClass().getSimpleName(), ex.getMessage() );
      System.exit( RETCODE_RUNTIME_ERROR );
    }
    logger.info( MSG_BYE );
  }

  /**
   * Подготовка запуска программы на основе опциональных параметров командной строки.
   *
   * @param aArgs String[] - аргументы командной строки
   * @return {@link IAppPreferences} - возвращает параметры конфигурации, считанные из INI-файла
   */
  private static IAppPreferences prepareProgram( String[] aArgs ) {
    // Аргументы командной строки
    ProgramArgs programArgs = new ProgramArgs( aArgs );
    // Показ справки
    if( programArgs.hasArg( CMD_LINE_ARG_HELP_1 ) || programArgs.hasArg( CMD_LINE_ARG_HELP_2 ) ) {
      logger.info( FMT_HELP, CMD_LINE_ARG_CFG_INI, DEFAULT_CFG_INI_FILE_NAME );
      System.exit( RETCODE_CANT_START );
    }
    // Считаем настройки программы из файла конфигурации
    String configFileName = DEFAULT_CFG_INI_FILE_NAME;
    if( programArgs.argValues().hasKey( CMD_LINE_ARG_CFG_INI ) ) {
      configFileName = programArgs.argValues().getByKey( CMD_LINE_ARG_CFG_INI );
    }
    File cfgFile = new File( configFileName );
    ValidationResult vr = TsFileUtils.VALIDATOR_FILE_READABLE.validate( cfgFile );
    if( vr.isError() ) {
      logger.error( FMT_ERR_NO_CFG_FILE, cfgFile.getAbsolutePath() );
      System.exit( RETCODE_CANT_START );
    }
    AbstractAppPreferencesStorage apStorage = new AppPreferencesConfigIniStorage( cfgFile );
    return new AppPreferences( apStorage );
  }

}
