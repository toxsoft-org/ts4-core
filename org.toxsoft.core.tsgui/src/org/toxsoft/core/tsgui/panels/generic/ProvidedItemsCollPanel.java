package org.toxsoft.core.tsgui.panels.generic;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IGenericCollPanel} implementation as a simple list with provided items and visualization.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public class ProvidedItemsCollPanel<T>
    extends AbstractGenericCollPanel<T> {

  private ITsItemsProvider<T>   itemsProvider   = ITsItemsProvider.EMPTY;
  private ITsVisualsProvider<T> visualsProvider = ITsVisualsProvider.DEFAULT;

  private ListViewer listViewer = null;

  /**
   * Constructor.
   * <p>
   * Constructor stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aIsViewer boolean - the viewer (read-only) panel flag
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ProvidedItemsCollPanel( ITsGuiContext aContext, boolean aIsViewer ) {
    super( aContext, aIsViewer );
  }

  // ------------------------------------------------------------------------------------
  // AbstractGenericCollPanel
  //

  @SuppressWarnings( "unchecked" )
  @Override
  protected Control doCreateControl( Composite aParent ) {
    listViewer = new ListViewer( aParent );
    listViewer.setContentProvider( (IStructuredContentProvider)aInput -> itemsProvider.listItems().toArray() );
    listViewer.setLabelProvider( new LabelProvider() {

      @Override
      public String getText( Object aElement ) {
        return visualsProvider.getName( (T)aElement );
      }
    } );
    listViewer.setInput( itemsProvider );
    listViewer
        .addSelectionChangedListener( aEvent -> selectionChangeEventHelper.fireTsSelectionEvent( selectedItem() ) );
    listViewer.addDoubleClickListener( aEvent -> doubleClickEventHelper.fireTsDoublcClickEvent( selectedItem() ) );
    return listViewer.getControl();
  }

  @SuppressWarnings( "unchecked" )
  @Override
  public T selectedItem() {
    IStructuredSelection selection = listViewer.getStructuredSelection();
    T sel = null;
    if( selection != null && !selection.isEmpty() ) {
      sel = (T)selection.getFirstElement();
    }
    return sel;
  }

  @Override
  public void setSelectedItem( T aItem ) {
    ISelection selection = null;
    if( aItem != null ) {
      selection = new StructuredSelection( aItem );
    }
    listViewer.setSelection( selection, true );
  }

  @Override
  public IList<T> items() {
    return itemsProvider.listItems();
  }

  @Override
  public void refresh() {
    T sel = selectedItem();
    listViewer.refresh();
    setSelectedItem( sel );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the provider of the {@link #items()} list.
   * <p>
   * Initial value is {@link ITsItemsProvider#EMPTY}.
   *
   * @return {@link ITsItemsProvider}&lt;T&gt; - the items provider
   */
  public ITsItemsProvider<T> getItemsProvider() {
    return itemsProvider;
  }

  /**
   * Sets the items provider {@link #getItemsProvider()}.
   * <p>
   * Refreshes the widget.
   *
   * @param aItemsProvider {@link ITsItemsProvider}&lt;T&gt; - the items provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setItemsProvider( ITsItemsProvider<T> aItemsProvider ) {
    TsNullArgumentRtException.checkNull( aItemsProvider );
    itemsProvider = aItemsProvider;
    refresh();
  }

  /**
   * Returns the provider of the displayed text.
   * <p>
   * Initial value is {@link ITsVisualsProvider#DEFAULT}.
   *
   * @return {@link ITsVisualsProvider}&lt;T&gt; - the visuals provider
   */
  public ITsVisualsProvider<T> getVisualsProvider() {
    return visualsProvider;
  }

  /**
   * Sets the visuals provider {@link #getVisualsProvider()}.
   * <p>
   * Refreshes the widget.
   *
   * @param aVisualsProvider {@link ITsVisualsProvider}&lt;T&gt; - the visuals provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setVisualsProvider( ITsVisualsProvider<T> aVisualsProvider ) {
    TsNullArgumentRtException.checkNull( aVisualsProvider );
    visualsProvider = aVisualsProvider;
    refresh();
  }

}
