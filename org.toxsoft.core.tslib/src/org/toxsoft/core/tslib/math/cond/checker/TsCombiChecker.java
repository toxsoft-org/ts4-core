package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsChecker} implemented as single checkers combined by logical operations.
 *
 * @author hazard157
 */
public class TsCombiChecker
    implements ITsChecker {

  private final boolean    isSingle;
  private final ITsChecker singleChecker;
  private final ITsChecker left;
  private final ELogicalOp op;
  private final ITsChecker right;
  private final boolean    isInverted;
  private final boolean    completeEvaluation;

  // ------------------------------------------------------------------------------------
  // Instance creation
  //

  private TsCombiChecker( ITsChecker aSingle, boolean aInverted ) {
    isSingle = true;
    singleChecker = aSingle;
    left = null;
    op = null;
    right = null;
    isInverted = aInverted;
    completeEvaluation = false;
  }

  private TsCombiChecker( ITsChecker aLeft, ELogicalOp aOp, ITsChecker aRight, boolean aInverted, boolean aComplete ) {
    isSingle = false;
    singleChecker = null;
    left = aLeft;
    op = aOp;
    right = aRight;
    isInverted = aInverted;
    completeEvaluation = aComplete;
  }

  /**
   * Creates the combined checker consisting of one single checker.
   *
   * @param <T> - type of checkered objects
   * @param aSingle {@link ITsChecker} - the single checker
   * @param aInverted boolean - the flag of the NOT unary operation applied to this condition check result
   * @return {@link ITsChecker}&lt;T&gt; - the created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> ITsChecker createSingle( ITsChecker aSingle, boolean aInverted ) {
    TsNullArgumentRtException.checkNull( aSingle );
    return new TsCombiChecker( aSingle, aInverted );
  }

  /**
   * Creates the combined checker consisting of left and right single checkers.
   *
   * @param <T> - type of checkered objects
   * @param aLeft {@link ITsChecker} - the left checker
   * @param aOp {@link ELogicalOp} - operation between left and right checkers
   * @param aRight {@link ITsChecker} - the right checker
   * @param aInverted boolean - the flag of the NOT unary operation applied to this condition check result
   * @param aComplete boolean - <code>true</code> to call all checkers even if checker result is already calculated
   * @return {@link ITsChecker}&lt;T&gt; - the created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> ITsChecker createCombi( ITsChecker aLeft, ELogicalOp aOp, ITsChecker aRight, boolean aInverted,
      boolean aComplete ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    return new TsCombiChecker( aLeft, aOp, aRight, aInverted, aComplete );
  }

  // ------------------------------------------------------------------------------------
  // ITsChecker
  //

  @Override
  public boolean checkCondition() {
    if( isSingle ) {
      boolean b = singleChecker.checkCondition();
      if( isInverted ) {
        return !b;
      }
      return b;
    }
    boolean bLeft = left.checkCondition();
    switch( op ) {
      case AND:
        if( !bLeft && !completeEvaluation ) {
          return false;
        }
        break;
      case OR:
        if( bLeft && !completeEvaluation ) {
          return true;
        }
        break;
      case XOR:
        break;
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
    boolean bRight = right.checkCondition();
    boolean b = op.op( bLeft, bRight );
    if( isInverted ) {
      return !b;
    }
    return b;
  }

}
