package org.toxsoft.core.tsgui.dialogs.datarec;

import org.toxsoft.core.tsgui.dialogs.ETsDialogCode;

/**
 * Package internal intarface to handle dialog button press.
 *
 * @author goga
 */
interface IButtonPressCallback {

  /**
   * Implementation handles button press.
   * <p>
   * Note: this method with argument {@link ETsDialogCode#CLOSE} is called also when user closes dialog window (eg. from
   * window menu or X close button in window title bar).
   *
   * @param aButtonId {@link ETsDialogCode} - pressed button ID
   * @return boolean - flags to continues internal handling <b>true</b> - default action on button will be performed (eg
   *         {@link ETsDialogCode#CLOSE} will close the dialog window;<br>
   *         <b>false</b> - no further actions will be performad.
   */
  boolean onDialogButtonPressed( ETsDialogCode aButtonId );

}
