package org.toxsoft.core.tslib.bricks.qnodes;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import java.io.*;
import java.util.*;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.ctx.impl.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;

/**
 * {@link IQNode} base implementation.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public abstract non-sealed class AbstractQNode<T>
    implements IQNode {

  private final String                      id;
  private final IQNodeKind<T>               kind;
  private final AbstractQRootNode<?>        root;
  private final ITsContext                  tsContext;
  private final IQNode                      parent;
  private final ITsContext                  nodeData   = new TsContext();
  private final IStridablesListEdit<IQNode> childNodes = new StridablesList<>();

  private T       entity;
  private boolean isChildsCached = false;

  /**
   * Constructor for non-root nodes.
   *
   * @param aId String - node ID
   * @param aKind {@link IQNodeKind} - node kind
   * @param aParent {@link IQNode} - parent node
   * @param aEntity &lt;T&gt; - entity in this node
   * @param aParams {@link IOptionSet} - {@link #nodeData} params initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws ClassCastException entity is not of expected class
   */
  public AbstractQNode( String aId, IQNodeKind<T> aKind, IQNode aParent, T aEntity, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aParent, aKind, aEntity );
    kind = aKind;
    parent = aParent;
    entity = kind.entityClass().cast( aEntity );
    IQNode node = this;
    while( node.parent() != null ) {
      node = node.parent();
    }
    root = AbstractQRootNode.class.cast( node );
    tsContext = root.tsContext();
    fillNodeDataFromEntity();
    nodeData.params().setAll( aParams );
  }

  /**
   * Constructor for non-root nodes.
   *
   * @param aId String - node ID
   * @param aKind {@link IQNodeKind} - node kind
   * @param aParent {@link IQNode} - parent node
   * @param aEntity &lt;T&gt; - entity in this node
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws ClassCastException entity is not of expected class
   */
  public AbstractQNode( String aId, IQNodeKind<T> aKind, IQNode aParent, T aEntity ) {
    this( aId, aKind, aParent, aEntity, IOptionSet.NULL );
  }

  /**
   * Constructor for root node implementing {@link IQRootNode}.
   *
   * @param aId String - node ID
   * @param aKind {@link IQNodeKind} - node kind
   * @param aContext {@link ITsContext} - tree context
   * @param aEntity &lt;T&gt; - entity in this node
   * @param aParams {@link IOptionSet} - {@link #nodeData} params initial values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException ID is not an IDpath
   * @throws ClassCastException entity is not of expected class
   */
  protected AbstractQNode( String aId, IQNodeKind<T> aKind, ITsContext aContext, T aEntity, IOptionSet aParams ) {
    id = StridUtils.checkValidIdPath( aId );
    TsNullArgumentRtException.checkNulls( aContext, aKind, aEntity );
    kind = aKind;
    parent = null;
    entity = kind.entityClass().cast( aEntity );
    root = AbstractQRootNode.class.cast( this );
    tsContext = aContext;
    fillNodeDataFromEntity();
    nodeData.params().setAll( aParams );
  }

  private void fillNodeDataFromEntity() {
    if( entity instanceof IStridable s ) {
      nodeData.params().setStr( TSID_NAME, s.nmName() );
      nodeData.params().setStr( TSID_DESCRIPTION, s.description() );
    }
    if( entity instanceof IIconIdable ii ) {
      if( ii.iconId() != null ) {
        nodeData.params().setStr( TSID_ICON_ID, ii.iconId() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Ensures that childs are created and hold in {@link #childNodes}.
   * <p>
   * If aRebuild = <code>true</code> refreshes childs list be calling {@link #doGetNodes()}.
   * <p>
   * Method is ignored if {@link IQNodeKind#canHaveChilds()} flags that node can not have childs.
   *
   * @param aRebuild boolean - flags that child cache must be rebuilt
   */
  private void cacheChilds( boolean aRebuild ) {
    if( kind.canHaveChilds() ) {
      if( aRebuild || !isChildsCached ) {
        childNodes.setAll( doGetNodes() );
        isChildsCached = true;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Marks child cache as invalid so at next call of {@link #childs()} the childs list will be rebuild.
   */
  protected void invalidateCache() {
    isChildsCached = false;
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  final public String id() {
    return id;
  }

  @Override
  public String nmName() {
    return doGetName();
  }

  @Override
  public String description() {
    return doGetDescription();
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return doGetIconId();
  }

  // ------------------------------------------------------------------------------------
  // IQNode
  //

  @Override
  final public IQNodeKind<?> kind() {
    return kind;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public <C extends ITsContext> C tsContext() {
    return (C)tsContext;
  }

  @Override
  public ITsContext nodeData() {
    return nodeData;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  final public T entity() {
    return entity;
  }

  @Override
  final public IQRootNode root() {
    return root;
  }

  @Override
  final public IQNode parent() {
    return parent;
  }

  @Override
  public IStridablesList<IQNode> childs() {
    cacheChilds( false );
    return childNodes;
  }

  @Override
  public IQNode findByEntity( Object aEntity, boolean aQuerySubtree ) {
    if( Objects.equals( entity, aEntity ) ) {
      return this;
    }
    // cache nodes only if requested
    if( aQuerySubtree ) {
      cacheChilds( true );
    }
    for( IQNode n : childNodes ) {
      if( Objects.equals( aEntity, n.entity() ) ) {
        return n;
      }
      IQNode found = n.findByEntity( aEntity, aQuerySubtree );
      if( found != null ) {
        return found;
      }
    }
    return null;
  }

  @Override
  public IQNode findByNodeId( String aNodeId ) {
    TsNullArgumentRtException.checkNull( aNodeId );
    if( id().equals( aNodeId ) ) {
      return this;
    }
    for( IQNode n : childNodes ) {
      if( aNodeId.equals( n.entity() ) ) {
        return n;
      }
      IQNode found = n.findByNodeId( aNodeId );
      if( found != null ) {
        return found;
      }
    }
    return null;
  }

  @Override
  public boolean hasSubNode( IQNode aNode ) {
    TsNullArgumentRtException.checkNull( aNode );
    if( this == aNode ) {
      return true;
    }
    for( IQNode node : childNodes ) {
      if( node.hasSubNode( aNode ) ) {
        return true;
      }
    }
    return false;
  }

  @Override
  public IStridablesList<IQNode> listExistingChilds() {
    return childNodes;
  }

  @Override
  public void rebuildSubtree( boolean aRebuild, boolean aQuerySubtree ) {
    cacheChilds( aRebuild );
    if( aQuerySubtree ) {
      for( IQNode n : childNodes ) {
        n.rebuildSubtree( aRebuild, true );
      }
    }
  }

  @Override
  public void refreshEntity() {
    T oldEntityRef = entity;
    Object newEntityRef = doGetEntity();
    TsInternalErrorRtException.checkNull( newEntityRef );
    entity = kind.entityClass().cast( newEntityRef );
    doEntityChanged( entity, oldEntityRef );
    root.fireNodeContentChanged( this );
  }

  @Override
  public void informOnChildsChange( ECrudOp aOp, String aChildNodeId ) {
    TsNullArgumentRtException.checkNull( aOp );
    invalidateCache();
    root.fireNodeSubtreeChanged( this, aOp, aChildNodeId );
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    String s = nmName().isEmpty() ? id : nmName();
    return getClass().getSimpleName() + ": " + kind.id() + " - " + s; //$NON-NLS-1$ //$NON-NLS-2$
  }

  // ------------------------------------------------------------------------------------
  // To implement & override
  //

  /**
   * Subclass must really create child nodes.
   * <p>
   * Method is never called for nodes {@link IQNodeKind#canHaveChilds()} = <code>false</code>.
   *
   * @return {@link IStridablesList}&lt;{@link IQNode}&gt; - list of child nodes
   */
  protected abstract IStridablesList<IQNode> doGetNodes();

  /**
   * Subclass may refresh reference to held entity when informed about changes by {@link #refreshEntity()}.
   * <p>
   * Base implementation simply returns entity. This method must be overriden only if reference to the changes, for
   * example, when held entity is an immutable class instance.
   *
   * @return &lt;T&gt; - new reference to the node entity, must not be <code>null</code>
   */
  protected T doGetEntity() {
    return entity;
  }

  /**
   * The subclass may work out the change in the node entity.
   * <p>
   * Recall that the reference to node entity is updated every time after {@link #refreshEntity()} is called. This
   * notification method is called even when both old and new references point to the same object. Indeed, the meaning
   * of {@link #refreshEntity()} is that the node is informed that there are changes in the entity, it does not matter
   * if the reference has changed or the entity field has changed or something has changed that the entity refers to.
   * For example, the entity is constant {@link File}, but the content of the file has changed.
   * <p>
   * Does nothing in the base class; when overridden, you do not need to call the parent method.
   *
   * @param aEntity &lt;T&gt; - current entity, the same as {@link #entity()}
   * @param aOldEntity &lt;T&gt; - previous reference to entity, before call to {@link #refreshEntity()}
   */
  protected void doEntityChanged( T aEntity, T aOldEntity ) {
    // nop
  }

  /**
   * Returns value to be returned by {@link #nmName()}.
   * <p>
   * Subclass may override default implementation that returns value of option {@link IAvMetaConstants#DDEF_NAME} from
   * {@link #nodeData()} params.
   *
   * @return String - short name of node, must not be <code>null</code>
   */
  protected String doGetName() {
    return DDEF_NAME.getValue( nodeData.params() ).asString();
  }

  /**
   * Returns value to be returned by {@link #description()}.
   * <p>
   * Subclass may override default implementation that returns value of option {@link IAvMetaConstants#DDEF_DESCRIPTION}
   * from {@link #nodeData()} params.
   *
   * @return String - description of node, must not be <code>null</code>
   */
  protected String doGetDescription() {
    return DDEF_DESCRIPTION.getValue( nodeData.params() ).asString();
  }

  /**
   * Returns value to be returned by {@link #iconId()}.
   * <p>
   * Subclass may override default implementation that returns value of option {@link IAvMetaConstants#TSID_ICON_ID}
   * from {@link #nodeData()} params or {@link IQNodeKind#iconId()} if first one is <code>null</code>.
   *
   * @return String - the icon ID or <code>null</code>
   */
  protected String doGetIconId() {
    String iconId = nodeData.params().getStr( TSID_ICON_ID, null );
    if( iconId == null ) {
      iconId = kind.iconId();
    }
    return iconId;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets {@link #nmName()} as {@link #nodeData()} option.
   *
   * @param aName String - short name
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void setName( String aName ) {
    TsNullArgumentRtException.checkNull( aName );
    nodeData.params().setStr( TSID_NAME, aName );
  }

  /**
   * Sets {@link #description()} as {@link #nodeData()} option.
   *
   * @param aDescription String - description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void setDescription( String aDescription ) {
    TsNullArgumentRtException.checkNull( aDescription );
    nodeData.params().setStr( TSID_DESCRIPTION, aDescription );
  }

  /**
   * Sets {@link #nmName()} and {@link #description()} as {@link #nodeData()} option.
   *
   * @param aName String - short name
   * @param aDescription String - description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected void setNameAndDescription( String aName, String aDescription ) {
    TsNullArgumentRtException.checkNulls( aName, aDescription );
    nodeData.params().setStr( TSID_NAME, aName );
    nodeData.params().setStr( TSID_DESCRIPTION, aDescription );
  }

  /**
   * Sets {@link #iconId()} as {@link #nodeData()} option.
   *
   * @param aIconId String - icon ID, or <code>null</code>
   */
  protected void setIconId( String aIconId ) {
    if( aIconId != null ) {
      nodeData.params().setStr( TSID_ICON_ID, aIconId );
    }
    else {
      nodeData.params().remove( TSID_ICON_ID );
    }
  }

}
