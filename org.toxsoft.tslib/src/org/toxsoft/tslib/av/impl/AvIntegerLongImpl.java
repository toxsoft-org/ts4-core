package org.toxsoft.tslib.av.impl;

import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.errors.AvDataLossRtException;

/**
 * Atomic value of type {@link EAtomicType#INTEGER} implemented as <code>long</code> value.
 * <p>
 * This implementation can hold only integer value in range {@link Long#MIN_VALUE} - {@link Long#MAX_VALUE} as
 * <code>long</code>.
 *
 * @author hazard157
 */
class AvIntegerLongImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  private final long value;

  AvIntegerLongImpl( long aValue ) {
    value = aValue;
  }

  AvIntegerLongImpl( int aValue ) {
    value = aValue;
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.INTEGER;
  }

  @Override
  public int asInt() {
    if( value > Integer.MAX_VALUE || value < Integer.MIN_VALUE ) {
      throw new AvDataLossRtException();
    }
    return (int)value;
  }

  @Override
  public long asLong() {
    return value;
  }

  @Override
  public double asDouble() {
    return value;
  }

  @Override
  public float asFloat() {
    return value;
  }

  @Override
  public String asString() {
    return Long.toString( value );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Long asValobj() {
    return Long.valueOf( value );
  }

  // ------------------------------------------------------------------------------------
  // abstract methods implementation
  //

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    return Long.compare( value, aThat.asLong() );
  }

  @Override
  protected int internalValueHashCode() {
    return (int)(value ^ (value >>> 32));
  }

}
