package org.toxsoft.core.tslib.math.lexan;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILexanToken} implementation base.
 *
 * @author hazard157
 */
public non-sealed class LexanToken
    implements ILexanToken {

  private final String kindId;
  private final String str;

  /**
   * Constructor.
   *
   * @param aKindId String - the token kind ID
   * @param aStr String - token as a {@link String}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   * @throws TsIllegalArgumentRtException index is negative
   */
  public LexanToken( String aKindId, String aStr ) {
    kindId = StridUtils.checkValidIdPath( aKindId );
    TsNullArgumentRtException.checkNull( aStr );
    str = aStr;
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  final public String kindId() {
    return kindId;
  }

  @Override
  public String str() {
    return str;
  }

  @Override
  public char ch() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public double number() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public boolean isKeyword() {
    return kindId.equals( ILexanConstants.TKID_KEYWORD );
  }

  @Override
  public boolean isFinisher() {
    return false;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return kindId + " - '" + str() + "'"; //$NON-NLS-1$//$NON-NLS-2$
  }

}
