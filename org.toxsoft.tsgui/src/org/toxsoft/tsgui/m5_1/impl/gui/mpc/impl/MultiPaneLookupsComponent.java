package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import static ru.toxsoft.tsgui.m5.gui.multipane.impl.ITsResources.*;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.dialogs.ETsDialogCode;
import org.toxsoft.tsgui.bricks.dialogs.TsDialogUtils;
import org.toxsoft.tsgui.m5.gui.viewers.IM5TreeViewer;
import org.toxsoft.tsgui.m5.gui.viewers.impl.M5TreeViewer;
import org.toxsoft.tsgui.m5_1.api.IM5ItemsProvider;
import org.toxsoft.tsgui.m5_1.api.IM5Model;
import org.toxsoft.tsgui.m5_1.api.helpers.IM5LookupProvider;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMultiPaneLookupsComponent;
import org.toxsoft.tsgui.m5_1.impl.gui.mpc.IMultiPaneModownComponent;
import org.toxsoft.tslib.utils.errors.TsUnsupportedFeatureRtException;

import ru.toxsoft.tsgui.dialogs.*;
import ru.toxsoft.tsgui.m5.gui.M5GuiUtils;
import ru.toxsoft.tsgui.m5.model.impl.M5DefaultItemsProvider;
import ru.toxsoft.tslib.utils.collections.basis.ITsReferenceCollection;

/**
 * Базовая реализация {@link IMultiPaneModownComponent}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MultiPaneLookupsComponent<T>
    extends BasicMultiPaneComponent<T>
    implements IMultiPaneLookupsComponent<T> {

  private IM5LookupProvider<T> lookupProvider = null;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   */
  public MultiPaneLookupsComponent( ITsGuiContext aContext, IM5Model<T> aModel ) {
    this( new M5TreeViewer<>( aContext, aModel ) );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aLookupProvider {@link IM5LookupProvider} - поставщик справочных сущнсотей, может быть <code>null</code>
   */
  public MultiPaneLookupsComponent( ITsGuiContext aContext, IM5Model<T> aModel, IM5LookupProvider<T> aLookupProvider ) {
    this( new M5TreeViewer<>( aContext, aModel ), aLookupProvider );
  }

  /**
   * Конструктор.
   *
   * @param aTreeViewer {@link IM5TreeViewer} - заранее созданный просмотрщик дерева
   */
  public MultiPaneLookupsComponent( IM5TreeViewer<T> aTreeViewer ) {
    this( aTreeViewer, null );
  }

  /**
   * Конструктор.
   *
   * @param aTreeViewer {@link IM5TreeViewer} - заранее созданный просмотрщик дерева
   * @param aLookupProvider {@link IM5LookupProvider} - поставщик справочных сущнсотей, может быть <code>null</code>
   */
  public MultiPaneLookupsComponent( IM5TreeViewer<T> aTreeViewer, IM5LookupProvider<T> aLookupProvider ) {
    super( aTreeViewer, new M5DefaultItemsProvider<>() );
    lookupProvider = aLookupProvider;
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса
  //

  @Override
  public M5DefaultItemsProvider<T> itemsProvider() {
    return (M5DefaultItemsProvider<T>)super.itemsProvider();
  }

  @Override
  public void setItemProvider( IM5ItemsProvider<T> aItemsProvider ) {
    throw new TsUnsupportedFeatureRtException();
  }

  // ------------------------------------------------------------------------------------
  // Реализация интерфейса IMultiPaneLookupsComponent
  //

  @Override
  public void setItems( ITsReferenceCollection<T> aItems ) {
    itemsProvider().items().setAll( aItems );
    refresh();
  }

  @Override
  public IM5LookupProvider<T> lookupProvider() {
    return lookupProvider;
  }

  @Override
  public void setLookupProvider( IM5LookupProvider<T> aLookupProvider ) {
    lookupProvider = aLookupProvider;
  }

  // ------------------------------------------------------------------------------------
  // Шаблонные методы
  //

  @Override
  protected boolean doGetIsAddAllowed( T aSel ) {
    return lookupProvider != null;
  }

  @Override
  protected boolean doGetIsEditAllowed( T aSel ) {
    return lookupProvider != null;
  }

  @Override
  protected boolean doGetIsRemoveAllowed( T aSel ) {
    return true;
  }

  @Override
  protected T doAddItem() {
    ICommonDialogInfo cdi = CommonDialogInfo.cdiSelectObjDialog( getShell() );
    T item = M5GuiUtils.askSelectLookupItem( tsContext().appContext(), model(), null, cdi, lookupProvider );
    if( item != null ) {
      itemsProvider().items().add( item );
    }
    return item;
  }

  @Override
  protected T doEditItem( T aItem ) {
    ICommonDialogInfo cdi = CommonDialogInfo.cdiSelectObjDialog( getShell() );
    T item = M5GuiUtils.askSelectLookupItem( tsContext().appContext(), model(), aItem, cdi, lookupProvider );
    if( item != null ) {
      int index = itemsProvider().items().indexOf( aItem );
      if( index >= 0 ) {
        itemsProvider().items().set( index, item );
      }
      else {
        itemsProvider().items().add( item );
      }
    }
    return item;
  }

  @Override
  protected boolean doRemoveItem( T aItem ) {
    if( TsDialogUtils.askYesNoCancel( getShell(), FMT_ASK_REMOVE_ITEM,
        model().getItemName( aItem ) ) != ETsDialogCode.YES ) {
      return false;
    }
    return itemsProvider().items().remove( aItem ) >= 0;
  }

}
