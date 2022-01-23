package org.toxsoft.tslib.bricks.strio.impl;

import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.tslib.bricks.strio.impl.ITsResources.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

import org.toxsoft.tslib.bricks.strio.IStrioWriter;
import org.toxsoft.tslib.bricks.strio.StrioRtException;
import org.toxsoft.tslib.bricks.strio.chario.ICharOutputStream;
import org.toxsoft.tslib.utils.errors.*;

// TRANSLATE

/**
 * Реализация писателя IStridWriter в выходной символьный поток.
 *
 * @author hazard157
 */
public class StrioWriter
    extends StrioStreamBase
    implements IStrioWriter {

  /**
   * Префикс при записи 16-ричных чисел методами {@link #writeIntHex(int)} и {@link #writeLongHex(long)}.
   */
  private static final String HEX_NUMBER_PREFIX = "0x"; //$NON-NLS-1$

  /**
   * Минимально допустимое записываемое методом {@link #writeTimestamp(long)} значение метки времени.
   */
  private static final long MIN_WRITREABLE_TIMESTAMP = -30609806400000L; // 1000-01-01 00:00:00.000

  /**
   * Максимаьлно допустимое записываемое методом {@link #writeTimestamp(long)} значение метки времени.
   */
  private static final long MAX_WRITREABLE_TIMESTAMP = 253402286399999L; // 9999-12-31 23:59:59.999

  /**
   * Строки форматирования для {@link #writeDouble(double, int)}
   */
  private static final String[] DOUBLE_FMT_STRS = new String[] { "%.0f", //$NON-NLS-1$
      "%.1f", //$NON-NLS-1$
      "%.2f", //$NON-NLS-1$
      "%.3f", //$NON-NLS-1$
      "%.4f", //$NON-NLS-1$
      "%.5f", //$NON-NLS-1$
      "%.6f", //$NON-NLS-1$
      "%.7f", //$NON-NLS-1$
      "%.8f" //$NON-NLS-1$
  };

  private ICharOutputStream charOut = ICharOutputStream.NONE;

  /**
   * Буфер для вывода целых чисел.
   * <p>
   * Использование этого буфера является оптимизацией метода {@link #outZeroPaddedInt(int, int)}, но делает невозможным
   * вызывать методы класса из разных потоков.
   */
  private char[] numBuf = new char[32];

  /**
   * Количество пробелов в отступе.
   */
  private int indentSpaces = 2;

  /**
   * Количество отстутпов в начале строки.
   */
  private int indentLevel = 0;

  /**
   * Создает писатель с привязкой с выходному символьному потоку.
   *
   * @param aSource ICharOutputStream - выхродной потока
   */
  public StrioWriter( ICharOutputStream aSource ) {
    charOut = TsNullArgumentRtException.checkNull( aSource );
  }

  // --------------------------------------------------------------------------
  // Внутренные операции вывода базовых примитивов
  //

  /**
   * Записывает символ в место назначение.
   *
   * @param aCh char - записываемый символ
   * @throws StrioRtException - при ошибке ввода/вывода
   */
  private void outChar( char aCh )
      throws StrioRtException {
    try {
      charOut.writeChar( aCh );
    }
    catch( IOException e ) {
      throw new StrioRtException( e, MSG_ERR_IO_EXCEPTION );
    }
  }

  /**
   * Записывает строку (без каких-либо изменений) в место назначения.
   *
   * @param aString String - записываемая строка
   * @throws TsNullArgumentRtException - аргумент = null
   * @throws StrioRtException - при ошибке ввода/вывода
   */
  private void outString( String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    try {
      for( int i = 0; i < aString.length(); i++ ) {
        charOut.writeChar( aString.charAt( i ) );
      }
    }
    catch( IOException e ) {
      throw new StrioRtException( e, MSG_ERR_IO_EXCEPTION );
    }
  }

  /**
   * Записывает целое число с дополнением слева нулями до заданного количества цифр.
   *
   * @param aValue int - записываемое значение
   * @param aDigits int - количество цифр, включая знак '-'
   */
  private void outZeroPaddedInt( int aValue, int aDigits ) {
    int val = aValue;
    int digits = aDigits;
    if( val < 0 ) {
      val = -val;
      outChar( '-' );
      --digits;
    }
    int count = 0;
    do {
      numBuf[count] = (char)((val % 10) + '0');
      val /= 10;
      ++count;
    } while( val != 0 );
    for( int i = count; i < digits; i++ ) {
      outChar( '0' );
    }
    for( int i = count - 1; i >= 0; i-- ) {
      outChar( numBuf[i] );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов IStridWriter
  //

  @Override
  public void setIndentSpaces( int aSpacesCount ) {
    if( aSpacesCount < 0 ) {
      indentSpaces = 0;
    }
    else {
      if( aSpacesCount > 20 ) {
        indentSpaces = 20;
      }
      else {
        indentSpaces = aSpacesCount;
      }
    }
  }

  @Override
  public int getIndentSpaces() {
    return indentSpaces;
  }

  @Override
  public void setIndentLevel( int aLevel ) {
    if( aLevel < 0 ) {
      indentLevel = 0;
    }
    else {
      if( aLevel > 20 ) {
        indentLevel = 20;
      }
      else {
        indentLevel = aLevel;
      }
    }
  }

  @Override
  public void incNewLine() {
    setIndentLevel( indentLevel + 1 );
    writeEol();
  }

  @Override
  public void decNewLine() {
    setIndentLevel( indentLevel - 1 );
    writeEol();
  }

  @Override
  public int getIndentLevel() {
    return indentLevel;
  }

  @Override
  public void writeChar( char aCh ) {
    outChar( aCh );
  }

  @Override
  public void writeChars( char... aChars ) {
    for( int i = 0; i < aChars.length; i++ ) {
      outChar( aChars[i] );
    }
  }

  @Override
  public void writeEol() {
    outChar( CHAR_EOL );
    int count = indentLevel * indentSpaces;
    for( int i = 0; i < count; i++ ) {
      outChar( CHAR_SPACE );
    }
  }

  @Override
  public void p( String aFormatString, Object... aArgs ) {
    TsNullArgumentRtException.checkNull( aFormatString );
    TsErrorUtils.checkArrayArg( aArgs );
    outString( String.format( aFormatString, aArgs ) );
  }

  @Override
  public void pl( String aFormatString, Object... aArgs ) {
    TsNullArgumentRtException.checkNull( aFormatString );
    TsErrorUtils.checkArrayArg( aArgs );
    outString( String.format( aFormatString, aArgs ) );
    writeEol();
  }

  @Override
  public void writeSpace() {
    outChar( CHAR_SPACE );
  }

  @Override
  public void writeSeparatorChar() {
    outChar( CHAR_ITEM_SEPARATOR );
  }

  @Override
  public void writeBoolean( boolean aValue ) {
    if( aValue ) {
      outString( STR_BOOLEAN_TRUE );
    }
    else {
      outString( STR_BOOLEAN_FALSE );
    }
  }

  @Override
  public void writeInt( int aValue ) {
    outString( Integer.toString( aValue ) );
  }

  @Override
  public void writeIntHex( int aValue ) {
    outString( HEX_NUMBER_PREFIX );
    outString( Integer.toString( aValue, 16 ).toUpperCase() );
  }

  @Override
  public void writeLong( long aValue ) {
    outString( Long.toString( aValue ) );
  }

  @Override
  public void writeLongHex( long aValue ) {
    outString( HEX_NUMBER_PREFIX );
    outString( Long.toString( aValue, 16 ).toUpperCase() );
  }

  @Override
  public void writeFloat( float aValue ) {
    outString( Float.toString( aValue ) );
  }

  @Override
  public void writeDouble( double aValue ) {
    outString( Double.toString( aValue ) );
  }

  @Override
  public void writeDouble( double aValue, int aDecimalDigits ) {
    int didx = aDecimalDigits;
    if( didx < 0 ) {
      didx = 0;
    }
    if( didx > 8 ) {
      didx = 8;
    }
    String fmtStr = DOUBLE_FMT_STRS[didx];
    outString( String.format( Locale.US, fmtStr, (Object)DOUBLE_FMT_STRS ) );
  }

  @Override
  public void writeDate( long aTimestamp ) {
    calendar().setTimeInMillis( aTimestamp );
    outZeroPaddedInt( calendar().get( Calendar.YEAR ), 4 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.MONTH ) + 1, 2 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.DAY_OF_MONTH ), 2 );
  }

  @Override
  public void writeDateTime( long aTimestamp ) {
    // проверить, в допустимом ли диапазоне метка времени
    TsIllegalArgumentRtException
        .checkFalse( aTimestamp >= MIN_WRITREABLE_TIMESTAMP && aTimestamp <= MAX_WRITREABLE_TIMESTAMP );
    calendar().setTimeInMillis( aTimestamp );
    outZeroPaddedInt( calendar().get( Calendar.YEAR ), 4 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.MONTH ) + 1, 2 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.DAY_OF_MONTH ), 2 );
    outChar( CHAR_TIMESTAMP_DATETIME_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.HOUR_OF_DAY ), 2 );
    outChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.MINUTE ), 2 );
    outChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.SECOND ), 2 );
  }

  @Override
  public void writeTimestamp( long aTimestamp ) {
    // проверить, в допустимом ли диапазоне метка времени
    TsIllegalArgumentRtException
        .checkFalse( aTimestamp >= MIN_WRITREABLE_TIMESTAMP && aTimestamp <= MAX_WRITREABLE_TIMESTAMP );
    calendar().setTimeInMillis( aTimestamp );
    outZeroPaddedInt( calendar().get( Calendar.YEAR ), 4 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.MONTH ) + 1, 2 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.DAY_OF_MONTH ), 2 );
    outChar( CHAR_TIMESTAMP_DATETIME_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.HOUR_OF_DAY ), 2 );
    outChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.MINUTE ), 2 );
    outChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.SECOND ), 2 );
    outChar( CHAR_TIMESTAMP_MILLISEC_SEPARATOR );
    outZeroPaddedInt( calendar().get( Calendar.MILLISECOND ), 3 );
  }

  @Override
  public void writeAsIs( String aString ) {
    outString( aString );
  }

  @Override
  public void writeQuotedString( String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    outChar( CHAR_QUOTE );
    for( int i = 0; i < aString.length(); i++ ) {
      char ch = aString.charAt( i );
      if( ch == CHAR_QUOTE || ch == CHAR_ESCAPE ) {
        outChar( CHAR_ESCAPE );
      }
      if( ch == '\n' ) {
        outChar( '\\' );
        outChar( 'n' );
      }
      else {
        if( ch == '\r' ) {
          outChar( '\\' );
          outChar( 'r' );
        }
        else {
          outChar( ch );
        }
      }
    }
    outChar( CHAR_QUOTE );
  }

  // ------------------------------------------------------------------------------------
  // Additional API
  //

  /**
   * Returns the destination stream.
   *
   * @return {@link ICharOutputStream} - character output
   */
  public ICharOutputStream getOutput() {
    return charOut;
  }

  /**
   * Sets the destination stream.
   *
   * @param aSource {@link ICharOutputStream} - character output
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public void setOutput( ICharOutputStream aSource ) {
    charOut = TsNullArgumentRtException.checkNull( aSource );
  }

}
