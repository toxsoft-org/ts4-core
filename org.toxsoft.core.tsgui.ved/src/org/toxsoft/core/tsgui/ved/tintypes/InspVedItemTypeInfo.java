package org.toxsoft.core.tsgui.ved.tintypes;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;

/**
 * Группа {@link ITinTypeInfo} для {@link IVedItem}.
 *
 * @author vs
 */
public class InspVedItemTypeInfo
    extends AbstractTinTypeInfo<IVedVisel> {

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  /**
   * Конструктор.
   *
   * @param aFieldInfoes IStridablesList&lt;ITinFieldInfo> - список полей
   */
  public InspVedItemTypeInfo( IStridablesList<ITinFieldInfo> aFieldInfoes ) {
    super( ETinTypeKind.GROUP, null, IVedVisel.class );
    for( ITinFieldInfo fieldInfo : aFieldInfoes ) {
      fieldInfos().add( fieldInfo );
    }
  }

  @Override
  protected ITinValue doGetNullTinValue() {
    IStringMapEdit<ITinValue> values = new StringMap<>();
    for( ITinFieldInfo fi : fieldInfos() ) {
      ITinValue tv = fi.typeInfo().makeValue( null );
      values.put( fi.id(), tv );
    }
    return TinValue.ofGroup( values );
  }

  @Override
  protected ITinValue doGetTinValue( IVedVisel aEntity ) {
    IOptionSet opSet = aEntity.props();

    IStringMapEdit<ITinValue> values = new StringMap<>();
    for( ITinFieldInfo fi : fieldInfos() ) {
      IAtomicValue av = opSet.getValue( fi.id() );
      ITinValue tv;
      if( fi.typeInfo().kind() != ETinTypeKind.ATOMIC ) {
        IStringMap<ITinValue> childVaues = fi.typeInfo().decompose( av );
        tv = TinValue.ofFull( av, childVaues );
      }
      else {
        tv = TinValue.ofAtomic( av );
      }
      values.put( fi.id(), tv );
    }
    return TinValue.ofGroup( values );
  }

}
