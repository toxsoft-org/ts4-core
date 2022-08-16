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

  // TODO TRANSLATE

  /**
   * Задает отображаемое имя узла.
   *
   * @param aName String - название узла, может быть null
   * @throws TsNullArgumentRtException аргумент = null
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
   * Задает новую сущность.
   *
   * @param aEntity &lt;T&gt; - новая сущность
   * @throws ClassCastException аргумент не явлсетя экземпляром класса {@link ITsNodeKind#entityClass()}
   */
  public final void setEntity( Object aEntity ) {
    entity = kind.entityClass().cast( aEntity );
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Указывает на необходимость обновть кеш дочерных узлов.
   */
  protected void invalidateCache() {
    isChildsCached = false;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsNode
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
    // строим кеш только при запросе глубокого поиска
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
  // Для переопределения
  //

  /**
   * Наследник должен сформировать список дочерних узлов.
   * <p>
   * Метод вызывается ровно один раз за время жизни тех узлов, у которых {@link ITsNodeKind#canHaveChilds()} =
   * <code>true</code>, для остальных узлов он вообще не вызывается.
   *
   * @return IList&lt;{@link ITsNode}&gt; - список дочерних узлов
   */
  protected abstract IList<ITsNode> doGetNodes();

  /**
   * Наследник может переопределить и вернуть отображаемое изображение.
   * <p>
   * Аргумент является рекомендацией. Метод может вернуть изображение другого размера, что соответственно, отразится в
   * отображении дерева.
   * <p>
   * В базовом классе возвращает null, при переопределении вызываеть не надо.
   *
   * @param aIconSize {@link EIconSize} - запрашиваемый размер изображения
   * @return {@link Image} - изображение узла или <code>null</code>
   */
  protected Image doGetImage( EIconSize aIconSize ) {
    return null;
  }

  /**
   * Наследник может переопределить и вернуть отображаемое название.
   * <p>
   * Возвращаемое значение не изменяет заданное методом {@link #setName(String)} имя. Как только этот метод вернет null,
   * то метод {@link #name()} вернет заданное методом {@link #setName(String)} название.
   * <p>
   * В базовом классе возвращает null, при переопределении вызываеть не надо.
   *
   * @return String - название узла или null
   */
  protected String doGetName() {
    return null;
  }

  // ------------------------------------------------------------------------------------
  // Методы класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return name() + " || " + TsMiscUtils.toQuotedLine( entity.toString() );
  }

  /**
   * GOGA 2019-04-15 не следует переопределять equals() и hashCode(), два разных узла никогда не равны между собой, даже
   * если содержат одну и ту же сущность. Ведь ITsNode всего-то оболочка, и, например, в узле вполне можно иметь два
   * подузла с одинаковой текстовой строкой.
   */
  // @Override
  // public boolean equals( Object aThat ) {
  // if( aThat == this ) {
  // return true;
  // }
  // if( aThat instanceof ITsNode ) {
  // ITsNode that = (ITsNode)aThat;
  // return this.kind.equals( that.kind() ) && Objects.equals( this.entity, that.entity() );
  // }
  // return false;
  // }
  //
  // @Override
  // public int hashCode() {
  // int result = CollectionsUtils.INITIAL_HASH_CODE;
  // result = CollectionsUtils.PRIME * result + kind.hashCode();
  // result = CollectionsUtils.PRIME * result + Objects.hashCode( entity );
  // return result;
  // }

}
