package org.toxsoft.tsgui.m5_3;

import org.toxsoft.tsgui.utils.ITsVisualsProvider;

/**
 * How to extract value from entity and display it.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 * @param <V> - field value type
 */
public interface IM5Getter<T, V>
    extends ITsVisualsProvider<T> {

  /**
   * Returns the field value of the specified entity.
   * <p>
   * If <code>aEntity</code> is <code>null</code>, depending on implementation method may return some value or even
   * throw an exception.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   * @return &lt;&gt; - the field value, may be <code>null</code>
   */
  V getValue( T aEntity );

}
