package org.toxsoft.core.tslib.av.avtree;

/**
 * Элемент пути в дереве значений {@link IAvTree}.
 * <p>
 * Из последовательнсти элементов состоит путь в дереве значений {@link IAvTreePath#elements()}. Каждый элемент пути
 * ссылается либо на одно из значений полей {@link IAvTree#fields()} или на узел (на одно из дочерних деревьев)
 * {@link IAvTree#nodes()}. Соответственно, в описании дерева {@link IAvTreeInfo} элемент пути ссылается либо на
 * описание поля, либо на описание узла.
 * <p>
 * Ссылается ли элемент на поле или узел, определяется методом {@link #isNode()}. Ссылка осущетвляется идентификатором
 * {@link #id()}. При навигации по дереву {@link IAvTree}, идентификатор указывает на поле или на узел (в зависимости от
 * {@link #isNode()}. При навигации по описанию {@link IAvTreeInfo}, если узел является семейством (то есть,
 * {@link IAvTreeInfo#isFamily()} = true), то идентификатор {@link #id()} указывает, как обычно, на узел а следующий
 * элемент пути, с {@link #isNode()}=true, указывает на член семейства.
 * <p>
 * Прин авигации по дереву значении {@link IAvTree} используются также указание на элмент массива. Для этого следует
 * проверить, что элемиент пути сслыается на элемент в массиве (то есть {@link #isArrayElement()} = true) и надо взять
 * элмент массива с индексом {@link #arrayElementIndex()}.
 * 
 * @author hazard157
 */
public interface IAvTreePathElement {

  /**
   * Возвращает признак, что элемент пути ссылается на узел, а не на поле дерева.
   * 
   * @return boolean - признак, что элемент пути ссылается на узел, а не на поле дерева
   */
  boolean isNode();

  /**
   * Возвращает идентификатор поля, узла или семейства.
   * <p>
   * Если {@link #isNode()}= false, то указывает на поле. Иначе указывает на узел дерева, а внутри узла, если это
   * описание семейства, то указывает на член семейства.
   * 
   * @return String - идентификатор поля, узла или семейства
   */
  String id();

  /**
   * Определяет, сслыается ли этот элемент пути на элемент массива в узле дерева значении {@link IAvTree}.
   * 
   * @return boolean - признак сслыки на элемент массива<br>
   *         <b>true</b> - это сслвка на элемент массива с индексом {@link #arrayElementIndex()};<br>
   *         <b>false</b> - это сслыка на узел или поле единичного дерева..
   */
  boolean isArrayElement();

  /**
   * Индекс элемента массива при {@link #isArrayElement()} = true.
   * 
   * @return int - индекс элемента массива
   */
  int arrayElementIndex();

}
