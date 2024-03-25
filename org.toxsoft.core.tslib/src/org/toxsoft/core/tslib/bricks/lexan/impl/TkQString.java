package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.bricks.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.bricks.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Token of kind {@link ILexanConstants#TKID_QSTRING}.
 * <p>
 * Meaning: contains unquoted string that was included in quotes.
 *
 * @author hazard157
 */
public final class TkQString
    extends AbstractLexanToken {

  private final String str;

  /**
   * Constructor.
   *
   * @param aStr String - the string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TkQString( String aStr ) {
    super( TKID_NUMBER );
    TsNullArgumentRtException.checkNull( aStr );
    str = aStr;
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  public String str() {
    return str;
  }

}
