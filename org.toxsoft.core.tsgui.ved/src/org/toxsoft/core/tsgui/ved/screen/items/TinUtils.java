package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Вспомогательные методы для работы с ITinXXX.
 *
 * @author vs
 */
public class TinUtils {

  /**
   * Возвращает {@link ITinTypeInfo}, содержащую общие для всех переданных элементов свойства.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактора
   * @param aItems IStridablesList&lt;IVedItem> - список элементов
   * @return {@link ITinTypeInfo} - общие для всех переданных элементов свойства
   */
  public static ITinTypeInfo createGroupTinTypeInfo( IVedScreen aVedScreen, IStridablesList<IVedItem> aItems ) {
    TsNullArgumentRtException.checkNulls( aVedScreen, aItems );
    TsIllegalArgumentRtException.checkTrue( aItems.size() < 0 );
    ITinTypeInfo ti = tinTypeInfo( aVedScreen, aItems.first() );
    IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>( ti.fieldInfos() );
    for( IVedItem item : aItems ) {
      fields = intersect( fields, tinTypeInfo( aVedScreen, item ).fieldInfos() );
    }
    return new PropertableEntitiesTinTypeInfo<>( fields, VedAbstractItem.class );
  }

  public static ITinTypeInfo tinTypeInfo( IVedScreen aVedScreen, IVedItem aItem ) {
    TsNullArgumentRtException.checkNulls( aVedScreen, aItem );
    if( aItem instanceof IVedVisel ) {
      IVedViselFactoriesRegistry facReg = aVedScreen.tsContext().get( IVedViselFactoriesRegistry.class );
      IVedViselFactory vFact = facReg.get( aItem.factoryId() );
      return vFact.typeInfo();
    }
    if( aItem instanceof IVedActor ) {
      IVedActorFactoriesRegistry facReg = aVedScreen.tsContext().get( IVedActorFactoriesRegistry.class );
      IVedActorFactory vFact = facReg.get( aItem.factoryId() );
      return vFact.typeInfo();
    }
    throw new TsIllegalArgumentRtException( "Wrong type of item (should be IVedVisel or IvedActor)" ); //$NON-NLS-1$
  }

  private static IStridablesListEdit<ITinFieldInfo> intersect( IStridablesListEdit<ITinFieldInfo> aList1,
      IStridablesList<ITinFieldInfo> aList2 ) {
    IStridablesListEdit<ITinFieldInfo> result = new StridablesList<>();
    for( int i = 0, n = aList2.size(); i < n; i++ ) {
      ITinFieldInfo value = aList2.get( i );
      if( aList1.hasElem( value ) ) {
        result.add( value );
      }
    }
    return result;
  }

  private static void foo( IStridablesList<ITinFieldInfo> aFields, IStridablesList<IVedItem> aItems ) {
    IStringMapEdit<IAtomicValue> values = new StringMap<>();

    // for( ITinFieldInfo fi: aFields ) {
    // for( IVedItem item : aItems ) {
    // if( !values.hasKey( fi.id() )) {
    // ITinValue tv = fi.typeInfo().makeValue( values );
    // values.put( fi.id(), item.props().getByKey( ) )
    // }
    // }
    // }
  }

  private TinUtils() {
    // nop
  }
}
