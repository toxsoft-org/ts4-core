package org.toxsoft.tsgui.valed.controls.basic;

import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.tsgui.valed.controls.basic.ITsResources.*;
import static org.toxsoft.tslib.av.impl.AvUtils.*;
import static org.toxsoft.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextRefDef;
import org.toxsoft.tsgui.bricks.ctx.impl.TsGuiContextRefDef;
import org.toxsoft.tsgui.utils.ITsVisualsProvider;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControl;
import org.toxsoft.tsgui.valed.impl.AbstractValedControlFactory;
import org.toxsoft.tslib.coll.IList;
import org.toxsoft.tslib.coll.IListEdit;
import org.toxsoft.tslib.coll.impl.ElemArrayList;
import org.toxsoft.tslib.utils.ITsItemsProvider;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Выпадающий список (ComboBox) выбора эелемента из заданного списка.
 *
 * @author goga
 * @param <V> - конкретный тип (класс) элементов списка выбора
 */
public class ValedComboSelector<V>
    extends AbstractValedControl<V, Combo> {

  /**
   * ID of context reference {@link #REFDEF_ITEMS_PROVIDER}.
   */
  public static final String REFID_ITEMS_PROVIDER = VALED_REFID_PREFIX + ".ComboSelector.ItemsProvider"; //$NON-NLS-1$

  /**
   * The context reference to the items provider {@link ITsItemsProvider} for drop down list.<br>
   * Reference type: {@link ITsItemsProvider}<br>
   * Usage: items provider must return items of edited value type. Items are listed once at valed creation.<br>
   * Default value: none
   */
  @SuppressWarnings( "rawtypes" )
  public static final ITsGuiContextRefDef<ITsItemsProvider> REFDEF_ITEMS_PROVIDER = //
      TsGuiContextRefDef.create( REFID_ITEMS_PROVIDER, ITsItemsProvider.class, //
          TSID_NAME, STR_N_ITEMS_PROVIDER, //
          TSID_DESCRIPTION, STR_D_ITEMS_PROVIDER, //
          TSID_IS_MANDATORY, AV_FALSE //
      );

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ComboSelector"; //$NON-NLS-1$

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<?> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<?, Combo> e = new ValedComboSelector<>( aContext );
      e.setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
      e.setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
      return e;
    }

  }

  private final IListEdit<V> items = new ElemArrayList<>();

  private ITsVisualsProvider<V> visualsProvider = ITsVisualsProvider.DEFAULT;
  private ITsItemsProvider<V>   itemsProvider   = ITsItemsProvider.EMPTY;

  private Combo combo;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedComboSelector( ITsGuiContext aContext ) {
    super( aContext );
    visualsProvider = REFDEF_VALUE_VISUALS_PROVIDER.getRef( aContext, ITsVisualsProvider.DEFAULT );
    itemsProvider = REFDEF_ITEMS_PROVIDER.getRef( aContext, ITsItemsProvider.EMPTY );
    items.setAll( itemsProvider.listItems() );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @param aItems {@link IList}&lt;V&gt; - элементы списка выбора
   * @param aNameProvider {@link ITsVisualsProvider} - поставщик названий элементов списка выбора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedComboSelector( ITsGuiContext aContext, IList<V> aItems, ITsVisualsProvider<V> aNameProvider ) {
    super( aContext );
    TsNullArgumentRtException.checkNulls( aItems, aNameProvider );
    visualsProvider = aNameProvider;
    items.setAll( aItems );
  }

  // ------------------------------------------------------------------------------------
  // Внутренние методы
  //

  void initComboItems() {
    if( combo != null ) {
      combo.removeAll();
      for( V v : items ) {
        String s = visualsProvider.getName( v );
        combo.add( s );
      }
      combo.select( items.isEmpty() ? -1 : 0 );
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedControl
  //

  @Override
  protected Combo doCreateControl( Composite aParent ) {
    combo = new Combo( aParent, SWT.DROP_DOWN | SWT.READ_ONLY );
    combo.addSelectionListener( notificationSelectionListener );
    initComboItems();
    return combo;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( combo != null ) {
      combo.setEnabled( aEditable );
    }
  }

  @Override
  protected V doGetUnvalidatedValue() {
    int selIndex = combo.getSelectionIndex();
    if( selIndex < 0 ) {
      return null;
    }
    return items().get( selIndex );
  }

  @Override
  protected void doSetUnvalidatedValue( V aValue ) {
    int index = -1;
    if( aValue != null ) {
      index = items().indexOf( aValue );
    }
    if( index < 0 ) {
      if( !items.isEmpty() ) {
        index = 0;
      }
    }
    combo.select( index );
  }

  @Override
  protected void doClearValue() {
    combo.select( -1 );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns the selected item.
   *
   * @return &lt;V&gt; - selected item or <code>null</code>
   */
  public V selectedItem() {
    if( combo != null ) {
      int index = combo.getSelectionIndex();
      if( index >= 0 ) {
        return items.get( index );
      }
    }
    return null;
  }

  /**
   * Selects specified item.
   * <p>
   * If argument is not in {@link #items()} list, <code>null</code> argument will be assumed.
   *
   * @param aItem &lt;V&gt; - item to select or <code>null</code>
   */
  public void setSelectedItem( V aItem ) {
    int index = -1;
    if( aItem != null ) {
      index = items.indexOf( aItem );
    }
    if( combo != null ) {
      combo.select( index );
    }
  }

  /**
   * Returns the items in drop-down list.
   *
   * @return {@link IList}&lt;V&gt; - items list
   */
  public IList<V> items() {
    return items;
  }

  /**
   * Sets the items in drop-down list.
   *
   * @param aItems {@link IList}&lt;V&gt; - items list, may be empty
   */
  public void setItems( IList<V> aItems ) {
    items.setAll( aItems );
    initComboItems();
  }

  /**
   * Returns the visuals provider {@link #items()}.
   *
   * @return {@link ITsVisualsProvider}&lt;V&gt; - visuals provider
   */
  public ITsVisualsProvider<V> visualsProvider() {
    return visualsProvider;
  }

  /**
   * Sets the visuals provider {@link #items()}.
   *
   * @param aVisualsProvider {@link ITsVisualsProvider}&lt;V&gt; - visuals provider
   */
  public void setVisualsProvider( ITsVisualsProvider<V> aVisualsProvider ) {
    TsNullArgumentRtException.checkNull( aVisualsProvider );
    visualsProvider = aVisualsProvider;
    if( combo != null ) {
      int selIndex = combo.getSelectionIndex();
      initComboItems();
      combo.select( selIndex );
    }
  }

}
