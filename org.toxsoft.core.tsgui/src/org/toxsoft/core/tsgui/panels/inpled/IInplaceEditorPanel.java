package org.toxsoft.core.tsgui.panels.inpled;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Panel to view and edit some content.
 * <p>
 * Contains in-place content panel, optional validation result pane and the button bar. Initially there is only "Edit"
 * button in button bar. Pressing "Edit" switches content to the editing state and "OK", "Cancel", "Revert", "Apply",
 * "Restore" buttons appear on button bar. "OK" and "Cancel" button finishes the editing and returns in-place editor to
 * the viewer mode.
 * <p>
 * For validation message panel respects {@link ValidationResultPanel} options.
 *
 * @author hazard157
 */
public sealed interface IInplaceEditorPanel
    extends IGenericContentPanel
    permits InplaceEditorPanel {

  /**
   * Determines current state of the panel.
   *
   * @return boolean <code>true</code> for editing state, <code>false</code> for viewer mode
   */
  boolean isEditing();

  /**
   * Starts editing - puts the panel in editing mode.
   *
   * @throws TsUnsupportedFeatureRtException panel is the viewer
   */
  void startEditing();

  /**
   * Determines if widgets contains values different than edited content.
   *
   * @return boolean - <code>true</code> when panel contains values not applied to the content
   */
  boolean isChanged();

  /**
   * Applies changed values to the underlying content and finishes editing.
   * <p>
   * Has no effect in the viewer mode.
   */
  void applyAndFinishEditing();

  /**
   * Cancels editing, restores initial values and finishes editing.
   * <p>
   * Has no effect in the viewer mode.
   */
  void cancelAndFinishEditing();

  /**
   * Informs this panel about changes in content panel.
   */
  void refresh();

}
