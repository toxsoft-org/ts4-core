package org.toxsoft.core.tslib.bricks.lexan.impl;

import org.toxsoft.core.tslib.bricks.lexan.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILexanToken} implementation base.
 *
 * @author hazard157
 */
public abstract class AbstractLexanToken
    implements ILexanToken {

  private final String kindId;

  private FormulaSubstring formulaSubstring = null;

  /**
   * Constructor.
   *
   * @param aKindId String - the token kind ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument is not an IDpath
   */
  public AbstractLexanToken( String aKindId ) {
    kindId = StridUtils.checkValidIdPath( aKindId );
  }

  // ------------------------------------------------------------------------------------
  // ILexerToken
  //

  @Override
  final public String kindId() {
    return kindId;
  }

  @Override
  public abstract String str();

  @Override
  public char ch() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public double number() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public boolean isTerminal() {
    return false;
  }

  @Override
  public FormulaSubstring formulaSubstring() {
    return formulaSubstring;
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  void setFormulaSubstring( FormulaSubstring aSubstring ) {
    formulaSubstring = aSubstring;
  }

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  public String toString() {
    return str();
  }

}
