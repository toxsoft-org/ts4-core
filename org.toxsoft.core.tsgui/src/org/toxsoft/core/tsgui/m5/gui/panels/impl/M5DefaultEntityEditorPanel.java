package org.toxsoft.core.tsgui.m5.gui.panels.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5EntityPanel} default implementation to show specified entity field values.
 * <p>
 * This panel is the editor so {@link #isViewer()} = <code>false</code>.
 * <p>
 * This panel contains editors for fields without flag {@link IM5Constants#M5FF_HIDDEN}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5DefaultEntityEditorPanel<T>
    extends M5EntityPanelWithValeds<T> {

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @param aLifecycleManager {@link IM5LifecycleManager} - optional lifecycle manager, may be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5DefaultEntityEditorPanel( ITsGuiContext aContext, IM5Model<T> aModel,
      IM5LifecycleManager<T> aLifecycleManager ) {
    super( aContext, aModel, false );
    setLifecycleManager( aLifecycleManager );
  }

}
