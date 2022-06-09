package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.singlemodown.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Реализация {@link IM5SingleModownFieldDef}.
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 * @param <V> - тип объекта-ссылки
 */
public class M5SingleModownFieldDef<T, V>
    extends M5SingleLinkFieldDefBase<T, V>
    implements IM5SingleModownFieldDef<T, V> {

  /**
   * Конструктор.
   * <p>
   * По умолчанию {@link #canUserSelectNull()} = <code>false</code>.
   *
   * @param aId String - идентификатор поля (ИД-путь)
   * @param aItemModelId String - идентификатор модели {@link #itemModel()}
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aId не ИД-пут
   * @throws TsIllegalArgumentRtException aItemModelId не ИД-путь
   */
  public M5SingleModownFieldDef( String aId, String aItemModelId ) {
    super( aId, aItemModelId );
    setValedEditor( ValedSingleModownEditor.FACTORY_NAME );
  }

}
