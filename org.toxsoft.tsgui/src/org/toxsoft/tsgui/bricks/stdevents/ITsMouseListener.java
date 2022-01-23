package org.toxsoft.tsgui.bricks.stdevents;

import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.toxsoft.tslib.bricks.geometry.ITsPoint;

/**
 * Слушатель событий мыши.
 * <p>
 * Обратите внимание, что все координаты мыши в этом интерфейсе передаются относительно дисплея. Если нужно пересчитать
 * относительно какой-либо контроли, используйте методв {@link Display#map(Control, Control, int, int)}.
 *
 * @author goga
 * @param <S> - тип источника сообщения
 */
public interface ITsMouseListener<S> {

  /**
   * Идентификация кнопки мыши.
   *
   * @author goga
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
   * @param aSource &lt;S&gt; - источник сообщения
   * @param aButton {@link EMouseButton} - нажатая кнопка мыши
   * @param aCoors {@link ITsPoint} - координаты мыши относительно дисплея
   */
  default void onMouseButtonDown( S aSource, EMouseButton aButton, ITsPoint aCoors ) {
    // nop
  }

  /**
   * Вызвается при отпускании любой кнопки мыши.
   *
   * @param aSource &lt;S&gt; - источник сообщения
   * @param aButton {@link EMouseButton} - отпущенная кнопка мыши
   * @param aCoors {@link ITsPoint} - координаты мыши относительно дисплея
   */
  default void onMouseButtonUp( S aSource, EMouseButton aButton, ITsPoint aCoors ) {
    // nop
  }

  /**
   * Вызвается при двойном щелчке левой кнопкой мыши.
   *
   * @param aSource &lt;S&gt; - источник сообщения
   * @param aCoors {@link ITsPoint} - координаты мыши относительно дисплея
   */
  default void onMouseDoubleClick( S aSource, ITsPoint aCoors ) {
    // nop
  }

  /**
   * Вызывается когда пользователь прокрутил колеско мыши.
   *
   * @param aSource &lt;S&gt; - источник сообщения
   * @param aScrollLines int - кол-во прокручиваемых строк (отрицательные значения - прокрутка вниз)
   */
  default void onMouseWheelEvent( S aSource, int aScrollLines ) {
    // nop
  }

}
