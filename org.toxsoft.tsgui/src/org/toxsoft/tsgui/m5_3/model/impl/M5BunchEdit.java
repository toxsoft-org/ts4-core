package org.toxsoft.tsgui.m5_3.model.impl;

import org.toxsoft.tsgui.m5_3.*;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IM5BunchEdit} implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5BunchEdit<T>
    extends M5AbstractBunch<T>
    implements IM5BunchEdit<T> {

  private T originalEntity;

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - entity model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5BunchEdit( IM5Model<T> aModel ) {
    super( aModel );
  }

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - entity model
   * @param aOriginalEntity &lt;T&gt; - modelled entity or <code>null</code> for field default field values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5BunchEdit( IM5Model<T> aModel, T aOriginalEntity ) {
    super( aModel );
    fillFrom( aOriginalEntity, true );
  }

  // ------------------------------------------------------------------------------------
  // M5AbstractBunch
  //

  @Override
  public T originalEntity() {
    return originalEntity;
  }

  // ------------------------------------------------------------------------------------
  // IM5BunchEdit
  //

  @Override
  public <V> void set( String aFieldId, V aValue ) {
    TsNullArgumentRtException.checkNull( aFieldId );
    IM5FieldDef<T, ?> fDef = model().fieldDefs().getByKey( aFieldId );
    if( aValue != null ) {
      TsIllegalArgumentRtException.checkFalse( fDef.valueClass().isInstance( aValue ) );
    }
    Object val = aValue != null ? aValue : NULL_VAL;
    valuesMap().put( aFieldId, val );
  }

  @Override
  public <V> void set( IM5FieldDef<T, V> aFieldDef, V aValue ) {
    TsNullArgumentRtException.checkNull( aFieldDef );
    TsIllegalArgumentRtException.checkFalse( this.model().equals( aFieldDef.ownerModel() ) );
    if( aValue != null ) {
      TsIllegalArgumentRtException.checkFalse( aFieldDef.valueClass().isInstance( aValue ) );
    }
    Object val = aValue != null ? aValue : NULL_VAL;
    valuesMap().put( aFieldDef.id(), val );
  }

  @Override
  public void fillFrom( IM5Bunch<T> aBunch, boolean aUpdateOriginalEntity ) {
    TsNullArgumentRtException.checkNull( aBunch );
    TsIllegalArgumentRtException.checkFalse( this.model().equals( aBunch.model() ) );
    for( IM5FieldDef<T, ?> fdef : model().fieldDefs() ) {
      Object value = aBunch.get( fdef );
      valuesMap().put( fdef.id(), value != null ? value : NULL_VAL );
    }
    if( aUpdateOriginalEntity ) {
      originalEntity = aBunch.originalEntity();
    }
  }

  @Override
  public void fillFrom( T aObject, boolean aUpdateOriginalEntity ) {
    if( aObject == null ) {
      internalFillDefaults();
    }
    else {
      TsIllegalArgumentRtException.checkFalse( model().isModelledObject( aObject ) );
      internalFillFrom( aObject );
    }
    if( aUpdateOriginalEntity ) {
      originalEntity = aObject;
    }
  }

  @Override
  public void clear() {
    internalFillDefaults();
    originalEntity = null;
  }

}
