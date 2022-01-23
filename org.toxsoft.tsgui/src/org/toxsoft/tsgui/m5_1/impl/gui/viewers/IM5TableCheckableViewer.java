package org.toxsoft.tsgui.m5_1.impl.gui.viewers;

import org.toxsoft.tsgui.m5.gui.viewers.IM5TableViewer;
import org.toxsoft.tsgui.utils.checkcoll.ITsCheckEditSupport;

/**
 * Расширяет возможности таблицы {@link IM5TableViewer} способностью помечать отдельные элементы (строки).
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 * @deprecated REMOVE THIS !
 */
@Deprecated
public interface IM5TableCheckableViewer<T>
    extends IM5TableViewer<T> {

  /**
   * Возвращает средство работы с отмеченностью элементов.
   *
   * @return {@link ITsCheckEditSupport} - средство работы с отмеченностью элементов
   */
  @Override
  ITsCheckEditSupport<T> checks();

}
