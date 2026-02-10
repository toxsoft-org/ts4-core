package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Field description containing the collection of references to the existing (lookup) objects.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - list element type (not collection type!)
 */
public interface IM5MultiLookupFieldDef<T, V>
    extends IM5FieldDef<T, IList<V>>, IM5MixinLookupField<V>, IM5MixinMultiLinkField {

  // nop

}
