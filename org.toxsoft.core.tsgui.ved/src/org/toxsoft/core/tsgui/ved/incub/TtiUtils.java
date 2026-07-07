package org.toxsoft.core.tsgui.ved.incub;

import static org.toxsoft.core.tsgui.bricks.tin.tti.ITtiConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;

/**
 * Вспомогательные методы создания {@link ITinFieldInfo}.
 * <p>
 *
 * @author vs
 */
public class TtiUtils {

  /**
   * Создает копию описания поля с новым именем и описанием.
   *
   * @param aFieldInfo {@link ITinFieldInfo} - образец поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @return {@link ITinFieldInfo} - новое описание поля
   */
  public static ITinFieldInfo fieldInfo( ITinFieldInfo aFieldInfo, String aName, String aDescription ) {
    return TinFieldInfo.makeCopy( aFieldInfo, TSID_NAME, aName, TSID_DESCRIPTION, aDescription );
  }

  /**
   * Создает копию описания поля с новым идентификатором.
   *
   * @param aId String - ИД поля
   * @param aFieldInfo {@link ITinFieldInfo} - образец поля
   * @return {@link ITinFieldInfo} - новое описание поля
   */
  public static ITinFieldInfo fieldInfo( String aId, ITinFieldInfo aFieldInfo ) {
    return new TinFieldInfo( aId, aFieldInfo.params(), aFieldInfo.typeInfo() );
  }

  /**
   * Создает копию описания поля с новым идентификатором и именем.
   *
   * @param aId String - ИД поля
   * @param aFieldInfo {@link ITinFieldInfo} - образец поля
   * @param aName String - новое имя поля
   * @return {@link ITinFieldInfo} - новое описание поля
   */
  public static ITinFieldInfo fieldInfo( String aId, ITinFieldInfo aFieldInfo, String aName ) {
    IOptionSetEdit params = new OptionSet( aFieldInfo.params() );
    params.setStr( TSID_NAME, aName );
    return new TinFieldInfo( aId, params, aFieldInfo.typeInfo() );
  }

  /**
   * Создает копию описания поля с новым идентификатором, именем и описанием.
   *
   * @param aId String - ИД поля
   * @param aFieldInfo {@link ITinFieldInfo} - образец поля
   * @param aName String - новое имя поля
   * @param aDescription String - описание поля
   * @return {@link ITinFieldInfo} - новое описание поля
   */
  public static ITinFieldInfo fieldInfo( String aId, ITinFieldInfo aFieldInfo, String aName, String aDescription ) {
    IOptionSetEdit params = new OptionSet( aFieldInfo.params() );
    params.setStr( TSID_NAME, aName );
    params.setStr( TSID_DESCRIPTION, aDescription );
    return new TinFieldInfo( aId, params, aFieldInfo.typeInfo() );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#INTEGER}.
   *
   * @param aId String - ИД поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo intFieldInfo( String aId, String aName, String aDescription ) {
    return new TinFieldInfo( aId, TTI_AT_INTEGER, TSID_NAME, aName, TSID_DESCRIPTION, aDescription );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#INTEGER} со значением по умолчанию.
   *
   * @param aId String - ИД поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @param aDefaultValue int - значение по умолчанию
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo intFieldInfo( String aId, String aName, String aDescription, int aDefaultValue ) {
    return new TinFieldInfo( aId, TTI_AT_INTEGER, TSID_NAME, aName, TSID_DESCRIPTION, aDescription, //
        TSID_DEFAULT_VALUE, avInt( aDefaultValue ) );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#BOOLEAN}.
   *
   * @param aId String - ИД поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo booleanFieldInfo( String aId, String aName, String aDescription ) {
    return new TinFieldInfo( aId, TTI_AT_BOOLEAN, TSID_NAME, aName, TSID_DESCRIPTION, aDescription );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#BOOLEAN} со значением по умолчанию.
   *
   * @param aId String - ИД поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @param aDefaultValue boolean - значение по умолчанию
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo booleanFieldInfo( String aId, String aName, String aDescription, boolean aDefaultValue ) {
    return new TinFieldInfo( aId, TTI_AT_BOOLEAN, TSID_NAME, aName, TSID_DESCRIPTION, aDescription, //
        TSID_DEFAULT_VALUE, avBool( aDefaultValue ) );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#FLOATING}.
   *
   * @param aId String - ИД поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo doubleFieldInfo( String aId, String aName, String aDescription ) {
    return new TinFieldInfo( aId, TTI_AT_FLOATING, TSID_NAME, aName, TSID_DESCRIPTION, aDescription );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#FLOATING} со значением по умолчанию.
   *
   * @param aId String - ИД поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @param aDefaultValue double - значение по умолчанию
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo doubleFieldInfo( String aId, String aName, String aDescription, double aDefaultValue ) {
    return new TinFieldInfo( aId, TTI_AT_FLOATING, TSID_NAME, aName, TSID_DESCRIPTION, aDescription, //
        TSID_DEFAULT_VALUE, avFloat( aDefaultValue ) );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#STRING}.
   *
   * @param aId String - ИД поля
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo strFieldInfo( String aId, String aName, String aDescription ) {
    return new TinFieldInfo( aId, TTI_AT_STRING, TSID_NAME, aName, TSID_DESCRIPTION, aDescription );
  }

  /**
   * Создает описание поля для значения типа {@link EAtomicType#INTEGER}.
   *
   * @param aId String - ИД поля
   * @param aTypeInfo {@link ITinTypeInfo} - тип поля для инспектора свойств
   * @param aName String - имя поля
   * @param aDescription String - описание поля
   * @return {@link ITinFieldInfo} - описание поля
   */
  public static ITinFieldInfo typedFieldInfo( String aId, ITinTypeInfo aTypeInfo, String aName, String aDescription ) {
    return new TinFieldInfo( aId, aTypeInfo, TSID_NAME, aName, TSID_DESCRIPTION, aDescription );
  }

  /**
   * Создает копию описания поля не видимого для инспектора.
   *
   * @param aFiledInfo {@link ITinFieldInfo} - описание поля для инспектора свойств
   * @return {@link ITinFieldInfo} - описание скрытого поля
   */
  public static ITinFieldInfo createHidden( ITinFieldInfo aFiledInfo ) {
    return TinFieldInfo.makeCopy( aFiledInfo, ITinWidgetConstants.PRMID_IS_HIDDEN, AV_TRUE );
  }

  private TtiUtils() {
    // Запрет на создание экземпляров
  }

}
