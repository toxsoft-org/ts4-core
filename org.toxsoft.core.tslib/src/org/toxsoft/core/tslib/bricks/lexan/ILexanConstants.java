package org.toxsoft.core.tslib.bricks.lexan;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.lexan.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.math.mathops.*;

/**
 * Constants of the simple formula lexical analyzer {@link LexicalAnalyzer}.
 * <p>
 * To use the formula lexical analyzer the instance of the {@link LexicalAnalyzer} must be created for each formula.
 * Then {@link LexicalAnalyzer#nextToken()} must be called until any terminating token {@link ILexanToken#isTerminal()}
 * = <code>true</code>.
 * <p>
 * Here is listen token kind IDs ({@link ILexanToken#kindId()}) recognized by the analyzer. Some token kinds recognition
 * is optional and may be specified in constructor
 * {@link LexicalAnalyzer#LexicalAnalyzer(String, IOptionSet, ILexanKeywordSubstituter)} using the option definitions
 * below.
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
   * Token ID: comma ','.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_COMMA = "tk.comma"; //$NON-NLS-1$

  /**
   * Token ID: semicolon ';'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_SEMICOLON = "tk.semicolon"; //$NON-NLS-1$

  /**
   * Token ID: colon ':'.<br>
   * {@link ILexanToken#ch()} returns the character.
   */
  String TKID_COLON = "tk.colon"; //$NON-NLS-1$

  /**
   * Token ID: {@link EMathBinaryOp} as a single character.<br>
   * {@link ILexanToken#ch()} returns the character {@link EMathBinaryOp#opChar()}.
   */
  String TKID_MATH_OP = "tk.math_op"; //$NON-NLS-1$

  /**
   * Token ID: {@link ELogicalOp} as a single character.<br>
   * {@link ILexanToken#ch()} returns the character {@link ELogicalOp#opChar()}.
   */
  String TKID_LOGICAL_OP = "tk.logical_op"; //$NON-NLS-1$

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
   * {@link LexicalAnalyzer} option: allow comma char '<b>,</b>'as a token {@link #TKID_COMMA}.
   */
  IDataDef OPDEF_USE_COMMA = DataDef.ofBoolFlag( "useComma", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow colon char '<b>:</b>'as a token {@link #TKID_COLON}.
   */
  IDataDef OPDEF_USE_COLON = DataDef.ofBoolFlag( "useColon", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow semicolon char '<b>;</b>'as a token {@link #TKID_SEMICOLON}.
   */
  IDataDef OPDEF_USE_SEMICOLON = DataDef.ofBoolFlag( "useSemicolon", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow {@link EMathBinaryOp#opChar()} as a token {@link #TKID_LOGICAL_OP}.
   */
  IDataDef OPDEF_USE_MATH_BINARY_OPS = DataDef.ofBoolFlag( "useMathBinaryOps", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow {@link ELogicalOp#opChar()} as a token {@link #TKID_LOGICAL_OP}.
   */
  IDataDef OPDEF_USE_LOGICAL_OP_CHARS = DataDef.ofBoolFlag( "useLogicalOpChars", false ); //$NON-NLS-1$

  /**
   * {@link LexicalAnalyzer} option: allow quoted strings as a token {@link #TKID_QSTRING}.
   */
  IDataDef OPDEF_USE_QSTRING = DataDef.ofBoolFlag( "useQstring", false ); //$NON-NLS-1$

}
