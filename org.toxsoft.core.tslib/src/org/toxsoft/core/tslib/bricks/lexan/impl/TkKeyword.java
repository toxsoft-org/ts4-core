package org.toxsoft.core.tslib.bricks.lexan.impl;

import static org.toxsoft.core.tslib.bricks.lexan.ILexanConstants.*;

import org.toxsoft.core.tslib.bricks.lexan.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Token of kind {@link ILexanConstants#TKID_KEYWORD}.
 * <p>
 * Meaning: contains IDpath (or IDname) keyword to be interpreted by the higher level parser.
 *
 * @author hazard157
 */
public final class TkKeyword
    extends AbstractLexanToken {

  private final String str;

  /**
   * Constructor.
   *
   * @param aStr String - the string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public TkKeyword( String aStr ) {
    super( TKID_KEYWORD );
    StridUtils.checkValidIdPath( aStr );
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
