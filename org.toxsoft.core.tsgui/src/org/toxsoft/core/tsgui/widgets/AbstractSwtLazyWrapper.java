package org.toxsoft.core.tsgui.widgets;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.panels.lazy.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The wrapper over SWT {@link Control} to make it {@link ILazyControl}.
 *
 * @author hazard157
 * @param <C> - type of the SWT control
 */
public abstract class AbstractSwtLazyWrapper<C extends Control>
    implements ILazyControl<C> {

  private C control = null;

  /**
   * Constructor.
   */
  public AbstractSwtLazyWrapper() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  public C createControl( Composite aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    TsIllegalStateRtException.checkNoNull( control );
    C c = doCreateControl( aParent );
    TsInternalErrorRtException.checkNull( c );
    control = c;
    return c;
  }

  @Override
  public C getControl() {
    return control;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must create the SWT control.
   *
   * @param aParent {@link Composite} - the parent composite
   * @return &lt;C&gt; - create control, must not be <code>null</code>
   */
  protected abstract C doCreateControl( Composite aParent );

}
