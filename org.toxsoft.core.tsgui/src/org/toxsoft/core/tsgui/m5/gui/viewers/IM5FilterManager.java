package org.toxsoft.core.tsgui.m5.gui.viewers;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Manages visible items filtering in {@link IM5CollectionViewer} implementations.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5FilterManager<T> {

  /**
   * Determines if filter is applied to the viewer.
   *
   * @return boolean - <code>true</code> if {@link #getFilter()} is not an {@link ITsFilter#ALL}
   */
  boolean isFiltered();

  /**
   * Returns the filtered (currently displayed) elements.
   * <p>
   * Returns the subset of {@link IM5CollectionViewer#items()}.
   *
   * @return {@link IList}&lt;T&gt; - filtered elements
   */
  IList<T> items();

  /**
   * Sets the filter to selectet displayed elements from {@link IM5CollectionViewer#items()}.
   * <p>
   * To turn filtering off use argument {@link ITsFilter#ALL}
   *
   * @param aFilter {@link ITsFilter} - the filter or {@link ITsFilter#ALL} to turn filtering off
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setFilter( ITsFilter<T> aFilter );

  /**
   * Returns the current filter.
   *
   * @return {@link ITsFilter} - the current filter
   */
  ITsFilter<T> getFilter();

}
