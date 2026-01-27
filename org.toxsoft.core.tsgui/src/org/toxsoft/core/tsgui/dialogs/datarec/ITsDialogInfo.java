package org.toxsoft.core.tsgui.dialogs.datarec;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.geometry.*;

/**
 * Encapsulates the settings common for any dialog window {@link TsDialog}.
 * <p>
 * These settings apply to all dialog boxes based on {@link TitleAreaDialog} and {@link TsDialog} in particular.
 *
 * @author hazard157
 */
public interface ITsDialogInfo {

  /**
   * Returns the dialog window caption displayed in the OS window caption line.
   *
   * @return String - single line text of the dialog caption
   */
  String caption();

  /**
   * Returns the text used to set {@link TitleAreaDialog#setTitle(String)}.
   * <p>
   * Argument may be multi line with new line characters in it, usually up to 3 lines of text.
   *
   * @return String - dialog title text
   */
  String title();

  /**
   * Returns the dialog box flags - ORed bits Returns the dialog box flags..
   *
   * @return int - the dialog box flags
   */
  int flags();

  /**
   * Returned shell will be used as dialog window parent.
   *
   * @return {@link Shell} - parent shell or <code>null</code>
   */
  Shell shell();

  /**
   * Returns the minimum size of the dialog content pane.
   * <p>
   * Positive values ​​are interpreted as pixel dimensions for {@link TsComposite#setMinimumWidth(int)} and
   * {@link TsComposite#setMinimumHeight(int)}. Negative values ​​are interpreted as percentages (with a different sign)
   * of the corresponding display size for {@link TsComposite#setMinWidthDisplayRelative(int)} and
   * {@link TsComposite#setMinHeightDisplayRelative(int)}.
   * <p>
   * Values ​​of 0 and {@link SWT#DEFAULT} indicate that the size is not specified.
   *
   * @return {@link ITsPoint} - the minimum size of the dialog content pane
   */
  ITsPoint minSize();

  /**
   * Returns the maximum size of the dialog content pane.
   * <p>
   * Positive values ​​are interpreted as pixel dimensions for {@link TsComposite#setMaximumWidth(int)} and
   * {@link TsComposite#setMaximumHeight(int)}. Negative values ​​are interpreted as percentages (with a different sign)
   * of the corresponding display size for {@link TsComposite#setMaxWidthDisplayRelative(int)} and
   * {@link TsComposite#setMaxHeightDisplayRelative(int)}. *
   * <p>
   * Values ​​of 0 and {@link SWT#DEFAULT} indicate that the size is not specified.
   *
   * @return {@link ITsPoint} - the maximum size of the dialog content pane.
   */
  ITsPoint maxSize();

  /**
   * Returns the GUI context set as {@link ITsGuiContextable#tsContext()} of the dialog content.
   *
   * @return {@link ITsContext} - the context
   */
  ITsGuiContext tsContext();

}
