package org.toxsoft.core.tslib.bricks.qnodes;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IQRootNode} abstract implementation.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public abstract non-sealed class AbstractQRootNode<T>
    extends AbstractQNode<T>
    implements IQRootNode {

  /**
   * {@link IQRootNode#eventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<IQRootNodeChangeListener> {

    private final IListEdit<IQNode> changedContentsNodes = new ElemArrayList<>();
    private final IListEdit<IQNode> changedSubtreeNodes  = new ElemArrayList<>();

    @Override
    protected boolean doIsPendingEvents() {
      return !changedContentsNodes.isEmpty() || !changedSubtreeNodes.isEmpty();
    }

    @Override
    protected void doClearPendingEvents() {
      changedContentsNodes.clear();
      changedSubtreeNodes.clear();
    }

    @Override
    protected void doFirePendingEvents() {
      for( IQNode node : changedContentsNodes ) {
        reallyFireContentEvent( node );
      }
      for( IQNode node : changedSubtreeNodes ) {
        reallyFireSubtreeEvent( node, ECrudOp.LIST, null );
      }
    }

    private void reallyFireContentEvent( IQNode aNode ) {
      for( IQRootNodeChangeListener l : listeners() ) {
        l.onNodeContentChanged( AbstractQRootNode.this, aNode );
      }
    }

    private void reallyFireSubtreeEvent( IQNode aNode, ECrudOp aOp, String aChildNodeId ) {
      for( IQRootNodeChangeListener l : listeners() ) {
        l.onNodeSubtreeChanged( AbstractQRootNode.this, aNode, aOp, aChildNodeId );
      }
    }

    public void fireNodeContentChanged( IQNode aNode ) {
      if( isFiringPaused() ) {
        // if it is repeated change, no need to fire additional event
        if( changedContentsNodes.hasElem( aNode ) ) {
          return;
        }
        // if changed node is in already changed subtree, no need to fire additional event
        for( IQNode node : changedSubtreeNodes ) {
          if( node.hasSubNode( aNode ) ) {
            return;
          }
        }
        changedContentsNodes.add( aNode );
        return;
      }
      reallyFireContentEvent( aNode );
    }

    public void fireNodeSubtreeChanged( IQNode aNode, ECrudOp aOp, String aChildNodeId ) {
      if( isFiringPaused() ) {
        // if changed subtree root is in already changed subtree, no need to fire additional event
        for( IQNode node : changedSubtreeNodes ) {
          if( node.hasSubNode( aNode ) ) {
            return;
          }
        }
        // remove earlier changed subtree roots if they are in newly changed subtree
        IListEdit<IQNode> toRemove = new ElemLinkedBundleList<>();
        for( IQNode node : changedSubtreeNodes ) {
          if( aNode.hasSubNode( aNode ) ) {
            toRemove.add( node );
          }
        }
        for( IQNode n : toRemove ) {
          changedSubtreeNodes.remove( n );
        }
        toRemove.clear();
        // remove changed node if it is in changed subtree
        for( IQNode n : changedContentsNodes ) {
          if( aNode.hasSubNode( n ) ) {
            toRemove.add( n );
          }
        }
        for( IQNode n : toRemove ) {
          changedContentsNodes.remove( n );
        }
        changedSubtreeNodes.add( aNode );
        return;
      }
      reallyFireSubtreeEvent( aNode, aOp, aChildNodeId );
    }

  }

  private final Eventer eventer = new Eventer();

  /**
   * Constructor.
   *
   * @param aId String - node ID
   * @param aKind {@link IQNodeKind} - node kind
   * @param aContext {@link ITsContext} - tree context
   * @param aEntity &lt;T&gt; - entity in this node
   * @param aParams {@link IOptionSet} - {@link #nodeData()} params initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   */
  public AbstractQRootNode( String aId, IQNodeKind<T> aKind, ITsContext aContext, T aEntity, IOptionSet aParams ) {
    super( aId, aKind, aContext, aEntity, aParams );
  }

  // ------------------------------------------------------------------------------------
  // IQRootNode
  //

  @Override
  public ITsEventer<IQRootNodeChangeListener> eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Nodes in tree call this method to fire node content change event.
   *
   * @param aNode {@link IQNode} - the changed node
   */
  public void fireNodeContentChanged( IQNode aNode ) {
    eventer.fireNodeContentChanged( aNode );
  }

  /**
   * Nodes in tree call this method to fire tree structure change event.
   *
   * @param aNode {@link IQNode} - root of the changed subtree
   * @param aOp {@link ECrudOp} - the reason of change
   * @param aChildNodeId String - affected node ID or <code>null</code>
   */
  public void fireNodeSubtreeChanged( IQNode aNode, ECrudOp aOp, String aChildNodeId ) {
    eventer.fireNodeSubtreeChanged( aNode, aOp, aChildNodeId );
  }

}
