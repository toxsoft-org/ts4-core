package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Описание поля, которое содержит коллекцию встраиваемых объектов.
 * <p>
 * Поле является владельцем объекта, которые сами описаны моделью {@link #itemModel()}. Отсюда и название Modown -
 * MODelled OWNed object.
 *
 * @author hazard157
 * @param <T> - тип моделированного объекта
 * @param <V> - тип элемента списка (а не тип коллекции!)
 */
public interface IM5MultiModownFieldDef<T, V>
    extends IM5FieldDef<T, IList<V>>, IM5MixinMultiLinkField, IM5MixinModelledField<V> {

  // nop

}
