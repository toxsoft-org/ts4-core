package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;

/**
 * Описание поля, которое содержит один встраиваемый объект.
 * <p>
 * Поле является владельцем объекта, которые сами описаны моделью {@link #itemModel()}. Отсюда и название Modown -
 * MODelled OWNed object.
 *
 * @author hazard157
 * @param <T> - тип моделированного объекта
 * @param <V> - тип объекта-ссылки
 */
public interface IM5SingleModownFieldDef<T, V>
    extends IM5FieldDef<T, V>, IM5MixinModelledField<V>, IM5MixinSingleLinkField {

  // nop

}
