package org.toxsoft.core.tsgui.bricks.tin;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

@SuppressWarnings( "javadoc" )
public interface ITinValue {

  ITinValue NULL = new InternalNullTinValue();

  ETinTypeKind kind();

  IAtomicValue atomicValue();

  IStringMap<ITinValue> childValues();

}

class InternalNullTinValue
    implements ITinValue {

  @Override
  public ETinTypeKind kind() {
    return ETinTypeKind.FULL;
  }

  @Override
  public IAtomicValue atomicValue() {
    return IAtomicValue.NULL;
  }

  @Override
  public IStringMap<ITinValue> childValues() {
    return IStringMap.EMPTY;
  }

}
