package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.EStrioSkipMode.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;

/**
 * Reads atomic value from the {@link IStrioReader}.
 * <p>
 * This is a helper class to implement values reading for miscellaneous purposes.
 *
 * @author hazard157
 */
public class AtomicValueReaderUtils {

  /**
   * Index of first delimiter in {@link #TIMESTAMP_FMT} string.
   */
  private static final int TIMESTAMP_FIRST_DELIM_POS = 4;

  /**
   * Tokens that directly maps to atomic value constants.
   */
  private static final String[] AV_CONST_TEXTS = { //
      Boolean.toString( true ), //
      Boolean.toString( false ), //
      "NaN", //$NON-NLS-1$
      "Infinity", //$NON-NLS-1$
      EAtomicType.NONE.id(), //
  };

  /**
   * Atomic value constants corresponding to the array {@link #AV_CONST_TEXTS}.
   */
  private static final IAtomicValue[] AV_CONSTS = { // <br>
      // <br>
      AV_TRUE, //
      AV_FALSE, //
      AV_NAN, //
      AV_POS_INF, //
      IAtomicValue.NULL };

  /**
   * No subclassing.
   */
  private AtomicValueReaderUtils() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private static IAtomicValue internalReadValobj( IStrioReader aSr ) {
    aSr.ensureChar( CHAR_VALOBJ_PREFIX );
    // "@{}"
    if( aSr.peekChar( SKIP_NONE ) == CHAR_SET_BEGIN ) {
      aSr.ensureChar( CHAR_SET_BEGIN );
      aSr.ensureChar( CHAR_SET_END );
      return AV_VALOBJ_NULL;
    }
    // "keeperId{...}"
    String keeperId = aSr.readIdPath();
    String textInBraces = StrioUtils.readInterbaceContent( aSr );
    return new AvValobjImpl( keeperId, textInBraces );
  }

  /**
   * Read numerical values (INTEGER, FLOATING, TIMESTAMP).
   *
   * @param aSr {@link IStrioReader} - reader
   * @return IAtomicValue - read value
   * @throws StrioRtException format violation
   */
  private static IAtomicValue internalReadNumerical( IStrioReader aSr ) {
    boolean isNegative = false;
    char ch = aSr.peekChar( SKIP_NONE );
    if( ch == '-' ) { // "-Infinity" или -Double или -Integer
      aSr.nextChar(); // считываем символ '-'
      isNegative = true;
      if( aSr.peekChar( SKIP_NONE ) == 'I' ) { // проверка на "Infinity"
        aSr.ensureString( "Infinity" ); //$NON-NLS-1$
        if( isNegative ) {
          return AV_NEG_INF;
        }
        return AV_POS_INF;
      }
    }
    // Читаем значение, думая, что оно целое.<br>
    // Если второй симвох 'x' или 'X', то считываем лексему пытаемся прочесть как 16-ричное число.<br>
    // Если встретим точку (.), то считываем лексему и преобразуем в double.<br>
    // Если строка начинает совпадать с TIMESTAMP_FMT, то считаем как метку времени.
    long longVal = 0;
    int count = -1; // кол-во считанных символов
    while( true ) {
      ch = aSr.nextChar();
      ++count;
      if( StrioUtils.isAsciiDigit( ch ) ) { // очередная цифра целого числа
        if( longVal >= Long.MAX_VALUE / 10 + 9 ) { // слишком большое число!
          throw new StrioRtException( MSG_ERR_INT_VALUE_OVER_MAX_LONG );
        }
        longVal *= 10;
        longVal += ch - '0';
      }
      else {
        // проверим, может это метка времени
        if( ch == CHAR_TIMESTAMP_YMD_SEPARATOR && count == TIMESTAMP_FIRST_DELIM_POS ) {
          StrioRtException.checkTrue( isNegative, MSG_ERR_INV_NUMERICAL_AV_TEXT_FORMAT );
          for( int i = 0; i <= count; i++ ) {
            aSr.putCharBack();
          }
          return avTimestamp( aSr.readTimestamp() );
        }
        // второй символ x или X?
        if( count == 1 && (ch == 'x' || ch == 'X') ) {
          if( longVal != 0 ) { // первый символ 16-ричного числа должен был быть 0
            throw new StrioRtException( MSG_ERR_INV_HEX_NUL_AV_TEXT_FORMAT );
          }
          // "0x" считали, остальное отработаем как лексему 16-ричного числа
          String s = aSr.readUntilDelimiter();
          try {
            long hexVal = Long.parseUnsignedLong( s, 16 );
            if( isNegative ) {
              hexVal = -hexVal;
            }
            return avInt( hexVal );
          }
          catch( Exception e ) {
            throw new StrioRtException( e, MSG_ERR_INV_HEX_NUL_AV_TEXT_FORMAT );
          }
        }
        // а может, это число с плавающей точкой?
        if( ch == '.' ) {
          // вернем считанные символы. Буфер не переполнится, поскольку count небольшое),
          // из-за проверки выше - на выход за пределы Long.MAX_VALUE
          for( int i = 0; i <= count; i++ ) {
            aSr.putCharBack();
          }
          String s = aSr.readUntilDelimiter();
          try {
            double dval = Double.parseDouble( s );
            if( isNegative ) {
              dval = -dval;
            }
            return avFloat( dval );
          }
          catch( Exception e ) {
            throw new StrioRtException( e, MSG_ERR_INV_NUMERICAL_AV_TEXT_FORMAT );
          }
        }
        // либо прочитали целое число, либо недопустимый символ
        if( !aSr.isDelimiterChar( ch ) && ch != CHAR_EOF ) {
          throw new StrioRtException( MSG_ERR_INV_NUMERICAL_AV_TEXT_FORMAT );
        }
        aSr.putCharBack();
        break; // почитали целое число
      }
    }
    if( isNegative ) {
      longVal = -longVal;
    }
    return avInt( longVal );
  }

  private static IAtomicValue internalReadInterpretedAvTypes( IStrioReader aSr ) {
    char ch = aSr.peekChar();
    // STRING
    if( ch == CHAR_QUOTE ) {
      return AvUtils.avStr( aSr.readQuotedString() );
    }
    // INTEGER, FLOATING, TIMESTAMP
    if( ch == '-' || StrioUtils.isAsciiDigit( ch ) ) {
      return internalReadNumerical( aSr );
    }
    // VALOBJ
    if( ch == CHAR_VALOBJ_PREFIX ) {
      return internalReadValobj( aSr );
    }
    return null;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Reads atomic value from current position until delimiter {@link IStrioReader#getDelimiterChars()},
   * <p>
   * If there is a format violation of atomic value representation, then an exception is thrown.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return {@link IAtomicValue} - read atomic value
   * @throws StrioRtException format violation of atomic value representation
   */
  public static IAtomicValue readAtomicValueOrException( IStrioReader aSr ) {
    IAtomicValue av = internalReadInterpretedAvTypes( aSr );
    if( av != null ) {
      return av;
    }
    // BOOLEAN and some constants
    String token = aSr.readUntilDelimiter();
    for( int i = 0; i < AV_CONST_TEXTS.length; i++ ) {
      if( token.equals( AV_CONST_TEXTS[i] ) ) {
        return AV_CONSTS[i];
      }
    }
    if( token.length() > 40 ) {
      token = token.substring( 0, 40 ) + " [[[...]]]"; //$NON-NLS-1$
    }
    throw new StrioRtException( FMT_ERR_INV_AV_TEXT, token );
  }

  /**
   * Reads atomic value from current position until delimiter {@link IStrioReader#getDelimiterChars()},
   * <p>
   * If there is a format violation of atomic value representation, then whole string from current position until
   * delimiter is interpreted as the string and atomic value of type {@link EAtomicType#STRING STRING} is returned.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return {@link IAtomicValue} - read atomic value
   */
  public static IAtomicValue readAtomicValueOrAsString( IStrioReader aSr ) {
    IAtomicValue av = internalReadInterpretedAvTypes( aSr );
    if( av != null ) {
      return av;
    }
    // BOOLEAN and some constants
    String token = aSr.readUntilDelimiter();
    for( int i = 0; i < AV_CONST_TEXTS.length; i++ ) {
      if( token.equals( AV_CONST_TEXTS[i] ) ) {
        return AV_CONSTS[i];
      }
    }
    return AvUtils.avStr( token );
  }

}
