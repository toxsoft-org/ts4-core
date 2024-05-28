package org.toxsoft.core.tsgui.m5.gui.panels;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;

/**
 * Panel to set filter parameters to filter out the collection of the modeled entities.
 * <p>
 * It is assumed that this panel contains widgets for filter parameters but not the controls to turn filter on or off.
 * Common usage of this panel is either to visually decorate width filter on/off check button or use as filter setting
 * dialog content.
 * <p>
 * Notes: there is no API to <b>set</b> the content of panel programmatically.
 * <p>
 * When user changes parameters of filter {@link #genericChangeEventer()} fires the event.
 *
 * @author hazard157
 * @param <T> - modeled entity type
 */
public interface IM5FilterPanel<T>
    extends IM5ModelRelated<T>, ILazyControl<Control>, ITsContextable, IGenericChangeEventCapable {

  /**
   * Returns the filter according to parameters entered by the user in this panel.
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

  /**
   * Determines if panel has built-in filter on/off and reset controls.
   * <p>
   * FIXME filter controls<br>
   * Used in panel wrappers. If this panel does noh has controls, wrapper will add filter On/Off switch and reset
   * buttons to the panel.
   *
   * @return boolean - panel has filter controls
   */
  // boolean hasFilterControls();

}
