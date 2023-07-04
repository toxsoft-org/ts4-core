package org.toxsoft.core.tsgui.m5.valeds.singlelookup;

import static org.toxsoft.core.tsgui.m5.valeds.singlelookup.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.TsLibUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link M5SingleLookupFieldDef} field editor as combo box with drop-down list..
 *
 * @author hazard157
 * @param <V> - lookup items type (also field value type)
 */
public class ValedSingleLookupComboEditor<V>
    extends AbstractValedSingleLookupEditor<V> {

  private final IListEdit<V> items = new ElemLinkedBundleList<>();

  private Combo combo;
  private V     value = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  public ValedSingleLookupComboEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( IValedControlConstants.OPID_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void updateDropDownList() {
    combo.removeAll();
    IList<V> listed = lookupProvider().listItems();
    if( fieldDef().canUserSelectNull() ) {
      combo.add( EMPTY_STRING );
    }
    items.setAll( listed );
    for( V v : listed ) {
      String s = lookupProvider().getName( v );
      combo.add( s );
    }
  }

  void refreshOnSomethingChanged() {
    doGetUnvalidatedValue(); // update value
    doSetUnvalidatedValue( value ); // update drop-down list and select value if possible
  }

  private V internalGetValueFromCombo() {
    V val = null;
    int selIndex = combo.getSelectionIndex();
    // if there is a selected item in list then return it otherwise return value
    if( selIndex >= 0 ) {
      // if null selection is allowed then first item in the drop-down list always is an empty text item
      if( fieldDef().canUserSelectNull() ) {
        if( selIndex != 0 ) {
          val = items.get( selIndex - 1 );
        }
      }
      // the drop-down list is the same as 'items' list, simply return selected item
      else {
        val = items.get( selIndex );
      }
    }
    return val;
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedSingleLookupEditor
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    combo = new Combo( aParent, SWT.DROP_DOWN | SWT.READ_ONLY );
    combo.addSelectionListener( notificationSelectionListener );
    updateDropDownList();
    doSetUnvalidatedValue( value );
    return combo;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( combo != null ) {
      combo.setEnabled( aEditable );
    }
  }

  @Override
  protected V doGetUnvalidatedValue() {
    value = internalGetValueFromCombo();
    return value;
  }

  @Override
  public ValidationResult canGetValue() {
    value = internalGetValueFromCombo();
    boolean isNullAllowed = tsContext().params().getBool( TSID_IS_NULL_ALLOWED, false );
    if( value == null && !isNullAllowed ) {
      return ValidationResult.error( MSG_ERR_CANT_GET_NULL_VALUE );
    }
    return ValidationResult.SUCCESS;
  }

  @Override
  protected void doDoSetUnvalidatedValue( V aValue ) {
    updateDropDownList();
    // value must be assigned event it is not in items list
    value = aValue;
    // toSelIndex will be selected, so if aValue is not in items list then we'll select -1
    int toSelIndex = -1;
    if( value != null ) {
      int index = items.indexOf( value );
      if( index >= 0 ) {
        // если предусмотрен ввод null, в combo есть дополнительный к items элемент - учтем это
        if( fieldDef().canUserSelectNull() ) {
          toSelIndex = index + 1;
        }
        else {
          toSelIndex = index;
        }
      }
    }
    // null is specified - that means no value
    else {
      // if null is allowed - select first (an empty) element in drop-down list
      if( fieldDef().canUserSelectNull() ) {
        toSelIndex = 0;
      }
    }
    setSelfEditing( true );
    combo.select( toSelIndex );
    setSelfEditing( false );
  }

  @Override
  protected void onMasterObjectChanged( Object aNewMaster, Object aOldMaster ) {
    refreshOnSomethingChanged();
  }

  @Override
  protected void doClearValue() {
    setSelfEditing( true );
    value = null;
    if( fieldDef().canUserSelectNull() ) {
      combo.select( 0 );
    }
    else {
      combo.setText( EMPTY_STRING );
    }
    setSelfEditing( false );
  }

  @Override
  protected void doRefreshOnLookupProviderChange() {
    updateDropDownList();
    refreshOnSomethingChanged();
  }

}
