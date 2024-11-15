package org.toxsoft.core.tsgui.ved.comps;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;

/**
 * Интерфейс визуального элемента - "кнопка".<br>
 *
 * @author vs
 */
public interface IViselButton
    extends IVedVisel {

  /**
   * Возвращает состояние кнопки.<br>
   *
   * @return {@link EButtonViselState} - состояние кнопки
   */
  EButtonViselState buttonState();

  /**
   * Возвращает контекст доступный кнопке
   *
   * @return {@link ITsGuiContext} - соответствующий контекст
   */
  ITsGuiContext tsContext();

  /**
   * Возвращает признак того является ли кнопка отмеченной.<br>
   *
   * @return <b>true</b> - кнопка отмечена<br>
   *         <b>false</b> - кнопка не отмечена
   */
  boolean isChecked();
}
