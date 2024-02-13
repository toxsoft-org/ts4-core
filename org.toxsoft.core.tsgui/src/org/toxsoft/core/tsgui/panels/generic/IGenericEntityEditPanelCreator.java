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
   * Creates to panel.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IGenericEntityEditPanel} - created panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IGenericEntityEditPanel<T> create( ITsGuiContext aContext );

}
