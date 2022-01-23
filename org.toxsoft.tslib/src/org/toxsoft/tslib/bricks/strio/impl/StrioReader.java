package org.toxsoft.tslib.bricks.strio.impl;

import static org.toxsoft.tslib.bricks.strio.EStrioSkipMode.*;
import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.tslib.bricks.strio.impl.ITsResources.*;
import static org.toxsoft.tslib.bricks.strio.impl.StrioUtils.*;

import java.util.Calendar;

import org.toxsoft.tslib.bricks.strid.impl.StridUtils;
import org.toxsoft.tslib.bricks.strio.*;
import org.toxsoft.tslib.bricks.strio.chario.ICharInputStream;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link IStrioReader} implementation.
 *
 * @author hazard157
 */
public class StrioReader
    extends StrioStreamBase
    implements IStrioReader {

  /**
   * Next token string builder.
   */
  private final StringBuilder tokenBuilder = new StringBuilder();

  /**
   * Оболочка над заданным потокм, используемая для чтения входных символов.
   */
  private final CharSource charSource;

  /**
   * Режим пропуска игнорируемых символов при чтении входного потока.
   */
  private EStrioSkipMode currentSkipMode = EStrioSkipMode.SKIP_COMMENTS;

  /**
   * Line comment starting character.
   */
  private char lineCommentChar = IStrioHardConstants.CHAR_LINE_COMMENT_SHELL;

  /**
   * Создает читатель, привязанный к входному потоку.
   *
   * @param aCharInputStream ICharInputStream - входной поток символов
   * @throws TsNullArgumentRtException аргумент = null
   */
  public StrioReader( ICharInputStream aCharInputStream ) {
    charSource = new CharSource( aCharInputStream );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  /**
   * Проверяет, что считывается ожидаемая строка.<br>
   * Чтение останавливается на первом отличающемся символе или по концу строки - аргумента.
   *
   * @param aString String - следующие символы должны совпдать с этой строкой
   * @return <b>true</b> - было считано aString.length() символов, совпадающих с символами в строке astring;<br>
   *         <b>false</b> - считываемые имволы не совпадают со строкой-аргументом.
   * @throws TsNullArgumentRtException - аргумент = null
   * @throws TsIoRtException ошибки ввода-вывода
   */
  private boolean checkString( String aString ) {
    for( int i = 0, n = aString.length(); i < n; i++ ) {
      if( nextChar() != aString.charAt( i ) ) {
        return false;
      }
    }
    return true;
  }

  /**
   * Считывает очередной символ, который не должен быть {@link IStrioHardConstants#CHAR_EOF}.
   *
   * @return char - считанный символ
   * @throws StrioRtException был считан {@link IStrioHardConstants#CHAR_EOF}
   */
  private char nextCharNonEof() {
    char ch = nextChar();
    StrioRtException.checkTrue( ch == CHAR_EOF, MSG_ERR_UNEXPECTED_EOF );
    return ch;
  }

  /**
   * Считывает неотрицательное целое число ровно из aDigitNum цифр.
   * <p>
   * Чтение останавливается после aDigitNum символов или на первой не-цифре (то есть,
   * {@link StrioUtils#isAsciiDigit(char) = false}).
   * <p>
   * Используется только для чтения меток времени.
   *
   * @param aDigitsNum int - сколькозначное целое считывать
   * @return long - неотрицательное считанное число
   * @throws StrioRtException aDigitNum символов не являются цифрами
   */
  private long readExactDigitDecUint( int aDigitsNum ) {
    long value = 0;
    for( int i = 0; i < aDigitsNum; i++ ) {
      char ch = nextChar();
      StrioRtException.checkFalse( isAsciiDigit( ch ), MSG_ERR_INV_DEC_DIGIT );
      value *= 10;
      value += ch - '0';
    }
    return value;
  }

  /**
   * Считывает неотрицательное целое число ровно из aDigitNum цифр, которая должна быть в заданных пределах.
   * <p>
   * Чтение останавливается после aDigitNum символов или на первой не-цифре (то есть,
   * {@link StrioUtils#isAsciiDigit(char) = false}).
   * <p>
   * Используется только для чтения меток времени.
   *
   * @param aDigitsNum int - сколькозначное целое считывать
   * @param aMinValue int - минимальное допустимое (включительно) значение числа
   * @param aMaxValue int - максимально допустимое (включительно) значение числа
   * @return int - неотрицательное считанное число
   * @throws StrioRtException aDigitNum символов не являются цифрами
   * @throws StrioRtException число выходит за заданные пределы
   */
  private int readExactDigitUint( int aDigitsNum, int aMinValue, int aMaxValue ) {
    long value = readExactDigitDecUint( aDigitsNum );
    StrioRtException.checkTrue( value < aMinValue, MSG_ERR_INV_TIMESTAMP );
    StrioRtException.checkTrue( value > aMaxValue, MSG_ERR_INV_TIMESTAMP );
    return (int)value;
  }

  /**
   * Считывает неотрицательное целое число ровно из aDigitNum 16-ричных цифр.
   * <p>
   * Чтение останавливается после aDigitNum символов или на первой не-цифре (то есть, {@link StrioUtils#isHexChar(char)
   * = false}).
   * <p>
   * Используется только для чтения двоичных строк.
   *
   * @param aDigitsNum int - сколькозначное целое считывать, должно быть <= 9
   * @return int - неотрицательное считанное число
   * @throws StrioRtException aDigitNum символов не являются hex-цифрами
   */
  private long readExactDigitHexUint( int aDigitsNum ) {
    long value = 0;
    for( int i = 0; i < aDigitsNum; i++ ) {
      char ch = nextChar();
      StrioRtException.checkFalse( isHexChar( ch ), MSG_ERR_INV_HEX_DIGIT );
      value *= 16;
      value += hexChar2Int( ch );
    }
    return value;
  }

  // ------------------------------------------------------------------------------------
  // IStrioReader
  //

  @Override
  public EStrioSkipMode skipMode() {
    return currentSkipMode;
  }

  @Override
  public EStrioSkipMode setSkipMode( EStrioSkipMode aSkipMode ) {
    EStrioSkipMode oldMode = currentSkipMode;
    currentSkipMode = TsNullArgumentRtException.checkNull( aSkipMode );
    return oldMode;
  }

  @Override
  public char getLineCommentChar() {
    return lineCommentChar;
  }

  @Override
  public void setLineCommentChar( char aChar ) {
    lineCommentChar = aChar;
  }

  // ------------------------------------------------------------------------------------
  // Basic reading

  @Override
  public void putCharBack() {
    charSource.putCharBack();
  }

  @Override
  public char nextChar( EStrioSkipMode aSkipMode ) {
    if( aSkipMode == null ) {
      throw new TsNullArgumentRtException();
    }
    char ch;
    switch( aSkipMode ) {
      case SKIP_NONE:
        return nextChar();
      case SKIP_SPACES:
        while( isSpaceChar( ch = nextChar() ) ) {
          if( ch == CHAR_EOF ) {
            return CHAR_EOF;
          }
        }
        return ch;
      case SKIP_BYPASSED:
        while( isBypassedChar( ch = nextChar() ) ) {
          if( ch == CHAR_EOF ) {
            return CHAR_EOF;
          }
        }
        return ch;
      case SKIP_COMMENTS:
        while( isBypassedChar( ch = nextChar() ) || ch == lineCommentChar ) {
          if( ch == lineCommentChar ) { // пропустим комментарии и продолжим пропуск символов-пропусков
            while( (ch = nextChar()) != CHAR_EOL ) {
              if( ch == CHAR_EOF ) {
                return CHAR_EOF;
              }
            }
          }
        }
        return ch;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  public char nextChar() {
    int c = charSource.nextChar();
    if( c == -1 ) {
      return CHAR_EOF;
    }
    return (char)c;
  }

  @Override
  public char peekChar() {
    return peekChar( currentSkipMode );
  }

  @Override
  public char peekChar( EStrioSkipMode aSkipMode ) {
    TsNullArgumentRtException.checkNull( aSkipMode );
    char ch = nextChar();
    switch( aSkipMode ) {
      case SKIP_NONE:
        break;
      case SKIP_SPACES:
        while( isSpaceChar( ch ) ) {
          ch = nextChar();
        }
        break;
      case SKIP_BYPASSED:
        while( isBypassedChar( ch ) ) {
          ch = nextChar();
        }
        break;
      case SKIP_COMMENTS:
        while( isBypassedChar( ch ) || ch == lineCommentChar ) {
          if( ch == lineCommentChar ) { // пропустим комментарии и продолжим пропуск символов-пропусков
            while( (ch = nextChar()) != CHAR_EOL ) {
              if( ch == CHAR_EOF ) {
                charSource.putCharBack();
                return ch;
              }
            }
          }
          ch = nextChar();
          if( ch == CHAR_EOF ) {
            charSource.putCharBack();
            return ch;
          }
        }
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    charSource.putCharBack();
    return ch;
  }

  @Override
  public char ensureChar( char aCh ) {
    char ch = nextChar( currentSkipMode );
    StrioRtException.checkTrue( ch == CHAR_EOF, FMT_ERR_EOF_INSETEAD_CH, Character.valueOf( aCh ) );
    StrioRtException.checkTrue( ch != aCh, FMT_ERR_CHAR_EXPECTED, Character.valueOf( ch ), Character.valueOf( aCh ) );
    return ch;
  }

  @Override
  public char ensureSeparatorChar() {
    return ensureChar( CHAR_ITEM_SEPARATOR );
  }

  @Override
  public void ensureString( String aStr ) {
    peekChar( currentSkipMode );
    StrioRtException.checkFalse( checkString( aStr ), FMT_ERR_STRING_EXPECTED, aStr );
  }

  @Override
  public String readUntilChar( char aCh ) {
    tokenBuilder.setLength( 0 );
    char ch = nextChar();
    while( ch != CHAR_EOF && ch != aCh ) {
      tokenBuilder.append( ch );
      ch = nextChar();
    }
    putCharBack();
    return tokenBuilder.toString();
  }

  @Override
  public String readLine() {
    tokenBuilder.setLength( 0 );
    char ch = nextChar();
    if( ch != CHAR_EOF ) {
      while( !isEol( ch ) ) {
        tokenBuilder.append( ch );
        ch = nextChar();
        if( ch == CHAR_EOF ) {
          break;
        }
      }
    }
    return tokenBuilder.toString();
  }

  @Override
  public String readUntilChars( String aCharsString ) {
    TsNullArgumentRtException.checkNull( aCharsString );
    TsIllegalArgumentRtException.checkTrue( aCharsString.isEmpty() );
    tokenBuilder.setLength( 0 );
    char ch = nextChar( currentSkipMode );
    if( ch != CHAR_EOF ) {
      while( aCharsString.indexOf( ch ) < 0 ) {
        tokenBuilder.append( ch );
        ch = nextChar();
        if( ch == CHAR_EOF ) {
          break;
        }
      }
      putCharBack();
    }
    return tokenBuilder.toString();
  }

  @Override
  public String readUntilDelimiter() {
    tokenBuilder.setLength( 0 );
    char ch = nextChar( currentSkipMode );
    if( ch != CHAR_EOF ) {
      while( !isDelimiterChar( ch ) ) {
        tokenBuilder.append( ch );
        ch = nextChar();
        if( ch == CHAR_EOF ) {
          break;
        }
      }
      putCharBack();
    }
    return tokenBuilder.toString();
  }

  // ------------------------------------------------------------------------------------
  // Read primitives

  @Override
  public boolean readBoolean() {
    String token = readUntilDelimiter();
    switch( token ) {
      case STR_BOOLEAN_TRUE:
        return true;
      case STR_BOOLEAN_FALSE:
        return false;
      default:
        throw new StrioRtException( FMT_ERR_INV_BOOL_NAME, token );
    }
  }

  @Override
  public int readInt() {
    long val = readLong();
    if( val < Integer.MIN_VALUE || val > Integer.MAX_VALUE ) {
      throw new StrioRtException( MSG_ERR_TOO_BIG_INT );
    }
    return (int)val;
  }

  private long readDecUint( char aFirstChar, boolean aIsNegative ) {
    char ch = aFirstChar;
    // должна быть хотя бы одна цифра
    if( !isAsciiDigit( ch ) ) {
      throw new StrioRtException( MSG_ERR_INV_INTEGER );
    }
    // Решение заимствовано из Long.parseLong:
    long retVal = 0;
    long limit = (aIsNegative ? Long.MIN_VALUE : -Long.MAX_VALUE);
    while( isAsciiDigit( ch ) ) {
      // Accumulating negatively avoids surprises near MAX_VALUE
      int digit = ch - '0';
      if( retVal < limit / 10 ) {
        throw new StrioRtException( MSG_ERR_TOO_BIG_LONG );
      }
      retVal *= 10;
      if( retVal < limit + digit ) {
        throw new StrioRtException( MSG_ERR_TOO_BIG_LONG );
      }
      retVal -= digit;
      ch = nextChar();
    }
    putCharBack();
    return retVal;
  }

  private long readHexUint( char aFirstChar, boolean aIsNegative ) {
    char ch = aFirstChar;
    // должна быть хотя бы одна цифра
    if( !isHexChar( ch ) ) {
      throw new StrioRtException( MSG_ERR_INV_INTEGER );
    }
    // Решение заимствовано из Long.parseLong:
    long retVal = 0;
    long limit = (aIsNegative ? Long.MIN_VALUE : -Long.MAX_VALUE);
    while( isHexChar( ch ) ) {
      // Accumulating negatively avoids surprises near MAX_VALUE
      int digit = hexChar2Int( ch );
      if( retVal < limit / 16 ) {
        throw new StrioRtException( MSG_ERR_TOO_BIG_LONG );
      }
      retVal *= 16;
      if( retVal < limit + digit ) {
        throw new StrioRtException( MSG_ERR_TOO_BIG_LONG );
      }
      retVal -= digit;
      ch = nextChar();
    }
    putCharBack();
    return retVal;
  }

  @Override
  public long readLong() {
    char ch = nextChar( currentSkipMode );
    if( ch == CHAR_EOF ) {
      throw new StrioRtException( MSG_ERR_UNEXPECTED_EOF );
    }
    boolean isNegative = false;
    if( ch == '-' ) {
      isNegative = true;
      ch = nextChar();
    }
    // проверим на то, что может быть 16-ричное число вида 0x123ABC
    boolean isHex = false;
    if( ch == '0' ) {
      char ch2 = peekChar( SKIP_NONE );
      if( ch2 == 'x' || ch == 'X' ) { // интерпретируем как 16-ричное число
        nextChar();
        ch = nextChar();
        isHex = true;
      }
    }
    long retVal;
    if( isHex ) {
      retVal = readHexUint( ch, isNegative );
    }
    else {
      retVal = readDecUint( ch, isNegative );
    }
    if( isNegative == false ) {
      retVal = -retVal;
    }
    return retVal;
  }

  @Override
  public long readLong( int aDigitsNum, boolean aHexNumber ) {
    TsIllegalArgumentRtException.checkTrue( aDigitsNum <= 0 );
    char ch = peekChar( currentSkipMode );
    if( ch == CHAR_EOF ) {
      throw new StrioRtException( MSG_ERR_UNEXPECTED_EOF );
    }
    if( aHexNumber ) {
      TsIllegalArgumentRtException.checkTrue( aDigitsNum > 8 );
      return readExactDigitHexUint( aDigitsNum );
    }
    TsIllegalArgumentRtException.checkTrue( aDigitsNum > 18 );
    return readExactDigitDecUint( aDigitsNum );
  }

  @Override
  public float readFloat() {
    return Float.parseFloat( readUntilDelimiter() );
  }

  private String readStringForDouble( EStrioSkipMode aSkipMode ) {
    tokenBuilder.setLength( 0 );
    char ch = nextChar( aSkipMode );
    if( ch == '-' ) { // необязательный унарный минус
      tokenBuilder.append( ch );
      ch = nextChar();
    }
    if( ch == 'N' || ch == 'I' ) { // чтение констант "NaN" или "Infinity"
      tokenBuilder.append( ch );
      if( ch == 'N' ) {
        tokenBuilder.append( ensureChar( 'a' ) );
        tokenBuilder.append( ensureChar( 'N' ) );
      }
      else {
        tokenBuilder.append( ensureChar( 'n' ) );
        tokenBuilder.append( ensureChar( 'f' ) );
        tokenBuilder.append( ensureChar( 'i' ) );
        tokenBuilder.append( ensureChar( 'n' ) );
        tokenBuilder.append( ensureChar( 'i' ) );
        tokenBuilder.append( ensureChar( 't' ) );
        tokenBuilder.append( ensureChar( 'y' ) );
      }
      return tokenBuilder.toString();
    }
    int hasPartsFlags = 0x00;
    while( isAsciiDigit( ch ) ) { // необязательные цифры до точки
      tokenBuilder.append( ch );
      ch = nextChar();
      hasPartsFlags |= 0x01;
    }
    if( ch == '.' ) { // десятичная точка
      tokenBuilder.append( ch );
      ch = nextChar();
      hasPartsFlags |= 0x02;
    }
    while( isAsciiDigit( ch ) ) { // необязательные цифры после точки
      tokenBuilder.append( ch );
      ch = nextChar();
      hasPartsFlags |= 0x04;
    }
    StrioRtException.checkTrue( (hasPartsFlags & (0x01 | 0x04)) == 0, MSG_ERR_INV_FLOATING_NUM );
    if( ch == 'e' || ch == 'E' ) { // число имеет степенную часть
      hasPartsFlags |= 0x08;
      tokenBuilder.append( ch );
      ch = nextChar();
      if( ch == '-' || ch == '+' ) { // знак степени числа
        tokenBuilder.append( ch );
        ch = nextChar();
        hasPartsFlags |= 0x10;
      }
      while( isAsciiDigit( ch ) ) { // цифры степени числа
        tokenBuilder.append( ch );
        ch = nextChar();
        hasPartsFlags |= 0x20;
      }
      StrioRtException.checkTrue( (hasPartsFlags & 0x20) == 0, MSG_ERR_INV_FLOATING_NUM );
    }
    putCharBack();
    return tokenBuilder.toString();
  }

  @Override
  public double readDouble() {
    String s = readStringForDouble( currentSkipMode );
    return Double.parseDouble( s );
  }

  @Override
  public long readTimestamp() {
    peekChar( currentSkipMode );
    int year = 0, month = 0, day = 0, hour = 0, min = 0, sec = 0, msec = 0;
    year = readExactDigitUint( 4, 0, 9999 );
    ensureChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    month = readExactDigitUint( 2, 1, 12 ) - 1;
    ensureChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    day = readExactDigitUint( 2, 1, 31 );
    char ch = nextChar();
    if( ch == CHAR_TIMESTAMP_DATETIME_SEPARATOR ) {
      hour = readExactDigitUint( 2, 0, 23 );
      ensureChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
      min = readExactDigitUint( 2, 0, 59 );
      ensureChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
      sec = readExactDigitUint( 2, 0, 59 );
      ch = nextChar();
      if( ch == CHAR_TIMESTAMP_MILLISEC_SEPARATOR ) {
        msec = readExactDigitUint( 3, 0, 999 );
      }
      else { // дата_время должна завершаться разделителем
        putCharBack(); // вернем - это не наш символ
        StrioRtException.checkFalse( isDelimiterChar( ch ), MSG_ERR_INV_TIMESTAMP );
      }
    }
    else { // только дата должна завершаться разделителем
      putCharBack(); // вернем - это не наш символ
      StrioRtException.checkFalse( isDelimiterChar( ch ), MSG_ERR_INV_TIMESTAMP );
    }
    calendar().set( Calendar.YEAR, year );
    calendar().set( Calendar.MONTH, month );
    calendar().set( Calendar.DAY_OF_MONTH, day );
    calendar().set( Calendar.HOUR_OF_DAY, hour );
    calendar().set( Calendar.MINUTE, min );
    calendar().set( Calendar.SECOND, sec );
    calendar().set( Calendar.MILLISECOND, msec );
    return calendar().getTimeInMillis();
  }

  @Override
  public String readQuotedString() {
    tokenBuilder.setLength( 0 );
    char ch = peekChar( currentSkipMode );
    if( ch != CHAR_QUOTE ) {
      throw new StrioRtException( FMT_ERR_QUOTE_EXPECTED, Character.valueOf( ch ) );
    }
    nextCharNonEof(); // считаем кавычку
    if( peekChar( SKIP_NONE ) == CHAR_QUOTE ) {
      nextChar();
      return TsLibUtils.EMPTY_STRING;
    }
    boolean done = false;
    while( !done ) {
      ch = nextCharNonEof();
      if( ch == CHAR_QUOTE ) {
        done = true;
        continue;
      }
      if( ch == CHAR_ESCAPE ) {
        ch = nextCharNonEof();
        switch( ch ) {
          case CHAR_ESCAPE:
          case CHAR_QUOTE:
            tokenBuilder.append( ch );
            break;
          case 'n':
            tokenBuilder.append( '\n' );
            break;
          case 'r':
            tokenBuilder.append( '\r' );
            break;
          default:
            tokenBuilder.append( CHAR_ESCAPE );
            tokenBuilder.append( ch );
            break;
        }
      }
      else {
        tokenBuilder.append( ch );
      }
    }
    return tokenBuilder.toString();
  }

  @Override
  public String readIdName() {
    tokenBuilder.setLength( 0 );
    char ch = nextChar( currentSkipMode );
    StrioRtException.checkFalse( StridUtils.isIdStart( ch ), FMT_ERR_INV_ID_PATH_START, Character.valueOf( ch ) );
    do {
      tokenBuilder.append( ch );
      ch = nextChar();
    } while( StridUtils.isIdNamePart( ch ) );
    putCharBack();
    String idName = tokenBuilder.toString();
    StrioRtException.checkFalse( StridUtils.isValidIdName( idName ), MSG_ERR_CANT_READ_ID_NAME );
    return idName;
  }

  @Override
  public String readIdPath() {
    tokenBuilder.setLength( 0 );
    char ch = nextChar( currentSkipMode );
    StrioRtException.checkFalse( StridUtils.isIdStart( ch ), FMT_ERR_INV_ID_PATH_START, Character.valueOf( ch ) );
    do {
      tokenBuilder.append( ch );
      ch = nextChar();
    } while( StridUtils.isIdPathPart( ch ) );
    putCharBack();
    String idPath = tokenBuilder.toString();
    StrioRtException.checkFalse( StridUtils.isValidIdPath( idPath ), MSG_ERR_CANT_READ_ID_PATH );
    return idPath;
  }

  // ------------------------------------------------------------------------------------
  // Read constructs

  @Override
  public boolean readArrayBegin() {
    peekChar( SKIP_COMMENTS );
    ensureChar( CHAR_ARRAY_BEGIN );
    if( peekChar( SKIP_COMMENTS ) == CHAR_ARRAY_END ) {
      nextChar();
      return false;
    }
    return true;
  }

  @Override
  public boolean readArrayNext() {
    switch( nextChar( SKIP_COMMENTS ) ) {
      case CHAR_ARRAY_END:
        return false;
      case CHAR_ITEM_SEPARATOR:
        // comma after last item IS allowed
        if( peekChar( SKIP_COMMENTS ) == CHAR_ARRAY_END ) {
          nextChar( SKIP_COMMENTS );
          return false;
        }
        return true;
      default:
        putCharBack();
        throw new StrioRtException( MSG_ERR_INV_ARRAY_NEXT );
    }
  }

  @Override
  public boolean readSetBegin() {
    peekChar( SKIP_COMMENTS );
    ensureChar( CHAR_SET_BEGIN );
    if( peekChar( SKIP_COMMENTS ) == CHAR_SET_END ) {
      nextChar();
      return false;
    }
    return true;
  }

  @Override
  public boolean readSetNext() {
    switch( nextChar( SKIP_COMMENTS ) ) {
      case CHAR_SET_END:
        return false;
      case CHAR_ITEM_SEPARATOR:
        // comma after last item IS allowed
        if( peekChar( SKIP_COMMENTS ) == CHAR_SET_END ) {
          nextChar( SKIP_COMMENTS );
          return false;
        }
        return true;
      default:
        putCharBack();
        throw new StrioRtException( MSG_ERR_INV_SET_NEXT );
    }
  }

  // ------------------------------------------------------------------------------------
  // Additional API
  //

  /**
   * Returns the source character stream.
   *
   * @return {@link ICharInputStream} - character input
   */
  public final ICharInputStream getInput() {
    return charSource.inputStream();
  }

  /**
   * Sets the source character stream.
   *
   * @param aCharInputStream {@link ICharInputStream} - character input
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public final void setInput( ICharInputStream aCharInputStream ) {
    charSource.setInputStream( aCharInputStream );
  }

}
