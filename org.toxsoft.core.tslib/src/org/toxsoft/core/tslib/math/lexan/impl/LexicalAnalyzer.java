package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.lexan.impl.ITsResources.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
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
   * Cached instances of the single-char tokens corresponding to the chars from {@link #singleChars}
   */
  private final IIntMapEdit<ILexanToken> singleCharTokensCache = new IntMap<>();

  /**
   * Bracket characters to be recognized as a token of kind {@link ILexanConstants}<code>.TKID_BRACKET_XXX</code>.
   * <p>
   * Which brackets will be recognized depends on options {@link ILexanConstants}<code>.OPDEF_USE_XXX_BRACKETS</code>.
   */
  private final String bracketChars;

  private final boolean isQStringsUsed;
  private boolean       isIdPathsAllowed;

  /**
   * Formula string reader is initialized and de-initialized in {@link #tokenize(String)}.
   */
  private IStrioReader sr = null;

  private final IListEdit<ILexanToken> tokensList = new ElemArrayList<>();

  private String          formulaString = EMPTY_STRING;
  private IStringListEdit subStrings    = new StringArrayList();

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

  /**
   * Returns token for the specified bracket symbol.
   *
   * @param aCh char - bracket symbol
   * @return ILexanToken - one of the <code>TK_BRACKET_XXX</code> constant
   * @throws TsIllegalArgumentRtException argument is not a bracket symbol
   */
  static ILexanToken getBracketToken( char aCh ) {
    return switch( aCh ) {
      case '(' -> TK_BRACKET_ROUND_LEFT;
      case ')' -> TK_BRACKET_ROUND_RIGHT;
      case '[' -> TK_BRACKET_SQUARE_LEFT;
      case ']' -> TK_BRACKET_SQUARE_RIGHT;
      case '{' -> TK_BRACKET_CURLY_LEFT;
      case '}' -> TK_BRACKET_CURLY_RIGHT;
      case '<' -> TK_BRACKET_TRIANGLE_LEFT;
      case '>' -> TK_BRACKET_TRIANGLE_RIGHT;
      default -> throw new TsIllegalArgumentRtException();
    };
  }

  private ILexanToken internalNextToken() {
    char ch = sr.nextChar( EStrioSkipMode.SKIP_NONE );
    // bypass spaces
    if( DEFAULT_SPACE_CHARS.indexOf( ch ) >= 0 ) {
      StringBuilder sb = new StringBuilder();
      do {
        sb.append( ch );
        ch = sr.nextChar( EStrioSkipMode.SKIP_NONE );
      } while( DEFAULT_SPACE_CHARS.indexOf( ch ) >= 0 );
      sr.putCharBack();
      return new TkSpace( sb.toString() );
    }
    // check for EOF
    if( ch == CHAR_EOF ) {
      return TkEof.TK_EOF;
    }
    // check for recognized brackets
    if( bracketChars.indexOf( ch ) >= 0 ) {
      return getBracketToken( ch );
    }
    // check for any other single char token
    if( singleChars.indexOf( ch ) >= 0 ) {
      ILexanToken tk = singleCharTokensCache.findByKey( ch );
      if( tk == null ) {
        tk = new TkSingleChar( TKID_SINGLE_CHAR, ch );
        singleCharTokensCache.put( ch, tk );
      }
      return tk;
    }
    sr.putCharBack();
    // ---------------------------------------------
    // is this a number?
    if( StrioUtils.isAsciiDigit( ch ) ) {
      try {
        return new TkNumber( sr.readDouble() );
      }
      catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
        return new TkError( MSG_ERR_NUMBER_WAS_EXPECTED );
      }
    }
    // ---------------------------------------------
    // is this an allowed quoted string?
    if( isQStringsUsed ) {
      if( ch == CHAR_QUOTE ) {
        try {
          return new LexanToken( TKID_QSTRING, sr.readQuotedString() );
        }
        catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
          return new TkError( MSG_ERR_QSRTING_WAS_EXPECTED );
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
      return new LexanToken( TKID_KEYWORD, kwStr );
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      return new TkError( isIdPathsAllowed ? MSG_ERR_IDPATH_WAS_EXPECTED : MSG_ERR_IDNAME_WAS_EXPECTED );
    }
  }

  private static ILexanToken checkForUnmatchedOpeningBracket( ILexanToken aToken, int aNum, String aErrorMsg ) {
    if( aNum > 0 ) {
      return new TkError( aErrorMsg );
    }
    return aToken;
  }

  private static ILexanToken checkForUnmatchedClosingBracket( ILexanToken aToken, int aNum, String aErrorMsg ) {
    if( aNum < 0 ) {
      return new TkError( aErrorMsg );
    }
    return aToken;
  }

  private ILexanToken checkBracketsIntegrity( ILexanToken aToken ) {
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
        ILexanToken t;
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
   * Performs the lexical analysis of the formula.
   *
   * @param aFormulaString String - the formula string
   * @return {@link IFormulaTokensEdit} - the analysis result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IFormulaTokensEdit tokenize( String aFormulaString ) {
    TsNullArgumentRtException.checkNull( aFormulaString );
    formulaString = aFormulaString;
    // initialize reader
    numOfRoundBrackets = 0;
    numOfSquareBrackets = 0;
    numOfCurlyBrackets = 0;
    numOfTrianlgeBrackets = 0;
    ICharInputStream chIn = new CharInputStreamString( aFormulaString );
    sr = new StrioReader( chIn );
    tokensList.clear();
    subStrings.clear();
    // parse formula
    ILexanToken tk;
    do {
      int beginIndex = sr.currentPosition();
      tk = internalNextToken();
      int endIndex = sr.currentPosition();
      // check brackets integrity, on error #tk becomes the error token
      tk = checkBracketsIntegrity( tk );
      tokensList.add( tk );
      String subs;
      if( tk.isFinisher() ) {
        subs = formulaString.substring( beginIndex );
      }
      else {
        subs = formulaString.substring( beginIndex, endIndex );
      }
      subStrings.add( subs );
    } while( !tk.isFinisher() );
    sr = null;
    return new FormulaTokens( formulaString, tokensList, subStrings );
  }

}
