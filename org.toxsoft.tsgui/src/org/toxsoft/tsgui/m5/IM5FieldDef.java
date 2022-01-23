package org.toxsoft.tsgui.m5;

import java.util.Comparator;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tslib.bricks.strid.IStridableParameterized;
import org.toxsoft.tslib.bricks.validator.ITsValidator;
import org.toxsoft.tslib.coll.primtypes.IStringMap;
import org.toxsoft.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Modelled entity field definition.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 * @param <V> - field value type
 */
public interface IM5FieldDef<T, V>
    extends IStridableParameterized, ITsGuiContextable {

  /**
   * Returns the value class.
   *
   * @return {@link Class}&lt;V&gt; - the value class
   */
  Class<V> valueClass();

  /**
   * This is shortanhd for domain context.
   *
   * @return {@link ITsGuiContext} - returns {@link IM5Domain#tsContext()}
   */
  @Override
  ITsGuiContext tsContext();

  /**
   * Returns the owning model.
   *
   * @return {@link IM5Model}&lt;T&gt; - the owning model, never is <code>null</code>
   * @throws TsIllegalStateRtException methdo is called before FieldDef is added to the model
   */
  IM5Model<T> ownerModel();

  /**
   * Returns the ORed hints flags {@link IM5Constants}<code>.<b>M5FF_XXX</b></code>.
   *
   * @return int - ORed bits {@link IM5Constants}<code>.M5FF_XXX</code>
   */
  int flags();

  /**
   * Returns the references map used to populate {@link ITsGuiContext} for valed creation.
   * <p>
   * When createing {@link IValedControl} for this field, valed-specific instance is created based on current
   * {@link ITsGuiContext}. Valed-specific context options are filled from field definitions
   * {@link IM5FieldDef#params()}. This map is used to configure valed-specific context references.
   *
   * @return {@link IStringMap} - arbitrary named references
   */
  IStringMap<Object> valedRefs();

  /**
   * Returns field value and visuals getter.
   *
   * @return {@link IM5Getter}&lt;T,V&gt; - the field value getter
   */
  IM5Getter<T, V> getter();

  /**
   * Returns the default value of the field.
   * <p>
   * This value is returned for <code>null</code> argument of the {@link #getFieldValue(Object)} call.
   *
   * @return &lt;V&gt; - the default value, may be <code>null</code>
   */
  V defaultValue();

  /**
   * Returns the field values validator.
   * <p>
   * Validator checks if field value has permissible value. For example, the validator may be used in GUI widgets to
   * check if user entered permissable value.
   *
   * @return {@link ITsValidator}&lt;V&gt; - the field value validator
   */
  ITsValidator<V> validator();

  /**
   * Returns the comparator of the field value.
   *
   * @return {@link Comparator}&lt;V&gt; - the comparator or <code>null</code> if values are not comparable
   */
  Comparator<V> comparator();

  // ------------------------------------------------------------------------------------
  // Convenience inline methods

  /**
   * Returns the field value of the specified entity.
   * <p>
   * If <code>aEntity</code> is <code>null</code>, method returns {@link #defaultValue()}.
   *
   * @param aEntity &lt;T&gt; - the entity, may be <code>null</code>
   * @return &lt;&gt; - the field value, may be <code>null</code>
   */
  default V getFieldValue( T aEntity ) {
    return getter().getValue( aEntity );
  }

  /**
   * Returns the owning domain.
   * <p>
   * This is {@link #ownerModel()}'s owning domain.
   *
   * @return {@link IM5Domain} - the owning domain
   */
  default IM5Domain domain() {
    return ownerModel().domain();
  }

  /**
   * Determines if values are comparable using {@link #comparator()}.
   * <p>
   * Simply performs the check {@link #comparator()} != <code>null</code>.
   *
   * @return boolean - flag that field values are comparable to each other useind {@link #comparator()}
   */
  default boolean isComparable() {
    return comparator() != null;
  }

  /**
   * Returns the field value from the bunch.
   *
   * @param aBunch {@link IM5BunchEdit}&lt;T&gt; - the bunch of field values
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @return &lt;&gt; - the field value, may be <code>null</code>
   */
  default V getFieldValue( IM5Bunch<T> aBunch ) {
    TsNullArgumentRtException.checkNull( aBunch );
    return aBunch.get( this );
  }

  /**
   * Sets the field value in the bunch.
   *
   * @param aBunch {@link IM5BunchEdit} - the edited bunch
   * @param aValue &lt;V&gt; - the value, may be <code>null</code>
   * @throws TsNullArgumentRtException <code>aBunch</code> = <code>null</code>
   */
  default void setFieldValue( IM5BunchEdit<T> aBunch, V aValue ) {
    TsNullArgumentRtException.checkNull( aBunch );
    aBunch.set( this, aValue );
  }

  /**
   * Determines if {@link #flags()} has any bit from argument.
   *
   * @param aHintFlag int - bits to be tested
   * @return boolean - if any non-zero bit of aHitFlag has value 1 in {@link #flags()}
   */
  default boolean hasFlag( int aHintFlag ) {
    return (flags() & aHintFlag) != 0;
  }

  /**
   * Determines if {@link #flags()} has all bits from argument.
   *
   * @param aHintFlag int - bits to be tested
   * @return boolean - if all non-zero bits of aHitFlag has value 1 in {@link #flags()}
   */
  default boolean hasFlags( int aHintFlag ) {
    return (flags() & aHintFlag) == aHintFlag;
  }

}
