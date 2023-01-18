package org.toxsoft.core.tsgui.panels.inpled;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to view and edit {@link IInplaceContentPanel}.
 * <p>
 * Contains inplace content {@link IInplaceContentPanel}, optional validation result pane and the button bar. Initially
 * there is only "edit" button in button bar. Presing "Edit" switches content to the editing state and "OK", "Cancel",
 * "Revert", "Apply", "Restore" buttons appear on button bar. "OK" and "Cancel" button finishes the editing and returns
 * inplace editor to the viewer mode.
 * <p>
 * For validation message panel respects {@link ValidationResultPanel} options.
 *
 * @author hazard157
 */
public interface IInplaceEditorPanel
    extends ILazyControl<Control> {

  /**
   * Returns the content panel.
   *
   * @return {@link IInplaceContentPanel} - the content panek or <code>null</code>
   */
  IInplaceContentPanel contentPanel();

  /**
   * Sets the content panel.
   * <p>
   * Existing content will be replaced. If exisintg panel is in editing mode it will be canc elled before remove,
   * <p>
   * Note: argument must be the panel with <b>no SWT control created</b>.
   *
   * @param aPanel {@link IInplaceContentPanel} - new content panel or <code>null</code> to clear content
   * @throws TsIllegalArgumentRtException argument has SWT control created
   * @throws TsIllegalArgumentRtException argument is in editing state
   */
  void setContentPanel( IInplaceContentPanel aPanel );

}
