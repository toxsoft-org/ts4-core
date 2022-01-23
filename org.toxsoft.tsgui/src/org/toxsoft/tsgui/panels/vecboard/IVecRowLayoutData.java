package org.toxsoft.tsgui.panels.vecboard;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.panels.vecboard.impl.VecRowLayoutData;

/**
 * Параметры элементов в раскладке {@link IVecRowLayout}.
 * <p>
 * Каждый элемент в {@link IVecRowLayout} - может иметь заданную ширину {@link #width()} и высоту {@link #height()}.
 * Соответствующая размерность может быть {@link SWT#DEFAULT}, чтобы его определяла метод
 * {@link Control#computeSize(int, int, boolean)}.
 *
 * @author goga
 */
public interface IVecRowLayoutData {

  /**
   * Параметры, рекомендованные по умолчанию.
   */
  IVecRowLayoutData DEFAULT = new VecRowLayoutData( SWT.DEFAULT, SWT.DEFAULT );

  /**
   * Возвращает предпочтительную ширину контроля.
   *
   * @return int - предпочтительная ширина или {@link SWT#DEFAULT}
   */
  int width();

  /**
   * Возвращает предпочтительную высоту контроля.
   *
   * @return int - предпочтительная высота или {@link SWT#DEFAULT}
   */
  int height();

}
