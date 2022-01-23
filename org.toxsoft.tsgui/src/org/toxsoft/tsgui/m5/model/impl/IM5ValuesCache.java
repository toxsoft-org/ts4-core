package org.toxsoft.tsgui.m5.model.impl;

import org.toxsoft.tsgui.m5.IM5Bunch;
import org.toxsoft.tslib.coll.basis.ITsClearable;

/**
 * {@link IM5Bunch} caching starategy for {@link M5Model}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5ValuesCache<T>
    extends ITsClearable {

  /**
   * Returns the bunch for specified entity.
   * <p>
   * Method alwayes returns the bunch for specified entity. Depending on implementation method may create new instance
   * and cache it for first time and for following calls return the bunch from the cache.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   * @return {@link IM5Bunch} - cached bunch of entity field values
   */
  IM5Bunch<T> getValues( T aEntity );

  /**
   * Finds cached bunch if any.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   * @return {@link IM5Bunch} - the cached bunch or <code>null</code> if it was not cached
   */
  IM5Bunch<T> findCached( T aEntity );

  /**
   * Removes the bunch from the cache.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   */
  void remove( T aEntity );

  /**
   * Clears the cache.
   */
  @Override
  void clear();

}
