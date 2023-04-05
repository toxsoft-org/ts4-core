package org.toxsoft.core.tslib.bricks.qnodes.helpers;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * Interface of external nodes contributor.
 * <p>
 * This interface is designed to be used by SPI (Service provider interface) based frameworks of QNode trees.
 * <p>
 * This is a helper interface. Helper interface is not used by QNodes package, rather it is kind of design pattern how
 * to implement frequently needed functionality related to QNodes.
 *
 * @author hazard157
 */
public interface IQNodeChildsContributor {

  /**
   * Creates child nodes for a given node.
   * <p>
   * Implementation must not remove or reorder existing content of <code>aChildNodes</code>. Only adding newly created
   * nodes is allowed. Created nodes must have the given node as parent, that is the equality {@link IQNode#parent()} =
   * <code>aNode</code> must be true.
   *
   * @param aNode {@link AbstractQNode} - the given node
   * @param aChildNodes {@link IListEdit}&lt;{@link AbstractQNode}&gt; - the list for the created nodes to be added to
   */
  void fillNodeChilds( AbstractQNode<?> aNode, IListEdit<AbstractQNode<?>> aChildNodes );

}
