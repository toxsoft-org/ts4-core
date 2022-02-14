package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ETjKind;

class TjValueNull
    extends TjValue {

  static final TjValue INSTANCE = new TjValueNull();
  static final String literal = TsJsonValueStorage.KW_NULL;

  TjValueNull() {
    // nop
  }

  @Override
  public ETjKind kind() {
    return ETjKind.NULL;
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов класса Object
  //

  @Override
  public String toString() {
    return literal;
  }

  @Override
  public boolean equals( Object aThat ) {
    return aThat == this;
  }

  @Override
  public int hashCode() {
    return literal.hashCode();
  }

}
