package org.toxsoft.core.tsgui.panels.inpled;

import org.toxsoft.core.tsgui.panels.generic.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Declaration of the concept "inplace editor".
 * <p>
 * Inplace editor is a panel for viewing and/or editing data from the some source. GUI user can start editing (put the
 * panel into the editing mode), edit data and finish editing, either with applying or canceling changes.
 * <p>
 * Note: implementation may be a viewer (ie. {@link #isViewer()} = <code>true</code>). Viewer cannot switch to edit
 * mode. Some panels may always be in editing mode, applying changes immediately while the data is being edited in the
 * widgets by the GUI user. For such a panels the edit mode changing methods has no effect.
 * <p>
 * For the easy of implementation following abstract base implementations are present:
 * <ul>
 * <li>{@link AbstractInplaceEditorViewerPanel} - TODO ???;</li>
 * <li>{@link AbstractInplaceEditorAlwaysEditingPanel} - TODO ???;</li>
 * <li>{@link InplaceEditorContainerPanel} - TODO ???.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface IInplaceEditorPanel
    extends IGenericContentPanel {

  /**
   * Determines editing state of the panel.
   *
   * @return boolean <code>true</code> - data is being edited in panel controls, <code>false</code> - a viewing mode
   */
  boolean isEditing();

  /**
   * Starts editing - puts the panel in editing mode.
   * <p>
   * Has no effect for a panels always in editing mode.
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
   * Has no effect for a panels always in editing mode.
   * <p>
   * Has no effect in the viewer mode.
   */
  void applyAndFinishEditing();

  /**
   * Cancels editing, restores initial values and finishes editing.
   * <p>
   * Has no effect for a panels always in editing mode.
   * <p>
   * Has no effect in the viewer mode.
   */
  void cancelAndFinishEditing();

  /**
   * Informs this panel about changes in data source caused be external resons.
   */
  void refresh();

}
