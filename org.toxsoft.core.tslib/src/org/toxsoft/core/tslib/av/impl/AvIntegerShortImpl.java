package org.toxsoft.core.tslib.av.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;

/**
 * Atomic value of type {@link EAtomicType#INTEGER} implemented as <code>short</code> value.
 * <p>
 * This implementation can hold only integer value in range {@link Short#MIN_VALUE} - {@link Short#MAX_VALUE} as
 * <code>short</code>.
 *
 * @author hazard157
 */
class AvIntegerShortImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  private final short value;

  AvIntegerShortImpl( long aValue ) {
    if( aValue < Short.MIN_VALUE || aValue > Short.MAX_VALUE ) {
      throw new AvDataLossRtException();
    }
    value = (short)aValue;
  }

  AvIntegerShortImpl( int aValue ) {
    if( aValue < Short.MIN_VALUE || aValue > Short.MAX_VALUE ) {
      throw new AvDataLossRtException();
    }
    value = (short)aValue;
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
    return Short.toString( value );
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public Short asValobj() {
    return Short.valueOf( value );
  }

  // ------------------------------------------------------------------------------------
  // abstract methods implementation
  //

  @Override
  protected boolean internalEqualsValue( IAtomicValue aThat ) {
    return value == aThat.asInt();
  }

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    return Long.compare( value, aThat.asLong() );
  }

  @Override
  protected int internalValueHashCode() {
    return value;
  }

}
