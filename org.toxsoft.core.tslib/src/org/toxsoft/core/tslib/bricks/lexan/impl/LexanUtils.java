package org.toxsoft.core.tslib.bricks.lexan.impl;

import org.toxsoft.core.tslib.bricks.lexan.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Logical formula utilities.
 *
 * @author hazard157
 */
public class LexanUtils {

  /**
   * Returns the substrings of formula string corresponding to the tokens.
   * <p>
   * This is a helper method. For example, to highlight tokens in GUI formula editor.
   * <p>
   * Concatenated substrings makes the formula string.
   *
   * @param aFormulaString String - the formula string
   * @param aTokens {@link IList}&lt;{@link ILexanToken}&gt; - parsed tokens
   * @return {@link IStringList} - substrings making the formula string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static final IStringList makeTokenSubstrings( String aFormulaString, IList<ILexanToken> aTokens ) {
    TsNullArgumentRtException.checkNulls( aFormulaString, aTokens );
    if( aTokens.isEmpty() ) {
      return IStringList.EMPTY;
    }
    IStringListEdit ll = new StringArrayList( aTokens.size() );
    int startIndex = 0;
    int endIndex = 0;
    // iterate all but last token
    for( int i = 0; i < aTokens.size() - 1; i++ ) {
      ILexanToken tkCurr = aTokens.get( i );
      ILexanToken tkNext = aTokens.get( i + 1 );
      startIndex = tkCurr.startIndex();
      endIndex = tkNext.startIndex();
      String s = aFormulaString.substring( startIndex, endIndex );
      ll.add( s );
    }
    // process last token, always make it up to the end of formula string
    ll.add( aFormulaString.substring( endIndex ) );
    return ll;
  }

  /**
   * No subclasses.
   */
  private LexanUtils() {
    // nop
  }

}
