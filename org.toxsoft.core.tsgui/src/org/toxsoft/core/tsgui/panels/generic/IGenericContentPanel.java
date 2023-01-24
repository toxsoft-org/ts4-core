package org.toxsoft.core.tsgui.panels.generic;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

/**
 * Generci panel to display (and optionally edit) some information (content).
 *
 * @author hazard157
 */
public interface IGenericContentPanel
    extends ILazyControl<Control>, IGenericChangeEventCapable, ITsContextable {

  /**
   * Determines if panel is created as viewer or as editor with ability to edit it's content.
   * <p>
   * If {@link #isViewer()} = <code>true</code> then this panel can not be switched to editing mode.
   *
   * @return boolean - viewer flag
   */
  boolean isViewer();

}
