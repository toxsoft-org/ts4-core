package org.toxsoft.core.tslib.math.combicond;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.logican.ILogicalFormulaConstants.*;
import static org.toxsoft.core.tslib.math.logicop.ILogicalOpConstants.*;

import org.toxsoft.core.tslib.bricks.strid.idgen.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Instance of this class represents {@link ICombiCondParams} as a tokens of a logical formula.
 *
 * @author hazard157
 */
public final class CombiCondTokenizer {

  /**
   * TODO change algorithm to eliminate unneeded brackets
   */

  private final IStridGenerator keywrodGenerator = new SimpleStridGenerator( "F", 0, 0 ); //$NON-NLS-1$

  private final ICombiCondParams                  ccp;
  private final IListEdit<ILexanToken>            tokens     = new ElemArrayList<>();
  private final IStringMapEdit<ISingleCondParams> singlesMap = new StringMap<>();
  private final String                            formulaString;

  private int counter = 0;

  /**
   * Constructor.
   *
   * @param aCcp {@link ICombiCondParams} - the parameters to represent as a formula
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public CombiCondTokenizer( ICombiCondParams aCcp ) {
    TsNullArgumentRtException.checkNull( aCcp );
    ccp = aCcp;
    add( aCcp );
    addToken( new TkEof( counter ) );
    formulaString = LexanUtils.makeFormulaString( tokens );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void add( ICombiCondParams aCcp ) {
    if( aCcp.isSingle() ) {
      addSimple( aCcp.single(), aCcp.isInverted() );
      return;
    }
    addLeftBracket();
    add( aCcp.left() );
    addLogicalOp( aCcp.op() );
    add( aCcp.right() );
    addRightBracket();
  }

  private void addSimple( ISingleCondParams aScp, boolean aInverted ) {
    if( aInverted ) {
      ILexanToken tk = new LexanToken( TKID_LOGICAL_NOT, KW_LOGICAL_NOT, counter );
      addToken( tk );
      addSpace();
    }
    String kw = makeKeyword( aScp );
    ILexanToken tk = new LexanToken( TKID_KEYWORD, kw, counter );
    addToken( tk );
  }

  private void addLogicalOp( ELogicalOp aOp ) {
    addSpace();
    ILexanToken tk = switch( aOp ) {
      case AND -> new LexanToken( TKID_LOGICAL_AND, KW_LOGICAL_AND, counter );
      case OR -> new LexanToken( TKID_LOGICAL_OR, KW_LOGICAL_OR, counter );
      case XOR -> new LexanToken( TKID_LOGICAL_XOR, KW_LOGICAL_XOR, counter );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
    addToken( tk );
    addSpace();
  }

  private void addLeftBracket() {
    ILexanToken tk = new TkSingleChar( TKID_BRACKET_ROUND_LEFT, '(', counter );
    addToken( tk );
  }

  private void addRightBracket() {
    ILexanToken tk = new TkSingleChar( TKID_BRACKET_ROUND_RIGHT, ')', counter );
    addToken( tk );
  }

  private void addToken( ILexanToken aToken ) {
    tokens.add( aToken );
    counter += aToken.str().length();
  }

  private String makeKeyword( ISingleCondParams aScp ) {
    for( String kw : singlesMap.keys() ) {
      ISingleCondParams p = singlesMap.getByKey( kw );
      if( aScp.equals( p ) ) {
        return kw;
      }
    }
    String kw = keywrodGenerator.nextId();
    singlesMap.put( kw, aScp );
    return kw;
  }

  private void addSpace() {
    if( counter != 0 ) {
      ++counter;
    }
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns represented {@link ICombiCondParams}.
   *
   * @return {@link ICombiCondParams} - the source
   */
  public ICombiCondParams getCombiCondParams() {
    return ccp;
  }

  /**
   * Returns the token representation of the {@link #getCombiCondParams()}.
   *
   * @return {@link IList}&lt;{@link ILexanToken}&gt; - list of tokens
   */
  public IList<ILexanToken> tokens() {
    return tokens;
  }

  /**
   * Returns the single parameters making the {@link #getCombiCondParams()}.
   *
   * @return {@link IStringMap}&lt;{@link ISingleCondParams}&gt; - map "keyword" - "single parameters"
   */
  public IStringMap<ISingleCondParams> singleParams() {
    return singlesMap;
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
