package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;

/**
 * Defined field containing single value found in provided lookup list.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 * @param <V> - lookup objects type that is field value type
 */
public interface IM5SingleLookupFieldDef<T, V>
    extends IM5FieldDef<T, V>, IM5MixinModelledField<V>, IM5MixinLookupField<V>, IM5MixinSingleLinkField {

  // nop

}
