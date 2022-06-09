package org.toxsoft.core.tsgui.m5.valeds.multimodown;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class for all {@link IM5MultiModownFieldDef} editing VALEDs implementations.
 * <p>
 * Just adds check that field definition in the context is of type {@link M5MultiModownFieldDef}.
 *
 * @author hazard157
 * @param <V> - modown items type (field value is list of it)
 */
public abstract class AbstractValedMultiModownEditor<V>
    extends AbstractValedM5FieldEditor<IList<V>> {

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  protected AbstractValedMultiModownEditor( ITsGuiContext aContext ) {
    super( aContext );
    checkM5FieldDefClass( aContext, M5MultiModownFieldDef.class );
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  /**
   * Возвращает описание редактируемого поля.
   *
   * @return {@link M5MultiModownFieldDef} - описание редактируемого поля
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public M5MultiModownFieldDef<?, V> fieldDef() {
    return (M5MultiModownFieldDef<?, V>)super.fieldDef();
  }

}
