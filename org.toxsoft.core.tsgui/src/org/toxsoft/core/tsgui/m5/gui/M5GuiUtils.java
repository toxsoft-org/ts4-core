package org.toxsoft.core.tsgui.m5.gui;

import static org.toxsoft.core.tsgui.m5.gui.ITsResources.*;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * M5 GUI utility methods.
 *
 * @author hazard157
 */
public class M5GuiUtils {

  /**
   * Internal class to implement {@link M5GuiUtils}.askXxx() methods.
   *
   * @author hazard157
   * @param <T> - modeled entity type
   */
  static class AskDialogContentPanel<T>
      extends AbstractTsDialogPanel<IM5Bunch<T>, ITsGuiContext> {

    final IM5EntityPanel<T> panel;
    final boolean           isCreation;

    protected AskDialogContentPanel( Composite aParent, TsDialog<IM5Bunch<T>, ITsGuiContext> aOwnerDialog,
        IM5EntityPanel<T> aPanel, boolean aIsCreation ) {
      super( aParent, aOwnerDialog );
      panel = TsNullArgumentRtException.checkNull( aPanel );
      isCreation = aIsCreation;
      this.setLayout( new BorderLayout() );
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
      panel.genericChangeEventer().addListener( notificationGenericChangeListener );
    }

    @Override
    protected void doSetDataRecord( IM5Bunch<T> aData ) {
      panel.setValues( aData );
    }

    @Override
    protected IM5Bunch<T> doGetDataRecord() {
      return panel.getValues();
    }

    @Override
    protected ValidationResult validateData() {
      ValidationResult vr = panel.canGetValues();
      if( vr.isError() ) {
        return vr;
      }
      if( panel.lifecycleManager() != null ) {
        if( isCreation ) {
          vr = ValidationResult.firstNonOk( vr, panel.lifecycleManager().canCreate( panel.getValues() ) );
        }
        else {
          vr = ValidationResult.firstNonOk( vr, panel.lifecycleManager().canEdit( panel.getValues() ) );
        }
      }
      return vr;
    }

  }

  /**
   * Item selection panel from the list provided by {@link IM5ItemsProvider}.
   * <p>
   * Used by {@link M5GuiUtils#askSelectItem(ITsDialogInfo, IM5Model, Object, IM5ItemsProvider, IM5LifecycleManager)}.
   *
   * @author hazard157
   * @param <T> - modeled entity type
   */
  static class SelectItemDialogContentPanel<T>
      extends AbstractTsDialogPanel<T, Object> {

    final IM5CollectionPanel<T> panel;

    protected SelectItemDialogContentPanel( Composite aParent, TsDialog<T, Object> aOwnerDialog,
        IM5CollectionPanel<T> aPanel ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      panel = aPanel;
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
      panel.addTsDoubleClickListener( ( aSource, aSelectedItem ) -> {
        fireContentChangeEvent();
        ownerDialog().closeDialog( ETsDialogCode.OK );
      } );
      panel.addTsSelectionListener( ( aSource, aSelectedItem ) -> fireContentChangeEvent() );
    }

    @Override
    protected ValidationResult validateData() {
      if( panel.selectedItem() != null ) {
        return ValidationResult.SUCCESS;
      }
      return ValidationResult.error( MSG_ERR_NO_SELECTED_ITEM );
    }

    @Override
    protected void doSetDataRecord( T aData ) {
      panel.setSelectedItem( aData );
    }

    @Override
    protected T doGetDataRecord() {
      return panel.selectedItem();
    }

  }

  /**
   * Checked items list panel from the list provided by {@link IM5ItemsProvider}.
   * <p>
   * Used by {@link M5GuiUtils#askSelectItemsList(ITsDialogInfo, IM5Model, IList, IM5ItemsProvider)}.
   *
   * @author hazard157
   * @param <T> - modeled entity type
   */
  static class SelectItemsListDialogContentPanel<T>
      extends AbstractTsDialogPanel<IList<T>, Object> {

    final IM5CollectionPanel<T> panel;

    protected SelectItemsListDialogContentPanel( Composite aParent, TsDialog<IList<T>, Object> aOwnerDialog,
        IM5CollectionPanel<T> aPanel ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      panel = aPanel;
      TsInternalErrorRtException.checkFalse( panel.checkSupport().isChecksSupported() );
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
      panel.addTsDoubleClickListener( ( aSource, aSelectedItem ) -> {
        fireContentChangeEvent();
        ownerDialog().closeDialog( ETsDialogCode.OK );
      } );
      panel.addTsSelectionListener( ( aSource, aSelectedItem ) -> fireContentChangeEvent() );
    }

    @Override
    protected ValidationResult validateData() {
      return ValidationResult.SUCCESS;
    }

    @Override
    protected void doSetDataRecord( IList<T> aData ) {
      if( aData != null ) {
        panel.checkSupport().setItemsCheckState( aData, true );
      }
      else {
        panel.checkSupport().setAllItemsCheckState( false );
      }
    }

    @Override
    protected IList<T> doGetDataRecord() {
      return panel.checkSupport().listCheckedItems( true );
    }

  }

  /**
   * Item selection panel from the list provided by {@link IM5LookupProvider}.
   * <p>
   * Used by {@link M5GuiUtils#askSelectLookupItem(ITsDialogInfo, IM5Model, Object, IM5LookupProvider)}.
   *
   * @author hazard157
   * @param <T> - modeled entity type
   */
  static class SelectLookupItemDialogContentPanel<T>
      extends AbstractTsDialogPanel<T, Object> {

    final IM5CollectionPanel<T> panel;

    protected SelectLookupItemDialogContentPanel( Composite aParent, TsDialog<T, Object> aOwnerDialog,
        IM5Model<T> aModel, IM5LookupProvider<T> aLookupProvider ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      IM5ItemsProvider<T> itemsProvider = new M5ItemsFromLookupProvider<>( aLookupProvider );
      ITsGuiContext ctx = new TsGuiContext( tsContext() );
      ctx.params().setBool( IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD, false );
      panel = aModel.panelCreator().createCollViewerPanel( ctx, itemsProvider );
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
      panel.addTsDoubleClickListener( ( aSource, aSelectedItem ) -> {
        fireContentChangeEvent();
        ownerDialog().closeDialog( ETsDialogCode.OK );
      } );
      panel.addTsSelectionListener( ( aSource, aSelectedItem ) -> fireContentChangeEvent() );
    }

    @Override
    protected ValidationResult validateData() {
      if( panel.selectedItem() != null ) {
        return ValidationResult.SUCCESS;
      }
      return ValidationResult.error( MSG_ERR_NO_SELECTED_ITEM );
    }

    @Override
    protected void doSetDataRecord( T aData ) {
      panel.setSelectedItem( aData );
    }

    @Override
    protected T doGetDataRecord() {
      return panel.selectedItem();
    }

  }

  /**
   * Modown items list editing dialog panel.
   *
   * @author hazard157
   * @param <T> - modeled entity type
   */
  static class EditModownCollDialogPanel<T>
      extends AbstractTsDialogPanel<IList<T>, Object> {

    final IM5CollectionPanel<T> panel;

    EditModownCollDialogPanel( Composite aParent, TsDialog<IList<T>, Object> aOwnerDialog, IM5Model<T> aItemsModel,
        IM5LifecycleManager<T> aLifecycleManager ) {
      super( aParent, aOwnerDialog );
      this.setLayout( new BorderLayout() );
      panel = aItemsModel.panelCreator().createCollEditPanel( tsContext(), aLifecycleManager.itemsProvider(),
          aLifecycleManager );
      panel.createControl( this );
      panel.getControl().setLayoutData( BorderLayout.CENTER );
    }

    @Override
    protected void doSetDataRecord( IList<T> aData ) {
      // items are provided by the lifecycle manager
      panel.refresh();
    }

    @Override
    protected IList<T> doGetDataRecord() {
      return new ElemArrayList<>( panel.items() );
    }

  }

  /**
   * Shows modeled entity creation dialog and creates new instance.
   *
   * @param <T> - M5-modeled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aValues {@link IM5Bunch} - initial values of entity fields or <code>null</code>
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager
   * @return &lt;T&gt; - created entity or <code>null</code> if user cancelled operation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> T askCreate( ITsGuiContext aContext, IM5Model<T> aModel, IM5Bunch<T> aValues,
      ITsDialogInfo aDialogInfo, IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNulls( aContext, aModel, aDialogInfo, aLifecycleManager );
    IDialogPanelCreator<IM5Bunch<T>, ITsGuiContext> creator = ( aParent, aOwnerDialog ) -> {
      IM5EntityPanel<T> panel = aModel.panelCreator().createEntityEditorPanel( aContext, aLifecycleManager );
      return new AskDialogContentPanel<>( aParent, aOwnerDialog, panel, true );
    };
    IM5Bunch<T> initVals = aValues;
    if( initVals == null ) {
      initVals = aModel.valuesOf( null );
    }
    TsDialog<IM5Bunch<T>, ITsGuiContext> d = new TsDialog<>( aDialogInfo, initVals, aContext, creator );
    IM5Bunch<T> vals = d.execData();
    if( vals == null ) {
      return null;
    }
    return aLifecycleManager.create( vals );
  }

  /**
   * Shows modeled entity creation dialog and creates new instance.
   *
   * @param <T> - M5-modeled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aPanel {@link IM5EntityPanel}&lt;T&gt - entity editor panel
   * @param aValues {@link IM5Bunch} - initial field values for entity creation
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @return &lt;T&gt; - created entity or <code>null</code> if user cancelled operation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException panel does not have lifecycle manager specified
   */
  public static <T> T askCreate( ITsGuiContext aContext, IM5EntityPanel<T> aPanel, IM5Bunch<T> aValues,
      ITsDialogInfo aDialogInfo ) {
    TsNullArgumentRtException.checkNulls( aContext, aPanel, aDialogInfo );
    TsIllegalArgumentRtException.checkTrue( aPanel.lifecycleManager() == null );
    IDialogPanelCreator<IM5Bunch<T>, ITsGuiContext> creator =
        ( aParent, aOwnerDialog ) -> new AskDialogContentPanel<>( aParent, aOwnerDialog, aPanel, true );
    IM5Bunch<T> initVals = aValues;
    if( initVals == null ) {
      initVals = aPanel.model().valuesOf( null );
    }
    ITsGuiContext ctx = new TsGuiContext( aContext );
    TsDialog<IM5Bunch<T>, ITsGuiContext> d = new TsDialog<>( aDialogInfo, initVals, ctx, creator );
    IM5Bunch<T> vals = d.execData();
    if( vals == null ) {
      return null;
    }
    return aPanel.lifecycleManager().create( vals );
  }

  /**
   * Shows modeled entity edit dialog and then edits the entity.
   *
   * @param <T> - M5-modeled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aEntity &lt;T&gt - the entity to edit or <code>null</code> for default values of entity fields
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager
   * @return &lt;T&gt; - edited entity or <code>null</code> if user cancelled operation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> T askEdit( ITsGuiContext aContext, IM5Model<T> aModel, T aEntity, ITsDialogInfo aDialogInfo,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNulls( aContext, aDialogInfo, aLifecycleManager );
    IDialogPanelCreator<IM5Bunch<T>, ITsGuiContext> creator = ( aParent, aOwnerDialog ) -> {
      IM5EntityPanel<T> panel = aModel.panelCreator().createEntityEditorPanel( aContext, aLifecycleManager );
      return new AskDialogContentPanel<>( aParent, aOwnerDialog, panel, false );
    };
    TsDialog<IM5Bunch<T>, ITsGuiContext> d =
        new TsDialog<>( aDialogInfo, aModel.valuesOf( aEntity ), aContext, creator );
    IM5Bunch<T> vals = d.execData();
    if( vals == null ) {
      return null;
    }
    return aLifecycleManager.edit( vals );
  }

  /**
   * Shows modeled entity edit dialog and then edits the entity.
   *
   * @param <T> - M5-modeled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aPanel {@link IM5EntityPanel}&lt;T&gt - entity editor panel
   * @param aEntity &lt;T&gt - the entity to edit or <code>null</code> for default values of entity fields
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @return &lt;T&gt; - edited entity or <code>null</code> if user cancelled operation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException panel does not have lifecycle manager specified
   */
  public static <T> T askEdit( ITsGuiContext aContext, IM5EntityPanel<T> aPanel, T aEntity,
      ITsDialogInfo aDialogInfo ) {
    TsNullArgumentRtException.checkNulls( aContext, aDialogInfo, aPanel );
    TsIllegalArgumentRtException.checkTrue( aPanel.lifecycleManager() == null );
    IDialogPanelCreator<IM5Bunch<T>, ITsGuiContext> creator =
        ( aParent, aOwnerDialog ) -> new AskDialogContentPanel<>( aParent, aOwnerDialog, aPanel, false );
    ITsGuiContext ctx = new TsGuiContext( aContext );
    TsDialog<IM5Bunch<T>, ITsGuiContext> d =
        new TsDialog<>( aDialogInfo, aPanel.model().valuesOf( aEntity ), ctx, creator );
    IM5Bunch<T> vals = d.execData();
    if( vals == null ) {
      return null;
    }
    return aPanel.lifecycleManager().edit( vals );
  }

  /**
   * Asks confirmation and removes the specified entity.
   * <p>
   * Method does the following:
   * <ul>
   * <li>checks via method {@link IM5LifecycleManager#canRemove(Object)} if entity could be removed;</li>
   * <li>shows item removal confirmation dialog;</li>
   * <li>removes an entity using the method {@link IM5LifecycleManager#remove(Object)}.</li>
   * </ul>
   *
   * @param <T> - M5-modeled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aRemovedObject &lt;T&gt - the entity to be removed
   * @param aShell {@link Shell} - the parent window for dialogs to show or <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - lifecycle manager that removes an item
   * @return boolean - a flag that the removal has been started (ie not canceled by the user)
   */
  public static <T> boolean askRemove( ITsGuiContext aContext, IM5Model<T> aModel, T aRemovedObject, Shell aShell,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNulls( aContext, aRemovedObject, aLifecycleManager );
    String itemName = aModel.visualsProvider().getName( aRemovedObject );
    ValidationResult vr = aLifecycleManager.canRemove( aRemovedObject );
    if( vr.isError() ) {
      TsDialogUtils.validationResult( aShell, vr );
      return false;
    }
    if( TsDialogUtils.askYesNoCancel( aShell, MSG_ASK_REMOVE_ITEM, itemName ) != ETsDialogCode.YES ) {
      return false;
    }
    if( TsDialogUtils.askContinueOnValidation( aShell, vr, MSG_ASK_CONTINUE_ITEM, itemName ) != ETsDialogCode.YES ) {
      return false;
    }
    aLifecycleManager.remove( aRemovedObject );
    return true;
  }

  /**
   * Shows provided items list and allows to select one.
   * <p>
   * If lifecycle manager is specified the dialog will contain editable panel creted by
   * {@link IM5PanelCreator#createCollEditPanel(ITsGuiContext, IM5ItemsProvider, IM5LifecycleManager)}, othewise viewer
   * panel be created by
   * {@link IM5PanelCreator#createCollEditPanel(ITsGuiContext, IM5ItemsProvider, IM5LifecycleManager)}.
   *
   * @param <T> - provided items class
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aModel {@link IM5Model} - lookup items model
   * @param aInitialSelected &lt;T&gt; - inititlly selected item or <code>null</code>
   * @param aItemsProvider {@link IM5ItemsProvider} - items provider
   * @param aLifecycleManager {@link IM5LifecycleManager} - lifecycle manager or <code>null</code>
   * @return &lt;T&gt; - selected item or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> T askSelectItem( ITsDialogInfo aDialogInfo, IM5Model<T> aModel, T aInitialSelected,
      IM5ItemsProvider<T> aItemsProvider, IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aModel, aItemsProvider );
    IDialogPanelCreator<T, Object> creator = ( aParent, aOwnerDlg ) -> {
      IM5CollectionPanel<T> panel;
      if( aLifecycleManager == null ) {
        panel = aModel.panelCreator().createCollViewerPanel( aOwnerDlg.tsContext(), aItemsProvider );
      }
      else {
        panel = aModel.panelCreator().createCollEditPanel( aOwnerDlg.tsContext(), aItemsProvider, aLifecycleManager );
      }
      return new SelectItemDialogContentPanel<>( aParent, aOwnerDlg, panel );
    };
    TsDialog<T, Object> d = new TsDialog<>( aDialogInfo, aInitialSelected, null, creator );
    return d.execData();
  }

  /**
   * Shows provided items list.
   *
   * @param <T> - provided items class
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aModel {@link IM5Model} - lookup items model
   * @param aItemsProvider {@link IM5ItemsProvider} - items provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> void showItemsList( ITsDialogInfo aDialogInfo, IM5Model<T> aModel,
      IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aModel, aItemsProvider );
    IDialogPanelCreator<T, Object> creator = ( aParent, aOwnerDlg ) -> {
      IM5CollectionPanel<T> panel;
      panel = aModel.panelCreator().createCollViewerPanel( aOwnerDlg.tsContext(), aItemsProvider );
      return new SelectItemDialogContentPanel<>( aParent, aOwnerDlg, panel );
    };
    TsDialog<T, Object> d = new TsDialog<>( aDialogInfo, null, null, creator );
    d.execDialog();
  }

  /**
   * Shows provided items list and allows to select any number of them.
   * <p>
   * Dialog will contain editable panel creted by
   * {@link IM5PanelCreator#createCollChecksPanel(ITsGuiContext, IM5ItemsProvider)}.
   *
   * @param <T> - provided items class
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aModel {@link IM5Model} - lookup items model
   * @param aInitialChecked {@link IList}&lt;T&gt; - inititlly selected items
   * @param aItemsProvider {@link IM5ItemsProvider} - items provider
   * @return {@link IList}&lt;T&gt; - checked items list or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> IList<T> askSelectItemsList( ITsDialogInfo aDialogInfo, IM5Model<T> aModel,
      IList<T> aInitialChecked, IM5ItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aModel, aItemsProvider );
    IDialogPanelCreator<IList<T>, Object> creator = ( aParent, aOwnerDlg ) -> {
      IM5CollectionPanel<T> panel =
          aModel.panelCreator().createCollChecksPanel( aOwnerDlg.tsContext(), aItemsProvider );
      return new SelectItemsListDialogContentPanel<>( aParent, aOwnerDlg, panel );
    };
    TsDialog<IList<T>, Object> d = new TsDialog<>( aDialogInfo, aInitialChecked, null, creator );
    return d.execData();
  }

  /**
   * Shows lookup items list and allows to select one.
   * <p>
   * Dialog does not allows to edit lookup items in the list.
   *
   * @param <T> - lookup items class
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aModel {@link IM5Model} - lookup items model
   * @param aInitialSelected &lt;T&gt; - inititlly selected item or <code>null</code>
   * @param aLookupProvider {@link IM5LookupProvider} - lookup items provider
   * @return &lt;T&gt; - selected item or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> T askSelectLookupItem( ITsDialogInfo aDialogInfo, IM5Model<T> aModel, T aInitialSelected,
      IM5LookupProvider<T> aLookupProvider ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aModel, aLookupProvider );
    IDialogPanelCreator<T, Object> creator = ( aParent,
        aOwnerDialog ) -> new SelectLookupItemDialogContentPanel<>( aParent, aOwnerDialog, aModel, aLookupProvider );
    TsDialog<T, Object> d = new TsDialog<>( aDialogInfo, aInitialSelected, null, creator );
    return d.execData();
  }

  /**
   * Shows modeled entity bunch edit dialog.
   * <p>
   * Depending on {@link IM5Bunch#originalEntity()} value checks either for creation (if originnale entity is
   * <code>null</code>) or editing.
   *
   * @param <T> - M5-modeled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aValues {@link IM5Bunch} - initial values of entity fields or <code>null</code>
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager
   * @return {@link IM5Bunch}&lt;T&gt; - edited bunch or <code>null</code> if user cancelled operation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> IM5Bunch<T> editBunch( ITsGuiContext aContext, IM5Model<T> aModel, IM5Bunch<T> aValues,
      ITsDialogInfo aDialogInfo, IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNulls( aContext, aModel, aDialogInfo, aLifecycleManager );
    IDialogPanelCreator<IM5Bunch<T>, ITsGuiContext> creator = ( aParent, aOwnerDialog ) -> {
      IM5EntityPanel<T> panel = aModel.panelCreator().createEntityEditorPanel( aContext, aLifecycleManager );
      return new AskDialogContentPanel<>( aParent, aOwnerDialog, panel, aValues.originalEntity() == null );
    };
    IM5Bunch<T> initVals = aValues;
    if( initVals == null ) {
      initVals = aLifecycleManager.createNewItemValues();
    }
    TsDialog<IM5Bunch<T>, ITsGuiContext> d = new TsDialog<>( aDialogInfo, initVals, aContext, creator );
    return d.execData();
  }

  /**
   * Shows editing dialog of the modown items list provided by the lifecycle manager.
   *
   * @param <T> - M5-modeled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aItemsModel {@link IM5Model} - the items model
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager
   * @return {@link IList}&lt;T&gt; - edited list or <code>null</code> if user cancelled operation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> IList<T> editModownColl( ITsGuiContext aContext, IM5Model<T> aItemsModel, ITsDialogInfo aDialogInfo,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNulls( aContext, aItemsModel, aDialogInfo, aLifecycleManager );
    IDialogPanelCreator<IList<T>, Object> creator = ( aParent, aOwnerDialog ) -> //
    new EditModownCollDialogPanel<>( aParent, aOwnerDialog, aItemsModel, aLifecycleManager );
    TsDialog<IList<T>, Object> d = new TsDialog<>( aDialogInfo, null, aContext, creator );
    return d.execData();
  }

  /**
   * Shows the specified panel in the dialog.
   * <p>
   * If dialog flags {@link ITsDialogInfo#flags()} requires OK button then returns the selected item.
   *
   * @param <T> - M5-modeled entity type
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @param aPanel {@link IM5CollectionPanel} - the panel to show in dialog
   * @return &lt;T&gt; - selected item or <code>null</code> if no selection or no OK button
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> T showCollPanel( ITsDialogInfo aDialogInfo, IM5CollectionPanel<T> aPanel ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aPanel );
    IDialogPanelCreator<T, Object> creator =
        ( aParent, aOwnerDlg ) -> new SelectItemDialogContentPanel<>( aParent, aOwnerDlg, aPanel );
    TsDialog<T, Object> d = new TsDialog<>( aDialogInfo, null, null, creator );
    if( (aDialogInfo.flags() & ITsDialogConstants.DF_NO_APPROVE) != 0 ) { // no OK buttin
      d.execDialog();
      return null;
    }
    // there is an OK button
    return d.execData();
  }

  /**
   * No subclasses.
   */
  private M5GuiUtils() {
    // nop
  }

}
