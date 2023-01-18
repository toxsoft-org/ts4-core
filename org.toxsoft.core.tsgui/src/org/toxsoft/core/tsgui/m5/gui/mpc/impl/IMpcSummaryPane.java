package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Summary pane.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMpcSummaryPane<T>
    extends IMpcPaneBase<T> {

  /**
   * Implementation must update panel content.
   * <p>
   * This method is called every time when displayed collection or filter or selection changes.
   *
   * @param aSelectedNode {@link ITsNode} - selected node or <code>null</code>
   * @param aSelEntity &lt;T&gt; - selected entity or <code>null</code>
   * @param aAllItems {@link IList}&lt;T&gt; - all items in tree (include hidden by the filter)
   * @param aFilteredItems {@link IList}&lt;T&gt; - displayed items in tree
   */
  void updateSummary( ITsNode aSelectedNode, T aSelEntity, IList<T> aAllItems, IList<T> aFilteredItems );

}
