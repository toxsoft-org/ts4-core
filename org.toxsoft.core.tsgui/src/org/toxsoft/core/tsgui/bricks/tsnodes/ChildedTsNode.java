package org.toxsoft.core.tsgui.bricks.tsnodes;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Node with childs default abstract implementation.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public abstract class ChildedTsNode<T>
    extends AbstractTsNode<T> {

  /**
   * Constructor.
   * <p>
   * For non-<code>null</code> entities initializes {@link #name()} as {@link Object#toString() aEntity.toString()}.
   *
   * @param aKind {@link ITsNodeKind} - node kind
   * @param aParent {@link ITsNode} - parent node
   * @param aEntity &lt;T&gt - entity in the node, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException aEntity type is not compatibvle with {@link ITsNodeKind#entityClass()}
   * @throws TsIllegalArgumentRtException {@link ITsNodeKind#canHaveChilds()} = <code>true</code>
   */
  public ChildedTsNode( ITsNodeKind<T> aKind, ITsNode aParent, T aEntity ) {
    super( aKind, aParent, aEntity );
    TsIllegalArgumentRtException.checkFalse( aKind.canHaveChilds() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsNode
  //

  @Override
  final protected IList<ITsNode> doGetNodes() {
    IListEdit<ITsNode> nodes = new ElemLinkedBundleList<>();
    doCollectNodes( nodes );
    for( ITsNode n : nodes ) {
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
   * Each node in filled list must have <b><code>this</code></b> as {@link ITsNode#parent()} otherwise
   * {@link #doGetNodes()} fill throw an {@link TsInternalErrorRtException} exception.
   *
   * @param aChilds {@link IListEdit}&lt;{@link ITsNode}&gt; - childs list to fill
   */
  protected abstract void doCollectNodes( IListEdit<ITsNode> aChilds );

}
