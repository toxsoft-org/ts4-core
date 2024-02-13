package org.toxsoft.core.tsgui.panels.generic;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Creator of {@link IGenericEntityPanel}.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericEntityPanelCreator<T> {

  /**
   * Creates to panel.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @return {@link IGenericEntityPanel} - created panel
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  IGenericEntityPanel<T> create( ITsGuiContext aContext );

}
