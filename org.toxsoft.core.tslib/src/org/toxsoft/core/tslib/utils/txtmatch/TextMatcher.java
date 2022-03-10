package org.toxsoft.core.tslib.utils.txtmatch;

import java.io.*;
import java.util.regex.*;

import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вспомогательный класс для реализации алгоритмов сравнения {@link ETextMatchMode}.
 * <p>
 * Созданный конструктором экземпляр готов к сравнению различных строк с заданной константной строкой в указанном
 * режиме. Сравнение производится методом {@link #match(String)}.
 *
 * @author hazard157
 */
public final class TextMatcher
    implements Serializable {

  private static final long serialVersionUID = 157157L;

  private final ETextMatchMode matchMode;
  private final boolean        isCaseSensitive;
  private final String         constString;
  private final String         constStringLowerCase;
  private final Pattern        pattern;

  /**
   * Создает сравниватель со всеми инвариантами.
   *
   * @param aMatchMode {@link ETextMatchMode} - режим сравнения
   * @param aConstString String - константная строка, с которой происходит сравнение исходной строки
   * @param aCaseSensitive boolean - признак проверки с учетом регистра
   */
  public TextMatcher( ETextMatchMode aMatchMode, String aConstString, boolean aCaseSensitive ) {
    TsNullArgumentRtException.checkNulls( aMatchMode, aConstString );
    matchMode = aMatchMode;
    constString = aConstString;
    isCaseSensitive = aCaseSensitive;
    switch( matchMode ) {
      case EXACT:
        pattern = null;
        constStringLowerCase = null;
        break;
      case CONTAINS:
      case STARTS:
      case ENDS:
        pattern = null;
        constStringLowerCase = constString.toLowerCase();
        return;
      case REGEXP:
        pattern = Pattern.compile( constString );
        constStringLowerCase = null;
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Создает сравниватель без учета регистра символов.
   *
   * @param aMatchMode {@link ETextMatchMode} - режим сравнения
   * @param aConstString String - константная строка, с которой происходит сравнение исходной строки
   */
  public TextMatcher( ETextMatchMode aMatchMode, String aConstString ) {
    this( aMatchMode, aConstString, false );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Возвращает режим сравнения текста.
   *
   * @return {@link ETextMatchMode} - режим сравнения текста
   */
  public ETextMatchMode matchMode() {
    return matchMode;
  }

  /**
   * Вовзращает константную строку, с которой происходит сравнение исходной строки.
   *
   * @return String - константная строка, с которой происходит сравнение исходной строки
   */
  public String constString() {
    return constString;
  }

  /**
   * Возвращает признак проверки с учетом регистра.
   * <p>
   * Некторые режимы сравнения (см. описания констант {@link ETextMatchMode}) могут происходить как с учетеом, так и без
   * учета регистра символов. Этот признак дает знать, как происходят такие сравнение.
   *
   * @return boolean - признак проверки с учетом регистра
   */
  public boolean isCaseSensitive() {
    return isCaseSensitive;
  }

  /**
   * Осуществляет сравнение исходной строки с {@link #constString()} в режиме сравнения {@link #matchMode()}.
   *
   * @param aSourceString String - исходная строка
   * @return boolean - результат срванения по правилам {@link ETextMatchMode}
   * @throws TsNullArgumentRtException аргумент = null
   */
  public boolean match( String aSourceString ) {
    if( aSourceString == null ) {
      throw new TsNullArgumentRtException();
    }
    switch( matchMode ) {
      case EXACT:
        return aSourceString.equals( constString );
      case CONTAINS:
        if( isCaseSensitive ) {
          return aSourceString.contains( constString );
        }
        return aSourceString.toLowerCase().contains( constStringLowerCase );
      case REGEXP:
        return pattern.matcher( aSourceString ).matches();
      case STARTS:
        if( isCaseSensitive ) {
          return aSourceString.startsWith( constString );
        }
        return aSourceString.toLowerCase().startsWith( constStringLowerCase );
      case ENDS:
        if( isCaseSensitive ) {
          return aSourceString.endsWith( constString );
        }
        return aSourceString.toLowerCase().endsWith( constStringLowerCase );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  public String toString() {
    return matchMode.id() + " \"" + TsMiscUtils.toQuotedLine( constString ) + "\" " + //
        (isCaseSensitive ? "CaseSens" : "CaseInsens");
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TextMatcher that ) {
      return matchMode.equals( that.matchMode ) //
          && constString.equals( that.constString ) //
          && isCaseSensitive == that.isCaseSensitive;
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + matchMode.hashCode();
    result = TsLibUtils.PRIME * result + constString.hashCode();
    result = TsLibUtils.PRIME * result + (isCaseSensitive ? 1 : 0);
    return result;
  }

}
