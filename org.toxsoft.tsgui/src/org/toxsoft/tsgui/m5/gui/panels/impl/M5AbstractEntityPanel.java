package org.toxsoft.tsgui.m5.gui.panels.impl;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.dialogs.TsDialogUtils;
import org.toxsoft.tsgui.m5.*;
import org.toxsoft.tsgui.m5.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.m5.model.IM5LifecycleManager;
import org.toxsoft.tsgui.m5.model.impl.M5Bunch;
import org.toxsoft.tsgui.m5.model.impl.M5BunchEdit;
import org.toxsoft.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.utils.errors.TsIllegalArgumentRtException;
import org.toxsoft.tslib.utils.errors.TsNullArgumentRtException;
import org.toxsoft.tslib.utils.logs.impl.LoggerUtils;

/**
 * {@link IM5EntityPanel} base implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public abstract class M5AbstractEntityPanel<T>
    extends M5PanelBase<T>
    implements IM5EntityPanel<T> {

  /**
   * Last field values.
   */
  private final M5BunchEdit<T> lastValues;

  /**
   * The lifecycle manager is used only by editor panel, may be <code>null</code>.
   */
  private IM5LifecycleManager<T> lifecycleManager = null;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @param aViewer boolean - flags that viewer (not editor) will be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5AbstractEntityPanel( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    super( aContext, aModel, aViewer );
    lastValues = new M5BunchEdit<>( model() );
  }

  // ------------------------------------------------------------------------------------
  // IM5EntityPanel
  //

  @Override
  public void setValues( IM5Bunch<T> aBunch ) {
    if( aBunch != null ) {
      TsIllegalArgumentRtException.checkFalse( model().equals( aBunch.model() ) );
      lastValues.fillFrom( aBunch, true );
    }
    else {
      lastValues.clear();
    }
    try {
      doSetValues( aBunch );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      TsDialogUtils.error( getShell(), ex );
    }
    doEditableStateChanged();
  }

  @Override
  public void setEntity( T aEntity ) {
    if( aEntity != null ) {
      TsIllegalArgumentRtException.checkFalse( model().isModelledObject( aEntity ) );
    }

    // TODO что задать при aEntity = null ???

    if( aEntity != null ) {
      setValues( model().valuesOf( aEntity ) );
    }
    else {
      setValues( getValues() );
    }
  }

  @Override
  public ValidationResult canGetValues() {
    try {
      return doCollectValues( lastValues );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
      return ValidationResult.error( ex );
    }
  }

  @Override
  public IM5Bunch<T> getValues() {
    ValidationResult vr;
    try {
      vr = doCollectValues( lastValues );
    }
    catch( Exception ex ) {
      vr = ValidationResult.error( ex );
    }
    TsValidationFailedRtException.checkError( vr );
    return new M5Bunch<>( lastValues );
  }

  @Override
  public IM5Bunch<T> lastValues() {
    return lastValues;
  }

  @Override
  final public IM5LifecycleManager<T> lifecycleManager() {
    return lifecycleManager;
  }

  @Override
  public void setLifecycleManager( IM5LifecycleManager<T> aLifecycleManager ) {
    lifecycleManager = aLifecycleManager;
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  @Override
  protected abstract Control doCreateControl( Composite aParent );

  /**
   * Subclass must copy values from <code>aBunch</code> argument to the GUI control.
   * <p>
   * If value can not be set to any of the control then method must throw an exception.
   *
   * @param aBunch {@link IM5BunchEdit}&lt;T&gt; - the values to copy to controls
   */
  protected abstract void doSetValues( IM5Bunch<T> aBunch );

  /**
   * Subclass must copy values from GUI controls to <code>aBunch</code> argument.
   * <p>
   * Method must not throw any exception. If value can be retrieved from any of the controls then error
   * {@link EValidationResultType#ERROR} must be returned.
   * <p>
   * The argument is filled with the last values set by {@link #setValues(IM5Bunch)}, or the last call to this method,
   * whichever was the last.
   *
   * @param aBunch {@link IM5BunchEdit}&lt;T&gt; - the values to be updated
   * @return {@link ValidationResult} - indicates the success of the value collection
   */
  protected abstract ValidationResult doCollectValues( IM5BunchEdit<T> aBunch );

}
