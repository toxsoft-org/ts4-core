package org.toxsoft.core.tslib.math.logican;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.logican.ILogicalFormulaConstants.*;
import static org.toxsoft.core.tslib.math.logicop.ILogicalOpConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Lexical analyzer of the logical expression (formula).
 *
 * @author hazard157
 */
public class LogicalFormulaAnalyzer {

  private final LexicalAnalyzer lexicalAnalyzer;

  private boolean recognizeBooleanConstants = false;

  /**
   * Constructor.
   */
  public LogicalFormulaAnalyzer() {
    IOptionSetEdit ops = new OptionSet();
    OPDEF_USE_ROUND_BRACKETS.setValue( ops, AV_TRUE );
    OPDEF_IS_IDPATHS_ALLOWED.setValue( ops, AV_TRUE );
    lexicalAnalyzer = new LexicalAnalyzer( ops, RECOGNIZED_CHARS );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Splits a formula string into individual tokens.
   * <p>
   * Returned list may contain tokens only of the following kind IDs:
   * <ul>
   * <li>{@link ILexanConstants#TKID_EOF} - only as the last token, indicates parsing success;</li>
   * <li>{@link ILexanConstants#TKID_ERROR} - only as the last token, indicates parsing error;</li>
   * <li>{@link ILexanConstants#TKID_BRACKET_ROUND_LEFT} - left round bracket;</li>
   * <li>{@link ILexanConstants#TKID_BRACKET_ROUND_RIGHT} - round bracket;</li>
   * <li>{@link ILexanConstants#TKID_KEYWORD} - an IDpath/IDname to be interpreted by the caller;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_NOT} - unary operation, logical inversion;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_AND} - binary operation, logical AND;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_OR} - binary operation, logical OR;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_XOR} - binary operation, logical XOR;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_TRUE} - constant <code>true</code>;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_FALSE} - constant <code><code>false</code> true</code>.</li>
   * </ul>
   * <p>
   * Last element of the returned list contains the only terminal token either EOF or ERROR.
   *
   * @param aFormulaString String - the formula string
   * @return {@link IListEdit}&lt;{@link ILexanToken}&gt; - the tokens making the formula
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IListEdit<ILexanToken> tokenize( String aFormulaString ) {
    // basic parsing returns tokens of kind: EOF, ERROR, SINGLE_CHAR (&|^!), KEYWORD, ROUND_BRACKET_LEFT/RIGHT
    IListEdit<ILexanToken> llTokens = lexicalAnalyzer.tokenize( aFormulaString );
    // substitute some tokens with specific logical operation tokens
    for( int i = 0; i < llTokens.size(); i++ ) {
      ILexanToken tkOrig = llTokens.get( i );
      ILexanToken tkSubstituted = tkOrig;
      switch( tkOrig.kindId() ) {
        case TKID_EOF:
        case TKID_ERROR:
        case TKID_BRACKET_ROUND_LEFT:
        case TKID_BRACKET_ROUND_RIGHT: {
          // this tokens remain unchanged
          break;
        }
        case TKID_SINGLE_CHAR: {
          switch( tkOrig.ch() ) {
            case CH_LOGICAL_AND: {
              tkSubstituted = new TkSingleChar( TKID_LOGICAL_AND, tkOrig.ch(), tkOrig.startIndex() );
              break;
            }
            case CH_LOGICAL_OR: {
              tkSubstituted = new TkSingleChar( TKID_LOGICAL_OR, tkOrig.ch(), tkOrig.startIndex() );
              break;
            }
            case CH_LOGICAL_XOR: {
              tkSubstituted = new TkSingleChar( TKID_LOGICAL_XOR, tkOrig.ch(), tkOrig.startIndex() );
              break;
            }
            case CH_LOGICAL_NOT: {
              tkSubstituted = new TkSingleChar( TKID_LOGICAL_NOT, tkOrig.ch(), tkOrig.startIndex() );
              break;
            }
            default: // no other sinle-char tokens may exist in logical formula
              throw new TsInternalErrorRtException();
          }
          break;
        }
        case TKID_KEYWORD: {
          switch( tkOrig.str().toUpperCase() ) {
            case KW_LOGICAL_AND: {
              tkSubstituted = new LexanToken( TKID_LOGICAL_AND, KW_LOGICAL_AND, tkOrig.startIndex() );
              break;
            }
            case KW_LOGICAL_OR: {
              tkSubstituted = new LexanToken( TKID_LOGICAL_OR, KW_LOGICAL_OR, tkOrig.startIndex() );
              break;
            }
            case KW_LOGICAL_XOR: {
              tkSubstituted = new LexanToken( TKID_LOGICAL_XOR, KW_LOGICAL_XOR, tkOrig.startIndex() );
              break;
            }
            case KW_LOGICAL_NOT: {
              tkSubstituted = new LexanToken( TKID_LOGICAL_NOT, KW_LOGICAL_NOT, tkOrig.startIndex() );
              break;
            }
            case KW_TRUE: {
              if( recognizeBooleanConstants ) {
                tkSubstituted = new LexanToken( TKID_LOGICAL_TRUE, KW_TRUE, tkOrig.startIndex() );
              }
              break;
            }
            case KW_FALSE: {
              if( recognizeBooleanConstants ) {
                tkSubstituted = new LexanToken( TKID_LOGICAL_FALSE, KW_FALSE, tkOrig.startIndex() );
              }
              break;
            }
            default: {
              // other keywords does not need to be substituted
              break;
            }
          }
          break;
        }
        default:
          // no other token kind IDs may exist in logical formula
          throw new TsInternalErrorRtException();
      }
      llTokens.set( i, tkSubstituted );
    }
    return llTokens;
  }

  /**
   * Determines if boolean constant names are recognized.
   * <p>
   * Boolean constant names {@link ILogicalFormulaConstants#KW_FALSE} and {@link ILogicalFormulaConstants#KW_TRUE} are
   * recognized case insensitive.
   *
   * @return boolean - the sign that boolean constant names are recognized
   */
  public boolean isBooleanConstantsRecognized() {
    return recognizeBooleanConstants;
  }

  /**
   * Sets the value of {@link #isBooleanConstantsRecognized()}.
   * <p>
   * Changing this value takes effect on next {@link #tokenize(String)} call.
   *
   * @param aRecognize boolean - the sign that boolean constant names are recognized
   */
  public void setBooleanConstantsRecognized( boolean aRecognize ) {
    recognizeBooleanConstants = aRecognize;
  }

}
