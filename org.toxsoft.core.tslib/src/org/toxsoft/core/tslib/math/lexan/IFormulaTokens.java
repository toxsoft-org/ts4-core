package org.toxsoft.core.tslib.math.lexan;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * Lexically analyzed formula tokens.
 *
 * @author hazard157
 */
public interface IFormulaTokens {

  /**
   * Returns last parsed formula string.
   *
   * @return String - formula string
   */
  String formulaString();

  /**
   * Returns the tokens of the last parsed formula.
   *
   * @return {@link IList}&lt;{@link ILexanToken}&gt; - parsed tokens
   */
  IList<ILexanToken> tokens();

  /**
   * Returns the token substrings in formula {@link #formulaString()}.
   * <p>
   * Concatenated elements of the returned list resores exactly the {@link #formulaString()}.
   *
   * @return {@link IStringList} - substrings making the tokens {@link #tokens()}
   */
  IStringList subStrings();

  // ------------------------------------------------------------------------------------
  // inline methods for convenience
  //

  /**
   * Determines if there was an error parsing the formula.
   * <p>
   * Actually checks if {@link #tokens()} contains at least one error token.
   *
   * @return boolean - formula parsing error flag
   */
  default boolean isError() {
    return firstErrorToken() != null;
  }

  default boolean isOk() {
    return !isError();
  }

  default boolean isEmpty() {
    return (tokens().size() == 1) && tokens().last().isEof();
  }

  default int firstErrorIndex() {
    for( int i = 0; i < tokens().size(); i++ ) {
      ILexanToken tk = tokens().get( i );
      if( tk.isError() ) {
        return i;
      }
    }
    return -1;
  }

  default ILexanToken firstErrorToken() {
    for( ILexanToken tk : tokens() ) {
      if( tk.isError() ) {
        return tk;
      }
    }
    return null;
  }

}
