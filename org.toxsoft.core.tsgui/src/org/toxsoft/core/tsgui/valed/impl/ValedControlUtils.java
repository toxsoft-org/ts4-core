package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.controls.enums.IValedEnumConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.time.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.controls.enums.*;
import org.toxsoft.core.tsgui.valed.controls.time.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.std.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Вспомгательные методы для работы с редакторами.
 *
 * @author goga
 */
public class ValedControlUtils {

  /**
   * Returns the default factory name for the specified atomic type.
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @return String - name of the editor factory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String getDefaultFactoryName( EAtomicType aAtomicType ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    switch( aAtomicType ) {
      case NONE:
        return ValedAvAnytypeText.FACTORY_NAME;
      case BOOLEAN:
        return ValedAvBooleanCheck.FACTORY_NAME;
      case INTEGER:
        return ValedAvIntegerSpinner.FACTORY_NAME;
      case FLOATING:
        return ValedAvFloatSpinner.FACTORY_NAME;
      case TIMESTAMP:
        return ValedAvTimestampMpv.FACTORY_NAME;
      case STRING:
        return ValedAvStringText.FACTORY_NAME;
      case VALOBJ:
        return ValedAvValobjRoText.FACTORY_NAME;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Returns the default factory for the specified atomic type.
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @return {@link AbstractValedControlFactory} - the factory
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static AbstractValedControlFactory getDefaultFactory( EAtomicType aAtomicType ) {
    TsNullArgumentRtException.checkNull( aAtomicType );
    switch( aAtomicType ) {
      case NONE:
        return ValedAvAnytypeText.FACTORY;
      case BOOLEAN:
        return ValedAvBooleanCheck.FACTORY;
      case INTEGER:
        return ValedAvIntegerSpinner.FACTORY;
      case FLOATING:
        return ValedAvFloatSpinner.FACTORY;
      case TIMESTAMP:
        return ValedAvTimestampMpv.FACTORY;
      case STRING:
        return ValedAvStringText.FACTORY;
      case VALOBJ:
        return ValedAvValobjRoText.FACTORY;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Chooses an editor factory based on atomic type and context parameters.
   * <p>
   * The argument aContext must the context used to create an editor instance. This mathod may change context parameters
   * and/or references.
   * <p>
   * Initially tries to create an editor as it is intended: using the factory name from option
   * {@link IValedControlConstants#OPID_EDITOR_FACTORY_NAME} and using reference to the
   * {@link ValedControlFactoriesRegistry} if both of them are present in the context.
   * <p>
   * Otherwise tries to find suitable editor based on atomic type default {@link #getDefaultFactory(EAtomicType)} and/or
   * other options in context (such as {@link IAvMetaConstants#TSID_KEEPER_ID} for {@link EAtomicType#VALOBJ}).
   *
   * @param aAtomicType {@link EAtomicType} - the atomic type
   * @param aContext {@link ITsGuiContext} - valed creation context
   * @return {@link IValedControlFactory} - most suitable editor, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IValedControlFactory guessAvEditorFactory( EAtomicType aAtomicType, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aAtomicType, aContext );
    IValedControlFactory factory = null;
    // straight way to get editor - take it from OPID_EDITOR_FACTORY_NAME option
    ValedControlFactoriesRegistry vcfRegistry = aContext.find( ValedControlFactoriesRegistry.class );
    IAtomicValue avFacName = IAtomicValue.NULL;
    if( aContext.params().hasKey( OPID_EDITOR_FACTORY_NAME ) ) {
      avFacName = aContext.params().getValue( OPID_EDITOR_FACTORY_NAME );
    }
    if( vcfRegistry != null && avFacName != IAtomicValue.NULL ) {
      factory = vcfRegistry.findFactory( avFacName.asString() );
    }
    // straight way not works, now using heuristic
    if( factory == null ) {
      switch( aAtomicType ) {
        case BOOLEAN:
        case INTEGER:
        case FLOATING:
        case TIMESTAMP:
        case STRING:
        case NONE:
          // HERE may be additional heuristic
          break;
        case VALOBJ: {
          // choosding editor by keeper ID from argument option TSID_KEEPER_ID
          String keeperId = aContext.params().getStr( TSID_KEEPER_ID, null );
          if( keeperId != null ) {
            // method findFactoryForValobjByKeeperId() recognizes some specifi keeper IDs
            factory = findFactoryForValobjByKeeperId( keeperId );
            if( factory == null ) {
              // some generic editors may be used depending on entity class
              IEntityKeeper<?> keeper = TsValobjUtils.findKeeperById( keeperId );
              if( keeper != null ) { // this happens only for unregistered VALOBJs
                /**
                 * For Java enum VALOBJ we have generic editor
                 */
                Class<?> rawClass = keeper.entityClass();
                if( rawClass != null ) {
                  // if VALOBJ entity is Java enum, use enum drop-down Combo editor
                  if( rawClass.isEnum() ) {
                    REFDEF_ENUM_CLASS.setRef( aContext, rawClass );
                    factory = ValedAvValobjEnumCombo.FACTORY;
                  }
                }
                // too bad - no suitable editor was found
              }
            }
          }
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
    // suitable factory not found - using very generic editor depending on atomic type of edited value
    if( factory == null ) {
      factory = ValedControlUtils.getDefaultFactory( aAtomicType );
    }
    return factory;
  }

  /**
   * Chooses an editor factory based on solely on edited value class.
   * <p>
   * The argument aContext must the context used to create an editor instance. This mathod may change context parameters
   * and/or references.
   *
   * @param aValueClass {@link Class} - the edited value class
   * @param aContext {@link ITsGuiContext} - valed creation context
   * @return {@link IValedControlFactory} - most suitable editor, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static IValedControlFactory guessRawEditorFactory( Class<?> aValueClass, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aValueClass, aContext );
    // IAtomicValue
    if( IAtomicValue.class.isAssignableFrom( aValueClass ) ) {
      return ValedAvAnytypeText.FACTORY;
    }
    // попробуем подобрать редактор для Enum-ов
    if( Enum.class.isAssignableFrom( aValueClass ) ) {
      IValedEnumConstants.REFDEF_ENUM_CLASS.setRef( aContext, aValueClass );
      return ValedEnumCombo.FACTORY;
    }

    // редакторы для временных классов
    if( aValueClass.equals( LocalTime.class ) ) {
      return ValedLocalTimeMpv.FACTORY;
    }
    if( aValueClass.equals( LocalDate.class ) ) {
      return ValedLocalDateMpv.FACTORY;
    }
    if( aValueClass.equals( LocalDateTime.class ) ) {
      return ValedLocalDateTimeMpv.FACTORY;
    }
    // nothing can be done...
    return null;
  }

  /**
   * Ищет фабрику редактор по идентификатору хранителя атомарного типа {@link EAtomicType#VALOBJ}.
   *
   * @param aKeeperId String - идентификатор хранителя
   * @return {@link IValedControlFactory} - найденная фабрика или <code>null</code>
   */
  private static IValedControlFactory findFactoryForValobjByKeeperId( String aKeeperId ) {
    switch( aKeeperId ) {
      case LocalTimeKeeper.KEEPER_ID:
        return ValedAvValobjLocalTimeMpv.FACTORY;
      case LocalDateKeeper.KEEPER_ID:
        return ValedAvValobjLocalDateMpv.FACTORY;
      case LocalDateTimeKeeper.KEEPER_ID:
        return ValedAvValobjLocalDateTimeMpv.FACTORY;
      default:
        return null;
    }
  }

  /**
   * Запрет на создание экземпляров.
   */
  private ValedControlUtils() {
    // nop
  }

}
