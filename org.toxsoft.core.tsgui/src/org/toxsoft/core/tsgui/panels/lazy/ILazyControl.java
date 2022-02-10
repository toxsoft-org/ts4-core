package org.toxsoft.core.tsgui.panels.lazy;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.core.tslib.utils.errors.TsIllegalStateRtException;
import org.toxsoft.core.tslib.utils.errors.TsNullArgumentRtException;

/**
 * Arbitrary {@link Control} on demand creation interface.
 *
 * @author hazard157
 * @param <C> - create control class
 */
public interface ILazyControl<C extends Control> {

  /**
   * Creates the control.
   * <p>
   * This method may be called once during inmstance lifecycle.
   *
   * @param aParent {@link Composite} - parent composite
   * @return {@link Control} - created control, must not be <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalStateRtException control was already created
   */
  C createControl( Composite aParent );

  /**
   * Returns control create be {@link #createControl(Composite)}.
   * <p>
   * Returned control may be already disposed.
   *
   * @return &lt;C&gt; - the control or <code>null</code> if not yet created
   */
  C getControl();

}
