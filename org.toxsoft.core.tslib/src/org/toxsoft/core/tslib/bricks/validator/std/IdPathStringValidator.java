package org.toxsoft.core.tslib.bricks.validator.std;

import static org.toxsoft.core.tslib.bricks.validator.std.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Валидатор, проверяющий, что строка является ИД-путем (или ИД-именем).
 * <p>
 * Проветяет значения типа {@link String}.
 * <p>
 * Можно унаследоваться от этого валидатора и осуществлять дополнительные проверки в методе
 * {@link #doAdditionalCheck(String)}.
 *
 * @author hazard157
 */
public class IdPathStringValidator
    implements ITsValidator<String> {

  /**
   * Синглтон валидатора ИД-пути с сообщением по умолчанию.
   */
  public static final ITsValidator<String> IDPATH_VALIDATOR = new IdPathStringValidator( false, false );

  /**
   * Синглтон валидатора ИД-имени с сообщением по умолчанию.
   */
  public static final ITsValidator<String> IDNAME_VALIDATOR = new IdPathStringValidator( true, false );

  /**
   * Синглтон валидатора ИД-пути или пустой строки с сообщением по умолчанию.
   */
  public static final ITsValidator<String> IDPATH_EMPTY_VALIDATOR = new IdPathStringValidator( false, true );

  /**
   * Синглтон валидатора ИД-имени или пустой строки с сообщением по умолчанию.
   */
  public static final ITsValidator<String> IDNAME_EMPTY_VALIDATOR = new IdPathStringValidator( true, true );

  private final boolean onlyIdName;
  private final boolean allowEmpty;
  private final String  invFmrStr;
  private final String  noneMsg;

  /**
   * Конструктор со всеми инвариантами.
   *
   * @param aOnlyIdName boolean - признак, что допускается только ИД-имя (а не ИД-путь)
   * @param aAllowEmpty boolean - признак, что допускается пустая строка
   * @param aInvMsgFmtStr String - форматная строка сообщения об ошибке при недопустимом формате
   * @param aNoneMsgStr String - строка сообщения о предупреждении при {@link IStridable#NONE_ID}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any string in a blank string
   */
  public IdPathStringValidator( boolean aOnlyIdName, boolean aAllowEmpty, String aInvMsgFmtStr, String aNoneMsgStr ) {
    onlyIdName = aOnlyIdName;
    allowEmpty = aAllowEmpty;
    invFmrStr = TsErrorUtils.checkNonBlank( aInvMsgFmtStr );
    noneMsg = TsErrorUtils.checkNonBlank( aNoneMsgStr );
  }

  /**
   * Конструктор с сообщениями по умолчанию.
   *
   * @param aOnlyIdName boolean - признак, что допускается только ИД-имя (а не ИД-путь)
   * @param aAllowEmpty boolean - признак, что допускается пустая строка
   */
  public IdPathStringValidator( boolean aOnlyIdName, boolean aAllowEmpty ) {
    this( aOnlyIdName, aAllowEmpty, aOnlyIdName ? FMT_ERR_INV_ID_NAME : FMT_ERR_INV_ID_PATH, MSG_WARN_NONE_ID );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITsValidator<String>
  //

  @Override
  public ValidationResult validate( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    boolean isValid;
    if( allowEmpty && aValue.isEmpty() ) {
      isValid = true;
    }
    else {
      if( onlyIdName ) {
        isValid = StridUtils.isValidIdName( aValue );
      }
      else {
        isValid = StridUtils.isValidIdPath( aValue );
      }
    }
    if( !isValid ) {
      return ValidationResult.error( invFmrStr, aValue );
    }
    ValidationResult vr = ValidationResult.SUCCESS;
    if( aValue.equals( IStridable.NONE_ID ) ) {
      vr = ValidationResult.warn( noneMsg, aValue );
    }
    return ValidationResult.firstNonOk( vr, doAdditionalCheck( aValue ) );
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
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
