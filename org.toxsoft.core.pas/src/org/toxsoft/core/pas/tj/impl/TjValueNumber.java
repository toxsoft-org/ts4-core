package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ETjKind;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

class TjValueNumber
    extends TjValue {

  private Number value;

  public TjValueNumber( Number aNumber ) {
    value = aNumber;
  }

  @Override
  public ETjKind kind() {
    return ETjKind.NUMBER;
  }

  @Override
  public boolean isInteger() {
    if( value instanceof Integer || value instanceof Long || value instanceof Short || value instanceof Byte ) {
      return true;
    }
    return false;
  }

  @Override
  public Number asNumber() {
    return value;
  }

  @Override
  public void setNumber( Number aNumber ) {
    if( aNumber == null ) {
      throw new TsNullArgumentRtException();
    }
    value = aNumber;
  }

  @Override
  public void setNumber( int aNumber ) {
    value = Integer.valueOf( aNumber );
  }

  @Override
  public void setNumber( long aNumber ) {
    value = Long.valueOf( aNumber );
  }

  @Override
  public void setNumber( double aNumber ) {
    value = Double.valueOf( aNumber );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return value.toString();
  }

  @Override
  public boolean equals( Object aThat ) {
    if( aThat == this ) {
      return true;
    }
    if( aThat instanceof TjValueNumber that ) {
      return value.equals( that.value );
    }
    return false;
  }

  @Override
  public int hashCode() {
    int result = TsLibUtils.INITIAL_HASH_CODE;
    result = TsLibUtils.PRIME * result + value.hashCode();
    return result;
  }

}
