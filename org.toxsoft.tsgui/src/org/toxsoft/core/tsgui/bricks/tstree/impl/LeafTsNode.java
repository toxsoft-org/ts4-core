package org.toxsoft.core.tsgui.bricks.tstree.impl;

import org.toxsoft.core.tsgui.bricks.tstree.ITsNode;
import org.toxsoft.core.tsgui.bricks.tstree.ITsNodeKind;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Leaf node default implementation.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public class LeafTsNode<T>
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
  public LeafTsNode( ITsNodeKind<T> aKind, ITsNode aParent, T aEntity ) {
    super( aKind, aParent, aEntity );
    TsIllegalArgumentRtException.checkTrue( aKind.canHaveChilds() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTsNode
  //

  @Override
  final protected IList<ITsNode> doGetNodes() {
    return IList.EMPTY;
  }

}
