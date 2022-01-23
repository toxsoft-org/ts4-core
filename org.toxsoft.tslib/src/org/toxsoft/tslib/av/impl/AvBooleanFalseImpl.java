package org.toxsoft.tslib.av.impl;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;

/**
 * {@link IAtomicValue} implementationn for {@link EAtomicType#BOOLEAN}.
 *
 * @author hazard157
 */
class AvBooleanFalseImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 158158L;

  AvBooleanFalseImpl() {
    // nop
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.BOOLEAN;
  }

  @Override
  public boolean asBool() {
    return false;
  }

  @Override
  public String asString() {
    return Boolean.toString( false );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Boolean asValobj() {
    return Boolean.FALSE;
  }

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    return Boolean.compare( false, aThat.asBool() );
  }

  @Override
  protected int internalValueHashCode() {
    return 0;
  }

}
