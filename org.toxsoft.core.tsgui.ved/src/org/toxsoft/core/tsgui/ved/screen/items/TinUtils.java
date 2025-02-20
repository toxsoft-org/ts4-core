package org.toxsoft.core.tsgui.ved.screen.items;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
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
  public static ITinTypeInfo createGroupTinTypeInfo( IVedScreen aVedScreen,
      IStridablesList<? extends IVedItem> aItems ) {
    TsNullArgumentRtException.checkNulls( aVedScreen, aItems );
    TsIllegalArgumentRtException.checkTrue( aItems.size() < 0 );
    ITinTypeInfo ti = tinTypeInfo( aVedScreen, aItems.first() );
    IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>( ti.fieldInfos() );
    for( IVedItem item : aItems ) {
      fields = intersect( fields, tinTypeInfo( aVedScreen, item ).fieldInfos() );
    }
    return new PropertableEntitiesTinTypeInfo<>( fields, VedAbstractItem.class );
  }

  /**
   * Возвращает {@link ITinValue}, содержащее общие для всех переданных элементов значенния свойств.
   *
   * @param aVedScreen {@link IVedScreen} - экран редактора
   * @param aItems IStridablesList&lt;IVedItem> - список элементов
   * @return {@link ITinTypeInfo} - общие для всех переданных элементов значения свойств свойства
   */
  public static ITinValue createGroupTinValue( IVedScreen aVedScreen, IStridablesList<? extends IVedItem> aItems ) {
    TsNullArgumentRtException.checkNulls( aVedScreen, aItems );
    TsIllegalArgumentRtException.checkTrue( aItems.size() < 0 );

    ITinTypeInfo typeInfo = createGroupTinTypeInfo( aVedScreen, aItems );

    IStringMapEdit<ITinValue> values = new StringMap<>();

    for( ITinFieldInfo fi : typeInfo.fieldInfos() ) {
      values.put( fi.id(), makeFieldValue( fi, aItems, aVedScreen.tsContext() ) );
    }
    return TinValue.ofGroup( values );
  }

  /**
   * Создает {@link ITinValue} из набора свойств полей.
   *
   * @param aOpSet {@link IOptionSet} - набор свойств полей
   * @param aTinType {@link ITinTypeInfo} - типы полей
   * @return {@link ITinValue} - значение для инспектора
   */
  public static ITinValue optionSet2TinValue( IOptionSet aOpSet, ITinTypeInfo aTinType ) {
    IStringMapEdit<ITinValue> values = new StringMap<>();
    for( ITinFieldInfo fi : aTinType.fieldInfos() ) {
      ITinValue tv = fi.typeInfo().makeValue( aOpSet.getValue( fi.id() ).asValobj() );
      values.put( fi.id(), tv );
    }
    return TinValue.ofGroup( values );
  }

  /**
   * Возвращает значения полей инспектора в виде набора свойств.
   *
   * @param aValue {@link TinValue} - значение инспектора свойств
   * @param aType {@link ITinTypeInfo} - тип значения в инспекторе свойств
   * @return {@link IOptionSet} - значения полей инспектора в виде набора свойств
   */
  public static IOptionSet tinValue2OptionSet( ITinValue aValue, ITinTypeInfo aType ) {
    IOptionSetEdit opSet = new OptionSet();
    for( ITinFieldInfo fi : aType.fieldInfos() ) {
      if( aValue.childValues().hasKey( fi.id() ) ) {
        ITinValue ftv = aValue.childValues().getByKey( fi.id() );
        if( ftv.kind() == ETinTypeKind.ATOMIC ) {
          opSet.setValue( fi.id(), ftv.atomicValue() );
        }
        if( ftv.kind() == ETinTypeKind.FULL ) {
          IAtomicValue av = fi.typeInfo().compose( ftv.childValues() );
          opSet.setValue( fi.id(), av );
        }
      }
      else {
        opSet.setValue( fi.id(), IAtomicValue.NULL );
      }
    }
    return opSet;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static ITinTypeInfo tinTypeInfo( IVedScreen aVedScreen, IVedItem aItem ) {
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

  private static ITinValue makeFieldValue( ITinFieldInfo aInfo, IStridablesList<? extends IVedItem> aItems,
      ITsGuiContext aCtx ) {
    ITinValue currTv = null;

    for( IVedItem item : aItems ) {
      IVedItemFactoryBase<?> factory = getFactory( item, aCtx );
      ITinValue tv = factory.typeInfo().makeValue( item );

      tv = tv.childValues().getByKey( aInfo.id() );

      if( currTv == null ) {
        currTv = tv;
        continue;
      }
      if( !currTv.equals( tv ) ) {
        return ITinValue.NULL;
      }
    }
    return currTv;
  }

  private static IVedItemFactoryBase<?> getFactory( IVedItem aVedItem, ITsGuiContext aTsContext ) {
    return switch( aVedItem.kind() ) {
      case VISEL -> {
        IVedViselFactoriesRegistry facReg = aTsContext.get( IVedViselFactoriesRegistry.class );
        yield facReg.get( aVedItem.factoryId() );
      }
      case ACTOR -> {
        IVedActorFactoriesRegistry facReg = aTsContext.get( IVedActorFactoriesRegistry.class );
        yield facReg.get( aVedItem.factoryId() );
      }
      default -> throw new TsNotAllEnumsUsedRtException( aVedItem.kind().id() );
    };
  }

  private TinUtils() {
    // nop
  }
}
