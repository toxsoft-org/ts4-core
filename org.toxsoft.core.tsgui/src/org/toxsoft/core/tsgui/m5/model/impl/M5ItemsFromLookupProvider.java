package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Implements {@link IM5ItemsProvider} as wrapper over {@link IM5LookupProvider}.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5ItemsFromLookupProvider<T>
    implements IM5ItemsProvider<T> {

  private IM5LookupProvider<T> lookupProvider;

  /**
   * Constructor.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - the wrapped provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5ItemsFromLookupProvider( IM5LookupProvider<T> aLookupProvider ) {
    TsNullArgumentRtException.checkNull( aLookupProvider );
    lookupProvider = aLookupProvider;
  }

  // ------------------------------------------------------------------------------------
  // IM5ItemsProvider
  //

  @Override
  public IList<T> listItems() {
    return lookupProvider.listItems();
  }

}
