package org.toxsoft.core.tslib.math.lexan;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Constants of the simple formula lexical analyzer {@link LexicalAnalyzer}.
 * <p>
 * To use the formula lexical analyzer the instance of the {@link LexicalAnalyzer} must be configured and created. Then
 * {@link LexicalAnalyzer#tokenize(String)} may be used for formulas parsing. = <code>true</code>.
 * <p>
 * Here is the list of token kind IDs ({@link ILexanToken#kindId()}) recognized by the analyzer. Some token kinds
 * recognition is optional and may be specified in constructor
 * {@link LexicalAnalyzer#LexicalAnalyzer(IOptionSet, String)} using the option definitions below.
 *
 * @author hazard157
 */
public interface ILexanConstants {

  /**
   * Token ID: EOF, the successful end of input.<br>
   * No methods of {@link ILexanToken} have a sense.
   */
  String TKID_EOF = "tk.eof"; //$NON-NLS-1$

  /**
   * Token ID: error occurred, no more analysis. <br>
   * {@link ILexanToken} must be casted to {@link TkError} to use it's API.
   */
  String TKID_ERROR = "tk.error"; //$NON-NLS-1$

  /**
   * Token ID: single character token (chars are specified in {@link LexicalAnalyzer} constructor.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_SINGLE_CHAR = "tk.single_char"; //$NON-NLS-1$

  /**
   * Token ID: left round bracket '('.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_ROUND_LEFT = "tk.br_round_l"; //$NON-NLS-1$

  /**
   * Token ID: right round bracket ')'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_ROUND_RIGHT = "tk.br_round_r"; //$NON-NLS-1$

  /**
   * Token ID: left square bracket '['.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_SQUARE_LEFT = "tk.br_square_l"; //$NON-NLS-1$

  /**
   * Token ID: right square bracket ']'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_SQUARE_RIGHT = "tk.br_square_r"; //$NON-NLS-1$

  /**
   * Token ID: left curly bracket '{'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_CURLY_LEFT = "tk.br_curly_l"; //$NON-NLS-1$

  /**
   * Token ID: right curly bracket '&lt;'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_CURLY_RIGHT = "tk.br_curly_r"; //$NON-NLS-1$

  /**
   * Token ID: left triangle bracket '&gt;'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_TRIANGLE_LEFT = "tk.br_triangle_l"; //$NON-NLS-1$

  /**
   * Token ID: right triangle bracket ')'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_BRACKET_TRIANGLE_RIGHT = "tk.br_triangle_r"; //$NON-NLS-1$

  /**
   * Token ID: the integer or floating number, represented as a <b><code>double</code></b>.<br>
   * {@link ILexanToken#number()} returns the number.
   */
  String TKID_NUMBER = "tk.number"; //$NON-NLS-1$

  /**
   * Token ID: the quoted string.<br>
   * {@link ILexanToken#str()} returns the unquoted string.
   */
  String TKID_QSTRING = "tk.qstring"; //$NON-NLS-1$

  /**
   * Token ID: the keyword (an IDpath).<br>
   * {@link ILexanToken#str()} returns the keyword.
   */
  String TKID_KEYWORD = "tk.keyword"; //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow IDpaths, not only IDnames as a keyword.
   */
  IDataDef OPDEF_IS_IDPATHS_ALLOWED = DataDef.ofBoolFlag( "isIdPathsAllowed", true ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow round bracket chars '<b>(</b>' '<b>)</b>'as tokens
   * {@link #TKID_BRACKET_ROUND_LEFT} and {@link #TKID_BRACKET_ROUND_RIGHT}.
   */
  IDataDef OPDEF_USE_ROUND_BRACKETS = DataDef.ofBoolFlag( "useRoundBrackets", true ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow square bracket chars '<b>[</b>' '<b>]</b>'as tokens
   * {@link #TKID_BRACKET_SQUARE_LEFT} and {@link #TKID_BRACKET_SQUARE_RIGHT}.
   */
  IDataDef OPDEF_USE_SQUARE_BRACKETS = DataDef.ofBoolFlag( "useSquareBrackets", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow curly bracket chars '<b>{</b>' '<b>}</b>'as tokens
   * {@link #TKID_BRACKET_CURLY_LEFT} and {@link #TKID_BRACKET_CURLY_RIGHT}.
   */
  IDataDef OPDEF_USE_CURLY_BRACKETS = DataDef.ofBoolFlag( "useCurlyBrackets", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow triangle bracket chars '<b>&lt;</b>' '<b>&gt;</b>'as tokens
   * {@link #TKID_BRACKET_TRIANGLE_LEFT} and {@link #TKID_BRACKET_TRIANGLE_RIGHT}.
   */
  IDataDef OPDEF_USE_TRIANGLE_BRACKETS = DataDef.ofBoolFlag( "useTrianleBrackets", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow quoted strings as a token {@link #TKID_QSTRING}.
   */
  IDataDef OPDEF_USE_QSTRING = DataDef.ofBoolFlag( "useQstring", false ); //$NON-NLS-1$

  /**
   * Returns token ID for the specified bracket symbol.
   *
   * @param aCh char - bracket symbol
   * @return String - one of the <code>TKID_BRACKET_XXX</code> constant
   * @throws TsIllegalArgumentRtException argument is not a bracket symbol
   */
  static String getBracketTokenId( char aCh ) {
    return switch( aCh ) {
      case '(' -> TKID_BRACKET_ROUND_LEFT;
      case ')' -> TKID_BRACKET_ROUND_RIGHT;
      case '[' -> TKID_BRACKET_SQUARE_LEFT;
      case ']' -> TKID_BRACKET_SQUARE_RIGHT;
      case '{' -> TKID_BRACKET_CURLY_LEFT;
      case '}' -> TKID_BRACKET_CURLY_RIGHT;
      case '<' -> TKID_BRACKET_TRIANGLE_LEFT;
      case '>' -> TKID_BRACKET_TRIANGLE_RIGHT;
      default -> throw new TsIllegalArgumentRtException();
    };
  }

}
