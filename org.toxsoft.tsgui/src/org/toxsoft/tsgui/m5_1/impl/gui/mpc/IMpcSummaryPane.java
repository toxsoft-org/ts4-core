package org.toxsoft.tsgui.m5_1.impl.gui.mpc;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tslib.coll.IList;

/**
 * MPC summary pane.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 */
public interface IMpcSummaryPane<T>
    extends ILazyControl<Control> {

  /**
   * Implementation must update summary pane content.
   * <p>
   * This method is called every time when items list or filter conditions or content of any item changes.
   *
   * @param aAllItems {@link IList}&lt;T&gt; - list of all items
   * @param aFilteredItems {@link IList}&lt;T&gt; - list of shown (filtered) items
   */
  void updatePane( IList<T> aAllItems, IList<T> aFilteredItems );

}
