package org.toxsoft.core.tslib.math.cond.filter;

import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.filter.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITsSingleFilterType} implementation base.
 *
 * @author hazard157
 */
public abstract class AbstractTsSingleFilterType
    extends TsSingleCondType
    implements ITsSingleFilterType {

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} values
   * @param aParamDefs {@link IStridablesList}&lt;{@link IDataDef}&gt; - description of the condition parameters
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public AbstractTsSingleFilterType( String aId, IOptionSet aParams, IStridablesList<IDataDef> aParamDefs ) {
    super( aId, aParams, aParamDefs );
  }

  // ------------------------------------------------------------------------------------
  // ITsSingleFilterType
  //

  @Override
  public <T> ITsFilter<T> create( ITsSingleCondInfo aCombi ) {
    TsNullArgumentRtException.checkNull( aCombi );
    TsIllegalArgumentRtException.checkFalse( aCombi.typeId().equals( id() ) );
    TsValidationFailedRtException.checkError( validateParams( aCombi.params() ) );
    return doCreateFilter( aCombi.params() );
  }

  // ------------------------------------------------------------------------------------
  // To override/implement
  //

  /**
   * Implementation must create the filter.
   * <p>
   * Argument is already checked by {@link #validateParams(IOptionSet)}.
   *
   * @param <T> - expected type of filtered objects
   * @param aParams {@link IOptionSet} - the filter options {@link ITsSingleCondInfo#params()}
   * @return {@link ITsFilter} - created filter
   */
  protected abstract <T> ITsFilter<T> doCreateFilter( IOptionSet aParams );

}
