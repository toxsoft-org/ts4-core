package org.toxsoft.core.tsgui.panels.vecboard;

import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.utils.icons.*;

// TODO TRANSLATE

/**
 * Параметры элементов в раскладке {@link IVecTabLayout}.
 * <p>
 * Каждый элемент в {@link IVecTabLayout} - это отдельная вкладка, и параметры задают отображаемые параметры вкладки.
 *
 * @author hazard157
 */
public interface IVecTabLayoutData
    extends IIconIdable {

  /**
   * Возвращает название, отображаемое на ярлыке вкладки.
   *
   * @return String - название, отображаемое на ярлыке вкладки
   */
  String name();

  /**
   * Возвращает всплывающую подсказку вкладки.
   *
   * @return String - всплывающая подсказка вкладки
   */
  String tooltipText();

  /**
   * Возвращает размер значка, отображаемого у вкладки.
   *
   * @return {@link EIconSize} - размер значка
   */
  EIconSize iconSize();

}
