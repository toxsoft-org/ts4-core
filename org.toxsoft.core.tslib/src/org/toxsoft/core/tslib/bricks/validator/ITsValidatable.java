package org.toxsoft.core.tslib.bricks.validator;

import org.toxsoft.core.tslib.bricks.validator.vrl.*;

/**
 * A mix-in interface of the entity with content that supports validation.
 *
 * @author hazard157
 */
public interface ITsValidatable {

  /**
   * Checks content for validity.
   * <p>
   * Note: if check finds out multiple results then the most harmful result must be returned.
   *
   * @return {@link ValidationResult} - the check result
   */
  ValidationResult validate();

  /**
   * Checks content for validity with multiple results to return.
   * <p>
   * Default implementation adds single result returned by {@link #validate()}.
   *
   * @param aVrl {@link IVrListEdit} - the list to add the result to
   * @return {@link IVrListEdit} - the argument
   */
  default IVrListEdit validateEx( IVrListEdit aVrl ) {
    aVrl.add( validate() );
    return aVrl;
  }

}
