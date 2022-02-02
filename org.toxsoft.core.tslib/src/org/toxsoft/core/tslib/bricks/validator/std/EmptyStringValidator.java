package org.toxsoft.core.tslib.bricks.validator.std;

import static org.toxsoft.core.tslib.bricks.validator.std.ITsResources.*;

import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Валидатор, проверяющий, что текстовая строка не пустае.
 *
 * @author hazard157
 */
public class EmptyStringValidator
    implements ITsValidator<String> {

  /**
   * Синглтон валидатора на ошибку со стандартным сообщением.
   */
  public static ITsValidator<String> ERROR_VALIDATOR = new EmptyStringValidator( true );

  /**
   * Синглтон предупреждающего валидатора со стандартным сообщением.
   */
  public static ITsValidator<String> WARNING_VALIDATOR = new EmptyStringValidator( false );

  private final boolean asError;
  private final String  message;

  /**
   * Создает валидатор со всеми инвариантами.
   *
   * @param aAsError boolean - интерпретировать пустую строку как ошибку, а не предупреждение
   * @param aMessage String - сообщение при пустой строке
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aMessage - пустая строка
   */
  public EmptyStringValidator( boolean aAsError, String aMessage ) {
    asError = aAsError;
    message = TsErrorUtils.checkNonBlank( aMessage );
  }

  /**
   * Создает валидатор со стандартным сообщением.
   *
   * @param aAsError boolean - интерпретировать пустую строку как ошибку, а не предупреждение
   * @throws TsNullArgumentRtException любой аргумент = null
   * @throws TsIllegalArgumentRtException aMessage - пустая строка
   */
  public EmptyStringValidator( boolean aAsError ) {
    this( aAsError, MSG_STRING_VALUE_IS_EMPTY );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsValidator<String>
  //

  @Override
  public ValidationResult validate( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aValue.isEmpty() ) {
      if( asError ) {
        return ValidationResult.error( message );
      }
      return ValidationResult.warn( message );
    }
    return ValidationResult.SUCCESS;
  }

}
