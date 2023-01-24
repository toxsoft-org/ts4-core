package org.toxsoft.core.tsgui.panels.generic;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Generic items collection viewer / editor panel.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericCollPanel<T>
    extends IGenericContentPanel, ITsSelectionProvider<T>, ITsDoubleClickEventProducer<T> {

  /**
   * Returns the list of items to display.
   * <p>
   * Not all items may be displayed in panel, for example, if panel contains filter pane to select subset of items.
   *
   * @return {@link IList}&lt;T&gt; - all items to how in panel
   */
  IList<T> items();

  /**
   * Refreshes panel content.
   * <p>
   * Usually items are hold in come cache of collection displaying widget. Refresh will refill cache from external
   * provider such as {@link ITsItemsProvider}.
   */
  void refresh();

  /**
   * Returns the items checking means.
   * <p>
   * For panels without checks support {@link ITsCheckSupport#isChecksSupported()} returns <code>false</code>.
   *
   * @return {@link ITsCheckSupport} - items checking helper
   */
  ITsCheckSupport<T> checkSupport();

}
