package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;

/**
 * Defines the field containing single embeddable entity.
 * <p>
 * The the field of the &lt;T&gt; object ownes (controls lifecycle of) the embeddable entity modeled by M5-model
 * {@link #itemModel()}. Here Modown - MODelled OWNed object.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - embeddable entity type
 */
public interface IM5SingleModownFieldDef<T, V>
    extends IM5FieldDef<T, V>, IM5MixinModelledField<V>, IM5MixinSingleLinkField {

  // nop

}
