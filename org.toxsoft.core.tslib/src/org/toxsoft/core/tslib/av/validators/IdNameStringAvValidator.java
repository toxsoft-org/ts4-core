package org.toxsoft.core.tslib.av.validators;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.std.IdPathStringValidator;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Validator check if {@link String} is an IDname.
 * <p>
 * Uses {@link IdPathStringValidator}.
 * <p>
 * May be subclassed to perform additional validation in {@link #doAdditionalCheck(String)}.
 *
 * @author hazard157
 */
public class IdNameStringAvValidator
    extends AbstractAvValidator {

  /**
   * Синглтон валидатора ИД-имени с сообщением по умолчанию.
   */
  public static final ITsValidator<IAtomicValue> IDNAME_VALIDATOR = new IdNameStringAvValidator( false );

  /**
   * Синглтон валидатора ИД-имени или пустой строки с сообщением по умолчанию.
   */
  public static final ITsValidator<IAtomicValue> IDNAME_EMPTY_VALIDATOR = new IdNameStringAvValidator( true );

  private final IdPathStringValidator ipsValidator;

  /**
   * Constructor.
   *
   * @param aAllowEmpty boolean - признак, что допускается пустая строка
   * @param aInvMsgFmtStr String - форматная строка сообщения об ошибке при недопустимом формате
   * @param aNoneMsgStr String - строка сообщения о предупреждении при {@link IStridable#NONE_ID}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any string in a blank string
   */
  public IdNameStringAvValidator( boolean aAllowEmpty, String aInvMsgFmtStr, String aNoneMsgStr ) {
    super( EAtomicType.STRING );
    ipsValidator = new IdPathStringValidator( true, aAllowEmpty, aInvMsgFmtStr, aNoneMsgStr ) {

      @Override
      protected ValidationResult doAdditionalCheck( String aId ) {
        return IdNameStringAvValidator.this.doAdditionalCheck( aId );
      }
    };
  }

  /**
   * Constructor.
   *
   * @param aAllowEmpty boolean - признак, что допускается пустая строка
   */
  public IdNameStringAvValidator( boolean aAllowEmpty ) {
    super( EAtomicType.STRING );
    ipsValidator = new IdPathStringValidator( true, aAllowEmpty ) {

      @Override
      protected ValidationResult doAdditionalCheck( String aId ) {
        return IdNameStringAvValidator.this.doAdditionalCheck( aId );
      }
    };
  }

  /**
   * Constructor.
   * <p>
   * Shorthand for {@link IdNameStringAvValidator#IdNameStringAvValidator(boolean) IdNameStringAvValidator(false)}.
   */
  public IdNameStringAvValidator() {
    this( false );
  }

  // ------------------------------------------------------------------------------------
  // ITsValidator
  //

  @Override
  public ValidationResult doValidate( IAtomicValue aValue ) {
    return ipsValidator.validate( aValue.asString() );
  }

  // ------------------------------------------------------------------------------------
  // For descendants
  //

  /**
   * Наследник может переопределить метод и осуществить дополнительные проверки.
   * <p>
   * В родительском классе просто возвращает {@link ValidationResult#SUCCESS}, при перелпределении вызывать не надо.
   *
   * @param aId String - валидный идентификатор (ИД-имя или ИД-путь)
   * @return {@link ValidationResult} - результат проверки
   */
  protected ValidationResult doAdditionalCheck( String aId ) {
    return ValidationResult.SUCCESS;
  }

}
