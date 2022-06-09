package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Provides objects that may be linked to the lookup fields.
 * <p>
 * Implements {@link ITsNameProvider} supplying texts and icons for lookup objects.
 *
 * @author hazard157
 * @param <V> - lookup objects type
 */
public interface IM5LookupProvider<V>
    extends ITsNameProvider<V> {

  /**
   * Returns the lookup objects.
   *
   * @return {@link IList}&lt;V&gt; - the list of lookup objects never is <code>null</code>
   */
  IList<V> listItems();

}
