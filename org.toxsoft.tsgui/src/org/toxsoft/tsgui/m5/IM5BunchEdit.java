package org.toxsoft.tsgui.m5;

import org.toxsoft.tslib.coll.basis.ITsClearable;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Editable extension of {@link IM5Bunch}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5BunchEdit<T>
    extends IM5Bunch<T>, ITsClearable {

  /**
   * Sets the field value in bunch.
   *
   * @param <V> - field value type
   * @param aFieldId String - the field ID
   * @param aValue &lt;V&gt; - new value, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no sych field in model
   * @throws TsIllegalArgumentRtException aValue type is not compatible with {@link IM5FieldDef#valueClass()}
   */
  <V> void set( String aFieldId, V aValue );

  /**
   * Sets the field value in bunch.
   *
   * @param <V> - field value type
   * @param aFieldDef {@link IM5FieldDef} - field definition
   * @param aValue &lt;V&gt; - new value, may be <code>null</code>
   * @throws TsNullArgumentRtException aFieldDef = <code>null</code>
   * @throws TsIllegalArgumentRtException aFieldDef is of different model
   * @throws TsIllegalArgumentRtException aValue type is not compatible with {@link IM5FieldDef#valueClass()}
   */
  <V> void set( IM5FieldDef<T, V> aFieldDef, V aValue );

  /**
   * Extracts field values from the entity.
   *
   * @param aSource {@link IM5Bunch}&lt;T&gt; - the source
   * @param aUpdateOriginal boolean - <code>true</code> also updates {@link #originalEntity()} from argument
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException source and this bunches are of different models
   */
  void fillFrom( IM5Bunch<T> aSource, boolean aUpdateOriginal );

  /**
   * Extracts field values from the entity.
   *
   * @param aEntity &lt;T&gt; - the entity or <code>null</code> to fill with default values
   * @param aUpdateOriginal boolean - <code>true</code> also updates {@link #originalEntity()} from argument
   * @throws TsIllegalArgumentRtException argument is not {@link IM5Model#isModelledObject(Object)}
   */
  void fillFrom( T aEntity, boolean aUpdateOriginal );

}
