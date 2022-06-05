package org.toxsoft.core.pas.tj.impl;

import static org.toxsoft.core.pas.tj.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.EStrioSkipMode.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.errors.TsNotAllEnumsUsedRtException;

/**
 * Загрузка/сохранение сущностей {@link ITjValue} в JSON представление.
 *
 * @author hazard157
 */
class TsJsonValueStorage {

  public static TsJsonValueStorage STORAGE = new TsJsonValueStorage();

  static final String KW_NULL  = "null";  //$NON-NLS-1$
  static final String KW_TRUE  = "true";  //$NON-NLS-1$
  static final String KW_FALSE = "false"; //$NON-NLS-1$

  TsJsonValueStorage() {
    // nop
  }

  static void saveArray( IStrioWriter aSr, IList<ITjValue> aArray ) {
    aSr.writeChar( CHAR_ARRAY_BEGIN );
    // запись пустого массива
    if( aArray.isEmpty() ) {
      aSr.writeChar( CHAR_ARRAY_END );
      return;
    }
    aSr.incNewLine();
    // запись элементов через запятую
    for( int i = 0, count = aArray.size(); i < count; i++ ) {
      ITjValue value = aArray.get( i );
      STORAGE.save( aSr, value );
      if( i < count - 1 ) {
        aSr.writeSeparatorChar();
        aSr.writeEol();
      }
    }
    aSr.decNewLine();
    aSr.writeChar( CHAR_ARRAY_END );
  }

  static IListEdit<ITjValue> readArray( IStrioReader aSr ) {
    IListEdit<ITjValue> list = new ElemLinkedBundleList<>();
    if( aSr.readArrayBegin() ) {
      do {
        ITjValue value = STORAGE.load( aSr );
        list.add( value );
      } while( aSr.readArrayNext() );
    }
    return list;
  }

  static Number readNumber( IStrioReader aSr ) {
    boolean isNegative = false;
    char ch = aSr.peekChar( SKIP_NONE );
    if( ch == '-' ) { // отрицательное число?
      aSr.nextChar(); // считываем символ '-'
      isNegative = true;
    }
    // Читаем значение, думая, что оно целое.<br>
    // Если втретим точку (.), то считываем лексему и преобразуем в double
    long longVal = 0;
    int count = -1; // кол-во считанных символов
    while( true ) {
      ch = aSr.nextChar();
      ++count;
      if( StrioUtils.isAsciiDigit( ch ) ) { // очередная цифра целого числа
        if( longVal >= Long.MAX_VALUE / 10 + 9 ) { // слишком большое число!
          throw new StrioRtException( MSG_ERR_INT_OVER_LONG );
        }
        longVal *= 10;
        longVal += ch - '0';
      }
      else {
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
            return Double.valueOf( dval );
          }
          catch( Exception e ) {
            throw new StrioRtException( e, MSG_ERR_INV_NUM_FORMAT );
          }
        }
        // либо прочитали целое число, либо недопустимый символ
        if( !aSr.isDelimiterChar( ch ) && ch != CHAR_EOF ) {
          throw new StrioRtException( MSG_ERR_INV_NUM_FORMAT );
        }
        aSr.putCharBack();
        break; // почитали целое число
      }
    }
    if( isNegative ) {
      longVal = -longVal;
    }
    if( longVal >= Integer.MIN_VALUE && longVal <= Integer.MAX_VALUE ) {
      return Integer.valueOf( (int)longVal );
    }
    return Long.valueOf( longVal );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  public ITjValue load( IStrioReader aSr ) {
    char ch = aSr.peekChar( SKIP_COMMENTS );
    // объект ETjKind.OBJECT начинается с '{'
    if( ch == CHAR_SET_BEGIN ) {
      ITjObject obj = TsJsonObjectStorage.STORAGE.load( aSr );
      return TjUtils.createObject( obj );
    }
    // массив ETjKind.ARRAY начинается с '['
    if( ch == CHAR_ARRAY_BEGIN ) {
      IList<ITjValue> array = readArray( aSr );
      return TjUtils.createArray( array );
    }
    // строка ETjKind.STRING начинается с '"'
    if( ch == CHAR_QUOTE ) {
      String str = aSr.readQuotedString();
      return TjUtils.createString( str );
    }
    // число начинается с '-' или '0-9'
    if( ch == '-' || StrioUtils.isAsciiDigit( ch ) ) {
      Number number = readNumber( aSr );
      return TjUtils.createNumber( number );
    }
    // проверим, что это одно из KW_XXX
    String s = aSr.readUntilDelimiter();
    switch( s ) {
      case KW_NULL:
        return TjUtils.NULL;
      case KW_TRUE:
        return TjUtils.TRUE;
      case KW_FALSE:
        return TjUtils.FALSE;
      default:
        throw new StrioRtException( FMR_ERR_UNKNOWN_TOCKEN, s );
    }
  }

  public void save( IStrioWriter aSw, ITjValue aValue ) {
    switch( aValue.kind() ) {
      case NULL:
        aSw.writeAsIs( KW_NULL );
        break;
      case TRUE:
        aSw.writeAsIs( KW_TRUE );
        break;
      case FALSE:
        aSw.writeAsIs( KW_FALSE );
        break;
      case STRING:
        aSw.writeQuotedString( aValue.asString() );
        break;
      case ARRAY:
        saveArray( aSw, aValue.asArray() );
        break;
      case NUMBER:
        if( aValue.isInteger() ) {
          aSw.writeLong( aValue.asNumber().longValue() );
        }
        else {
          aSw.writeDouble( aValue.asNumber().doubleValue() );
        }
        break;
      case OBJECT:
        TsJsonObjectStorage.STORAGE.save( aSw, aValue.asObject() );
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

}
