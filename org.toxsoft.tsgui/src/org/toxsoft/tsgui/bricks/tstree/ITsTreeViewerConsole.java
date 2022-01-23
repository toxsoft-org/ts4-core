package org.toxsoft.tsgui.bricks.tstree;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Tree;
import org.toxsoft.tsgui.utils.jface.ViewerPaintHelper;
import org.toxsoft.tslib.bricks.geometry.ITsPoint;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Control console for visual representation of the tree.
 * <p>
 * May be used both for tslib specific trees like {@link ITsTreeViewer} or for any SWT-based trees like
 * {@link TreeViewer}.
 *
 * @author hazard157
 */
public interface ITsTreeViewerConsole {

  // TODO TRANSLATE

  /**
   * Раскрывает всё поддерево узла.
   * <p>
   * Если аргумент null, метод ничего не делает.
   *
   * @param aNode {@link Object} - раскрываемый узел
   */
  void expandNode( Object aNode );

  /**
   * Раскрывает указанное количество уровней узла.
   * <p>
   * Если аргумент null, метод ничего не делает.
   *
   * @param aNode {@link Object} - раскрываемый узел
   * @param aLevel int - количество раскрываемых уровней
   */
  void expandNodeTo( Object aNode, int aLevel );

  /**
   * Раскрывает дерево на заданное количество уровней.
   *
   * @param aLevel int - количество раскрываемых уровней
   */
  void expandTreeTo( int aLevel );

  /**
   * Полностью раскрывает всё дерево.
   */
  void expandAll();

  /**
   * Схлопывает всё поддерево узла.
   * <p>
   * Если аргумент null, метод ничего не делает.
   *
   * @param aNode {@link Object} - схлопываемый узел
   */
  void collapseNode( Object aNode );

  /**
   * Полностью схлопывает всё дерево.
   */
  void collapseAll();

  /**
   * Обновляет поддерево, начиная с указанного узла.
   * <p>
   * Если узел задан в null, обновляет все дерево.
   *
   * @param aNode {@link Object} - схлопываемый узел или null для обновления всего дерева
   */
  void refresh( Object aNode );

  /**
   * Обновляет визуальное представление заданного узла.
   * <p>
   * Метод не обрабатывает изменения в структуре дерева, для обновления дерева при изменении структуры следует вызвать
   * {@link #refresh(Object)}.
   *
   * @param aNode {@link Object} - обновляемый узел
   * @throws TsNullArgumentRtException аргумент = null
   */
  void updateNode( Object aNode );

  /**
   * Возвращает выбранный узел.
   *
   * @return {@link Object} - выбранный узел, может быть <code>null</code>
   */
  Object selectedNode();

  /**
   * Делает выбранным заданный узел.
   *
   * @param aNode {@link Object} - выделяемый узел или <code>null</code> для отмены выделения
   */
  void setSelectedNode( Object aNode );

  /**
   * Выбирает предыдущий узел дерева.
   * <p>
   * Аргумент <code>true</code> позволяет циклически обойти все дерево.
   *
   * @param aOnlySiblings boolean - признак, выбора только среди узлов одного уровня
   * @return boolean - признак, что выбранный узел был сменен
   */
  boolean selectPrev( boolean aOnlySiblings );

  /**
   * Выбирает следующий узел дерева.
   * <p>
   * Аргумент <code>true</code> позволяет циклически обойти все дерево.
   *
   * @param aOnlySiblings boolean - признак, выбора только среди узлов одного уровня
   * @return boolean - признак, что выбранный узел был сменен
   */
  boolean selectNext( boolean aOnlySiblings );

  /**
   * Регистрирует помощник отрисовки дерева.
   * <p>
   * Внимание: этот метод работает только в RCP, хотя компилируется и в RAP.
   *
   * @param aPaintHelper {@link ViewerPaintHelper} - помощник рисования дерева, может быть null для отмены помощника
   */
  void installPaintHelper( ViewerPaintHelper<Tree> aPaintHelper );

  /**
   * Показывает указанный элемент в просмотрщике (прокручивает просмотрщик так, чтобы строка была видима).
   *
   * @param aNode {@link Object} - раскрываемый узел
   * @throws TsNullArgumentRtException аргумент = null
   */
  void reveal( Object aNode );

  /**
   * Возвращает размеры области отображения SWT-контроля дерева.
   *
   * @return {@link ITsPoint} - ширина и высота в пикселях
   */
  ITsPoint getTreeArea();

  /**
   * Возвращает количество столбцов в виджете дерева.
   *
   * @return int - количество столбцов
   */
  int getColumnsCount();

  /**
   * Возвращает ширину запрошенного столбца.
   *
   * @param aColumnIndex int - индекс столюца от 0 до {@link #getColumnsCount()}-1
   * @return int - ширина столбца в пикселях
   * @throws TsIllegalArgumentRtException индекс выходит за допустимые пределы
   */
  int getColumnWidth( int aColumnIndex );

}
