package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Single character tokens class.
 *
 * @author hazard157
 */
public class TkSingleChar
    extends AbstractLexanToken {

  private final char   ch;
  private final String str;

  /**
   * Constructor.
   *
   * @param aKindId String - the token kind ID
   * @param aChar char - the symbol
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public TkSingleChar( String aKindId, char aChar ) {
    super( aKindId );
    ch = aChar;
    str = EMPTY_STRING + ch;
  }

  // ------------------------------------------------------------------------------------
  // AbstractLexanToken
  //

  @Override
  public String str() {
    return str;
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
