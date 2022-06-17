package org.toxsoft.core.tslib.av.impl;

import org.toxsoft.core.tslib.av.*;

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
  protected boolean internalEqualsValue( IAtomicValue aThat ) {
    return aThat.asBool();
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
