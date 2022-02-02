
package org.toxsoft.core.tslib.av.validators;

import java.io.File;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.std.FileObjectValidator;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Валидатор, проверяющий, что {@link EAtomicType#STRING} является именем существующего в файловой системе объекта.
 * <p>
 * Можно унаследоваться от валидатора и осуществлять дополнительные проверки в методе {@link #doAdditionalCheck(File)}.
 * <p>
 * Для проверки используется валидатор {@link FileObjectValidator}.
 *
 * @author hazard157
 */
public class FileStringAvValidator
    extends AbstractAvValidator {

  /**
   * Синглтон валидатора существования файла с предупреждением.
   */
  public static ITsValidator<IAtomicValue> FILE_WARN_VALIDATOR = new FileStringAvValidator( false, false );

  /**
   * Синглтон валидатора существования файла с ошибкой.
   */
  public static ITsValidator<IAtomicValue> FILE_ERR_VALIDATOR = new FileStringAvValidator( false, true );

  /**
   * Синглтон валидатора существования директория с предупреждением.
   */
  public static ITsValidator<IAtomicValue> DIR_WARN_VALIDATOR = new FileStringAvValidator( true, false );

  /**
   * Синглтон валидатора существования директория с ошибкой.
   */
  public static ITsValidator<IAtomicValue> DIR_ERR_VALIDATOR = new FileStringAvValidator( true, true );

  private final FileObjectValidator foValidator;

  /**
   * Конструктор со всеми инвариантами.
   * <p>
   * Форматная строка предназначения для метда {@link String#format(String, Object...)}, где в качестве аргумента
   * передается строка {@link File#getAbsolutePath()}.
   *
   * @param aCheckForDir boolean - признак провреки строки как иемни директория
   * @param aCheckForError boolean - признак проверки на ошибку (иначе будет выдано предупреждение) отсутствия объекта
   * @param aExistsMsgFormatStr String - форматная строка сообщения об отсутствии объекта
   * @param aCorrectObjectMsgStr String - форматная строка сообщения о том, что это не файл (директория)
   */
  public FileStringAvValidator( boolean aCheckForDir, boolean aCheckForError, String aExistsMsgFormatStr,
      String aCorrectObjectMsgStr ) {
    super( EAtomicType.STRING );
    foValidator = new FileObjectValidator( aCheckForDir, aCheckForError, aExistsMsgFormatStr, aCorrectObjectMsgStr ) {

      @Override
      protected ValidationResult doAdditionalCheck( File aFile ) {
        return FileStringAvValidator.this.doAdditionalCheck( aFile );
      }
    };
  }

  /**
   * Конструктор о стандартным сообщением.
   *
   * @param aCheckForDir boolean - признак провреки строки как иемни директория
   * @param aCheckForError boolean - признак проверки на ошибку (иначе будет выдано предупреждение)
   */
  public FileStringAvValidator( boolean aCheckForDir, boolean aCheckForError ) {
    super( EAtomicType.STRING );
    foValidator = new FileObjectValidator( aCheckForDir, aCheckForError ) {

      @Override
      protected ValidationResult doAdditionalCheck( File aFile ) {
        return FileStringAvValidator.this.doAdditionalCheck( aFile );
      }
    };
  }

  // ------------------------------------------------------------------------------------
  // AbstractAvValidator
  //

  @Override
  public ValidationResult doValidate( IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    File f = new File( aValue.asString() );
    return foValidator.validate( f );
  }

  // ------------------------------------------------------------------------------------
  // For descendants
  //

  /**
   * Наследник может переопределить метод и осуществить дополнительные проверки.
   * <p>
   * Вызывается, если проверка на существование и правильный тип не привело к ошибке.
   * <p>
   * В родительском классе просто возвращает {@link ValidationResult#SUCCESS}, при перелпределении вызывать не надо.
   *
   * @param aFile {@link File} - файл, созданный из строки
   * @return {@link ValidationResult} - результат проверки
   */
  protected ValidationResult doAdditionalCheck( File aFile ) {
    return ValidationResult.SUCCESS;
  }

}
