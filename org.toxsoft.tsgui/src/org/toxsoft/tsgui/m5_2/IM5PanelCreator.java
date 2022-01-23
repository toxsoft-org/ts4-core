package org.toxsoft.tsgui.m5_2;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.m5_2.gui.panels.IM5CollPanelBase;
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
   * The created panel can be used to view the properties of an existing entity.
   * <p>
   * Default panel has all fields except fields with {@link IM5Constants#M5FF_HIDDEN} hint.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @return {@link IM5EntityPanel} - new instantce of viewer panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityPanel<T> createEntityViewerPanel( ITsGuiContext aContext );

  /**
   * Creates entity editor panel.
   * <p>
   * The reated panel can be used either for existing entities editing or new entities creation via lifecycle manager.
   * <p>
   * Default panel has all fields expect fields with {@link IM5Constants#M5FF_HIDDEN} hint.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @return {@link IM5EntityPanel} - new instantce of editor panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5EntityPanel<T> createEntityEditorPanel( ITsGuiContext aContext );

  /**
   * Creates panel to view list of entities.
   * <p>
   * The created panel can be used to view the list of exsiting entities and optionally to select one or many items.
   *
   * @param aContext {@link ITsGuiContext} - panel creation context and parameters
   * @param aItemsProvider {@link IM5ItemsProvider} - the viewed items provider, may be <code>null</code>
   * @param aCheckSupport boolean - <code>true</code> = add multi-item selection ability with check marks
   * @return {@link IM5CollPanelBase} - new instance of the panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IM5CollPanelBase<T> createCollViewerPanel( ITsGuiContext aContext, IM5ItemsProvider<T> aItemsProvider,
      boolean aCheckSupport );

}
