package org.toxsoft.core.tsgui.bricks.tin.impl;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinTypeInfo} implementation for {@link IPropertable} entities.
 *
 * @author vs
 * @author hazard157
 * @param <T> - the Java class of this type
 */
public class PropertableEntitiesTinTypeInfo<T extends IPropertable<?>>
    extends AbstractTinTypeInfo<T> {

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

  /**
   * Constructor.
   *
   * @param aFieldInfoes {@link IStridablesList}&lt;{@link ITinFieldInfo}&gt; - the field informations
   * @param aEntityClass {@link Class} - the Java class of this type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>null</code> data type for type with atomic values
   */
  public PropertableEntitiesTinTypeInfo( IStridablesList<ITinFieldInfo> aFieldInfoes, Class<T> aEntityClass ) {
    super( ETinTypeKind.GROUP, null, aEntityClass );
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
  protected ITinValue doGetTinValue( T aEntity ) {
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
