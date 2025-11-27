package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.model.impl.ITsResources.*;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class for {@link IM5Bunch} and {@link IM5BunchEdit} implementations.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
abstract class M5AbstractBunch<T>
    implements IM5Bunch<T> {

  /**
   * Map "Field ID" - "Field Value" stores <code>null</code> as {@link #NULL_VAL}.
   *
   * @author hazard157
   * @param <T> - modeled entity type
   */
  static class ValuesMap<T> {

    /**
     * Value used instead of <code>null</code> to be stored in {@link #valuesMap}.
     */
    private static final Object NULL_VAL = new Object();

    private final IStringMapEdit<Object> valuesMap = new StringMap<>();

    /**
     * Sets the field value in {@link #valuesMap}.
     *
     * @param aFieldDef {@link IM5FieldDef} - valid field definition
     * @param aValue {@link Object} - field value must have valid type or may be <code>null</code>
     */
    public void setValue( IM5FieldDef<T, ?> aFieldDef, Object aValue ) {
      Object value = aValue != null ? aValue : NULL_VAL;
      valuesMap.put( aFieldDef.id(), value );
    }

    public Object getValue( String aFieldId ) {
      Object value = valuesMap.getByKey( aFieldId );
      return value != NULL_VAL ? value : null;
    }

  }

  protected final ValuesMap<T> valuesMap = new ValuesMap<>();

  private final IM5Model<T> model;

  /**
   * Constructor.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - entity model
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5AbstractBunch( IM5Model<T> aModel ) {
    model = TsNullArgumentRtException.checkNull( aModel );
    internalFillDefaults();
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  protected void internalFillFrom( T aObject ) {
    for( IM5FieldDef<T, ?> fdef : model.fieldDefs() ) {
      Object value = fdef.getFieldValue( aObject );
      valuesMap.setValue( fdef, value );
    }
  }

  protected void internalFillDefaults() {
    for( IM5FieldDef<T, ?> fdef : model.fieldDefs() ) {
      Object val = fdef.defaultValue();
      valuesMap.setValue( fdef, val );
    }
  }

  // ------------------------------------------------------------------------------------
  // IM5Bunch
  //

  @Override
  final public IM5Model<T> model() {
    return model;
  }

  @Override
  public <V> V get( String aFieldId ) {
    @SuppressWarnings( { "rawtypes", "unchecked" } )
    IM5FieldDef<T, V> fdef = (IM5FieldDef)model.fieldDefs().getByKey( aFieldId );
    return get( fdef );
  }

  @Override
  final public <V> V get( IM5FieldDef<T, V> aFieldDef ) {
    TsNullArgumentRtException.checkNull( aFieldDef );
    return getAs( aFieldDef.id(), aFieldDef.valueClass() );
  }

  @Override
  final public <V> V getAs( String aFieldId, Class<V> aValueClass ) {
    TsNullArgumentRtException.checkNull( aValueClass );
    IM5FieldDef<T, ?> fDef = model().fieldDefs().getByKey( aFieldId );
    if( !aValueClass.isAssignableFrom( fDef.valueClass() ) ) {
      throw new TsIllegalArgumentRtException( FMT_ERR_CANT_GET_FIELD_VALUE_AS_CLASS, fDef.id(),
          fDef.valueClass().getSimpleName(), aValueClass.getSimpleName() );
    }
    Object v = valuesMap.getValue( aFieldId );
    if( v != null && !aValueClass.isInstance( v ) ) {
      throw new TsIllegalStateRtException( FMT_ERR_INV_VALUE_CLASS, fDef.id(), v.getClass().getSimpleName(),
          aValueClass.getSimpleName() );
    }
    return aValueClass.cast( v );
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  @Override
  abstract public T originalEntity();

  // ------------------------------------------------------------------------------------
  // Object
  //

  @Override
  final public String toString() {
    return this.getClass().getSimpleName() + ": " + model().id(); //$NON-NLS-1$
  }

  @Override
  final public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof M5AbstractBunch ) {
      M5AbstractBunch<?> that = (M5AbstractBunch<?>)aThat;
      if( model().equals( that.model() ) ) {
        return valuesMap.equals( that.valuesMap );
      }
    }
    return false;
  }

  @Override
  final public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + model().hashCode();
    result = TsLibUtils.PRIME * result + valuesMap.hashCode();
    return result;
  }

}
