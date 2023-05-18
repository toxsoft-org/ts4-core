package org.toxsoft.core.singlesrc;

import org.eclipse.swt.events.MouseEvent;

/**
 * Mouse track listener interface.
 * <p>
 * Must be used both in RCP and RAP since RCP interface<code>MouseTrackListener</code> does not exists in RAP.
 *
 * @author mvk
 */
public interface ISingleSourcing_MouseTrackListener {

  /**
   * Sent when the mouse pointer passes into the area of the screen covered by a control.
   *
   * @param e an event containing information about the mouse enter
   */
  void mouseEnter( MouseEvent e );

  /**
   * Sent when the mouse pointer passes out of the area of the screen covered by a control.
   *
   * @param e an event containing information about the mouse exit
   */
  void mouseExit( MouseEvent e );

  /**
   * Sent when the mouse pointer hovers (that is, stops moving for an (operating system specified) period of time) over
   * a control.
   *
   * @param e an event containing information about the hover
   */
  void mouseHover( MouseEvent e );

}
