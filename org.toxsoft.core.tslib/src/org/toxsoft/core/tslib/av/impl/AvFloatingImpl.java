package org.toxsoft.core.tslib.av.impl;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.errors.AvDataLossRtException;

/**
 * Atomic value of type {@link EAtomicType#FLOATING}.
 *
 * @author hazard157
 */
class AvFloatingImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  private final double value;

  AvFloatingImpl( double aValue ) {
    value = aValue;
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.FLOATING;
  }

  @Override
  public double asDouble() {
    return value;
  }

  @Override
  public float asFloat() {
    if( value > Float.MAX_VALUE || value < -(double)Float.MAX_VALUE ) {
      throw new AvDataLossRtException();
    }
    return (float)value;
  }

  @Override
  public String asString() {
    return Double.toString( value );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Double asValobj() {
    return Double.valueOf( value );
  }

  // ------------------------------------------------------------------------------------
  // abstract methods implementation
  //

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    return Double.compare( value, aThat.asDouble() );
  }

  @Override
  protected int internalValueHashCode() {
    long dblval = Double.doubleToRawLongBits( value );
    return (int)(dblval ^ (dblval >>> 32));
  }

}
