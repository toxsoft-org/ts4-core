package org.toxsoft.tsgui.m5_3.gui.panels;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_3.gui.panels.impl.*;
import org.toxsoft.tsgui.m5_3.model.IM5LifecycleManager;
import org.toxsoft.tsgui.m5_3.model.impl.M5DefaultPanelCreator;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Creates GUI panels for modelled entities view and editing.
 * <p>
 * THis interface has defalt implementation {@link M5DefaultPanelCreator}. Default implementation is used by the model
 * until
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

}
