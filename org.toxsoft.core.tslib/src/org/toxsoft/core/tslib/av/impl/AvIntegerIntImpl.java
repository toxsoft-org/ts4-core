package org.toxsoft.core.tslib.av.impl;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.errors.AvDataLossRtException;

/**
 * Atomic value of type {@link EAtomicType#INTEGER} implemented as <code>int</code> value.
 * <p>
 * This implementation can hold only integer value in range {@link Integer#MIN_VALUE} - {@link Integer#MAX_VALUE} as
 * <code>int</code>.
 *
 * @author hazard157
 */
class AvIntegerIntImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  private final int value;

  AvIntegerIntImpl( long aValue ) {
    if( aValue < Integer.MIN_VALUE || aValue > Integer.MAX_VALUE ) {
      throw new AvDataLossRtException();
    }
    value = (int)aValue;
  }

  AvIntegerIntImpl( int aValue ) {
    if( aValue < Integer.MIN_VALUE || aValue > Integer.MAX_VALUE ) {
      throw new AvDataLossRtException();
    }
    value = aValue;
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.INTEGER;
  }

  @Override
  public int asInt() {
    return value;
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
    return Integer.toString( value );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Integer asValobj() {
    return Integer.valueOf( value );
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
    return value;
  }

}
