package org.toxsoft.core.tsgui.mws.appinf;

import static org.toxsoft.core.tsgui.mws.appinf.ITsResources.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.EEncloseMode;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.TsVersion;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Редактируемая реализация {@link ITsApplicationInfo}.
 *
 * @author hazard157
 */
public class TsApplicationInfo
    extends Stridable
    implements ITsApplicationInfo {

  /**
   * Псевдоним {@link ITsApplicationInfo#alias()} по умолчани.
   */
  public static final String DEFAULT_ALIAS = "tsapp"; //$NON-NLS-1$

  /**
   * Версия {@link ITsApplicationInfo#version()} по умолчани.
   */
  public static final TsVersion DEFAULT_VERSION = new TsVersion( 0, 1, 0L );

  /**
   * Валидатор проверки псевдонима приложения {@link ITsApplicationInfo#alias()}.
   */
  public static ITsValidator<String> ALIAS_VALIDATOR = new ITsValidator<>() {

    @SuppressWarnings( "boxing" )
    @Override
    public ValidationResult validate( String aValue ) {
      ValidationResult vr1 = StridUtils.validateIdName( aValue );
      if( vr1.isError() ) {
        return vr1;
      }
      int len = aValue.length();
      ValidationResult vr2 = ValidationResult.SUCCESS;
      if( len < MIN_ABBREV_ID_LEN ) {
        vr2 = ValidationResult.error( FMT_ERR_SHORT_ALIAS, MIN_ABBREV_ID_LEN, aValue );
      }
      else {
        if( len > MAX_ABBREV_ID_LEN ) {
          vr2 = ValidationResult.error( FMT_ERR_LONG_ALIAS, MAX_ABBREV_ID_LEN, aValue );
        }
      }
      vr1 = ValidationResult.firstNonOk( vr1, vr2 );
      if( vr1.isError() ) {
        return vr1;
      }
      for( int i = 0; i < len; i++ ) {
        char ch = aValue.charAt( i );
        if( ch == '_' ) {
          vr2 = ValidationResult.warn( FMR_WARN_ALIAS_HAS_UNDERSCORE, aValue );
          break;
        }
        if( Character.isUpperCase( ch ) ) {
          vr2 = ValidationResult.warn( FMR_WARN_ALIAS_HAS_UPPERCASE, aValue );
          break;
        }
      }
      vr1 = ValidationResult.firstNonOk( vr1, vr2 );
      return vr1;
    }

  };

  /**
   * Экземпляр-синглтон хранителя.
   */
  public static final IEntityKeeper<ITsApplicationInfo> KEEPER =
      new AbstractEntityKeeper<>( ITsApplicationInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, ITsApplicationInfo aEntity ) {
          aSw.incNewLine();
          // id()
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // alias(), nmName()
          aSw.writeAsIs( aEntity.alias() );
          aSw.writeSeparatorChar();
          aSw.writeSpace();
          aSw.writeQuotedString( aEntity.nmName() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // description()
          aSw.writeQuotedString( aEntity.description() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // version()
          TsVersion.KEEPER.write( aSw, aEntity.version() );
          aSw.writeSeparatorChar();
          aSw.decNewLine();
        }

        @Override
        protected ITsApplicationInfo doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          TsApplicationInfo appinfo = new TsApplicationInfo( id );
          appinfo.setAlias( aSr.readIdName() );
          aSr.ensureSeparatorChar();
          appinfo.setName( aSr.readQuotedString() );
          aSr.ensureSeparatorChar();
          appinfo.setDescription( aSr.readQuotedString() );
          aSr.ensureSeparatorChar();
          appinfo.setVersion( TsVersion.KEEPER.read( aSr ) );
          return appinfo;
        }
      };

  static final int MIN_RECOMMENDED_DEPLAPP_ID_COMPS_COUNT = 3;
  static final int MIN_ABBREV_ID_LEN                      = 2;
  static final int MAX_ABBREV_ID_LEN                      = 5;

  private String    alias   = DEFAULT_ALIAS;
  private TsVersion version = DEFAULT_VERSION;

  /**
   * Конструктор.
   *
   * @param aId String - полный идентификатор (ИД-путь) приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public TsApplicationInfo( String aId ) {
    super( aId, DEFAULT_TSAPP_NAME, DEFAULT_TSAPP_DESCRIPTION );
    clear();
  }

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aId String - полный идентификатор (ИД-путь) приложения
   * @param aName String - краткое название
   * @param aDescription String - описание
   * @param aAlias String - сокращенный идентификатор (ИД-имя длиной 2-5 символов) разворачиваемого приложения
   * @param aVersion {@link TsVersion} - версия приложения
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException идентификатор не ИД-путь
   */
  public TsApplicationInfo( String aId, String aName, String aDescription, String aAlias, TsVersion aVersion ) {
    super( aId, aName, aDescription );
    alias = StridUtils.checkValidIdName( aAlias );
    version = TsNullArgumentRtException.checkNull( aVersion );
  }

  /**
   * Конструктор копирования.
   *
   * @param aSrc {@link ITsApplicationInfo} - истоник копирования
   * @throws TsNullArgumentRtException аргумент = null
   */
  public TsApplicationInfo( ITsApplicationInfo aSrc ) {
    super( ALIAS_VALIDATOR.checkValid( TsNullArgumentRtException.checkNull( aSrc ).id() ), EMPTY_STRING, EMPTY_STRING );
    setNameAndDescription( aSrc.nmName(), aSrc.description() );
    setAlias( aSrc.alias() );
    setVersion( aSrc.version() );
  }

  // ------------------------------------------------------------------------------------
  // API редактирования
  //

  @Override
  public void setNameAndDescription( String aName, String aDescription ) {
    super.setNameAndDescription( aName, aDescription );
  }

  @Override
  public void setName( String aName ) {
    super.setName( aName );
  }

  @Override
  public void setDescription( String aDescription ) {
    super.setDescription( aDescription );
  }

  /**
   * Задает псевдоним.
   *
   * @param aAlias String - псевдоним
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException не прошла проверка валидатором {@link #ALIAS_VALIDATOR}
   */
  public void setAlias( String aAlias ) {
    ALIAS_VALIDATOR.checkValid( aAlias );
    alias = aAlias;
  }

  /**
   * Задает версию приложения.
   *
   * @param aVersion {@link TsVersion} - версия
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setVersion( TsVersion aVersion ) {
    TsNullArgumentRtException.checkNull( aVersion );
    version = aVersion;
  }

  /**
   * Устанавливает все поля (кроме {@link #id()}) в значения по умолчанию.
   */
  public void clear() {
    setNameAndDescription( DEFAULT_TSAPP_NAME, DEFAULT_TSAPP_DESCRIPTION );
    setAlias( DEFAULT_ALIAS );
    setVersion( DEFAULT_VERSION );
  }

  // ------------------------------------------------------------------------------------
  // ITsApplicationInfo
  //

  @Override
  public String alias() {
    return alias;
  }

  @Override
  public TsVersion version() {
    return version;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return StridUtils.printf( StridUtils.FORMAT_ID_NAME, this );
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof ITsApplicationInfo ) {
      ITsApplicationInfo that = (ITsApplicationInfo)aThat;
      return super.equals( aThat ) && //
          this.alias.equals( that.alias() ) && //
          this.version.equals( that.version() );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = TsLibUtils.PRIME * result + alias.hashCode();
    result = TsLibUtils.PRIME * result + version.hashCode();
    return result;
  }

}
