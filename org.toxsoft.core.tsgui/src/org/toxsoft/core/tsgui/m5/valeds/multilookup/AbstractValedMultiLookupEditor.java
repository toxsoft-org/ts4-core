package org.toxsoft.core.tsgui.m5.valeds.multilookup;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Base class for all {@link IM5MultiLookupFieldDef} editing VALEDs implementations.
 * <p>
 * Just adds check that field definition in the context is of type {@link M5MultiLookupFieldDef}.
 *
 * @author hazard157
 * @param <V> - lookup items type (field value is list of it)
 */
public abstract class AbstractValedMultiLookupEditor<V>
    extends AbstractValedM5FieldEditor<IList<V>> {

  private IM5LookupProvider<V> lookupProvider = IM5LookupProvider.EMPTY;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  protected AbstractValedMultiLookupEditor( ITsGuiContext aContext ) {
    super( aContext );
    checkM5FieldDefClass( aContext, M5MultiLookupFieldDef.class );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedM5FieldEditor
  //

  @Override
  final protected void doSetUnvalidatedValue( IList<V> aValue ) {
    IListEdit<V> ll = new ElemArrayList<>( aValue.size() );
    IList<V> lookupList = lookupProvider.listItems();
    for( V v : aValue ) {
      if( lookupList.hasElem( v ) ) {
        ll.add( v );
      }
    }
    doDoSetUnvalidatedValue( ll );
  }

  // ------------------------------------------------------------------------------------
  // For subclasses
  //

  /**
   * Returns the field definition of concrete type..
   *
   * @return {@link M5MultiLookupFieldDef} - M5-field definition
   */
  @SuppressWarnings( "unchecked" )
  @Override
  public M5MultiLookupFieldDef<?, V> fieldDef() {
    return (M5MultiLookupFieldDef<?, V>)super.fieldDef();
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
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup items provider or <code>null</code> for empty
   */
  final public void setLookupProvider( IM5LookupProvider<V> aLookupProvider ) {
    lookupProvider = aLookupProvider != null ? aLookupProvider : IM5LookupProvider.EMPTY;
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
   * This method is called from {@link #doSetUnvalidatedValue(IList)}.
   *
   * @param aValue {@link IList}&lt;V&gt; - new value verified that all items are fromn the lookup list
   * @throws TsIllegalArgumentRtException value has uncompatibe type
   */
  protected abstract void doDoSetUnvalidatedValue( IList<V> aValue );

}
