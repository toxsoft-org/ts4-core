package org.toxsoft.core.tsgui.m5.gui.panels;

import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.generic.*;

/**
 * The entities list viewer/editor panel.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5CollectionPanel<T>
    extends IGenericCollPanel<T>, IM5PanelBase<T> {

  /**
   * Returns the items provider.
   * <p>
   * By default, when provider not set, returns {@link IM5ItemsProvider#EMPTY}.
   *
   * @return {@link IM5ItemsProvider} - the items provider never is <code>null</code>
   */
  IM5ItemsProvider<T> itemsProvider();

  /**
   * Sets the items provider.
   * <p>
   * Note: changing items provider does not leads to the tree update - {@link #refresh()} must be called.
   *
   * @param aItemsProvider {@link IM5ItemsProvider} - the items provider or <code>null</code> for empty provider
   */
  void setItemsProvider( IM5ItemsProvider<T> aItemsProvider );

  /**
   * Returns the lifecycle manager.
   *
   * @return {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  IM5LifecycleManager<T> lifecycleManager();

  /**
   * Sets the lifecycle manager.
   * <p>
   * Notes:
   * <ul>
   * <li>changing lifecycle manager does not leads to the tree update - {@link #refresh()} must be called;</li>
   * <li>panel does <b>not</b> uses items provider of lifecycle manager {@link IM5LifecycleManager#itemsProvider()}.
   * Items provider must be set separately via {@link #setItemsProvider(IM5ItemsProvider)}.</li>
   * </ul>
   *
   * @param aLifecycleManager {@link IM5LifecycleManager} - the lifecycle manager or <code>null</code>
   */
  void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager );

}
