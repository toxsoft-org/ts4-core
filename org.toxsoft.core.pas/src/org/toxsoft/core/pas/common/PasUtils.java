package org.toxsoft.core.pas.common;

import static org.toxsoft.core.pas.common.ITsResources.*;
import static org.toxsoft.core.tslib.utils.TsTestUtils.*;

import java.io.File;
import java.io.OutputStreamWriter;

import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.impl.TjUtils;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AtomicValueKeeper;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.bricks.apprefs.IAppPreferences;
import org.toxsoft.core.tslib.bricks.apprefs.IPrefBundle;
import org.toxsoft.core.tslib.bricks.apprefs.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.ITsContext;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.files.TsFileUtils;
import org.toxsoft.core.tslib.utils.logs.impl.LoggerUtils;
import org.toxsoft.core.tslib.utils.progargs.ProgramArgs;

/**
 * Вспомогательные методы для ввода/вывода.
 *
 * @author mvk
 */
public class PasUtils {

  // ------------------------------------------------------------------------------------
  // Открытые методы
  //
  /**
   * Сохраняет объект {@link ITjObject} в потоке писателя {@link OutputStreamWriter}.
   *
   * @param aOut {@link OutputStreamWriter} - выходной поток
   * @param aObj {@link ITjObject} - JSON-объект
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIoRtException ошибка записи в поток
   */
  public static void writeJsonObject( OutputStreamWriter aOut, ITjObject aObj ) {
    if( aOut == null ) {
      throw new TsNullArgumentRtException();
    }
    try {
      TjUtils.saveObject( aOut, aObj );
    }
    catch( Throwable e ) {
      // Ошибка записи объекта в поток канала
      throw new TsIoRtException( e, ERR_WRITE, aObj, cause( e ) );
    }
  }

  /**
   * Заносит в контекст aContext параметры из командной стори и файла конфигурации.
   *
   * @param aContext {@link ITsContext} - подготавливамый контекст
   * @param aArgs {@link ProgramArgs} - аргументы командно строки
   * @param aConfigFileName String - имя файла конфигурации
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public static void prepareContextParams( ITsContext aContext, ProgramArgs aArgs, String aConfigFileName ) {
    TsNullArgumentRtException.checkNull( aContext );
    TsNullArgumentRtException.checkNull( aArgs );
    // сначала загрузим конфигурационный файл
    String configFileName = TsNullArgumentRtException.checkNull( aConfigFileName );
    File cfgFile = new File( configFileName );
    ValidationResult vr = TsFileUtils.VALIDATOR_FILE_READABLE.validate( cfgFile );
    if( vr.isError() ) {
      pl( ERR_NO_CFG_FILE, cfgFile.getAbsolutePath() );
      System.exit( 1 );
    }
    // занесем параметры из файла в контекст
    AbstractAppPreferencesStorage apStorage = new AppPreferencesConfigIniStorage( cfgFile );
    IAppPreferences appPreferences = new AppPreferences( apStorage );
    for( String pbId : appPreferences.listPrefBundleIds() ) {
      IPrefBundle pb = appPreferences.getBundle( pbId );
      aContext.params().addAll( pb.params() );
    }
    // аргументы командной строки имеют высший приоритет
    for( String argId : aArgs.argValues().keys() ) {
      // допускаются только аргументы - ИД-пути
      if( !StridUtils.isValidIdPath( argId ) ) {
        LoggerUtils.errorLogger().warning( ERR_IGNORED_INV_ARG_ID, argId );
        continue;
      }
      String valStr = aArgs.getArgValue( argId );
      // Значение аргумента должно быть атомарным значением, иначе интерпретируем как строку
      IAtomicValue av;
      try {
        av = AtomicValueKeeper.KEEPER.str2ent( valStr );
      }
      catch( @SuppressWarnings( "unused" ) Exception ex ) {
        LoggerUtils.errorLogger().warning( ERR_ARG_VAL_AS_STRING, argId );
      }
      av = AvUtils.avStr( valStr );
      aContext.params().setValue( argId, av );
    }
  }

  /**
   * Возвращает из контекста значение текстового параметра проверяя значение на отличие от пустой строки
   *
   * @param aContext {@link ITsContextRo} контекст
   * @param aOp {@link IDataDef} описание параметра
   * @return String значение параметра
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException значение параметра является пустой строкой
   */
  public static String getNonEmptyCtxStringParam( ITsContextRo aContext, IDataDef aOp ) {
    TsNullArgumentRtException.checkNull( aContext );
    TsNullArgumentRtException.checkNull( aOp );
    String s = aOp.getValue( aContext.params() ).asString();
    if( s.isEmpty() ) {
      throw new TsIllegalArgumentRtException( ERR_EMPRY_STR_PARAM, aOp.id() );
    }
    return s;
  }

  // ------------------------------------------------------------------------------------
  // Вспомогательные методы
  //
  /**
   * Возвращает причину ошибки
   *
   * @param aError {@link Throwable} ошибка
   * @return String причина ошибки
   */
  public static String cause( Throwable aError ) {
    if( aError == null ) {
      return "???"; //$NON-NLS-1$
    }
    String err = aError.getClass().getSimpleName();
    return (aError.getLocalizedMessage() != null ? err + ':' + ' ' + aError.getLocalizedMessage() : err);
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //
  /**
   * Запрет на создание экземпляров.
   */
  private PasUtils() {
    // nop
  }

}
