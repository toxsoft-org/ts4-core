package org.toxsoft.core.tsgui.bricks.tsnodes;

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * An {@link ITsNode} base implementation.
 *
 * @author hazard157
 * @param <T> - node content (entity) type
 */
public abstract class AbstractTsNode<T>
    implements ITsNode, ITsGuiContextable {

  private final ITsGuiContext      tsContext;
  private final ITsNodeKind<T>     kind;
  private final ITsNode            parent;
  private final IOptionSetEdit     params     = new OptionSet();
  private final IListEdit<ITsNode> childNodes = new ElemLinkedBundleList<>();

  private T       entity         = null;
  private boolean isChildsCached = false;
  private String  name           = null;
  private String  iconId         = null;
  private ITsNode root           = null;

  /**
   * Constructor.
   *
   * @param aKind {@link ITsNodeKind} - node kind
   * @param aParent {@link ITsNode} - parent node
   * @param aEntity &lt;T&gt - entity in the node, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException aEntity type is not compatibvle with {@link ITsNodeKind#entityClass()}
   */
  public AbstractTsNode( ITsNodeKind<T> aKind, ITsNode aParent, T aEntity ) {
    TsNullArgumentRtException.checkNulls( aKind, aParent );
    kind = aKind;
    parent = aParent;
    ITsNode node = this;
    while( node.parent() != null ) {
      node = node.parent();
    }
    tsContext = node.context();
    setEntity( aEntity );
  }

  /**
   * Constructor for root node implementation subclasses.
   * <p>
   * For non-<code>null</code> entities initializes {@link #name()} as {@link Object#toString() aEntity.toString()}.
   *
   * @param aKind {@link ITsNodeKind} - node kind
   * @param aEntity &lt;T&gt - entity in the node, may be <code>null</code>
   * @param aContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException aEntity type is not compatibvle with {@link ITsNodeKind#entityClass()}
   */
  public AbstractTsNode( ITsNodeKind<T> aKind, T aEntity, ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNulls( aKind, aContext );
    kind = aKind;
    parent = null;
    tsContext = aContext;
    setEntity( aEntity );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  /**
   * Ensures that childs are created and hold in {@link #childNodes}.
   * <p>
   * If aRebuild = <code>true</code> refreshes childs list be calling {@link #doGetNodes()}.
   * <p>
   * Method is ignored if {@link ITsNodeKind#canHaveChilds()} flags that node can not have childs.
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
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the node display name.
   *
   * @param aName String - the node display name, may be <code>null</code>
   */
  public void setName( String aName ) {
    name = aName;
  }

  /**
   * Sets node icon ID.
   *
   * @param aIconId String - node IconId, may be <code>null</code>
   */
  public void setIconId( String aIconId ) {
    iconId = aIconId;
  }

  /**
   * Changes the reference to the entity stored in the node.
   *
   * @param aEntity &lt;T&gt; - new referemce, may be <code>null</code>
   * @throws ClassCastException non-<code>null</code> argument is not of class {@link ITsNodeKind#entityClass()}
   */
  public final void setEntity( Object aEntity ) {
    entity = kind.entityClass().cast( aEntity );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Indicates that the cache of child nodes needs to be updated.
   * <p>
   * After call of this method list of childs will be requested to fill the cache, that is method {@link #doGetNodes()}
   * will be called inside the {@link #childs()}.
   */
  protected void invalidateCache() {
    isChildsCached = false;
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // ITsNode
  //

  @Override
  final public ITsGuiContext context() {
    return tsContext;
  }

  @Override
  public ITsNode root() {
    if( root == null ) {
      if( parent == null ) {
        root = this;
      }
      else {
        root = parent.root();
      }
    }
    return root;
  }

  @Override
  public ITsNodeKind<?> kind() {
    return kind;
  }

  @Override
  public ITsNode parent() {
    return parent;
  }

  /**
   * Returns the text to be displayed in the tree node.
   * <p>
   * The display name is determined in the following order:
   * <ul>
   * <li>string returned by {@link #doGetName()}, if not <code>null</code>;</li>
   * <li>the {@link #name} field, if not <code>null</code>;</li>
   * <li>{@link ITsNodeKind#getEntityName(Object)}, if not <code>null</code>;</li>
   * <li>{@link Object#toString() entity.toString()} if {@link #entity} is not <code>null</code>;</li>
   * <li>an empty string if everything above is <code>null</code>.</li>
   * </ul>
   */
  @Override
  public String name() {
    String s = doGetName();
    if( s != null ) {
      return s;
    }
    if( name != null ) {
      return name;
    }
    s = kind.getEntityName( entity );
    if( s != null ) {
      return s;
    }
    if( entity != null ) {
      return entity.toString();
    }
    return TsLibUtils.EMPTY_STRING;
  }

  @Override
  public Image getIcon( EIconSize aIconSize ) {
    TsNullArgumentRtException.checkNull( aIconSize );
    Image image = doGetImage( aIconSize );
    if( image != null ) {
      return image;
    }
    String iid = iconId != null ? iconId : kind.iconId();
    if( iid != null ) {
      return iconManager().loadStdIcon( iid, aIconSize );
    }
    return null;
  }

  @Override
  public T entity() {
    return entity;
  }

  @Override
  public String iconId() {
    return iconId;
  }

  @Override
  public IList<ITsNode> childs() {
    cacheChilds( false );
    return childNodes;
  }

  @Override
  public IList<ITsNode> listExistingChilds() {
    return childNodes;
  }

  @Override
  public ITsNode findByEntity( Object aEntity, boolean aQuerySubtree ) {
    if( Objects.equals( entity, aEntity ) ) {
      return this;
    }
    // rebuild the cache only when requesting a deep search
    if( aQuerySubtree ) {
      cacheChilds( true );
    }
    for( ITsNode n : childNodes ) {
      if( aEntity.equals( n.entity() ) ) {
        return n;
      }
      ITsNode found = n.findByEntity( aEntity, aQuerySubtree );
      if( found != null ) {
        return found;
      }
    }
    return null;
  }

  @Override
  public void rebuildSubtree( boolean aRebuild, boolean aQuerySubtree ) {
    cacheChilds( aRebuild );
    if( aQuerySubtree ) {
      for( ITsNode n : childNodes ) {
        n.rebuildSubtree( aRebuild, true );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass must create to list of child nodes.
   * <p>
   * Never is called for nodes with {@link ITsNodeKind#canHaveChilds()} = <code>true</code>.
   * <p>
   * Returned childs are hold in the internal cache. Method {@link #childs()} returns cached nodes. However, cache
   * rebuild may be explicitly queried with the methods {@link #invalidateCache()},
   * {@link #rebuildSubtree(boolean, boolean)}, {@link #findByEntity(Object, boolean)}.
   *
   * @return IList&lt;{@link ITsNode}&gt; - список дочерних узлов
   */
  protected abstract IList<ITsNode> doGetNodes();

  /**
   * The subclass may override and return the display image.
   * <p>
   * Returns <code>null</code> in the base class; no need to call it when overridden.
   *
   * @param aIconSize {@link EIconSize} - requested image size
   * @return {@link Image} - the image or null <code>null</code>
   */
  protected Image doGetImage( EIconSize aIconSize ) {
    return null;
  }

  /**
   * The subclass may override and return the display name.
   * <p>
   * Returns <code>null</code> in the base class; no need to call it when overridden.
   *
   * @return String - the node display name or <code>null</code>
   * @see #name()
   */
  protected String doGetName() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return name() + " || " + TsMiscUtils.toQuotedLine( entity.toString() );
  }

  /**
   * GOGA 2019-04-15 equals() and hashCode() should not be overridden. Two different nodes are never equal, even if they
   * contain the same entity. After all, ITsNode is just a wrapper, and, for example, in a node it is quite possible to
   * have two sub-nodes with the same text string.
   */

}
