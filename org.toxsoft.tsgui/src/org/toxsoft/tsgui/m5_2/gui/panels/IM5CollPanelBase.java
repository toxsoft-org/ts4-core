package org.toxsoft.tsgui.m5_2.gui.panels;

import org.toxsoft.tsgui.m5.gui.panels.IM5PanelBase;
import org.toxsoft.tsgui.panels.generic.IGenericCollPanel;
import org.toxsoft.tsgui.utils.checkcoll.ITsCheckSupport;
import org.toxsoft.tslib.coll.IList;

/**
 * Base interface of all M5-modelled entities collection view / edit panels.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5CollPanelBase<T>
    extends IGenericCollPanel<T>, IM5PanelBase<T> {

  @Override
  IList<T> items();

  @Override
  void refresh();

  @Override
  ITsCheckSupport<T> checkSupport();

  // /**
  // * Returns the displayed items provider.
  // * <p>
  // * The {@link #items()} are elemenets retreived from item provider. However {@link #items()} are stored in panel
  // while
  // * provider gets elements from external source. Panel uses cached {@link #items()} until {@link #refresh()} is
  // called
  // * either by client or by the implementation of panel.
  // *
  // * @return {@link IM5ItemsProvider} - displayed items provider, never is <code>null</code>
  // */
  // IM5ItemsProvider<T> itemsProvider();
  //
  // /**
  // * Sets items provider.
  // *
  // * @param aItemsProvider {@link IM5ItemsProvider} - the items provider
  // * @throws TsNullArgumentRtException any argument = <code>null</code>
  // */
  // void setItemsProvider( IM5ItemsProvider<T> aItemsProvider );

}
