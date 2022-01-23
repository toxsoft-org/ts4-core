package org.toxsoft.tslib.av.impl;

import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.impl.ITsResources.*;
import static org.toxsoft.tslib.bricks.strio.EStrioSkipMode.*;
import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.keeper.AbstractEntityKeeper;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strio.*;
import org.toxsoft.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.tslib.utils.errors.TsNotAllEnumsUsedRtException;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

/**
 * {@link IAtomicValue} keeper.
 *
 * @author hazard157
 */
public class AtomicValueKeeper
    extends AbstractEntityKeeper<IAtomicValue> {

  /**
   * Value-object registration identifier for {@link TsValobjUtils}.
   */
  public static final String KEEPER_ID = "av"; //$NON-NLS-1$

  /**
   * Keeper singleton.
   */
  public static final IEntityKeeper<IAtomicValue> KEEPER = new AtomicValueKeeper();

  private AtomicValueKeeper() {
    super( IAtomicValue.class, EEncloseMode.NOT_IN_PARENTHESES, null );
  }

  @Override
  protected void doWrite( IStrioWriter aSw, IAtomicValue aEntity ) {
    switch( aEntity.atomicType() ) {
      case NONE:
        aSw.writeAsIs( EAtomicType.NONE.id() );
        break;
      case BOOLEAN:
        aSw.writeBoolean( aEntity.asBool() );
        break;
      case TIMESTAMP:
        aSw.writeTimestamp( aEntity.asLong() );
        break;
      case INTEGER:
        aSw.writeLong( aEntity.asLong() );
        break;
      case FLOATING:
        aSw.writeDouble( aEntity.asDouble() );
        break;
      case STRING:
        aSw.writeQuotedString( aEntity.asString() );
        break;
      case VALOBJ: {
        if( aEntity != AV_VALOBJ_NULL ) {
          AvValobjImpl avImpl = (AvValobjImpl)aEntity;
          aSw.writeAsIs( avImpl.getKtor() );
        }
        else {
          aSw.writeAsIs( AvValobjNullImpl.KTOR );
        }
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  @Override
  protected IAtomicValue doRead( IStrioReader aSr ) {
    char ch = aSr.peekChar();
    // STRING
    if( ch == CHAR_QUOTE ) {
      String s = aSr.readQuotedString();
      if( s.isEmpty() ) {
        return AV_STR_EMPTY;
      }
      return avStr( s );
    }
    // INTEGER, FLOATING, TIMESTAMP
    if( ch == '-' || StrioUtils.isAsciiDigit( ch ) ) {
      return internalReadNumerical( aSr );
    }
    // VALOBJ
    if( ch == CHAR_VALOBJ_PREFIX ) {
      return internalReadValobj( aSr );
    }
    // BOOLEAN and some constants
    String lastToken = aSr.readUntilDelimiter();
    for( int i = 0; i < AV_CONST_TEXTS.length; i++ ) {
      if( lastToken.equals( AV_CONST_TEXTS[i] ) ) {
        return AV_CONSTS[i];
      }
    }
    if( lastToken.length() > 100 ) {
      lastToken = lastToken.substring( 0, 100 );
    }
    throw new StrioRtException( FMT_ERR_INV_AV_TEXT, lastToken );
  }

  // ------------------------------------------------------------------------------------
  // Internal implementation
  //

  /**
   * Index of first delimiter in {@link #TIMESTAMP_FMT} string.
   */
  private static final int TIMESTAMP_FIRST_DELIM_POS = 4;

  /**
   * Tockens that directly maps to atomic value constants.
   */
  private static final String[] AV_CONST_TEXTS = new String[] { //
      Boolean.toString( true ), //
      Boolean.toString( false ), //
      "NaN", //$NON-NLS-1$
      "Infinity", //$NON-NLS-1$
      EAtomicType.NONE.id(), //
  };

  /**
   * Atomic value constants corresponding to the array {@link #AV_CONST_TEXTS}.
   */
  private static final IAtomicValue[] AV_CONSTS = new IAtomicValue[] { // <br>
      // <br>
      AV_TRUE, //
      AV_FALSE, //
      AV_NAN, //
      AV_POS_INF, //
      IAtomicValue.NULL };

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

}
