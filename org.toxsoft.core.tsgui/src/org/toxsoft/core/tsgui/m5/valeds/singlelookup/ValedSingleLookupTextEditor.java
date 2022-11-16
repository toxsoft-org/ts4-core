package org.toxsoft.core.tsgui.m5.valeds.singlelookup;

import static org.toxsoft.core.tsgui.m5.valeds.singlelookup.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.IValedImplementationHelpers.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link M5SingleLookupFieldDef} field editor as single-line text with edit button at right.
 *
 * @author hazard157
 * @param <V> - lookup items type (also field value type)
 */
public class ValedSingleLookupTextEditor<V>
    extends AbstractValedSingleLookupEditor<V> {

  private TsComposite board      = null;
  private Text        text       = null;
  private Button      btnSelItem = null;
  private V           value      = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  public ValedSingleLookupTextEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  // ------------------------------------------------------------------------------------
  // implmentation
  //

  void selectItem() {
    ITsDialogInfo cdi = new TsDialogInfo( tsContext(), STR_C_SELECT_ITEM, STR_T_SELECT_ITEM );
    V selValue = M5GuiUtils.askSelectLookupItem( cdi, fieldDef().itemModel(), getValue(), lookupProvider() );
    if( selValue != null && !Objects.equals( value, selValue ) ) {
      doSetUnvalidatedValue( selValue );
      fireModifyEvent( true );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedSingleLookupEditor
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    text = new Text( board, SWT.BORDER );
    text.setEditable( false );
    text.setLayoutData( BorderLayout.CENTER );
    btnSelItem = new Button( board, SWT.PUSH );
    btnSelItem.setText( STR_ELLIPSIS );
    btnSelItem.setToolTipText( STR_P_BTN_SELECT_ITEM );
    btnSelItem.setLayoutData( BorderLayout.EAST );
    btnSelItem.addSelectionListener( new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        selectItem();
      }
    } );
    return board;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    btnSelItem.setEnabled( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    return ValidationResult.SUCCESS;
  }

  @Override
  protected V doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doDoSetUnvalidatedValue( V aValue ) {
    value = aValue;
    if( value != null ) {
      text.setText( lookupProvider().getName( aValue ) );
    }
    else {
      text.setText( TsLibUtils.EMPTY_STRING );
    }
  }

  @Override
  protected void doClearValue() {
    value = null;
    text.setText( TsLibUtils.EMPTY_STRING );
  }

  @Override
  protected void doRefreshOnLookupProviderChange() {
    IList<V> items = lookupProvider().listItems();
    V oldValue = value;
    if( !items.hasElem( value ) ) {
      doSetUnvalidatedValue( null );
    }
    else {
      doSetUnvalidatedValue( value );
    }
    if( !Objects.equals( oldValue, value ) ) {
      fireModifyEvent( true );
    }
  }

}
