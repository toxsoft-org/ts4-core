package org.toxsoft.tsgui.m5_1.impl;

import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link IM5BunchEdit}.
 *
 * @author hazard157
 * @param <T> - класс моделированной сущности
 */
public final class M5BunchEdit<T>
    extends M5AbstractBunch<T>
    implements IM5BunchEdit<T> {

  private T originalEntity;

  /**
   * Конструктор с набором значений полей по умолчанию.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - модель объекта
   * @throws TsNullArgumentRtException аргумент = null
   */
  public M5BunchEdit( IM5Model<T> aModel ) {
    super( aModel );
    internalFillMapWithDefaults();
  }

  /**
   * Конструктор.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - модель объекта
   * @param aOriginalEntity &lt;T&gt; - моделированный объект, может быть <code>null</code> для значений по умолчнаию
   * @throws TsNullArgumentRtException аргумент = null
   */
  public M5BunchEdit( IM5Model<T> aModel, T aOriginalEntity ) {
    super( aModel );
    fillFrom( aOriginalEntity, true );
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5Bunch
  //

  @Override
  public T originalEntity() {
    return originalEntity;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5BunchEdit
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
    TsIllegalArgumentRtException.checkFalse( this.model().equals( aFieldDef.owninigModel() ) );
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
      internalFillMapWithDefaults();
      if( aUpdateOriginalEntity ) {
        originalEntity = null;
      }
      return;
    }
    TsIllegalArgumentRtException.checkFalse( model().isModelledObject( aObject ) );
    internalFillMapFrom( aObject );
    if( aUpdateOriginalEntity ) {
      originalEntity = aObject;
    }
  }

  @Override
  public IM5Bunch<T> getBunch() {
    return new M5Bunch<>( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  @Override
  public void clear() {
    internalFillMapWithDefaults();
    originalEntity = null;
  }

}
