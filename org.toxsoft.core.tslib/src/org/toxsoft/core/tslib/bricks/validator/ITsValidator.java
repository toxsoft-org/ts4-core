package org.toxsoft.core.tslib.bricks.validator;

import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Interface to check any entity (value) for validity.
 * <p>
 * This is implementation of pattern "Strategy".
 *
 * @author hazard157
 * @param <V> - checked entity class
 */
public interface ITsValidator<V> {

  /**
   * Special case validator, returning success on any check.
   */
  @SuppressWarnings( "rawtypes" )
  ITsValidator PASS = new PassTsValidator();

  /**
   * Special case validator, returning error on any check.
   */
  @SuppressWarnings( "rawtypes" )
  ITsValidator FAIL = new FailTsValidator();

  /**
   * The implementation must check the value against specified criteria in the validator.
   *
   * @param aValue &lt;V&gt; - checked value (entity)
   * @return {@link ValidationResult} - the validation result
   * @throws TsNullArgumentRtException argument is <code>null</code>
   */
  ValidationResult validate( V aValue );

  /**
   * Checks for validation error absence.
   *
   * @param aValue &lt;V&gt; - checked value (entity)
   * @return boolean - error absence flag while checking using method {@link #validate(Object)}<br>
   *         <b>true</b> - validation result is either {@link EValidationResultType#WARNING}, or
   *         {@link EValidationResultType#OK};<br>
   *         <b>false</b> - validation result is {@link EValidationResultType#ERROR}.
   * @throws TsNullArgumentRtException argument is <code>null</code>
   */
  default boolean isValid( V aValue ) {
    return !validate( aValue ).isError();
  }

  /**
   * Validates value and throws an exception in case of error.
   *
   * @param aValue &lt;V&gt;} - checked value (entity)
   * @return &lt;V&gt; - returns argument
   * @throws TsNullArgumentRtException argument is <code>null</code>
   * @throws TsValidationFailedRtException {@link #validate(Object)} returned {@link ValidationResult#isError()}
   */
  default V checkValid( V aValue ) {
    TsValidationFailedRtException.checkError( validate( aValue ) );
    return aValue;
  }

}
