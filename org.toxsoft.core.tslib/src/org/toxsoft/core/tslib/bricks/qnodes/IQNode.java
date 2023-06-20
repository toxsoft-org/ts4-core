package org.toxsoft.core.tslib.bricks.qnodes;

import java.util.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.icons.*;

/**
 * General purpose tree node.
 * <p>
 * Node identifier {@link #id()} must be unique among siblings, that is among {@link #childs()} or {@link #parent()}
 * node.
 * <p>
 * An editable user-defined node options in {@link #nodeData()} are hold individually for each node. Default
 * implementation of {@link AbstractQNode} uses options {@link IAvMetaConstants#TSID_NAME},
 * {@link IAvMetaConstants#TSID_DESCRIPTION} and {@link IAvMetaConstants#TSID_ICON_ID} are used to store
 * {@link #nmName()}, {@link #description()} and {@link #iconId()} values respectively.
 *
 * @author hazard157
 */
@SuppressWarnings( "rawtypes" )
public sealed interface IQNode
    extends IStridable, IIconIdable
    permits IQRootNode, AbstractQNode {

  /**
   * Returns the node kind.
   *
   * @return {@link IQNodeKind} - the node kind
   */
  IQNodeKind<?> kind();

  /**
   * Returns the tree context.
   * <p>
   * For all nodes returns the same reference. Usually context is set for root node and cwhole tree refers to the root
   * node's context as a tree context.
   *
   * @param <C> - expected type of context
   * @return &lt;C&gt; - the tree context
   */
  <C extends ITsContext> C tsContext();

  /**
   * Returns the supplementary node data.
   * <p>
   * Note difference between {@link #tsContext()} and {@link #nodeData()}. First is the single context of whole tree and
   * second is some storage that may freely be used by the application. However default implementation of
   * {@link AbstractQNode} uses corresponding options (as in {@link IAvMetaConstants}) to store values of
   * {@link #nmName()}, {@link #description()} and {@link #iconId()}.
   *
   * @return {@link ITsContext} - supplementary references and options
   */
  ITsContext nodeData();

  // ------------------------------------------------------------------------------------
  // Node content

  /**
   * Return an entity hold by this node.
   * <p>
   * It is common practice to represent hierarchical tree of entities with node {@link IQNode}. For such usage node
   * holds corresponding entity, {@link #parent()} node holds parent entity and so on. Anyway, non-<code>null</code>
   * entity always is held by node.
   *
   * @param <T> - expected type of entity
   * @return &lt;T&gt; - entity held by node, never is <code>null</code>
   */
  <T> T entity();

  // ------------------------------------------------------------------------------------
  // hierarchy

  /**
   * Returns the root node.
   * <p>
   * Root node is the only node without parent.
   *
   * @return {@link IQRootNode} - the root node
   */
  IQRootNode root();

  /**
   * Returns the parent node.
   *
   * @return {@link IQNode} - parent node or <code>null</code> for root node
   */
  IQNode parent();

  /**
   * Returns the child nodes.
   * <p>
   * If childs are not already created, requests node creation and returns created nodes. See note to
   * {@link #listExistingChilds()}.
   * <p>
   * For nodes that can node have childs {@link IQNodeKind#canHaveChilds()} = <code>false</code> returns an empty list.
   *
   * @return {@link IStridablesList}&lt;{@link IQNode}&gt; - child nodes list, never is <code>null</code>
   */
  IStridablesList<IQNode> childs();

  /**
   * Searches for node by entity in subtree rooted by this node.
   * <p>
   * Note: setting <code>aQuerySubtree</code> to <code>true</code> may be heavily resource-consuming - whole subtree
   * will be created. When <code>aQuerySubtree</code> is <code>false</code>, only existing (cached) nodes will be
   * searched.
   *
   * @param aEntity {@link Object} - reference to entity checked with {@link Objects#equals(Object, Object)}
   * @param aQuerySubtree boolean - child nodes creation request flag
   * @return {@link IQNode} - found node or <code>null</code>
   */
  IQNode findByEntity( Object aEntity, boolean aQuerySubtree );

  /**
   * Searches for the existing node by the node ID.
   *
   * @param aNodeId String - the node ID
   * @return {@link IQNode} - found node or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IQNode findByNodeId( String aNodeId );

  /**
   * Determines if asked node is in this nodes subtree.
   * <p>
   * The node is searched by simple <b><code>==</code></b> operator, not by {@link Object#equals(Object)} method.
   * <p>
   * Note: obviously, only existing nodes are considered.
   *
   * @param aNode {@link IQNode} - asked node
   * @return boolean - <code>true</code> if asked node is under this node or is this node itself
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  boolean hasSubNode( IQNode aNode );

  // ------------------------------------------------------------------------------------
  // optimization

  /**
   * Returns existing (cached) child nodes, does not requests creation.
   * <p>
   * Note: IQNode implemetaion does not requires to create childs at node creation. Usually child nodes are created at
   * first request either by {@link #childs()} call or {@link #rebuildSubtree(boolean, boolean)
   * rebuildSubtree(<b>true</b>, boolean)}. However sometimes itis needed to access only child node that are already
   * created, for example, when searching or filtering nodes in GUI tree.
   *
   * @return {@link IStridablesList} &lt;{@link IQNode}&gt; - list of existing (cached) child nodes
   */
  IStridablesList<IQNode> listExistingChilds();

  /**
   * Creates unexisting nodes and/or creates all nodes from scratch.
   * <p>
   * When aRebuild=<code>false</code> creates only unexisting nodes. aRebuild=<code>true</code> will (re)create all
   * nodes.
   * <p>
   * <code>aQuerySubtree</code> determines if only childs will be rebuild (when <code>false</code>) or whole subtree
   * will be rebuilt (when <code>true</code>).
   * <p>
   * Note: setting <code>aQuerySubtree</code> to <code>true</code> may be heavily resource-consuming - whole subtree
   * will be created. When <code>aQuerySubtree</code> is <code>false</code>, only existing (cached) nodes will be
   * searched.
   *
   * @param aRebuild boolean - <code>true</code> to recreate existing nodes too, not only unexisting ones
   * @param aQuerySubtree boolean - child nodes creation request flag
   */
  void rebuildSubtree( boolean aRebuild, boolean aQuerySubtree );

  /**
   * Determines if node has childs.
   * <p>
   * This method is kind of optimization like <code>ITreeContentProvider.hasChildren(Object)</code>. Default
   * implementation call {@link #childs()} method hence requests creation of unexisting nodes. However, method may be
   * overrideren to provide information about childs existance if child creation is heavily resource-consuming. This
   * method usually is ised in GUI tree to draw node expansion sign.
   * <p>
   * For nodes that can node have childs {@link IQNodeKind#canHaveChilds()} = <code>false</code> always returns
   * <code>false</code>.
   *
   * @return boolean - <code>true</code> if the given node has children
   */
  default boolean hasChilds() {
    return !childs().isEmpty();
  }

  // ------------------------------------------------------------------------------------
  // notifications

  /**
   * Informs this node about changes and refreshed reference to the {@link #entity()}
   * <p>
   * In general, this node assumes that the {@link #entity()} link has changed, not just the properties of the existing
   * entity. Also, this method may be called when {@link #nodeData()} has been changed, especially, if it is used to
   * store {@link #nmName()}, {@link #description()} or {@link #iconId()}.
   * <p>
   * Note: there is no need to call this method if {@link #informOnChildsChange(ECrudOp, String)} with
   * {@link ECrudOp#EDIT} was called with this node {@link #id()} - parent implementation must call this method.
   */
  void refreshEntity();

  /**
   * Informs this node about changes in child nodes and invalidates childs cache.
   * <p>
   * According to <code>aOp</code> value there are different reasons:
   * <ul>
   * <li>{@link ECrudOp#CREATE} - new node added with ID <code>aChildNodeId</code>;</li>
   * <li>{@link ECrudOp#REMOVE} - child node with ID <code>aChildNodeId</code> was removed;</li>
   * <li>{@link ECrudOp#EDIT} - properties of node with ID <code>aChildNodeId</code> has been changed without changes in
   * tree structure. Implementaion must call {@link #refreshEntity()} for the specified child node;</li>
   * <li>{@link ECrudOp#LIST} - there is changes in subtree structure affecting several nodes at once. Usually all
   * childs need to be recreated. In this case value of argument <code>aChildNodeId</code> is ignored and may be
   * <code>null</code>.</li>
   * </ul>
   * Note: this method has no effect if childs was not created yet.
   *
   * @param aOp {@link ECrudOp} - the reason of change
   * @param aChildNodeId String - affected node ID or <code>null</code>
   */
  void informOnChildsChange( ECrudOp aOp, String aChildNodeId );

  // ------------------------------------------------------------------------------------
  // TODO Tree paths API
  //

  // IdChain getAbsPath();
  //
  // IdChain getRelPath( IQNode aSubNode );
  //
  // IQNode findByRelPath( IdChain aRelativePath, boolean aQueryAlongPath );
  //
  // default IQNode findByAbsPath( IdChain aAbsolutePath, boolean aQueryAlongPath ) {
  // return root().findByRelPath( aAbsolutePath, aQueryAlongPath );
  // }

}
