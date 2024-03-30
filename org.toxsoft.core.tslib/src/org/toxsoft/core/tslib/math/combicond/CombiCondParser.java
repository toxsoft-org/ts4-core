package org.toxsoft.core.tslib.math.combicond;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.logican.ILogicalFormulaConstants.*;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.combicond.impl.*;
import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.math.logican.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parser to create the the {@link ICombiCondParams} from the formula.
 * <p>
 * Parser consumes {@link LogicalFormulaAnalyzer} out in method {@link #parse(IList, IStringMap)}.
 * <p>
 * <strong>Implementation notes:</strong> the Little Language programming pattern is used as described i the book Марк
 * Гранд,"Шаблоны проектирования в Java", Москва, ООО "Новое знание", 2004, pp 333-359.
 * <p>
 * Here is the productions list;
 *
 * <pre>
 * combination = simpleCombination LOGICAL_OP combination
 * combination = simpleCombination
 * simpleCombination = ( combination )
 * simpleCombination = KEYWORD
 * simpleCombination = NOT KEYWORD
 * simpleCombination = TRUE
 * simpleCombination = FALSE
 * </pre>
 *
 * @author hazard157
 */
public class CombiCondParser {

  IList<ILexanToken>            allTokens = null;
  IStringMap<ISingleCondParams> singles   = null;
  int                           currIndex = 0;
  ILexanToken                   currToken = null;

  /**
   * Constructor.
   */
  public CombiCondParser() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ICombiCondParams readLogicalOpCombination() {
    ICombiCondParams ccp = readSimpleCombination();
    ELogicalOp op = checkCurrentTokenAsLogicalOp();
    if( op != null ) {
      ccp = CombiCondParams.createCombi( ccp, op, readLogicalOpCombination() );
    }
    return ccp;
  }

  private ICombiCondParams readSimpleCombination() {
    return switch( currToken.kindId() ) {
      case TKID_BRACKET_ROUND_LEFT -> {
        nextToken();
        ICombiCondParams ccp = readLogicalOpCombination();
        expectToken( TKID_BRACKET_ROUND_RIGHT );
        yield ccp;
      }
      case TKID_LOGICAL_NOT -> {
        nextToken();
        yield CombiCondParams.createCombi( readLogicalOpCombination(), true );
      }
      case TKID_KEYWORD -> {
        ICombiCondParams ccp = CombiCondParams.createSingle( singles.getByKey( currToken.str() ) );
        nextToken();
        yield ccp;
      }
      case TKID_LOGICAL_TRUE -> {
        nextToken();
        yield CombiCondParams.createSingle( ISingleCondParams.ALWAYS );
      }
      case TKID_LOGICAL_FALSE -> {
        nextToken();
        yield CombiCondParams.createSingle( ISingleCondParams.ALWAYS );
      }
      default -> throw new TsInternalErrorRtException();
    };
  }

  private ELogicalOp checkCurrentTokenAsLogicalOp() {
    ELogicalOp op = switch( currToken.kindId() ) {
      case TKID_LOGICAL_AND -> ELogicalOp.AND;
      case TKID_LOGICAL_OR -> ELogicalOp.OR;
      case TKID_LOGICAL_XOR -> ELogicalOp.XOR;
      default -> null;
    };
    if( op != null ) {
      nextToken();
    }
    return op;
  }

  private void nextToken() {
    if( currIndex < allTokens.size() ) {
      currToken = allTokens.get( currIndex++ );
    }
  }

  private void expectToken( String aTokenId ) {
    TsIllegalStateRtException.checkFalse( currToken.kindId().equals( aTokenId ) );
    nextToken();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Parses tokens to the {@link ICombiCondParams}.
   *
   * @param aTokens {@link IList}&lt;{@link ILexanToken}&gt; - output of {@link LogicalFormulaAnalyzer}
   * @param aSingles {@link IStringMap}&lt;{@link ISingleCondParams}&gt; - map "keyword" - "single params"
   * @return {@link ICombiCondParams} - parsed combined condition parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException invalid token sequence
   */
  public ICombiCondParams parse( IList<ILexanToken> aTokens, IStringMap<ISingleCondParams> aSingles ) {
    TsNullArgumentRtException.checkNulls( aTokens, aSingles );
    allTokens = aTokens;
    singles = aSingles;
    currIndex = 0;
    TsIllegalArgumentRtException.checkTrue( aTokens.isEmpty() );
    TsIllegalArgumentRtException.checkTrue( aTokens.last().kindId().equals( TKID_ERROR ) );
    nextToken();
    ICombiCondParams ccp = readLogicalOpCombination();
    expectToken( TKID_EOF );
    return ccp;
  }

}
