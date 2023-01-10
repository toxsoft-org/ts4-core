package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Описание поля, ссылающегося на коллекцию справочных (lookup) объектов.
 *
 * @author hazard157
 * @param <T> - тип моделированного объекта
 * @param <V> - тип справочного объекта
 */
public interface IM5MultiLookupFieldDef<T, V>
    extends IM5FieldDef<T, IList<V>>, IM5MixinLookupField<V>, IM5MixinMultiLinkField {

  // nop

}
