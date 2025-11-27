package org.toxsoft.core.tsgui.m5.gui.mpc.impl;

import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.filter.*;

/**
 * Filter pane.
 *
 * @author hazard157
 * @param <T> - displayed M5-modeled entity type
 */
public interface IMpcFilterPane<T>
    extends IMpcPaneBase<T>, IGenericChangeEventCapable {

  /**
   * Determines if filtering is turned on.
   * <p>
   * Usually pane contains check-box to turn on/off the filtering. However some implementations may not the ability to
   * change filtering flag. For such implementations method always returns <code>true</code> and
   * {@link #setFilterOn(boolean)} does not have any effect.
   *
   * @return boolean - a flag that filtering is on
   */
  boolean isFilterOn();

  /**
   * Turn on/off the filtering if implementation allows it.
   *
   * @param aOn boolean - a flag that filtering is on
   */
  void setFilterOn( boolean aOn );

  /**
   * Returns the filter according to parameters entered by the user in this panel.
   *
   * @return {@link ITsFilter}&lt;T&gt; - user-specified filter, never is <code>null</code>
   */
  ITsFilter<T> getFilter();

}
