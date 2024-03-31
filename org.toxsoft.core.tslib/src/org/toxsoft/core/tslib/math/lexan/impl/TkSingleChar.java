package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Single character tokens class.
 *
 * @author hazard157
 */
public class TkSingleChar
    extends LexanToken {

  private final char ch;

  /**
   * Constructor.
   *
   * @param aKindId String - the token kind ID
   * @param aChar char - the symbol
   * @param aStartIndex int - token starting index in the formula string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public TkSingleChar( String aKindId, char aChar, int aStartIndex ) {
    super( aKindId, EMPTY_STRING + aChar, aStartIndex );
    ch = aChar;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the token character.
   *
   * @return char - the token symbol
   */
  @Override
  public char ch() {
    return ch;
  }

}
