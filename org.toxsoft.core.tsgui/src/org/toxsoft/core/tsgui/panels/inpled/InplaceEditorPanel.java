package org.toxsoft.core.tsgui.panels.inpled;

import static org.toxsoft.core.tsgui.bricks.actions.ITsStdActionDefs.*;
import static org.toxsoft.core.tsgui.panels.inpled.IInplaceEditorConstants.*;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tsgui.panels.misc.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IInplaceEditorPanel} implementation.
 *
 * @author hazard157
 */
public class InplaceEditorPanel
    extends AbstractLazyPanel<Control>
    implements IInplaceEditorPanel {

  /**
   * Button bar for {@link InplaceEditorPanel}.
   *
   * @author hazard157
   */
  class ButtonBar
      extends Composite {

    private final SelectionAdapter buttonListener = new SelectionAdapter() {

      @Override
      public void widgetSelected( SelectionEvent aEvent ) {
        Button btn = (Button)aEvent.widget;
        processAction( (String)btn.getData() );
      }

    };

    private final EIconSize iconSize;

    private final IStringMapEdit<Button> btnsMap = new StringMap<>();

    public ButtonBar( Composite aParent ) {
      super( aParent, SWT.BORDER );
      iconSize = hdpiService().getDefaultIconSize(); // TODO how to specify icon size ?
    }

    private void createButton( ITsActionDef aActionDef ) {
      Button btn = new Button( this, SWT.PUSH );
      btn.setData( aActionDef.id() );
      btn.setText( aActionDef.nmName() );
      btn.setToolTipText( aActionDef.description() );
      btn.setImage( iconManager().loadStdIcon( aActionDef.iconId(), iconSize ) );
      btn.addSelectionListener( buttonListener );
      btnsMap.put( aActionDef.id(), btn );
    }

    /**
     * Расставляет кнопки на нижней панели {@link #buttonsPane}.
     * <p>
     * Сначала удаляет все конпки, а потом расставляет их согласно текущему состоянию.
     */
    void resetButtonPane() {
      removeButtons();

      // TODO ButtonBar.resetButtonPane()
      updateState();
    }

    void updateState() {
      // TODO ButtonBar.updateState()
    }

    private void removeButtons() {
      while( !btnsMap.isEmpty() ) {
        Button b = btnsMap.getByKey( btnsMap.keys().first() );
        b.dispose();
      }
    }

  }

  private final IGenericChangeListener panelChangeListener = aSource -> handlePanelChange();

  // ------------------------------------------------------------------------------------
  // Components of the panel

  private TsComposite           backplane;                 // persistent background panel
  private ValidationResultPanel vrPanel     = null;        // north panel of validation result display or null
  private BorderLayout          contentHolderBorderLayout; // layout for #contentHolderBorder
  private ScrolledComposite     contentHolder;             // scroller contains the inplace editor content panel
  private ButtonBar             buttonBar;                 // south button bar
  private IInplaceContentPanel  inpledPanel = null;        // inplacde editor content, may be null

  /**
   * Middle panel of the specified (default - RED) color contains scroller {@link #contentHolder}.
   * <p>
   * In editing mode size of the scroller comopnent is decreased by the specified border thickness thus the red
   * surrounding rectangle is displayed.
   */
  private TsComposite contentHolderBorder;

  // ------------------------------------------------------------------------------------
  // currenbt state fields

  /**
   * <code>true</code> - panel was changed, that is panel content does not natches edited entity.
   */
  boolean wasChanges = false;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public InplaceEditorPanel( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  void handlePanelChange() {
    TsInternalErrorRtException.checkNull( inpledPanel );
    wasChanges = true;
    updatePanelState();
  }

  /**
   * Updates validation panel (if any) and buttons enabled state on {@link #buttonBar}.
   */
  void updatePanelState() {
    ValidationResult vr = inpledPanel != null ? inpledPanel.validate() : ValidationResult.SUCCESS;
    vrPanel.setShownValidationResult( vr );
    buttonBar.updateState();
  }

  /**
   * Controls is surrounding colored rectangle is displayed.
   *
   * @param aShow boolean - <code>true</code> to show colored border
   */
  private void showRedBorder( boolean aShow ) {
    if( aShow ) {
      int borderThinkness = OPDEF_BORDER_THICKNESS.getValue( tsContext().params() ).asInt();
      contentHolderBorderLayout.setMargins( borderThinkness, borderThinkness, borderThinkness, borderThinkness );
    }
    else {
      contentHolderBorderLayout.setMargins( 0, 0, 0, 0 );
    }
    contentHolderBorder.layout();
  }

  private void undefferLayout() {
    // FIXME что-то надо делать с исключением Widget is disposed!!!
    backplane.getParent().layout( true, true );
    backplane.setLayoutDeferred( false );
  }

  void processAction( String aActionId ) {
    switch( aActionId ) {
      case ACTID_EDIT: {
        // TODO InplaceEditorPanel.processAction()
        break;
      }
      case ACTID_OK_CHANGES: {
        // TODO InplaceEditorPanel.processAction()
        break;
      }
      case ACTID_APPLY_CHANGES: {
        // TODO InplaceEditorPanel.processAction()
        break;
      }
      case ACTID_CANCEL_CHANGES: {
        // TODO InplaceEditorPanel.processAction()
        break;
      }
      case ACTID_REVERT_CHANGES: {
        // TODO InplaceEditorPanel.processAction()
        break;
      }
      case ACTID_RESTORE_DEFAULTS: {
        // TODO InplaceEditorPanel.processAction()
        break;
      }
      default:
        throw new TsNotAllEnumsUsedRtException( aActionId );
    }

    // TODO InplaceEditorPanel.processAction()
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
    // buttonsPane
    buttonBar = new ButtonBar( backplane );
    buttonBar.setLayoutData( BorderLayout.SOUTH );
    buttonBar.setLayout( new RowLayout( SWT.HORIZONTAL ) );
    buttonBar.resetButtonPane();
    // setup
    return backplane;
  }

  @Override
  protected void doDispose() {
    if( inpledPanel != null && inpledPanel.isEditing() ) {
      inpledPanel.actionCancel();
    }
  }

  // ------------------------------------------------------------------------------------
  // IInplaceEditorPanel
  //

  @Override
  public IInplaceContentPanel contentPanel() {
    return inpledPanel;
  }

  @Override
  public void setContentPanel( IInplaceContentPanel aPanel ) {
    // check preconditions
    if( Objects.equals( aPanel, inpledPanel ) ) {
      return;
    }
    if( aPanel != null ) {
      TsIllegalArgumentRtException.checkTrue( aPanel.getControl() != null );
      TsIllegalArgumentRtException.checkTrue( aPanel.isEditing() );
    }
    // remove old (if aby) and create new #inpledPanel
    backplane.setLayoutDeferred( true );
    try {
      showRedBorder( false );
      if( inpledPanel != null ) {
        if( inpledPanel.isEditing() ) {
          inpledPanel.actionCancel();
        }
        if( inpledPanel.getControl() != null ) {
          inpledPanel.genericChangeEventer().removeListener( panelChangeListener );
          inpledPanel.getControl().dispose();
          contentHolder.setContent( null );
        }
        inpledPanel = aPanel;
        if( inpledPanel != null ) {
          // remove validation result display panel for readonly content panel
          if( vrPanel != null ) {
            vrPanel.setVisible( !inpledPanel.isReadonly() );
          }
          inpledPanel.createControl( contentHolder );
          contentHolder.setContent( inpledPanel.getControl() );
          contentHolder.setSize( inpledPanel.getControl().computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
          contentHolder.setExpandHorizontal( true );
          contentHolder.setExpandVertical( true );
          inpledPanel.genericChangeEventer().addListener( panelChangeListener );
          buttonBar.resetButtonPane();
        }
      }
    }
    finally {
      undefferLayout();
    }
  }

  void removeContentPanel() {
    if( inpledPanel != null ) {
      // удалим и обнулим панель
      try {
        if( inpledPanel.getControl() != null ) {
          if( inpledPanel.isEditing() ) {
            inpledPanel.actionCancel();
          }
          inpledPanel.genericChangeEventer().removeListener( panelChangeListener );
          inpledPanel.getControl().dispose();
          contentHolder.setContent( null );
        }
      }
      catch( Exception ex ) {
        LoggerUtils.errorLogger().error( ex );
      }
      inpledPanel = null;
      buttonBar.resetButtonPane();
    }
  }

}
