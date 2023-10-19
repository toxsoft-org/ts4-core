package org.toxsoft.core.tsgui.bricks.tin.impl;

import static org.toxsoft.core.tsgui.bricks.tin.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ITinTypeInfo} implementation base.
 *
 * @author hazard157
 * @param <T> - the Java class of this type
 */
public abstract class AbstractTinTypeInfo<T>
    implements ITinTypeInfo {

  private final IStridablesListEdit<ITinFieldInfo> fieldInfos = new StridablesList<>();

  /**
   * Field with lazy initialization in {@link #valueVisualizer()}.
   */
  private ITsVisualsProvider<ITinValue> valueVisualizer = null;

  private final ETinTypeKind kind;
  private final IDataType    dataType;
  private final Class<T>     entityClass;

  /**
   * Constructor.
   * <p>
   * For types with atomic value representation {@link ETinTypeKind#hasAtomic()} = <code>true</code> the
   * non-<code>null</code> data type must be specified.
   *
   * @param aKind {@link ETinTypeKind} - the type kind
   * @param aDataType {@link IDataType} - the atomic data type or <code>null</code>
   * @param aEntityClass {@link Class} - the Java class of this type
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException <code>null</code> data type for type with atomic values
   */
  public AbstractTinTypeInfo( ETinTypeKind aKind, IDataType aDataType, Class<T> aEntityClass ) {
    TsNullArgumentRtException.checkNulls( aKind, aEntityClass );
    kind = aKind;
    if( kind.hasAtomic() && aDataType == null ) {
      throw new TsIllegalArgumentRtException();
    }
    dataType = aDataType;
    entityClass = aEntityClass;
  }

  // ------------------------------------------------------------------------------------
  // ITinTypeInfo
  //

  @Override
  final public ETinTypeKind kind() {
    return kind;
  }

  @Override
  final public IDataType dataType() {
    TsUnsupportedFeatureRtException.checkFalse( kind.hasAtomic() );
    TsInternalErrorRtException.checkNull( dataType );
    return dataType;
  }

  @Override
  final public IStridablesListEdit<ITinFieldInfo> fieldInfos() {
    return fieldInfos;
  }

  final public IStringList visibleFieldIds( ITinValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    IStringList vfIds = doGetVisibleFieldIds( aValue );
    for( String vfId : vfIds ) {
      TsInternalErrorRtException.checkFalse( fieldInfos.hasKey( vfId ) );
    }
    return vfIds;
  }

  @Override
  public ITinValue applyFieldChange( ITinValue aOldValue, String aFieldId, ITinValue aChildFieldNewValue ) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public ITsVisualsProvider<ITinValue> valueVisualizer() {
    if( valueVisualizer == null ) {
      valueVisualizer = new DefaultValueVisualizer( this );
    }
    return valueVisualizer;
  }

  @Override
  public IAtomicValue compose( IStringMap<ITinValue> aChildValues ) {
    TsUnsupportedFeatureRtException.checkFalse( kind.hasAtomic() );
    IStringMapEdit<ITinValue> childVals = new StringMap<>();
    for( ITinFieldInfo finf : fieldInfos ) {
      ITinValue fieldVal;
      if( aChildValues != null && aChildValues.hasKey( finf.id() ) ) {
        fieldVal = aChildValues.getByKey( finf.id() );
      }
      else {
        fieldVal = finf.defaultValue();
      }
      childVals.put( finf.id(), fieldVal );
    }
    return doCompose( childVals );
  }

  @Override
  public ValidationResult canDecompose( IAtomicValue aValue ) {
    if( !kind.hasAtomic() ) {
      return ValidationResult.error( MSG_ERR_NOT_ATOMIC_TI );
    }
    if( aValue == null ) {
      return ValidationResult.SUCCESS;
    }
    if( !AvTypeCastRtException.canAssign( dataType.atomicType(), aValue.atomicType() ) ) {
      return ValidationResult.error( FMT_ERR_INCOMPATIBLE_AT, dataType.atomicType().id(), aValue.atomicType().id() );
    }
    return doCanDecompose( aValue );
  }

  @Override
  public IStringMap<ITinValue> decompose( IAtomicValue aValue ) {
    TsValidationFailedRtException.checkError( canDecompose( aValue ) );
    IStringMapEdit<ITinValue> childValues = new StringMap<>();
    doDecompose( aValue, childValues );
    return childValues;
  }

  @Override
  public ITinValue makeValue( Object aEntity ) {
    ITinValue tinValue;
    if( aEntity != null ) {
      T entity = entityClass.cast( aEntity );
      TsIllegalArgumentRtException.checkFalse( entityClass.isInstance( aEntity ) );
      tinValue = doGetTinValue( entity );
    }
    else {
      return doGetNullTinValue();
    }
    TsInternalErrorRtException.checkTrue( tinValue.kind() != kind() );
    return tinValue;
  }

  @Override
  final public T makeEntity( ITinValue aValue ) {
    TsNullArgumentRtException.checkNull( aValue );
    Object instance = doCreateEntity( aValue );
    return entityClass.cast( instance );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets the {@link #valueVisualizer()}.
   * <p>
   * By default the instance of visualizer is used created with constructor
   * {@link DefaultValueVisualizer#DefaultValueVisualizer(ITinTypeInfo)}.
   *
   * @param aVisualizer {@link ITsVisualsProvider}&lt;{@link ITinValue}&gt; - value visualizer
   */
  public void setValueVisualizer( ITsVisualsProvider<ITinValue> aVisualizer ) {
    TsNullArgumentRtException.checkNull( aVisualizer );
    valueVisualizer = aVisualizer;
  }

  // ------------------------------------------------------------------------------------
  // Helper API for subclasses
  //

  /**
   * Extracts atomic value from {@link ETinTypeKind#ATOMIC} child field.
   *
   * @param aFieldId String - the field ID
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - the child values
   * @param aDefaultValue {@link IAtomicValue} - the value returned when no child found
   * @return {@link IAtomicValue} - atomic value of the specified child field
   * @throws TsIllegalArgumentRtException found child kind {@link ETinTypeKind#hasAtomic()} != <code>true</code>
   */
  public IAtomicValue extractChildAtomic( String aFieldId, IStringMap<ITinValue> aChildValues,
      IAtomicValue aDefaultValue ) {
    TsNullArgumentRtException.checkNulls( aFieldId, aChildValues );
    ITinValue tinValue = aChildValues.findByKey( aFieldId );
    if( tinValue == null ) {
      return aDefaultValue;
    }
    TsIllegalArgumentRtException.checkFalse( tinValue.kind().hasAtomic() );
    return tinValue.atomicValue();
  }

  /**
   * Extracts atomic value from {@link ETinTypeKind#ATOMIC} child field as <code>int</code>.
   *
   * @param aFieldId String - the field ID
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - the child values
   * @param aDefaultValue int - the value returned when no child found
   * @return int - value of the specified child field
   * @throws TsIllegalArgumentRtException found child kind {@link ETinTypeKind#hasAtomic()} != <code>true</code>
   */
  public int extractChildInt( String aFieldId, IStringMap<ITinValue> aChildValues, int aDefaultValue ) {
    IAtomicValue av = extractChildAtomic( aFieldId, aChildValues, null );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asInt();
  }

  /**
   * Extracts atomic value from {@link ETinTypeKind#ATOMIC} child field as <code>long</code>.
   *
   * @param aFieldId String - the field ID
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - the child values
   * @param aDefaultValue long - the value returned when no child found
   * @return long - value of the specified child field
   * @throws TsIllegalArgumentRtException found child kind {@link ETinTypeKind#hasAtomic()} != <code>true</code>
   */
  public long extractChildLong( String aFieldId, IStringMap<ITinValue> aChildValues, long aDefaultValue ) {
    IAtomicValue av = extractChildAtomic( aFieldId, aChildValues, null );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asLong();
  }

  /**
   * Extracts atomic value from {@link ETinTypeKind#ATOMIC} child field as {@link String}.
   *
   * @param aFieldId String - the field ID
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - the child values
   * @param aDefaultValue String - the value returned when no child found
   * @return String - value of the specified child field
   * @throws TsIllegalArgumentRtException found child kind {@link ETinTypeKind#hasAtomic()} != <code>true</code>
   */
  public String extractChildString( String aFieldId, IStringMap<ITinValue> aChildValues, String aDefaultValue ) {
    IAtomicValue av = extractChildAtomic( aFieldId, aChildValues, null );
    if( av == null ) {
      return aDefaultValue;
    }
    return av.asString();
  }

  /**
   * Extracts atomic value from {@link ETinTypeKind#ATOMIC} child field as a value object.
   *
   * @param <V> - expected type of value-object
   * @param aFieldId String - the field ID
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - the child values
   * @param aDefaultValue &lt;V&gt; - the value returned when no child found
   * @return &lt;V&gt; - value of the specified child field
   * @throws TsIllegalArgumentRtException found child kind {@link ETinTypeKind#hasAtomic()} != <code>true</code>
   */
  public <V> V extractChildValobj( String aFieldId, IStringMap<ITinValue> aChildValues, V aDefaultValue ) {
    IAtomicValue av = extractChildAtomic( aFieldId, aChildValues, null );
    if( av == null || av == IAtomicValue.NULL ) {
      return aDefaultValue;
    }
    return av.asValobj();
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Implementation may determine which fields to display in inspector.
   * <p>
   * <p>
   * In base class returns all keys from the map {@link #fieldInfos()}, there is no need to call superclass method when
   * overriding.
   *
   * @param aValue {@link ITinValue} - the field value, may be <code>null</code>
   * @return {@link IStringList} - IDs of the visible fields
   */
  protected IStringList doGetVisibleFieldIds( ITinValue aValue ) {
    return fieldInfos.ids();
  }

  /**
   * Implementation may perform additional atomic value validity check.
   * <p>
   * Called only for types with atomic values, ie {@link ETinTypeKind#hasAtomic()} = <code>true</code>, after mandatory
   * checks passed.
   * <p>
   * In base class returns {@link ValidationResult#SUCCESS}, there is no need to call superclass method when overriding.
   *
   * @param aValue {@link IAtomicValue} - field atomic value or <code>null</code>
   * @return {@link ValidationResult} the check result
   */
  protected ValidationResult doCanDecompose( IAtomicValue aValue ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Implementation must create atomic value representation from the child fields values.
   * <p>
   * Called only for types with atomic values, ie {@link ETinTypeKind#hasAtomic()} = <code>true</code>.
   * <p>
   * The argument contains all child field values.
   * <p>
   * In base class simply throws an exception {@link TsUnsupportedFeatureRtException}, so superclass method must not be
   * called when overriding.
   *
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - all child values as the map "field ID" - "value"
   * @return {@link IAtomicValue} - composed atomic value representation of the field value
   */
  protected IAtomicValue doCompose( IStringMap<ITinValue> aChildValues ) {
    throw new TsUnsupportedFeatureRtException();
  }

  /**
   * Implementation must decompose atomic value if {@link ETinTypeKind#hasAtomic()} = <code>true</code>.
   * <p>
   * Called only for types with atomic values and after check that non-null argument is atomic type compatible.
   * <p>
   * In base class simply throws an exception {@link TsUnsupportedFeatureRtException}, so superclass method must not be
   * called when overriding.
   *
   * @param aValue {@link IAtomicValue} - field atomic value or <code>null</code>
   * @param aChildValues {@link IStringMapEdit}&lt;{@link ITinValue}&gt; - editable child field values
   */
  protected void doDecompose( IAtomicValue aValue, IStringMapEdit<ITinValue> aChildValues ) {
    throw new TsUnsupportedFeatureRtException();
  }

  /**
   * Implementation must create {@link ITinValue} for the <code>null</code> entity, that is initial default value.
   * <p>
   * Note: for {@link ETinTypeKind#FULL} there is no need to create child values, they will be created internally by TIN
   * framework.
   *
   * @return {@link ITinValue} - created TIN value
   */
  protected abstract ITinValue doGetNullTinValue();

  /**
   * Implementation must create {@link ITinValue} for the specified entity.
   * <p>
   * Note: for {@link ETinTypeKind#FULL} there is no need to create child values, they will be created internally by TIN
   * framework.
   *
   * @param aEntity &lt;T&gt; - the entity, never is <code>null</code>
   * @return {@link ITinValue} - created TIN value
   */
  protected abstract ITinValue doGetTinValue( T aEntity );

  /**
   * Implementation may create entity from value, if supported.
   * <p>
   * Returns <code>null</code> in the base class, there is no need to call superclass method when overriding.
   *
   * @param aValue {@link ITinValue} - the value, never is <code>null</code>
   * @return &lt;T&gt; - created entity
   */
  protected T doCreateEntity( ITinValue aValue ) {
    return null;
  }

}
