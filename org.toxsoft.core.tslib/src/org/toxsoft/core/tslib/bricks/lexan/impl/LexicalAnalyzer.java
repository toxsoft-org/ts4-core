package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.bricks.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.bricks.lexan.impl.ITsResources.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import java.security.spec.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.lexan.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.chario.*;
import org.toxsoft.core.tslib.bricks.strio.chario.impl.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.math.mathops.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple formula string lexical analyzer.
 * <p>
 * The analyzer sequentially goes through the formula string (given in the constructor) and returns the next token using
 * the {@link #nextToken()} method. The analyzer is intended for one-time use. To re-parse a formula, you need to create
 * a new instance of this analyzer.
 *
 * @author hazard157
 */
public class LexicalAnalyzer {

  private final String                   formulaStr;
  private final ILexanKeywordSubstituter keywordSubstituter;

  private final IStrioReader sr;

  private final IListEdit<TkSingleChar>   singleCharTokens = new ElemArrayList<>();
  private final IStringMapEdit<TkKeyword> cachedKeywords   = new StringMap<>();

  /**
   * The last token in the formula and at the same time non-<code>null</code> is a sign of completion of the analysis.
   * <p>
   * In formulas without errors, the last token is always of type {@link ILexanConstants#TKID_EOF}. If in the formula
   * there was an error, the last token is always of type {@link ILexanConstants#TKID_ERROR}.
   * <p>
   * Once the value of this field is set to non-<code>null</code>, all subsequent calls to {@link #nextToken()} fail
   * perform the analysis and return this final token.
   */
  private AbstractLexanToken finalToken = null;

  private boolean isIdPathsAllowed;
  private boolean isQStringsUsed;

  private int numOfRoundBrackets    = 0;
  private int numOfSquareBrackets   = 0;
  private int numOfCurlyBrackets    = 0;
  private int numOfTrianlgeBrackets = 0;

  /**
   * Constructor.
   *
   * @param aFormula String - the formula string to by analyzed
   * @param aOps {@link IOptionSet} - the analysis options as listed in {@link ILexanConstants}
   * @param aSubstituter {@link InvalidKeySpecException} - keyword substituter
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public LexicalAnalyzer( String aFormula, IOptionSet aOps, ILexanKeywordSubstituter aSubstituter ) {
    TsNullArgumentRtException.checkNulls( aFormula, aOps, aSubstituter );
    formulaStr = aFormula;
    keywordSubstituter = aSubstituter;
    ICharInputStream chIn = new CharInputStreamString( formulaStr );
    sr = new StrioReader( chIn );
    isIdPathsAllowed = OPDEF_IS_IDPATHS_ALLOWED.getValue( aOps ).asBool();
    isQStringsUsed = OPDEF_USE_QSTRING.getValue( aOps ).asBool();
    init( aOps );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void init( IOptionSet aOps ) {
    if( OPDEF_USE_ROUND_BRACKETS.getValue( aOps ).asBool() ) {
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_ROUND_LEFT, '(' ) );
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_ROUND_RIGHT, ')' ) );
    }
    if( OPDEF_USE_SQUARE_BRACKETS.getValue( aOps ).asBool() ) {
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_SQUARE_LEFT, '[' ) );
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_SQUARE_RIGHT, ']' ) );
    }
    if( OPDEF_USE_CURLY_BRACKETS.getValue( aOps ).asBool() ) {
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_CURLY_LEFT, '{' ) );
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_CURLY_RIGHT, '}' ) );
    }
    if( OPDEF_USE_TRIANGLE_BRACKETS.getValue( aOps ).asBool() ) {
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_TRIANGLE_LEFT, '<' ) );
      singleCharTokens.add( new TkSingleChar( TKID_BRACKET_TRIANGLE_LEFT, '>' ) );
    }
    if( OPDEF_USE_COMMA.getValue( aOps ).asBool() ) {
      singleCharTokens.add( new TkSingleChar( TKID_COMMA, ',' ) );
    }
    if( OPDEF_USE_COLON.getValue( aOps ).asBool() ) {
      singleCharTokens.add( new TkSingleChar( TKID_COLON, ':' ) );
    }
    if( OPDEF_USE_SEMICOLON.getValue( aOps ).asBool() ) {
      singleCharTokens.add( new TkSingleChar( TKID_SEMICOLON, ';' ) );
    }
    if( OPDEF_USE_MATH_BINARY_OPS.getValue( aOps ).asBool() ) {
      for( EMathBinaryOp mop : EMathBinaryOp.asList() ) {
        TsInternalErrorRtException.checkNoNull( findSingleCharToken( mop.opChar() ) );
        singleCharTokens.add( new TkSingleChar( TKID_MATH_OP, mop.opChar() ) );
      }
    }
    if( OPDEF_USE_LOGICAL_OP_CHARS.getValue( aOps ).asBool() ) {
      for( ELogicalOp lop : ELogicalOp.asList() ) {
        TsInternalErrorRtException.checkNoNull( findSingleCharToken( lop.opChar() ) );
        singleCharTokens.add( new TkSingleChar( TKID_LOGICAL_OP, lop.opChar() ) );
      }
    }
  }

  private TkSingleChar findSingleCharToken( char aCh ) {
    for( TkSingleChar t : singleCharTokens ) {
      if( t.ch() == aCh ) {
        return t;
      }
    }
    return null;
  }

  private AbstractLexanToken internalNextToken() {
    if( finalToken != null ) { // analysis was finished (either EOF or ERROR)
      return finalToken;
    }
    char ch = sr.nextChar( EStrioSkipMode.SKIP_SPACES );
    // ---------------------------------------------
    // check for any single char token
    if( ch == CHAR_EOF ) {
      finalToken = new TkEof();
      return finalToken;
    }
    TkSingleChar tc = findSingleCharToken( ch );
    if( tc != null ) {
      return tc;
    }
    sr.putCharBack();
    // ---------------------------------------------
    // is this a number?
    if( StrioUtils.isAsciiDigit( ch ) ) {
      try {
        return new TkNumber( sr.readDouble() );
      }
      catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
        finalToken = new TkError( currPos(), MSG_ERR_NUMBER_WAS_EXPECTED );
        return finalToken;
      }
    }
    // ---------------------------------------------
    // is this an allowed quoted string?
    if( isQStringsUsed ) {
      if( ch == CHAR_QUOTE ) {
        try {
          return new TkQString( sr.readQuotedString() );
        }
        catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
          finalToken = new TkError( currPos(), MSG_ERR_QSRTING_WAS_EXPECTED );
          return finalToken;
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
    }
    catch( @SuppressWarnings( "unused" ) Exception ex ) {
      if( isIdPathsAllowed ) {
        finalToken = new TkError( currPos(), MSG_ERR_IDPATH_WAS_EXPECTED );
      }
      else {
        finalToken = new TkError( currPos(), MSG_ERR_IDNAME_WAS_EXPECTED );
      }
      return finalToken;
    }
    TkKeyword tk = cachedKeywords.findByKey( kwStr );
    if( tk == null ) {
      tk = new TkKeyword( kwStr );
      cachedKeywords.put( kwStr, tk );
    }
    return tk;
  }

  private AbstractLexanToken checkForUnmatchedOpeningBracket( AbstractLexanToken aToken, int aNum, String aErrorMsg ) {
    if( aNum > 0 ) {
      finalToken = new TkError( aToken.formulaSubstring().start(), aErrorMsg );
      finalToken.setFormulaSubstring( aToken.formulaSubstring() );
      return finalToken;
    }
    return aToken;
  }

  private AbstractLexanToken checkForUnmatchedClosingBracket( AbstractLexanToken aToken, int aNum, String aErrorMsg ) {
    if( aNum < 0 ) {
      finalToken = new TkError( aToken.formulaSubstring().start(), aErrorMsg );
      finalToken.setFormulaSubstring( aToken.formulaSubstring() );
      return finalToken;
    }
    return aToken;
  }

  private AbstractLexanToken checkBracketsIntegrity( AbstractLexanToken aToken ) {
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
        AbstractLexanToken t;
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
   * Analyzes the formula and returns the next token.
   * <p>
   * When a syntax error occurs, it does not throw an exception, but returns the corresponding information in the form
   * of a {@link ILexanToken} token of the form {@link ILexanConstants#TKID_ERROR}.
   * <p>
   * After parsing is completed, or after an error, further calls to the method return the same value - the last token.
   *
   * @return {@link ILexanToken} - next token or last token when analysis is finished
   */
  public ILexanToken nextToken() {
    // get next token
    int prevPos = currPos();
    AbstractLexanToken tk = internalNextToken();
    int currPos = currPos();
    if( tk.kindId().equals( TKID_EOF ) ) { // only EOF goes behind the formula string
      currPos = prevPos;
    }
    String subs = formulaStr.substring( prevPos, currPos );
    FormulaSubstring fs = new FormulaSubstring( formulaStr, subs, prevPos, currPos );
    tk.setFormulaSubstring( fs );
    // substitute keyword if needed
    if( tk.kindId().equals( TKID_KEYWORD ) ) {
      tk = keywordSubstituter.substituteKeyword( tk );
      tk.setFormulaSubstring( fs );
      if( tk.isTerminal() ) {
        finalToken = tk;
        return finalToken;
      }
    }
    // check brackets
    tk = checkBracketsIntegrity( tk );
    if( tk.isTerminal() ) {
      tk.setFormulaSubstring( fs );
      finalToken = tk;
      return finalToken;
    }
    // anyway, formula substring represents original substring
    tk.setFormulaSubstring( fs );
    return tk;
  }

  /**
   * Returns the index of the current symbol when parsing a formula.
   * <p>
   * Immediately after creating the analyzer, the current position is set to 0. Upon normal completion, the current
   * position indicates after the last character in the line. If there is a parsing error, the current position points
   * to the first character, where the analysis stopped.
   *
   * @return int - character index in the formula line (starts at 0)
   */
  public int currPos() {
    return sr.currentPosition();
  }

}
