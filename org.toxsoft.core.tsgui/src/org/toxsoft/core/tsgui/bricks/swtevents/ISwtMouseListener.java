package org.toxsoft.core.tsgui.bricks.swtevents;

import org.eclipse.swt.events.*;
import org.toxsoft.core.singlesrc.rcp.*;

/**
 * All SWT mouse events listener.
 *
 * @author vs
 * @author hazard157
 */
public interface ISwtMouseListener
    extends MouseListener, MouseMoveListener, ISingleSourcing_MouseWheelListener, MouseTrackListener {

  /**
   * Left mouse button in {@link MouseEvent#button}.
   */
  int BTN_LEFT = 1;

  /**
   * Middle mouse button in {@link MouseEvent#button}.
   */
  int BTN_MIDDLE = 2;

  /**
   * Right mouse button in {@link MouseEvent#button}.
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
