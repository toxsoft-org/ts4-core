package org.toxsoft.tsgui.m5.gui;

import static org.toxsoft.tsgui.m5.gui.ITsResources.*;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.dialogs.ETsDialogCode;
import org.toxsoft.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.tsgui.dialogs.datarec.*;
import org.toxsoft.tsgui.m5.IM5Bunch;
import org.toxsoft.tsgui.m5.IM5Model;
import org.toxsoft.tsgui.m5.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.tsgui.utils.layout.BorderLayout;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

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
   * @param <T> - M5-modelled entity type
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
   * Shows modelled entity creation dialog and creates new instance.
   *
   * @param <T> - M5-modelled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aValues {@link IM5Bunch} - initl values of entity fields or <code>null</code>
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
   * Shows modelled entity edit dialog and then edits the entity.
   *
   * @param <T> - M5-modelled entity type
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aEntity &lt;T&gt - the entity to edit
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window properties
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager
   * @return &lt;T&gt; - edited entity or <code>null</code> if user cancelled operation
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static <T> T askEdit( ITsGuiContext aContext, IM5Model<T> aModel, T aEntity, ITsDialogInfo aDialogInfo,
      IM5LifecycleManager<T> aLifecycleManager ) {
    TsNullArgumentRtException.checkNulls( aContext, aDialogInfo, aEntity, aLifecycleManager );
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
   * Asks confirmation and removes the specified entity.
   * <p>
   * Method does the following:
   * <ul>
   * <li>checks via method {@link IM5LifecycleManager#canRemove(Object)} if entity could be removed;</li>
   * <li>shows item removal confirmation dialog;</li>
   * <li>removes an entity using the method {@link IM5LifecycleManager#remove(Object)}.</li>
   * </ul>
   *
   * @param <T> - M5-modelled entity type
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
   * No subclassing.
   */
  private M5GuiUtils() {
    // nop
  }

}
