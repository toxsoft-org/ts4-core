package org.toxsoft.core.tsgui.m5.gui.viewers;

import org.eclipse.swt.widgets.Tree;
import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tsgui.bricks.tstree.tmm.ITsTreeMaker;
import org.toxsoft.core.tsgui.utils.jface.ViewerPaintHelper;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;

/**
 * M5 tree viewer.
 * <p>
 * Tree viewer unlike {@link IM5TableViewer} contains not entities &lt;T&gt; but nodes {@link ITsNode} which themself
 * contains modelled entities as described in comments to the method {@link ITsTreeMaker#makeRoots(ITsNode, IList)}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5TreeViewer<T>
    extends IM5CollectionViewer<T> {

  /**
   * Returns current tree hierarchy building strategy.
   * <p>
   * By default (if not set by method {@link #setTreeMaker(ITsTreeMaker)}) all items are placed in the root of tree.
   * That is, in fact, the tree is shown as a table.
   *
   * @return {@link ITsTreeMaker} - tree hierarchy building strategy, never is <code>null</code>
   */
  ITsTreeMaker<T> treeMaker();

  /**
   * Sets tree hierarchy building strategy and rebuilds content.
   *
   * @param aTreeMaker {@link ITsTreeMaker} - the strategy or <code>null</code> for default (table) strategy
   */
  void setTreeMaker( ITsTreeMaker<T> aTreeMaker );

  /**
   * Returns the only invisible root of the tree.
   * <p>
   * All visibly root nodes must be childs of invisible root node.
   *
   * @return {@link ITsNode} - the invisible single root node
   */
  ITsNode rootNode();

  /**
   * Returns tree visualisation control console.
   * <p>
   * Note: items in console (like returned by {@link ITsTreeViewerConsole#selectedNode()} are {@link ITsNode}.
   *
   * @return {@link ITsTreeViewerConsole} - tree visualisation control console
   * @throws TsIllegalStateRtException tree veiwer SWT control was not created yet
   */
  ITsTreeViewerConsole console();

  /**
   * Sets tree rows painting helper (user drawn cells).
   * <p>
   * Warning: this method works only in RCP mode not in RAP.
   *
   * @param aHelper {@link ViewerPaintHelper} - painter or <code>null</code> for default drawing
   * @throws TsIllegalStateRtException lazy SWT control was not created yet
   */
  void setTreePaintHelper( ViewerPaintHelper<Tree> aHelper );

}
