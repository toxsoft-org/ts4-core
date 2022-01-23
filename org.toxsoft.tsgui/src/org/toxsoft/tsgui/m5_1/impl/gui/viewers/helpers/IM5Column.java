package org.toxsoft.tsgui.m5_1.impl.gui.viewers.helpers;

import org.eclipse.swt.graphics.Image;
import org.toxsoft.tsgui.bricks.tstree.ITsViewerColumn;
import org.toxsoft.tsgui.graphics.icons.EIconSize;
import org.toxsoft.tsgui.graphics.image.EThumbSize;
import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.m5_1.impl.gui.viewers.IM5CollectionViewer;

/**
 * Колонка просмотрщика {@link IM5CollectionViewer}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IM5Column<T>
    extends ITsViewerColumn {

  /**
   * Возвращает идентификатор поля (отображаемого в колонке) в модели данных {@link IM5Model}.
   *
   * @return int - идентификатор поля
   */
  String fieldId();

  /**
   * Извлекает из объекта и возвращает текстовую строку с содержимым отображаемого поля.
   *
   * @param aObj &lt;T&gt; - моделируемый объект, значенип поля которого берется
   * @return String - содержимое поля указанного объекта в виде текста
   */
  String getCellText( T aObj );

  /**
   * Извлекает из объекта и возвращает изображение для ячейки таблицы.
   *
   * @param aObj &lt;T&gt; - моделируемый объект, значенип поля которого берется
   * @param aThubSize {@link EThumbSize} - запрашиваемый размер миниатюры
   * @return String - изображение, соответствующее содержимому поля указанного объекта
   */
  Image getCellThumb( T aObj, EThumbSize aThubSize );

  /**
   * Извлекает из объекта и возвращает значок для ячейки таблицы.
   *
   * @param aObj &lt;T&gt; - моделируемый объект, значенип поля которого берется
   * @param aIconSize {@link EIconSize} - запрашиваемый размер значка
   * @return String - изображение значка запрошенного размера, соответствующее содержимому поля указанного объекта
   */
  Image getCellIcon( T aObj, EIconSize aIconSize );

}
