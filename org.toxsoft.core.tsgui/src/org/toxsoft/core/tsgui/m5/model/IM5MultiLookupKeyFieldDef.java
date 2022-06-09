package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.model.helpers.*;

/**
 * Описание поля, ссылающегося на коллекцию справочных (lookup) объектов.
 *
 * @author goga
 * @param <T> - тип моделированного объекта
 * @param <V> - тип справочного объекта
 */
public interface IM5MultiLookupKeyFieldDef<T, V>
    extends IM5MultiLookupFieldDef<T, V>, IM5MixinKeyLookupField<V> {

  // nop

}
