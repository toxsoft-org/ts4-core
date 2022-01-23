package org.toxsoft.tsgui.m5_1.impl.gui.viewers;

import org.toxsoft.tsgui.m5.gui.viewers.IM5TreeViewer;
import org.toxsoft.tsgui.utils.checkcoll.ITsCheckEditSupport;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;

/**
 * Расширяет возможности дерева {@link IM5TreeViewer} способностью помечать отдельные элементы (строки).
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 * @deprecated REMOVE THIS !
 */
@Deprecated
public interface IM5TreeCheckableViewer<T>
    extends IM5TreeViewer<T> {

  /**
   * Возвращает средство работы с отмеченностью элементов.
   * <p>
   * Внимание: при генерации события изменения отметок, в методе
   * {@link IGenericChangeListener#onGenericChangeEvent(Object)} в качестве источника aSource передается ссылка на этот
   * просмотрщик {@link IM5TreeCheckableViewer} (а не на {@link #checks()}).
   *
   * @return {@link ITsCheckEditSupport} - средство работы с отмеченностью элементов
   */
  @Override
  ITsCheckEditSupport<T> checks();

}
