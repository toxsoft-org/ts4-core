package org.toxsoft.core.tslib.bricks.qnodes.helpers;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Tree of QNodes visitor interface.
 * <p>
 * This is a helper interface. Helper interface is not used by QNodes package, rather it is kind of design pattern how
 * to implement frequently needed functionality related to QNodes.
 *
 * @author hazard157
 */
public sealed interface IQNodeVisitor permits AbstractQNodeVisitor {

  /**
   * Iterates over subtree starting from root <code>aNode</code> visiting each node by {@link #visitNode(IQNode)}.
   *
   * @param aSubtreeRoot {@link IQNode} - root of subtree to be visited
   * @return boolean - visiting cancellation flag<br>
   *         <b>true</b> - stop visiting process immediately;<br>
   *         <b>false</b> - continue visiting.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean visitSubtree( IQNode aSubtreeRoot );

  /**
   * Visits the specified node without it's subtree.
   * <p>
   * Return value is used by {@link #visitSubtree(IQNode)}.
   *
   * @param aNode {@link IQNode} - the visited node
   * @return boolean - visiting cancellation flag<br>
   *         <b>true</b> - stop visiting process immediately;<br>
   *         <b>false</b> - continue visiting.
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean visitNode( IQNode aNode );

}
