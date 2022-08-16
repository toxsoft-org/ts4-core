package org.toxsoft.core.tsgui.bricks.tstree;

import org.eclipse.jface.viewers.*;
import org.toxsoft.core.tsgui.bricks.tsnodes.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Просмотрщик дерева из узлов типа {@link ITsNode}.
 * <p>
 * К базовому интерфейсу {@link ITsBasicTreeViewer} добавляет возможность задавать корневые узлы и создавать столбцы.
 *
 * @author hazard157
 */
public interface ITsTreeViewer
    extends ITsBasicTreeViewer, IIconSizeableEx, IThumbSizeableEx {

  /**
   * Задает список узлов, отображаемых как корневые.
   *
   * @param aRootNodes {@link ITsCollection}&lt;{@link ITsNode}&gt; - список корневых узлов
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException у какого-либо узла родитель не это дерево
   */
  void setRootNodes( ITsCollection<ITsNode> aRootNodes );

  /**
   * Очищает содержимое дерева.
   */
  void clear();

  /**
   * Возвращает столюбцы дерева-таблицы.
   *
   * @return IList&lt;{@link ITsViewerColumn}&gt; - список столбецов, возможно пустой
   */
  IList<ITsViewerColumn> columns();

  /**
   * Добавляет столбец справа к имеющимся столбцам (последним в список {@link #columns()}).
   *
   * @param aTitle String - название
   * @param aAlignment {@link EHorAlignment} - горизонтальное выравнивание текста внутри колонки
   * @param aNameProvider {@link ITsVisualsProvider}&lt;{@link ITsNode}&gt; - постащик текст и изображения ячейки
   * @return {@link ITsViewerColumn} - созданный столбец
   */
  ITsViewerColumn addColumn( String aTitle, EHorAlignment aAlignment, ITsVisualsProvider<ITsNode> aNameProvider );

  /**
   * Удаляет все столбцы.
   */
  void removeColumns();

  /**
   * Задает поставщика шрифта для ячеек таблицы или null, если используются только шрифты по умолчанию.
   * <p>
   * Внимание: после этого метода следует обновить отрисовщики ячеек вызовом
   * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
   *
   * @param aFontProvider {@link ITableFontProvider} - поставщик шрифтов ячеек или null
   */
  void setFontProvider( ITableFontProvider aFontProvider );

  /**
   * Задает поставщика цветов для ячеек таблицы или null, если используются только цвета по умолчанию.
   * <p>
   * Внимание: после этого метода следует обновить отрисовщики ячеек вызовом
   * {@link ColumnViewer#setLabelProvider(IBaseLabelProvider) ColumnViewer.setLabelProvider(this)}.
   *
   * @param aColorProvider {@link ITableColorProvider} - поставщик цветов ячеек или null
   */
  void setColorProvider( ITableColorProvider aColorProvider );

}
