package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.bricks.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.bricks.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Token of kind {@link ILexanConstants#TKID_ERROR}.
 * <p>
 * Meaning: Analysis error.
 * <p>
 * The error message is returned by the {@link #message()} method, and the position (character index, where an error is
 * recognized) in the line is returned by the {@link #pos()} method.
 *
 * @author hazard157
 */
public class TkError
    extends AbstractLexanToken {

  private final String message;
  private final int    pos;

  /**
   * Constructor.
   *
   * @param aPos int - index of the symbol in the formula (starting from 0)
   * @param aMessage String - error message
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException poition is a negative number
   */
  TkError( int aPos, String aMessage ) {
    super( TKID_ERROR );
    TsIllegalArgumentRtException.checkTrue( aPos < 0 );
    TsNullArgumentRtException.checkNull( aMessage );
    pos = aPos;
    message = aMessage;
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  public String str() {
    return Integer.toString( pos ) + ": " + message; //$NON-NLS-1$
  }

  @Override
  public boolean isTerminal() {
    return true;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns an error message.
   *
   * @return String - error message
   */
  public String message() {
    return message;
  }

  /**
   * Returns the index of the character in the formula string at which the error occurred.
   *
   * @return int - index of the symbol in the formula (starting from 0)
   */
  public int pos() {
    return pos;
  }

}
