package org.toxsoft.core.tslib.bricks.validator.std;

import java.io.File;

import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Валидатор, проверяющий, что {@link String} является именем существующего в файловой системе объекта.
 * <p>
 * Можно унаследоваться от валидатора и осуществлять дополнительные проверки в методе {@link #doAdditionalCheck(File)}.
 * <p>
 * Для проверки используется валидатор {@link FileObjectValidator}.
 *
 * @author hazard157
 */
public class FileStringValidator
    implements ITsValidator<String> {

  /**
   * Синглтон валидатора существования файла с предупреждением.
   */
  public static ITsValidator<String> FILE_WARN_VALIDATOR = new FileStringValidator( false, false );

  /**
   * Синглтон валидатора существования файла с ошибкой.
   */
  public static ITsValidator<String> FILE_ERR_VALIDATOR = new FileStringValidator( false, true );

  /**
   * Синглтон валидатора существования директория с предупреждением.
   */
  public static ITsValidator<String> DIR_WARN_VALIDATOR = new FileStringValidator( true, false );

  /**
   * Синглтон валидатора существования директория с ошибкой.
   */
  public static ITsValidator<String> DIR_ERR_VALIDATOR = new FileStringValidator( true, true );

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
  public FileStringValidator( boolean aCheckForDir, boolean aCheckForError, String aExistsMsgFormatStr,
      String aCorrectObjectMsgStr ) {
    foValidator = new FileObjectValidator( aCheckForDir, aCheckForError, aExistsMsgFormatStr, aCorrectObjectMsgStr ) {

      @Override
      protected ValidationResult doAdditionalCheck( File aFile ) {
        return FileStringValidator.this.doAdditionalCheck( aFile );
      }
    };
  }

  /**
   * Конструктор о стандартным сообщением.
   *
   * @param aCheckForDir boolean - признак провреки строки как иемни директория
   * @param aCheckForError boolean - признак проверки на ошибку (иначе будет выдано предупреждение)
   */
  public FileStringValidator( boolean aCheckForDir, boolean aCheckForError ) {
    foValidator = new FileObjectValidator( aCheckForDir, aCheckForError ) {

      @Override
      protected ValidationResult doAdditionalCheck( File aFile ) {
        return FileStringValidator.this.doAdditionalCheck( aFile );
      }
    };
  }

  // ------------------------------------------------------------------------------------
  // ITsValidator
  //

  @Override
  public ValidationResult validate( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    File f = new File( aValue );
    return foValidator.validate( f );
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
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
