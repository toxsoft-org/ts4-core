package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ETjKind;
import org.toxsoft.core.tslib.utils.TsLibUtils;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

class TjValueString
    extends TjValue {

  private String value;

  public TjValueString( String aString ) {
    value = aString;
  }

  @Override
  public ETjKind kind() {
    return ETjKind.STRING;
  }

  @Override
  public String asString() {
    return value;
  }

  @Override
  public void setString( String aString ) {
    if( aString == null ) {
      throw new TsNullArgumentRtException();
    }
    value = aString;
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
    if( aThat instanceof TjValueString that ) {
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
