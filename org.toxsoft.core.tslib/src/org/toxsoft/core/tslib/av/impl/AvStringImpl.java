package org.toxsoft.core.tslib.av.impl;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Atomic value of type {@link EAtomicType#STRING}.
 *
 * @author hazard157
 */
class AvStringImpl
    extends AbstractAtomicValue {

  private static final long serialVersionUID = 157157L;

  private final String value;

  AvStringImpl( String aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    value = aValue;
  }

  @Override
  public EAtomicType atomicType() {
    return EAtomicType.STRING;
  }

  @Override
  public String asString() {
    return value;
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public String asValobj() {
    return value;
  }

  // ------------------------------------------------------------------------------------
  // abstract methods implementation
  //

  @Override
  protected boolean internalEqualsValue( IAtomicValue aThat ) {
    return value.equals( aThat.asString() );
  }

  @Override
  protected int internalCompareValue( IAtomicValue aThat ) {
    return value.compareTo( aThat.asString() );
  }

  @Override
  protected int internalValueHashCode() {
    return value.hashCode();
  }

}
