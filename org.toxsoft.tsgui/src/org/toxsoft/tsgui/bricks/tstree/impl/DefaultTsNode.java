package org.toxsoft.tsgui.bricks.tstree.impl;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.tstree.*;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Реализация узла по умолчанию - добавляет к {@link AbstractTsNode} возможность задачь дочерные узлы извне.
 *
 * @author goga
 * @param <T> - конкретный тип содержимого узла
 */
public class DefaultTsNode<T>
    extends AbstractTsNode<T> {

  private final IListEdit<ITsNode> userNodes = new ElemLinkedBundleList<>();

  /**
   * Конструктор.
   * <p>
   * Для не-null сущностей aEntity инициализирует имя {@link #name()} в {@link Object#toString() aEntity.toString()}.
   *
   * @param aKind {@link ITsNodeKind} - вид узла
   * @param aParent {@link ITsNode} - родительский узел, а при создании корневых улов - {@link ITsTreeViewer}
   * @param aEntity T - сущность в узле, может быть null
   * @throws TsNullArgumentRtException любой аргумент (кроме aEntity) = null
   * @throws ClassCastException тип объекта aEntity не соответствует {@link ITsNodeKind#entityClass()}
   */
  public DefaultTsNode( ITsNodeKind<T> aKind, ITsNode aParent, T aEntity ) {
    super( aKind, aParent, aEntity );
  }

  /**
   * Конструктор для наследника - корневого узла.
   * <p>
   * Для не-null сущностей aEntity инициализирует имя {@link #name()} в {@link Object#toString() aEntity.toString()}.
   *
   * @param aKind {@link ITsNodeKind} - вид узла
   * @param aEntity T - сущность в узле, может быть null
   * @param aContext {@link ITsGuiContext} - контекст
   * @throws TsNullArgumentRtException любой аргумент (кроме aEntity) = null
   * @throws ClassCastException тип объекта aEntity не соответствует {@link ITsNodeKind#entityClass()}
   */
  public DefaultTsNode( ITsNodeKind<T> aKind, T aEntity, ITsGuiContext aContext ) {
    super( aKind, aEntity, aContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  final protected IList<ITsNode> doGetNodes() {
    return userNodes;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Добавляет дочерный узел.
   * <p>
   * Внимание! Изменения в структуре поддерева будут отображены только после того, как каким-либо способом будет
   * обновлена структура дерева. Другими словами, изменения вступят в силу, после вызова в родителе метода
   * {@link #doGetNodes()}.
   *
   * @param aNode {@link ITsNode} - добавляемый узел
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemAlreadyExistsRtException узел уже добавлен
   * @throws TsIllegalArgumentRtException родитель аргумента {@link ITsNode#parent()} не является этим узлом
   */
  public void addNode( ITsNode aNode ) {
    TsItemAlreadyExistsRtException.checkTrue( userNodes.hasElem( aNode ) );
    TsIllegalArgumentRtException.checkTrue( aNode.parent() != this );
    userNodes.add( aNode );
    invalidateCache();
  }

  /**
   * Заменяет узел в списке дочерных узлов.
   *
   * @param aOldNode {@link ITsNode} - старый узел
   * @param aNewNode {@link ITsNode} - новый узел
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsItemNotFoundRtException старого узла нет в списке дочерних
   */
  public void replaceNode( ITsNode aOldNode, ITsNode aNewNode ) {
    TsNullArgumentRtException.checkNulls( aOldNode, aNewNode );
    userNodes.set( userNodes.indexOf( aOldNode ), aNewNode );
    invalidateCache();
  }

  /**
   * Удаляет элемент из списка.
   * <p>
   * Если узла нет в списке, ничего не делает.
   *
   * @param aNodeToRemove {@link ITsNode} - удаляемый узел
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void removeNode( ITsNode aNodeToRemove ) {
    userNodes.remove( aNodeToRemove );
    invalidateCache();
  }

  /**
   * Заменяет сщуствующие узлы новыми.
   * <p>
   * Внимание! Изменения в структуре поддерева будут отображены только после того, как каким-либо способом будет
   * обновлена структура дерева. Другими словами, изменения вступят в силу, после вызова в родителе метода
   * {@link #doGetNodes()}.
   *
   * @param aNodes {@link IList}&lt;{@link ITsNode}&gt; - новые узлы
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemAlreadyExistsRtException повторяющейся ущлы в аргмуенте-списке
   * @throws TsIllegalArgumentRtException родитель одного из добавляемых узлов не является этим узлом
   */
  public void setNodes( IList<ITsNode> aNodes ) {
    TsNullArgumentRtException.checkNull( aNodes );
    clearNodes();
    for( ITsNode n : aNodes ) {
      addNode( n );
    }
    invalidateCache();
  }

  /**
   * Удаляет все дочерние узлы.
   * <p>
   * Внимание! Изменения в структуре поддерева будут отображены только после того, как каким-либо способом будет
   * обновлена структура дерева. Другими словами, изменения вступят в силу, после вызова в родителе метода
   * {@link #doGetNodes()}.
   */
  public void clearNodes() {
    userNodes.clear();
    invalidateCache();
  }

}
