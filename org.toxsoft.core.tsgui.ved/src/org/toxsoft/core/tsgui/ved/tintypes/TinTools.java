package org.toxsoft.core.tsgui.ved.tintypes;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

public class TinTools {

  public static IAtomicValue getValue( String aId, IStringMap<ITinValue> aChildValues ) {
    if( aChildValues.hasKey( aId ) ) {
      ITinValue tv = aChildValues.getByKey( aId );
      if( tv != ITinValue.NULL ) {
        return tv.atomicValue();
      }
    }
    return IAtomicValue.NULL;
  }

  public static IAtomicValue getValue( String aId, IStringMap<ITinValue> aChildValues, IAtomicValue aDefautValue ) {
    if( aChildValues.hasKey( aId ) ) {
      ITinValue tv = aChildValues.getByKey( aId );
      if( tv != ITinValue.NULL ) {
        if( tv.atomicValue().isAssigned() ) {
          return tv.atomicValue();
        }
      }
    }
    return aDefautValue;
  }

  /**
   * Запрет на создание экземпляров.
   */
  private TinTools() {
    // nop
  }
}
