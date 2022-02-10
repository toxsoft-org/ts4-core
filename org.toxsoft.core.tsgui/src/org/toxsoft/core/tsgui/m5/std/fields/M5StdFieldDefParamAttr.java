package org.toxsoft.core.tsgui.m5.std.fields;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.IM5AttributeFieldDef;
import org.toxsoft.core.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataType;
import org.toxsoft.core.tslib.av.utils.IParameterized;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

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
  }

  // ------------------------------------------------------------------------------------
  // M5AttributeFieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    IAtomicValue value = aEntity.params().findByKey( id() );
    if( value == null ) {
      value = defaultValue();
    }
    return value;
  }

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    IAtomicValue val = doGetFieldValue( aEntity );
    String fmtStr = aEntity.params().getStr( TSID_FORMAT_STRING, null );
    return AvUtils.printAv( fmtStr, val );
  }

}
