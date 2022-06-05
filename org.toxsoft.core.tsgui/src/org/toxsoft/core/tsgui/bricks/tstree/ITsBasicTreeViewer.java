package org.toxsoft.core.tsgui.bricks.tstree;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.tstree.impl.TsNodeKind;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tsgui.utils.jface.ViewerPaintHelper;
import org.toxsoft.core.tslib.coll.primtypes.IIntList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

// TODO TRANSLATE

/**
 * Базовый интерфейс дерева из узлов типа {@link ITsNode}.
 *
 * @author hazard157
 */
public interface ITsBasicTreeViewer
    extends ITsSelectionProvider<ITsNode>, ITsDoubleClickEventProducer<ITsNode>, ITsKeyDownEventProducer, ITsNode,
    ILazyControl<Control> {

  /**
   * Тип узла, возвращаемый методом {@link ITsBasicTreeViewer#kind()}.
   */
  ITsNodeKind<Object> TREE_KIND = new TsNodeKind<>( ITsBasicTreeViewer.class.getSimpleName(), TsLibUtils.EMPTY_STRING,
      TsLibUtils.EMPTY_STRING, Object.class, true, null );

  /**
   * Возвращает средства управления визуальным представлением дерева.
   *
   * @return {@link ITsTreeViewerConsole} - средства управления визуальным представлением дерева
   * @throws TsIllegalStateRtException метод вызван до создания контроля
   */
  ITsTreeViewerConsole console();

  /**
   * Возвращает путь к текущему выбранному элементу.
   * <p>
   * Полученный путь имеет смысл только в качестве аргумента метода {@link #setSelectedPath(IIntList)}.
   *
   * @return {@link IIntList} - путь к выбранному узлу в дереве или пустой список, если ничего не выбрано
   */
  IIntList getSelectedPath();

  /**
   * Выбирает узел по пути, ранее полученной методом {@link #getSelectedPath()}.
   * <p>
   * Обратите внимание, что если содержимое дерева отличается от того, что было при вызове {@link #getSelectedPath()} (в
   * том числе, применен другой фильтр), то может оказаться, что быдет выбран неверный узел, или вообще ни один узел не
   * будет выбран.
   *
   * @param aPathIndexes {@link IIntList} - путь к выбранному элементу, пустой список для очистки выбора
   * @throws TsNullArgumentRtException аргумент = null
   */
  void setSelectedPath( IIntList aPathIndexes );

  /**
   * Задает помощник пользовательской отрисовки ячеек дерева.
   * <p>
   * Для удаления текущего помощника (восстановления рисования по умолчанию) следует задать null.
   * <p>
   * Внимание: метод работает только в RCP, в RAP ничего не делает.
   *
   * @param aHelper {@link ViewerPaintHelper} - помощник отрисовки дерева или null
   */
  void setTreePaintHelper( ViewerPaintHelper<Tree> aHelper );

}
