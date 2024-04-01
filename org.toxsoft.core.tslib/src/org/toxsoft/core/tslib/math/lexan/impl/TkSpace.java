package org.toxsoft.core.tslib.math.lexan.impl;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Token of kind {@link ILexanConstants#TKID_SPACE}.
 *
 * @author hazard157
 */
public final class TkSpace
    extends LexanToken {

  /**
   * The singleton instance of the single space char.
   */
  public static final ILexanToken TK_SINGLE = new TkSpace( " " ); //$NON-NLS-1$

  /**
   * Constructor.
   *
   * @param aSpace String - the space string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is an empty string (length = 0)
   * @throws TsIllegalArgumentRtException argument contains chars other than
   *           {@link IStrioHardConstants#DEFAULT_SPACE_CHARS}
   */
  public TkSpace( String aSpace ) {
    super( TKID_SPACE, aSpace );
    TsIllegalArgumentRtException.checkTrue( aSpace.isEmpty() );
    for( int i = 0; i < aSpace.length(); i++ ) {
      char ch = aSpace.charAt( i );
      if( IStrioHardConstants.DEFAULT_SPACE_CHARS.indexOf( ch ) < 0 ) {
        throw new TsIllegalArgumentRtException();
      }
    }
  }

}
