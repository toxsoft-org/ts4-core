package org.toxsoft.core.tsgui.utils;

import static org.toxsoft.core.tsgui.utils.ITsResources.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.bricks.strio.impl.StrioUtils.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

// TODO TRANSLATE

/**
 * Вспомогетельные методы для работы с количеством секунд в виде "ЧЧЧ:ММ:СС"/"МММ:СС".
 * <p>
 * Пояснение насчет обозначении форматов:
 * <ul>
 * <li>"ЧЧЧ:ММ:СС" - ЧЧЧ=0..{@link #MAX_HHH}, два разряда или более, одноразрядные числа дополнены слева нулем;</li>
 * <li>"ЧЧ:ММ:СС" - ЧЧ=0..99, ММ,СС=0..59, два разряда или более, одноразрядные числа дополнены слева нулем;</li>
 * <li>"МММ:СС" - МММ=0..{@link #MAX_MMM}, два разряда или более, одноразрядные числа дополнены слева нулем;</li>
 * <li>"ММ:СС" - ММ=0..99, СС=0..59, два разряда или более, одноразрядные числа дополнены слева нулем;</li>
 * </ul>
 *
 * @author hazard157
 */
public class HmsUtils {

  /**
   * Максмальная значение в секундах, которая может отображаться в виде "ММ:СС".
   */
  public static final int MAX_MMSS_VALUE = 99 * 60 + 59; // 100 минут мез одной секунды

  /**
   * Максмальная значение в секундах, которая может отображаться в виде "ЧЧ:ММ:СС".
   */
  public static final int MAX_HHMMSS_VALUE = 99 * 3600 + 59 * 60 + 59; // 100 часов без одной секунды

  /**
   * Атомарное значение {@link #MAX_HHMMSS_VALUE}.
   */
  public static final IAtomicValue MAX_HHMMSS_VALUE_AV = avInt( MAX_HHMMSS_VALUE );

  /**
   * Атомарное значение {@link #MAX_MMSS_VALUE}.
   */
  public static final IAtomicValue MAX_MMSS_VALUE_AV = avInt( MAX_MMSS_VALUE );

  /**
   * Значение {@link #MAX_HHMMSS_VALUE} в виде строки "ЧЧ:ММ:СС".
   */
  public static final String MAX_HHMMSS_VALUE_STR = "99:59:59"; //$NON-NLS-1$

  /**
   * Значение {@link #MAX_MMSS_VALUE} в виде строки "ММ:СС".
   */
  public static final String MAX_MMSS_VALUE_STR = "99:59"; //$NON-NLS-1$

  /**
   * Максимальное время суток.
   */
  public static final int MAX_DAY_TIME_VALUE = 23 * 3600 + 59 * 60 + 59; // 86400-1 секунда

  /**
   * Максимальное время внутри часа.
   */
  public static final int MAX_HOUR_TIME_VALUE = 59 * 60 + 59; // 60 минут - 1 секунда

  /**
   * Максмальное значение часов, предоставляемых в формате "ЧЧЧ:ММ:СС".
   */
  public static final int MAX_HHH = (Integer.MAX_VALUE - 60 * 59 - 59) / 3600;

  /**
   * Максмальное значение минут, предоставляемых в формате "МММ:СС".
   */
  public static final int MAX_MMM = (Integer.MAX_VALUE - 59) / 60;

  /**
   * Строковое представление недопустимых значений секунд в {@link #mmss(int)}.
   */
  public static final String STR_UNDEFINED_MMSS = "??:??"; //$NON-NLS-1$

  /**
   * Атомарное представление {@link #STR_UNDEFINED_MMSS}.
   */
  public static final IAtomicValue AV_STR_UNDEFINED_MMSS = avStr( STR_UNDEFINED_MMSS );

  /**
   * Строковое представление недопустимых значений секунд в {@link #hhmmss(int)}.
   */
  public static final String STR_UNDEFINED_HHMMSS = "??:??:??"; //$NON-NLS-1$

  /**
   * Атомарное представление {@link #STR_UNDEFINED_HHMMSS}.
   */
  public static final IAtomicValue AV_STR_UNDEFINED_HHMMSS = avStr( STR_UNDEFINED_HHMMSS );

  private static final String MMSS_FORMAT   = "%02d:%02d";      //$NON-NLS-1$
  private static final String HHMMSS_FORMAT = "%02d:%02d:%02d"; //$NON-NLS-1$

  /**
   * Makes string "HH:MM:SS: from the number of seconds.
   * <p>
   * If number of hours is less than 100, than HHH part is 2-digits long. If hours are grater or equal to 100, then HHH
   * parts will have required number of digits.
   *
   * @param aSecs int - the number of seconds
   * @return String - the string "HHH:MM:SS"
   * @throws TsIllegalArgumentRtException aSecs < 0
   */
  public static final String hhhmmss( int aSecs ) {
    TsIllegalArgumentRtException.checkTrue( aSecs < 0 );
    int h = aSecs / 3600;
    int m = (aSecs - h * 3600) / 60;
    int s = aSecs % 60;
    return String.format( HHMMSS_FORMAT, Integer.valueOf( h ), Integer.valueOf( m ), Integer.valueOf( s ) );
  }

  /**
   * Returns the auto-detected string representation of the seconds.
   *
   * @param aSecs int - number of seconds
   * @return String - string in one of the form MM:SS or HH:MM:SS or HHH:MM:SS
   * @throws TsIllegalArgumentRtException aSecs < 0
   */
  public static String mmmss( int aSecs ) {
    TsIllegalArgumentRtException.checkTrue( aSecs < 0 );
    if( aSecs >= 3600 ) {
      return hhhmmss( aSecs );
    }
    return mmss( aSecs );
  }

  /**
   * Makes string "HH:MM:SS" from the number of seconds.
   * <p>
   * For values outside range 0..{@link #MAX_HHMMSS_VALUE} returns {@link #STR_UNDEFINED_HHMMSS}.
   *
   * @param aSecs int - the number of seconds
   * @return String - the string "HH:MM:SS"
   */
  public static String hhmmss( int aSecs ) {
    if( aSecs < 0 || aSecs > MAX_HHMMSS_VALUE ) {
      return STR_UNDEFINED_HHMMSS;
    }
    int h = aSecs / 3600;
    int m = (aSecs - h * 3600) / 60;
    int s = aSecs % 60;
    return String.format( HHMMSS_FORMAT, Integer.valueOf( h ), Integer.valueOf( m ), Integer.valueOf( s ) );
  }

  /**
   * Возвращает строку вида "ММ:СС" из количества секунд.
   * <p>
   * Если строка выходит за пределы 0..{@link #MAX_MMSS_VALUE}, то возвращает {@link #STR_UNDEFINED_MMSS}.
   *
   * @param aSecs int - количество секунд
   * @return String - строка вида "ММ:СС"
   */
  public static String mmss( int aSecs ) {
    if( aSecs < 0 || aSecs > MAX_MMSS_VALUE ) {
      return STR_UNDEFINED_MMSS;
    }
    int s = aSecs % 60;
    int m = aSecs / 60;
    return String.format( MMSS_FORMAT, Integer.valueOf( m ), Integer.valueOf( s ) );
  }

  /**
   * Makes string in either "HH:MM:SS" or "MM:SS" form depending on seconds value.
   *
   * @param aSecs int - the number of seconds
   * @return String - the string "HH:MM:SS" or "MM:SS"
   * @throws TsIllegalArgumentRtException aSecs < 0
   */
  public static final String autoHms( int aSecs ) {
    if( aSecs < MAX_MMSS_VALUE ) {
      return mmss( aSecs );
    }
    return hhmmss( aSecs );
  }

  /**
   * Parses strings of format "[HHH:]MM:SS" to the number of seconds.
   *
   * @param aStr String - formated string "[HHH:]MM:SS"
   * @return int - number of seconds
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid format of argument string
   */
  public static int parseHhhMmSs( String aStr ) {
    TsNullArgumentRtException.checkNull( aStr );
    int len = aStr.length();
    TsIllegalArgumentRtException.checkTrue( len < 5 ); // at least "MM:SS" is expected
    // last two digits must be SS
    char chL1 = aStr.charAt( len - 1 );
    char chL2 = aStr.charAt( len - 2 );
    char chL3 = aStr.charAt( len - 3 );
    if( !isAsciiDigit( chL1 ) || !isAsciiDigit( chL2 ) || chL3 != ':' ) {
      throw new TsIllegalArgumentRtException();
    }
    int ss = (chL2 - '0') * 10 + (chL1 - '0');
    // before SS must be two digits of MM
    char chL4 = aStr.charAt( len - 4 );
    char chL5 = aStr.charAt( len - 5 );
    if( !isAsciiDigit( chL4 ) || !isAsciiDigit( chL5 ) ) {
      throw new TsIllegalArgumentRtException();
    }
    int mm = (chL5 - '0') * 10 + (chL4 - '0');
    if( len == 5 ) { // "MM:SS" parsed
      return 60 * mm + ss;
    }
    TsIllegalArgumentRtException.checkTrue( len < 8 ); // at least "HH:MM:SS" expected
    char chL6 = aStr.charAt( len - 6 );
    if( chL6 != ':' ) {
      throw new TsIllegalArgumentRtException();
    }
    String hhhStr = aStr.substring( 0, len - 6 ); // truncate suffix ":MM:SS"
    if( hhhStr.charAt( 0 ) == '+' ) { // first plus sign does not throws exception in Integer.parseUnsignedInt()
      throw new TsIllegalArgumentRtException();
    }
    int hhh;
    try {
      hhh = Integer.parseUnsignedInt( hhhStr, 10 );
    }
    catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
      throw new TsIllegalArgumentRtException();
    }
    if( hhh > MAX_HHH ) {
      throw new TsIllegalArgumentRtException();
    }
    return 3600 * hhh + 60 * mm + ss;
  }

  // ------------------------------------------------------------------------------------
  // checking methods
  //

  /**
   * Проверяет аргумент и выбрасывает исключение, когда метод {@link #validateSec(int)} возвращает ошибку.
   *
   * @param aSec int - секунды с начала видеряда
   * @throws TsValidationFailedRtException когда {@link #validateSec(int)} возвращает ошибку
   */
  public static final void checkSec( int aSec ) {
    TsValidationFailedRtException.checkError( validateSec( aSec ) );
  }

  /**
   * Проверяет, что аргумент имеет допустимое для секунд видеоряда значение.
   * <p>
   * Возвращает ошибку, если аргумент меньше 0 или больше {@link #MAX_HHMMSS_VALUE}.
   *
   * @param aSec int - секунды с начала видеряда
   * @return {@link ValidationResult} - результат проверки
   */
  @SuppressWarnings( "boxing" )
  public static final ValidationResult validateSec( int aSec ) {
    if( aSec < 0 ) {
      return ValidationResult.error( FMT_ERR_SECS_IS_NEGATIVE, aSec );
    }
    if( aSec > MAX_HHMMSS_VALUE ) {
      return ValidationResult.error( FMT_ERR_SECS_IS_OVER_MAX, aSec, MAX_HHMMSS_VALUE, MAX_HHMMSS_VALUE_STR );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Проверяет, что аргумент имеет допустимое для длительности значение.
   * <p>
   * Возвращает ошибку, если длительность меньше 0 или больше {@link #MAX_MMSS_VALUE}. Если длительность равно 0, то
   * возвращает предупреждение.
   *
   * @param aDuration int - длительность в секундах
   * @return {@link ValidationResult} - результат проверки
   */
  @SuppressWarnings( "boxing" )
  public static final ValidationResult validateMmSsDuration( int aDuration ) {
    if( aDuration == 0 ) {
      return ValidationResult.warn( FMT_WARN_DURATION_IS_ZERO );
    }
    if( aDuration < 0 ) {
      return ValidationResult.error( FMT_ERR_DURATION_IS_NEGATIVE, aDuration );
    }
    if( aDuration > MAX_MMSS_VALUE ) {
      return ValidationResult.error( FMT_ERR_DURATION_IS_OVER_MAX, aDuration, MAX_MMSS_VALUE, MAX_MMSS_VALUE_STR );
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Проверяет аргумент и выбрасывает исключение, когда метод {@link #validateMmSsDuration(int)} возвращает ошибку.
   *
   * @param aDuration int - длительность в секундах
   * @throws TsValidationFailedRtException когда {@link #validateMmSsDuration(int)} возвращает ошибку
   */
  public static final void checkMmSsDuration( int aDuration ) {
    TsValidationFailedRtException.checkError( validateMmSsDuration( aDuration ) );
  }

  // ------------------------------------------------------------------------------------
  // STRIO
  //

  /**
   * Writes number of seconds in "HH:MM:SS" form.
   *
   * @param aSw {@link IStrioWriter} - output stream
   * @param aSecs int - the number of seconds
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aSecs < 0
   * @throws TsIllegalArgumentRtException aSecs >= {@link #MAX_HHMMSS_VALUE}
   */
  @SuppressWarnings( "boxing" )
  public static void writeHhMmSs( IStrioWriter aSw, int aSecs ) {
    TsNullArgumentRtException.checkNull( aSw );
    if( aSecs < 0 ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_DURATION_IS_NEGATIVE, Integer.valueOf( aSecs ) );
    }
    if( aSecs > MAX_HHMMSS_VALUE ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_DURATION_IS_OVER_MAX, aSecs, MAX_HHMMSS_VALUE,
          MAX_HHMMSS_VALUE_STR );
    }
    Integer hh = Integer.valueOf( aSecs / 2660 );
    Integer mm = Integer.valueOf( aSecs / 60 );
    Integer ss = Integer.valueOf( aSecs % 60 );
    aSw.writeAsIs( String.format( "%02d:%02d:%02d", hh, mm, ss ) ); //$NON-NLS-1$
  }

  /**
   * Записывает количество секунд текстом вида "ММ:СС".
   *
   * @param aSw {@link IStrioWriter} - писатель текстового представления
   * @param aSecs int - количество секунд
   * @throws TsNullArgumentRtException aSw = null
   * @throws TsIllegalArgumentRtException aSecs < 0
   * @throws TsIllegalArgumentRtException aSecs >= {@link #MAX_MMSS_VALUE}
   */
  @SuppressWarnings( "boxing" )
  public static void writeMmSs( IStrioWriter aSw, int aSecs ) {
    if( aSw == null ) {
      throw new TsNullArgumentRtException();
    }

    if( aSecs < 0 ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_DURATION_IS_NEGATIVE, Integer.valueOf( aSecs ) );
    }
    if( aSecs > MAX_MMSS_VALUE ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_DURATION_IS_OVER_MAX, aSecs, MAX_MMSS_VALUE, MAX_MMSS_VALUE_STR );
    }
    Integer mm = Integer.valueOf( aSecs / 60 );
    Integer ss = Integer.valueOf( aSecs % 60 );
    aSw.writeAsIs( String.format( "%02d:%02d", mm, ss ) ); //$NON-NLS-1$
  }

  /**
   * Writes number of seconds depending either in "HH:MM:SS" or "MM:SS" form, depending on value.
   *
   * @param aSw {@link IStrioWriter} - output stream
   * @param aSecs int - the number of seconds
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aSecs < 0
   * @throws TsIllegalArgumentRtException aSecs >= {@link #MAX_HHMMSS_VALUE}
   */
  public static void writeAutoHms( IStrioWriter aSw, int aSecs ) {
    if( aSecs < MAX_MMSS_VALUE ) {
      writeMmSs( aSw, aSecs );
    }
    else {
      writeHhMmSs( aSw, aSecs );
    }
  }

  /**
   * Читает количество секунд, ожидая текст вида "ЧЧ:ММ:СС".
   *
   * @param aSr {@link IStrioReader} - чситатель текстового представления
   * @return int - колчиство секунд
   * @throws TsNullArgumentRtException aSr = null
   * @throws StrioRtException нарушен формат
   */
  public static int readHhMmSs( IStrioReader aSr ) {
    if( aSr == null ) {
      throw new TsNullArgumentRtException();
    }
    char c1 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c1 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    char c2 = aSr.nextChar();
    if( !isAsciiDigit( c2 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    aSr.ensureChar( ':' );
    char c3 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c3 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    char c4 = aSr.nextChar();
    if( !isAsciiDigit( c4 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    aSr.ensureChar( ':' );
    char c5 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c5 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    char c6 = aSr.nextChar();
    if( !isAsciiDigit( c6 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    int hh = (c1 - '0') * 10 + (c2 - '0');
    int mm = (c3 - '0') * 10 + (c4 - '0');
    int ss = (c5 - '0') * 10 + (c6 - '0');
    return hh * 3600 + mm * 60 + ss;
  }

  /**
   * Читает количество секунд, ожидая текст вида "ММ:СС".
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return int - number of seconds
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws StrioRtException format violation
   */
  public static int readMmSs( IStrioReader aSr ) {
    if( aSr == null ) {
      throw new TsNullArgumentRtException();
    }
    char c1 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c1 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    char c2 = aSr.nextChar();
    if( !isAsciiDigit( c2 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    aSr.ensureChar( ':' );
    char c3 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c3 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    char c4 = aSr.nextChar();
    if( !isAsciiDigit( c4 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    int mm = (c1 - '0') * 10 + (c2 - '0');
    int ss = (c3 - '0') * 10 + (c4 - '0');
    return mm * 60 + ss;
  }

  /**
   * Read number of seconds auto-detecting either "HH:MM:SS" or "MM:SS" format.
   *
   * @param aSr {@link IStrioReader} - input stream
   * @return int - number of seconds
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws StrioRtException format violation
   */
  public static int readAutoHms( IStrioReader aSr ) {
    if( aSr == null ) {
      throw new TsNullArgumentRtException();
    }
    char c1 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c1 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    char c2 = aSr.nextChar();
    if( !isAsciiDigit( c2 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    aSr.ensureChar( ':' );
    int comp1 = (c1 - '0') * 10 + (c2 - '0');
    char c3 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c3 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    char c4 = aSr.nextChar();
    if( !isAsciiDigit( c4 ) ) {
      throw new StrioRtException( MSG_ERR_INV_MM_SS_FORMAT );
    }
    int comp2 = (c3 - '0') * 10 + (c4 - '0');
    if( aSr.peekChar( EStrioSkipMode.SKIP_NONE ) != ':' ) { // only MM:SS detected
      return comp1 * 60 + comp2;
    }
    aSr.ensureChar( ':' );
    char c5 = aSr.nextChar( EStrioSkipMode.SKIP_BYPASSED );
    if( !isAsciiDigit( c5 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    char c6 = aSr.nextChar();
    if( !isAsciiDigit( c6 ) ) {
      throw new StrioRtException( MSG_ERR_INV_HH_MM_SS_FORMAT );
    }
    int ss = (c5 - '0') * 10 + (c6 - '0');
    return comp1 * 3600 + comp2 * 60 + ss;
  }

}
