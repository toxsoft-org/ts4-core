package org.toxsoft.core.tsgui.dialogs;

import static org.toxsoft.core.tsgui.dialogs.ITsGuiDialogResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.stdevents.*;
import org.toxsoft.core.tsgui.bricks.stdevents.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.utils.jface.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Simple dialog show items list and optionally allows select an item.
 * <p>
 * Item list and visualization provider is specified by user.
 *
 * @author hazard157
 */
public class DialogItemsList {

  /**
   * Dialog content panel.
   *
   * @author hazard157
   */
  static class ContentPanel
      extends AbstractTsDialogPanel<Object, Object>
      implements ITsSelectionProvider<Object> {

    /**
     * Content provider for {@link ListViewer}, simply returns items array.
     *
     * @author hazard157
     */
    class ListContentProvider
        extends StructuredContentProviderAdapter<Object> {

      @Override
      public Object[] getElements( Object aInputElement ) {
        return items.toArray();
      }

    }

    /**
     * Label provider for {@link ListViewer}, uses {@link ITsNameProvider}.
     *
     * @author hazard157
     */
    class ListLabelProvider
        extends LabelProvider {

      @Override
      public String getText( Object aElement ) {
        return nameProvider.getName( aElement );
      }
    }

    final TsSelectionChangeEventHelper<Object> selectionChangeEventHelper;
    final IListEdit<Object>                    items = new ElemLinkedBundleList<>();
    final ListViewer                           listViewer;
    final ITsNameProvider<Object>              nameProvider;

    ContentPanel( Composite aParent, TsDialog<Object, Object> aOwnerDialog, IList<Object> aItems,
        ITsNameProvider<Object> aNameProvider ) {
      super( aParent, aOwnerDialog );
      selectionChangeEventHelper = new TsSelectionChangeEventHelper<>( this );
      nameProvider = aNameProvider;
      setLayout( new BorderLayout() );
      items.setAll( aItems );
      listViewer = new ListViewer( this, SWT.SINGLE | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER );
      listViewer.getControl().setLayoutData( BorderLayout.CENTER );
      listViewer.setLabelProvider( new ListLabelProvider() );
      listViewer.setContentProvider( new ListContentProvider() );
      listViewer.setInput( items );
      listViewer.addSelectionChangedListener( notificationSelectionChangedListener );
      listViewer.addDoubleClickListener( event -> ownerDialog().closeDialog( ETsDialogCode.OK ) );
    }

    // ------------------------------------------------------------------------------------
    // AbstractTsDialogPanel
    //

    @Override
    protected void doSetDataRecord( Object aData ) {
      setSelectedItem( aData );
    }

    @Override
    protected Object doGetDataRecord() {
      return selectedItem();
    }

    @Override
    protected ValidationResult validateData() {
      if( selectedItem() == null ) {
        return ValidationResult.error( MSG_ERR_MUST_SELECT_ELEM_IN_LIST );
      }
      return ValidationResult.SUCCESS;
    }

    // ------------------------------------------------------------------------------------
    // ITsSelectionProvider
    //

    @Override
    public void addTsSelectionListener( ITsSelectionChangeListener<Object> aListener ) {
      selectionChangeEventHelper.addTsSelectionListener( aListener );
    }

    @Override
    public void removeTsSelectionListener( ITsSelectionChangeListener<Object> aListener ) {
      selectionChangeEventHelper.removeTsSelectionListener( aListener );
    }

    @Override
    public Object selectedItem() {
      IStructuredSelection selection = (IStructuredSelection)listViewer.getSelection();
      if( !selection.isEmpty() ) {
        return selection.getFirstElement();
      }
      return null;
    }

    @Override
    public void setSelectedItem( Object aItem ) {
      IStructuredSelection selection = null;
      if( aItem != null ) {
        selection = new StructuredSelection( aItem );
      }
      listViewer.setSelection( selection, true );
    }

  }

  // ------------------------------------------------------------------------------------
  // Dialog invocation
  //

  /**
   * Simply displays items dialog with single close button.
   *
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aItems IList&lt;T&gt; - list of items to display
   * @param aNameProvider {@link ITsNameProvider} - item visuals provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  public static void show( ITsDialogInfo aDialogInfo, final IList aItems, final ITsNameProvider aNameProvider ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aItems );
    IDialogPanelCreator<Object, Object> creator =
        ( aParent, aOwnerDialog ) -> new ContentPanel( aParent, aOwnerDialog, aItems, aNameProvider );
    TsDialog<Object, Object> d = new TsDialog<>( aDialogInfo, null, aNameProvider, creator );
    d.execDialog();
  }

  /**
   * Displays items selection dialog and returns the selected item.
   *
   * @param <T> - items type
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aItems IList&lt;T&gt; - list of items to display
   * @param aSel {@link Object} - initially selected item or <code>null</code>
   * @param aNameProvider {@link ITsNameProvider} - item visuals provider
   * @return &lt;T&gt; - user selected item or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public static <T> T select( ITsDialogInfo aDialogInfo, final IList<T> aItems, Object aSel,
      final ITsNameProvider aNameProvider ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aItems );
    IDialogPanelCreator<Object, Object> creator =
        ( aParent, aOwnerDialog ) -> new ContentPanel( aParent, aOwnerDialog, (IList)aItems, aNameProvider );
    TsDialog<Object, Object> d = new TsDialog<>( aDialogInfo, aSel, aNameProvider, creator );
    return (T)d.execData();
  }

  /**
   * Displays items selection dialog with default name provider and returns the selected item.
   *
   * @param <T> - items type
   * @param aDialogInfo {@link ITsDialogInfo} - dialog window parameters
   * @param aItems IList&lt;T&gt; - list of items to display
   * @param aSel {@link Object} - initially selected item or <code>null</code>
   * @return &lt;T&gt; - user selected item or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( { "unchecked", "rawtypes" } )
  public static <T> T select( ITsDialogInfo aDialogInfo, final IList<T> aItems, Object aSel ) {
    TsNullArgumentRtException.checkNulls( aDialogInfo, aItems );
    ITsNameProvider np = ITsNameProvider.DEFAULT;
    IDialogPanelCreator<Object, Object> creator =
        ( aParent, aOwnerDialog ) -> new ContentPanel( aParent, aOwnerDialog, (IList)aItems, np );
    TsDialog<Object, Object> d = new TsDialog<>( aDialogInfo, aSel, np, creator );
    return (T)d.execData();
  }

}
