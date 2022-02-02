package org.toxsoft.core.tslib.bricks.strio;

import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Basic interface for both {@link IStrioWriter} and {@link IStrioReader}.
 *
 * @author hazard157
 */
public interface IStrioStreamBase {

  // TRANSLATE

  /**
   * Returns space characters as single string.
   * <p>
   * Initiali По умолчанию (то есть, до первого вызова {@link #setSpaceChars(String)} возвращает
   * {@link IStrioHardConstants#DEFAULT_SPACE_CHARS}.
   * <p>
   * Понятие пробелов, пропусков и разделителей описано в комментарии к {@link IStrioHardConstants#DEFAULT_SPACE_CHARS}.
   *
   * @return String - строка символов-пробелов
   */
  String getSpaceChars();

  /**
   * Изменяет набор символов-пробелов.
   *
   * @see #getSpaceChars()
   * @see IStrioHardConstants#DEFAULT_SPACE_CHARS
   * @param aSpaceChars String - строка символов-пробелов
   * @return String предыдущий набор символов-пробелов
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент пустая строка
   */
  String setSpaceChars( String aSpaceChars );

  /**
   * Возвращает символы-пропуски в виде строки.
   * <p>
   * По умолчанию (то есть, до первого вызова {@link #setBypassedChars(String)} возвращает
   * {@link IStrioHardConstants#DEFAULT_BYPASSED_CHARS}.
   * <p>
   * Понятие пробелов, пропусков и разделителей описано в комментарии к {@link IStrioHardConstants#DEFAULT_SPACE_CHARS}.
   *
   * @return String - строка символов-пропусков
   */
  String getBypassedChars();

  /**
   * Изменяет набор символов-пропусков.
   * <p>
   * Для того, чтобы символы-пробелы входили в состав символов-пропусков, их надо включить в аргумент aBypassedChars.
   *
   * @see #getBypassedChars()
   * @see IStrioHardConstants#DEFAULT_SPACE_CHARS
   * @see IStrioHardConstants#DEFAULT_BYPASSED_CHARS
   * @param aBypassedChars String - строка символов-пропусков
   * @return String предыдущий набор символов-пропусков
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент пустая строка
   */
  String setBypassedChars( String aBypassedChars );

  /**
   * Возвращает символы-разделители в виде строки.
   * <p>
   * По умолчанию (то есть, до первого вызова {@link #setDelimiterChars(String)} возвращает
   * {@link IStrioHardConstants#DEFAULT_BYPASSED_CHARS}.
   * <p>
   * Понятие пробелов, пропусков и разделителей описано в комментарии к {@link IStrioHardConstants#DEFAULT_SPACE_CHARS}.
   *
   * @return String - строка символов-разделителей
   */
  String getDelimiterChars();

  /**
   * Изменяет набор символов-разделителей.
   * <p>
   * Для того, чтобы символы-пропуски и пробелы входили в состав символов-разделителей, их надо включить в аргумент
   * aDelimiterChars.
   *
   * @see #getDelimiterChars()
   * @see IStrioHardConstants#DEFAULT_SPACE_CHARS
   * @see IStrioHardConstants#DEFAULT_DELIMITER_CHARS
   * @param aDelimiterChars String - строка символов-разделителей
   * @return String предыдущий набор символов-разделителей
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент пустая строка
   */
  String setDelimiterChars( String aDelimiterChars );

  /**
   * Определяет, является ли символ пробелом.
   * <p>
   * Для аргумента {@link IStrioHardConstants#CHAR_EOF} возвращает false.
   *
   * @param aCh char - проверяемый символ
   * @return <b>true</b> - символ является пробелом;<br>
   *         <b>false</b> - символ не пробел.
   * @see #getSpaceChars()
   * @see IStrioHardConstants#DEFAULT_SPACE_CHARS
   */
  boolean isSpaceChar( char aCh );

  /**
   * Определяет, является ли символ пропуском.
   * <p>
   * Для аргумента {@link IStrioHardConstants#CHAR_EOF} возвращает false.
   *
   * @param aCh char - проверяемый символ
   * @return <b>true</b> - символ является пропуском;<br>
   *         <b>false</b> - символ не пропуск.
   * @see #getBypassedChars()
   * @see IStrioHardConstants#DEFAULT_SPACE_CHARS
   * @see IStrioHardConstants#DEFAULT_BYPASSED_CHARS
   */
  boolean isBypassedChar( char aCh );

  /**
   * Определяет, является ли символ разделителем.
   * <p>
   * Для аргумента {@link IStrioHardConstants#CHAR_EOF} возвращает false.
   *
   * @param aCh char - проверяемый символ
   * @return <b>true</b> - символ является разделителем;<br>
   *         <b>false</b> - символ не пропуск.
   * @see #getDelimiterChars()
   * @see IStrioHardConstants#DEFAULT_SPACE_CHARS
   * @see IStrioHardConstants#DEFAULT_DELIMITER_CHARS
   */
  boolean isDelimiterChar( char aCh );

}
