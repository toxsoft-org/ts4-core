package org.toxsoft.core.tsgui.bricks.stdevents;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Слушатель событий мыши.
 * <p>
 * Обратите внимание, что все координаты мыши в этом интерфейсе передаются относительно дисплея. Если нужно пересчитать
 * относительно какой-либо контроли, используйте методв {@link Display#map(Control, Control, int, int)}.
 *
 * @author hazard157
 */
public interface ITsMouseListener {

  /**
   * Идентификация кнопки мыши.
   *
   * @author hazard157
   */
  public enum EMouseButton {
    /**
     * Левая кнопка мыши.
     */
    LEFT,
    /**
     * Средняя кнопка мыши.
     */
    MIDDLE,
    /**
     * Правая кнопка мыши.
     */
    RIGHT,
    /**
     * Другие кнопки (не используются в tsgui)
     */
    OTHER
  }

  /**
   * Вызвается при щелчке любой кнопкой мыши.
   *
   * @param aSource Object - источник сообщения
   * @param aButton {@link EMouseButton} - нажатая кнопка мыши
   * @param aCoors {@link ITsPoint} - координаты мыши относительно дисплея
   */
  default void onMouseButtonDown( Object aSource, EMouseButton aButton, ITsPoint aCoors ) {
    // nop
  }

  /**
   * Вызвается при отпускании любой кнопки мыши.
   *
   * @param aSource Object - источник сообщения
   * @param aButton {@link EMouseButton} - отпущенная кнопка мыши
   * @param aCoors {@link ITsPoint} - координаты мыши относительно дисплея
   */
  default void onMouseButtonUp( Object aSource, EMouseButton aButton, ITsPoint aCoors ) {
    // nop
  }

  /**
   * Вызвается при двойном щелчке левой кнопкой мыши.
   *
   * @param aSource Object - источник сообщения
   * @param aCoors {@link ITsPoint} - координаты мыши относительно дисплея
   */
  default void onMouseDoubleClick( Object aSource, ITsPoint aCoors ) {
    // nop
  }

  /**
   * Вызывается когда пользователь прокрутил колеско мыши.
   *
   * @param aSource Object - источник сообщения
   * @param aScrollLines int - кол-во прокручиваемых строк (отрицательные значения - прокрутка вниз)
   */
  default void onMouseWheelEvent( Object aSource, int aScrollLines ) {
    // nop
  }

}
