package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogInfo;
import org.toxsoft.core.tsgui.dialogs.datarec.TsDialogInfo;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.M5GuiUtils;
import org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponent;
import org.toxsoft.core.tsgui.m5.gui.viewers.IM5TreeViewer;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.M5TreeViewer;
import org.toxsoft.core.tsgui.m5.model.IM5ItemsProvider;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.m5.model.impl.M5BunchEdit;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tslib.av.impl.AvUtils;
import org.toxsoft.core.tslib.coll.helpers.ECrudOp;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMultiPaneComponent} implementation uses lifecycle manager for CRUD operations.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MultiPaneComponentModown<T>
    extends MultiPaneComponent<T> {

  /**
   * The lifecycle mnagaer for CRUD operations and items enumeration.
   */
  private IM5LifecycleManager<T> lifecycleManager = null;

  /**
   * Constructor.
   *
   * @param aViewer {@link IM5TreeViewer} - the tree viewer to be used
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException viewer has {@link ILazyControl#getControl()} already initialized
   */
  public MultiPaneComponentModown( IM5TreeViewer<T> aViewer ) {
    super( aViewer );
  }

  /**
   * Creates instance to view entities, not to edit.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   */
  public MultiPaneComponentModown( ITsGuiContext aContext, IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider ) {
    super( new M5TreeViewer<>( makeContext( aContext, false ), aModel ) );
    setItemProvider( aItemsProvider );
  }

  /**
   * Creates instance to edit entities.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  public MultiPaneComponentModown( ITsGuiContext aContext, IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    super( new M5TreeViewer<>( makeContext( aContext, true ), aModel ) );
    setItemProvider( aItemsProvider );
    setLifecycleManager( aLifecycleManager );
  }

  private static ITsGuiContext makeContext( ITsGuiContext aContext, boolean aEditor ) {
    ITsGuiContext ctx = new TsGuiContext( aContext );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.avBool( aEditor ) );
    return ctx;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IM5LifecycleManager<T> getNonNullLM() {
    TsIllegalStateRtException.checkNull( lifecycleManager );
    return lifecycleManager;
  }

  // ------------------------------------------------------------------------------------
  // MultiPaneComponent
  //

  @Override
  protected boolean doGetAlive() {
    return lifecycleManager != null;
  }

  @Override
  protected boolean doGetIsAddAllowed( T aSel ) {
    return lifecycleManager.isCrudOpAllowed( ECrudOp.CREATE );
  }

  @Override
  protected boolean doGetIsEditAllowed( T aSel ) {
    return lifecycleManager.isCrudOpAllowed( ECrudOp.EDIT );
  }

  @Override
  protected boolean doGetIsRemoveAllowed( T aSel ) {
    return lifecycleManager.isCrudOpAllowed( ECrudOp.REMOVE );
  }

  @Override
  protected T doAddItem() {
    ITsDialogInfo cdi = TsDialogInfo.forCreateEntity( tsContext() );
    IM5BunchEdit<T> initVals = new M5BunchEdit<>( model() );
    doAdjustEntityCreationInitialValues( initVals );
    return M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, getNonNullLM() );
  }

  @Override
  protected T doEditItem( T aItem ) {
    ITsDialogInfo cdi = TsDialogInfo.forEditEntity( tsContext() );
    return M5GuiUtils.askEdit( tsContext(), model(), aItem, cdi, getNonNullLM() );
  }

  @Override
  protected boolean doRemoveItem( T aItem ) {
    return M5GuiUtils.askRemove( tsContext(), model(), aItem, getShell(), getNonNullLM() );
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Returns the lifecycle manager.
   *
   * @return {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  public IM5LifecycleManager<T> lifecycleManager() {
    return lifecycleManager;
  }

  /**
   * Sets the lifecycle manager.
   *
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  public void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager ) {
    lifecycleManager = aLifecycleManager;
  }

  // ------------------------------------------------------------------------------------
  // to override
  //

  /**
   * Subclass may adjust initial values for entity creation.
   * <p>
   * Example usage is to set non-default values for fields flagged with {@link IM5Constants#M5FF_HIDDEN} flag.
   *
   * @param aValues {@link IM5BunchEdit}&lt;T&gt; - initial values created by {@link M5BunchEdit#M5BunchEdit(IM5Model)}
   */
  protected void doAdjustEntityCreationInitialValues( IM5BunchEdit<T> aValues ) {
    // nop
  }

}
