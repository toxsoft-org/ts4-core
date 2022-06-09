package org.toxsoft.core.tsgui.m5.valeds.multilookup;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link M5MultiLookupFieldDef} field viewer as single-line text.
 *
 * @author hazard157
 * @param <V> - lookup items type
 */
public class ValedMultiLookupTextViewer<V>
    extends AbstractValedMultiLookupEditor<V> {

  Text         text;
  IListEdit<V> itemsList = new ElemLinkedBundleList<>();

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  public ValedMultiLookupTextViewer( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedMultiLookupEditors
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    text = new Text( aParent, SWT.BORDER );
    text.setEditable( false );
    return text;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    // nop
  }

  @Override
  public ValidationResult canGetValue() {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected IList<V> doGetUnvalidatedValue() {
    return itemsList;
  }

  @Override
  protected void doDoSetUnvalidatedValue( IList<V> aValue ) {
    if( aValue == null ) {
      itemsList.clear();
    }
    else {
      itemsList.setAll( aValue );
    }
    text.setText( fieldDef().formatFieldText( aValue ) );
  }

  @Override
  protected void doClearValue() {
    doSetUnvalidatedValue( null );
  }

  @Override
  protected void doRefreshOnLookupProviderChange() {
    doDoSetUnvalidatedValue( itemsList );
  }

}
