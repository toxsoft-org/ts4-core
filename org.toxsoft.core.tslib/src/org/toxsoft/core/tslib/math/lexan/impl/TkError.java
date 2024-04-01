package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Token of kind {@link ILexanConstants#TKID_ERROR}.
 *
 * @author hazard157
 */
public class TkError
    extends LexanToken {

  /**
   * Constructor.
   *
   * @param aMessage String - error message
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException index is negative
   */
  TkError( String aMessage ) {
    super( TKID_ERROR, aMessage );
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  public boolean isFinisher() {
    return true;
  }

}
