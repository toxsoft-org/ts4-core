package org.toxsoft.core.tsgui.m5.valeds.singlemodown;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class for all {@link IM5SingleModownFieldDef} editing VALEDs implementations.
 * <p>
 * Just adds check that field definition in the context is of type {@link M5SingleModownFieldDef}.
 *
 * @author hazard157
 * @param <V> - modown items type (also field value type)
 */
public abstract class AbstractValedSingleModownEditor<V>
    extends AbstractValedM5FieldEditor<V> {

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  protected AbstractValedSingleModownEditor( ITsGuiContext aContext ) {
    super( aContext );
    checkM5FieldDefClass( aContext, M5SingleModownFieldDef.class );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the field definition of concrete type..
   *
   * @return {@link IM5SingleModownFieldDef} - M5-field definition
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public IM5SingleModownFieldDef<?, V> fieldDef() {
    return (IM5SingleModownFieldDef<?, V>)super.fieldDef();
  }

}
