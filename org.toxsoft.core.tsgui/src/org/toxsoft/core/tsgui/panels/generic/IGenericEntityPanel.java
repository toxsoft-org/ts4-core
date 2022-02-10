package org.toxsoft.core.tsgui.panels.generic;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextable;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeEventCapable;

/**
 * Generci panel to display propertis of one entity.
 *
 * @author hazard157
 * @param <T> - the entity type
 */
public interface IGenericEntityPanel<T>
    extends ILazyControl<Control>, IGenericChangeEventCapable, ITsContextable {

  /**
   * Sets entity to be displayed in panel.
   *
   * @param aEntity &lt;T&gt; - the displayed entity, may be <code>null</code>
   */
  void setEntity( T aEntity );

}
