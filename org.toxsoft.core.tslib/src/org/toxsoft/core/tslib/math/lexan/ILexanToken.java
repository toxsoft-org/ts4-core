package org.toxsoft.core.tslib.math.lexan;

import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Syntactic token of a simple formula.
 * <p>
 * The formula analyzer {@link LexicalAnalyzer#tokenize(String)} returns these tokens as the resulting list.
 *
 * @author hazard157
 */
public sealed interface ILexanToken
    permits LexanToken {

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
   * Determines if the token denotes a keyword.
   * <p>
   * This is a simple check id {@link #kindId()} is equal to {@link ILexanConstants#TKID_KEYWORD}.
   *
   * @return boolean - <code>true</code> if token contains a keyword in {@link #str()}
   */
  boolean isKeyword();

  /**
   * Determines if this token is a finisher token, the last token.
   *
   * @return boolean - the flag that analysis was finished
   */
  boolean isFinisher();

  /**
   * Returns the starting index of the token in the formula string.
   *
   * @return int - token starting index
   */
  // int startIndex();

}
