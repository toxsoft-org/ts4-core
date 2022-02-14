package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ETjKind;

class TjValueFalse
    extends TjValue {

  static final TjValue INSTANCE = new TjValueFalse();
  static final String literal = TsJsonValueStorage.KW_FALSE;

  TjValueFalse() {
    // nop
  }

  @Override
  public ETjKind kind() {
    return ETjKind.FALSE;
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
