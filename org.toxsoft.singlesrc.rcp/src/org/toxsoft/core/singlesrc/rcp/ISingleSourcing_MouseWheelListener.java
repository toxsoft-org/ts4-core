package org.toxsoft.core.singlesrc.rcp;

import org.eclipse.swt.events.MouseEvent;

/**
 * Mouse wheel listener interface.
 * <p>
 * Must be used both in RCP and RAP since RCP interface<code>MouseWheelListener</code> does not exists in RAP.
 *
 * @author goga
 */
public interface ISingleSourcing_MouseWheelListener {

  /**
   * Sent when the mouse wheel is scrolled.
   *
   * @param e an event containing information about the mouse wheel action
   */
  void mouseScrolled( MouseEvent e );

}
