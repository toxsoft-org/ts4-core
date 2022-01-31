package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

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
   * <p>
   * Preferrable way to create the values bunch of the entity is {@link IM5Model#valuesOf(Object)}. So this constructor
   * is package private.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - entity model
   * @param aOriginalEntity &lt;T&gt; - modelled entity or <code>null</code> for field default field values
   * @throws TsNullArgumentRtException aModel = null
   * @throws TsIllegalArgumentRtException aObject is modelled by aModel
   */
  M5Bunch( IM5Model<T> aModel, T aOriginalEntity ) {
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
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      Object value = source.valuesMap.getValue( fdef.id() );
      valuesMap.setValue( fdef, value );
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
