package org.toxsoft.core.tsgui.m5.valeds.singlemodown;

import static org.toxsoft.core.tsgui.m5.valeds.singlemodown.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.helpers.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link M5SingleModownFieldDef} field editor as inplace {@link IM5EntityPanel}.
 * <p>
 * By definition of modown linked entities they are owned (lifecycle managed) by the link owner entity. So each time
 * when user edits properties linked entity will be created.
 *
 * @author hazard157
 * @param <V> - edited value type (also field value type)
 */
public class ValedSingleModownInplaceEditor<V>
    extends AbstractValedSingleModownEditor<V> {

  /**
   * {@link #checkboxIsNull} listener.
   */
  private final SelectionListener checkboxIsNullSelectionListener = new SelectionAdapter() {

    @Override
    public void widgetSelected( SelectionEvent aEvent ) {
      whenCheckboxIsNullSelectionChanged();
    }
  };

  private final IM5EntityPanel<V> panel;

  /**
   * Checkbox used to explicitly set edited field value to <code>null</code>.
   * <p>
   * Check box is created only if user is allowed to set field value to <code>null</code>, that is only when
   * {@link IM5MixinSingleLinkField#canUserSelectNull()} = <code>true</code>.
   */
  private Button checkboxIsNull = null;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - editor context (used as VALED creation argument)
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException {@link IM5ValedConstants#M5_VALED_REFDEF_FIELD_DEF} not found in the context
   * @throws ClassCastException found reference is not of expected type {@link M5SingleModownFieldDef}
   */
  public ValedSingleModownInplaceEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, avInt( 3 ) );
    if( isCreatedUneditable() ) {
      panel = fieldDef().itemModel().panelCreator().createEntityViewerPanel( aContext );
    }
    else {
      Object master = findMasterObject();
      IM5LifecycleManager<V> lm = fieldDef().itemModel().getLifecycleManager( master );
      panel = fieldDef().itemModel().panelCreator().createEntityEditorPanel( aContext, lm );
    }
    panel.genericChangeEventer().addListener( widgetValueChangeListener );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * On {@link #checkboxIsNull} state chenge fire chenge event and enable/disable inplace panel.
   */
  void whenCheckboxIsNullSelectionChanged() {
    fireModifyEvent( true );
    panel.setEditable( !checkboxIsNull.getSelection() );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedSingleModownEditor
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    // if used may explicitly set value to null then create also "Set to null" checkboxIsNull
    if( fieldDef().canUserSelectNull() && !isCreatedUneditable() ) {
      TsComposite board = new TsComposite( aParent );
      board.setLayout( new BorderLayout() );
      // checkboxIsNull
      checkboxIsNull = new Button( board, SWT.CHECK );
      checkboxIsNull.setText( STR_T_CHECKBOX_IS_NULL );
      checkboxIsNull.setToolTipText( STR_P_CHECKBOX_IS_NULL );
      checkboxIsNull.setLayoutData( BorderLayout.NORTH );
      checkboxIsNull.addSelectionListener( checkboxIsNullSelectionListener );
      // panel
      panel.createControl( board );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
      return board;
    }
    panel.createControl( aParent );
    return panel.getControl();
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( checkboxIsNull != null ) {
      checkboxIsNull.setEnabled( aEditable );
    }
    panel.setEditable( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    // if explicitly set to null then OK
    if( checkboxIsNull != null && checkboxIsNull.getSelection() ) {
      return ValidationResult.SUCCESS;
    }
    // check edited entity creation/editing ability
    ValidationResult vr = panel.canGetValues();
    if( !vr.isError() ) {
      IM5Bunch<V> vals = panel.getValues();
      IM5LifecycleManager<V> lifecycleManager = fieldDef().itemModel().getLifecycleManager( findMasterObject() );
      vr = ValidationResult.firstNonOk( vr, lifecycleManager.canCreate( vals ) );
    }
    return vr;
  }

  @Override
  protected V doGetUnvalidatedValue() {
    // if explicitly set to null then simply return null
    if( checkboxIsNull != null && checkboxIsNull.getSelection() ) {
      return null;
    }
    // return the entity
    IM5Bunch<V> vals = panel.getValues();
    IM5LifecycleManager<V> lifecycleManager = fieldDef().itemModel().getLifecycleManager( findMasterObject() );
    V item = lifecycleManager.create( vals );
    return item;
  }

  @Override
  protected void doSetUnvalidatedValue( V aValue ) {
    panel.setValues( fieldDef().itemModel().valuesOf( aValue ) );
    if( checkboxIsNull != null ) {
      setSelfEditing( true );
      checkboxIsNull.setSelection( aValue == null );
      setSelfEditing( false );
    }
  }

  @Override
  protected void doClearValue() {
    checkboxIsNull.setSelection( true );
    panel.setValues( null );
  }

}
