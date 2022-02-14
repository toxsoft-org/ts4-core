package org.toxsoft.core.pas.tj.impl;

import org.toxsoft.core.pas.tj.ETjKind;

class TjValueTrue
    extends TjValue {

  static final TjValue INSTANCE = new TjValueTrue();
  static final String literal = TsJsonValueStorage.KW_TRUE;

  TjValueTrue() {
    // nop
  }

  @Override
  public ETjKind kind() {
    return ETjKind.TRUE;
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
