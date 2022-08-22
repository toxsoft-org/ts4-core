package org.toxsoft.core.tslib.bricks.qnodes.helpers;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Default abstract implementation of node with childs.
 * <p>
 * This is a helper class. Helper classis not used by QNodes package, rather it is kind of design pattern how to
 * implement frequently needed functionality related to QNodes.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public abstract class AbstractChildedQNode<T>
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
  public AbstractChildedQNode( String aId, IQNodeKind<T> aKind, IQNode aParent, T aEntity ) {
    super( aId, aKind, aParent, aEntity );
    TsIllegalArgumentRtException.checkFalse( aKind.canHaveChilds() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQNode
  //

  @Override
  final protected IStridablesList<IQNode> doGetNodes() {
    IStridablesListEdit<IQNode> nodes = new StridablesList<>();
    doCollectNodes( nodes );
    for( IQNode n : nodes ) {
      TsInternalErrorRtException.checkTrue( n.parent() != this );
    }
    return nodes;
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Subclass shall fill argument with created child nodes.
   * <p>
   * Each node in filled list must have <b><code>this</code></b> as {@link IQNode#parent()} otherwise
   * {@link #doGetNodes()} fill throw an {@link TsInternalErrorRtException} exception.
   *
   * @param aChilds {@link IStridablesListEdit}&lt;{@link IQNode}&gt; - childs list to fill
   */
  protected abstract void doCollectNodes( IStridablesListEdit<IQNode> aChilds );

}
