package org.toxsoft.tsgui.m5.gui.panels;

import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.m5.model.IM5ModelRelated;
import org.toxsoft.tsgui.panels.lazy.ILazyControl;
import org.toxsoft.tslib.bricks.ctx.ITsContextable;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeEventCapable;

/**
 * Base interface of all panels to view and/or edit entities of one model.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5PanelBase<T>
    extends IM5ModelRelated<T>, ILazyControl<Control>, ITsContextable, IGenericChangeEventCapable {

  /**
   * Determines if panel is created as viewer or as editor with ability to edit it's content.
   * <p>
   * If {@link #isViewer()} = <code>true</code> then this panel can not be switched to editing mode,
   * {@link #setEditable(boolean) setEditable(<b>true</b>)} will be ignored and {@link #isEditable()} will always return
   * <code>false</code>.
   *
   * @return boolean - viewer mode flag
   */
  boolean isViewer();

  /**
   * Determines if panel content editing is allowed right now.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> always returns <code>false</code>.
   *
   * @return boolean - edit mode flag
   */
  boolean isEditable();

  /**
   * Toggles panel content edit mode.
   * <p>
   * For viewers {@link #isViewer()} = <code>true</code> this method does nothing.
   *
   * @param aEditable boolean - edit mode flag
   */
  void setEditable( boolean aEditable );

}
