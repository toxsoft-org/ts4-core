package org.toxsoft.core.tslib.math.logican;

import static org.toxsoft.core.tslib.math.lexan.ILexanConstants.*;
import static org.toxsoft.core.tslib.math.logican.ILogicalFormulaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.toxsoft.core.tslib.math.lexan.*;
import org.toxsoft.core.tslib.math.lexan.impl.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Parses the logical formula.
 * <p>
 * The result of the parsing is root {@link ILogFoNode} corresponding to the formula. Besides the node, lexical analysis
 * results may be retrieved for syntax highlight, error messaging erc.
 * <p>
 * Class instance after constructor has state as it has just parsed an empty string.
 *
 * @author hazard157
 */
public class LogicalFormulaParser {

  // FIXME L10N

  private final LogicalFormulaAnalyzer logicalFormulaAnalyzer;

  private ILogFoNode         lfnRoot       = null;
  private IFormulaTokensEdit formulaTokens = null;

  /**
   * While parsing, points to the current token in {@link IFormulaTokens#tokens()}.
   */
  private int currIndex = -1;

  /**
   * Constructor.
   */
  public LogicalFormulaParser() {
    logicalFormulaAnalyzer = new LogicalFormulaAnalyzer();
    parse( EMPTY_STRING );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ILogFoNode readLogicalOpCombination() {
    ILogFoNode lfn = readSimpleCombination();
    if( lfn != ILogFoNode.NONE ) {
      ELogicalOp op = checkCurrentTokenAsLogicalOp();
      if( op != null ) {
        lfn = new LfnNode( lfn, op, readLogicalOpCombination(), false );
      }
    }
    return lfn;
  }

  // return NONE on error
  private ILogFoNode readSimpleCombination() {
    return switch( currToken().kindId() ) {
      case TKID_BRACKET_ROUND_LEFT -> {
        nextToken();
        ILogFoNode lfn = readLogicalOpCombination();
        if( lfn == ILogFoNode.NONE ) {
          yield lfn;
        }
        // expect right round bracket ')'
        if( !currToken().kindId().equals( TKID_BRACKET_ROUND_RIGHT ) ) {
          yield setCurrError( "Right round bracket ')' was expected" );
        }
        nextToken();
        yield lfn;
      }
      case TKID_LOGICAL_NOT -> {
        nextToken();
        ILogFoNode lfn = readLogicalOpCombination();
        if( lfn == ILogFoNode.NONE ) {
          yield lfn;
        }
        yield LfnNode.invert( lfn );
      }
      case TKID_KEYWORD -> {
        ILogFoNode lfn = new LfnKeyword( currToken().str(), false );
        if( lfn == ILogFoNode.NONE ) {
          yield lfn;
        }
        nextToken();
        yield lfn;
      }
      case TKID_LOGICAL_TRUE -> {
        nextToken();
        yield ILogFoNode.TRUE;
      }
      case TKID_LOGICAL_FALSE -> {
        nextToken();
        yield ILogFoNode.FALSE;
      }
      default -> {
        yield setCurrError( "Simple combination was expected" );
      }
    };
  }

  private ELogicalOp checkCurrentTokenAsLogicalOp() {
    ELogicalOp op = switch( currToken().kindId() ) {
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

  private ILexanToken nextToken() {
    if( currIndex == formulaTokens.tokens().size() ) {
      return formulaTokens.tokens().last();
    }
    ++currIndex;
    ILexanToken tk = formulaTokens.tokens().get( currIndex );
    while( tk.kindId().equals( TKID_SPACE ) ) {
      tk = nextToken();
    }
    return tk;
  }

  private ILexanToken currToken() {
    return formulaTokens.tokens().get( currIndex );
  }

  private ILogFoNode setCurrError( String aMsg ) {
    formulaTokens.replaceToken( currIndex, new TkError( aMsg ) );
    return ILogFoNode.NONE;
  }

  private ILogFoNode parseLogically() {
    // list contains one token -> either error TKID_ERROR or TKID_EOF for an empty formula
    if( formulaTokens.tokens().size() <= 1 ) {
      return ILogFoNode.NONE;
    }
    // LexicalAnalyzer already returned an error
    if( formulaTokens.tokens().last().kindId().equals( TKID_ERROR ) ) {
      return ILogFoNode.NONE;
    }
    // start parsing and building formula root node
    currIndex = -1;
    nextToken();
    ILogFoNode lfn = readLogicalOpCombination();
    if( lfn != ILogFoNode.NONE ) {
      if( !currToken().kindId().equals( TKID_EOF ) ) {
        return setCurrError( "End fo formula expected" );
      }
    }
    return lfn;
  }

  /**
   * Parses the logical formula and returns result as a root node {@link ILogFoNode}.
   * <p>
   * If formula can not be parsed, {@link #formulaTokens()} either is empty or contains at least one error token of kind
   * {@link ILexanConstants#TKID_ERROR} and this method returns {@link ILogFoNode#NONE} constant.
   *
   * @param aFormulaString String - the formula string
   * @return {@link ILogFoNode} - parsed formula node or {@link ILogFoNode#NONE} on error or an empty formula
   */
  public ILogFoNode parse( String aFormulaString ) {
    formulaTokens = logicalFormulaAnalyzer.tokenize( aFormulaString );
    lfnRoot = parseLogically();

    // --- DEBUG
    // if( formulaTokens.isError() ) {
    // if( formulaTokens.firstErrorToken() == null ) {
    // TsTestUtils.pl( "NO errors, WHY ???" );
    // }
    // else {
    // TsTestUtils.pl( "Parse err: %s", formulaTokens.firstErrorToken().str() );
    // }
    // }
    // else {
    // String fs = LexanUtils.makeFormulaString( formulaTokens.tokens() );
    // TsTestUtils.pl( "Parse OK: '%s'", fs );
    // }
    // TsTestUtils.pl( "Node: '%s'", lfnRoot.toString() );
    // ---

    return lfnRoot;
  }

  /**
   * Returns results of the internal syntactic analysis in {@link #parse(String)}.
   *
   * @return {@link IFormulaTokens} - the syntactic analysis result
   */
  public IFormulaTokens formulaTokens() {
    TsInternalErrorRtException.checkNull( formulaTokens );
    return formulaTokens;
  }

  /**
   * Returns the last parsed logical formula as a root node {@link ILogFoNode}.
   * <p>
   * Simply returns the result of last call of method {@link #parse(String)}.
   *
   * @return {@link ILogFoNode} - parsed formula node or {@link ILogFoNode#NONE} on error
   */
  public ILogFoNode getFormulaNode() {
    TsInternalErrorRtException.checkNull( lfnRoot );
    return lfnRoot;
  }

}
