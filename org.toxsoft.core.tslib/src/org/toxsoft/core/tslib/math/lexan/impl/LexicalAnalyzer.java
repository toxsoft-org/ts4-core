package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.lexan.impl.ITsResources.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The lexical analyzer - splits formula string into individual tokens.
 * <p>
 * Once configured and created, the {@link #tokenize(String)} method may be called multiple time.
 * <p>
 * Few important notes of analyzer implementation:
 * <ul>
 * <li>any symbol in quoted string becomes the part of the token {@link ILexanConstants#TKID_QSTRING}, the rules below
 * does not allies to them;</li>
 * <li>any char symbol while reading IDpath/IDname keyword becomes the part of the token
 * {@link ILexanConstants#TKID_KEYWORD}, the rules below does not allies to them;</li>
 * <li>{@link IStrioHardConstants#DEFAULT_SPACE_CHARS} are recognized only as a token delimiters;</li>
 * <li>all char symbols {@link #singleChars} are tokens and token delimiter at the same time;</li>
 * <li>all bracket symbols {@link #bracketChars} are tokens and token delimiter at the same time;</li>
 * <li>numbers must start with a decimal digit and follow {@link Double#parseDouble(String)} conventions;</li>
 * <li>all other symbols will lead to error.</li>
 * </ul>
 * When creating tokens sequence from a scratch, tokens {@link ILexanConstants#TKID_KEYWORD KEYWORD},
 * {@link ILexanConstants#TKID_NUMBER NUMBER} and any user-defind alphanumeric token (like AND, TRUE, CONST, etc) must
 * be correctly separated from token before and after.
 *
 * @author hazard157
 */
public class LexicalAnalyzer {

  /**
   * Single characters to be recognized as a token.
   * <p>
   * Characters are specified in constructor.
   */
  private final String singleChars;

  /**
   * Bracket characters to be recognized as a token of kind {@link ILexanConstants}<code>.TKID_BRACKET_XXX</code>.
   * <p>
   * Which brackets will be recognized depends on options {@link ILexanConstants}<code>.OPDEF_USE_XXX_BRACKETS</code>.
   */
  private final String bracketChars;

  private final boolean isQStringsUsed;
  private boolean       isIdPathsAllowed;

  /**
   * Formula string reader is initialized and deinitialized in {@link #tokenize(String)}.
   */
  private IStrioReader sr = null;

  private int numOfRoundBrackets    = 0;
  private int numOfSquareBrackets   = 0;
  private int numOfCurlyBrackets    = 0;
  private int numOfTrianlgeBrackets = 0;

  /**
   * Constructor.
   *
   * @param aOps {@link IOptionSet} - configuration options from {@link ILexanConstants}<code>.OPDEF_XXX</code>
   * @param aSingleChars String - symbols recognized as a token of kind {@link ILexanConstants#TKID_SINGLE_CHAR}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public LexicalAnalyzer( IOptionSet aOps, String aSingleChars ) {
    TsNullArgumentRtException.checkNulls( aOps, aSingleChars );
    singleChars = aSingleChars;
    isIdPathsAllowed = OPDEF_IS_IDPATHS_ALLOWED.getValue( aOps ).asBool();
    isQStringsUsed = OPDEF_USE_QSTRING.getValue( aOps ).asBool();
    // initialize #bracketChars
    StringBuilder sb = new StringBuilder();
    if( OPDEF_USE_ROUND_BRACKETS.getValue( aOps ).asBool() ) {
      sb.append( "()" ); //$NON-NLS-1$
    }
    if( OPDEF_USE_SQUARE_BRACKETS.getValue( aOps ).asBool() ) {
      sb.append( "[]" ); //$NON-NLS-1$
    }
    if( OPDEF_USE_CURLY_BRACKETS.getValue( aOps ).asBool() ) {
      sb.append( "{}" ); //$NON-NLS-1$
    }
    if( OPDEF_USE_TRIANGLE_BRACKETS.getValue( aOps ).asBool() ) {
      sb.append( "<>" ); //$NON-NLS-1$
    }
    bracketChars = sb.toString();
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private LexanToken internalNextToken() {
    int startIndex = sr.currentPosition();
    char ch = sr.nextChar( EStrioSkipMode.SKIP_SPACES );
    // check for EOF
    if( ch == CHAR_EOF ) {
      return new TkEof( startIndex );
    }
    // check for recognized brackets
    if( bracketChars.indexOf( ch ) >= 0 ) {
      return new TkSingleChar( getBracketTokenId( ch ), ch, startIndex );
    }
    // check for any other single char token
    if( singleChars.indexOf( ch ) >= 0 ) {
      return new TkSingleChar( TKID_SINGLE_CHAR, ch, startIndex );
    }
    sr.putCharBack();
    // ---------------------------------------------
    // is this a number?
    if( StrioUtils.isAsciiDigit( ch ) ) {
      try {
        return new TkNumber( sr.readDouble(), startIndex );
      }
      catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
        return new TkError( MSG_ERR_NUMBER_WAS_EXPECTED, startIndex );
      }
    }
    // ---------------------------------------------
    // is this an allowed quoted string?
    if( isQStringsUsed ) {
      if( ch == CHAR_QUOTE ) {
        try {
          return new LexanToken( TKID_QSTRING, sr.readQuotedString(), startIndex );
        }
        catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
          return new TkError( MSG_ERR_QSRTING_WAS_EXPECTED, startIndex );
        }
      }
    }
    // ---------------------------------------------
    // here must start an IDpath/IDname keyword
    String kwStr;
    try {
      if( isIdPathsAllowed ) {
        kwStr = sr.readIdPath();
      }
      else {
        kwStr = sr.readIdName();
      }
      return new LexanToken( TKID_KEYWORD, kwStr, startIndex );
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      return new TkError( isIdPathsAllowed ? MSG_ERR_IDPATH_WAS_EXPECTED : MSG_ERR_IDNAME_WAS_EXPECTED, startIndex );
    }
  }

  private static LexanToken checkForUnmatchedOpeningBracket( LexanToken aToken, int aNum, String aErrorMsg ) {
    if( aNum > 0 ) {
      return new TkError( aErrorMsg, aToken.startIndex() );
    }
    return aToken;
  }

  private static LexanToken checkForUnmatchedClosingBracket( LexanToken aToken, int aNum, String aErrorMsg ) {
    if( aNum < 0 ) {
      return new TkError( aErrorMsg, aToken.startIndex() );
    }
    return aToken;
  }

  private LexanToken checkBracketsIntegrity( LexanToken aToken ) {
    switch( aToken.kindId() ) {
      case TKID_BRACKET_ROUND_LEFT: {
        ++numOfRoundBrackets;
        return aToken;
      }
      case TKID_BRACKET_ROUND_RIGHT: {
        --numOfRoundBrackets;
        return checkForUnmatchedClosingBracket( aToken, numOfRoundBrackets, MSG_ERR_LONE_BRACKET_ROUND_R );
      }
      case TKID_BRACKET_SQUARE_LEFT: {
        ++numOfSquareBrackets;
        return aToken;
      }
      case TKID_BRACKET_SQUARE_RIGHT: {
        --numOfSquareBrackets;
        return checkForUnmatchedClosingBracket( aToken, numOfSquareBrackets, MSG_ERR_LONE_BRACKET_SQUARE_R );
      }
      case TKID_BRACKET_CURLY_LEFT: {
        ++numOfCurlyBrackets;
        return aToken;
      }
      case TKID_BRACKET_CURLY_RIGHT: {
        --numOfCurlyBrackets;
        return checkForUnmatchedClosingBracket( aToken, numOfCurlyBrackets, MSG_ERR_LONE_BRACKET_CURLY_R );
      }
      case TKID_BRACKET_TRIANGLE_LEFT: {
        ++numOfTrianlgeBrackets;
        return aToken;
      }
      case TKID_BRACKET_TRIANGLE_RIGHT: {
        --numOfTrianlgeBrackets;
        return checkForUnmatchedClosingBracket( aToken, numOfCurlyBrackets, MSG_ERR_LONE_BRACKET_TRIANGLE_R );
      }
      case TKID_EOF: {
        LexanToken t;
        t = checkForUnmatchedOpeningBracket( aToken, numOfRoundBrackets, MSG_ERR_LONE_BRACKET_ROUND_L );
        if( t != aToken ) {
          return t;
        }
        t = checkForUnmatchedOpeningBracket( aToken, numOfSquareBrackets, MSG_ERR_LONE_BRACKET_SQUARE_L );
        if( t != aToken ) {
          return t;
        }
        t = checkForUnmatchedOpeningBracket( aToken, numOfCurlyBrackets, MSG_ERR_LONE_BRACKET_CURLY_L );
        if( t != aToken ) {
          return t;
        }
        t = checkForUnmatchedOpeningBracket( aToken, numOfTrianlgeBrackets, MSG_ERR_LONE_BRACKET_TRIANGLE_L );
        if( t != aToken ) {
          return t;
        }
        return aToken;
      }
      default:
        return aToken;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Splits a formula string into individual tokens.
   * <p>
   * Last element of the returned list contains the only terminal token either EOF or ERROR.
   *
   * @param aFormulaString String - the formula string
   * @return {@link IListEdit}&lt;{@link ILexanToken}&gt; - the tokens making the formula
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IListEdit<ILexanToken> tokenize( String aFormulaString ) {
    // initialize reader
    numOfRoundBrackets = 0;
    numOfSquareBrackets = 0;
    numOfCurlyBrackets = 0;
    numOfTrianlgeBrackets = 0;
    ICharInputStream chIn = new CharInputStreamString( aFormulaString );
    sr = new StrioReader( chIn );
    // parse formula
    IListEdit<ILexanToken> ll = new ElemArrayList<>();
    LexanToken tk;
    do {
      tk = internalNextToken();
      // check brackets integrity, on error #tk becomes the error token
      tk = checkBracketsIntegrity( tk );
      ll.add( tk );
    } while( !tk.isFinisher() );
    sr = null;
    return ll;
  }

  public IList<ILexanToken> getTokens() {

  }

  /**
   * Returns the token substrings in last parsed formula.
   *
   * @return {@link IStringList} - substrings making the tokens {@link #getTokens()}
   */
  public IStringList getSubStrings() {

  }

}
