package org.toxsoft.tsgui.m5_3.model.impl;

import org.toxsoft.tsgui.m5_3.IM5Bunch;
import org.toxsoft.tsgui.m5_3.IM5Model;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IM5Bunch} implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5Bunch<T>
    extends M5AbstractBunch<T> {

  private final T originalEntity;

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - entity model
   * @param aOriginalEntity &lt;T&gt; - modelled entity or <code>null</code> for field default field values
   * @throws TsNullArgumentRtException aModel = null
   * @throws TsIllegalArgumentRtException aObject is modelled by aModel
   */
  public M5Bunch( IM5Model<T> aModel, T aOriginalEntity ) {
    super( aModel );
    if( aOriginalEntity != null ) {
      TsIllegalArgumentRtException.checkFalse( aModel.isModelledObject( aOriginalEntity ) );
    }
    originalEntity = aOriginalEntity;
    if( originalEntity != null ) {
      internalFillFrom( originalEntity );
    }
  }

  /**
   * Copy constructor.
   * <p>
   * Copies both field values and the {@link #originalEntity()}.
   *
   * @param aSource {@link IM5Bunch} - an editable bunch
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5Bunch( IM5Bunch<T> aSource ) {
    super( TsNullArgumentRtException.checkNull( aSource ).model() );
    @SuppressWarnings( { "unchecked", "rawtypes" } )
    M5AbstractBunch<T> source = (M5AbstractBunch)aSource;
    originalEntity = source.originalEntity();
    for( String fid : source.valuesMap().keys() ) {
      valuesMap().put( fid, source.valuesMap().getByKey( fid ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // M5AbstractBunch
  //

  @Override
  public T originalEntity() {
    return originalEntity;
  }

}
