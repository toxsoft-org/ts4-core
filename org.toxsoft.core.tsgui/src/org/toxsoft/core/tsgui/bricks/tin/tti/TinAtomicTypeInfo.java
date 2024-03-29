package org.toxsoft.core.tsgui.bricks.tin.tti;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
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
   * {@link TinAtomicTypeInfo} for {@link Boolean}.
   *
   * @author hazard157
   */
  public static class TtiBoolean
      extends TinAtomicTypeInfo<Boolean> {

    /**
     * Constructor.
     *
     * @param aDataType {@link IDataType} - the data type
     * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     * @throws TsIllegalArgumentRtException number of elements in array is uneven
     * @throws ClassCastException argument types convention is violated
     */
    public TtiBoolean( IDataType aDataType, Object... aIdsAndValues ) {
      super( DataType.create( aDataType, aIdsAndValues ), Boolean.class );
      TsIllegalArgumentRtException.checkTrue( aDataType.atomicType() != EAtomicType.BOOLEAN );

    }

    @Override
    protected IAtomicValue doGetAtomicValue( Boolean aEntity ) {
      return avBool( aEntity.booleanValue() );
    }

  }

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
     * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     * @throws TsIllegalArgumentRtException number of elements in array is uneven
     * @throws ClassCastException argument types convention is violated
     */
    public TtiInteger( IDataType aDataType, Object... aIdsAndValues ) {
      super( DataType.create( aDataType, aIdsAndValues ), Integer.class );
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
     * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     * @throws TsIllegalArgumentRtException number of elements in array is uneven
     * @throws ClassCastException argument types convention is violated
     */
    public TtiLong( IDataType aDataType, Object... aIdsAndValues ) {
      super( DataType.create( aDataType, aIdsAndValues ), Long.class );
      TsIllegalArgumentRtException.checkTrue(
          aDataType.atomicType() != EAtomicType.INTEGER && aDataType.atomicType() != EAtomicType.TIMESTAMP );
    }

    @Override
    protected IAtomicValue doGetAtomicValue( Long aEntity ) {
      return avInt( aEntity.longValue() );
    }

  }

  /**
   * {@link TinAtomicTypeInfo} for {@link Float}.
   *
   * @author hazard157
   */
  public static class TtiFloat
      extends TinAtomicTypeInfo<Float> {

    /**
     * Constructor.
     *
     * @param aDataType {@link IDataType} - the data type
     * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     * @throws TsIllegalArgumentRtException number of elements in array is uneven
     * @throws ClassCastException argument types convention is violated
     */
    public TtiFloat( IDataType aDataType, Object... aIdsAndValues ) {
      super( DataType.create( aDataType, aIdsAndValues ), Float.class );
      TsIllegalArgumentRtException.checkTrue( aDataType.atomicType() != EAtomicType.FLOATING );
    }

    @Override
    protected IAtomicValue doGetAtomicValue( Float aEntity ) {
      return avFloat( aEntity.floatValue() );
    }

  }

  /**
   * {@link TinAtomicTypeInfo} for {@link Double}.
   *
   * @author hazard157
   */
  public static class TtiDouble
      extends TinAtomicTypeInfo<Double> {

    /**
     * Constructor.
     *
     * @param aDataType {@link IDataType} - the data type
     * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     * @throws TsIllegalArgumentRtException number of elements in array is uneven
     * @throws ClassCastException argument types convention is violated
     */
    public TtiDouble( IDataType aDataType, Object... aIdsAndValues ) {
      super( DataType.create( aDataType, aIdsAndValues ), Double.class );
      TsIllegalArgumentRtException.checkTrue( aDataType.atomicType() != EAtomicType.FLOATING );
    }

    @Override
    protected IAtomicValue doGetAtomicValue( Double aEntity ) {
      return avFloat( aEntity.doubleValue() );
    }

  }

  /**
   * {@link TinAtomicTypeInfo} for {@link String}.
   *
   * @author hazard157
   */
  public static class TtiString
      extends TinAtomicTypeInfo<String> {

    /**
     * Constructor.
     *
     * @param aDataType {@link IDataType} - the data type
     * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     * @throws TsIllegalArgumentRtException number of elements in array is uneven
     * @throws ClassCastException argument types convention is violated
     */
    public TtiString( IDataType aDataType, Object... aIdsAndValues ) {
      super( DataType.create( aDataType, aIdsAndValues ), String.class );
      TsIllegalArgumentRtException.checkTrue( aDataType.atomicType() != EAtomicType.STRING );
    }

    @Override
    protected IAtomicValue doGetAtomicValue( String aEntity ) {
      return avStr( aEntity );
    }

  }

  /**
   * {@link TinAtomicTypeInfo} for VALOBJ objects.
   *
   * @author hazard157
   * @param <T> - class of value-object
   */
  public static class TtiValobj<T>
      extends TinAtomicTypeInfo<T> {

    /**
     * Constructor.
     *
     * @param aDataType {@link IDataType} - the data type
     * @param aValueObjectClass {@link Class} - class of value-object
     * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
     * @throws TsNullArgumentRtException any argument = <code>null</code>
     * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
     * @throws TsIllegalArgumentRtException number of elements in array is uneven
     * @throws ClassCastException argument types convention is violated
     */
    public TtiValobj( IDataType aDataType, Class<T> aValueObjectClass, Object... aIdsAndValues ) {
      super( DataType.create( aDataType, aIdsAndValues ), aValueObjectClass );
      TsIllegalArgumentRtException.checkTrue( aDataType.atomicType() != EAtomicType.VALOBJ );
    }

    @Override
    protected IAtomicValue doGetAtomicValue( T aEntity ) {
      return avValobj( aEntity );
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
    IAtomicValue defVal = aDataType.params().getValue( TSID_DEFAULT_VALUE, aDataType.atomicType().defaultValue() );
    tinNullVal = TinValue.ofAtomic( defVal );
  }

  /**
   * Static constructor of {@link TinAtomicTypeInfo} for {@link Boolean}.
   *
   * @param aDataType {@link IDataType} - the data type
   * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
   * @return {@link TinAtomicTypeInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static TinAtomicTypeInfo<Boolean> ofBoolean( IDataType aDataType, Object... aIdsAndValues ) {
    return new TtiBoolean( aDataType, aIdsAndValues );
  }

  /**
   * Static constructor of {@link TinAtomicTypeInfo} for {@link Integer}.
   *
   * @param aDataType {@link IDataType} - the data type
   * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
   * @return {@link TinAtomicTypeInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static TinAtomicTypeInfo<Integer> ofInteger( IDataType aDataType, Object... aIdsAndValues ) {
    return new TtiInteger( aDataType, aIdsAndValues );
  }

  /**
   * Static constructor of {@link TinAtomicTypeInfo} for {@link Long}.
   *
   * @param aDataType {@link IDataType} - the data type
   * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
   * @return {@link TinAtomicTypeInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static TinAtomicTypeInfo<Long> ofLong( IDataType aDataType, Object... aIdsAndValues ) {
    return new TtiLong( aDataType, aIdsAndValues );
  }

  /**
   * Static constructor of {@link TinAtomicTypeInfo} for {@link Float}.
   *
   * @param aDataType {@link IDataType} - the data type
   * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
   * @return {@link TinAtomicTypeInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static TinAtomicTypeInfo<Float> ofFloat( IDataType aDataType, Object... aIdsAndValues ) {
    return new TtiFloat( aDataType, aIdsAndValues );
  }

  /**
   * Static constructor of {@link TinAtomicTypeInfo} for {@link Double}.
   *
   * @param aDataType {@link IDataType} - the data type
   * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
   * @return {@link TinAtomicTypeInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static TinAtomicTypeInfo<Double> ofDouble( IDataType aDataType, Object... aIdsAndValues ) {
    return new TtiDouble( aDataType, aIdsAndValues );
  }

  /**
   * Static constructor of {@link TinAtomicTypeInfo} for {@link String}.
   *
   * @param aDataType {@link IDataType} - the data type
   * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
   * @return {@link TinAtomicTypeInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static TinAtomicTypeInfo<String> ofString( IDataType aDataType, Object... aIdsAndValues ) {
    return new TtiString( aDataType, aIdsAndValues );
  }

  /**
   * Static constructor of {@link TinAtomicTypeInfo} for value-object.
   *
   * @param <T> - class of value-object
   * @param aDataType {@link IDataType} - the data type
   * @param aValueObjectClass {@link Class} - class of value-object
   * @param aIdsAndValues Object[] - identifier / value pairs of additional {@link IDataType#params()} options
   * @return {@link TinAtomicTypeInfo} - created instance
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException argument atomic type is not compatible with the entity class
   * @throws TsIllegalArgumentRtException number of elements in array is uneven
   * @throws ClassCastException argument types convention is violated
   */
  public static <T> TinAtomicTypeInfo<T> ofValobj( IDataType aDataType, Class<T> aValueObjectClass,
      Object... aIdsAndValues ) {
    return new TtiValobj<>( aDataType, aValueObjectClass, aIdsAndValues );
  }

  // ------------------------------------------------------------------------------------
  // AbstractTinTypeInfo
  //

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
