package org.toxsoft.core.tsgui.panels.generic;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Generic panel to select (choose) an item.
 * <p>
 * There are several ways to implement items selection panel: collection viewer (when there is a foreseeable quantity
 * items), browser (eg. to select URL of the Internet resource), even a text edit line to directly specify the item.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericSelectorPanel<T>
    extends IGenericContentPanel, ITsSelectionProvider<T> {

  /**
   * Refreshes panel content.
   * <p>
   * For example, when items are hold in come cache of collection displaying widget refresh will refill cache from
   * external provider such as {@link ITsItemsProvider}.
   */
  void refresh();

}
