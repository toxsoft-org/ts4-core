package org.toxsoft.core.tsgui.panels.opsedit.set;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;

/**
 * An editable extension to {@link IPanelOptionSetView}.
 * <p>
 * Allows to edit values of the options but not the list of the options.
 * <p>
 * On each edit generates two events:
 * <ul>
 * <li>first it generates {@link IOptionValueChangeListener#onOptionValueChange(Object, String)};</li>
 * <li>then {@link IGenericChangeListener#onGenericChangeEvent(Object)}.</li>
 * </ul>
 * When exclude option checkboxes are enabled, checkbox state change generates only generic event
 * {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
 *
 * @author hazard157
 */
public interface IPanelOptionSetEdit
    extends IPanelOptionSetView, IGenericChangeEventCapable {

  /**
   * Determines if panel is in editable state now.
   * <p>
   * By default panel is created in edtable state and this method returns <code>true</code>.
   *
   * @return booolean - the editable state flag
   */
  boolean isEditable();

  /**
   * Sets editable state of the panel.
   * <p>
   * When state is edtable, all option editors are enabled to edit values, when read-only state option editors are in
   * viewer mode.
   *
   * @param aEditable booolean - the editable state flag
   */
  void setEditable( boolean aEditable );

  /**
   * Returns option's values stored in editors.
   * <p>
   * During editing some editors may contain invalid values (eg. empty text in {@link Spinner}) so sometimes method may
   * throw an {@link TsValidationFailedRtException}. To avoid exceptions check values validity by the method
   * {@link #validateValues()}.
   *
   * @return {@link IOptionSet} - the options values
   * @throws TsValidationFailedRtException method {@link #validateValues()} failed
   */
  @Override
  IOptionSet getValues();

  /**
   * Checks the validity of the values in the editors.
   * <p>
   * Method checks editors one by one and returns first error (if any). If no error was encountered remembers and
   * returns first warning. {@link ValidationResult#SUCCESS} is returned when everything is OK.
   *
   * @return {@link ValidationResult} - validation result
   */
  ValidationResult validateValues();

  /**
   * Returns option values change eventer.
   * <p>
   * Option value change events are fired each time user changes values in option VALEDs. As notes in
   * {@link #getValues()} some changes may lead to the invalid option value so {@link #validateValues()} fail and
   * {@link #getValues()} may throw an exception.
   *
   * @return {@link ITsEventer}&lt;{@link IOptionValueChangeListener}&gt; - the eventer
   */
  ITsEventer<IOptionValueChangeListener> optionValueChangeEventer();

}
