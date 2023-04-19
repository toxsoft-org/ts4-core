package org.toxsoft.core.tsgui.panels.pgv;

import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.uievents.*;
import org.toxsoft.core.tsgui.graphics.image.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.utils.margins.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Displays items of type &lt;V&gt; as the thumbnails grid.
 * <p>
 * Viewer may be configured at creation time by the constants listed in {@link IPicsGridViewerConstants}.
 *
 * @author hazard157
 * @param <V> - displayed items type
 */
public interface IPicsGridViewer<V>
    extends ITsDoubleClickEventProducer<V>, ITsSelectionProvider<V>, IThumbSizeableEx, ITsUserInputProducer,
    ITsKeyInputProducer, ITsMouseInputProducer, ITsContextable {

  // TODO item popup menu support

  /**
   * Returns the displayed items.
   *
   * @return {@link IList}&lt;V&gt; - the list of displayed items in the order of placement
   */
  IList<V> items();

  // TODO TRANSLATE

  /**
   * Задает отображаемые сущности.
   * <p>
   * Для очистки следует задать пустой список или <code>null</code>.
   *
   * @param aItems {@link IList}&lt;V&gt; - список отображаемых сущностей, может быть <code>null</code>
   */
  void setItems( IList<V> aItems );

  /**
   * Возвращает параметры настройки сетки миниатюр.
   *
   * @return {@link ITsGridMargins} - параметры настройка границ и интервалов рисования сетки значков
   */
  ITsGridMargins getMargins();

  /**
   * Задает параметры настройки сетки миниатюр.
   * <p>
   * Изменение параметров применяется немедленно.
   *
   * @param aMargins {@link ITsGridMargins} - параметры настройка границ и интервалов рисования сетки значков
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setMargins( ITsGridMargins aMargins );

  /**
   * Возвращает поставщик текстов к миниатюрам.
   *
   * @return {@link ITsVisualsProvider} - поставщик текстов к миниатюрам
   */
  ITsVisualsProvider<V> getVisualsProvider();

  /**
   * Задает поставщик текстов к миниатюрам.
   *
   * @param aVisualsProvider {@link ITsVisualsProvider} - поставщик текстов к миниатюрам
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void setVisualsProvider( ITsVisualsProvider<V> aVisualsProvider );

  /**
   * Обновляет панель, включая применение измененных параметров контекста.
   */
  void refresh();

  /**
   * Возвращает SWT контрол реализации интерфейса.
   *
   * @return {@link TsComposite} - SWT-контроль
   */
  TsComposite getControl();

}
