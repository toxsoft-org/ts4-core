package org.toxsoft.core.tslib.bricks.validator;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Compound validator calls registered {@link ITsValidator}s in order of registration.
 * <p>
 * Implementation may use different strategies which error/warning result to return in {@link #validate(Object)}.
 *
 * @author hazard157
 * @param <V> - checked entity (value) class
 */
public interface ITsCompoundValidator<V>
    extends ITsValidator<V> {

  /**
   * Returns validators in the order of registration.
   *
   * @return IList&lt;{@link ITsValidator}&gt; - ordered list of validators
   */
  IList<ITsValidator<V>> listValidators();

  /**
   * Adds (registers) validator.
   * <p>
   * Already registered validators are ignored.
   *
   * @param aValidator {@link ITsValidator} - validator to register
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void addValidator( ITsValidator<V> aValidator );

  /**
   * Removes (unregisters) validator.
   * <p>
   * Unregistered validators are ignored.
   *
   * @param aValidator {@link ITsValidator} - validator to be removed
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  void removeValidator( ITsValidator<V> aValidator );

  /**
   * Temporary disables the specified validator.
   * <p>
   * After call of this method the {@link #validate(Object)} will not include disabled validator untill the
   * {@link #unmuteValidator(ITsValidator)} call. However the {@link #listValidators()} will include disabled validator.
   * <p>
   * Unregistered validator is ignored.
   *
   * @param aValidator {@link ITsValidator} - the validator to be disabled
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void muteValidator( ITsValidator<V> aValidator );

  /**
   * Determines if the specified validator is disabled,
   *
   * @param aValidator {@link ITsValidator} - the validator to be checked
   * @return boolean - <code>true</code> if validator is diables
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  boolean isValidatorMuted( ITsValidator<V> aValidator );

  /**
   * Enables prevously disabled validator.
   * <p>
   * Unregistered validator is ignored. {@link ITsValidator}&ltV&gt; - the validator to be disabled
   *
   * @param aValidator {@link ITsValidator} - the validator to be resumed
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void unmuteValidator( ITsValidator<V> aValidator );

}
