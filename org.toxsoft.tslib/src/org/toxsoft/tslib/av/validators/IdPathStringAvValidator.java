package org.toxsoft.tslib.av.validators;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.std.IdPathStringValidator;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Валидатор, проверяющий, что {@link EAtomicType#STRING} является является ИД-путем (или ИД-именем).
 * <p>
 * Для проверки использует {@link IdPathStringValidator}.
 * <p>
 * Можно унаследоваться от этого валидатора и осуществлять дополнительные проверки в методе
 * {@link #doAdditionalCheck(String)}.
 *
 * @author hazard157
 */
public class IdPathStringAvValidator
    extends AbstractAvValidator {

  /**
   * Синглтон валидатора ИД-пути с сообщением по умолчанию.
   */
  public static final ITsValidator<IAtomicValue> IDPATH_VALIDATOR = new IdPathStringAvValidator( false );

  /**
   * Синглтон валидатора ИД-пути или пустой строки с сообщением по умолчанию.
   */
  public static final ITsValidator<IAtomicValue> IDPATH_EMPTY_VALIDATOR = new IdPathStringAvValidator( true );

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
  public IdPathStringAvValidator( boolean aAllowEmpty, String aInvMsgFmtStr, String aNoneMsgStr ) {
    super( EAtomicType.STRING );
    ipsValidator = new IdPathStringValidator( false, aAllowEmpty, aInvMsgFmtStr, aNoneMsgStr ) {

      @Override
      protected ValidationResult doAdditionalCheck( String aId ) {
        return IdPathStringAvValidator.this.doAdditionalCheck( aId );
      }
    };
  }

  /**
   * Constructor.
   *
   * @param aAllowEmpty boolean - признак, что допускается пустая строка
   */
  public IdPathStringAvValidator( boolean aAllowEmpty ) {
    super( EAtomicType.STRING );
    ipsValidator = new IdPathStringValidator( false, aAllowEmpty ) {

      @Override
      protected ValidationResult doAdditionalCheck( String aId ) {
        return IdPathStringAvValidator.this.doAdditionalCheck( aId );
      }
    };
  }

  /**
   * Constructor.
   * <p>
   * Shorthand for {@link IdNameStringAvValidator#IdNameStringAvValidator(boolean) IdNameStringAvValidator(false)}.
   */
  public IdPathStringAvValidator() {
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
