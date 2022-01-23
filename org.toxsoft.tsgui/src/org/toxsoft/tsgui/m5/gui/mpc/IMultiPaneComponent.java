package org.toxsoft.tsgui.m5.gui.mpc;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.tsgui.bricks.stdevents.*;
import org.toxsoft.tsgui.bricks.tstree.tmm.ITreeModeManager;
import org.toxsoft.tsgui.m5.gui.viewers.IM5TreeViewer;
import org.toxsoft.tsgui.m5.model.IM5ItemsProvider;
import org.toxsoft.tsgui.m5.model.IM5ModelRelated;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tsgui.widgets.TsComposite;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Multi-pane composite (MPC) - displays M5-modelled items with supplementary information and actions.
 * <p>
 * MPC consists of following panes:
 * <ul>
 * <li>tree viewer - main and mandatory panel displays the items as table or tree;</li>
 * <li>toolbar - (optional) contains actions as buttons to control display and MPC behaveuor;</li>
 * <li>filter pane - (optional) controls which items will be displayed in tree viewer;</li>
 * <li>detail pane - (optional) displays more datails of item selected in tree viewer;</li>
 * <li>summary pane - (options) displayes summaty information of all and/or filtered items.</li>
 * </ul>
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMultiPaneComponent<T>
    extends //
    IM5ModelRelated<T>, //
    ITsSelectionProvider<T>, //
    ITsDoubleClickEventProducer<T>, //
    ITsKeyDownEventProducer, //
    ILazyControl<TsComposite>, //
    ITsGuiContextable {

  /**
   * Returns a reference to a tree showing the elements.
   *
   * @return {@link IM5TreeViewer} - the tree
   */
  IM5TreeViewer<T> tree();

  /**
   * Populates the viewer with items from an items provider (actually updates the list).
   *
   * @param aToSelect &lt;T&gt; - item to be selected after or <code>null</code> for no selection
   */
  void fillViewer( T aToSelect );

  /**
   * Updates the contents of the tree component.
   * <p>
   * This method is equal to {@link #fillViewer(Object)} where item to be selected is currently selected item.
   */
  void refresh();

  /**
   * Returns the manager of grouping modes.
   *
   * @return {@link ITreeModeManager} - tree grouping modes manager
   */
  ITreeModeManager<T> treeModeManager();

  /**
   * Determines if component content editing is allowed right now.
   *
   * @return boolean - edit mode flag
   */
  boolean isEditable();

  /**
   * Toggles component content edit mode.
   *
   * @param aEditable boolean - edit mode flag
   */
  void setEditable( boolean aEditable );

  /**
   * Returns the items provider.
   * <p>
   * By default, when provider not set, returns {@link IM5ItemsProvider#EMPTY}.
   *
   * @return {@link IM5ItemsProvider} - the items provider never is <code>null</code>
   */
  IM5ItemsProvider<T> itemsProvider();

  /**
   * Sets the items provider.
   * <p>
   * Note: changing items provider does not leads to the tree update - {@link #refresh()} must be called.
   *
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setItemProvider( IM5ItemsProvider<T> aItemsProvider );

}
