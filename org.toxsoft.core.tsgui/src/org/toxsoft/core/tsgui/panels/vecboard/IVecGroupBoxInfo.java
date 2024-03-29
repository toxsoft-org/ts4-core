package org.toxsoft.core.tsgui.panels.vecboard;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.graphics.*;

/**
 * Свойства оформления (обрамления) панели {@link IVecBoard} как группы контролей {@link Group}.
 *
 * @author hazard157
 */
public interface IVecGroupBoxInfo {

  /**
   * Отображаемый заголовок группы контролей.
   *
   * @return String - заголовок группы контролей
   */
  String title();

  /**
   * Всплывающая подсказка к группе.
   *
   * @return String - вспылвающая подсказка
   */
  String tooltipText();

  /**
   * Тип обрамления группы.
   * <p>
   * Обратите внимание, что разных плтформах поддерживаются разные подмножества типов границы {@link EBorderType}.
   *
   * @return {@link EBorderType} - тип обрамления (границы)
   */
  EBorderType borderType();

}
