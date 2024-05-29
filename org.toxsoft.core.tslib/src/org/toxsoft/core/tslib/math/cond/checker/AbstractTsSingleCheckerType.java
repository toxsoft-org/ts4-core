package org.toxsoft.core.tslib.math.cond.checker;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsSingleCheckerType} implementation base.
 *
 * @author dima
 * @param <E> - the checker environment class
 */
public abstract class AbstractTsSingleCheckerType<E>
    extends TsSingleCondType
    implements ITsSingleCheckerType<E> {

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} values
   * @param aParamDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - description of the condition parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public AbstractTsSingleCheckerType( String aId, IOptionSet aParams, IStridablesList<IDataDef> aParamDefs ) {
    super( aId, aParams, aParamDefs );
  }

  // ------------------------------------------------------------------------------------
  // ITsSingleFilterType
  //

  @Override
  final public AbstractTsSingleChecker<E> create( E aEnviron, ITsSingleCondInfo aCombiCondInfo ) {
    TsNullArgumentRtException.checkNull( aEnviron );
    TsNullArgumentRtException.checkNull( aCombiCondInfo );
    TsIllegalArgumentRtException.checkFalse( aCombiCondInfo.typeId().equals( id() ) );
    TsValidationFailedRtException.checkError( validateParams( aCombiCondInfo.params() ) );
    return doCreateChecker( aEnviron, aCombiCondInfo.params() );
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Implementation must create the checker.
   * <p>
   * Argument is already checked by {@link #validateParams(IOptionSet)}.
   *
   * @param aEnviron - &lt;E&gt; - the environment
   * @param aParams {@link IOptionSet} - the checker options {@link ITsSingleCondInfo#params()}
   * @return {@link AbstractTsSingleChecker} - created checker
   */
  protected abstract AbstractTsSingleChecker<E> doCreateChecker( E aEnviron, IOptionSet aParams );

}
