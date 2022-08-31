package org.toxsoft.core.tslib.av.avtree;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Путь к конкретному элементу в дереве {@link IAvTree} и/или в описании дерева {@link IAvTreeInfo} значений.
 * 
 * @author goga
 */
public interface IAvTreePath {

  /**
   * Возвращает элементы пути.
   * 
   * @return IList&lt;{@link IAvTreePathElement}&gt; - список элементов пути
   */
  IList<IAvTreePathElement> elements();

  /**
   * Определяет, пустой ли путь.
   * <p>
   * Возвращает то же самое, что и <code>elements().isEmpty()</code>.
   * 
   * @return boolean - признак этсутствия элементов в пути
   */
  boolean isEmpty();

  /**
   * Возвращает последный элемент пути.
   * <p>
   * Возвращает то же самое, что и <code>elements().get( elements().size() - 1 )</code>.
   * 
   * @return {@link IAvTreePathElement} - последный элемент пути
   * @throws TsIllegalArgumentRtException путь пустой, не содержит ни одного элемента
   */
  IAvTreePathElement lastElement();

  /**
   * Определяет, ссылается ли путь на поле или на узел.
   * <p>
   * Пустой путь считается, что ссылается на корневой узел дерева. Для непуcтого дерева возвращает
   * {@link IAvTreePathElement#isNode()} последнего узла.
   * 
   * @return boolean - признак, что путь ссылается на узел, не на дерево
   */
  boolean isNode();

}
