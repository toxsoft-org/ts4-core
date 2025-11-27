package org.toxsoft.core.tsgui.m5.gui.panels.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The {@link IM5MultiLookupPanel} implementation that wraps ove {@link MultiPaneComponentLookup}.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5MultiLookupPanelMpcLookupWrapper<T>
    extends M5AbstractCollectionPanel<T>
    implements IM5MultiLookupPanel<T> {

  private final MultiPaneComponentLookup<T> source;

  /**
   * Constructor.
   *
   * @param aMpc {@link MultiPaneComponentLookup} - the wrapped component
   * @param aViewer boolean - flags that viewer (not editor) will be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5MultiLookupPanelMpcLookupWrapper( MultiPaneComponentLookup<T> aMpc, boolean aViewer ) {
    super( aMpc.tsContext(), aMpc.model(), aViewer );
    source = aMpc;
    source.addTsDoubleClickListener( doubleClickEventHelper );
    source.addTsSelectionListener( selectionChangeEventHelper );
    source.genericChangeEventer().addListener( aSource -> fireChangeEvent() );
  }

  // ------------------------------------------------------------------------------------
  // M5AbstractCollectionPanel
  //

  @Override
  public T selectedItem() {
    return source.selectedItem();
  }

  @Override
  public void setSelectedItem( T aItem ) {
    source.setSelectedItem( aItem );
  }

  @Override
  public IList<T> items() {
    return source.items();
  }

  @Override
  public void refresh() {
    source.refresh();
  }

  @Override
  public ITsCheckSupport<T> checkSupport() {
    return source.tree().checks();
  }

  @Override
  public IM5ItemsProvider<T> itemsProvider() {
    return source.itemsProvider();
  }

  @Override
  public void setItemsProvider( IM5ItemsProvider<T> aItemsProvider ) {
    source.setItemProvider( aItemsProvider );
  }

  @Override
  public IM5LifecycleManager<T> lifecycleManager() {
    return null;
  }

  @Override
  public void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager ) {
    // lifecycle manager is not used for lookups panel
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return source.createControl( aParent );
  }

  // ------------------------------------------------------------------------------------
  // IM5MultiLookupPanel
  //

  @Override
  public void setItems( IList<T> aItems ) {
    source.setItems( aItems );
  }

  @Override
  public IM5LookupProvider<T> lookupProvider() {
    return source.lookupProvider();
  }

  @Override
  public void setLookupProvider( IM5LookupProvider<T> aLookupProvider ) {
    source.setLookupProvider( aLookupProvider );
  }

}
