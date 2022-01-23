package org.toxsoft.tsgui.m5_1.impl;

import static org.toxsoft.tsgui.m5_1.impl.ITsResources.*;

import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Базовый класс для реализации {@link IM5Bunch} и {@link IM5BunchEdit}.
 *
 * @author hazard157
 * @param <T> - класс моделированной сущности
 */
abstract class M5AbstractBunch<T>
    implements IM5Bunch<T> {

  protected static final Object NULL_VAL = new Object();

  private final IStringMapEdit<Object> valuesMap = new StringMap<>();
  private final IM5Model<T>            model;

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - модель объекта
   * @throws TsNullArgumentRtException аргумент = null
   */
  public M5AbstractBunch( IM5Model<T> aModel ) {
    TsNullArgumentRtException.checkNull( aModel );
    model = aModel;
    internalFillMapWithDefaults();
  }

  // ------------------------------------------------------------------------------------
  // Для наследников
  //

  protected final IStringMapEdit<Object> valuesMap() {
    return valuesMap;
  }

  protected void internalFillMapFrom( T aObject ) {
    for( int i = 0, count = model().fieldDefs().size(); i < count; i++ ) {
      String fId = model().fieldDefs().keys().get( i );
      IM5FieldDef<T, ?> fDef = model().fieldDefs().values().get( i );
      Object value = fDef.getFieldValue( aObject );
      if( value == null ) {
        value = NULL_VAL;
      }
      valuesMap.put( fId, value );
    }
  }

  protected void internalFillMapWithDefaults() {
    for( int i = 0, count = model().fieldDefs().size(); i < count; i++ ) {
      String fId = model().fieldDefs().keys().get( i );
      IM5FieldDef<T, ?> fDef = model().fieldDefs().values().get( i );
      Object value = fDef.defaultValue();
      if( value == null ) {
        value = NULL_VAL;
      }
      valuesMap.put( fId, value );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5Bunch
  //

  @Override
  final public IM5Model<T> model() {
    return model;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public <V> V get( String aFieldId ) {
    IM5FieldDef<T, ?> fDef = model().fieldDefs().getByKey( aFieldId );
    Object value = get( model.fieldDefs().getByKey( aFieldId ) );
    return (V)fDef.valueClass().cast( value );
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
    Object v = valuesMap.getByKey( aFieldId );
    if( v == NULL_VAL ) {
      v = null;
    }
    if( v != null && !aValueClass.isInstance( v ) ) {
      throw new TsIllegalStateRtException( FMT_ERR_INV_VALUE_CLASS, fDef.id(), v.getClass().getSimpleName(),
          aValueClass.getSimpleName() );
    }
    return aValueClass.cast( v );
  }

  // ------------------------------------------------------------------------------------
  // Для переопределения
  //

  @Override
  abstract public T originalEntity();

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @SuppressWarnings( "nls" )
  @Override
  final public String toString() {
    return this.getClass().getSimpleName() + ": " + model().id();
  }

  @Override
  final public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof M5AbstractBunch ) {
      M5AbstractBunch<?> that = (M5AbstractBunch<?>)aThat;
      if( model().equals( that.model() ) ) {
        return valuesMap.equals( that.valuesMap() );
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
