package org.toxsoft.tsgui.m5.gui.mpc.impl;

import org.toxsoft.tslib.bricks.events.change.IGenericChangeEventCapable;
import org.toxsoft.tslib.bricks.filter.ITsFilter;

/**
 * Filter pane.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMpcFilterPane<T>
    extends IMpcPaneBase<T>, IGenericChangeEventCapable {

  /**
   * Determines if filtering is turned on.
   * <p>
   * Usually ane contains checkbox to turn on/off the filtering. However some implementations may not the ability to
   * change filtering flag. For such implementations method always returns <code>true</code> and
   * {@link #setFilterOn(boolean)} does not have any effect.
   *
   * @return boolean - a flag that filtering is on
   */
  boolean isFilterOn();

  /**
   * Turnw on/off the filtering if implementation allows it.
   *
   * @param aOn boolean - a flag that filtering is on
   */
  void setFilterOn( boolean aOn );

  /**
   * Returns the filter according to paramaters entered by the user in this panel.
   *
   * @return {@link ITsFilter}&lt;T&gt; - user-specified filter, never is <code>null</code>
   */
  ITsFilter<T> getFilter();

}
