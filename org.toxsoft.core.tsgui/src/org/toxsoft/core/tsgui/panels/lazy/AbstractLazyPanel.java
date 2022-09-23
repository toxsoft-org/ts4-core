package org.toxsoft.core.tsgui.panels.lazy;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link ILazyControl} base implementation.
 *
 * @author hazard157
 * @param <C> - type of the impementin SWT control
 */
public abstract class AbstractLazyPanel<C extends Control>
    implements ILazyControl<C>, ITsGuiContextable {

  /**
   * Context parameters and references change listener.
   */
  final ITsContextListener contextListener = new ITsContextListener() {

    @Override
    public <CTX extends ITsContextRo> void onContextRefChanged( CTX aSource, String aName, Object aRef ) {
      AbstractLazyPanel.this.onContextRefChanged( aName, aRef );
    }

    @Override
    public <CTX extends ITsContextRo> void onContextOpChanged( CTX aSource, String aId, IAtomicValue aValue ) {
      AbstractLazyPanel.this.onContextOpChanged( aId, aValue );
    }

  };

  private final ITsGuiContext tsContext;

  private C control = null;

  /**
   * Constructor.
   * <p>
   * Constructos stores reference to the context, does not creates copy.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException аргумент = null
   */
  public AbstractLazyPanel( ITsGuiContext aContext ) {
    TsNullArgumentRtException.checkNull( aContext );
    tsContext = aContext;
    tsContext.addContextListener( contextListener );
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  final public C createControl( Composite aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    TsIllegalStateRtException.checkNoNull( control );
    C c = doCreateControl( aParent );
    if( control != null && c != control ) {
      throw new TsInternalErrorRtException();
    }
    control = c;
    control.addDisposeListener( e -> whenDisposed() );
    return control;
  }

  private void whenDisposed() {
    tsContext().removeContextListener( contextListener );
    doDispose();
  }

  @Override
  final public C getControl() {
    return control;
  }

  // ------------------------------------------------------------------------------------
  // ITsContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Sets the control that will be returned by {@link #getControl()}.
   * <p>
   * Motivation: Allows a subclass to set the return value of the {@link #getControl ()} method in
   * {@link #doCreateControl (Composite)}. You might need this to call methods from {@link #doCreateControl (Composite)}
   * that expect to receive a reference to the control {@link #getControl ()}.
   * <p>
   * Method may be called multiple time but with the same argument with non-<code>null</code> argument. If
   * {@link #doCreateControl(Composite)} returns reference other than argument aControl an exception will be thrown by
   * {@link #createControl(Composite)}.
   *
   * @param aControl &lt;C&gt; - created control
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException контроль {@link #getControl()} уже был задан
   */
  public void setControl( C aControl ) {
    TsNullArgumentRtException.checkNull( aControl );
    if( control != null ) {
      TsIllegalStateRtException.checkTrue( control != aControl );
    }
    control = aControl;
  }

  /**
   * Determines that widget (control) is accessible.
   * <p>
   * Control is accessible if it was created and not yet disposed.
   *
   * @return boolean - <code>true</code> if control exists and is not disposed
   */
  public boolean isControlValid() {
    return control != null && !control.isDisposed();
  }

  /**
   * If widget is not accessible throws an exception.
   *
   * @throws TsIllegalArgumentRtException если {@link #isControlValid()} = <code>false</code>
   */
  public void checkLazyControl() {
    TsIllegalStateRtException.checkFalse( isControlValid() );
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Implementation must create SWT widget.
   * <p>
   * Method is called once from {@link #createControl(Composite)}.
   *
   * @param aParent {@link Composite} - parent composite
   * @return &lt;C&gt; - created composite, must not be <code>null</code>
   */
  abstract protected C doCreateControl( Composite aParent );

  /**
   * Subclasses may perform additional operations on SWT control dispose.
   */
  protected void doDispose() {
    // nop
  }

  /**
   * Informs about context option value change.
   *
   * @param aId String - changed option ID or <code>null</code> on batch changes
   * @param aValue {@link IAtomicValue} - the value of the changed option or <code>null</code> on batch changes
   */
  protected void onContextOpChanged( String aId, IAtomicValue aValue ) {
    // nop
  }

  /**
   * Informs about context reference change.
   *
   * @param aName String - changed reference name or <code>null</code> on batch changes
   * @param aRef {@link IAtomicValue} - the changed reference or <code>null</code> on batch changes
   */
  protected void onContextRefChanged( String aName, Object aRef ) {
    // nop
  }

}
