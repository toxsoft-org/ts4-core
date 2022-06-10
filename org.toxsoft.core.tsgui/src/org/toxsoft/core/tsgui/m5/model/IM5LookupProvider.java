package org.toxsoft.core.tsgui.m5.model;

import java.io.*;

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
   * An emty list provider.
   */
  @SuppressWarnings( "rawtypes" )
  IM5LookupProvider EMPTY = new InternalEmptyLookupProvider();

  /**
   * Returns the lookup objects.
   *
   * @return {@link IList}&lt;V&gt; - the list of lookup objects never is <code>null</code>
   */
  IList<V> listItems();

}

class InternalEmptyLookupProvider
    implements IM5LookupProvider<Object>, Serializable {

  private static final long serialVersionUID = 157157L;

  /**
   * Correctly deserializes {@link IM5LookupProvider#EMPTY}.
   *
   * @return Object - always {@link IM5LookupProvider#EMPTY}
   * @throws ObjectStreamException just declaration is never thrown
   */
  @SuppressWarnings( { "static-method" } )
  private Object readResolve()
      throws ObjectStreamException {
    return IM5LookupProvider.EMPTY;
  }

  @Override
  public String getName( Object aItem ) {
    return DEFAULT.getName( aItem );
  }

  @Override
  public IList<Object> listItems() {
    return IList.EMPTY;
  }

}
