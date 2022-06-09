package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.model.helpers.*;

/**
 * Описание поля, ссылающегося один справочный (lookup) объект.
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 * @param <V> - тип справочного объекта
 */
public interface IM5SingleLookupKeyFieldDef<T, V>
    extends IM5SingleLookupFieldDef<T, V>, IM5MixinKeyLookupField<V> {

  // nop

}
