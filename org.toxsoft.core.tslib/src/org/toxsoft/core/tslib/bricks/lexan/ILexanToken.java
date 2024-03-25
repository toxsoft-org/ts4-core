package org.toxsoft.core.tslib.bricks.lexan;

import org.toxsoft.core.tslib.bricks.lexan.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Syntactic token of a simple formula.
 * <p>
 * The formula analyzer {@link LexicalAnalyzer#nextToken()} returns these tokens as it analyzes the formula text
 * string.
 *
 * @author hazard157
 */
public interface ILexanToken {

  /**
   * Returns the token kind.
   *
   * @return String - the token kind ID
   */
  String kindId();

  /**
   * Returns the textual representation of the token.
   *
   * @return String - token as a {@link String}
   */
  String str();

  /**
   * Returns the symbol making the token.
   *
   * @return char - the single symbol of token
   * @throws TsUnsupportedFeatureRtException token is not a single char token
   */
  char ch();

  /**
   * Returns the number.
   *
   * @return double - the number
   * @throws TsUnsupportedFeatureRtException token is not a number
   */
  double number();

  /**
   * Determines if this token is a terminal token.
   *
   * @return boolean - the flag that analysis was finished
   */
  boolean isTerminal();

  /**
   * Returns the token positioning in the formula string.
   * <p>
   * Sequence of the {@link #formulaSubstring()} make the whole formula string.
   *
   * @return {@link FormulaSubstring} - token as a formula substring
   */
  FormulaSubstring formulaSubstring();

}
