package org.toxsoft.tsgui.m5_1.api;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_1.IM5Constants;
import org.toxsoft.tsgui.m5_1.gui.*;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Creates GUI panels for modelled entities view and editing.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5PanelCreator<T> {

  /**
   * Creates entity viewer panel.
   * <p>
   * The created panel can be used to view the properties of an existing entity..
   * <p>
   * Default panel has all fields expect fields with {@link IM5Constants#M5FF_HIDDEN} hint.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @return {@link IM5EntityPanel} - new instantce ov viewer panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityPanel<T> createEntityViewerPanel( ITsGuiContext aContext );

  /**
   * Creates entity editor panel.
   * <p>
   * The reated panel can be used either for existing entities editing or new entities creation via lifecycle manager.
   * <p>
   * Default panel has all fields expect fields with {@link IM5Constants#M5FF_HIDDEN} hint.
   * <p>
   * If leficyle manager is specified as <code>null</code>, it must be set to non-<code>null</code> value by
   * {@link IM5EntityEditPanel#setLifecycleManager(IM5LifecycleManager)} before actual use of created panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aLifecycleManager {@link IM5LifecycleManager} - manager to create/edit the entity, may be <code>null</code>
   * @return {@link IM5EntityPanel} - new instantce ov viewer panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityEditPanel<T> createEntityEditorPanel( ITsGuiContext aContext, IM5LifecycleManager<T> aLifecycleManager );

  /**
   * Creates panel to view list of entities.
   * <p>
   * The create panel can be used to view the list of exsiting entities and to select one of them.
   * <p>
   * If specified provider is <code>null</code> it must be set to non-<code>null</code> value by
   * {@link IM5CollViewerPanel#setItemsProvider(IM5ItemsProvider)} before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @return {@link IM5CollViewerPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  IM5CollViewerPanel<T> createCollViewerPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider );

  /**
   * Creates entities list edit panel: add, remove items to the list.
   * <p>
   * If specified <code>null</code> at creation time, both lifecycle manager and items provider must be set to
   * non-<code>null</code> values before actual use of the panel.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @param aLifecycleManager {@link IM5LifecycleManager} - manager to CRUD the entity, may be <code>null</code>
   * @return {@link IM5CollEditPanel} - new instance of the panel
   * @throws TsNullArgumentRtException aContext = <code>null</code>
   */
  IM5CollEditPanel<T> createCollEditPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      IM5LifecycleManager<T> aLifecycleManager );

}
