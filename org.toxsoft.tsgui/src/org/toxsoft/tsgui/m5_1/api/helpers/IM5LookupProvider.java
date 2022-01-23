package org.toxsoft.tsgui.m5_1.api.helpers;

import org.toxsoft.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.tslib.bricks.filter.ITsCombiFilterParams;
import org.toxsoft.tslib.coll.IList;

/**
 * Provides an elements for various lookups.
 * <p>
 * Implements {@link ITsVisualsProvider} used for provided elements display.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5LookupProvider<T>
    extends ITsVisualsProvider<T> {

  /**
   * Returns the list of all lookup elements.
   * <p>
   * FIXME ???
   * <p>
   * Аргумент позволяет отобрать подмножество из всех возможных справочных объектов. Например, при выборе человека,
   * можно отобрать только женщин, или только совершеннолетных и т.п.
   *
   * @param aFilterParams {@link ITsCombiFilterParams} - параметры фильтра отбора элементов, может быть
   *          <code>null</code>
   * @return {@link IList}&lt;T&gt; - список справочных объектов
   */
  IList<T> listItems( ITsCombiFilterParams aFilterParams );

}
