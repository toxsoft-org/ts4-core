package org.toxsoft.core.tsgui.panels.inpled;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Inplace editor content panel.
 * <p>
 * Note: panel may be read-only so it does not supports editing at all. Editable panels may be in viewer mode or be in
 * an editing state. Read-only panels alwayes remain in viewer mode.
 *
 * @author hazard157
 */
public interface IInplaceContentPanel
    extends IGenericChangeEventCapable, ILazyControl<Control> {

  /**
   * Determines if panel supported editing or is a viewer.
   *
   * @return boolean - <code>true</code> {@link #startEditing()} may be called, <code>false</code> - panel can't edit
   */
  boolean isReadonly();

  /**
   * Starts editing - puts the panel in editing mode.
   * <p>
   * Editing may be finished either by {@link #actionOk()} or {@link #actionCancel()} methods.
   *
   * @throws TsUnsupportedFeatureRtException panel is the viewer
   */
  void startEditing();

  /**
   * Determinies current state of the panel.
   *
   * @return boolean <code>true</code> for editing statem, <code>false</code> for viewer mode
   */
  boolean isEditing();

  /**
   * Applies the changes to the underline editad entity and finshes edinting.
   * <p>
   * This is the same action as {@link #actionApply()} but the panel returns to the viewer mode.
   */
  void actionOk();

  /**
   * Restores last applieed values in the widgets and finshes edinting.
   * <p>
   * This is the same action as {@link #actionRevert()} but the panel returns to the viewer mode.
   */
  void actionCancel();

  /**
   * Applies the changes to the underline editad entity.
   * <p>
   * This is the same action as {@link #actionOk()} but the panel remains in the editing state.
   */
  void actionApply();

  /**
   * Restores last applieed values in the widgets.
   * <p>
   * This is the same action as {@link #actionCancel()} but the panel remains in the editing state.
   */
  void actionRevert();

  /**
   * Determines if {@link #actionRestoreDefaults()} is supported.
   *
   * @return true - if method {@link #actionRestoreDefaults()} may be called
   */
  boolean isActionRestoreDefaultsSupported();

  /**
   * Restores default (some predefined) values to the widgets.
   *
   * @throws TsUnsupportedFeatureRtException action is not supported
   */
  void actionRestoreDefaults();

  /**
   * Validates the content of the panel.
   *
   * @return {@link ValidationResult} - validation result
   */
  ValidationResult validate();

}
