package org.toxsoft.tsgui.utils.checkcoll;

/**
 * Mixin interface aggregating {@link ITsCheckSupport} to the collection viewer.
 *
 * @author hazard157
 * @param <T> - type of elements in collection viewer
 */
public interface ITsCheckSupportable<T> {

  /**
   * Returns the checks support.
   *
   * @return {@link ITsCheckSupport}&lt;T&gt; - the checks support
   */
  ITsCheckSupport<T> checks();

}
