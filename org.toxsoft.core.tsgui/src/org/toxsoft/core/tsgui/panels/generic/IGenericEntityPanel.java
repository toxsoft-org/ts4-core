package org.toxsoft.core.tsgui.panels.generic;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * Generci panel to display propertis of one entity.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericEntityPanel<T>
    extends ILazyControl<Control>, IGenericChangeEventCapable, ITsContextable {

  /**
   * Determines if panel is created as viewer or as editor with ability to edit it's content.
   * <p>
   * If {@link #isViewer()} = <code>true</code> then this panel can not be switched to editing mode.
   *
   * @return boolean - viewer mode flag
   */
  boolean isViewer();

  /**
   * Sets entity to be displayed in panel.
   *
   * @param aEntity &lt;T&gt; - the displayed entity, may be <code>null</code>
   */
  void setEntity( T aEntity );

}
