package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsUnsupportedFeatureRtException;

/**
 * Базовый класс реализации различных {@link ITjValue}.
 *
 * @author goga
 */
abstract class TjValue
    implements ITjValue {

  TjValue() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса ITjValue
  //

  @Override
  public boolean isInteger() {
    return false;
  }

  @Override
  public String asString() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public Number asNumber() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public ITjObject asObject() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public IListEdit<ITjValue> asArray() {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public void setString( String aString ) {
    TsNullArgumentRtException.checkNull( aString );
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public void setNumber( Number aNumber ) {
    TsNullArgumentRtException.checkNull( aNumber );
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public void setNumber( int aNumber ) {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public void setNumber( long aNumber ) {
    throw new TsUnsupportedFeatureRtException();
  }

  @Override
  public void setNumber( double aNumber ) {
    throw new TsUnsupportedFeatureRtException();
  }

}
