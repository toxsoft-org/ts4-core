package org.toxsoft.core.tsgui.bricks.qtree;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Control console for visual representation of the tree.
 *
 * @author hazard157
 */
public interface IQTreeConsole {

  /**
   * Expands subtree rooted at the given node to the given level.
   * <p>
   * Expands all ancestors of the given element or tree path so that the given element becomes visible in this viewer's
   * tree control, and then expands the subtree rooted at the given element to the given level.
   *
   * @param aNode {@link IQNode} - the node to expand
   * @param aLevel int - non-negative level, or <0 to expand all levels of the subtree
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void expandNodeTo( IQNode aNode, int aLevel );

  /**
   * Collapses the subtree rooted at the given node to the given level.
   *
   * @param aNode {@link IQNode} - node to collapse
   * @param aLevel int - non-negative level, or <0 to collase all levels of the subtree
   */
  void collapseNode( IQNode aNode, int aLevel );

  /**
   * Refreshes subtree rooted at the given node.
   * <p>
   * Recreates child nodes so handles strctural changes in the tree model.
   *
   * @param aNode {@link IQNode} - root node of the subtree
   */
  void refresh( IQNode aNode );

  /**
   * Updates visual presentation of the given node without handling structural changes.
   *
   * @param aNode {@link IQNode} - node to update
   */
  void update( Object aNode );

  /**
   * Returns the currently selected single node.
   * <p>
   * If node node is selected or twoor more nodes are selected then returns <code>null</code>.
   *
   * @return {@link IQNode} - single selected node or <code>null</code>
   */
  IQNode selectedNode();

  /**
   * returns all selected nodes.
   *
   * @return {@link IList}&lt;{@link IQNode}&gt; - selected nodes, never is <code>null</code>
   */
  IList<IQNode> selectedNodes();

  /**
   * Selects the only node, all other nodes beace unseleted.
   * <p>
   * Specifiing <code>null</code> removes all selection
   *
   * @param aNode {@link IQNode} - the node to select or <code>null</code>
   */
  void setSelectedNode( IQNode aNode );

  /**
   * Sets multiple selected nodes.
   * <p>
   * All other nodes became unselected.
   *
   * @param aNodes {@link IList}&lt;{@link IQNode}&gt; - nodes to select
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void setSelectedNodes( IList<IQNode> aNodes );

}
