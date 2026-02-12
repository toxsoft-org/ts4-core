package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import java.util.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMultiPaneComponent} implementation uses lifecycle manager for CRUD operations.
 *
 * @author hazard157
 * @param <T> - displayed M5-modeled entity type
 */
public class MultiPaneComponentModown<T>
    extends MultiPaneComponent<T> {

  /**
   * The lifecycle manager for CRUD operations, for item enumeration {@link #itemsProvider()} is used
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
    ITsDialogInfo cdi = doCreateDialogInfoToAddItem();
    IM5LifecycleManager<T> lm = getNonNullLM();
    IM5BunchEdit<T> initVals = lm.createNewItemValues();
    doAdjustEntityCreationInitialValues( initVals );
    return M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, lm );
  }

  @Override
  protected T doAddCopyItem( T aSrcItem ) {
    ITsDialogInfo cdi = doCreateDialogInfoToAddCopyItem( aSrcItem );
    IM5LifecycleManager<T> lm = getNonNullLM();
    IM5BunchEdit<T> initVals = new M5BunchEdit<>( model() );
    initVals.fillFrom( aSrcItem, false ); // leave originalEntity = null
    doAdjustEntityTemplateCreationInitialValues( initVals, aSrcItem );
    return M5GuiUtils.askCreate( tsContext(), model(), initVals, cdi, lm );
  }

  @Override
  protected T doEditItem( T aItem ) {
    ITsDialogInfo cdi = doCreateDialogInfoToEditItem( aItem );
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
   * <p>
   * In the base class does nothing.
   *
   * @param aValues {@link IM5BunchEdit}&lt;T&gt; - initial values to adjust
   */
  protected void doAdjustEntityCreationInitialValues( IM5BunchEdit<T> aValues ) {
    // nop
  }

  /**
   * Subclass may adjust initial values for template-based entity creation.
   * <p>
   * Argument is new instance of an editable bunch returned by {@link IM5LifecycleManager#createNewItemValues()}.
   * <p>
   * Default implemetaion calls {@link #doAdjustEntityCreationInitialValues(IM5BunchEdit)} and for {@link IStridable}
   * items adjusts ID field if it the same as source item's ID.
   *
   * @param aValues {@link IM5BunchEdit}&lt;T&gt; - initial values to adjust
   * @param aSrcItem &lt;T&gt; - source item used as a template for new item creation
   */
  protected void doAdjustEntityTemplateCreationInitialValues( IM5BunchEdit<T> aValues, T aSrcItem ) {
    doAdjustEntityCreationInitialValues( aValues );
    // for IStridable items guess and prepare ID field
    IM5FieldDef<T, ?> fdef = model().fieldDefs().findByKey( FID_ID );
    if( fdef == null || !IStridable.class.isAssignableFrom( model().entityClass() ) ) {
      return;
    }
    // get new and source ID values as a raw objects
    IM5BunchEdit<T> srcBunch = new M5BunchEdit<>( model() );
    srcBunch.fillFrom( aSrcItem, true );
    Object rawNewId = aValues.get( FID_ID );
    Object rawSrcId = srcBunch.get( FID_ID );
    // if new and source are same, adjust new ID
    if( Objects.equals( rawSrcId, rawNewId ) ) {
      String newId = null;
      boolean isAtomicValue = false;
      if( fdef.valueClass().equals( String.class ) ) {
        newId = (String)rawNewId;
      }
      else {
        if( fdef.valueClass().equals( IAtomicValue.class ) ) {
          IAtomicValue av = (IAtomicValue)rawNewId;
          if( av.atomicType() == EAtomicType.STRING ) {
            isAtomicValue = true;
            newId = av.asString();
          }
        }
      }
      if( newId != null ) { // known field type class detected, process new ID change
        newId = StridUtils.createIdPathCopy( newId );
        rawNewId = isAtomicValue ? avStr( newId ) : newId;
        aValues.set( FID_ID, rawNewId );
      }
    }
  }

  /**
   * Subclass may set own parameters for item adding dialog {@link M5GuiUtils}<code>.askCreate()</code>.
   * <p>
   * In base class returns {@link TsDialogInfo#forCreateEntity(ITsGuiContext)}, there is no need to call parent method
   * when overriding.
   * <p>
   * Note: method is called from {@link #doAddItem()}.
   *
   * @return {@link ITsDialogInfo} - editing dialog window parameters
   */
  protected ITsDialogInfo doCreateDialogInfoToAddItem() {
    return TsDialogInfo.forCreateEntity( tsContext() );
  }

  /**
   * Subclass may set own parameters for adding item copy dialog {@link M5GuiUtils}<code>.askCreate()</code>.
   * <p>
   * In base class returns {@link TsDialogInfo#forCreateEntity(ITsGuiContext)}, there is no need to call parent method
   * when overriding.
   * <p>
   * Note: method is called from {@link #doAddCopyItem(Object)}.
   *
   * @param aSrcItem &lt;T&gt; - source item used as a template for new item creation
   * @return {@link ITsDialogInfo} - editing dialog window parameters
   */
  protected ITsDialogInfo doCreateDialogInfoToAddCopyItem( T aSrcItem ) {
    return TsDialogInfo.forCreateEntity( tsContext() );
  }

  /**
   * Subclass may set own parameters for item editing dialog {@link M5GuiUtils}<code>.askEdit()</code>..
   * <p>
   * In base class returns {@link TsDialogInfo#forEditEntity(ITsGuiContext)}, there is no need to call parent method
   * when overriding.
   * <p>
   * Note: method is called from {@link #doEditItem(Object)}.
   *
   * @param aItem &lt;T&gt; - the item to be edited
   * @return {@link ITsDialogInfo} - editing dialog window parameters
   */
  protected ITsDialogInfo doCreateDialogInfoToEditItem( T aItem ) {
    return TsDialogInfo.forEditEntity( tsContext() );
  }

}
