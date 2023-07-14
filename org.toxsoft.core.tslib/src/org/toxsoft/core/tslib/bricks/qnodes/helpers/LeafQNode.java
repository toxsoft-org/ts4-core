package org.toxsoft.core.tslib.bricks.qnodes.helpers;

import static org.toxsoft.core.tslib.bricks.qnodes.helpers.ITsResources.*;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Leaf node default implementation.
 * <p>
 * This is a helper class. Helper class is not used by QNodes package, rather it is kind of design pattern how to
 * implement frequently needed functionality related to QNodes.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public class LeafQNode<T>
    extends AbstractQNode<T> {

  /**
   * Constructor.
   *
   * @param aId String - node ID
   * @param aKind {@link IQNodeKind} - node kind
   * @param aParent {@link IQNode} - parent node
   * @param aEntity &lt;T&gt - entity in the node, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException entity is not of expected class
   * @throws TsIllegalArgumentRtException {@link IQNodeKind#canHaveChilds()} = <code>true</code>
   */
  public LeafQNode( String aId, IQNodeKind<T> aKind, IQNode aParent, T aEntity ) {
    super( aId, aKind, aParent, aEntity );
    TsIllegalArgumentRtException.checkTrue( aKind.canHaveChilds(), FMT_LEAF_NODE_KIND_CANT_HAVE_CHILDREN, aId );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQNode
  //

  @Override
  final protected IStridablesList<IQNode> doGetNodes() {
    return IStridablesList.EMPTY;
  }

}
