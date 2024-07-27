package org.toxsoft.core.tslib.utils.txtmatch;

import java.io.*;
import java.util.regex.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Helper class for implementing comparison algorithms of the constants {@link ETextMatchMode}.
 * <p>
 * The created instance is ready to compare different strings with the given constant string in the specified mode. The
 * comparison is performed by the {@link #match(String)} method.
 *
 * @author hazard157
 */
public final class TextMatcher
    implements ITsFilter<String>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final ETextMatchMode matchMode;
  private final boolean        isCaseSensitive;
  private final String         constString;
  private final String         constStringLowerCase;
  private final Pattern        pattern;

  /**
   * Constructor.
   *
   * @param aMatchMode {@link ETextMatchMode} - the string comparison (matching) mode
   * @param aConstString String - the string constant to compare to
   * @param aCaseSensitive boolean - the case sensitivity flag during comparison
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
   * Returns the text matching mode (the comparison method).
   *
   * @return {@link ETextMatchMode} - the matching mode
   */
  public ETextMatchMode matchMode() {
    return matchMode;
  }

  /**
   * Returns the string constant used to compare accepted string to.
   *
   * @return String - he string constant to compare to
   */
  public String constString() {
    return constString;
  }

  /**
   * Returns a case-sensitive check flag.
   * <p>
   * Some comparison modes (see descriptions of {@link ETextMatchMode} constants) can occur both with and without
   * case-sensitive characters. This feature lets you know how such comparisons occur.
   *
   * @return boolean - the case sensitivity flag during comparison
   */
  public boolean isCaseSensitive() {
    return isCaseSensitive;
  }

  /**
   * Compares <code>aString</code> to the {@link #constString()} according to the {@link #matchMode()}.
   *
   * @param aString String - the string to compare
   * @return boolean - the string matching flag according to {@link ETextMatchMode}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public boolean match( String aString ) {
    if( aString == null ) {
      throw new TsNullArgumentRtException();
    }
    switch( matchMode ) {
      case EXACT:
        return aString.equals( constString );
      case CONTAINS:
        if( isCaseSensitive ) {
          return aString.contains( constString );
        }
        return aString.toLowerCase().contains( constStringLowerCase );
      case REGEXP:
        return pattern.matcher( aString ).matches();
      case STARTS:
        if( isCaseSensitive ) {
          return aString.startsWith( constString );
        }
        return aString.toLowerCase().startsWith( constStringLowerCase );
      case ENDS:
        if( isCaseSensitive ) {
          return aString.endsWith( constString );
        }
        return aString.toLowerCase().endsWith( constStringLowerCase );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsFilter
  //

  @Override
  public boolean accept( String aObj ) {
    return match( aObj );
  }

  // ------------------------------------------------------------------------------------
  // Object
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
