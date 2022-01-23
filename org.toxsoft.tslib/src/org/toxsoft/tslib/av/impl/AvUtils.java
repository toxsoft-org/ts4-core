package org.toxsoft.tslib.av.impl;

import static org.toxsoft.tslib.av.impl.ITsResources.*;
import static org.toxsoft.tslib.bricks.strio.IStrioHardConstants.*;

import java.util.*;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.errors.AvTypeCastRtException;
import org.toxsoft.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.tslib.bricks.strid.IStridable;
import org.toxsoft.tslib.bricks.strio.IStrioReader;
import org.toxsoft.tslib.bricks.strio.StrioRtException;
import org.toxsoft.tslib.bricks.strio.chario.impl.CharInputStreamString;
import org.toxsoft.tslib.bricks.strio.impl.StrioReader;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;
import org.toxsoft.tslib.utils.valobj.TsValobjUtils;

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
  public static final IAtomicValue AV_N1          = new AvIntegerShortImpl( 1 );
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
  // static staff
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
   *
   * @param aFormatString String - форматирующая строка, может быть null
   * @param aValue {@link IAtomicValue} - значение отображаемого данного, может быть null
   * @return String - отформатированный вывод значения данного
   */
  public static String printAv( String aFormatString, IAtomicValue aValue ) {
    if( aValue == null ) {
      return NULL_VALUE_STRING;
    }
    if( aFormatString == null ) {
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
        return String.format( aFormatString, o != null ? o.toString() : TsLibUtils.EMPTY_STRING );
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
    if( aValue >= 0 && aValue <= AV_INT_ARR.length ) {
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
    if( aValue >= 0 && aValue <= AV_INT_ARR.length ) {
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
    if( Float.isInfinite( aValue ) ) {
      if( aValue > 0 ) {
        return AV_POS_INF;
      }
      return AV_NEG_INF;
    }
    if( Float.isNaN( aValue ) ) {
      return AV_NAN;
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
    if( Double.isInfinite( aValue ) ) {
      if( aValue > 0 ) {
        return AV_POS_INF;
      }
      return AV_NEG_INF;
    }
    if( Double.isNaN( aValue ) ) {
      return AV_NAN;
    }
    return new AvFloatingImpl( aValue );
  }

  /**
   * Returns atomic value of type {@link EAtomicType#TIMESTAMP}.
   * <p>
   * For timestamp 0 returns constant {@link #AV_TIME_0}.
   *
   * @param aValue long - time in millisecods from epoch start
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
   * This method may be used when keeper is not registered yet. For example, when initializing defult values of
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
   * Converts argumnet to the atomic value {@link IAtomicValue}.
   * <p>
   * The classes of argumnet is converted to the corresponding atomic types:
   * <ul>
   * <li>{@link String} as {@link EAtomicType#STRING};</li>
   * <li>{@link Boolean} as {@link EAtomicType#BOOLEAN};</li>
   * <li>{@link Byte}, {@link Short}, {@link Integer}, {@link Long} as {@link EAtomicType#INTEGER};</li>
   * <li>{@link Float}, {@link Double} as {@link EAtomicType#FLOATING};</li>
   * <li>{@link Date} as {@link EAtomicType#TIMESTAMP};</li>
   * <li>{@link IAtomicValue} is returned as is;</li>
   * <li>Any other Object is analysed by the {@link TsValobjUtils#findByClass(Class)} for being an value-object.
   * Value-objects are returned as {@link EAtomicType#VALOBJ}.</li>
   * </ul>
   * <p>
   * If argument is not recognized as stated above or argument is <code>null</code>, <code>null</code> is returned.
   *
   * @param aObj {@link Object} - object to be recocnized as {@link IAtomicValue}, may be <code>null</code>
   * @return {@link IAtomicValue} - recognized atomic value or <code>null</code>
   */
  public static IAtomicValue avFromObj( Object aObj ) {
    if( aObj == null ) {
      return null;
    }
    if( aObj instanceof IAtomicValue ) {
      return (IAtomicValue)aObj;
    }
    Class<? extends Object> clazz = aObj.getClass();
    if( clazz == String.class ) {
      return avStr( ((String)aObj) );
    }
    if( clazz == Boolean.class ) {
      return avBool( ((Boolean)aObj).booleanValue() );
    }
    if( clazz == Byte.class ) {
      return avInt( ((Byte)aObj).byteValue() );
    }
    if( clazz == Short.class ) {
      return avInt( ((Short)aObj).shortValue() );
    }
    if( clazz == Integer.class ) {
      return avInt( ((Integer)aObj).intValue() );
    }
    if( clazz == Long.class ) {
      return avInt( ((Long)aObj).longValue() );
    }
    if( clazz == Float.class ) {
      return avFloat( ((Float)aObj).floatValue() );
    }
    if( clazz == Double.class ) {
      return avFloat( ((Double)aObj).doubleValue() );
    }
    if( aObj instanceof IAtomicValue ) {
      return (IAtomicValue)aObj;
    }
    if( aObj instanceof Date ) {
      return avTimestamp( ((Date)aObj).getTime() );
    }
    String keeperId = TsValobjUtils.findKeeperIdByClass( clazz );
    if( keeperId != null ) {
      return avValobj( aObj );
    }
    return null;
  }

  /**
   * Converts the keeped text representation of the entity to the atomic value of type {@link EAtomicType#STRING}.
   *
   * @param <T> - entity class
   * @param aEntity &lt;T&gt; - the entity
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @return {@link IAtomicValue} - atomic value of the type {@link EAtomicType#STRING}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> IAtomicValue keepable2av( T aEntity, IEntityKeeper<T> aKeeper ) {
    if( aEntity == null || aKeeper == null ) {
      throw new TsNullArgumentRtException();
    }
    if( aKeeper.noneObject() != null && aEntity == aKeeper.noneObject() ) {
      return IAtomicValue.NULL;
    }
    return avStr( aKeeper.ent2str( aEntity ) );
  }

  /**
   * Restores entity from the atomic value created by {@link #keepable2av(Object, IEntityKeeper)}.
   *
   * @param <T> - entity class
   * @param aValue {@link IAtomicValue} - atomic value that holds keeped entity text representation
   * @param aKeeper {@link IEntityKeeper} - entity keeper
   * @return &lt;T&gt; - restored entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws AvTypeCastRtException value is not of type {@link EAtomicType#STRING}
   * @throws StrioRtException text invalid text representation
   */
  public static <T> T av2keepable( IAtomicValue aValue, IEntityKeeper<T> aKeeper ) {
    TsNullArgumentRtException.checkNull( aValue );
    if( aKeeper.noneObject() != null && aValue == IAtomicValue.NULL ) {
      return aKeeper.noneObject();
    }
    AvTypeCastRtException.checkTrue( aValue.atomicType() != EAtomicType.STRING, FMT_ERR_ENTITY_AV_NOT_STRING_TYPE,
        aValue.atomicType().id() );
    return aKeeper.str2ent( aValue.asString() );
  }

  // ------------------------------------------------------------------------------------
  // Private parts
  //

  /**
   * Prohibition of descendants creation.
   */
  private AvUtils() {
    // nop
  }

}
