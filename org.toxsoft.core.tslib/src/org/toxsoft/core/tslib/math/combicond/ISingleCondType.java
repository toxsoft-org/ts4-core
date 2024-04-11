package org.toxsoft.core.tslib.math.combicond;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Description of the single condition type.
 * <p>
 * In may comments this interface (and subclasses) are references by <b>SCT</b> abbreviation.
 *
 * @author hazard157
 */
public interface ISingleCondType
    extends IStridableParameterized {

  /**
   * Returns description of the condition parameters.
   *
   * @return {@link IStridablesList}&lt;{@link IDataDef}&gt; - description of the condition parameters
   */
  IStridablesList<IDataDef> paramDefs();

  /**
   * Checks condition parameters for validity.
   *
   * @param aCondParams {@link IOptionSet} - condition parameters
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult validateParams( IOptionSet aCondParams );

  /**
   * Return human-readable description of the condition, based on the parameters.
   * <p>
   * If parameters can not be validated, returns error message from {@link #validateParams(IOptionSet)}.
   *
   * @param aCondParams {@link IOptionSet} - condition parameters
   * @return String - human-readable description, never is <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  String humanReadableDescription( IOptionSet aCondParams );

}
