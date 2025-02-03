package org.toxsoft.core.tsgui.m5.model.impl;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.validators.defav.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5AttributeFieldDef} implementation.
 * <p>
 * Note: default value is stored in {@link #params()}, not in internal field as in superclass.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5AttributeFieldDef<T>
    extends M5FieldDef<T, IAtomicValue>
    implements IM5AttributeFieldDef<T> {

  private EAtomicType attrType;

  /**
   * Constructor.
   *
   * @param aId String - the field ID
   * @param aAtomicType {@link EAtomicType} - attribute atomic type
   * @param aIdsAndValues Object[] - identifier / value pairs of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public M5AttributeFieldDef( String aId, EAtomicType aAtomicType, Object... aIdsAndValues ) {
    super( aId, IAtomicValue.class, null );
    TsNullArgumentRtException.checkNull( aAtomicType );
    attrType = aAtomicType;
    params().addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    validator().addValidator( new DefaultAvValidator( this ) );
    setComparator( AvUtils.DEFAULT_AV_COMPARATOR );
  }

  /**
   * Constructor.
   *
   * @param aId String - the field ID
   * @param aDataType {@link IDataDef} - the template for attribute data definition
   * @param aIdsAndValues Object[] - identifier / value pairs of {@link #params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public M5AttributeFieldDef( String aId, IDataType aDataType, Object... aIdsAndValues ) {
    super( aId, IAtomicValue.class, null );
    TsNullArgumentRtException.checkNull( aDataType );
    attrType = aDataType.atomicType();
    params().addAll( aDataType.params() );
    params().addAll( OptionSetUtils.createOpSet( aIdsAndValues ) );
    setFlags( M5_OPDEF_FLAGS.getValue( params() ).asInt() );
    if( aDataType instanceof IDataDef ddef ) {
      validator().addValidator( ddef.validator() );
      setComparator( ddef.comparator() );
    }
    else {
      validator().addValidator( new DefaultAvValidator( this ) );
      setComparator( AvUtils.DEFAULT_AV_COMPARATOR );
    }
  }

  /**
   * Constructor.
   *
   * @param aDataDef {@link IDataDef} - the template for attribute data definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public M5AttributeFieldDef( IDataDef aDataDef ) {
    this( TsNullArgumentRtException.checkNull( aDataDef ).id(), aDataDef );
  }

  // ------------------------------------------------------------------------------------
  // IM5AttributeFieldDef
  //

  @Override
  public EAtomicType atomicType() {
    return attrType;
  }

  @Override
  public IAtomicValue defaultValue() {
    return params().getValue( TSID_DEFAULT_VALUE, IAtomicValue.NULL );
  }

  // ------------------------------------------------------------------------------------
  // M5FieldDef
  //

  @Override
  protected String doGetFieldValueName( T aEntity ) {
    String formatString = params().getStr( TSID_FORMAT_STRING, null );
    if( formatString == null ) {
      formatString = params().getStr( TSID_FORMAT_STRING, null );
    }
    IAtomicValue av = getFieldValue( aEntity );
    return AvUtils.printAv( formatString, av );
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * For <code>null</code> argument sets {@link #defaultValue()} to {@link IAtomicValue#NULL}.
   */
  @Override
  public void setDefaultValue( IAtomicValue aValue ) {
    params().setValue( TSID_DEFAULT_VALUE, aValue != null ? aValue : IAtomicValue.NULL );
  }

}
