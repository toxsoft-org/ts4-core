package org.toxsoft.tsgui.m5_3.gui.mpc.impl;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.m5_3.gui.mpc.IMultiPaneComponent;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;

/**
 * Base intreface of data display panes of {@link IMultiPaneComponent}.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMpcPaneBase<T>
    extends ILazyControl<Control> {

  /**
   * Returns the owner MPC.
   *
   * @return {@link IMultiPaneComponent} - the owner MPC
   */
  IMultiPaneComponent<T> owner();

}
