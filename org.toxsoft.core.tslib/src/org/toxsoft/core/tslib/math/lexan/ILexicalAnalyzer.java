package org.toxsoft.core.tslib.math.lexan;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.math.logican.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Common interface of lexical analyzers.
 * <p>
 * There is a configurable basic implementation {@link LexicalAnalyzer}. Using basic analyzer specific implementation
 * like {@link LogicalFormulaAnalyzer} may be built implementing the same interface.
 *
 * @author hazard157
 */
public interface ILexicalAnalyzer {

  /**
   * Splits a formula string into individual tokens.
   * <p>
   * Last element of the returned list contains the only terminal token either EOF or ERROR.
   *
   * @param aFormulaString String - the formula string
   * @return {@link IList}&lt;{@link ILexanToken}&gt; - the tokens making the formula
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IList<ILexanToken> tokenize( String aFormulaString );

  /**
   * Returns last parsed formula string.
   *
   * @return String - formula string
   */
  String getFormulaString();

  /**
   * Returns the tokens of the last parsed formula.
   *
   * @return {@link IList}&lt;{@link ILexanToken}&gt; - parsed tokens
   */
  IList<ILexanToken> getTokens();

  /**
   * Returns the token substrings in last parsed formula.
   *
   * @return {@link IStringList} - substrings making the tokens {@link #getTokens()}
   */
  IStringList getSubStrings();

}
