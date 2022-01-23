package org.toxsoft.tslib.bricks.validator;

import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

// TRANSLATE

/**
 * Поддержка валидации (предварительно проверки) редактирования.
 *
 * @author hazard157
 * @param <V> - конкретный интерфейс валидатора
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
   * @return &ltV&gt; - валидатор
   */
  V validator();

  /**
   * Добавляет валидатор.
   * <p>
   * Если такой валидатор уже зарегистрирован, метод ничего не делает.
   *
   * @param aValidator &ltV&gt; - добавляемый валидатор
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void addValidator( V aValidator );

  /**
   * Удаляет валидатор.
   * <p>
   * Если такой валидатор не зарегистрирован, метод ничего не делает.
   *
   * @param aValidator &ltV&gt; - удаляемый валидатор
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  void removeValidator( V aValidator );

  /**
   * Temprary disables the specified validator.
   * <p>
   * After call of this method the {@link #validator()} will not include disabled validator untill the
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
   * Enables prevously disabled validator.
   * <p>
   * Unregistered validator is ignored.
   *
   * @param aValidator &ltV&gt; - the validator to be disabled
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void resumeValidator( V aValidator );

}
