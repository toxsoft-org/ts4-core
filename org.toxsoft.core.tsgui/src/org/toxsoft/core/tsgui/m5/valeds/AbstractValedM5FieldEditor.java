package org.toxsoft.core.tsgui.m5.valeds;

import static org.toxsoft.core.tsgui.m5.valeds.IM5ValedConstants.*;
import static org.toxsoft.core.tsgui.m5.valeds.ITsResources.*;

import java.util.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class for {@link IM5FieldDef} defined linked objects fields VALEDs.
 * <p>
 * Introduces concepts of:
 * <ul>
 * <li>field definition - stored as {@link IM5FieldDef} reference {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} in
 * context;</li>
 * <li>master-object - stored as {@link Object} reference {@link IM5ValedConstants#M5_VALED_REFDEF_MASTER_OBJ} in
 * context.</li>
 * </ul>
 *
 * @author hazard157
 * @param <V> - edited value type that is field value type
 */
public abstract class AbstractValedM5FieldEditor<V>
    extends AbstractValedControl<V, Control> {

  @SuppressWarnings( "rawtypes" )
  private final IM5FieldDef fieldDef;

  private Object lastMasterObject;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link IM5FieldDef}
   */
  protected AbstractValedM5FieldEditor( ITsGuiContext aContext ) {
    super( aContext );
    fieldDef = M5_VALED_REFDEF_FIELD_DEF.getRef( aContext );
    lastMasterObject = findMasterObject();
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  /**
   * {@inheritDoc}
   * <p>
   * In {@link AbstractValedM5FieldEditor} handles master object reference change, subclass <b>must</b> call superclass
   * method.
   */
  @Override
  public <X extends ITsContextRo> void onContextRefChanged( X aSource, String aName, Object aRef ) {
    Object newMasterObject = findMasterObject();
    if( !Objects.equals( lastMasterObject, newMasterObject ) ) {
      Object oldMaster = lastMasterObject;
      lastMasterObject = newMasterObject;
      onMasterObjectChanged( newMasterObject, oldMaster );
    }
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the link field editing VALED widgets type ID.
   *
   * @return String - widget type ID or an empty string
   */
  public String getValedWidgetTypeId() {
    return M5_VALED_OPDEF_WIDGET_TYPE_ID.getValue( tsContext().params() ).asString();
  }

  /**
   * Finds the master-object reference from the context.
   *
   * @return {@link Object} - reference to the master-object or <code>null</code>
   */
  public Object findMasterObject() {

    /**
     * TODO check and fix if needed<br>
     * GOGA 2024-02-10 it seems it's an error: the master must be retrieved from context, not lastMasterObject returned
     */

    // return lastMasterObject;
    return M5_VALED_REFDEF_MASTER_OBJ.getRef( tsContext() );
  }

  /**
   * Finds the master-object reference of the expected type from the context.
   *
   * @param <M> - expected type of master-object
   * @param aMasterObjectClass {@link Class}&lt;T&gt; - expected type of master-object
   * @return &lt;M&gt; - reference to the master-object or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws ClassCastException found reference is not of expected type
   */
  public <M> M findMasterObject( Class<M> aMasterObjectClass ) {
    return aMasterObjectClass.cast( lastMasterObject );
  }

  /**
   * Returns the master-object reference from the context.
   *
   * @return {@link Object} - reference to the master-object
   * @throws TsItemNotFoundRtException there is no reference in context
   */
  public Object getMasterObject() {
    if( lastMasterObject == null ) {
      throw new TsItemNotFoundRtException();
    }
    return lastMasterObject;
  }

  /**
   * Returns the master-object reference of the expected type from the context.
   *
   * @param <M> - expected type of master-object
   * @param aMasterObjectClass {@link Class}&lt;T&gt; - expected type of master-object
   * @return &lt;M&gt; - reference to the master-object
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException there is no reference in context
   * @throws ClassCastException found reference is not of expected type
   */
  public <M> Object getMasterObject( Class<M> aMasterObjectClass ) {
    TsNullArgumentRtException.checkNull( aMasterObjectClass );
    if( lastMasterObject == null ) {
      throw new TsItemNotFoundRtException();
    }
    return aMasterObjectClass.cast( lastMasterObject );
  }

  /**
   * Returns the field definition.
   *
   * @param <F> - extected field definition class (must match one specified in constructor)
   * @return {@link IM5FieldDef} - the field definition
   */
  public <F extends IM5FieldDef<?, ?>> F fieldDef() {
    return (F)fieldDef;
  }

  /**
   * Checks if M5-modeled reference {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} exists and is of expected type.
   *
   * @param <T> - expected class of field definition
   * @param aContext {@link ITsGuiContext} - the context
   * @param aExpectedClass {@link Class} - expected class of {@link IM5FieldDef} implementation
   * @return &lt;T&gt; - field definition from context of expected class
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link IM5FieldDef}
   */
  @SuppressWarnings( "rawtypes" )
  public static <T extends IM5FieldDef> T checkM5FieldDefClass( ITsGuiContext aContext, Class<T> aExpectedClass ) {
    TsNullArgumentRtException.checkNulls( aContext, aExpectedClass );
    IM5FieldDef fieldDef = IM5ValedConstants.M5_VALED_REFDEF_FIELD_DEF.getRef( aContext );
    if( !aExpectedClass.isInstance( fieldDef ) ) {
      String msg = String.format( FMT_ERR_INV_FIELD_DEF_CLASS, aExpectedClass.getSimpleName() );
      throw new ClassCastException( msg );
    }
    return aExpectedClass.cast( fieldDef );
  }

  /**
   * Validates <code>aCount</code> against specified constraints.
   *
   * @param aCount int - number of elements in multi linked field
   * @param aMaxCount int - max allowed number of elements (0 - no resriction)
   * @param aIsExactCount boolean - treat <code>aMaxCount</code> as exact count of items
   * @return {@link ValidationResult} - validation result
   */
  @SuppressWarnings( "boxing" )
  public ValidationResult validateItemsCount( int aCount, int aMaxCount, boolean aIsExactCount ) {
    if( aMaxCount > 0 ) {
      if( aIsExactCount && aCount != aMaxCount ) {
        return ValidationResult.error( FMT_ERR_NON_EXACT_COUNT, aCount, aMaxCount );
      }
      if( aCount > aMaxCount ) {
        return ValidationResult.error( FMT_ERR_TOO_MANY_ITEMS, aCount, aMaxCount );
      }
    }
    return super.canGetValue();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may process when reference to master object changes in context.
   * <p>
   * Does nothing in the base there is no need call superclass method when overriding.
   *
   * @param aNewMaster Object - new master object, may be <code>null</code>
   * @param aOldMaster Object - old master object, may be <code>null</code>
   */
  protected void onMasterObjectChanged( Object aNewMaster, Object aOldMaster ) {
    // nop
  }

}
