package org.toxsoft.core.tsgui.m5.model.impl;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5ValuesCache} default implementation caches only {@link IM5Bunch} for <code>null</code> entity.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
class M5DefaultValuesCache<T>
    implements IM5ValuesCache<T> {

  final IM5Model<T> model;
  IM5Bunch<T>       defaultNullBunch = null;

  M5DefaultValuesCache( IM5Model<T> aModel ) {
    model = TsNullArgumentRtException.checkNull( aModel );
  }

  @Override
  public void clear() {
    defaultNullBunch = null;
  }

  private IM5Bunch<T> getIfSpecialCase( T aEntity ) {
    if( aEntity == null ) {
      if( defaultNullBunch == null ) {
        defaultNullBunch = new M5Bunch<>( model, null );
      }
      return defaultNullBunch;
    }
    return null;
  }

  @Override
  public IM5Bunch<T> getValues( T aEntity ) {
    IM5Bunch<T> b = getIfSpecialCase( aEntity );
    if( b == null ) {
      b = new M5Bunch<>( model, aEntity );
    }
    return b;
  }

  @Override
  public IM5Bunch<T> findCached( T aEntity ) {
    return getIfSpecialCase( aEntity );
  }

  @Override
  public void remove( T aEntity ) {
    if( aEntity == null ) {
      defaultNullBunch = null;
    }
  }

}
