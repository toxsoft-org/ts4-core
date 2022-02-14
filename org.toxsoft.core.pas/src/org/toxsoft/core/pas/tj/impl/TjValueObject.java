package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ETjKind;
import org.toxsoft.core.pas.tj.ITjObject;
import org.toxsoft.core.tslib.utils.TsLibUtils;

class TjValueObject
    extends TjValue {

  private ITjObject value;

  public TjValueObject( ITjObject aObject ) {
    value = aObject;
  }

  @Override
  public ETjKind kind() {
    return ETjKind.OBJECT;
  }

  @Override
  public ITjObject asObject() {
    return value;
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
    if( aThat instanceof TjValueObject that ) {
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
