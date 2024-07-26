package org.toxsoft.core.tslib.bricks.validator;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Pattern: validate (check possibility) if some changes (edit actions) may be performed..
 *
 * @author hazard157
 * @param <V> - the validator type
 */
public interface ITsValidationSupport<V> {

  /**
   * Returns the validator.
   * <p>
   * The validator performs validation by sequentially calling all registered validators. First built-in (non-removable)
   * validators are called and then the validators added by the {@link #addValidator(Object)} method are called in the
   * order they were added. The first error {@link ValidationResult#isError()} = <code>true</code> is returned
   * immediately. The warning is returned according to the rules of
   * {@link ValidationResult#firstNonOk(ValidationResult, ValidationResult)}.
   *
   * @return &ltV&gt; - the validator
   */
  V validator();

  /**
   * Add the validator.
   * <p>
   * If validator is already registered then method does nothing.
   *
   * @param aValidator &ltV&gt; - the validator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void addValidator( V aValidator );

  /**
   * Removes the validator.
   * <p>
   * If validator is not registered then method does nothing.
   *
   * @param aValidator &ltV&gt; - the validator
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void removeValidator( V aValidator );

  /**
   * Temporary disables the specified validator.
   * <p>
   * After call of this method the {@link #validator()} will not include disabled validator until the
   * {@link #resumeValidator(Object)} call. The order of the argument - validator in {@link #validator()} calls list is
   * not changed.
   * <p>
   * Unregistered validator is ignored.
   *
   * @param aValidator &ltV&gt; - the validator to be disabled
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void pauseValidator( V aValidator );

  /**
   * Enables previously disabled validator.
   * <p>
   * Unregistered validator is ignored.
   *
   * @param aValidator &ltV&gt; - the validator to be disabled
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void resumeValidator( V aValidator );

}
