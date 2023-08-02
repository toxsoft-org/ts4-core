package org.toxsoft.core.tslib.bricks.strio.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.ITsResources.*;

import java.io.*;
import java.time.*;
import java.util.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IStrioWriter} implementation.
 *
 * @author hazard157
 */
public class StrioWriter
    extends StrioStreamBase
    implements IStrioWriter {

  /**
   * Prefix when writing hexadecimal numbers using the <code>writeXxxHex()</code> methods.
   */
  private static final String HEX_NUMBER_PREFIX = "0x"; //$NON-NLS-1$

  /**
   * The minimum allowed timestamp value written by the {@link #writeTimestamp(long)} method.
   */
  private static final long MIN_WRITREABLE_TIMESTAMP = -30609806400000L; // 1000-01-01 00:00:00.000

  /**
   * The maximum allowed timestamp value written by the {@link #writeTimestamp(long)} method.
   */
  private static final long MAX_WRITREABLE_TIMESTAMP = 253402286399999L; // 9999-12-31 23:59:59.999

  /**
   * The minimum allowed timestamp value written by the <code>writeTime()</code> methods.
   */
  private static final LocalDate MIN_WRITREABLE_TIME_DATE = LocalDate.of( 1000, Month.JANUARY, 1 );

  /**
   * The maximum allowed timestamp value written by the <code>writeTime()</code> methods.
   */
  private static final LocalDate MAX_WRITREABLE_TIME_DATE = LocalDate.of( 9999, Month.DECEMBER, 31 );

  /**
   * Format strings for {@link #writeDouble(double, int)}
   */
  private static final String[] DOUBLE_FMT_STRS = { "%.0f", //$NON-NLS-1$
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
   * Buffer for outputting integers.
   * <p>
   * Using this buffer is an optimization of the {@link #outZeroPaddedInt(int, int)} method.
   */
  private char[] numBuf = new char[32];

  /**
   * The number of spaces in the indent.
   */
  private int indentSpaces = 2;

  /**
   * Number of indents at the beginning of a line.
   */
  private int indentLevel = 0;

  /**
   * Constructor.
   *
   * @param aSource {@link ICharOutputStream} - output char stream
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public StrioWriter( ICharOutputStream aSource ) {
    charOut = TsNullArgumentRtException.checkNull( aSource );
  }

  // --------------------------------------------------------------------------
  // Internal operations for outputting basic primitives to a stream
  //

  /**
   * Outputs the single char.
   *
   * @param aCh char - the char to write
   * @throws StrioRtException the I/O error
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
   * Outputs the string (the sequence of chars).
   *
   * @param aString String - the string to write
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws StrioRtException the I/O error
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
   * Writes an integer padded with zeros on the left up to the specified number of digits.
   *
   * @param aValue int - the value to write
   * @param aDigits int - the number of digits (including minus '-' sign)
   * @throws StrioRtException the I/O error
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
  // IStridWriter
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
    TsIllegalArgumentRtException
        .checkTrue( aTimestamp < MIN_WRITREABLE_TIMESTAMP || aTimestamp > MAX_WRITREABLE_TIMESTAMP );
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
    TsIllegalArgumentRtException
        .checkTrue( aTimestamp < MIN_WRITREABLE_TIMESTAMP || aTimestamp > MAX_WRITREABLE_TIMESTAMP );
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
  public void writeTime( LocalDate aTime ) {
    TsNullArgumentRtException.checkNull( aTime );
    TsIllegalArgumentRtException.checkTrue( aTime.isBefore( MIN_WRITREABLE_TIME_DATE ) );
    TsIllegalArgumentRtException.checkTrue( aTime.isAfter( MAX_WRITREABLE_TIME_DATE ) );
    outZeroPaddedInt( aTime.getYear(), 4 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( aTime.getMonthValue(), 2 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( aTime.getDayOfMonth(), 2 );
  }

  @Override
  public void writeTime( LocalDateTime aTime ) {
    TsNullArgumentRtException.checkNull( aTime );
    TsIllegalArgumentRtException.checkTrue( aTime.toLocalDate().isBefore( MIN_WRITREABLE_TIME_DATE ) );
    TsIllegalArgumentRtException.checkTrue( aTime.toLocalDate().isAfter( MAX_WRITREABLE_TIME_DATE ) );
    outZeroPaddedInt( aTime.getYear(), 4 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( aTime.getMonthValue(), 2 );
    outChar( CHAR_TIMESTAMP_YMD_SEPARATOR );
    outZeroPaddedInt( aTime.getDayOfMonth(), 2 );
    outChar( CHAR_TIMESTAMP_DATETIME_SEPARATOR );
    outZeroPaddedInt( aTime.getHour(), 2 );
    outChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    outZeroPaddedInt( aTime.getMinute(), 2 );
    outChar( CHAR_TIMESTAMP_HMS_SEPARATOR );
    outZeroPaddedInt( aTime.getSecond(), 2 );
    outChar( CHAR_TIMESTAMP_MILLISEC_SEPARATOR );
    outZeroPaddedInt( aTime.getNano() / 1000, 3 );
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
