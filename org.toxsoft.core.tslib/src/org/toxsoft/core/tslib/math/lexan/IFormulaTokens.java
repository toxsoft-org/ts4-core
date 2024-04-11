package org.toxsoft.core.tslib.math.lexan;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;

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
   * Concatenated elements of the returned list restores exactly the {@link #formulaString()}.
   *
   * @return {@link IStringList} - substrings making the tokens {@link #tokens()}
   */
  IStringList subStrings();

  /**
   * Returns list of keywords, {@link ILexanToken#str()} of tokens of kind {@link ILexanConstants#TKID_KEYWORD}.
   *
   * @return {@link IStringList} - list of keywords
   */
  IStringList listKeywords();

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

  /**
   * Returns inverted value of {@link #isError()}.
   *
   * @return boolean - formula parsing success flag
   */
  default boolean isOk() {
    return !isError();
  }

  /**
   * Determines if formula is empty.
   *
   * @return boolean - <code>true</code> if formula contains only spaces and EOF tokens
   */
  default boolean isEmpty() {
    // space and EOF tokens are ignored
    for( ILexanToken tk : tokens() ) {
      switch( tk.kindId() ) {
        case TKID_EOF:
        case TKID_SPACE: {
          break;
        }
        default:
          return false;
      }
    }
    return true;
  }

  /**
   * Returns index of the first ERROR token in {@link #tokens()}.
   *
   * @return int - first error token index or -1 if none found
   */
  default int firstErrorIndex() {
    for( int i = 0; i < tokens().size(); i++ ) {
      ILexanToken tk = tokens().get( i );
      if( tk.isError() ) {
        return i;
      }
    }
    return -1;
  }

  /**
   * Returns the first ERROR token in {@link #tokens()}.
   *
   * @return int - first error token or <code>null</code> if none found
   */
  default ILexanToken firstErrorToken() {
    for( ILexanToken tk : tokens() ) {
      if( tk.isError() ) {
        return tk;
      }
    }
    return null;
  }

}
