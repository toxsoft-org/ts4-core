package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.coll.helpers.*;
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
   * Constructor - creates instance to view entities, not to edit.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   */
  public MultiPaneComponentModown( ITsGuiContext aContext, IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider ) {
    super( new M5TreeViewer<>( aContext, aModel, //
        OPDEF_IS_SUPPORTS_CHECKS.getValue( aContext.params() ).asBool() ) );
    setItemProvider( aItemsProvider );
  }

  /**
   * Constructor - creates instance to edit entities.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  public MultiPaneComponentModown( ITsGuiContext aContext, IM5Model<T> aModel, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    super( new M5TreeViewer<>( aContext, aModel, OPDEF_IS_SUPPORTS_CHECKS.getValue( aContext.params() ).asBool() ) );
    setItemProvider( aItemsProvider );
    setLifecycleManager( aLifecycleManager );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private IM5LifecycleManager<T> getNonNullLM() {
    if( lifecycleManager == null ) {
      throw new TsIllegalStateRtException();
    }
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
    IM5LifecycleManager<T> lm = getNonNullLM();
    IM5BunchEdit<T> initVals = lm.createNewItemValues();
    doAdjustEntityCreationInitialValues( initVals );
    // dima 06.03.23
    // return M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, null );
    return M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, lm );
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
   * Argument is new instance of an editable bunch returned by {@link IM5LifecycleManager#createNewItemValues()}.
   *
   * @param aValues {@link IM5BunchEdit}&lt;T&gt; - initial values to adjust
   */
  protected void doAdjustEntityCreationInitialValues( IM5BunchEdit<T> aValues ) {
    // nop
  }

}
