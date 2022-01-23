package org.toxsoft.tsgui.m5_1.impl;

import org.toxsoft.tsgui.m5_1.api.IM5Bunch;
import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.m5_1.api.helpers.IM5ValuesCache;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Default cache stroes bunches for <code>null</code> entities.
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

  private IM5Bunch<T> getIfSpecialCase( T aObj ) {
    if( aObj == null ) {
      if( defaultNullBunch == null ) {
        defaultNullBunch = new M5Bunch<>( model, null );
      }
      return defaultNullBunch;
    }
    return null;
  }

  @Override
  public IM5Bunch<T> getValues( T aObj ) {
    IM5Bunch<T> b = getIfSpecialCase( aObj );
    if( b == null ) {
      b = new M5Bunch<>( model, aObj );
    }
    return b;
  }

  @Override
  public IM5Bunch<T> findCached( T aObj ) {
    return getIfSpecialCase( aObj );
  }

  @Override
  public void remove( T aObj ) {
    if( aObj == null ) {
      defaultNullBunch = null;
    }
  }

}
