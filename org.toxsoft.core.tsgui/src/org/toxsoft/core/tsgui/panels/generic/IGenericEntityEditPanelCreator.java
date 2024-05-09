package org.toxsoft.core.tsgui.panels.generic;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Creator of {@link IGenericEntityEditPanel}.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericEntityEditPanelCreator<T> {

  /**
   * Creates the panel.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aViewer boolean - viewer flag, sets {@link IGenericEntityEditPanel#isViewer()} value
   * @return {@link IGenericEntityEditPanel} - created panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IGenericEntityEditPanel<T> create( ITsGuiContext aContext, boolean aViewer );

}
