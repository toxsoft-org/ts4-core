package org.toxsoft.core.tslib.bricks.strid.impl;

import static org.toxsoft.core.tslib.bricks.strid.impl.ITsResources.*;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Methods to work with strids (IPnames and IDpaths).
 * <p>
 * IDname and IDpath concepts are introduced:
 * <ul>
 * <li><b>IDname</b> – is lexeme, conforming following rules: consist of ASCII letter (a-z, A-z), digits (0-9) and
 * underscore character (_). IDname starting character must be letter or underscore, digits is not allowed to be first
 * character of IDname. IDname must have at least one character length. Maximal legth of IDname is not limited.
 * Examples: "<code>gml</code>", "<code>Structure_Type</code>", "<code>Const11</code>".</li>
 * <li><b>IDpath</b> – consists of one or more IDnames, separated by dot (.) character. Two sequental dots are not
 * allowed. By definition, any IDname is valid IDpath. Examples: "<code>org.toxsoft.gml</code>",
 * "<code>this.is.test1.of.id_path</code>", "<code>this_id_name_is_id_path_too</code>".</li>
 * </ul>
 * All methods of this class are thread-safe.
 * <p>
 * Some additional terminology:
 * <ul>
 * <li>IDpath <b>component</b> - one of the IDnames that make up the IDpath. Component word is used to specify IDname as
 * part of IDpath.</li>
 * <li><b>prefix</b> - an IDpath consisting from any number of components starting from the first one. Prefix is IDpath
 * for multi-component IDpaths and empty string for IDname (IDpath with one component).</li>
 * <li><b>suffix</b> - an IDpath consisting from any number of components starting from somewhere in the ID path and
 * ending with last one. Suffix is IDpath for multi-component IDpaths and empty string for IDname (IDpath with one
 * component).</li>
 * </ul>
 *
 * @author hazard157
 */
public final class StridUtils {

  // ------------------------------------------------------------------------------------
  // Check for validity
  //

  /**
   * Characters allowed as the first symbol of IDpath (or IDname).
   */
  @SuppressWarnings( "nls" )
  public static final String CHARS_IDNAME_START = "_" + "abcdefghijklmnopqrstuvwxyz" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ";

  /**
   * Characters allowed in any place in an IDname.
   * <p>
   * Includes all characters from {@link #CHARS_IDNAME_START}.
   */
  @SuppressWarnings( "nls" )
  public static final String CHARS_IDNAME_PART = "0123456789" + CHARS_IDNAME_START;

  /**
   * IDnames in IDpath delimiter char (dot).
   */
  public static final char CHAR_ID_PATH_DELIMITER = '.';

  /**
   * Check if argument equals to {@link IStridable#NONE_ID}.
   *
   * @param aId String - string to be checked
   * @return boolean - {@link IStridable#NONE_ID} flag
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isNullId( String aId ) {
    TsNullArgumentRtException.checkNull( aId );
    return aId.equals( IStridable.NONE_ID );
  }

  /**
   * Determines if the specified character may be the first char in IDname or IDpath.
   *
   * @param aCh char - specified character
   * @return <b>true</b> - character may be at IDname or IDpath start;<br>
   *         <b>false</b> - this is invalid char for IDname/IDpath to start from.
   */
  public static boolean isIdStart( char aCh ) {
    if( aCh == '_' ) {
      return true;
    }
    if( aCh < 'A' || aCh > 'z' ) {
      return false;
    }
    if( aCh > 'Z' && aCh < 'a' ) {
      return false;
    }
    return true;
  }

  /**
   * Determines if the specified character may be the part of an IDname.
   *
   * @param aCh char - specified character
   * @return <b>true</b> - character may be part of an IDname ;<br>
   *         <b>false</b> - this is invalid char for IDname.
   */
  public static boolean isIdNamePart( char aCh ) {
    if( aCh >= 'A' && aCh <= 'Z' ) {
      return true;
    }
    if( aCh >= 'a' && aCh <= 'z' ) {
      return true;
    }
    if( aCh == '_' ) {
      return true;
    }
    if( aCh >= '0' && aCh <= '9' ) {
      return true;
    }
    return false;
  }

  /**
   * Определяет, может ли символ находится в ИД-пути.
   *
   * @param aCh char - проверяемый символ
   * @return <b>true</b> - да, символ может быть частью (но не обязательно началом) и ИД-пути (но не обязательно
   *         ИД-имени);<br>
   *         <b>false</b> - недопустимый в ИД-имени и ИД-пути символ.
   */
  /**
   * Determines if the specified character may be the part of an IDpath.
   *
   * @param aCh char - specified character
   * @return <b>true</b> - character may be part of an IDpath;<br>
   *         <b>false</b> - this is invalid char for IDpath.
   */
  public static boolean isIdPathPart( char aCh ) {
    if( aCh >= 'A' && aCh <= 'Z' ) {
      return true;
    }
    if( aCh >= 'a' && aCh <= 'z' ) {
      return true;
    }
    if( aCh == '_' || aCh == CHAR_ID_PATH_DELIMITER ) {
      return true;
    }
    if( aCh >= '0' && aCh <= '9' ) {
      return true;
    }
    return false;
  }

  /**
   * Checks if specified string is valid IDname.
   *
   * @param aIdName String - specified string
   * @return <b>true</b> - argumnet is valid IDname;<br>
   *         <b>false</b> - specified string violates IDname format.
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isValidIdName( String aIdName ) {
    return !validateIdName( aIdName ).isError();
  }

  /**
   * Checks if specified string is valid IDpath.
   *
   * @param aIdPath String - specified string
   * @return <b>true</b> - argumnet is valid IDpath;<br>
   *         <b>false</b> - specified string violates IDpath format.
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static boolean isValidIdPath( String aIdPath ) {
    return !validateIdPath( aIdPath ).isError();
  }

  /**
   * Throws and exception if specified string is not valid IDname.
   *
   * @param aIdName String - specified string
   * @return String - argument
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link #validateIdName(String)}
   */
  public static String checkValidIdName( String aIdName ) {
    TsValidationFailedRtException.checkError( validateIdName( aIdName ) );
    return aIdName;
  }

  /**
   * Throws and exception if specified string is not valid IDpath.
   *
   * @param aIdPath String - specified string
   * @return String - argument
   * @throws TsNullArgumentRtException argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link #validateIdPath(String)}
   */
  public static String checkValidIdPath( String aIdPath ) {
    TsValidationFailedRtException.checkError( validateIdPath( aIdPath ) );
    return aIdPath;
  }

  /**
   * Validates if specified string is valid IDname.
   * <p>
   * Returns error {@link EValidationResultType#ERROR} result if:
   * <ul>
   * <li>argument is an empty string;</li>
   * <li>first symbol is not of {@link #CHARS_IDNAME_START};</li>
   * <li>argument contains any character except {@link #CHARS_IDNAME_PART}.</li>
   * </ul>
   *
   * @param aIdName String - specified string
   * @return {@link ValidationResult} - the validation results
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ValidationResult validateIdName( String aIdName ) {
    TsNullArgumentRtException.checkNull( aIdName );
    if( aIdName.length() == 0 ) {
      return ValidationResult.error( ERR_MSG_EMPTY_ID_STRING );
    }
    char ch = aIdName.charAt( 0 );
    if( !isIdStart( ch ) ) {
      return ValidationResult.error( ERR_MSG_INV_ID_STRING_START, Character.valueOf( ch ) );
    }
    for( int i = 1, n = aIdName.length(); i < n; i++ ) {
      ch = aIdName.charAt( i );
      if( !isIdNamePart( ch ) ) {
        return ValidationResult.error( ERR_MSG_INV_ID_NAME_PART, Character.valueOf( ch ) );
      }
    }
    return ValidationResult.SUCCESS;
  }

  /**
   * Validates if specified string is valid IDpath.
   * <p>
   * Returns error {@link EValidationResultType#ERROR} result if:
   * <ul>
   * <li>argument is an empty string;</li>
   * <li>first symbol is not of {@link #CHARS_IDNAME_START};</li>
   * <li>argument contains any character except {@link #CHARS_IDNAME_PART} and {@link #CHAR_ID_PATH_DELIMITER};</li>
   * <li>there is two delimiter {@link #CHAR_ID_PATH_DELIMITER} chars in a row;</li>
   * <li>the last char in argument is delimieter.</li>
   * </ul>
   *
   * @param aIdPath String - specified string
   * @return {@link ValidationResult} - the validation results
   * @throws TsNullArgumentRtException argument = <code>null</code>
   */
  public static ValidationResult validateIdPath( String aIdPath ) {
    TsNullArgumentRtException.checkNull( aIdPath );
    int argLen = aIdPath.length();
    if( argLen <= 0 ) {
      return ValidationResult.error( ERR_MSG_EMPTY_ID_STRING );
    }
    char ch = aIdPath.charAt( 0 );
    if( !isIdStart( aIdPath.charAt( 0 ) ) ) {
      return ValidationResult.error( ERR_MSG_INV_ID_STRING_START, Character.valueOf( ch ) );
    }
    boolean prevWasDelim = false;
    for( int i = 1; i < argLen; i++ ) {
      ch = aIdPath.charAt( i );
      if( ch == CHAR_ID_PATH_DELIMITER ) {
        if( i == argLen - 1 ) { // last dot is not allowed
          return ValidationResult.error( ERR_MSG_ILLEGAL_LAST_DOT );
        }
        if( prevWasDelim ) { // two dots in row is not allowed
          return ValidationResult.error( ERR_MSG_DUPLICATED_DOTS );
        }
        prevWasDelim = true;
      }
      else {
        if( prevWasDelim ) {
          if( !isIdStart( ch ) ) {
            return ValidationResult.error( ERR_MSG_INV_ID_PATH_PART_START, Character.valueOf( ch ) );
          }
        }
        prevWasDelim = false;
        if( !isIdPathPart( ch ) ) {
          return ValidationResult.error( ERR_MSG_INV_ID_PATH_PART, Character.valueOf( ch ) );
        }
      }
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // Content investigation
  //
  /**
   * Determines if argument IDpath containing of two or more compnents (ie is not an IDname).
   *
   * @param aId String - an IDpath to be checked
   * @return <code>true</code> if argument is valid IDpath containig two or more comonents
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException argument is not valid IDpath
   */
  public static boolean isIdAPath( String aId ) {
    checkValidIdPath( aId );
    return aId.indexOf( CHAR_ID_PATH_DELIMITER ) >= 0;
  }

  /**
   * Test if aIdPath starts with aPrefixIdPath.
   *
   * @param aIdPath String - IDpath to be checked
   * @param aPrefixIdPath String - possible prefix
   * @return boolean - <code>true</code> if aIdPath starts with aPrefixIdPath or they are equeal
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is not an IDpath
   */
  public static boolean startsWithIdPath( String aIdPath, String aPrefixIdPath ) {
    checkValidIdPath( aIdPath );
    checkValidIdPath( aPrefixIdPath );
    if( aIdPath.startsWith( aPrefixIdPath ) ) {
      if( aIdPath.length() == aPrefixIdPath.length() ) {
        return true;
      }
      return aIdPath.charAt( aPrefixIdPath.length() ) == CHAR_ID_PATH_DELIMITER;
    }
    return false;
  }

  /**
   * Tests if aIdPath ends with aSuffixIdPath.
   *
   * @param aIdPath String - IDpath to be checked
   * @param aSuffixIdPath String - possible suffix
   * @return boolean - <code>true</code> if aIdPath ends with aSuffixIdPath or they are equeal
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any argument is not an IDpath
   */
  public static boolean endsWithIdPath( String aIdPath, String aSuffixIdPath ) {
    checkValidIdPath( aIdPath );
    checkValidIdPath( aSuffixIdPath );
    if( aIdPath.endsWith( aSuffixIdPath ) ) {
      if( aIdPath.length() == aSuffixIdPath.length() ) {
        return true;
      }
      return aIdPath.charAt( aIdPath.length() ) == CHAR_ID_PATH_DELIMITER;
    }
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Assemble IDpaths/names
  //

  /**
   * Creates IDpath as concatenation of two components.
   *
   * @param aIdPath1 String - left component (IDpath) or an empty string
   * @param aIdPath2 String - right component (IDpath) or an empty string
   * @return String - created IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException both compnents are empty strings
   * @throws TsIllegalArgumentRtException any of the non-empty component is not an IDpath
   */
  public static String makeIdPath( String aIdPath1, String aIdPath2 ) {
    if( aIdPath1 == null || aIdPath2 == null ) {
      throw new TsNullArgumentRtException();
    }
    boolean path1empty = aIdPath1.isEmpty();
    boolean path2empty = aIdPath2.isEmpty();
    TsIllegalArgumentRtException.checkTrue( path1empty && path2empty );
    if( !path1empty ) {
      checkValidIdPath( aIdPath1 );
    }
    if( !path2empty ) {
      checkValidIdPath( aIdPath2 );
    }
    if( path1empty || path2empty ) {
      return aIdPath1 + aIdPath2;
    }
    return aIdPath1 + CHAR_ID_PATH_DELIMITER + aIdPath2;
  }

  /**
   * Creates IDpath from components.
   *
   * @param aComps {@link IList}&lt;String&gt; - components (IDnames)
   * @return String - created IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any component is not valid IDname
   */
  public static String makeIdPath( IList<String> aComps ) {
    TsNullArgumentRtException.checkNull( aComps );
    return makeIdPath( aComps, 0, aComps.size() );
  }

  /**
   * Creates IDpath from components.
   *
   * @param aComps {@link IList}&lt;String&gt; - components (IDnames)
   * @param aStartIndex int - index of the first used component in argument
   * @param aCount int - number of components to be used for IDpath creation
   * @return String - created IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any component is not valid IDname
   * @throws TsIllegalArgumentRtException aStartIndex < 0
   * @throws TsIllegalArgumentRtException aCount <= 0
   * @throws TsIllegalArgumentRtException aStartIndex is out of allowed range
   * @throws TsIllegalArgumentRtException aStartIndex + aCount is out of allowed range
   */
  public static String makeIdPath( IList<String> aComps, int aStartIndex, int aCount ) {
    TsNullArgumentRtException.checkNull( aComps );
    TsIllegalArgumentRtException.checkTrue( aStartIndex < 0 );
    TsIllegalArgumentRtException.checkTrue( aCount < 0 );
    int n = aComps.size();
    TsIllegalArgumentRtException.checkTrue( aStartIndex >= n );
    TsIllegalArgumentRtException.checkTrue( aStartIndex + aCount > n );
    StringBuilder strBuilder = new StringBuilder();
    boolean wasAny = false;
    for( int i = aStartIndex; i < aStartIndex + aCount; i++ ) {
      String s = aComps.get( i );
      TsIllegalArgumentRtException.checkFalse( isValidIdName( s ) );
      if( wasAny ) {
        strBuilder.append( CHAR_ID_PATH_DELIMITER );
      }
      else {
        wasAny = true;
      }
      strBuilder.append( s );
    }
    return strBuilder.toString();
  }

  /**
   * Creates IDpath as concatenation of an IDname to the IDpath.
   *
   * @param aIdPath String - starting IDpath
   * @param aIdName String - ending IDname
   * @return String - created IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aIdPath is not valid IDpath
   * @throws TsIllegalArgumentRtException aIdName is not valid IDname
   */
  public static String appendIdName( String aIdPath, String aIdName ) {
    StridUtils.checkValidIdPath( aIdPath );
    StridUtils.checkValidIdName( aIdName );
    return aIdPath + CHAR_ID_PATH_DELIMITER + aIdName;
  }

  /**
   * Creates IDpath as concatenation of an IDpath to the IDname.
   *
   * @param aIdName String - starting IDname
   * @param aIdPath String - ending IDpath
   * @return String - created IDpath
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aIdPath is not valid IDpath
   * @throws TsIllegalArgumentRtException aIdName is not valid IDname
   */
  public static String prependIdName( String aIdName, String aIdPath ) {
    StridUtils.checkValidIdName( aIdName );
    StridUtils.checkValidIdPath( aIdPath );
    return aIdName + CHAR_ID_PATH_DELIMITER + aIdPath;
  }

  // ------------------------------------------------------------------------------------
  // Disassemple IDpaths/names
  //

  /**
   * Returns all compmonents of the argument.
   *
   * @param aIdPath String - an IDpath
   * @return {@link IStringList} - components (IDnames) of the argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aIdPath is not an IDpath
   */
  public static IStringList getComponents( String aIdPath ) {
    IStringListEdit result = new StringArrayList();
    if( isIdAPath( aIdPath ) ) {
      int ind1 = -1, ind2 = 0;
      do {
        ind2 = aIdPath.indexOf( CHAR_ID_PATH_DELIMITER, ind1 + 1 );
        if( ind2 == -1 ) {
          if( ind1 != 0 ) {
            result.add( aIdPath.substring( ind1 + 1 ) );
          }
        }
        else {
          result.add( aIdPath.substring( ind1 + 1, ind2 ) );
        }
        ind1 = ind2;
      } while( ind2 >= 0 );
    }
    else {
      result.add( aIdPath );
    }
    return result;
  }

  /**
   * Returns the component of specified index.
   *
   * @param aIdPath String - an IDpath
   * @param aIndex int - component index starting from 0
   * @return String - component (IDname)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException index is out of range
   */
  public static String getComponent( String aIdPath, int aIndex ) {
    return getComponents( aIdPath ).get( aIndex );
  }

  /**
   * Returns the first component.
   * <p>
   * If argument is IDname then returns argument.
   * <p>
   * Returns the same as {@link #getComponent(String, int) getComponent(aIdPath, 0)}
   *
   * @param aIdPath String - an IDpath
   * @return String - first component (IDname)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException index is out of range
   */
  public static String getFirst( String aIdPath ) {
    return getComponents( aIdPath ).first();
  }

  /**
   * Returns the first component.
   * <p>
   * If argument is IDname then returns argument.
   * <p>
   * Returns the same as {@link #getComponent(String, int) getComponent(aIdPath, lastIndex)}
   *
   * @param aIdPath String - an IDpath
   * @return String - first component (IDname)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException index is out of range
   */
  public static String getLast( String aIdPath ) {
    return getComponents( aIdPath ).last();
  }

  /**
   * Removes specified number of strarting components.
   * <p>
   * If all components removed, returns an empty string.
   *
   * @param aIdPath String - an IDpath
   * @param aCount int - number of components to remove
   * @return String - remianing IDpath or the empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aCount < 0
   * @throws TsIllegalArgumentRtException aIdPath is not an IDpath
   * @throws TsIllegalArgumentRtException aCount > number of components in argument
   */
  public static String removeStartingIdNames( String aIdPath, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aCount < 0 );
    IStringList comps = getComponents( aIdPath );
    TsIllegalArgumentRtException.checkTrue( comps.size() < aCount );
    if( comps.size() == aCount ) {
      return TsLibUtils.EMPTY_STRING;
    }
    return makeIdPath( comps, aCount, comps.size() - aCount );
  }

  /**
   * Removes specified number of ending components.
   * <p>
   * If all components removed, returns an empty string.
   *
   * @param aIdPath String - an IDpath
   * @param aCount int - number of components to remove
   * @return String - remianing IDpath or the empty string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aCount < 0
   * @throws TsIllegalArgumentRtException aIdPath is not an IDpath
   * @throws TsIllegalArgumentRtException aCount > number of components in argument
   */
  public static String removeTailingIdNames( String aIdPath, int aCount ) {
    TsIllegalArgumentRtException.checkTrue( aCount < 0 );
    IStringList comps = getComponents( aIdPath );
    TsIllegalArgumentRtException.checkTrue( comps.size() < aCount );
    if( comps.size() == aCount ) {
      return TsLibUtils.EMPTY_STRING;
    }
    return makeIdPath( comps, 0, comps.size() - aCount );
  }

  // ------------------------------------------------------------------------------------
  // IStridable formatted output to the string
  //

  /**
   * Format string: {@link IStridable#nmName()}.
   */
  public static final String FORMAT_NAME = "%2$s"; //$NON-NLS-1$

  /**
   * Format string: {@link IStridable#id()}
   */
  public static final String FORMAT_ID = "%s"; //$NON-NLS-1$

  /**
   * Format string: {@link IStridable#description()}
   */
  public static final String FORMAT_DESCRIPTION = "%3$s"; //$NON-NLS-1$

  /**
   * Format string: "{@link IStridable#id()} - {@link IStridable#nmName()}"
   */
  public static final String FORMAT_ID_NAME = "%s - %3$s"; //$NON-NLS-1$

  /**
   * Format string: "{@link IStridable#nmName()} ({@link IStridable#id()})".
   */
  public static final String FORMAT_NAME_ID = "%2$s (%1$s)"; //$NON-NLS-1$

  /**
   * Format string: "{@link IStridable#id()} - {@link IStridable#description()}"
   */
  public static final String FORMAT_ID_DESCRITPTION = "%s - %3$s"; //$NON-NLS-1$

  /**
   * Format string: "{@link IStridable#description()} ({@link IStridable#id()})"
   */
  public static final String FORMAT_DESCRIPTION_ID = "%3$s (%1$s)"; //$NON-NLS-1$

  /**
   * Format string: "{@link IStridable#id()} - {@link IStridable#nmName()} ({@link IStridable#description()})"
   */
  public static final String FORMAT_ID_NAME_DESCRIPTION = "%1$s - %2$s (%3$s)"; //$NON-NLS-1$

  /**
   * Format string: "{@link IStridable#nmName()} ({@link IStridable#description()})"
   */
  public static final String FORMAT_NAME_DESCRIPTION = "%2$s (%3$s)"; //$NON-NLS-1$

  /**
   * Returns string with {@link IStridable} entity formated with {@link String#format(String, Object...)} method.
   * <p>
   * Arguments are passed in the following order: {@link IStridable#id() id()}, {@link IStridable#nmName() nmName()},
   * {@link IStridable#description() description()}.
   * <p>
   * Either on of the predefined <code>FORMAT_XXX</code> strings may be used as <code>aFmtString</code> argument or
   * format string with indexed access via "%<b>n$</b>s" may be specified by user.
   *
   * @param aFmtString String - A <a href="../util/Formatter.html#syntax">format string</a>
   * @param aEntity {@link IStridable} - {@link IStridable} entity
   * @return String - A formatted string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static String printf( String aFmtString, IStridable aEntity ) {
    TsNullArgumentRtException.checkNulls( aFmtString, aEntity );
    return String.format( aFmtString, aEntity.id(), aEntity.nmName(), aEntity.description() );
  }

  /**
   * Prohibition of descendants creation.
   */
  private StridUtils() {
    // nop
  }

}
