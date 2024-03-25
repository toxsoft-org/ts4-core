package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.bricks.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.bricks.strio.IStrioHardConstants.*;

import org.toxsoft.core.tslib.bricks.lexan.*;

/**
 * Token of kind {@link ILexanConstants#TKID_EOF}.
 *
 * @author hazard157
 */
public final class TkEof
    extends TkSingleChar {

  TkEof() {
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
  public boolean isTerminal() {
    return true;
  }

}
