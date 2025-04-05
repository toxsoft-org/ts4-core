package org.toxsoft.core.tslib.av.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * Helper methods and constants to work with the atomic values.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public class AvUtils {

  // EAtomicType identifiers
  public static final String DDID_NONE      = "None";     //$NON-NLS-1$
  public static final String DDID_BOOLEAN   = "Boolean";  //$NON-NLS-1$
  public static final String DDID_INTEGER   = "Integer";  //$NON-NLS-1$
  public static final String DDID_FLOATING  = "Float";    //$NON-NLS-1$
  public static final String DDID_STRING    = "String";   //$NON-NLS-1$
  public static final String DDID_TIMESTAMP = "Timestamp";//$NON-NLS-1$
  public static final String DDID_VALOBJ    = "Valobj";   //$NON-NLS-1$

  // constant atomic values
  public static final IAtomicValue AV_FALSE       = new AvBooleanFalseImpl();
  public static final IAtomicValue AV_TRUE        = new AvBooleanTrueImpl();
  public static final IAtomicValue AV_0           = new AvIntegerShortImpl( 0 );
  public static final IAtomicValue AV_1           = new AvIntegerShortImpl( 1 );
  public static final IAtomicValue AV_N1          = new AvIntegerShortImpl( -1 );
  public static final IAtomicValue AV_F_0         = new AvFloatingImpl( 0.0 );
  public static final IAtomicValue AV_F_1         = new AvFloatingImpl( 1.0 );
  public static final IAtomicValue AV_NAN         = new AvFloatingImpl( Double.NaN );
  public static final IAtomicValue AV_POS_INF     = new AvFloatingImpl( Double.POSITIVE_INFINITY );
  public static final IAtomicValue AV_NEG_INF     = new AvFloatingImpl( Double.NEGATIVE_INFINITY );
  public static final IAtomicValue AV_TIME_0      = new AvTimestampImpl( 0 );
  public static final IAtomicValue AV_TIME_START  = new AvTimestampImpl( System.currentTimeMillis() );
  public static final IAtomicValue AV_STR_EMPTY   = new AvStringImpl( TsLibUtils.EMPTY_STRING );
  public static final IAtomicValue AV_STR_NONE_ID = new AvStringImpl( IStridable.NONE_ID );
  public static final IAtomicValue AV_VALOBJ_NULL = AvValobjNullImpl.VALOBJ_NULL;

  // ------------------------------------------------------------------------------------
  // static stuff
  //

  /**
   * Array of preliminary created instances of integer atomic values from 0 to array size.
   */
  static final IAtomicValue AV_INT_ARR[] = new IAtomicValue[128];

  static {
    AV_INT_ARR[0] = AV_0;
    AV_INT_ARR[1] = AV_1;
    for( int i = 2; i < AV_INT_ARR.length; i++ ) {
      AV_INT_ARR[i] = new AvIntegerShortImpl( i );
    }
  }

  /**
   * External comparator using {@link IAtomicValue#compareTo(IAtomicValue)} comparison.
   */
  public static final Comparator<IAtomicValue> DEFAULT_AV_COMPARATOR = ( aO1, aO2 ) -> {
    if( aO1 == null || aO2 == null ) {
      throw new NullPointerException();
    }
    if( aO1 == aO2 ) {
      return 0;
    }
    return aO1.compareTo( aO2 );
  };

  // ------------------------------------------------------------------------------------
  // formatted output
  //

  // TRANSLATE

  /**
   * Output string of {@link #printAv(String, IAtomicValue)} when argument aValue = <code>null</code>.
   */
  public static final String NULL_VALUE_STRING = "<null>"; //$NON-NLS-1$

  // processing format string "%Б[FalseText|TrueText]"
  private static String processTsBoolFormat( String aFmt, boolean aValue ) {
    int len = aFmt.length();
    int index = aFmt.indexOf( '%' );
    if( len < 6 || index < 0 || (len - index) < 6 ) {
      return null;
    }
    IStrioReader sr = new StrioReader( new CharInputStreamString( aFmt, index ) );
    StringBuilder sb = new StringBuilder( aFmt.substring( 0, index ) );
    // идем до строки "%Б"
    while( true ) {
      char ch = sr.nextChar();
      if( ch == CHAR_EOF ) {
        return null;
      }
      if( ch != '%' ) {
        sb.append( ch );
        continue;
      }
      ch = sr.nextChar();
      if( ch != 'Б' ) {
        if( ch != '%' ) { // пропустим второй их двух подряд идущих '%'
          sb.append( ch );
        }
        continue;
      }
      // тут мы поймали "%Б", завершим цикл и обработаем дальше
      break;
    }
    if( sr.nextChar() != '[' ) {
      return null;
    }
    String falseText = sr.readUntilChars( "|" ); //$NON-NLS-1$
    if( sr.nextChar() != '|' ) {
      return null;
    }
    String trueText = sr.readUntilChars( "]" ); //$NON-NLS-1$
    if( sr.nextChar() != ']' ) {
      return null;
    }
    if( aValue ) {
      sb.append( trueText );
    }
    else {
      sb.append( falseText );
    }
    sb.append( sr.readUntilChars( Character.toString( CHAR_EOF ) ) );
    return sb.toString();
  }

  /**
   * Осуществляет вывод в строку значения данного с использованием форматированния по шаблону aFormatString.
   * <p>
   * Форматированный вывод осуществляется по правилам метода {@link String#format(String, Object...)}. При этом, в
   * зависимости от типа данного, в качестве аргумента используется разные Java-типы, что требует соответствующего
   * спецификатора форматирования. В частности для следующих типов данных при форматированом выводе передаются следующие
   * аргументы:
   * <ul>
   * <li>{@link EAtomicType#NONE} - строка {@link EAtomicType#NONE}<code><b>.id()</b></code>;</li>
   * <li>{@link EAtomicType#BOOLEAN} - {@link Boolean} (спецификатор форматирования "%b" и "%Б (см. ниже)");</li>
   * <li>{@link EAtomicType#INTEGER} - {@link Long} (спецификаторы форматирования "%d", "%o", "%x", "%X");</li>
   * <li>{@link EAtomicType#FLOATING} - {@link Double} (спецификаторы форматирования "%e", "%E", "%f", "%g", "%G", "%a",
   * "%A");</li>
   * <li>{@link EAtomicType#TIMESTAMP} - {@link Long} (спецификаторы форматирования "%t". "%T");</li>
   * <li>{@link EAtomicType#STRING} - {@link String} (спецификатор форматирования "%s");</li>
   * <li>{@link EAtomicType#VALOBJ} - {@link IAtomicValue#asValobj() asValobj().toString()} (спецификатор форматирования
   * "%s").</li>
   * </ul>
   * При форматировании передается только один аргумент, поэтому, если нужно более одного раза использовать аргумент,
   * составьте строку форматирования aFormatString с использованием индекса аргумента "%1$". Например, "%1$tF %1$tT" для
   * аргумента типа {@link EAtomicType#TIMESTAMP} выведет строку вида "2006-11-31 23:59:59".
   * <p>
   * В добавок к стандартным спецификаторам, определен специальный спецификатор для булевого значения формата <b>
   * "%Б[Текст ЛОЖЬ| Текст ИСТИНА]"</b>. Этот псецификатор (с русской заглавной буквой <b>Б</b>) применим только к
   * булевому значению (т.е. для типа {@link EAtomicType#BOOLEAN}). В зависимости от значения, отображается одна из
   * строк <b>"Текст ЛОЖЬ"</b> или <b>"Текс ИСТИНА"</b>, находящейся в квадратных скобках, и разделенные вертикальной
   * чертой. Текст для значения false не может содержать в себе символ вертикальной черты <b>'|'</b>, а текст для
   * значения true не может содержать символ правой квадратной скобки <b>']'</b>. Таким образом, спецификатор
   * <b>"%Б"</b> позволяет вместо стандартных "false"/"true" отображать практически любой текст для булевого значения.
   * <p>
   * Для значения null возвращает строку {@link #NULL_VALUE_STRING}. Для значения {@link IAtomicValue#NULL} возвращает
   * строку {@link IAtomicValue#NULL}.asString().
   * <p>
   * Если aFormatString равен null, то возвращает стандартное представление методом {@link IAtomicValue#asString()}. Во
   * всех остальных случаях возвращается строка, отформатировання методом {@link AvUtils#printAv(String, IAtomicValue)}.
   * <p>
   * TODO for {@link EAtomicType#VALOBJ} uses {@link Object#toString()} but for {@link IStridable}
   * {@link StridUtils#printf(String, IStridable)}
   *
   * @param aFormatString String - форматирующая строка, может быть null
   * @param aValue {@link IAtomicValue} - значение отображаемого данного, может быть null
   * @return String - отформатированный вывод значения данного
   */
  public static String printAv( String aFormatString, IAtomicValue aValue ) {
    if( aValue == null ) {
      return NULL_VALUE_STRING;
    }
    if( aFormatString == null && aValue.atomicType() != EAtomicType.VALOBJ ) {
      return aValue.asString();
    }
    if( aValue == IAtomicValue.NULL ) {
      return IAtomicValue.NULL.asString();
    }
    switch( aValue.atomicType() ) {
      case NONE:
        return String.format( aFormatString, IAtomicValue.NULL.asString() );
      case BOOLEAN:
        String result;
        try {
          result = processTsBoolFormat( aFormatString, aValue.asBool() );
        }
        catch( @SuppressWarnings( "unused" ) Exception e ) {
          throw new UnknownFormatConversionException( "%Б" ); //$NON-NLS-1$
        }
        if( result != null ) {
          return result;
        }
        return String.format( aFormatString, Boolean.valueOf( aValue.asBool() ) );
      case INTEGER:
        return String.format( aFormatString, Long.valueOf( aValue.asLong() ) );
      case FLOATING:
        return String.format( aFormatString, Double.valueOf( aValue.asDouble() ) );
      case TIMESTAMP:
        return String.format( aFormatString, Long.valueOf( aValue.asLong() ) );
      case STRING:
        return String.format( aFormatString, aValue.asString() );
      case VALOBJ: {
        Object o = aValue.asValobj();
        if( o instanceof IStridable ss ) {
          String ssFmt = aFormatString != null ? aFormatString : StridUtils.FORMAT_NAME;
          return StridUtils.printf( ssFmt, ss );
        }
        String objStr = o != null ? o.toString() : TsLibUtils.EMPTY_STRING;
        if( aFormatString == null ) {
          return objStr;
        }
        return String.format( aFormatString, objStr );
      }
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // static constructors

  /**
   * Returns atomic value of type {@link EAtomicType#BOOLEAN}.
   *
   * @param aValue boolean - boolean value
   * @return {@link IAtomicValue } - always returns one of the constants {@link IAvHardConstants#AV_FALSE} or
   *         {@link IAvHardConstants#AV_TRUE}
   */
  public static final IAtomicValue avBool( boolean aValue ) {
    if( aValue ) {
      return AV_TRUE;
    }
    return AV_FALSE;
  }

  /**
   * Returns atomic value of type {@link EAtomicType#INTEGER}.
   * <p>
   * For values in range {@link #AV_INT_ARR} returns values from array.
   *
   * @param aValue int - integer value
   * @return {@link IAtomicValue} - atomic value holding argument value
   */
  public static final IAtomicValue avInt( int aValue ) {
    if( aValue >= 0 && aValue < AV_INT_ARR.length ) {
      return AV_INT_ARR[aValue];
    }
    if( aValue >= Short.MIN_VALUE && aValue <= Short.MAX_VALUE ) {
      return new AvIntegerShortImpl( aValue );
    }
    return new AvIntegerIntImpl( aValue );
  }

  /**
   * Returns atomic value of type {@link EAtomicType#INTEGER}.
   * <p>
   * For values in range {@link #AV_INT_ARR} returns values from array.
   *
   * @param aValue long - long integer value
   * @return {@link IAtomicValue} - atomic value holding argument value
   */
  public static final IAtomicValue avInt( long aValue ) {
    if( aValue >= 0 && aValue < AV_INT_ARR.length ) {
      return AV_INT_ARR[(int)aValue];
    }
    if( aValue >= Short.MIN_VALUE && aValue <= Short.MAX_VALUE ) {
      return new AvIntegerShortImpl( aValue );
    }
    if( aValue >= Integer.MIN_VALUE && aValue <= Integer.MAX_VALUE ) {
      return new AvIntegerIntImpl( aValue );
    }
    return new AvIntegerLongImpl( aValue );
  }

  /**
   * Returns atomic value of type {@link EAtomicType#FLOATING}.
   * <p>
   * For values 0.0, 1.0,, {@link Float#NEGATIVE_INFINITY}, {@link Float#POSITIVE_INFINITY}, {@link Float#NaN} returns
   * respective <code>AV_XXX</code>.
   *
   * @param aValue float - floating point value
   * @return {@link IAtomicValue} - atomic value holding argument value
   */
  public static final IAtomicValue avFloat( float aValue ) {
    if( Float.compare( aValue, 0.0f ) == 0 ) {
      return AV_F_0;
    }
    if( Float.compare( aValue, 1.0f ) == 0 ) {
      return AV_F_1;
    }
    if( Float.isNaN( aValue ) ) {
      return AV_NAN;
    }
    if( Float.isInfinite( aValue ) ) {
      if( aValue > 0 ) {
        return AV_POS_INF;
      }
      return AV_NEG_INF;
    }
    return new AvFloatingImpl( aValue );
  }

  /**
   * Returns atomic value of type {@link EAtomicType#FLOATING}.
   * <p>
   * For values 0.0, 1.0,, {@link Double#NEGATIVE_INFINITY}, {@link Double#POSITIVE_INFINITY}, {@link Double#NaN}
   * returns respective <code>AV_XXX</code>.
   *
   * @param aValue double - double precision floating point value
   * @return {@link IAtomicValue} - atomic value holding argument value
   */
  public static final IAtomicValue avFloat( double aValue ) {
    if( Double.compare( aValue, 0.0 ) == 0 ) {
      return AV_F_0;
    }
    if( Double.compare( aValue, 1.0 ) == 0 ) {
      return AV_F_1;
    }
    if( Double.isNaN( aValue ) ) {
      return AV_NAN;
    }
    if( Double.isInfinite( aValue ) ) {
      if( aValue > 0 ) {
        return AV_POS_INF;
      }
      return AV_NEG_INF;
    }
    return new AvFloatingImpl( aValue );
  }

  /**
   * Returns atomic value of type {@link EAtomicType#TIMESTAMP}.
   * <p>
   * For timestamp 0 returns constant {@link #AV_TIME_0}.
   *
   * @param aValue long - time in milliseconds from epoch start
   * @return {@link IAtomicValue} - atomic value holding argument value
   */
  public static final IAtomicValue avTimestamp( long aValue ) {
    if( aValue == 0 ) {
      return AV_TIME_0;
    }
    return new AvTimestampImpl( aValue );
  }

  /**
   * Returns atomic value of type {@link EAtomicType#STRING}.
   * <p>
   * For empty string returns constant {@link #AV_STR_EMPTY}.
   *
   * @param aValue String - string
   * @return {@link IAtomicValue} - atomic value holding argument value
   */
  public static final IAtomicValue avStr( String aValue ) {
    if( aValue == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aValue.isEmpty() ) {
      return AV_STR_EMPTY;
    }
    return new AvStringImpl( aValue );
  }

  /**
   * Returns atomic value of type {@link EAtomicType#VALOBJ}.
   * <p>
   * For <code>null</code> returns constant {@link #AV_VALOBJ_NULL}.
   *
   * @param aValobj Object - value-object
   * @return {@link IAtomicValue} - atomic value holding argument value
   * @throws TsItemNotFoundRtException not an value-object
   */
  public static final IAtomicValue avValobj( Object aValobj ) {
    if( aValobj == null ) {
      return AV_VALOBJ_NULL;
    }
    return new AvValobjImpl( aValobj );
  }

  /**
   * Creates {@link IAtomicValue} of {@link EAtomicType#VALOBJ}.
   * <p>
   * This method may be used when keeper is not registered yet. For example, when initializing default values of
   * <code>static final</code> constants before keeper registration.
   *
   * @param <T> - type of value-object
   * @param aValobj &lt;T&gt; - value-object, must not be <code>null</code>
   * @param aKeeper {@link IEntityKeeper}&lt;T&gt; - the keeper
   * @param aKeeperId String - the keeper ID
   * @return {@link IAtomicValue} - atomic value holding argument value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> IAtomicValue avValobj( T aValobj, IEntityKeeper<T> aKeeper, String aKeeperId ) {
    TsNullArgumentRtException.checkNulls( aValobj, aKeeper, aKeeperId );
    String textInBrace = aKeeper.ent2str( aValobj );
    if( !aKeeper.isEnclosed() ) {
      textInBrace = '{' + textInBrace + '}';
    }
    return new AvValobjImpl( aKeeperId, textInBrace );
  }

  // ------------------------------------------------------------------------------------
  // helper staff
  //

  /**
   * Converts argument to the atomic value {@link IAtomicValue}.
   * <p>
   * The classes of argument is converted to the corresponding atomic types:
   * <ul>
   * <li>{@link String} as {@link EAtomicType#STRING};</li>
   * <li>{@link Boolean} as {@link EAtomicType#BOOLEAN};</li>
   * <li>{@link Byte}, {@link Short}, {@link Integer}, {@link Long} as {@link EAtomicType#INTEGER};</li>
   * <li>{@link Float}, {@link Double} as {@link EAtomicType#FLOATING};</li>
   * <li>{@link Date} as {@link EAtomicType#TIMESTAMP};</li>
   * <li>{@link IAtomicValue} is returned as is;</li>
   * <li>Any other Object is analyzed by the {@link TsValobjUtils#findByClass(Class)} for being an value-object.
   * Value-objects are returned as {@link EAtomicType#VALOBJ}.</li>
   * </ul>
   * <p>
   * If argument is not recognized as stated above or argument is <code>null</code>, <code>null</code> is returned.
   *
   * @param aObj {@link Object} - object to be recognized as {@link IAtomicValue}, may be <code>null</code>
   * @return {@link IAtomicValue} - recognized atomic value or <code>null</code>
   */
  public static IAtomicValue avFromObj( Object aObj ) {
    return switch( aObj ) {
      case null -> null;
      case IAtomicValue av -> av;
      case String s -> avStr( s );
      case Boolean b -> avBool( b.booleanValue() );
      case Byte n -> avInt( n.intValue() );
      case Short n -> avInt( n.intValue() );
      case Integer n -> avInt( n.intValue() );
      case Long n -> avInt( n.longValue() );
      case Float n -> avFloat( n.floatValue() );
      case Double n -> avFloat( n.doubleValue() );
      case Date d -> avTimestamp( d.getTime() );
      default -> {
        String keeperId = TsValobjUtils.findKeeperIdByClass( aObj.getClass() );
        if( keeperId != null ) {
          yield avValobj( aObj );
        }
        yield null;
      }
    };
  }

  /**
   * Determines the data type of the value returned by {@link #avFromObj(Object)}.
   * <p>
   * In case when method {@link #avFromObj(Object)} should return <code>null</code>, this method returns
   * {@link EAtomicType#NONE}.
   *
   * @param aObj {@link Object} - object to be recognized as {@link IAtomicValue}, may be <code>null</code>
   * @return {@link EAtomicType} - atomic type of the recognized atomic value
   */
  public static EAtomicType atFromObj( Object aObj ) {
    IAtomicValue av = avFromObj( aObj );
    return av != null ? av.atomicType() : EAtomicType.NONE;
  }

  // ------------------------------------------------------------------------------------
  // Private parts
  //

  /**
   * No subclasses.
   */
  private AvUtils() {
    // nop
  }

}
