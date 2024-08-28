package org.toxsoft.core.tslib.bricks.strio;

import java.io.*;
import java.time.*;

import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Text representation reader stream.
 *
 * @author hazard157
 */
public interface IStrioReader
    extends IStrioStreamBase {

  /**
   * Returns the characters skipping current mode.
   * <p>
   * Initially returns {@link EStrioSkipMode#SKIP_COMMENTS}.
   *
   * @return {@link EStrioSkipMode} - current skipping mode
   */
  EStrioSkipMode skipMode();

  /**
   * Sets the characters skipping current mode.
   *
   * @param aSkipMode {@link EStrioSkipMode} - new skipping mode
   * @return {@link EStrioSkipMode} - current skipping mode
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  EStrioSkipMode setSkipMode( EStrioSkipMode aSkipMode );

  /**
   * Returns line comment starting character.
   * <p>
   * Content starting from this charachter and until end of line are considered as commmend and ignored by reader. Both
   * comment start charachter and end of line are ignored and not considered as part of comments.
   * <p>
   * By default returns {@link IStrioHardConstants#CHAR_LINE_COMMENT_SHELL}.
   *
   * @return char - line comment starting character
   */
  char getLineCommentChar();

  /**
   * Sets line comment starting character.
   *
   * @param aChar char - line comment starting character
   */
  void setLineCommentChar( char aChar );

  // ------------------------------------------------------------------------------------
  // Basic reading
  //

  /**
   * Returns the current position of the character to be read.
   * <p>
   * The count starts from the moment of creation of this instance considering in put stream as a array of
   * <code>char</code>s. Putting symbols back changes this counter.
   *
   * @return int - index of the of the character to be read (starts at 0)
   */
  int currentPosition();

  /**
   * Marks last read character to be read once again.
   * <p>
   * Subsequent calls to this method shifts reading back to the already read characters. Maximum number of subsequent
   * calls are limited by internal buffer. Buffer size may be specified in constructor. Anyway buffer has at least 64
   * characters.
   *
   * @throws StrioRtException too many subsequent calls
   * @throws StrioRtException attempt to put back character before head of the input stream
   */
  void putCharBack();

  /**
   * Returns next character from input stream and advances in input stream.
   *
   * @return char - the read character, may be {@link IStrioHardConstants#CHAR_EOF}
   * @throws TsIoRtException {@link IOException} occured while reading from input stream
   */
  char nextChar();

  /**
   * Returns next character from input stream and advances in input stream.
   * <p>
   * Depending on argument, some characters from input stream will be ignored as if they were not present in input
   * stream.
   * <p>
   * If there were "put-back" characters (via {@link #putCharBack()}, first they will be returned.
   *
   * @param aSkipMode {@link EStrioSkipMode} - skipping mode
   * @return char - the read character, may be {@link IStrioHardConstants#CHAR_EOF}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured while reading from input stream
   */
  char nextChar( EStrioSkipMode aSkipMode );

  /**
   * Returns next character from input stream but does <b>not</b> advances in input stream.
   *
   * @return char - first non-skipped character, may be {@link IStrioHardConstants#CHAR_EOF}
   * @throws TsIoRtException {@link IOException} occured while reading from input stream
   */
  char peekChar();

  /**
   * Returns next character from input stream but does <b>not</b> advances in input stream.
   * <p>
   * Subsequent calls of this method will return the same character. There is one exception: after
   * {@link IStrioHardConstants#CHAR_EOF} input stream may receive more bytes (eg. by network) and next call of this
   * method will returnnew character.
   * <p>
   * Method skips some characters depending on argument value, stops on and returns first non-skipped character.
   *
   * @param aSkipMode {@link EStrioSkipMode} - skipping mode
   * @return char - first non-skipped character, may be {@link IStrioHardConstants#CHAR_EOF}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIoRtException {@link IOException} occured while reading from input stream
   */
  char peekChar( EStrioSkipMode aSkipMode );

  /**
   * Checks next non-skipped character is equal to the expected one.
   *
   * @param aCh char - expected character
   * @return char - always return argumets as character just read
   * @throws StrioRtException first non-skipped char was not aCh
   * @throws StrioRtException first non-skipped char was EOF
   */
  char ensureChar( char aCh );

  /**
   * Checks that character sequence starting from first non-skiiped char is equal to specified one.
   *
   * @param aStr String - expected sequence of characters
   * @throws StrioRtException read sequence does not matches argument
   */
  void ensureString( String aStr );

  /**
   * Checks that next non-skipped char is {@link IStrioHardConstants#CHAR_ITEM_SEPARATOR}.
   *
   * @return char - always returns {@link IStrioHardConstants#CHAR_ITEM_SEPARATOR}
   * @throws StrioRtException first non-skipped char was not {@link IStrioHardConstants#CHAR_ITEM_SEPARATOR}
   * @throws StrioRtException first non-skipped char was EOF
   */
  char ensureSeparatorChar();

  /**
   * Reads sequence of characters starting from first next non-skipped char to the first delimiter char.
   * <p>
   * Delimiter characters are one of the {@link #getDelimiterChars()} and {@link IStrioHardConstants#CHAR_EOF}.
   *
   * @return String - read sequence of characters
   */
  String readUntilDelimiter();

  /**
   * Reads sequence of characters starting from first next non-skipped char to the specified char.
   * <p>
   * End-of-file {@link IStrioHardConstants#CHAR_EOF} also stops reading.
   *
   * @param aCh char - delimiter character
   * @return String - read sequence of characters
   */
  String readUntilChar( char aCh );

  /**
   * Reads sequence of characters starting from first next non-skipped char to the first delimiter char.
   * <p>
   * Delimiter characters are one of the aCharsString and end-of-file {@link IStrioHardConstants#CHAR_EOF}.
   *
   * @param aCharsString String - delimiter characters
   * @return String - read sequence of characters
   */
  String readUntilChars( String aCharsString );

  /**
   * Reads and returns the input until end of the line.
   * <p>
   * End of the line is determied by {@link StrioUtils#isEol(char)}.
   * <p>
   * The end of input stream {@link IStrioHardConstants#CHAR_EOF} is also considered as end of line.
   *
   * @return String - остаток текущей строки (линии текста)
   */
  String readLine();

  // ------------------------------------------------------------------------------------
  // Read primitives
  //

  /**
   * Reads boolean value.
   * <p>
   * Boolean values are stored as "<code>true</code>" and "<code>false</code>" case-sensitive string tockens.
   * <p>
   * Reading process stops before first dleimiter {@link #getDelimiterChars()}.
   *
   * @return boolean - read value
   * @throws StrioRtException read sequence is not valid boolean name
   */
  boolean readBoolean();

  /**
   * Read next token as decimal of hexadecimal integer number.
   * <p>
   * Hexadecimal numbers must be in form 0x0ABC, case insensitive.
   *
   * @return int - read number
   * @throws StrioRtException invalid integer number format
   * @throws StrioRtException read number is out of range of the {@link Integer} value
   */
  int readInt();

  /**
   * Read next token as decimal of hexadecimal long integer number.
   * <p>
   * Hexadecimal numbers must be in form 0x0ABC, case insensitive.
   *
   * @return long - read number
   * @throws StrioRtException invalid integer number format
   * @throws StrioRtException read number is out of range of the {@link Long} value
   */
  long readLong();

  /**
   * Reads unsigned long integer, consisting of exactly specified number of digits.
   *
   * @param aDigitsNum int - number of digits to read, must be <= 9
   * @param aHexNumber boolean - <code>true</code> to read as hexadecimal, <code>false</code> for decimal representation
   * @return long - read number
   * @throws TsIllegalArgumentRtException aDigitsNum <= 0
   * @throws StrioRtException invalid integer number format
   * @throws StrioRtException read number is out of range of the {@link Long} value
   */
  long readLong( int aDigitsNum, boolean aHexNumber );

  /**
   * Reads real number.
   * <p>
   * Text representation must be as specified by {@link Float#toString(float)}.
   *
   * @return float - read number
   * @throws StrioRtException invalid real number format
   */
  float readFloat();

  /**
   * Reads real number.
   * <p>
   * Text representation must be as specified by {@link Double#toString(double)}.
   *
   * @return read - read number
   * @throws StrioRtException invalid real number format
   */
  double readDouble();

  /**
   * Reads timestamp as milliseconds after epoch.
   * <p>
   * Timestamp format is specified in {@link IStrioHardConstants#TIMESTAMP_FMT}. Timestamp also may have shorter format.
   * For short notation absent components will be set to 0.
   *
   * @return long - read timestamp (milliseconds since epoch start 01.01.1970 00:00:00)
   * @throws StrioRtException invalid timestamp representation format
   */
  long readTimestamp();

  /**
   * Reads timestamp as a date {@link LocalDate} .
   * <p>
   * Timestamp format is specified in {@link IStrioHardConstants#TIMESTAMP_FMT}. Timestamp also may have shorter format.
   * For short notation absent components will be set to 0.
   *
   * @return {@link LocalDate} - the read date
   * @throws StrioRtException invalid timestamp representation format
   */
  LocalDate readTimestampAsDate();

  /**
   * Reads timestamp as {@link LocalDateTime} with milliseconds accuracy.
   * <p>
   * Timestamp format is specified in {@link IStrioHardConstants#TIMESTAMP_FMT}. Timestamp also may have shorter format.
   * For short notation absent components will be set to 0.
   *
   * @return {@link LocalDateTime} - the read timestamp
   * @throws StrioRtException invalid timestamp representation format
   */
  LocalDateTime readTimestampAsDateTime();

  /**
   * Reads quoted string.
   * <p>
   * The quoted string format is described in {@link TsMiscUtils#toQuotedLine(String)}.
   *
   * @return string - read string
   * @throws StrioRtException invalid quoted string representation format
   */
  String readQuotedString();

  /**
   * Reads IDname string.
   * <p>
   * Reading stops before first character that can't be an IDname part.
   *
   * @return String - valid IDname
   * @throws StrioRtException not an IDname in input stream
   */
  String readIdName();

  /**
   * Reads IDpath string.
   * <p>
   * Reading stops before first character that can't be an IDpath part.
   *
   * @return String - valid IDpath
   * @throws StrioRtException not an IDpath in input stream
   */
  String readIdPath();

  // ------------------------------------------------------------------------------------
  // Read constructs
  //

  /**
   * Starts reading the sequence of the elements enclosed in the squuare brackets.
   * <p>
   * Sequence of the elements may be enclosed either between square brackets ("[a,b,c]") or the braces ("{a,b,c}"). To
   * read sequence in square brackets {@link #readArrayBegin()}/{@link #readArrayNext()} methds are used. For braces -
   * {@link #readSetBegin()}/{@link #readSetNext()} should be used. Items are separated by the comma
   * {@link IStrioHardConstants#CHAR_ITEM_SEPARATOR} character. Optional comma after last item in sequence may be used.
   * <p>
   * Commonly between square brackets are places elements of the same type hence <i>Array</i> word is used in
   * {@link #readArrayBegin()} and {@link #readArrayNext()} method names. Between the braces may be placed different
   * type of the elements. Elements may optionally be identified by the string names. Hence word <i>Set</i> is used in
   * {@link #readSetBegin()} and {@link #readSetNext()} method names.
   * <p>
   * If there is any element in seuqnce, then reading stops before first non-skipped character after sequence begin
   * character and method returns <code>true</code>. If there are no items in list, reads sequence matching end
   * character, and reading stops before first non-skipped character after <b>end</b> character and method returns
   * <code>false</code>.
   * <p>
   * This method together with {@link #readArrayNext()} is intended to make easy reading of the sequences:
   *
   * <pre>
   * // reading collection/list/array, maybe the empty one
   * if( strioReader.readListBegin() ) {
   *   do {
   *     // read and process next element
   *     String someElemStr = strioReader.readUntilDelimiter();
   *     // ... do somthing
   *   } while( readListNext() );
   * }
   * </pre>
   *
   * @return <b>true</b> - the is at least one element in list, need to read elements separated by
   *         {@link IStrioHardConstants#CHAR_ITEM_SEPARATOR};<br>
   *         <b>false</b> - list has no elements and it was read already.
   * @throws StrioRtException first non-skipped character is not {@link IStrioHardConstants#CHAR_ARRAY_BEGIN}
   */
  boolean readArrayBegin();

  /**
   * Determines if there is next element to read from sequence enclosed in the square brackets.
   * <p>
   * This method is intended to be used together with the {@link #readArrayBegin()} method.
   *
   * @return <b>true</b> - item separator was read, without ending char, there are more elements to read;<br>
   *         <b>false</b> - sequence ending character was read, no more elements to read.
   * @throws StrioRtException any other character was read
   */
  boolean readArrayNext();

  /**
   * Starts reading the sequence of the elements enclosed in the squuare brackets.
   * <p>
   * This method is used like {@link #readArrayBegin()}
   *
   * @return <b>true</b> - the is at least one element in list, need to read elements separated by
   *         {@link IStrioHardConstants#CHAR_ITEM_SEPARATOR};<br>
   *         <b>false</b> - list has no elements and it was read already.
   * @throws StrioRtException first non-skipped character is not {@link IStrioHardConstants#CHAR_ARRAY_BEGIN}
   * @see #readArrayBegin()
   */
  boolean readSetBegin();

  /**
   * Determines if there is next element to read from sequence enclosed in the braces.
   * <p>
   * This method is intended to be used together with the {@link #readSetBegin()} method.
   *
   * @return <b>true</b> - item separator was read, without ending char, there are more elements to read;<br>
   *         <b>false</b> - sequence ending character was read, no more elements to read.
   * @throws StrioRtException any other character was read
   */
  boolean readSetNext();

}
