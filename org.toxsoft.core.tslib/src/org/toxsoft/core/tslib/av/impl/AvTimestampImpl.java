package org.toxsoft.core.tslib.av.impl;

import java.time.*;

import org.toxsoft.core.tslib.av.*;

/**
 * Atomic value of type {@link EAtomicType#TIMESTAMP}.
 *
 * @author hazard157
 */
class AvTimestampImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  private final long value;

  AvTimestampImpl( long aValue ) {
    value = aValue;
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.TIMESTAMP;
  }

  @Override
  public long asLong() {
    return value;
  }

  @Override
  public String asString() {
    Long date = Long.valueOf( value );
    return String.format( "%tF %tT.%tL", date, date, date ); //$NON-NLS-1$
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public LocalDateTime asValobj() {
    return LocalDateTime.ofInstant( Instant.ofEpochMilli( value ), ZoneId.systemDefault() );
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
