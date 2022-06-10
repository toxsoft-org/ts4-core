package org.toxsoft.core.tsgui.m5.valeds.multilookup;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link M5MultiLookupFieldDef} field editor as table with toobar.
 *
 * @author hazard157
 * @param <V> - lookup objects type that is edited field value type
 */
public class ValedMultiLookupTableEditor<V>
    extends AbstractValedMultiLookupEditor<V> {

  private IM5MultiLookupPanel<V> panel = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  public ValedMultiLookupTableEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, avInt( 15 ) );
    setLookupProvider( fieldDef().lookupProvider() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedMultiLookupEditor
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    IM5Model<V> model = fieldDef().itemModel();
    panel = model.panelCreator().createMultiLookupPanel( tsContext(), lookupProvider() );
    panel.createControl( aParent );
    panel.setEditable( true );
    panel.refresh();
    panel.genericChangeEventer().addListener( widgetValueChangeListener );
    return panel.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    panel.setEditable( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    ValidationResult vr = validateItemsCount( panel.items().size(), fieldDef().maxCount(), fieldDef().isExactCount() );
    if( vr.isError() ) {
      return vr;
    }
    return ValidationResult.firstNonOk( vr, super.canGetValue() );
  }

  @Override
  protected IList<V> doGetUnvalidatedValue() {
    return panel.items();
  }

  @Override
  protected void doDoSetUnvalidatedValue( IList<V> aValue ) {
    if( aValue == null ) {
      panel.setItems( IList.EMPTY );
    }
    else {
      panel.setItems( aValue );
    }
  }

  @Override
  protected void doClearValue() {
    panel.setItems( IList.EMPTY );
  }

  @Override
  protected void doRefreshOnLookupProviderChange() {
    if( panel != null ) {
      doSetUnvalidatedValue( panel.items() );
    }
  }

}
