package org.toxsoft.tsgui.m5_3.gui.mpc.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Base class {@link IMpcPaneBase} implementation.
 *
 * @author hazard157
 * @param <T> - displayed M5-modelled entity type
 * @param <C> - SWT-widget type
 */
public abstract class AbstractMpcPane<T, C extends Control>
    implements IMpcPaneBase<T> {

  private final MultiPaneComponent<T> owner;

  private C control = null;

  /**
   * Constructor for subclasses.
   *
   * @param aOwner {@link MultiPaneComponent} - the owner component
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public AbstractMpcPane( MultiPaneComponent<T> aOwner ) {
    owner = TsNullArgumentRtException.checkNull( aOwner );
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  final public C createControl( Composite aParent ) {
    TsIllegalStateRtException.checkNoNull( control );
    control = doCreateControl( aParent );
    TsInternalErrorRtException.checkNull( control );
    return control;
  }

  @Override
  final public C getControl() {
    return control;
  }

  // ------------------------------------------------------------------------------------
  // IMpcPaneBase
  //

  @Override
  public MultiPaneComponent<T> owner() {
    return owner;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  protected abstract C doCreateControl( Composite aParent );

}
