package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Token of kind {@link ILexanConstants#TKID_NUMBER}.
 * <p>
 * Meaning: the number, both integer or floating numbers are represented as <code><b>double</b></code>.
 *
 * @author hazard157
 */
public final class TkNumber
    extends LexanToken {

  private final double number;

  /**
   * Constructor.
   *
   * @param aNumber double - the number
   * @param aStartIndex int - token starting index in the formula string
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   * @throws TsIllegalArgumentRtException index is negative
   */
  public TkNumber( double aNumber, int aStartIndex ) {
    super( TKID_NUMBER, Double.toString( aNumber ), aStartIndex );
    number = aNumber;
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  public double number() {
    return number;
  }

}
