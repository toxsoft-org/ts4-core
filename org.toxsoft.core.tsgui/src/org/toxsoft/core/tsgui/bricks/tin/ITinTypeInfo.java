package org.toxsoft.core.tsgui.bricks.tin;

import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Information about the field type.
 *
 * @author hazard157
 */
public interface ITinTypeInfo {

  /**
   * Returns the kind - how values are represented for this type of the fields.
   *
   * @return {@link ETinTypeKind} - the kind
   */
  ETinTypeKind kind();

  /**
   * Returns the data type of the atomic value representation of this field.
   *
   * @return {@link IDataType} - the atomic data type
   * @throws TsUnsupportedFeatureRtException invalid kind, {@link ETinTypeKind#hasAtomic()} = <code>false</code>
   */
  IDataType dataType();

  /**
   * Returns information about all fields of this type.
   *
   * @return {@link IStridablesList}&lt;{@link ITinFieldInfo}&gt; - field infos
   */
  IStridablesList<ITinFieldInfo> fieldInfos();

  /**
   * Determines which fields is visible in the inspector for the specified value of the field.
   * <p>
   * The <code>null</code> argument means default, that is initial value of the field.
   * <p>
   * The returned list contains IDs from the {@link #fieldInfos()} keys.
   *
   * @param aValue {@link ITinValue} - the field value, may be <code>null</code>
   * @return {@link IStringList} - IDs of the visible fields
   */
  IStringList visibleFieldIds( ITinValue aValue );

  /**
   * Returns the visualizer of the field value.
   * <p>
   * This TIN type's value visualizer is used as a default value visualizer of TIN fields
   * {@link ITinFieldInfo#valueVisualizer()}.
   *
   * @return {@link ITsVisualsProvider}&lt;{@link ITinValue}&gt; - field value visualizer, never is <code>null</code>
   */
  ITsVisualsProvider<ITinValue> valueVisualizer();

  /**
   * Creates atomic value representation from the child fields values.
   * <p>
   * The <code>null</code> argument means default, that is initial value of the field must be returned.
   * <p>
   * The argument may contain only subset of the field values.
   *
   * @param aChildValues {@link IStringMap}&lt;{@link ITinValue}&gt; - map "field ID" - "value" or <code>null</code>
   * @return {@link IAtomicValue} - composed atomic value representation of the field value
   * @throws TsUnsupportedFeatureRtException no atomic value, {@link ETinTypeKind#hasAtomic()} = <code>false</code>
   */
  IAtomicValue compose( IStringMap<ITinValue> aChildValues );

  /**
   * Determines if atomic value can be decomposed.
   * <p>
   * Test at least the kind (that {@link ETinTypeKind#hasAtomic()} = <code>true</code>) and atomic type compiance with
   * {@link #dataType()}. Additionaly implementation may perform additional check such as value is in allowed range,
   * etc.
   *
   * @param aValue {@link IAtomicValue} - field atomic value or <code>null</code>
   * @return {@link ValidationResult} the check result
   */
  ValidationResult canDecompose( IAtomicValue aValue );

  /**
   * Decomposes (creates child fields values) from the atomic value representation of the field.
   * <p>
   * The <code>null</code> argument means default, that is initial value of the field.
   *
   * @param aValue {@link IAtomicValue} - field atomic value or <code>null</code>
   * @return {@link IStringMap}&lt;{@link ITinValue}&gt; - child field values
   * @throws TsValidationFailedRtException failed {@link #canDecompose(IAtomicValue)}
   */
  IStringMap<ITinValue> decompose( IAtomicValue aValue );

  /**
   * Extracts object field values as {@link ITinValue}.
   * <p>
   * For <code>null</code> argument returns default (initial) values of the object fields.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   * @return {@link ITinValue} - the values of the entity fields
   * @throws ClassCastException entity class does not matches expected class of this type
   */
  ITinValue makeValue( Object aEntity );

  /**
   * Optional method creates entity from the {@link ITinValue}.
   *
   * @param aValue {@link ITinValue} - the value
   * @return {@link Object} - created entity or <code>null</code> if creation is not supported
   */
  Object makeEntity( ITinValue aValue );

}
