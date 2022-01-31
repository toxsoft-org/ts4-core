package org.toxsoft.core.tslib.av.validators;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.std.EmptyStringValidator;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Валидатор, проверяющий что строка не пустая.
 * <p>
 * Для проверки используется валидатор {@link EmptyStringValidator}.
 *
 * @author hazard157
 */
public class EmptyStringAvValidator
    extends AbstractAvValidator {

  /**
   * Синглтон валидатора на ошибку со стандартным сообщением.
   */
  public static ITsValidator<IAtomicValue> ERROR_VALIDATOR = new EmptyStringAvValidator( true );

  /**
   * Синглтон предупреждающего валидатора со стандартным сообщением.
   */
  public static ITsValidator<IAtomicValue> WARNING_VALIDATOR = new EmptyStringAvValidator( false );

  private EmptyStringValidator esValidator;

  /**
   * Создает валидатор со всеми инвариантами.
   *
   * @param aAsError boolean - интерпретировать пустую строку как ошибку, а не предупреждение
   * @param aMessage String - сообщение при пустой строке
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aMessage - пустая строка
   */
  public EmptyStringAvValidator( boolean aAsError, String aMessage ) {
    super( EAtomicType.STRING );
    esValidator = new EmptyStringValidator( aAsError, aMessage );
  }

  /**
   * Создает валидатор со стандартным сообщением.
   *
   * @param aAsError boolean - интерпретировать пустую строку как ошибку, а не предупреждение
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aMessage - пустая строка
   */
  public EmptyStringAvValidator( boolean aAsError ) {
    super( EAtomicType.STRING );
    esValidator = new EmptyStringValidator( aAsError );
  }

  // ------------------------------------------------------------------------------------
  // AbstractAvValidator
  //

  @Override
  public ValidationResult doValidate( IAtomicValue aValue ) {
    return esValidator.validate( aValue.asString() );
  }

}
