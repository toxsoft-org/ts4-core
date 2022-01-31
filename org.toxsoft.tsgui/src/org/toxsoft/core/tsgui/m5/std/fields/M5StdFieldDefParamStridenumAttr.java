package org.toxsoft.core.tsgui.m5.std.fields;

import org.toxsoft.core.tslib.av.EAtomicType;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.metainfo.IDataDef;
import org.toxsoft.core.tslib.av.metainfo.IDataType;
import org.toxsoft.core.tslib.av.utils.IParameterized;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link M5StdFieldDefParamAttr} extnsion for {@link IStridable} <code><b>enum</b>s</code> in
 * {@link EAtomicType#VALOBJ}.
 *
 * @author hazard157
 * @param <T> - concrete type of the {@link IParameterized} entity
 */
public class M5StdFieldDefParamStridenumAttr<T extends IParameterized>
    extends M5StdFieldDefParamAttr<T> {

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
  public M5StdFieldDefParamStridenumAttr( String aId, EAtomicType aAtomicType, Object... aIdsAndValues ) {
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
  public M5StdFieldDefParamStridenumAttr( String aId, IDataType aDataType ) {
    super( aId, aDataType );
  }

  /**
   * Constructor.
   *
   * @param aDataDef {@link IDataDef} - the template for attribute data definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public M5StdFieldDefParamStridenumAttr( IDataDef aDataDef ) {
    super( aDataDef );
  }

  // ------------------------------------------------------------------------------------
  // M5StdFieldDefParamStridenumAttr
  //

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    IAtomicValue val = doGetFieldValue( aEntity );
    IStridable s = IStridable.class.cast( val.asValobj() );
    return s.nmName();
  }

}
