package org.toxsoft.core.tsgui.m5.valeds.singlemodown;

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
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link M5SingleModownFieldDef} field editor as single-line text with edit button at right.
 *
 * @author hazard157
 * @param <V> - edited value type (also field value type)
 */
public class ValedSingleModownTextEditor<V>
    extends AbstractValedSingleModownEditor<V> {

  private final SelectionListener btnEditSelectionListener = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent aE ) {
      editDialog();
    }

  };

  private Text   text    = null;
  private Button btnEdit = null;
  private V      value   = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  public ValedSingleModownTextEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
  }

  // ------------------------------------------------------------------------------------
  // Внутренные методы
  //

  void editDialog() {
    TsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext() );
    cdi.setMaxSizeShellRelative( 70, 80 );
    IM5LifecycleManager<V> lm = fieldDef().itemModel().getLifecycleManager( findMasterObject() );
    V newValue = M5GuiUtils.askEdit( tsContext(), fieldDef().itemModel(), value, cdi, lm );
    if( newValue != null && !Objects.equals( newValue, value ) ) {
      doSetUnvalidatedValue( newValue );
      fireModifyEvent( true );
    }
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    TsComposite board = new TsComposite( aParent );
    board.setLayout( new BorderLayout() );
    text = new Text( board, SWT.BORDER );
    text.setLayoutData( BorderLayout.CENTER );
    text.setEditable( false );
    btnEdit = new Button( board, SWT.PUSH );
    btnEdit.setLayoutData( BorderLayout.EAST );
    btnEdit.setText( STR_ELLIPSIS );
    btnEdit.addSelectionListener( btnEditSelectionListener );
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
  protected V doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( V aValue ) {
    value = aValue;
    if( value != null ) {
      text.setText( fieldDef().itemModel().visualsProvider().getName( aValue ) );
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

}
