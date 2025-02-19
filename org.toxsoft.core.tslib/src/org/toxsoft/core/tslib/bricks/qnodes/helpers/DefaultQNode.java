package org.toxsoft.core.tslib.bricks.qnodes.helpers;

import org.toxsoft.core.tslib.bricks.qnodes.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Default implementation of childed nodes that simply holds child nodes list.
 * <p>
 * This is a helper class. Helper classis not used by QNodes package, rather it is kind of design pattern how to
 * implement frequently needed functionality related to QNodes.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public class DefaultQNode<T>
    extends AbstractQNode<T> {

  private final IStridablesListEdit<IQNode> userNodes = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - node ID
   * @param aKind {@link IQNodeKind} - node kind
   * @param aParent {@link IQNode} - parent node
   * @param aEntity &lt;T&gt; - entity in this node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws ClassCastException entity is not of expected class
   */
  public DefaultQNode( String aId, IQNodeKind<T> aKind, IQNode aParent, T aEntity ) {
    super( aId, aKind, aParent, aEntity );
  }

  // ------------------------------------------------------------------------------------
  // AbstractQNode
  //

  @Override
  final protected IStridablesList<IQNode> doGetNodes() {
    return userNodes;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Add child node.
   * <p>
   * Note: this method does <b>not</b> informs the tree about node subtree structure change. However method invalidates
   * children cache so next call to {@link #childs()} will return refreshed actual list of children. If needed, method
   * {@link IQNode#informOnChildsChange(ECrudOp, String)} must be called after changes.
   *
   * @param aNode {@link IQNode} - node to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemAlreadyExistsRtException node with same ID is already c a child
   * @throws TsIllegalArgumentRtException {@link IQNode#parent()} is not <code>this</code>
   */
  public void addNode( IQNode aNode ) {
    TsItemAlreadyExistsRtException.checkTrue( userNodes.hasKey( aNode.id() ) );
    TsIllegalArgumentRtException.checkTrue( aNode.parent() != this );
    userNodes.add( aNode );
    invalidateCache();
  }

  /**
   * Replaces existing or adds a child node.
   * <p>
   * Note: this method does <b>not</b> informs the tree about node subtree structure change. However method invalidates
   * children cache so next call to {@link #childs()} will return refreshed actual list of children. If needed, method
   * {@link IQNode#informOnChildsChange(ECrudOp, String)} must be called after changes.
   *
   * @param aNode {@link IQNode} - node to add
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException {@link IQNode#parent()} is not <code>this</code>
   */
  public void putNode( IQNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    userNodes.put( aNode );
    invalidateCache();
  }

  /**
   * Removes child node if it exists.
   * <p>
   * Note: this method does <b>not</b> informs the tree about node subtree structure change. However method invalidates
   * children cache so next call to {@link #childs()} will return refreshed actual list of children. If needed, method
   * {@link IQNode#informOnChildsChange(ECrudOp, String)} must be called after changes.
   *
   * @param aNodeId String - ID of node to remove
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException {@link IQNode#parent()} is not <code>this</code>
   */
  public void removeNode( String aNodeId ) {
    userNodes.removeById( aNodeId );
    invalidateCache();
  }

  /**
   * Replaces existing nodes by the given nodes.
   * <p>
   * Note: this method does <b>not</b> informs the tree about node subtree structure change. However method invalidates
   * children cache so next call to {@link #childs()} will return refreshed actual list of children. If needed, method
   * {@link IQNode#informOnChildsChange(ECrudOp, String)} must be called after changes.
   *
   * @param aNodes {@link IStridablesList}&lt;{@link IQNode}&gt; - new nodes
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException for any given node {@link IQNode#parent()} is not <code>this</code>
   */
  public void setNodes( IStridablesList<IQNode> aNodes ) {
    TsNullArgumentRtException.checkNull( aNodes );
    clearNodes();
    for( IQNode n : aNodes ) {
      addNode( n );
    }
    invalidateCache();
  }

  /**
   * Removes all child nodes.
   * <p>
   * Note: this method does <b>not</b> informs the tree about node subtree structure change. However method invalidates
   * children cache so next call to {@link #childs()} will return refreshed actual list of children. If needed, method
   * {@link IQNode#informOnChildsChange(ECrudOp, String)} must be called after changes.
   */
  public void clearNodes() {
    userNodes.clear();
    invalidateCache();
  }

}
