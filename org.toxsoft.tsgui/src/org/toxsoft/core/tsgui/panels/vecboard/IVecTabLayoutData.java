package org.toxsoft.core.tsgui.panels.vecboard;

import org.toxsoft.core.tsgui.graphics.icons.EIconSize;
import org.toxsoft.core.tsgui.graphics.icons.ITsIconManager;

/**
 * Параметры элементов в раскладке {@link IVecTabLayout}.
 * <p>
 * Каждый элемент в {@link IVecTabLayout} - это отдельная вкладка, и параметры задают отображаемые параметры вкладки.
 *
 * @author goga
 */
public interface IVecTabLayoutData {

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
   * Возвращает идентификатор (имя) значка вкладки.
   * <p>
   * Значок вкладки загружается методом {@link ITsIconManager#loadStdIcon(String, EIconSize)}. Таким образом, можно
   * использовать встроенные в tsgui значки любых размеров, или собственные значки, предварительно зарегистрировав их в
   * {@link ITsIconManager}.
   *
   * @return String - имя значка или пустая строка, при отсутствии значка у вкладки
   */
  String iconId();

  /**
   * Возвращает размер значка, отображаемого у вкладки.
   *
   * @return {@link EIconSize} - размер значка
   */
  EIconSize iconSize();

}
