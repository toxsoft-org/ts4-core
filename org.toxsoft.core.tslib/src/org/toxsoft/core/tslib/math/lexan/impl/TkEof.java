package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;
import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.math.lexan.*;

/**
 * Token of kind {@link ILexanConstants#TKID_EOF}.
 *
 * @author hazard157
 */
public final class TkEof
    extends TkSingleChar {

  /**
   * The singleton instance.
   */

  public static final ILexanToken TK_EOF = new TkEof();

  private TkEof() {
    super( TKID_EOF, CHAR_EOF );
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  public String str() {
    return "<<EOF>>"; //$NON-NLS-1$
  }

  @Override
  public boolean isFinisher() {
    return true;
  }

}
