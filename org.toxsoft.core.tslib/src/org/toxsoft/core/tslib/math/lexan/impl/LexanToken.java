package org.toxsoft.core.tslib.math.lexan.impl;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILexanToken} implementation base.
 *
 * @author hazard157
 */
public class LexanToken
    implements ILexanToken {

  private final String kindId;
  private final String str;
  private final int    startIndex;

  /**
   * Constructor.
   *
   * @param aKindId String - the token kind ID
   * @param aStr String - token as a {@link String}
   * @param aStartIndex int - token starting index in the formula string
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   * @throws TsIllegalArgumentRtException index is negative
   */
  public LexanToken( String aKindId, String aStr, int aStartIndex ) {
    kindId = StridUtils.checkValidIdPath( aKindId );
    TsNullArgumentRtException.checkNull( aStr );
    TsIllegalArgumentRtException.checkTrue( aStartIndex < 0 );
    str = aStr;
    startIndex = aStartIndex;
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

  @Override
  public int startIndex() {
    return startIndex;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return str();
  }

}
