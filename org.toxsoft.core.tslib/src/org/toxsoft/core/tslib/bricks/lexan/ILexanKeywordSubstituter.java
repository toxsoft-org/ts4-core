package org.toxsoft.core.tslib.bricks.lexan;

import org.toxsoft.core.tslib.bricks.lexan.impl.*;

/**
 * Declares the keyword token substitution strategy.
 * <p>
 * {@link LexicalAnalyzer#nextToken()} has an ability to substitute keyword tokens (ie tokens with ID
 * {@link ILexanConstants#TKID_KEYWORD}) with other token, even user-defined one. There may be different use cases of
 * the token substitution, for example:
 * <ul>
 * <li>substitute constants with number values, in mathematical formula, eg "<code>PI</code>" keyword may be replaced by
 * the number 3.1415;</li>
 * <li>substitute constants with user defined token, eg "<code>TRUE</code>" may be substituted in logical formula with
 * used defined token with {@link ILexanToken#kindId()} = "<code>LOGICAL_TRUE</code>";</li>
 * <li>substitute function names with used defined token, eg. "<code>cos</code>", "<code>sin</code>" and others may be
 * implemented as trigonometric function names.</li>
 * </ul>
 *
 * @author hazard157
 */
public interface ILexanKeywordSubstituter {

  /**
   * Singleton of substituter doing nothing.
   */
  ILexanKeywordSubstituter NONE = aKeywordToken -> aKeywordToken;

  /**
   * Implementation may substitute the keyword token with any token.
   * <p>
   * If substitution is not needed method must return the same reference as an argument.
   *
   * @param aKeywordToken {@link AbstractLexanToken} - token of kind {@link ILexanConstants#TKID_KEYWORD}
   * @return {@link AbstractLexanToken} - substitution token or the argument
   */
  AbstractLexanToken substituteKeyword( AbstractLexanToken aKeywordToken );

}
