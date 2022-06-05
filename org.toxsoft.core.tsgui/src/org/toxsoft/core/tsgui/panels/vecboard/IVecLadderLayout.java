package org.toxsoft.core.tsgui.panels.vecboard;

/**
 * Раскладка контролей одним столбцом, вертикальной лесенкой.
 * <p>
 * При создании раскладки, в конструкторе указываются параметры раслакдки:
 * <ul>
 * <li>{@link #isLabelsShown()} - показывать ли подписи к элементам. Когда подписы включены, отдельные элементы могут
 * все равно не иметь подписи, если {@link IVecLadderLayoutData#isLabelShown()}=false. Когда подписы скрыты в раскладке,
 * ни один элемент не имеет отображаемых подписей.</li>
 * </ul>
 * <p>
 * Добавление контролей происходит сверху вниз по лестнице.
 *
 * @author hazard157
 */
public interface IVecLadderLayout
    extends IVecLayout<IVecLadderLayoutData> {

  /**
   * Вовзращает признак показа подписей к полям ввода.
   *
   * @return boolean - признак показа подписей к полям ввода
   */
  boolean isLabelsShown();

}
