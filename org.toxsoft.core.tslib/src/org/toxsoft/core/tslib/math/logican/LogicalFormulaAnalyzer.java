package org.toxsoft.core.tslib.math.logican;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.logican.ILogicalFormulaConstants.*;
import static org.toxsoft.core.tslib.math.logican.ITsResources.*;
import static org.toxsoft.core.tslib.math.logicop.ILogicalOpConstants.*;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
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
  // private final IListEdit<ILexanToken> tokensList = new ElemArrayList<>();

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
   * Performs the lexical analysis of the formula.
   * <p>
   * Returned tokens may contain tokens only of the following kind IDs:
   * <ul>
   * <li>{@link ILexanConstants#TKID_EOF EOF} - only as the last token, indicates parsing success;</li>
   * <li>{@link ILexanConstants#TKID_ERROR ERROR} - only as the last token, indicates parsing error;</li>
   * <li>{@link ILexanConstants#TKID_SPACE SPACE} - spaces between tokens, retained to restore original formula;</li>
   * <li>{@link ILexanConstants#TKID_BRACKET_ROUND_LEFT BRACKET_ROUND_LEFT} - left round bracket;</li>
   * <li>{@link ILexanConstants#TKID_BRACKET_ROUND_RIGHT BRACKET_ROUND_RIGHT} - round bracket;</li>
   * <li>{@link ILexanConstants#TKID_KEYWORD KEYWORD} - an IDpath/IDname to be interpreted by the caller;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_NOT LOGICAL_NOT} - unary operation, logical inversion;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_AND LOGICAL_AND} - binary operation, logical AND;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_OR LOGICAL_OR} - binary operation, logical OR;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_XOR LOGICAL_XOR} - binary operation, logical XOR;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_TRUE LOGICAL_TRUE} - constant <code>true</code>;</li>
   * <li>{@link ILogicalFormulaConstants#TKID_LOGICAL_FALSE LOGICAL_FALSE} - constant <code><code>false</code>.</li>
   * </ul>
   * <p>
   *
   * @param aFormulaString String - the formula string
   * @return {@link IFormulaTokensEdit} - the analysis result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public IFormulaTokensEdit tokenize( String aFormulaString ) {
    // basic parsing returns tokens of kind: EOF, ERROR, SINGLE_CHAR (&|^!), KEYWORD, ROUND_BRACKET_LEFT/RIGHT
    IFormulaTokensEdit ft = lexicalAnalyzer.tokenize( aFormulaString );
    // substitute some tokens with specific logical operation tokens
    for( int i = 0; i < ft.tokens().size(); i++ ) {
      ILexanToken tkOrig = ft.tokens().get( i );
      ILexanToken tkSubstituted = switch( tkOrig.kindId() ) {
        case TKID_EOF -> tkOrig;
        case TKID_ERROR -> tkOrig;
        case TKID_SPACE -> tkOrig;
        case TKID_BRACKET_ROUND_LEFT -> tkOrig;
        case TKID_BRACKET_ROUND_RIGHT -> tkOrig;
        case TKID_SINGLE_CHAR -> switch( tkOrig.ch() ) {
          case CH_LOGICAL_AND -> TK_LOGICAL_AND;
          case CH_LOGICAL_OR -> TK_LOGICAL_OR;
          case CH_LOGICAL_XOR -> TK_LOGICAL_XOR;
          case CH_LOGICAL_NOT -> TK_LOGICAL_NOT;
          default -> new TkError(
              String.format( FMT_ERR_UNKNOWN_SINGLE_CHAR_TOKEN, Character.valueOf( tkOrig.ch() ) ) );
        };
        case TKID_KEYWORD -> switch( tkOrig.str().toUpperCase() ) {
          case KW_LOGICAL_AND -> TK_LOGICAL_AND;
          case KW_LOGICAL_OR -> TK_LOGICAL_OR;
          case KW_LOGICAL_XOR -> TK_LOGICAL_XOR;
          case KW_LOGICAL_NOT -> TK_LOGICAL_NOT;
          case KW_TRUE -> TK_LOGICAL_TRUE;
          case KW_FALSE -> TK_LOGICAL_FALSE;
          default -> tkOrig; // other keywords does not need to be substituted
        };
        default -> new TkError( String.format( FMT_ERR_UNKNOWN_TOKEN_KIND, tkOrig.kindId() ) );
      };
      ft.replaceToken( i, tkSubstituted );
    }
    return ft;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

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
