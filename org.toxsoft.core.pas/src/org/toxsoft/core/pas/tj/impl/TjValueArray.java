package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ETjKind;
import org.toxsoft.core.pas.tj.ITjValue;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.coll.IListEdit;
import org.toxsoft.core.tslib.coll.impl.ElemLinkedBundleList;
import org.toxsoft.core.tslib.utils.TsLibUtils;

class TjValueArray
    extends TjValue {

  private final IListEdit<ITjValue> value;

  public TjValueArray( IList<ITjValue> aItems ) {
    value = new ElemLinkedBundleList<>( aItems );
  }

  @Override
  public ETjKind kind() {
    return ETjKind.ARRAY;
  }

  @Override
  public IListEdit<ITjValue> asArray() {
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
    if( aThat instanceof TjValueArray that ) {
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
