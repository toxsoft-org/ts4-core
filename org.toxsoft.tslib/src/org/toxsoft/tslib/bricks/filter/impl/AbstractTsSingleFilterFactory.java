package org.toxsoft.tslib.bricks.filter.impl;

import static org.toxsoft.tslib.bricks.filter.impl.ITsResources.*;
import static org.toxsoft.tslib.utils.TsLibUtils.*;

import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.av.opset.IOptionSet;
import org.toxsoft.tslib.bricks.filter.*;
import org.toxsoft.tslib.bricks.strid.coll.IStridablesListEdit;
import org.toxsoft.tslib.bricks.strid.coll.impl.StridablesList;
import org.toxsoft.tslib.bricks.strid.impl.Stridable;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Abstract implementation of {@link ITsSingleFilterFactory}.
 *
 * @author hazard157
 * @param <T> - type of filtered objects
 */
public abstract class AbstractTsSingleFilterFactory<T>
    extends Stridable
    implements ITsSingleFilterFactory<T> {

  private final IStridablesListEdit<IDataDef> paramDefs = new StridablesList<>();
  private final Class<T>                      objectClass;

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @param aName String - human-readable short name
   * @param aDescription String - human-readable description, may be empty string
   * @param aObjClass {@link Class}&lt;T&gt; - accpeted objects class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public AbstractTsSingleFilterFactory( String aId, String aName, String aDescription, Class<T> aObjClass ) {
    super( aId, aName, aDescription );
    objectClass = TsNullArgumentRtException.checkNull( aObjClass );
  }

  /**
   * Constructor.
   *
   * @param aId String - identifier (IDname or IDpath)
   * @param aObjClass {@link Class}&lt;T&gt; - accpeted objects class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException aId is not valid IDpath
   */
  public AbstractTsSingleFilterFactory( String aId, Class<T> aObjClass ) {
    this( aId, EMPTY_STRING, EMPTY_STRING, aObjClass );
  }

  // ------------------------------------------------------------------------------------
  // ITsSingleFilterFactory
  //

  @Override
  public IStridablesListEdit<IDataDef> paramDefs() {
    return paramDefs;
  }

  @Override
  public Class<T> objectClass() {
    return objectClass;
  }

  @Override
  public ITsFilter<T> create( ITsSingleFilterParams aFilterParams ) {
    TsNullArgumentRtException.checkNull( aFilterParams );
    for( IDataDef opInfo : paramDefs ) {
      if( opInfo.isMandatory() ) {
        if( !aFilterParams.params().hasValue( opInfo ) ) {
          throw new TsItemNotFoundRtException( FMT_ERR_NO_MANDATORY_PARAM, id(), opInfo.id() );
        }
      }
    }
    ITsFilter<T> f = doCreateFilter( aFilterParams.params() );
    TsInternalErrorRtException.checkNull( f );
    return f;
  }

  // ------------------------------------------------------------------------------------
  // Additional API
  //

  @Override
  public void setNameAndDescription( String aName, String aDescription ) {
    super.setNameAndDescription( aName, aDescription );
  }

  // ------------------------------------------------------------------------------------
  // abstract methods
  //

  /**
   * Descndant must create filter with specified parameters.
   *
   * @param aParams {@link IOptionSet} - filter parameters (all mandatory params exist)
   * @return {@link ITsFilter} - create filter, must not be <code>null</code>
   */
  protected abstract ITsFilter<T> doCreateFilter( IOptionSet aParams );

}
