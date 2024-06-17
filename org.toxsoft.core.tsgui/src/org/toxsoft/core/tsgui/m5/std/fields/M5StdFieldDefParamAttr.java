package org.toxsoft.core.tsgui.m5.std.fields;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.gui.*;
import org.toxsoft.core.tslib.utils.valobj.*;

/**
 * {@link IM5AttributeFieldDef} implementation for field values stored in {@link IParameterized#params()}.
 * <p>
 * The field ID {@link #id()} is used as key to access values in {@link IParameterized#params()}.
 *
 * @author hazard157
 * @param <T> - concrete type of the {@link IParameterized} entity
 */
public class M5StdFieldDefParamAttr<T extends IParameterized>
    extends M5AttributeFieldDef<T> {

  private boolean isIconIdable = false;

  /**
   * Constructor.
   *
   * @param aId String - the field ID is used as key to access values in {@link IParameterized#params()}
   * @param aAtomicType {@link EAtomicType} - attribute atomic type
   * @param aIdsAndValues Object[] - identifier / value pairs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public M5StdFieldDefParamAttr( String aId, EAtomicType aAtomicType, Object... aIdsAndValues ) {
    super( aId, aAtomicType, aIdsAndValues );
  }

  /**
   * Constructor.
   *
   * @param aId String - the field ID is used as key to access values in {@link IParameterized#params()}
   * @param aDataType {@link IDataDef} - the template for attribute data definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public M5StdFieldDefParamAttr( String aId, IDataType aDataType ) {
    super( aId, aDataType );
  }

  /**
   * Constructor.
   *
   * @param aDataDef {@link IDataDef} - the template for attribute data definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public M5StdFieldDefParamAttr( IDataDef aDataDef ) {
    super( aDataDef );
    if( aDataDef.atomicType() == EAtomicType.VALOBJ ) {
      String keeperId = aDataDef.params().getStr( TSID_KEEPER_ID, null );
      if( keeperId != null ) {
        IEntityKeeper<?> keeper = TsValobjUtils.findKeeperById( keeperId );
        if( keeper != null ) {
          isIconIdable = IIconIdable.class.isAssignableFrom( keeper.entityClass() );
        }
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return aEntity.params().getValue( id(), defaultValue() );
  }

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    IAtomicValue val = doGetFieldValue( aEntity );
    if( isIconIdable ) {
      String iconId = IIconIdable.class.cast( val.asValobj() ).iconId();
      if( iconId != null ) {
        return TsLibUtils.EMPTY_STRING;
      }
    }
    String fmtStr = aEntity.params().getStr( TSID_FORMAT_STRING, null );
    return AvUtils.printAv( fmtStr, val );
  }

  @Override
  protected Image doGetFieldValueIcon( T aEntity, EIconSize aIconSize ) {
    if( isIconIdable && aEntity != null ) {
      String iconId = IIconIdable.class.cast( doGetFieldValue( aEntity ).asValobj() ).iconId();
      if( iconId != null ) {
        return iconManager().loadStdIcon( iconId, aIconSize );
      }
    }
    return null;
  }

}
