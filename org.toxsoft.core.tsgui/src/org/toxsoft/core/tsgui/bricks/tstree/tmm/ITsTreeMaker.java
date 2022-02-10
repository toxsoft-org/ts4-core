package org.toxsoft.core.tsgui.bricks.tstree.tmm;

import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * A strategy to build a hierarchical tree structure from a flat list.
 * <p>
 * This interface is not used in this package. Instead it is desgined as hint to the implementers. The meaning of this
 * interface is that a tree is built from entities of the same type &lt;T&gt; Some {@link ITsNode} nodes can be grouping
 * (service) ones, for tree building, and may not contain objects as {@link ITsNode#entity()}. Grouping nodes can be
 * distinguished from entity nodes by the {@link #isItemNode(ITsNode)} method.
 * <p>
 * This interface allows to create part of the tree (the subtree) or the whole tree. When creating whole tree
 * {@link ITsBasicTreeViewer} instance must be supplied as <code>aRootNode</code> argument to the method
 * {@link #makeRoots(ITsNode, IList)}.
 *
 * @author hazard157
 * @param <T> - entity type
 */
public interface ITsTreeMaker<T> {

  /**
   * Creates root nodes and subtress from list of modelled entities.
   * <p>
   * While returned roots and subtrees must contain all items from the argument <code>aItems</code> some nodes (usually
   * grouping nodes) may not contain modelled entities as {@link ITsNode#entity()}. Implementation must distinguish such
   * nodes by the method {@link #isItemNode(ITsNode)}.
   * <p>
   * Returned root nodes must be childs of <code>aRootNode</code> argument. M5-viewer will set returned list as roots by
   * the method {@link ITsTreeViewer#setRootNodes(ITsCollection)}.
   *
   * @param aRootNode {@link ITsNode} - the invisible root of the tree
   * @param aItems {@link ITsCollection}&lt;T&gt; - list of items to be shown in viewer
   * @return IList&lt;T&gt; - created root nodes with childs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<ITsNode> makeRoots( ITsNode aRootNode, IList<T> aItems );

  /**
   * Determines if spcified node containes modelled entity.
   *
   * @param aNode {@link ITsNode} - the node to check
   * @return boolean - <code>true</code> if {@link ITsNode#entity()} is modelled item, <code>false</code> - this is
   *         service (usually grouping) node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isItemNode( ITsNode aNode );

}
