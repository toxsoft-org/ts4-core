package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.bricks.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.bricks.lexan.*;

/**
 * Token of kind {@link ILexanConstants#TKID_NUMBER}.
 * <p>
 * Meaning: the number, both integer or floating numbers are represented as <code><b>double</b></code>.
 *
 * @author hazard157
 */
public final class TkNumber
    extends AbstractLexanToken {

  private final double number;
  private final String str;

  /**
   * Constructor.
   *
   * @param aNumber double - the number
   */
  public TkNumber( double aNumber ) {
    super( TKID_NUMBER );
    number = aNumber;
    str = Double.toString( number );
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  public String str() {
    return str;
  }

  @Override
  public double number() {
    return number;
  }

}
