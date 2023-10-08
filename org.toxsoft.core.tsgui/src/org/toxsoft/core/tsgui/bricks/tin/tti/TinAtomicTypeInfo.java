package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinTypeInfo} implementation base for TIN type info of kind {@link ETinTypeKind#ATOMIC}.
 *
 * @author hazard157
 * @param <T> - the Java class of this type
 */
public abstract class TinAtomicTypeInfo<T>
    extends AbstractTinTypeInfo<T> {

  /**
   * {@link TinAtomicTypeInfo} for {@link Integer}.
   *
   * @author hazard157
   */
  public static class TtiInteger
      extends TinAtomicTypeInfo<Integer> {

    /**
     * Constructor.
     *
     * @param aDataType {@link IDataType} - the data type
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     */
    public TtiInteger( IDataType aDataType ) {
      super( aDataType, Integer.class );
      TsIllegalArgumentRtException.checkTrue( aDataType.atomicType() != EAtomicType.INTEGER );
    }

    @Override
    protected IAtomicValue doGetAtomicValue( Integer aEntity ) {
      return avInt( aEntity.intValue() );
    }

  }

  /**
   * {@link TinAtomicTypeInfo} for {@link Long}.
   *
   * @author hazard157
   */
  public static class TtiLong
      extends TinAtomicTypeInfo<Long> {

    /**
     * Constructor.
     *
     * @param aDataType {@link IDataType} - the data type
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     */
    public TtiLong( IDataType aDataType ) {
      super( aDataType, Long.class );
      TsIllegalArgumentRtException.checkTrue(
          aDataType.atomicType() != EAtomicType.INTEGER || aDataType.atomicType() != EAtomicType.TIMESTAMP );
    }

    @Override
    protected IAtomicValue doGetAtomicValue( Long aEntity ) {
      return avInt( aEntity.longValue() );
    }

  }

  private final ITinValue tinNullVal;

  /**
   * Constructor.
   *
   * @param aDataType {@link IDataType} - the atomic data type
   * @param aEntityClass {@link Class} - the Java class of this type
   * @param aNullValue {@link IAtomicValue} - the value returned for <code>null</code> entity
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TinAtomicTypeInfo( IDataType aDataType, Class<T> aEntityClass, IAtomicValue aNullValue ) {
    super( ETinTypeKind.ATOMIC, aDataType, aEntityClass );
    tinNullVal = TinValue.ofAtomic( aNullValue );
  }

  /**
   * Constructor.
   * <p>
   * The value returned for <code>null</code> entity by {@link ITinTypeInfo#makeValue(Object)} will by initialized as
   * the default value retrieved from the argument <code>aDataType</code>.
   *
   * @param aDataType {@link IDataType} - the atomic data type
   * @param aEntityClass {@link Class} - the Java class of this type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public TinAtomicTypeInfo( IDataType aDataType, Class<T> aEntityClass ) {
    super( ETinTypeKind.ATOMIC, aDataType, aEntityClass );
    IAtomicValue defVal = aDataType.params().getValue( TSID_DEFAULT_VALUE, IAtomicValue.NULL );
    tinNullVal = TinValue.ofAtomic( defVal );
  }

  @Override
  final protected ITinValue doGetNullTinValue() {
    return tinNullVal;
  }

  @Override
  final protected ITinValue doGetTinValue( T aEntity ) {
    return TinValue.ofAtomic( doGetAtomicValue( aEntity ) );
  }

  /**
   * Subclass must return the atomic value representation of the entity.
   *
   * @param aEntity &lt;T&gt; - the entity, never is <code>null</code>
   * @return {@link IAtomicValue} - the atomic value representation of the entity
   */
  protected abstract IAtomicValue doGetAtomicValue( T aEntity );

}
