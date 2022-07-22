package org.toxsoft.core.tsgui.ved.incub.tsmskbd;

import org.eclipse.swt.events.*;

/**
 * Обработчик "мыши".
 * <p>
 * В большинстве случаев необходимо обрабатывать все события "мыши", поэтому все java интерфейсы слушателей событий мыши
 * были сведены в один, для упрощения.
 *
 * @author vs
 */
public interface ITsMouseListener
    extends MouseListener, MouseMoveListener, MouseWheelListener, MouseTrackListener {

  /**
   * Левая кнопка мыши
   */
  int BTN_LEFT = 1;

  /**
   * Средняя кнопка мыши
   */
  int BTN_MIDDLE = 2;

  /**
   * Правая кнопка мыши
   */
  int BTN_RIGHT = 3;

  @Override
  default void mouseDoubleClick( MouseEvent aEvent ) {
    // nop
  }

  @Override
  default void mouseDown( MouseEvent aEvent ) {
    // nop
  }

  @Override
  default void mouseUp( MouseEvent aEvent ) {
    // nop
  }

  @Override
  default void mouseMove( MouseEvent aEvent ) {
    // nop
  }

  @Override
  default void mouseScrolled( MouseEvent aEvent ) {
    // nop
  }

  @Override
  default void mouseEnter( MouseEvent aEvent ) {
    // nop
  }

  @Override
  default void mouseExit( MouseEvent aEvent ) {
    // nop
  }

  @Override
  default void mouseHover( MouseEvent aEvent ) {
    // nop
  }

}
