package org.toxsoft.tsgui.m5_3.gui.panels;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.m5_3.IM5Bunch;
import org.toxsoft.tsgui.m5_3.model.IM5ModelRelated;
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
 * Notes:
 * <ul>
 * <li>there is no API to <b>set</b> the content of panel programmaticaly;</li>
 * <li>created filter does not accept an entities of the modelled type, but a bunch of values {@link IM5Bunch};</li>
 * <li>zzz.</li>
 * </ul>
 * <p>
 * When user changes paraneters of filter {@link #genericChangeEventer()} fires the event.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5FilterPanel<T>
    extends IM5ModelRelated<T>, ILazyControl<Control>, ITsContextable, IGenericChangeEventCapable {

  /**
   * Returns the filter according to paramaters entered by the user in this panel.
   * <p>
   * May return <code>null</code> if no parameters were specified. Depending on theusage environment, <code>null</code>
   * may be interpreted either as {@link ITsFilter#ALL} ot {@link ITsFilter#NONE}.
   *
   * @return {@link ITsFilter}&lt;{@link IM5Bunch}&lt;T&gt;&gt; - user-specified filter or <code>null</code>
   */
  ITsFilter<IM5Bunch<T>> getFilter();

}
