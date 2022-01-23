package org.toxsoft.tsgui.m5_1.impl;

import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Реализация {@link IM5Bunch}.
 *
 * @author hazard157
 * @param <T> - класс моделированной сущности
 */
public class M5Bunch<T>
    extends M5AbstractBunch<T> {

  private final T originalEntity;

  /**
   * Создает набор значений полей указанного объекта.
   *
   * @param aModel {@link IM5Model}&lt;T&gt; - модель объекта
   * @param aOriginalEntity &lt;T&gt; - объект, может быть null
   * @throws TsNullArgumentRtException aModel = null
   * @throws TsIllegalArgumentRtException aObject не null и не моделируем с помощью модели aModel
   */
  public M5Bunch( IM5Model<T> aModel, T aOriginalEntity ) {
    super( aModel );
    if( aOriginalEntity != null ) {
      TsIllegalArgumentRtException.checkFalse( aModel.isModelledObject( aOriginalEntity ) );
    }
    originalEntity = aOriginalEntity;
    if( originalEntity != null ) {
      internalFillMapFrom( originalEntity );
    }
    else {
      internalFillMapWithDefaults();
    }
  }

  /**
   * Конструктор для вызова из {@link IM5BunchEdit#getBunch()}.
   *
   * @param aEditor {@link M5BunchEdit} - вызывающий редактор
   */
  M5Bunch( M5BunchEdit<T> aEditor ) {
    super( aEditor.model() );
    originalEntity = aEditor.originalEntity();
    for( String fid : aEditor.valuesMap().keys() ) {
      valuesMap().put( fid, aEditor.valuesMap().getByKey( fid ) );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IM5Bunch
  //

  @Override
  public T originalEntity() {
    return originalEntity;
  }

}
