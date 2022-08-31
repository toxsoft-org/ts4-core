package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.multilookup.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IM5MultiLookupFieldDef}.
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 * @param <V> - тип справочного объекта
 */
public class M5MultiLookupKeyFieldDef<T, V>
    extends M5MultiLookupFieldDef<T, V>
    implements IM5MultiLookupKeyFieldDef<T, V> {

  private final String   keyFieldId;
  private final Class<?> keyType;

  /**
   * Конструктор.
   * <p>
   * По умолчанию количество связанных объекто не ограничено, то есть {@link #maxCount()} = 0 и {@link #isExactCount()}
   * = <code>false</code>.
   *
   * @param aId String - идентификатор поля (ИД-путь)
   * @param aItemModelId String - идентификатор модели {@link #itemModel()}
   * @param aKeyFieldId String - идентификатор ключевого поля в модели справочных элементов
   * @param aKeyType {@link Class} - тип (класс) ключа
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-пут
   * @throws TsIllegalArgumentRtException aItemModelId не ИД-путь
   */
  public M5MultiLookupKeyFieldDef( String aId, String aItemModelId, String aKeyFieldId, Class<?> aKeyType ) {
    super( aId, aItemModelId );
    keyFieldId = TsErrorUtils.checkNonEmpty( aKeyFieldId );
    keyType = TsNullArgumentRtException.checkNull( aKeyType );
    setDefaultValue( IList.EMPTY );
    setValedEditor( ValedMultiLookupEditor.FACTORY_NAME );
  }

  // ------------------------------------------------------------------------------------
  // IM5MultiLookupKeyFieldDef
  //

  @Override
  public String keyFieldId() {
    return keyFieldId;
  }

  @Override
  public Class<?> keyType() {
    return keyType;
  }

}
