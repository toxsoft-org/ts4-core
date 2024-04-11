package org.toxsoft.core.tslib.math.combicond.impl;

import static org.toxsoft.core.tslib.math.combicond.impl.ITsResources.*;

import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.math.combicond.*;
import org.toxsoft.core.tslib.math.logican.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Builds {@link ICombiCondParams} from the {@link ILogFoNode}.
 * <p>
 * This is a stateless class so the singleton {@link #INSTANCE} exists.
 *
 * @author hazard157
 */
public final class CombiCondParamsBuilder {

  /**
   * The singleton instance.
   */
  public static final CombiCondParamsBuilder INSTANCE = new CombiCondParamsBuilder();

  /**
   * Constructor.
   */
  private CombiCondParamsBuilder() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private ValidationResult canMakeCcp( ILogFoNode aRoot, IStringMap<ISingleCondParams> aScpMap ) {
    if( aRoot == ILogFoNode.NONE ) {
      return ValidationResult.error( MSG_ERR_NONE_LFN );
    }
    if( aRoot.isLeaf() ) {
      ISingleCondParams scp = aScpMap.findByKey( aRoot.keyword() );
      if( scp == null ) {
        return ValidationResult.error( FMT_ERR_NO_SCP_BY_KW, aRoot.keyword() );
      }
      return ValidationResult.SUCCESS;
    }
    ValidationResult vr = canMakeCcp( aRoot.left(), aScpMap );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, canMakeCcp( aRoot.right(), aScpMap ) );
  }

  private ICombiCondParams makeCcp( ILogFoNode aRoot, IStringMap<ISingleCondParams> aScpMap ) {
    if( aRoot.isLeaf() ) {
      return CombiCondParams.createSingle( aScpMap.getByKey( aRoot.keyword() ), aRoot.isInverted() );
    }
    ICombiCondParams left = makeCcp( aRoot.left(), aScpMap );
    ICombiCondParams right = makeCcp( aRoot.right(), aScpMap );
    return CombiCondParams.createCombi( left, aRoot.op(), right, aRoot.isInverted() );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Determines if {@link ICombiCondParams} can be built.
   *
   * @param aRoot {@link ILogFoNode} - parsed binary tree of the logical expression
   * @param aScpMap {@link IStringMap}&lt;{@link ISingleCondParams}&gt; - map "keyword" - "single params"
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValidationResult canBuild( ILogFoNode aRoot, IStringMap<ISingleCondParams> aScpMap ) {
    TsNullArgumentRtException.checkNulls( aRoot, aScpMap );
    return canMakeCcp( aRoot, aScpMap );
  }

  /**
   * Determines if {@link ICombiCondParams} can be built.
   *
   * @param aRoot {@link ILogFoNode} - parsed binary tree of the logical expression
   * @param aScpMap {@link IStringMap}&lt;{@link ISingleCondParams}&gt; - map "keyword" - "single params"
   * @return {@link ICombiCondParams} - built combined condition parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed {@link #canBuild(ILogFoNode, IStringMap)}
   */
  public ICombiCondParams build( ILogFoNode aRoot, IStringMap<ISingleCondParams> aScpMap ) {
    TsValidationFailedRtException.checkError( canBuild( aRoot, aScpMap ) );
    return makeCcp( aRoot, aScpMap );
  }

}
