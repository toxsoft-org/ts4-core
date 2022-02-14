package org.toxsoft.core.txtproj.lib.impl;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.txtproj.lib.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.DataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.opset.IOptionSet;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.av.opset.impl.OptionSet;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Информация о файле проекта.
 * <p>
 * Хранится в файле проекта. Пока особо не используется, нужно будет для идентификации, котнроля версии и т.п.
 * <p>
 * Это неизменяемый класс
 *
 * @author hazard157
 */
public final class TsProjectFileFormatInfo {

  /**
   * Минимальная версия формата.
   */
  private static final int MIN_TS_FILE_FORMAT_VERSION = 1;

  /**
   * Текущая версия формата.
   * <p>
   * Верися формата может находится в пределах от {@link #MIN_TS_FILE_FORMAT_VERSION} до текущей.
   */
  private static final int CURRENT_TS_FILE_FORMAT_VERSION = 1;

  private static final String PARAM_ID_TS_PROJ_FILE_VERSION  = "TsFileFormatVersion";  //$NON-NLS-1$
  private static final String PARAM_ID_APP_PROJ_FILE_VERSION = "AppFileFormatVersion"; //$NON-NLS-1$
  private static final String PARAM_ID_APP_ID                = "ApplicationId";        //$NON-NLS-1$

  private static final IDataDef TS_PROJ_FILE_VERSION = DataDef.create( PARAM_ID_TS_PROJ_FILE_VERSION, INTEGER, //
      TSID_NAME, STR_N_TS_PROJ_FILE_VERSION, //
      TSID_DESCRIPTION, STR_D_TS_PROJ_FILE_VERSION, //
      TSID_DEFAULT_VALUE, AV_1, //
      TSID_IS_MANDATORY, AV_TRUE//
  );

  private static final IDataDef APP_PROJ_FILE_VERSION = DataDef.create( PARAM_ID_APP_PROJ_FILE_VERSION, INTEGER, //
      TSID_NAME, STR_N_APP_PROJ_FILE_VERSION, //
      TSID_DESCRIPTION, STR_D_APP_PROJ_FILE_VERSION, //
      TSID_DEFAULT_VALUE, AV_1, //
      TSID_IS_MANDATORY, AV_TRUE //
  );

  private static final IDataDef APP_ID = DataDef.create( PARAM_ID_APP_ID, STRING, //
      TSID_NAME, STR_N_APP_ID, //
      TSID_DESCRIPTION, STR_D_APP_ID, //
      TSID_DEFAULT_VALUE, AV_STR_EMPTY, //
      TSID_IS_MANDATORY, AV_TRUE //
  );

  private static final IDataDef[] ALL_PARAM_INFOES = { //
      TS_PROJ_FILE_VERSION, //
      APP_PROJ_FILE_VERSION, //
      APP_ID //
  };

  final IOptionSetEdit params = new OptionSet();

  /**
   * Конструктор со всеми инварианами.
   *
   * @param aAppId String - идентификатор (ИД-путь) приложения
   * @param aAppFormatVersion int - приложение-специфичная версия формата файла проекта
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public TsProjectFileFormatInfo( String aAppId, int aAppFormatVersion ) {
    StridUtils.checkValidIdPath( aAppId );
    TS_PROJ_FILE_VERSION.setValue( params, avInt( CURRENT_TS_FILE_FORMAT_VERSION ) );
    APP_PROJ_FILE_VERSION.setValue( params, avInt( aAppFormatVersion ) );
    APP_ID.setValue( params, avStr( aAppId ) );
  }

  @SuppressWarnings( "boxing" )
  TsProjectFileFormatInfo( IOptionSet aParams ) {
    params.addAll( aParams );
    for( IDataDef pInfo : ALL_PARAM_INFOES ) {
      if( pInfo.isMandatory() ) {
        if( !aParams.hasValue( pInfo ) ) {
          throw new TsIllegalArgumentRtException( FMT_ERR_NO_MANDATORY_PARAM, pInfo.id() );
        }
      }
    }
    StridUtils.checkValidIdPath( APP_ID.getValue( params ).asString() );
    int tsFmtVer = TS_PROJ_FILE_VERSION.getValue( params ).asInt();
    if( tsFmtVer < MIN_TS_FILE_FORMAT_VERSION || tsFmtVer > CURRENT_TS_FILE_FORMAT_VERSION ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_UNKNOWN_TS_FMT_VER, tsFmtVer, MIN_TS_FILE_FORMAT_VERSION,
          CURRENT_TS_FILE_FORMAT_VERSION );
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает версию формата файла.
   * <p>
   * Версия всегда >= 0.
   *
   * @return int - версия формата файла проекта
   */
  public int projectFileFormatVersion() {
    return TS_PROJ_FILE_VERSION.getValue( params ).asInt();
  }

  /**
   * Возвращает идентификатор приложения создавшего файл проекта.
   *
   * @return String - идентификатор (ИД-путь) приложения
   */
  public String appId() {
    return APP_ID.getValue( params ).asString();
  }

  /**
   * Возвращает приложение-специфичную версию формата файла проекта.
   *
   * @return int - приложение-специфичная версия формата файла проекта
   */
  public int appFormatVersion() {
    return APP_PROJ_FILE_VERSION.getValue( params ).asInt();
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    for( String name : params.keys() ) {
      IAtomicValue value = params.getValue( name );
      sb.append( name );
      sb.append( " = " );
      sb.append( value.asString() );
      sb.append( "\n" );
    }
    return sb.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TsProjectFileFormatInfo that ) {
      return params.equals( that.params );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + params.hashCode();
    return result;
  }

}
