package org.toxsoft.core.tsgui.bricks.qtree.tmm;

import org.toxsoft.core.tsgui.bricks.tstree.*;
import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * A strategy to build a hierarchical tree structure from a flat list.
 * <p>
 * This interface is not used in this package. Instead it is designed as a hint to the implementers. The meaning of this
 * interface is that a tree is built from entities of the same type &lt;T&gt; Some {@link IQNode} nodes can be grouping
 * (service) ones for tree building and may not contain objects as {@link IQNode#entity()}. Grouping nodes can be
 * distinguished from entity nodes by the {@link #isItemNode(IQNode)} method.
 * <p>
 * This interface allows to create part of the tree (the subtree) or the whole tree. When creating whole tree
 * {@link ITsBasicTreeViewer} instance must be supplied as <code>aRootNode</code> argument to the method
 * {@link #makeRoots(IQNode, IList)}.
 *
 * @author hazard157
 * @param <T> - entity type
 */
public interface IQTreeMaker<T> {

  /**
   * Creates root nodes and sub-trees from list of entities.
   * <p>
   * While returned roots and subtrees must contain all items from the argument <code>aItems</code> some nodes (usually
   * grouping nodes) may not contain modeled entities as {@link IQNode#entity()}. Implementation must distinguish such
   * nodes by the method {@link #isItemNode(IQNode)}.
   * <p>
   * Returned root nodes must be childs of <code>aRootNode</code> argument. Viewer will set returned list as roots by
   * the method {@link ITsTreeViewer#setRootNodes(ITsCollection)}.
   *
   * @param aRootNode {@link IQNode} - the invisible root of the tree
   * @param aItems {@link ITsCollection}&lt;T&gt; - list of items to be shown in viewer
   * @return IList&lt;T&gt; - created root nodes with childs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<IQNode> makeRoots( IQNode aRootNode, IList<T> aItems );

  /**
   * Determines if specified node contains entity.
   *
   * @param aNode {@link IQNode} - the node to check
   * @return boolean - <code>true</code> if {@link IQNode#entity()} is modeled item, <code>false</code> - this is
   *         service (usually grouping) node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isItemNode( IQNode aNode );

}
