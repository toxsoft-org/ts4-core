package org.toxsoft.core.tslib.utils;

import org.toxsoft.core.tslib.coll.*;

/**
 * Arbitrary elements provider.
 *
 * @author hazard157
 * @param <T> - type of provided elements
 */
public interface ITsItemsProvider<T> {

  /**
   * Always empty list provider.
   */
  @SuppressWarnings( "rawtypes" )
  ITsItemsProvider EMPTY = () -> IList.EMPTY;

  /**
   * Returns elements.
   * <p>
   * In general, subsequent calls may return different list of elements.
   *
   * @return {@link IList}&lt;T&gt; - the list of provided elements may be empty but not <code>null</code>
   */
  IList<T> listItems();

}
