package org.toxsoft.core.tsgui.panels.opdefs;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;

/**
 * An editable extension to {@link IPanelOptionSet}.
 * <p>
 * Allows to edit values of the options but not the list of the options.
 *
 * @author hazard157
 */
public interface IPanelOptionSetEdit
    extends IPanelOptionSet, IGenericChangeEventCapable {

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
   * returns first warning. {@link ValidationResult#SUCCESS} is returned when everythin is OK.
   *
   * @return {@link ValidationResult} - validation result
   */
  ValidationResult validateValues();

}
