package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.notifier.*;
import org.toxsoft.core.tslib.coll.notifier.impl.*;

/**
 * {@link IM5ItemsProvider} implementation as items container.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5DefaultItemsProvider<T>
    implements IM5ItemsProvider<T> {

  final GenericChangeEventer         eventer;
  private final INotifierListEdit<T> items;
  private final IListReorderer<T>    listReorderer;

  /**
   * Конструктор.
   */
  public M5DefaultItemsProvider() {
    eventer = new GenericChangeEventer( this );
    items = new NotifierListEditWrapper<>( new ElemLinkedBundleList<T>() );
    items.addCollectionChangeListener( ( aSource, aOp, aItem ) -> eventer.fireChangeEvent() );
    listReorderer = new ListReorderer<>( items );
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // ITsItemsProvider
  //

  @Override
  public IList<T> listItems() {
    return items;
  }

  // ------------------------------------------------------------------------------------
  // IM5ItemsProvider
  //

  @Override
  public boolean isReorderSupported() {
    return true;
  }

  @Override
  public IListReorderer<T> reorderer() {
    return listReorderer;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns items container.
   *
   * @return {@link IListEdit}&lt;T&gt; - editable list of items
   */
  public IListEdit<T> items() {
    return items;
  }

}
