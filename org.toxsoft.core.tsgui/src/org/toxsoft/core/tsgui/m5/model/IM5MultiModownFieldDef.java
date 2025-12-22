package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Description of the field that contains the collection of embeddable (modown) objects.
 * <p>
 * The field is the owner of the objects that are themselves described by the {@link #itemModel()} model. Hence the name
 * Modown - MODeled and OWNed objects.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - list element type (not collection type!)
 */
public interface IM5MultiModownFieldDef<T, V>
    extends IM5FieldDef<T, IList<V>>, IM5MixinMultiLinkField, IM5MixinModelledField<V> {

  // nop

}
