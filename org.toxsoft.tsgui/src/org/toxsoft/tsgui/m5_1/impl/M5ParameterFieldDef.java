package org.toxsoft.tsgui.m5_1.impl;

import org.toxsoft.tsgui.m5.model.IM5AttributeFieldDef;
import org.toxsoft.tsgui.m5.model.impl.M5AttributeFieldDef;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.av.metainfo.IDataDef;
import org.toxsoft.tslib.av.utils.IParameterized;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IM5AttributeFieldDef} implementation for attributes hold as {@link IParameterized#params()} option.
 *
 * @author hazard157
 * @param <T> - modelled {@link IParameterized} entity type
 */
public class M5ParameterFieldDef<T extends IParameterized>
    extends M5AttributeFieldDef<T> {

  /**
   * Constructor.
   *
   * @param aId String - the field ID
   * @param aAtomicType {@link EAtomicType} - attribute atomic type
   * @param aIdsAndValues Object[] - identifier / value pairs
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public M5ParameterFieldDef( String aId, EAtomicType aAtomicType, Object... aIdsAndValues ) {
    super( aId, aAtomicType, aIdsAndValues );
  }

  /**
   * Constructor.
   *
   * @param aId String - the field ID
   * @param aDataDef {@link IDataDef} - the template for attribute data definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public M5ParameterFieldDef( String aId, IDataDef aDataDef ) {
    super( aId, aDataDef );
  }

  /**
   * Constructor.
   *
   * @param aDataDef {@link IDataDef} - the template for attribute data definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public M5ParameterFieldDef( IDataDef aDataDef ) {
    super( aDataDef );
  }

  // ------------------------------------------------------------------------------------
  // M5FieldDef
  //

  @Override
  protected IAtomicValue doGetFieldValue( T aEntity ) {
    return aEntity.params().getValue( id(), defaultValue() );
  }

}
