package org.toxsoft.core.tslib.math.cond.impl;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.logican.ILogicalFormulaConstants.*;
import static org.toxsoft.core.tslib.math.logicop.ILogicalOpConstants.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Instance of this class represents {@link ITsCombiCondInfo} as a tokens of a logical formula.
 *
 * @author hazard157
 */
public final class TsCombiCondInfoTokenizer {

  /**
   * TODO change algorithm to eliminate unneeded brackets in formula string
   */

  private final ITsCombiCondInfo       ccp;
  private final IListEdit<ILexanToken> tokens = new ElemArrayList<>();
  private final String                 formulaString;

  /**
   * Constructor.
   *
   * @param aCcInfo {@link ITsCombiCondInfo} - the parameters to represent as a formula
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TsCombiCondInfoTokenizer( ITsCombiCondInfo aCcInfo ) {
    TsNullArgumentRtException.checkNull( aCcInfo );
    ccp = aCcInfo;
    add( aCcInfo.rootNode() );
    addToken( TkEof.TK_EOF );
    formulaString = LexanUtils.makeFormulaString( tokens );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void add( ITsCombiCondNode aCcNode ) {
    if( aCcNode.isSingle() ) {
      addSimple( aCcNode.singleCondId(), aCcNode.isInverted() );
      return;
    }
    addLeftBracket();
    add( aCcNode.left() );
    addLogicalOp( aCcNode.op() );
    add( aCcNode.right() );
    addRightBracket();
  }

  private void addSimple( String aSingleCondId, boolean aInverted ) {
    if( aInverted ) {
      ILexanToken tk = new LexanToken( TKID_LOGICAL_NOT, KW_LOGICAL_NOT );
      addToken( tk );
      addToken( TkSpace.TK_SINGLE );
    }
    addToken( new LexanToken( TKID_KEYWORD, aSingleCondId ) );
  }

  private void addLogicalOp( ELogicalOp aOp ) {
    addToken( TkSpace.TK_SINGLE );
    ILexanToken tk = switch( aOp ) {
      case AND -> new LexanToken( TKID_LOGICAL_AND, KW_LOGICAL_AND );
      case OR -> new LexanToken( TKID_LOGICAL_OR, KW_LOGICAL_OR );
      case XOR -> new LexanToken( TKID_LOGICAL_XOR, KW_LOGICAL_XOR );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    addToken( tk );
    addToken( TkSpace.TK_SINGLE );
  }

  private void addLeftBracket() {
    ILexanToken tk = new TkSingleChar( TKID_BRACKET_ROUND_LEFT, '(' );
    addToken( tk );
  }

  private void addRightBracket() {
    ILexanToken tk = new TkSingleChar( TKID_BRACKET_ROUND_RIGHT, ')' );
    addToken( tk );
  }

  private void addToken( ILexanToken aToken ) {
    tokens.add( aToken );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns represented {@link ITsCombiCondInfo}.
   *
   * @return {@link ITsCombiCondInfo} - the source
   */
  public ITsCombiCondInfo getTsCombiCondInfo() {
    return ccp;
  }

  /**
   * Returns the token representation of the {@link #getTsCombiCondInfo()}.
   *
   * @return {@link IList}&lt;{@link ILexanToken}&gt; - list of tokens
   */
  public IList<ILexanToken> tokens() {
    return tokens;
  }

  /**
   * Returns the formula string, represented by the {@link #tokens()}.
   *
   * @return String - the formula string
   */
  public String getFormulaString() {
    return formulaString;
  }

}
