package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.m5.IM5Bunch;
import org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponent;
import org.toxsoft.core.tsgui.m5.gui.viewers.IM5TreeViewer;

/**
 * Details pane.
 * <p>
 * Displays details about selected item in collection viewer tree.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMpcDetailsPane<T>
    extends IMpcPaneBase<T> {

  /**
   * Implementation must display details about selected item.
   * <p>
   * Tree viewer {@link IMultiPaneComponent#tree()} as any {@link IM5TreeViewer} in general contains entity and grouping
   * nodes. If entity node (including grouping nodes made of entities) is selected {@link ITsNode#entity()
   * aSelectedNode.entity()} contains modelled entity of type &lt;T&gt; and <code>aValues</code> contains bunch of
   * entity field values. When grouping node is selected <code>aValues</code> = <code>null</code> and
   * <code>aSelectedNode</code> is the grouping node.
   * <p>
   * When selection changes to no item (eg. when collection becames empty) then both arguments are <code>null</code>.
   *
   * @param aSelectedNode {@link ITsNode} - selected node in tree, or <code>null</code> when no selected item in tree
   * @param aValues {@link IM5Bunch} - modelled entity field values or <code>null</code> if no entity node is selected
   */
  void setValues( ITsNode aSelectedNode, IM5Bunch<T> aValues );

  // TODO inplace editing?

}
