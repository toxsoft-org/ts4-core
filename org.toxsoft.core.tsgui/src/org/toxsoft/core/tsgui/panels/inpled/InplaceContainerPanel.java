package org.toxsoft.core.tsgui.panels.inpled;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.panels.inpled.IInplaceEditorConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * TODO ??? {@link IInplaceEditorPanel} implementation.
 * <p>
 * Contains in-place content panel {@link AbstractInplaceContentPanel}, optional validation result pane and the button
 * bar. Initially there is only "Edit" button in button bar. Pressing "Edit" switches content to the editing state and
 * "OK", "Cancel", "Revert", "Apply", "Restore" buttons appear on button bar. "OK" and "Cancel" button finishes the
 * editing and returns in-place editor to the viewer mode.
 * <p>
 * For validation message panel respects {@link ValidationResultPanel} options.
 *
 * @author hazard157
 */
public final class InplaceContainerPanel
    extends AbstractLazyPanel<Control>
    implements IInplaceEditorPanel {

  /**
   * Default buttons in edit mode.
   * <p>
   * Actions defined in content panel as {@link AbstractInplaceContentPanel#listSupportedActions()} may add several more
   * buttons.
   */
  public static final IStridablesList<ITsActionDef> DEFAULT_EDIT_ACTION_DEFS = new StridablesList<>( //
      ACDEF_OK_CHANGES, //
      ACDEF_CANCEL_CHANGES, //
      ACDEF_APPLY_CHANGES, //
      ACDEF_REVERT_CHANGES //
  );

  private final GenericChangeEventer        eventer;
  private final AbstractInplaceContentPanel contentPanel;

  /**
   * List of actions (button) in edit mode.
   * <p>
   * List contains actions from {@link #DEFAULT_EDIT_ACTION_DEFS} with additions from
   * {@link AbstractInplaceContentPanel#listSupportedActions()}.
   */
  private final IStridablesList<ITsActionDef> editActionDefs;

  private TsComposite           backplane;                 // persistent background panel
  private ValidationResultPanel vrPanel = null;            // north panel of validation result display or null
  private BorderLayout          contentHolderBorderLayout; // layout for #contentHolderBorder
  private ScrolledComposite     contentHolder;             // scroller contains the inplace editor content panel
  private ButtonBar             buttonsBar;                // south panel with buttons

  /**
   * Middle panel of the specified (default - RED) color contains scroller {@link #contentHolder}.
   * <p>
   * In editing mode size of the scroller component is decreased by the specified border thickness thus the red
   * surrounding rectangle is displayed.
   */
  private TsComposite contentHolderBorder;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aContentPanel {@link AbstractInplaceContentPanel} - content panel
   * @throws TsNullArgumentRtException аргумент = null
   */
  public InplaceContainerPanel( ITsGuiContext aContext, AbstractInplaceContentPanel aContentPanel ) {
    super( aContext );
    TsNullArgumentRtException.checkNull( aContentPanel );
    TsIllegalArgumentRtException.checkTrue( aContentPanel.getControl() != null );
    contentPanel = aContentPanel;
    eventer = new GenericChangeEventer( this );
    eventer.addListener( s -> whenContentPanelChanges() );
    aContentPanel.genericChangeEventer().addListener( eventer );
    IStridablesListEdit<ITsActionDef> ll = new StridablesList<>( DEFAULT_EDIT_ACTION_DEFS );
    ll.addAll( contentPanel.listSupportedActions() );
    editActionDefs = ll;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private void whenButtonPressed( String aActionId ) {
    switch( aActionId ) {
      case ACTID_EDIT: {
        startEditing();
        return;
      }
      case ACTID_OK_CHANGES: {
        applyAndFinishEditing();
        break;
      }
      case ACTID_CANCEL_CHANGES: {
        cancelAndFinishEditing();
        break;
      }
      case ACTID_APPLY_CHANGES: {
        applyAndContinueEditing();
        break;
      }
      case ACTID_REVERT_CHANGES: {
        cancelAndContinueEditing();
        break;
      }
      default: {
        contentPanel.handleAction( aActionId );
        break;
      }
    }
  }

  void whenContentPanelChanges() {
    updatePanelState();
  }

  private void applyAndContinueEditing() {
    if( isEditing() ) {
      contentPanel.applyChanges();
      updatePanelState();
    }
  }

  private void cancelAndContinueEditing() {
    if( isEditing() ) {
      contentPanel.revertChanges();
      updatePanelState();
    }
  }

  private ValidationResult updateValidatonPane() {
    ValidationResult vr = ValidationResult.SUCCESS;
    if( isEditing() ) {
      vr = contentPanel.validate();
    }
    else {
      vr = contentPanel.canStartEditing();
    }
    if( vrPanel != null ) {
      vrPanel.setShownValidationResult( vr );
    }
    return vr;
  }

  private void updateButtonsEnabledState( ValidationResult aVr ) {
    if( isEditing() ) {
      for( ITsActionDef adef : editActionDefs ) {
        boolean enabled = switch( adef.id() ) {
          case ACTID_OK_CHANGES -> !aVr.isError();
          case ACTID_CANCEL_CHANGES -> true;
          case ACTID_REVERT_CHANGES, ACTID_APPLY_CHANGES -> isChanged();
          default -> contentPanel.isActionEnabled( adef.id() );
        };
        buttonsBar.setActionEnabled( adef.id(), enabled );
      }
    }
    else {
      buttonsBar.setActionEnabled( ACTID_EDIT, !aVr.isError() );
      // set tooltip with reason why the editing is disabled
      // NOTE: disabled button does not shows tootip so we'll set/resety tooltip for whole byutton bar
      if( aVr.isError() ) {
        buttonsBar.setButtonTooltip( ACTID_EDIT, aVr.message() );
        buttonsBar.setToolTipText( aVr.message() );
      }
      else {
        buttonsBar.setButtonTooltip( ACTID_EDIT, ACDEF_EDIT.description() );
        buttonsBar.setToolTipText( TsLibUtils.EMPTY_STRING );
      }
    }
  }

  private void resetButtonPane() {
    ValidationResult vr = updateValidatonPane();
    buttonsBar.removeButtons();
    if( isEditing() ) {
      for( ITsActionDef adef : editActionDefs ) {
        buttonsBar.createButton( adef );
      }
    }
    else {
      buttonsBar.createButton( ACDEF_EDIT );
    }
    updateButtonsEnabledState( vr );
  }

  private void updatePanelState() {
    ValidationResult vr = updateValidatonPane();
    updateButtonsEnabledState( vr );
  }

  private void internalSetEditMode( boolean aMode ) {
    if( isEditing() == aMode ) {
      return;
    }
    backplane.setLayoutDeferred( true );
    try {
      contentPanel.setEditMode( aMode );
      // hide/show "red border" and #vrPanel
      if( aMode ) {
        int borderThinkness = OPDEF_BORDER_THICKNESS.getValue( tsContext().params() ).asInt();
        contentHolderBorderLayout.setMargins( borderThinkness, borderThinkness, borderThinkness, borderThinkness );
      }
      else {
        contentHolderBorderLayout.setMargins( 0, 0, 0, 0 );
      }
      if( vrPanel != null ) {
        vrPanel.setVisible( aMode );
      }
      resetButtonPane();
    }
    finally {
      backplane.getParent().layout( true, true );
      backplane.setLayoutDeferred( false );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractLazyPanel
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    // backplane
    backplane = new TsComposite( aParent );
    backplane.setLayout( new BorderLayout() );
    // messagePanel
    if( OPDEF_IS_VALIDATION_USED.getValue( tsContext().params() ).asBool() ) {
      vrPanel = new ValidationResultPanel( backplane, tsContext() );
      vrPanel.setLayoutData( BorderLayout.NORTH );
      vrPanel.setVisible( false );
    }
    // contentHolderBorder
    contentHolderBorder = new TsComposite( backplane, SWT.NONE );
    contentHolderBorderLayout = new BorderLayout();
    contentHolderBorder.setLayout( contentHolderBorderLayout );
    contentHolderBorder.setLayoutData( BorderLayout.CENTER );
    RGB rgb = OPDEF_BORDER_COLOR.getValue( tsContext().params() ).asValobj();
    contentHolderBorder.setBackground( colorManager().getColor( rgb ) );
    // contentHolder
    contentHolder = new ScrolledComposite( contentHolderBorder, SWT.H_SCROLL | SWT.V_SCROLL );
    contentHolder.setLayoutData( BorderLayout.CENTER );
    // contentPanel
    contentPanel.createControl( contentHolder );
    contentHolder.setContent( contentPanel.getControl() );
    contentHolder.setSize( contentPanel.getControl().computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    contentHolder.setExpandHorizontal( true );
    contentHolder.setExpandVertical( true );
    // buttonsPane
    buttonsBar = new ButtonBar( backplane, tsContext(), this::whenButtonPressed );
    buttonsBar.setLayoutData( BorderLayout.SOUTH );
    buttonsBar.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    // setup
    resetButtonPane();
    return backplane;
  }

  // ------------------------------------------------------------------------------------
  // IInplaceEditorPanel
  //

  @Override
  public boolean isViewer() {
    return contentPanel.isViewer();
  }

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  @Override
  public boolean isEditing() {
    return contentPanel.isEditing();
  }

  @Override
  public void startEditing() {
    if( !isEditing() ) {
      internalSetEditMode( true );
    }
  }

  @Override
  public boolean isChanged() {
    return contentPanel.isChanged();
  }

  @Override
  public void applyAndFinishEditing() {
    if( isEditing() ) {
      contentPanel.applyChanges();
      internalSetEditMode( false );
    }
  }

  @Override
  public void cancelAndFinishEditing() {
    if( isEditing() ) {
      contentPanel.revertChanges();
      internalSetEditMode( false );
    }
  }

  @Override
  public void refresh() {
    updatePanelState();
  }

}
