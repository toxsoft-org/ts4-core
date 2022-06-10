package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tsgui.m5.gui.mpc.impl.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.gui.viewers.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IMultiPaneComponent} implementation uses lookup provider for container items management.
 * <p>
 * Generates {@link IGenericChangeListener#onGenericChangeEvent(Object)} when {@link #items()} list changes.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MultiPaneComponentLookup<T>
    extends MultiPaneComponent<T>
    implements IGenericChangeEventCapable {

  private final GenericChangeEventer eventer;

  private IM5LookupProvider<T>      lookupProvider         = IM5LookupProvider.EMPTY;
  private M5DefaultItemsProvider<T> containedItemsProvider = new M5DefaultItemsProvider<>();
  private CollConstraint            collConstraint         = CollConstraint.NONE;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the model
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup provider or <code>null</code>
   */
  public MultiPaneComponentLookup( ITsGuiContext aContext, IM5Model<T> aModel, IM5LookupProvider<T> aLookupProvider ) {
    super( new M5TreeViewer<>( makeContext( aContext, true ), aModel ) );
    eventer = new GenericChangeEventer( this );
    setLookupProvider( aLookupProvider );
    super.setItemProvider( containedItemsProvider );
    containedItemsProvider.genericChangeEventer().addListener( aSource -> eventer.fireChangeEvent() );
  }

  private static ITsGuiContext makeContext( ITsGuiContext aContext, boolean aEditor ) {
    ITsGuiContext ctx = new TsGuiContext( aContext );
    OPDEF_IS_ACTIONS_CRUD.setValue( ctx.params(), AvUtils.avBool( aEditor ) );
    return ctx;
  }

  // ------------------------------------------------------------------------------------
  // MultiPaneComponent
  //

  @Override
  protected boolean doGetIsAddAllowed( T aSel ) {
    return !collConstraint.isSizeResticted() || //
        containedItemsProvider.items().size() < collConstraint.maxCount();
  }

  @Override
  protected boolean doGetIsEditAllowed( T aSel ) {
    return aSel != null && //
        containedItemsProvider.items().size() != lookupProvider.listItems().size();
  }

  @Override
  protected boolean doGetIsRemoveAllowed( T aSel ) {
    return aSel != null;
  }

  @Override
  protected T doAddItem() {
    ITsDialogInfo cdi = TsDialogInfo.forSelectEntity( tsContext() );
    T item = M5GuiUtils.askSelectLookupItem( cdi, model(), null, lookupProvider );
    if( item != null ) {
      containedItemsProvider.items().add( item );
    }
    return item;
  }

  @Override
  protected T doEditItem( T aItem ) {
    ITsDialogInfo cdi = TsDialogInfo.forSelectEntity( tsContext() );
    T item = M5GuiUtils.askSelectLookupItem( cdi, model(), aItem, lookupProvider );
    if( item != null ) {
      int index = containedItemsProvider.items().indexOf( aItem );
      if( index >= 0 ) {
        containedItemsProvider.items().set( index, item );
      }
      else {
        containedItemsProvider.items().add( item );
      }
    }
    return item;
  }

  @Override
  protected boolean doRemoveItem( T aItem ) {
    if( TsDialogUtils.askYesNoCancel( getShell(), FMT_ASK_REMOVE_ITEM,
        model().visualsProvider().getName( aItem ) ) != ETsDialogCode.YES ) {
      return false;
    }
    return containedItemsProvider.items().remove( aItem ) >= 0;
  }

  @Override
  public void setItemProvider( IM5ItemsProvider<T> aItemsProvider ) {
    // changing items provider is not allowed
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Class API
  //

  /**
   * Returns the contained items.
   *
   * @return {@link IList}&lt;&gt; - contained items list
   */
  public IList<T> items() {
    return tree().items();
  }

  /**
   * Sets the contained items.
   * <p>
   * Items not in provided items list are ignored.
   *
   * @param aItems {@link IList}&lt;T&gt; - contained items list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setItems( IList<T> aItems ) {
    IListEdit<T> ll = new ElemArrayList<>( aItems.size() );
    IList<T> lookupList = lookupProvider.listItems();
    for( T item : aItems ) {
      if( lookupList.hasElem( item ) ) {
        ll.add( item );
      }
    }
    containedItemsProvider.items().setAll( ll );
  }

  /**
   * Returns the lookup items provider.
   *
   * @return {@link IM5LookupProvider} - the lookup items provider, never is <code>null</code>
   */
  public final IM5LookupProvider<T> lookupProvider() {
    return lookupProvider;
  }

  /**
   * Sets the lookup items provider.
   *
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup items provider or <code>null</code> for empty one
   */
  public final void setLookupProvider( IM5LookupProvider<T> aLookupProvider ) {
    lookupProvider = (aLookupProvider != null) ? aLookupProvider : IM5LookupProvider.EMPTY;
    setItems( containedItemsProvider.items() );
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
