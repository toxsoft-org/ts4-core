package org.toxsoft.tsgui.m5_1.impl.gui.mpc.impl;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5.gui.viewers.impl.M5TreeCheckableViewer;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.impl.gui.viewers.IM5TreeCheckableViewer;
import org.toxsoft.tslib.coll.IList;

/**
 * Расширение {@link MultiPaneModownComponent} средствами пометки элементов.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public class MultiPaneModownCheckableComponent<T>
    extends MultiPaneModownComponent<T> {

  private final IM5TreeCheckableViewer<T> checkableViewer;

  /**
   * Конструктор.
   *
   * @param aTreeViewer {@link IM5TreeCheckableViewer} - заранее созданный просмотрщик дерева с пометками
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  public MultiPaneModownCheckableComponent( IM5TreeCheckableViewer<T> aTreeViewer, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager ) {
    super( aTreeViewer, aItemsProvider, aLifecycleManager );
    checkableViewer = aTreeViewer;
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст
   * @param aModel {@link IM5Model} - модель сущностей
   * @param aItemsProvider {@link IM5ItemsProvider} - поставщик элементов, может быть <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - менеджер ЖЦ, может быть <code>null</code>
   */
  public MultiPaneModownCheckableComponent( ITsGuiContext aContext, IM5Model<T> aModel,
      IM5ItemsProvider<T> aItemsProvider, IM5LifecycleManager<T> aLifecycleManager ) {
    this( new M5TreeCheckableViewer<>( aContext, aModel ), aItemsProvider, aLifecycleManager );
  }

  // ------------------------------------------------------------------------------------
  // Реализация MultiPaneModownComponent
  //

  @Override
  protected void doCheckAll() {
    if( !tree().items().isEmpty() ) {
      T sel = selectedItem();
      try {
        checkableViewer.checks().pauseFiring();
        IList<T> filtered = tree().filterManager().items();
        for( T item : tree().items() ) {
          checkableViewer().checks().setItemCheckState( item, filtered.hasElem( item ) );
        }
        setSelectedItem( sel == null ? filtered.first() : sel );
      }
      finally {
        checkableViewer.checks().resumeFiring( true );
      }
    }
  }

  @Override
  protected void doUnCheckAll() {
    if( !tree().items().isEmpty() ) {
      T sel = selectedItem();
      try {
        checkableViewer().checks().setAllItemsCheckState( false );
        setSelectedItem( sel == null ? tree().items().first() : sel );
      }
      finally {
        checkableViewer.checks().resumeFiring( true );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Возвращает низлежащее дерево с пометками.
   *
   * @return {@link IM5TreeCheckableViewer} - низлежащее дерево с пометками
   */
  public IM5TreeCheckableViewer<T> checkableViewer() {
    return checkableViewer;
  }

}
