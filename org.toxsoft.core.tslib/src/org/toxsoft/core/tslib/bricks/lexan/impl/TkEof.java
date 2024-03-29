package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.bricks.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Token of kind {@link ILexanConstants#TKID_EOF}.
 *
 * @author hazard157
 */
public final class TkEof
    extends TkSingleChar {

  /**
   * Constructor.
   *
   * @param aStartIndex int - token starting index in the formula string
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public TkEof( int aStartIndex ) {
    super( TKID_EOF, CHAR_EOF, aStartIndex );
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  public String str() {
    return "<<EOF>>"; //$NON-NLS-1$
  }

  @Override
  public boolean isTerminal() {
    return true;
  }

}
