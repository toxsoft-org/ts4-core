package org.toxsoft.core.tslib.utils;

import org.toxsoft.core.tslib.coll.IList;

/**
 * Arbitrary elements provider.
 *
 * @author hazard157
 * @param <T> - type of provided elemens
 */
public interface ITsItemsProvider<T> {

  /**
   * Always empty list provider.
   */
  @SuppressWarnings( "rawtypes" )
  ITsItemsProvider EMPTY = new InternalEmptyItemsProvider();

  /**
   * Returns elements.
   * <p>
   * In general, subsequent calls may return different list of elements.
   *
   * @return {@link IList}&lt;T&gt; - the list of provideed elements may be empty but not <code>null</code>
   */
  IList<T> listItems();

}

@SuppressWarnings( "rawtypes" )
class InternalEmptyItemsProvider
    implements ITsItemsProvider {

  @Override
  public IList listItems() {
    return IList.EMPTY;
  }

}
