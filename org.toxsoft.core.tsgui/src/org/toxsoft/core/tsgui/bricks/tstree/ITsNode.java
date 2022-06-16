package org.toxsoft.core.tsgui.bricks.tstree;

import java.util.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;

// TODO NODE

/**
 * TS tree node.
 * <p>
 * Узел дерева содержит в себе список дочерних узлов {@link #childs()}. Этот список формируется только при обращении к
 * методу {@link #childs()} (а также, в зависимости от аргументов, методами {@link #findByEntity(Object, boolean)} и
 * {@link #rebuildSubtree(boolean, boolean)}).
 *
 * @author hazard157
 */
public interface ITsNode
    extends IParameterizedEdit, IIconIdable {

  /**
   * Возращает контекст и параметры дерева.
   * <p>
   * Для всех узлов в дереве возвращается ссылка на один и тот же экземпляр контекста.
   * <p>
   * В зависимости от способа создания дерева (точнее, в зависимости от корневго узла), метод может возвращать null.
   *
   * @return {@link ITsGuiContext} - обобщенный контекст всех узлов, может быть null
   */
  ITsGuiContext context();

  /**
   * Возвращает корневой узел дерева.
   *
   * @return {@link ITsNode} - корневой узел дерева
   */
  ITsNode root();

  // ------------------------------------------------------------------------------------
  // Работа с содержимым узла
  //

  /**
   * Возвращает вид узла.
   *
   * @return {@link ITsNodeKind} - вид узла
   */
  ITsNodeKind<?> kind();

  /**
   * Возвращает отображаемое название узла.
   *
   * @return String - отображаемое название узла
   */
  String name();

  /**
   * Возвращает значок для узла.
   *
   * @param aIconSize {@link EIconSize} - запрошенный размер значка
   * @return {@link Image} - изображение или <code>null</code>
   */
  Image getIcon( EIconSize aIconSize );

  /**
   * Возвращает ту сущность, которому соответствует узел.
   *
   * @return &lt;T&gt; - сущность, которому соответствует узел, может быть null
   */
  Object entity();

  // ------------------------------------------------------------------------------------
  // Работа с иерархией узлов
  //

  /**
   * Возвращает родительский узел.
   *
   * @return {@link ITsNode} - родительский узел или null для дерева-владельца
   */
  ITsNode parent();

  /**
   * Возвращает список дочерных узлов.
   * <p>
   * Если дети уже были хоть раз запрошены, или перестроены, то возвращает кешированные узлы. Иначе строит кеш дочерных
   * узлов (но не внучек и дадлее).
   *
   * @return IList&lt;{@link ITsNode}&gt; - список дочерных узлов
   */
  IList<ITsNode> childs();

  /**
   * Возвращает список очерных узлов, если они уже были созданы.
   * <p>
   * Напомним, что реализации {@link ITsNode} не обязаны сразу создавать дочерные узлы, а только при первом запросе
   * (нпаример, методами {@link #childs()},{@link #hasChilds()}) или явно (например,
   * {@link #rebuildSubtree(boolean, boolean)}). В то же время, иногда нужно иметь возможность получить только
   * существующие, уже созданные узлы. Например, при поиске по дереву. Для этих нужд и служит этот метод - он отличается
   * от {@link #childs()} только тем, что не содзает узлы, не заполняет кеш.
   *
   * @return IList&lt;{@link ITsNode}&gt; - список существующих дочерных узлов
   */
  IList<ITsNode> listExistingChilds();

  /**
   * Определяет, есть ли дети у узла.
   * <p>
   * Приводит к вызову {@link #childs()}, то есть, может быть ресурсоемкой операцией.
   *
   * @return boolean - признак наличия детей у узла
   */
  default boolean hasChilds() {
    return !childs().isEmpty();
  }

  /**
   * Находит среди поддерева (включая себя) узел, который содержит указанную ссылку.
   * <p>
   * Обратите внимание, что установка aQuerySubtree=<code>true</code> может оказаться весьма ресурсоемкой операцией -
   * будет создано всё поддерево, начиная от этого узла. При aQuerySubtree=<code>false</code> поиск будет идти по всем
   * уже кешированным узлам поддерева.
   *
   * @param aEntity {@link Object} - ссылка, которая проверяется {@link Objects#equals(Object, Object)}
   * @param aQuerySubtree boolean - признак обновления кеша дочек всего потомства
   * @return {@link ITsNode} - найденный узел или null
   */
  ITsNode findByEntity( Object aEntity, boolean aQuerySubtree );

  /**
   * Обеспечивает (пере)создание дочерных узлов.
   * <p>
   * В обычном случае (параметр aRebuild=<code>false</code>), проверяет, что дочерние узлы уже были созданы, и
   * перестраивает их, только если они не были созданы. Указание aRebuild=<code>true</code> приводит к тому, что
   * дочерные узлы будет безусловно посрроены заново.
   * <p>
   * Параметр aQuerySubtree регулирует глубину перестроения поддерева. Если aQuerySubtree=<code>true</code>, то будет
   * построно все дерево под этим узлом. Если aQuerySubtree=<code>false</code>, то будет (пере)созданы только
   * непосредственные дочки этого узла, не затрагивая внуков и более глубоких наследников.
   * <p>
   * Обратите внимание, что установка aQuerySubtree=<code>true</code> может оказаться весьма ресурсоемкой операцией -
   * будет создано всё поддерево, начиная от этого узла.
   *
   * @param aRebuild boolean - признак безусловного пересторения
   * @param aQuerySubtree boolean - признак обновления списка дочек всего потомства
   */
  void rebuildSubtree( boolean aRebuild, boolean aQuerySubtree );

  /**
   * Возвращает все узлы поддерева.
   * <p>
   * Если aIncludeSelf = true, то первым элементом в списке будет этот узел. Иначе этого узла не будет в возвращаемом
   * списке, в частности, для листа вернется пустой список. В любом случае, мтеод вернет редактируемый список.
   * <p>
   * Обратите внимание, что вызов метода может оказаться весьма ресурсоемкой операцией - будет создано всё поддерево,
   * начиная от этого узла.
   *
   * @param aIncludeSelf boolean - признак включения этго узла в результат
   * @return {@link IListEdit}&lt;{@link ITsNode}&gt; -
   */
  default IListEdit<ITsNode> listAllTsNodesBelow( boolean aIncludeSelf ) {
    IListEdit<ITsNode> result = new ElemArrayList<>();
    if( aIncludeSelf ) {
      result.add( this );
    }
    for( ITsNode n : childs() ) {
      result.add( n );
      n.internalAddNodesBelow( result );
    }
    return result;
  }

  @SuppressWarnings( "javadoc" )
  default void internalAddNodesBelow( IListEdit<ITsNode> aList ) {
    for( ITsNode n : childs() ) {
      aList.add( n );
      n.internalAddNodesBelow( aList );
    }
  }

  /**
   * Возвращает все уже созданные узлы поддерева.
   * <p>
   * Если aIncludeSelf = true, то первым элементом в списке будет этот узел. Иначе этого узла не будет в возвращаемом
   * списке, в частности, для листа вернется пустой список. В любом случае, мтеод вернет редактируемый список.
   * <p>
   * Обратите внимание, что хотя вызов метода и быстрый, но он возвращает только те узлы, которые уже были созданы и
   * закешированы.
   *
   * @param aIncludeSelf boolean - признак включения этго узла в результат
   * @return {@link IListEdit}&lt;{@link ITsNode}&gt; -
   */
  default IListEdit<ITsNode> listAllExistingTsNodesBelow( boolean aIncludeSelf ) {
    IListEdit<ITsNode> result = new ElemArrayList<>();
    if( aIncludeSelf ) {
      result.add( this );
    }
    for( ITsNode n : listExistingChilds() ) {
      result.add( n );
      n.internalAddExistingNodesBelow( result );
    }
    return result;
  }

  @SuppressWarnings( "javadoc" )
  default void internalAddExistingNodesBelow( IListEdit<ITsNode> aList ) {
    for( ITsNode n : listExistingChilds() ) {
      aList.add( n );
      n.internalAddExistingNodesBelow( aList );
    }
  }

}
