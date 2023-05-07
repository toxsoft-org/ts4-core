package org.toxsoft.core.tsgui.m5.model;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Items provider for M5 GUI components.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5ItemsProvider<T>
    extends ITsItemsProvider<T>, IGenericChangeEventCapable {

  /**
   * Always empty list provider with no reorder support.
   */
  @SuppressWarnings( "rawtypes" )
  IM5ItemsProvider EMPTY = new InternalEmptyM5ItemsProvider();

  @Override
  IList<T> listItems();

  /**
   * Determines if items reordering is supported.
   *
   * @return boolean - if {@link #reorderer()} != null
   */
  default boolean isReorderSupported() {
    return reorderer() != null;
  }

  /**
   * Returns the items list reorderer.
   *
   * @return {@link IListReorderer} - the items list reorderer or <code>null</code> if reorder is not supported
   */
  default IListReorderer<T> reorderer() {
    return null;
  }

  /**
   * Forces {@link #genericChangeEventer()} to fire an event if supported.
   * <p>
   * Default implementation does nothing.
   */
  default void informOnItemsListChange() {
    // nop
  }

  @Override
  default IGenericChangeEventer genericChangeEventer() {
    return NoneGenericChangeEventer.INSTANCE;
  }

}

@SuppressWarnings( "rawtypes" )
class InternalEmptyM5ItemsProvider
    implements IM5ItemsProvider {

  @Override
  public IList listItems() {
    return IList.EMPTY;
  }

  @Override
  public boolean isReorderSupported() {
    return false;
  }

  @Override
  public IListReorderer reorderer() {
    return null;
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return NoneGenericChangeEventer.INSTANCE;
  }

}
