package org.toxsoft.core.tsgui.m5.gui.panels.impl;

import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsDoubleClickListener;
import org.toxsoft.core.tsgui.bricks.stdevents.ITsSelectionChangeListener;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.TsDoubleClickEventHelper;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.TsSelectionChangeEventHelper;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5CollectionPanel;
import org.toxsoft.core.tsgui.m5.model.IM5ItemsProvider;
import org.toxsoft.core.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.core.tsgui.utils.checkcoll.ITsCheckSupport;
import org.toxsoft.core.tslib.coll.IList;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Base implementation on {@link IM5CollectionPanel}.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public abstract class M5AbstractCollectionPanel<T>
    extends M5PanelBase<T>
    implements IM5CollectionPanel<T> {

  protected final TsSelectionChangeEventHelper<T> selectionChangeEventHelper;
  protected final TsDoubleClickEventHelper<T>     doubleClickEventHelper;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @param aViewer boolean - flags that viewer (not editor) will be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected M5AbstractCollectionPanel( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    super( aContext, aModel, aViewer );
    selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
    doubleClickEventHelper = new TsDoubleClickEventHelper<>( this );
  }

  // ------------------------------------------------------------------------------------
  // ITsSelectionProvider
  //

  @Override
  public void addTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeEventHelper.addTsSelectionListener( aListener );
  }

  @Override
  public void removeTsSelectionListener( ITsSelectionChangeListener<T> aListener ) {
    selectionChangeEventHelper.removeTsSelectionListener( aListener );
  }

  @Override
  public abstract T selectedItem();

  @Override
  public abstract void setSelectedItem( T aItem );

  // ------------------------------------------------------------------------------------
  // ITsDoubleClickEventProducer
  //

  @Override
  public void addTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickEventHelper.addTsDoubleClickListener( aListener );
  }

  @Override
  public void removeTsDoubleClickListener( ITsDoubleClickListener<T> aListener ) {
    doubleClickEventHelper.removeTsDoubleClickListener( aListener );
  }

  // ------------------------------------------------------------------------------------
  // IGenericCollPanel
  //

  @Override
  public abstract IList<T> items();

  @Override
  public abstract void refresh();

  @Override
  public abstract ITsCheckSupport<T> checkSupport();

  // ------------------------------------------------------------------------------------
  // IM5CollectionPanel
  //

  @Override
  public abstract IM5ItemsProvider<T> itemsProvider();

  @Override
  public abstract void setItemsProvider( IM5ItemsProvider<T> aItemsProvider );

  @Override
  public abstract IM5LifecycleManager<T> lifecycleManager();

  @Override
  public abstract void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager );
}
