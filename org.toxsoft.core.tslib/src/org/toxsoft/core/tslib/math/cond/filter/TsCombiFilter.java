package org.toxsoft.core.tslib.math.cond.filter;

import java.io.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.math.logicop.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsFilter} implemented as single filters combined by logical operations.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public class TsCombiFilter<T>
    implements ITsFilter<T>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final boolean      isSingle;
  private final ITsFilter<T> singleFilter;
  private final ITsFilter<T> left;
  private final ELogicalOp   op;
  private final ITsFilter<T> right;
  private final boolean      isInverted;
  private final boolean      completeEvaluation;

  // ------------------------------------------------------------------------------------
  // Instance creation
  //

  private TsCombiFilter( ITsFilter<T> aSingle, boolean aInverted ) {
    isSingle = true;
    singleFilter = aSingle;
    left = null;
    op = null;
    right = null;
    isInverted = aInverted;
    completeEvaluation = false;
  }

  private TsCombiFilter( ITsFilter<T> aLeft, ELogicalOp aOp, ITsFilter<T> aRight, boolean aInverted,
      boolean aComplete ) {
    isSingle = false;
    singleFilter = null;
    left = aLeft;
    op = aOp;
    right = aRight;
    isInverted = aInverted;
    completeEvaluation = aComplete;
  }

  /**
   * Creates the combined filter consisting of one single filter.
   *
   * @param <T> - type of filtered objects
   * @param aSingle {@link ITsFilter} - the single filter
   * @param aInverted boolean - the flag of the NOT unary operation applied to this condition check result
   * @return {@link ITsFilter}&lt;T&gt; - the created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> ITsFilter<T> createSingle( ITsFilter<T> aSingle, boolean aInverted ) {
    TsNullArgumentRtException.checkNull( aSingle );
    return new TsCombiFilter<>( aSingle, aInverted );
  }

  /**
   * Creates the combined filter consisting of left and right single filters.
   *
   * @param <T> - type of filtered objects
   * @param aLeft {@link ITsFilter} - the left filter
   * @param aOp {@link ELogicalOp} - operation between left and right filters
   * @param aRight {@link ITsFilter} - the right filter
   * @param aInverted boolean - the flag of the NOT unary operation applied to this condition check result
   * @param aComplete boolean - <code>true</code> to call all filters even if filter result is already calculated
   * @return {@link ITsFilter}&lt;T&gt; - the created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> ITsFilter<T> createCombi( ITsFilter<T> aLeft, ELogicalOp aOp, ITsFilter<T> aRight,
      boolean aInverted, boolean aComplete ) {
    TsNullArgumentRtException.checkNulls( aLeft, aOp, aRight );
    return new TsCombiFilter<>( aLeft, aOp, aRight, aInverted, aComplete );
  }

  // ------------------------------------------------------------------------------------
  // ITsFilter
  //

  @Override
  public boolean accept( T aElement ) {
    if( isSingle ) {
      boolean b = singleFilter.accept( aElement );
      if( isInverted ) {
        return !b;
      }
      return b;
    }
    boolean bLeft = left.accept( aElement );
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
    boolean bRight = right.accept( aElement );
    boolean b = op.op( bLeft, bRight );
    if( isInverted ) {
      return !b;
    }
    return b;
  }

}
