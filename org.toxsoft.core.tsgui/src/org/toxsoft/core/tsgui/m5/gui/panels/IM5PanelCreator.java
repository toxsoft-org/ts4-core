package org.toxsoft.core.tsgui.m5.gui.panels;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.core.tsgui.utils.checkcoll.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Creates GUI panels for modeled entities view and editing.
 * <p>
 * THis interface has defalt implementation {@link M5DefaultPanelCreator}. Default implementation is used by the model
 * until
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5PanelCreator<T> {

  /**
   * Creates entity viewer panel.
   * <p>
   * The created panel can be used to view the properties of an existing entity..
   * <p>
   * Default implementation creates {@link M5DefaultEntityViewerPanel}.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @return {@link IM5EntityPanel} - new instantce of viewer panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityPanel<T> createEntityViewerPanel( ITsGuiContext aContext );

  /**
   * Creates entity editor panel.
   * <p>
   * The created panel can be used either for existing entities editing or new entities creation via lifecycle manager.
   * <p>
   * Default implementation creates {@link M5DefaultEntityEditorPanel}.
   * <p>
   * If leficyle manager is specified as <code>null</code>, it must be set to non-<code>null</code> value by
   * {@link IM5EntityPanel#setLifecycleManager(IM5LifecycleManager)} before actual use of created panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aLifecycleManager {@link IM5LifecycleManager} - manager to create/edit the entity, may be <code>null</code>
   * @return {@link IM5EntityPanel} - new instantce of editor panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityPanel<T> createEntityEditorPanel( ITsGuiContext aContext, IM5LifecycleManager<T> aLifecycleManager );

  /**
   * Creates entity details panel.
   * <p>
   * The created panel can be used to view the properties of an existing entity.
   * <p>
   * Default implementation creates {@link M5DefaultEntityViewerPanel}.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @return {@link IM5EntityPanel} - new instantce of details panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityPanel<T> createEntityDetailsPanel( ITsGuiContext aContext );

  /**
   * Creates entity editor panel.
   * <p>
   * The created panel can be used either for existing entities editing or new entities creation via lifecycle manager.
   * <p>
   * Default implementation creates {@link M5DefaultEntityControlledPanel}.
   * <p>
   * If lefecyle manager is specified as <code>null</code>, it must be set to non-<code>null</code> value by
   * {@link IM5EntityPanel#setLifecycleManager(IM5LifecycleManager)} before actual use of created panel.
   * <p>
   * This method creates {@link M5EntityPanelWithValeds} editor with the specified controller. If controller is
   * <code>null</code> the default controller will be used.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aLifecycleManager {@link IM5LifecycleManager} - manager to create/edit the entity, may be <code>null</code>
   * @param aController {@link M5EntityPanelWithValedsController} - the controller or <code>null</code> for default
   * @return {@link IM5EntityPanel} - new instantce of controlled editor panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityPanel<T> createEntityControlledPanel( ITsGuiContext aContext, IM5LifecycleManager<T> aLifecycleManager,
      M5EntityPanelWithValedsController<T> aController );

  /**
   * Creates the panel to enter common filtering criteria for the modeled entity.
   * <p>
   * Default implementation creates {@link M5DefaultEntityFilterPanel}.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @return {@link IM5FilterPanel} - new instance of the filter panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5FilterPanel<T> createFilterPanel( ITsGuiContext aContext );

  /**
   * Creates panel to view list of entities.
   * <p>
   * The create panel can be used to view the list of exsiting entities and to select one of them.
   * <p>
   * If specified provider is <code>null</code> it must be set to non-<code>null</code> value by
   * {@link IM5CollectionPanel#setItemsProvider(IM5ItemsProvider)} before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  IM5CollectionPanel<T> createCollViewerPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider );

  /**
   * Creates entities list edit panel: add, remove items to the list.
   * <p>
   * If specified <code>null</code> at creation time, both lifecycle manager and items provider must be set to
   * non-<code>null</code> values before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - manager to CRUD the entity, may be <code>null</code>
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  IM5CollectionPanel<T> createCollEditPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager );

  /**
   * Creates the several lookup items selection panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aLookupProvider {@link IM5LookupProvider} - the lookup items provider
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5MultiLookupPanel<T> createMultiLookupPanel( ITsGuiContext aContext, IM5LookupProvider<T> aLookupProvider );

  /**
   * Creates panel to select several items from the list of entities.
   * <p>
   * To get selected items check marks supplier {@link IM5CollectionPanel#checkSupport()}, namely with method
   * {@link ITsCheckSupport#listCheckedItems(boolean)}.
   * <p>
   * If specified provider is <code>null</code> it must be set to non-<code>null</code> value by
   * {@link IM5CollectionPanel#setItemsProvider(IM5ItemsProvider)} before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @return {@link IM5CollectionPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  IM5CollectionPanel<T> createCollChecksPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider );

}
