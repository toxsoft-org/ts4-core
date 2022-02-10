package org.toxsoft.core.tsgui.panels.vecboard;

import org.eclipse.swt.custom.SashForm;

/**
 * Раскладка контролей одним столбцом (или строкой), разедленных перемещаемыми планками.
 * <p>
 * При создании раскладки, в конструкторе указываются параметры раслакдки:
 * <ul>
 * <li>{@link #isHorizontal()} - признак расположения элементов по горизонтали слева направо (а не по вертикали, сверху
 * вниз);</li>
 * <li>{@link #sashWidth()} - ширина перемещаемой планки в пикселях.</li>
 * </ul>
 * <p>
 * Прядок добавления контроли в данную раскладку определяет их порядок в родителе сверху вниз или снизу вверх. Напомним,
 * что контроли разделяются перемещаемыми планками. Параметром размещения элемента является число {@link Integer}. Число
 * определяет относительную ширину контрля в родителе как в методе {@link SashForm#setWeights(int[])}.
 *
 * @author goga
 */
public interface IVecSashLayout
    extends IVecLayout<Integer> {

  /**
   * Возвращает признак расположения элементов по горизонтали слева направо (а не по вертикали, сверху вниз).
   *
   * @return boolean - признак расположения элементов по горизонтали слева направо (а не по вертикали, сверху вниз)
   */
  boolean isHorizontal();

  /**
   * Возвращает ширинe перемещаемой планки в пикселях.
   *
   * @return int - ширина перемещаемой планки в пикселях
   */
  int sashWidth();

}