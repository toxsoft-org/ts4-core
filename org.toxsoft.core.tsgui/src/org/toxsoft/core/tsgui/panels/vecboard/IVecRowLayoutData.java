package org.toxsoft.core.tsgui.panels.vecboard;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;

/**
 * Параметры элементов в раскладке {@link IVecRowLayout}.
 * <p>
 * Каждый элемент в {@link IVecRowLayout} - может иметь заданную ширину {@link #width()} и высоту {@link #height()}.
 * Соответствующая размерность может быть {@link SWT#DEFAULT}, чтобы его определяла метод
 * {@link Control#computeSize(int, int, boolean)}.
 *
 * @author hazard157
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
