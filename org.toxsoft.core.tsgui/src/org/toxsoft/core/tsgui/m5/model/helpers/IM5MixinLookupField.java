package org.toxsoft.core.tsgui.m5.model.helpers;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Mix-in interface of fields containing either keys or lookup elements themselves.
 *
 * @author hazard157
 * @param <V> - the type of modeled entity references by or contained in the field
 */
public interface IM5MixinLookupField<V> {

  /**
   * Returns provider of the lookup items.
   * <p>
   * Some implementations may throw {@link TsIllegalStateRtException} if the lookup provider was not set yet.
   *
   * @return {@link IM5LookupProvider} - lookup items provider, never is <code>null</code>
   */
  IM5LookupProvider<V> lookupProvider();

}
