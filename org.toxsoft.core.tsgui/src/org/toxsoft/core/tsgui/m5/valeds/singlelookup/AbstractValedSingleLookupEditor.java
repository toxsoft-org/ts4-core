package org.toxsoft.core.tsgui.m5.valeds.singlelookup;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class for all {@link IM5SingleLookupFieldDef} editing VALEDs implementations.
 * <p>
 * Just adds check that field definition in the context is of type {@link M5SingleLookupFieldDef}.
 *
 * @author hazard157
 * @param <V> - lookup items type (also field value type)
 */
public abstract class AbstractValedSingleLookupEditor<V>
    extends AbstractValedM5FieldEditor<V> {

  private IM5LookupProvider<V> lookupProvider = null;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  protected AbstractValedSingleLookupEditor( ITsGuiContext aContext ) {
    super( aContext );
    checkM5FieldDefClass( aContext, M5SingleLookupFieldDef.class );
    lookupProvider = fieldDef().lookupProvider();
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedM5FieldEditor
  //

  @Override
  final protected void doSetUnvalidatedValue( V aValue ) {
    if( lookupProvider().listItems().hasElem( aValue ) ) {
      doDoSetUnvalidatedValue( aValue );
      return;
    }
    doClearValue();
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the field definition of concrete type..
   *
   * @return {@link IM5SingleLookupFieldDef} - M5-field definition
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public IM5SingleLookupFieldDef<?, V> fieldDef() {
    return (IM5SingleLookupFieldDef<?, V>)super.fieldDef();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the lookup items provider.
   * <p>
   * Initially lookup rpovider is set to {@link IM5SingleLookupFieldDef#lookupProvider()}.
   *
   * @return {@link IM5LookupProvider} - the lookup items provider never is <code>null</code>
   */
  final public IM5LookupProvider<V> lookupProvider() {
    return lookupProvider;
  }

  /**
   * Sets the lookup items provider.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup items provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  final public void setLookupProvider( IM5LookupProvider<V> aLookupProvider ) {
    TsNullArgumentRtException.checkNull( aLookupProvider );
    lookupProvider = aLookupProvider;
    doRefreshOnLookupProviderChange();
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Called from {@link #setLookupProvider(IM5LookupProvider)},
   */
  protected abstract void doRefreshOnLookupProviderChange();

  /**
   * Subclass must the value to editor widget(s).
   * <p>
   * This method is called from {@link #doSetUnvalidatedValue(Object)}.
   *
   * @param aValue &lt;V&gt; - new value verified that is in lookup list
   * @throws TsIllegalArgumentRtException value has uncompatibe type
   */
  protected abstract void doDoSetUnvalidatedValue( V aValue );

}
