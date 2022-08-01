package org.toxsoft.core.tsgui.bricks.swtevents;

import org.eclipse.swt.events.*;

/**
 * SWT key press and release events listener.
 *
 * @author hazard157
 */
public interface ISwtKeyListener
    extends KeyListener {

  @Override
  default void keyPressed( KeyEvent e ) {
    // nop
  }

  @Override
  default void keyReleased( KeyEvent e ) {
    // nop
  }

}
