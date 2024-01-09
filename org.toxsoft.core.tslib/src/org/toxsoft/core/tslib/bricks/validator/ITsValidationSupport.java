package org.toxsoft.core.tslib.bricks.validator;

import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Поддержка валидации (предварительно проверки) редактирования.
 *
 * @author hazard157
 * @param <V> - the validator type
 */
public interface ITsValidationSupport<V> {

  /**
   * Возвращает валидатор.
   * <p>
   * Валидатор проводит валидацию путем последовательного вызова сначала всех встроенных (неудаляемых) валидаторов, а
   * потом валидаторов, добавленных методом {@link #addValidator(Object)}, в порядке их добавления. Первая же ошибка
   * {@link ValidationResult#isError()} = <code>true</code> немедленно возвращается. Предупреждение же возвращается
   * согласно правилам {@link ValidationResult#firstNonOk(ValidationResult, ValidationResult)}.
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
