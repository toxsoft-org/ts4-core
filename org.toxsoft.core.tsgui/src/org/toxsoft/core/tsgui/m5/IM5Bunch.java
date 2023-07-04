package org.toxsoft.core.tsgui.m5;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Modelled entity field values.
 * <p>
 * Field falues may be either extracted from existing entity or may be created programmatically. In first case original
 * entity may be stored as {@link #originalEntity()}. Note that if bunch is editable (is instance of
 * {@link IM5BunchEdit}, field values may differ from {@link #originalEntity()} field values.
 * <p>
 * This bunch always contains all field values as fields are listed in {@link IM5Model#fieldDefs()}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5Bunch<T>
    extends IM5ModelRelated<T> {

  /**
   * Returns original entity if any.
   *
   * @return &lt;T&gt; - original entity or <code>null</code>
   */
  T originalEntity();

  /**
   * Returns the field value.
   *
   * @param <V> - expected value type
   * @param aFieldId String - the field ID
   * @return &lt;V&gt; - the field value, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such field in model
   */
  <V> V get( String aFieldId );

  /**
   * Returns field value.
   *
   * @param <V> - expected value type
   * @param aFieldDef {@link IM5FieldDef} - field definition
   * @return &lt;V&gt; - the field value, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException aFieldDef is of different model
   * @throws TsIllegalStateRtException field value type does not matches {@link IM5FieldDef#valueClass()}
   */
  <V> V get( IM5FieldDef<T, V> aFieldDef );

  /**
   * Returns field value with value type check.
   *
   * @param <V> - expected value type
   * @param aFieldId String - the field ID
   * @param aValueClass {@link Class}&lt;V&gt; - expected value type
   * @return &lt;V&gt; - the field value, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such field in model
   * @throws TsIllegalStateRtException field value type does not matches expected type
   */
  <V> V getAs( String aFieldId, Class<V> aValueClass );

  // ------------------------------------------------------------------------------------
  // Convenience inline methods
  //

  @SuppressWarnings( "javadoc" )
  default IAtomicValue getAsAv( String aFieldId ) {
    return getAs( aFieldId, IAtomicValue.class );
  }

}
