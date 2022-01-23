package org.toxsoft.tslib.av.impl;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;

/**
 * {@link IAtomicValue} implementationn for {@link EAtomicType#BOOLEAN}.
 *
 * @author hazard157
 */
class AvBooleanTrueImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 158158L;

  AvBooleanTrueImpl() {
    // nop
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.BOOLEAN;
  }

  @Override
  public boolean asBool() {
    return true;
  }

  @Override
  public String asString() {
    return Boolean.toString( true );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Boolean asValobj() {
    return Boolean.TRUE;
  }

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    return Boolean.compare( true, aThat.asBool() );
  }

  @Override
  protected int internalValueHashCode() {
    return 1;
  }

}
