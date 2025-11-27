package org.toxsoft.core.tsgui.m5.gui.panels;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to select several items from the provided lookup items list.
 * <p>
 * There are two panel to select item(s) from the lookup list - {@link IM5SingleLookupPanel} and
 * {@link IM5MultiLookupPanel}. Although similar, the two panels behave differently:
 * <ul>
 * <li>in <b>single item</b> selection panel {@link IM5SingleLookupPanel#items()} denotes list of all <i>provided lookup
 * items</i>, one <i>selected item</i> is set by {@link IM5SingleLookupPanel#setSelectedItem(Object)} and retrieved by
 * {@link IM5SingleLookupPanel#selectedItem()};</li>
 * <li>in <b>multi items</b> selection panel {@link IM5MultiLookupPanel#items()} denotes to the <i>selected items</i>
 * and can be changed by {@link IM5MultiLookupPanel#setItems(IList)} while list of all <i>provided lookup items</i> may
 * be retrieved with {@link IM5MultiLookupPanel#lookupProvider()}.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5MultiLookupPanel<T>
    extends IGenericCollPanel<T>, IM5PanelBase<T> {

  /**
   * Returns selected items.
   *
   * @return {@link IList}&lt;T&gt; - selected items list
   */
  @Override
  IList<T> items();

  /**
   * Sets the list of items as selected items list.
   * <p>
   * Items not contained in provided items list are ignored.
   *
   * @param aItems {@link IList}&lt;T&gt; - selected items list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setItems( IList<T> aItems );

  /**
   * Returns the lookup items provider.
   *
   * @return {@link IM5LookupProvider} - the lookup items provider
   */
  IM5LookupProvider<T> lookupProvider();

  /**
   * Sets the lookup items provider.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup items provider or <code>null</code> for empty one
   */
  void setLookupProvider( IM5LookupProvider<T> aLookupProvider );

  /**
   * Clears selected items list.
   */
  default void deselectAll() {
    setItems( IList.EMPTY );
  }

  /**
   * Sets all lookp items as selected.
   */
  default void selectAll() {
    setItems( lookupProvider().listItems() );
  }

}
