package org.toxsoft.core.tsgui.m5.gui.panels.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5CollectionPanel} implementation that wraps over {@link MultiPaneComponentModown}.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public class M5CollectionPanelMpcModownWrapper<T>
    extends M5AbstractCollectionPanel<T> {

  private final MultiPaneComponentModown<T> source;

  /**
   * Constructor.
   *
   * @param aMpc {@link MultiPaneComponentModown} - the wrapped component
   * @param aViewer boolean - flags that viewer (not editor) will be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5CollectionPanelMpcModownWrapper( MultiPaneComponentModown<T> aMpc, boolean aViewer ) {
    super( aMpc.tsContext(), aMpc.model(), aViewer );
    source = aMpc;
    source.setEditable( isEditable() );
    source.addTsDoubleClickListener( doubleClickEventHelper );
    source.addTsSelectionListener( selectionChangeEventHelper );
    source.genericChangeEventer().addListener( aSource -> fireChangeEvent() );
  }

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
    return source.tree().items();
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
    return source.lifecycleManager();
  }

  @Override
  public void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager ) {
    source.setLifecycleManager( aLifecycleManager );
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    return source.createControl( aParent );
  }

  @Override
  protected void doEditableStateChanged() {
    source.setEditable( isEditable() );
  }

}
