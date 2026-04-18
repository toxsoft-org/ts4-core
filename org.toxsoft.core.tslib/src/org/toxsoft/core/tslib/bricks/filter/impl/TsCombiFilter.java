package org.toxsoft.core.tslib.bricks.filter.impl;

import java.io.*;

import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsFilter} implemented as single filters combined by logical operaions.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public class TsCombiFilter<T>
    implements ITsFilter<T>, Serializable {

  private static final long serialVersionUID = 157157L;

  private final ITsCombiFilterParams params;
  private final ITsFilter<T>         singleFilter;
  private final TsCombiFilter<T>     left;
  private final TsCombiFilter<T>     right;
  private final boolean              completeEvaluation;

  // ------------------------------------------------------------------------------------
  // Instance creation
  //

  /**
   * Creates filter specifying all necessary information.
   *
   * @param <T> - type of filtered objects
   * @param aParams {@link ITsCombiFilterParams} - parameter of the filter to be created
   * @param aFacReg ITsFilterFactoriesRegistry&lt;T&gt; - single filter factories registry to use
   * @param aCompleteEvaluation boolean - the full calculation flag
   * @return {@link ITsFilter}&lt;T&gt; - created filter instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException parameters references to the filter ID not registered in the supplied registry
   */
  public static <T> ITsFilter<T> create( ITsCombiFilterParams aParams, ITsFilterFactoriesRegistry<T> aFacReg,
      boolean aCompleteEvaluation ) {
    if( aParams == ITsCombiFilterParams.NONE ) {
      return ITsFilter.NONE;
    }
    if( aParams == ITsCombiFilterParams.ALL ) {
      return ITsFilter.ALL;
    }
    return new TsCombiFilter<>( aParams, aFacReg, aCompleteEvaluation );
  }

  /**
   * Creates filter in the incomplete calculation mode.
   *
   * @param <T> - type of filtered objects
   * @param aParams {@link ITsCombiFilterParams} - parameter of the filter to be created
   * @param aFacReg ITsFilterFactoriesRegistry&lt;T&gt; - single filter factories registry to use
   * @return {@link ITsFilter}&lt;T&gt; - created filter instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException parameters references to the filter ID not registered in the supplied registry
   */
  public static <T> ITsFilter<T> create( ITsCombiFilterParams aParams, ITsFilterFactoriesRegistry<T> aFacReg ) {
    return create( aParams, aFacReg, false );
  }

  /**
   * Hidden constructor correctly initializes all fields.
   *
   * @param aParams {@link ITsCombiFilterParams} - parameters of filter creation
   * @param aFacReg ITsFilterFactoriesRegistry&lt;T&gt; - the factory used for each single filter creation
   * @param aCompleteEvaluation boolean - the complete evaluation flag
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  private TsCombiFilter( ITsCombiFilterParams aParams, ITsFilterFactoriesRegistry<T> aFacReg,
      boolean aCompleteEvaluation ) {
    TsNullArgumentRtException.checkNulls( aParams, aFacReg );
    completeEvaluation = aCompleteEvaluation;
    params = aParams;
    // create filter
    if( aParams.isSingle() ) { // single filter
      ITsSingleFilterFactory<T> f = aFacReg.get( aParams.single().typeId() );
      singleFilter = f.create( aParams.single() );
      left = null;
      right = null;
    }
    else { // combi filter
      singleFilter = null;
      left = new TsCombiFilter<>( aParams.left(), aFacReg, aCompleteEvaluation );
      right = new TsCombiFilter<>( aParams.right(), aFacReg, aCompleteEvaluation );
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsCombiFilter
  //

  @Override
  public boolean accept( T aElement ) {
    if( params.isSingle() ) {
      boolean b = singleFilter.accept( aElement );
      if( params.isInverted() ) {
        return !b;
      }
      return b;
    }
    boolean bLeft = left.accept( aElement );
    switch( params.op() ) {
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
    boolean b = params.op().op( bLeft, bRight );
    if( params.isInverted() ) {
      return !b;
    }
    return b;
  }

}
