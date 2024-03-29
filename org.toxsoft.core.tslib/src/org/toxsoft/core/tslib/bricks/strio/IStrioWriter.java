package org.toxsoft.core.tslib.bricks.strio;

import java.time.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Text representation writer stream.
 *
 * @author hazard157
 */
public interface IStrioWriter
    extends IStrioStreamBase {

  // TRANSLATE

  // ------------------------------------------------------------------------------------
  // Write with indent

  /**
   * Возвращает количество пробелов в одном отступе, заданный методом {@link #setIndentSpaces(int)}.
   * <p>
   * Отступы - это пробелы, вставляемые в начале строки вызовом метода {@link #writeEol()}. Данный метод задает
   * количество символов пробела, которые состояляют один отступ. Количество отступов в начале строки задается методом
   * {@link #setIndentLevel(int)}. Таким образом, в начале каждой строки, созданной методом {@link #writeEol()}
   * вставляется {@link #getIndentSpaces()} * {@link #getIndentLevel()} символов пробела (и никаких табуляции, только
   * пробелы {@link IStrioHardConstants#CHAR_SPACE}).
   * <p>
   * Обратите внимание, что другие способы перевода строки (например, вывод символа '\n') не приводит к отступам.
   * <p>
   * По умолчанию, в одном отступе 2 пробела.
   *
   * @return int - количество пробелов в одном отступе
   */
  int getIndentSpaces();

  /**
   * Задает количество пробелов в отступе.
   * <p>
   * Если количество пробелов выходит за пределы 1..8, то он принудительно устанавливаеться в границах этих пределов.
   *
   * @param aSpacesCount int - количество отступов
   */
  void setIndentSpaces( int aSpacesCount );

  /**
   * Возвращает количество отстутпов в начале строки, создаваемой методом {@link #writeEol()}.
   * <p>
   * Пояснение по отступам дано в комментариях к методу {@link #setIndentSpaces(int)}.
   *
   * @return int - количество отстутпов в начале строки
   */
  int getIndentLevel();

  /**
   * Задает количество отстутпов в начале строки, создаваемой методом {@link #writeEol()}.
   * <p>
   * Пояснение по отступам дано в комментариях к методу {@link #setIndentSpaces(int)}.
   * <p>
   * Если количество отступов выходит за пределы 0..20, то он принудительнос устанавливаеться в границах этих пределов.
   *
   * @param aLevel - количество отстутпов в начале строки
   */
  void setIndentLevel( int aLevel );

  /**
   * Increases indenting level and writes EOL (end of line).
   * <p>
   * Has the same result as calls to the methods:<br>
   * <code><br>
   * incIndentLevel()<br>
   * writeEol();
   * <br></code>
   */
  void incNewLine();

  /**
   * Decreases indenting level and writes EOL (end of line).
   * <p>
   * Has the same result as calls to the methods:<br>
   * <code><br>
   * decIndentLevel()<br>
   * writeEol();
   * <br></code>
   */
  void decNewLine();

  /**
   * Записывает символ пробела.
   */
  void writeSpace();

  /**
   * Записывает символ запятая, эквивалентна {@link #writeChar(char) writeChar(CHAR_ITEM_SEPARATOR)}.
   */
  void writeSeparatorChar();

  /**
   * Writes EOL (end of line) withh indenting rules applied.
   */
  void writeEol();

  // ------------------------------------------------------------------------------------
  // посимвольная запись

  /**
   * Записывает один символ в выходной поток.
   *
   * @param aCh char - записываемый символ
   * @throws TsIoRtException I/O error
   */
  void writeChar( char aCh );

  /**
   * Записиывает несколько символов в заданной последовательности.
   *
   * @param aChars char[] - символы, которые будат в записаны в заданной последовательности
   * @throws TsIoRtException I/O error
   */
  void writeChars( char... aChars );

  // ------------------------------------------------------------------------------------
  // запись примитивов

  /**
   * Writes boolean value as "<code>true</code>" or "<code>false</code>" text.
   *
   * @param aValue boolean - записываемое значение
   * @throws TsIoRtException I/O error
   */
  void writeBoolean( boolean aValue );

  /**
   * Записывает целое число (в десятичной системе счисления).
   *
   * @param aValue int - записываемое значение
   * @throws TsIoRtException I/O error
   */
  void writeInt( int aValue );

  /**
   * Записывает целое число (в шестнадцеричной системе счисления) в виде 0x123ABC.
   *
   * @param aValue int - записываемое значение
   * @throws TsIoRtException I/O error
   */
  void writeIntHex( int aValue );

  /**
   * Записывает целое число (в десятичной системе счисления).
   *
   * @param aValue long - записываемое значение
   * @throws TsIoRtException I/O error
   */
  void writeLong( long aValue );

  /**
   * Записывает целое число (в шестнадцеричной системе счисления) в виде 0x123ABC.
   *
   * @param aValue long - записываемое значение
   * @throws TsIoRtException I/O error
   */
  void writeLongHex( long aValue );

  /**
   * Записывает вещественное число в текстовой вид.
   *
   * @param aValue float - записываемое значение
   * @throws TsIoRtException I/O error
   * @see Float#toString() - формат записи
   */
  void writeFloat( float aValue );

  /**
   * Записывает вещественное число в текстовой вид.
   *
   * @param aValue double - записываемое значение
   * @throws TsIoRtException I/O error
   * @see Double#toString() - формат записи
   */
  void writeDouble( double aValue );

  /**
   * Записывает вещественное число с округлением в текстовой вид.
   *
   * @param aValue double - записываемое значение
   * @param aPrecition int - количество цифр после запятой при округлении (от 0 до 8)
   * @throws TsIoRtException I/O error
   * @see Double#toString() - формат записи
   */
  void writeDouble( double aValue, int aPrecition );

  /**
   * Записывает метку времени сокращенном виде, только дата.
   *
   * @param aTimestamp long - метка времени (миллисекунды с начала эпохи)
   * @throws TsIoRtException I/O error
   */
  void writeDate( long aTimestamp );

  /**
   * Записывает метку времени сокращенном виде, дата и время суток с точностью до секунды.
   *
   * @param aTimestamp long - метка времени (миллисекунды с начала эпохи)
   * @throws TsIoRtException I/O error
   */
  void writeDateTime( long aTimestamp );

  /**
   * Записывает метку времени в виде {@link IStrioHardConstants#TIMESTAMP_FMT}.
   *
   * @param aTimestamp long - метка времени (миллисекунды с начала эпохи)
   * @throws TsIoRtException I/O error
   */
  void writeTimestamp( long aTimestamp );

  /**
   * Writes timestamp as a date.
   * <p>
   * Writes the timestamp representation in the same format as the method {@link #writeDate(long)}.
   *
   * @param aTime {@link LocalDate} - the date timestamp
   */
  void writeTime( LocalDate aTime );

  /**
   * Writes timestamp as a timestamp of milliseconds accuracy.
   * <p>
   * Writes the timestamp representation in the same format as the method {@link #writeDateTime(long)}.
   * <p>
   * Note: nanoseconds accuracy of {@link LocalDateTime} is truncated to milliseconds.
   *
   * @param aTime {@link LocalDateTime} - the timestamp
   */
  void writeTime( LocalDateTime aTime );

  /**
   * Записывает переданную строку "как есть", без дополнительной обработки.
   *
   * @param aString String - записываемая строка
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException I/O error
   */
  void writeAsIs( String aString );

  /**
   * Записывает переданную строку с обрамлением кавчками (quoted string).
   * <p>
   * The quoted string format is described in {@link TsMiscUtils#toQuotedLine(String)}.
   *
   * @param aString String - записываемая строка
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException I/O error
   */
  void writeQuotedString( String aString );

  /**
   * Writes formated string to the output.
   * <p>
   * {@link String#format(String, Object...)} formating rules are used.
   *
   * @param aFormatString String - the format string
   * @param aArgs Object[] - format arguments
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void p( String aFormatString, Object... aArgs );

  /**
   * Writes formated string to the output and appends EOL (end of line).
   * <p>
   * As an EOL the method {@link #writeEol()} is used to apply the indenting rules.
   * <p>
   * {@link String#format(String, Object...)} formating rules are used.
   *
   * @param aFormatString String - the format string
   * @param aArgs Object[] - format arguments
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  void pl( String aFormatString, Object... aArgs );

}
