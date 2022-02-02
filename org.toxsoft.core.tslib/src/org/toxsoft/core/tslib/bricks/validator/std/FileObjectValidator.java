package org.toxsoft.core.tslib.bricks.validator.std;

import static org.toxsoft.core.tslib.bricks.validator.std.ITsResources.*;

import java.io.File;

import org.toxsoft.core.tslib.bricks.validator.ITsValidator;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.utils.errors.TsErrorUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Валидатор, проверяющий, что {@link File} является существующим в файловой системе объектом.
 * <p>
 * Можно унаследоваться от валидатора и осуществлять дополнительные проверки в методе {@link #doAdditionalCheck(File)}.
 * <p>
 * Проверяется на ошибку соответствие файлового объекта заданному типу (файл/директория). Результат проверки на
 * существование указанного объекта будет ошибкой или предупреждением в зависимости от аргумента конструктора.
 * Естественно, что если объект отсутствует, проверка на тип объекта не производится (тип не может быть определен).
 *
 * @author hazard157
 */
public class FileObjectValidator
    implements ITsValidator<File> {

  /**
   * Синглтон валидатора существования файла с предупреждением.
   */
  public static ITsValidator<File> FILE_WARN_VALIDATOR = new FileObjectValidator( false, false );

  /**
   * Синглтон валидатора существования файла с ошибкой.
   */
  public static ITsValidator<File> FILE_ERR_VALIDATOR = new FileObjectValidator( false, true );

  /**
   * Синглтон валидатора существования директория с предупреждением.
   */
  public static ITsValidator<File> DIR_WARN_VALIDATOR = new FileObjectValidator( true, false );

  /**
   * Синглтон валидатора существования директория с ошибкой.
   */
  public static ITsValidator<File> DIR_ERR_VALIDATOR = new FileObjectValidator( true, true );

  private final boolean checkForDir;
  private final boolean checkForError;
  private final String  exsistsMsgFormatString;
  private final String  correctObjMsgFormatString;

  /**
   * Конструктор со всеми инвариантами.
   * <p>
   * Форматная строка предназначения для метда {@link String#format(String, Object...)}, где в качестве аргумента
   * передается строка {@link File#getAbsolutePath()}.
   *
   * @param aCheckForDir boolean - признак провреки строки как иемни директория
   * @param aCheckForError boolean - признак проверки на ошибку (иначе будет выдано предупреждение) отсутствия объекта
   * @param aExistsMsgFormatString String - форматная строка сообщения об отсутствии объекта
   * @param aCorrectObjectMsgString String - форматная строка сообщения о том, что это не файл (директория)
   */
  public FileObjectValidator( boolean aCheckForDir, boolean aCheckForError, String aExistsMsgFormatString,
      String aCorrectObjectMsgString ) {
    checkForDir = aCheckForDir;
    checkForError = aCheckForError;
    exsistsMsgFormatString = TsErrorUtils.checkNonBlank( aExistsMsgFormatString );
    correctObjMsgFormatString = TsErrorUtils.checkNonBlank( aCorrectObjectMsgString );
  }

  /**
   * Конструктор о стандартным сообщением.
   *
   * @param aCheckForDir boolean - признак провреки строки как иемни директория
   * @param aCheckForError boolean - признак проверки на ошибку (иначе будет выдано предупреждение)
   */
  public FileObjectValidator( boolean aCheckForDir, boolean aCheckForError ) {
    checkForDir = aCheckForDir;
    checkForError = aCheckForError;
    if( checkForDir ) {
      if( checkForError ) {
        exsistsMsgFormatString = FMT_ERR_NON_EXSISTANT_DIR;
      }
      else {
        exsistsMsgFormatString = FMT_WARN_NON_EXSISTANT_DIR;
      }
      correctObjMsgFormatString = FMT_ERR_NOT_A_DIR;
    }
    else {
      if( checkForError ) {
        exsistsMsgFormatString = FMT_ERR_NON_EXSISTANT_FILE;
      }
      else {
        exsistsMsgFormatString = FMT_WARN_NON_EXSISTANT_FILE;
      }
      correctObjMsgFormatString = FMT_ERR_NOT_A_FILE;
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsValidator
  //

  @Override
  public ValidationResult validate( File aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    ValidationResult result = ValidationResult.SUCCESS;
    if( aValue.exists() ) {
      if( !aValue.isFile() && !checkForDir ) {
        return ValidationResult.error( correctObjMsgFormatString, aValue.getAbsolutePath() );
      }
      if( !aValue.isDirectory() && checkForDir ) {
        return ValidationResult.error( correctObjMsgFormatString, aValue.getAbsolutePath() );
      }
    }
    else {
      if( checkForError ) {
        return ValidationResult.error( exsistsMsgFormatString, aValue.getAbsolutePath() );
      }
      result = ValidationResult.warn( exsistsMsgFormatString, aValue.getAbsolutePath() );
    }
    return ValidationResult.lastNonOk( result, doAdditionalCheck( aValue ) );
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
