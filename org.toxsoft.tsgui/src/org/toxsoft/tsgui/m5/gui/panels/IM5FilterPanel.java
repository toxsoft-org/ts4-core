package org.toxsoft.tsgui.m5.gui.panels;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.m5.model.IM5ModelRelated;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tslib.bricks.ctx.ITsContextable;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeEventCapable;
import org.toxsoft.tslib.bricks.filter.ITsFilter;

/**
 * Panel to set filter parameters to filter out the collection of the modelled entities.
 * <p>
 * It is assumed that this panel contains widgets for filter parameters but not the controls to turn filter on or off.
 * Common usage of this panel is either to visually decorate width filter on/off check button or use as filter setting
 * dialog content.
 * <p>
 * Notes: there is no API to <b>set</b> the content of panel programmaticaly.
 * <p>
 * When user changes parameters of filter {@link #genericChangeEventer()} fires the event.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5FilterPanel<T>
    extends IM5ModelRelated<T>, ILazyControl<Control>, ITsContextable, IGenericChangeEventCapable {

  /**
   * Returns the filter according to paramaters entered by the user in this panel.
   * <p>
   * May return <code>null</code> if no parameters were specified. Depending on the usage environment, <code>null</code>
   * may be interpreted either as {@link ITsFilter#ALL} ot {@link ITsFilter#NONE}.
   *
   * @return {@link ITsFilter}&lt;T&gt; - user-specified filter or <code>null</code>
   */
  ITsFilter<T> getFilter();

  /**
   * Resets content of the panel to the initial state.
   */
  void reset();

}
