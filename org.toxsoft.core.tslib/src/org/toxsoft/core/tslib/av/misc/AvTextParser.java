package org.toxsoft.core.tslib.av.misc;

import static java.util.Calendar.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.misc.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;

import java.util.Calendar;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.bricks.keeper.IEntityKeeper;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesList;
import org.toxsoft.core.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.core.tslib.bricks.strio.IStrioReader;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.CharInputStreamString;
import org.toxsoft.core.tslib.bricks.strio.impl.StrioReader;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.core.tslib.coll.basis.ITsCollection;
import org.toxsoft.core.tslib.coll.primtypes.IStringList;
import org.toxsoft.core.tslib.coll.primtypes.IStringListEdit;
import org.toxsoft.core.tslib.coll.primtypes.impl.StringArrayList;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.valobj.TsValobjUtils;

/**
 * Parses human readable text as {@link IAtomicValue}.
 * <p>
 * TODO how it works
 *
 * @author hazard157
 */
public class AvTextParser {

  /**
   * Список разрешенных по умолчанию типов.
   */
  public static final IStridablesList<EAtomicType> DEFAULT_ALLOWED_TYPES = new StridablesList<>( EAtomicType.INTEGER,
      EAtomicType.BOOLEAN, EAtomicType.FLOATING, EAtomicType.TIMESTAMP, EAtomicType.STRING );

  /**
   * Default names for <code>true</code>.
   */
  public static final IStringList DEFAULT_TRUE_NAMES = new StringArrayList( "true", "yes", "1" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

  /**
   * Default names for <code>false</code>.
   */
  public static final IStringList DEFAULT_FALSE_NAMES = new StringArrayList( "false", "no", "0" ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

  /**
   * Календарь для манипуляции с метками времени.
   */
  private final Calendar calendar = Calendar.getInstance();

  private final IStridablesListEdit<EAtomicType> allowedTypes = new StridablesList<>( DEFAULT_ALLOWED_TYPES );

  private final IStringListEdit bTrueNames  = new StringArrayList( DEFAULT_TRUE_NAMES );
  private final IStringListEdit bFalseNames = new StringArrayList( DEFAULT_FALSE_NAMES );

  private boolean interpretBlankTextAsNull = true;
  private boolean interpretErrorAsNull     = false;

  /**
   * Образенный текст, который был передан в качестве аргумента методу {@link #validate(String)}.
   * <p>
   * {@link #validate(String)} вызывается первым в методе {@link #parse(String)}.
   */
  private String argumentText = TsLibUtils.EMPTY_STRING;

  /**
   * Разобранное атомарное значение.
   * <p>
   * Разбор текст происходит в методе {@link #validate(String)}, поскольку проверка и разбор происходит в один прием.
   */
  private IAtomicValue parsedValue = IAtomicValue.NULL;

  /**
   * Пустой конструктор.
   */
  public AvTextParser() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  private ValidationResult cantParseAsType( EAtomicType aAsType ) {
    return ValidationResult.error( FMT_CANT_PARSE_AS_TYPE, argumentText, aAsType.nmName() );
  }

  private ValidationResult parseAsNone( String aText ) {
    if( parseAsInt().isOk() ) {
      return ValidationResult.SUCCESS;
    }
    if( parseAsBool().isOk() ) {
      return ValidationResult.SUCCESS;
    }
    if( parseAsFloat().isOk() ) {
      return ValidationResult.SUCCESS;
    }
    if( parseAsTimetsamp().isOk() ) {
      return ValidationResult.SUCCESS;
    }
    return parseAsString( aText );
  }

  private ValidationResult parseAsBool() {
    for( String s : bTrueNames ) {
      if( s.equalsIgnoreCase( argumentText ) ) {
        parsedValue = AV_TRUE;
        return ValidationResult.SUCCESS;
      }
    }
    for( String s : bFalseNames ) {
      if( s.equalsIgnoreCase( argumentText ) ) {
        parsedValue = AV_FALSE;
        return ValidationResult.SUCCESS;
      }
    }
    return cantParseAsType( EAtomicType.BOOLEAN );
  }

  private ValidationResult parseAsInt() {
    long value;
    try {
      value = Long.parseLong( argumentText, 10 );
    }
    catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
      // проверим как hex число
    }
    try {
      value = Long.parseLong( argumentText, 10 );
    }
    catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
      return cantParseAsType( EAtomicType.INTEGER );
    }
    parsedValue = avInt( value );
    return ValidationResult.SUCCESS;
  }

  private ValidationResult parseAsFloat() {
    double value;
    try {
      value = Double.parseDouble( argumentText );
    }
    catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
      return cantParseAsType( EAtomicType.FLOATING );
    }
    parsedValue = avFloat( value );
    return ValidationResult.SUCCESS;
  }

  private ValidationResult parseAsString( String aText ) {
    parsedValue = avStr( aText );
    return ValidationResult.SUCCESS;
  }

  private ValidationResult parseAsTimetsamp() {
    // попробуем прочитать в формате IStridReader#readTimestamp()
    IStrioReader sr = new StrioReader( new CharInputStreamString( argumentText ) );
    try {
      long timestamp = sr.readTimestamp();
      parsedValue = avTimestamp( timestamp );
      return ValidationResult.SUCCESS;
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      // не получилось как IStridReader#readTimestamp()
    }
    // считаем как "DD.MM.YYYY [HH:MM:SS]"
    String[] ss = argumentText.split( "\\s" ); // разделим по пробелам //$NON-NLS-1$
    // создадим список токенов, игнорируя разделители
    IStringListEdit sl = new StringArrayList();
    boolean wasDate = false;
    for( String s : ss ) {
      if( s.length() == 10 ) {
        if( wasDate ) {
          return cantParseAsType( EAtomicType.TIMESTAMP );
        }
        sl.add( s );
        wasDate = true;
      }
      if( s.length() == 8 ) {
        sl.add( s );
      }
    }
    // должно быть один или два токена
    if( sl.size() < 1 || sl.size() > 2 ) {
      return cantParseAsType( EAtomicType.TIMESTAMP );
    }
    calendar.setTimeInMillis( 0 );
    // находим дату и время
    for( String s : sl ) {
      char[] chars = s.toCharArray();
      switch( s.length() ) {
        case 10: // дата?
          if( chars[2] != '.' || chars[5] != '.' ) {
            return cantParseAsType( EAtomicType.TIMESTAMP );
          }
          if( !isAsciiDigit( chars[0] ) || !isAsciiDigit( chars[1] ) || !isAsciiDigit( chars[3] )
              || !isAsciiDigit( chars[4] ) || !isAsciiDigit( chars[6] ) || !isAsciiDigit( chars[7] )
              || !isAsciiDigit( chars[8] ) || !isAsciiDigit( chars[9] ) ) {
            return cantParseAsType( EAtomicType.TIMESTAMP );
          }
          calendar.set( DAY_OF_MONTH, Integer.parseInt( new String( chars, 0, 2 ) ) );
          calendar.set( MONTH, Integer.parseInt( new String( chars, 3, 2 ) ) - 1 );
          calendar.set( YEAR, Integer.parseInt( new String( chars, 6, 4 ) ) );
          break;
        case 8: // время дня?
          if( chars[2] != ':' || chars[5] != ':' ) {
            return cantParseAsType( EAtomicType.TIMESTAMP );
          }
          if( !isAsciiDigit( chars[0] ) || !isAsciiDigit( chars[1] ) || !isAsciiDigit( chars[3] )
              || !isAsciiDigit( chars[4] ) || !isAsciiDigit( chars[6] ) || !isAsciiDigit( chars[7] ) ) {
            return cantParseAsType( EAtomicType.TIMESTAMP );
          }
          calendar.set( HOUR_OF_DAY, Integer.parseInt( new String( chars, 0, 2 ) ) );
          calendar.set( MINUTE, Integer.parseInt( new String( chars, 3, 2 ) ) );
          calendar.set( SECOND, Integer.parseInt( new String( chars, 6, 2 ) ) );
          break;
        default:
          return cantParseAsType( EAtomicType.TIMESTAMP );
      }
    }
    parsedValue = avTimestamp( calendar.getTimeInMillis() );
    return ValidationResult.SUCCESS;
  }

  private ValidationResult parseAsValobj() {
    try {
      IStrioReader sr = new StrioReader( new CharInputStreamString( argumentText ) );
      sr.ensureChar( CHAR_VALOBJ_PREFIX );
      String keeperId = sr.readIdPath();
      IEntityKeeper<?> keeper = TsValobjUtils.getKeeperById( keeperId );
      Object valobj = keeper.read( sr );
      sr.ensureChar( CHAR_EOF );
      parsedValue = avValobj( valobj );
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      return cantParseAsType( EAtomicType.VALOBJ );
    }
    return ValidationResult.SUCCESS;
  }

  private ValidationResult makeNotAnyTypeError( String aText ) {
    StringBuilder sb = new StringBuilder();
    for( int i = 0, n = allowedTypes.size(); i < n; i++ ) {
      sb.append( allowedTypes.get( i ).nmName() );
      if( i < n - 1 ) {
        sb.append( ", " ); //$NON-NLS-1$
      }
    }
    return ValidationResult.error( FMT_CANT_PARSE_ANY_TYPE, aText, sb.toString() );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает упорядоченный список разрешенных атомарных типов.
   * <p>
   * По умолчанию задан следующий состав и порядок типов: {@link EAtomicType#BOOLEAN}, {@link EAtomicType#INTEGER},
   * {@link EAtomicType#FLOATING}, {@link EAtomicType#TIMESTAMP}, {@link EAtomicType#STRING};
   *
   * @return IStridablesList&lt;EAtomicType&gt; - упорядоченный список разрешенных атомарных типов
   */
  public IStridablesList<EAtomicType> getAllowedTypes() {
    return allowedTypes;
  }

  /**
   * Задает состав и порядок разрешенных атомарных типов.
   * <p>
   * Пустой список восстанавливает умолчание {@link #DEFAULT_ALLOWED_TYPES}.
   *
   * @param aTypes ITsCollection&lt;EAtomicType&gt; - упорядоченный список разрешенных атомарных типов
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException аргумент - пустой список
   */
  public void setAllowedTypes( ITsCollection<EAtomicType> aTypes ) {
    TsNullArgumentRtException.checkNull( aTypes );
    if( aTypes.isEmpty() ) {
      allowedTypes.setAll( DEFAULT_ALLOWED_TYPES );
    }
    else {
      allowedTypes.setAll( aTypes );
    }
  }

  /**
   * Возвращает список лексем, которые интерпретируются как булево значение <code>true</code>.
   * <p>
   * Initially contains {@link #DEFAULT_TRUE_NAMES}.
   *
   * @return IStringList - набор лексем, которые интерпретируются как <code>true</code>
   */
  public IStringList getBooleanTrueNames() {
    return bTrueNames;
  }

  /**
   * Задает состав лексем, которые интерпретируются как булево значение <code>true</code>.
   *
   * @param aNames IStringList - набор лексем, которые интерпретируются как <code>true</code>
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setBooleanTrueNames( IStringList aNames ) {
    TsNullArgumentRtException.checkNull( aNames );
    bTrueNames.setAll( aNames );
  }

  /**
   * Возвращает список лексем, которые интерпретируются как булево значение <code>false</code>.
   * <p>
   * Initially contains {@link #DEFAULT_FALSE_NAMES}.
   *
   * @return IStringList - набор лексем, которые интерпретируются как <code>false</code>
   */
  public IStringList getBooleanFalseNames() {
    return bFalseNames;
  }

  /**
   * Задает состав лексем, которые интерпретируются как булево значение <code>false</code>.
   *
   * @param aNames IStringList - набор лексем, которые интерпретируются как <code>false</code>
   * @throws TsNullArgumentRtException аргумент = null
   */
  public void setBooleanFalseNames( IStringList aNames ) {
    TsNullArgumentRtException.checkNull( aNames );
    bFalseNames.setAll( aNames );
  }

  /**
   * Возвращает признак, что пустая входная строка интерпретируется как {@link IAtomicValue#NULL}.
   * <p>
   * В зависимости от установки признака, меняется повдение парсера. При установленном признаке, пустая строка приводит
   * к возвращению значения {@link IAtomicValue#NULL}. При сброшенном признаке, либо распознается как пустая строка
   * (если тип {@link EAtomicType#STRING} был запрошен), либо выбрасывается исключение валидации.
   * <p>
   * По умолчанию возвращает <code>true</code> - признак установлен.
   *
   * @return boolean - признак интерпретации пустой строки как {@link IAtomicValue#NULL}
   */
  public boolean isEmptyStringNull() {
    return interpretBlankTextAsNull;
  }

  /**
   * Задает признак интерпретации пустой строки как {@link IAtomicValue#NULL}.
   * <p>
   * Пустой считается строка, для которой {@link String#isBlank()} == <code>true</code>.
   *
   * @param aInterpretBlankStringAsNull boolean - признак интерпретации пустой строки как {@link IAtomicValue#NULL}
   */
  public void setEmptyStringNull( boolean aInterpretBlankStringAsNull ) {
    interpretBlankTextAsNull = aInterpretBlankStringAsNull;
  }

  /**
   * Возвращает признак возврата {@link IAtomicValue#NULL} вместо исключения {@link TsValidationFailedRtException}.
   * <p>
   * Если флаг установлен, то метод {@link #parse(String)} c с пустой строко будет возвращаеть значение
   * {@link IAtomicValue#NULL}, а не {@link EAtomicType#STRING} со текстом-аргументом.
   * <p>
   * По умолчанию возвращает <code>false</code> - признак сброшен.
   *
   * @return boolean - признак возврата {@link IAtomicValue#NULL} вместо исключения
   *         {@link TsValidationFailedRtException} .
   */
  public boolean isErrorAsNull() {
    return interpretErrorAsNull;
  }

  /**
   * Задает признак возврата {@link IAtomicValue#NULL} вместо исключения {@link TsValidationFailedRtException}.
   *
   * @param aErrorAsNull boolean - признак возврата {@link IAtomicValue#NULL} вместо исключения
   *          {@link TsValidationFailedRtException}.
   */
  public void setErrorAsNull( boolean aErrorAsNull ) {
    interpretErrorAsNull = aErrorAsNull;
  }

  /**
   * Определяет, возможно ли корректное толкование аргумента при заданных параметрах парсера.
   *
   * @param aText String - интерпретируемый текст
   * @return {@link ValidationResult} - результат проверки, только {@link ValidationResult#isError()} или
   *         {@link ValidationResult#SUCCESS}
   */
  public ValidationResult validate( String aText ) {
    TsNullArgumentRtException.checkNull( aText );
    argumentText = aText.trim();
    // обработка пустой строки
    if( argumentText.isBlank() ) {
      if( interpretBlankTextAsNull ) {
        parsedValue = IAtomicValue.NULL;
        return ValidationResult.SUCCESS;
      }
      // пустая строка будет текстом, только если EAtomicType.STRING разрешен, иначе - ошибка
      parsedValue = avStr( aText );
      if( allowedTypes.hasElem( EAtomicType.STRING ) ) {
        return ValidationResult.SUCCESS;
      }
      return makeNotAnyTypeError( aText );
    }
    for( EAtomicType at : allowedTypes ) {
      ValidationResult vr = switch( at ) {
        case NONE -> parseAsNone( aText );
        case BOOLEAN -> parseAsBool();
        case INTEGER -> parseAsInt();
        case FLOATING -> parseAsFloat();
        case STRING -> parseAsString( aText );
        case TIMESTAMP -> parseAsTimetsamp();
        case VALOBJ -> parseAsValobj();
        default -> throw new TsNotAllEnumsUsedRtException();
      };
      if( vr.isOk() ) {
        return vr;
      }
    }
    if( allowedTypes.size() == 1 ) {
      return cantParseAsType( allowedTypes.get( 0 ) );
    }
    return makeNotAnyTypeError( aText );
  }

  /**
   * Разбирает аргумент как один из типов {@link #allowedTypes}.
   * <p>
   * Разбор происходит путем попыток интерпретировать аргумент как текстовое представление значения конкретного
   * атомарного типа. Определение атомарного типа. Порядок перебора по атомарным типам определяется порядком в типов
   * списке {@link #getAllowedTypes()}. Например, строка "1" интерпретируется как <code>true</code>, <code>(int)1</code>
   * и <code>(double)1.0</code>, а что конкретно вернет метод, зависит от последовательности типов в
   * {@link #getAllowedTypes()}. В частности, имеет смысл тип {@link EAtomicType#STRING} всегда оставлять в конце - ведь
   * любой аргумент можно интерпретировать как строку, и соответственно, другие типы после {@link EAtomicType#STRING}
   * даже не будут рассмотрены.
   * <p>
   * Ести установлен флаг {@link #isErrorAsNull()}, то вместо выбрасывания исключения
   * {@link TsValidationFailedRtException} метод вернет {@link IAtomicValue#NULL}.
   * <p>
   * Если установлен флаг {@link #isEmptyStringNull()}, то пустой аргумент приведет к возврату {@link IAtomicValue#NULL}
   * .
   *
   * @param aText String - разбираемый текст
   * @return {@link IAtomicValue} - атомарное значение
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsValidationFailedRtException невозможно причитать текст как одно из типов {@link #getAllowedTypes()}
   */
  public IAtomicValue parse( String aText ) {
    ValidationResult r = validate( aText );
    if( r.isError() ) {
      if( interpretErrorAsNull ) {
        return IAtomicValue.NULL;
      }
      throw new TsValidationFailedRtException( r );
    }
    return parsedValue;
  }

}
