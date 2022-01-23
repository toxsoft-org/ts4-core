package org.toxsoft.tsgui.m5.gui.panels.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.m5.gui.mpc.impl.MultiPaneComponentModown;
import org.toxsoft.tsgui.m5.gui.panels.IM5CollectionPanel;
import org.toxsoft.tsgui.m5.model.IM5ItemsProvider;
import org.toxsoft.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.tsgui.utils.checkcoll.ITsCheckSupport;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * {@link IM5CollectionPanel} implementation that wraps over {@link MultiPaneComponentModown}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
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

}
