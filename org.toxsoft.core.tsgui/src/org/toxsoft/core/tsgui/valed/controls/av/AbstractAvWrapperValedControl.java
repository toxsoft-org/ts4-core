package org.toxsoft.core.tsgui.valed.controls.av;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.errors.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * The wrapper converts value of some type &lt;T&gt; from underlying valed to the {@link IAtomicValue}.
 *
 * @author hazard157
 * @param <T> - value type of the wrapped valed
 */
public abstract class AbstractAvWrapperValedControl<T>
    implements IValedControl<IAtomicValue> {

  /**
   * {@link AbstractAvWrapperValedControl#eventer()} implementing class.
   * <p>
   * Is used as wrepper over underlying valed's {@link IValedControl#eventer()}.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<IValedControlValueChangeListener>
      implements IValedControlValueChangeListener {

    private boolean wasEvent     = false;
    private boolean editFinished = false;

    @Override
    protected boolean doIsPendingEvents() {
      return wasEvent;
    }

    @Override
    protected void doFirePendingEvents() {
      if( wasEvent ) {
        internalFireEvent( editFinished );
      }
    }

    @Override
    protected void doClearPendingEvents() {
      wasEvent = true;
    }

    private void internalFireEvent( boolean aEditFinished ) {
      for( IValedControlValueChangeListener l : listeners() ) {
        l.onEditorValueChanged( AbstractAvWrapperValedControl.this, aEditFinished );
      }
    }

    // ------------------------------------------------------------------------------------
    // IValedControlValueChangeListener

    @Override
    public void onEditorValueChanged( IValedControl<?> aSource, boolean aEditFinished ) {
      fireEvent( aEditFinished );
    }

    // ------------------------------------------------------------------------------------
    // API

    void fireEvent( boolean aEditFinished ) {
      if( isFiringPaused() ) {
        wasEvent = true;
        editFinished = aEditFinished;
      }
      else {
        internalFireEvent( aEditFinished );
      }
    }

  }

  private final IValedControl<T> source;
  private final EAtomicType      atomicType;
  private final Eventer          eventer = new Eventer();

  protected AbstractAvWrapperValedControl( ITsGuiContext aTsContext, EAtomicType aAtomicType,
      IValedControlFactory aUnderlyingFactory ) {
    TsNullArgumentRtException.checkNulls( aAtomicType, aUnderlyingFactory );
    source = aUnderlyingFactory.createEditor( aTsContext );
    atomicType = aAtomicType;
    source.eventer().addListener( eventer );
  }

  @Override
  public Control createControl( Composite aParent ) {
    return source.createControl( aParent );
  }

  @Override
  public Control getControl() {
    return source.getControl();
  }

  @Override
  public ITsContext tsContext() {
    return source.tsContext();
  }

  @Override
  public IOptionSetEdit params() {
    return source.params();
  }

  @Override
  public boolean isEditable() {
    return source.isEditable();
  }

  @Override
  public void setEditable( boolean aEditable ) {
    source.setEditable( aEditable );
  }

  @Override
  public ValidationResult canGetValue() {
    ValidationResult vr = source.canGetValue();
    if( !vr.isError() ) {
      T value = source.getValue();
      vr = ValidationResult.firstNonOk( vr, doCanGetValue( value ) );
    }
    return vr;
  }

  @Override
  public IAtomicValue getValue() {
    T tv = source.getValue();
    if( tv == null ) {
      return IAtomicValue.NULL;
    }
    IAtomicValue av = tv2av( tv );
    TsInternalErrorRtException.checkNull( av );
    if( !AvTypeCastRtException.canAssign( atomicType, av.atomicType() ) ) {
      throw new TsInternalErrorRtException();
    }
    return av;
  }

  @Override
  public void setValue( IAtomicValue aValue ) {
    if( aValue == null || aValue == IAtomicValue.NULL ) {
      clearValue();
      return;
    }
    AvTypeCastRtException.checkCanAssign( atomicType, aValue.atomicType() );
    T tv = av2tv( aValue );
    TsInternalErrorRtException.checkNull( tv );
    source.setValue( tv );
  }

  @Override
  public void clearValue() {
    // lastValue = IAtomicValue.NULL;
    source.clearValue();
  }

  @Override
  public ITsEventer<IValedControlValueChangeListener> eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // TTo override
  //

  /**
   * Implementaion may perform additional check if atomic value may be returned by {@link #getValue()}.
   * <p>
   * This methid is called when underlying valed {@link IValedControl#canGetValue()} returns without error. As an
   * argument the underlying valed's value is used.
   * <p>
   * In base cclass simply returns {@link ValidationResult#SUCCESS}, when overriding, there is no need to call
   * superclass method.
   *
   * @param aValue &lt;T&gt; - value in underlying valed
   * @return {@link ValidationResult} - the validation result
   */
  protected ValidationResult doCanGetValue( T aValue ) {
    return ValidationResult.SUCCESS;
  }

  /**
   * Implementation must resolve value from underlyine valed to atomic value.
   * <p>
   * This is the value returned by underlying {@link IValedControl#getValue()}.
   * <p>
   * Note that method must return either {@link IAtomicValue#NULL} or value of {@link #atomicType}.
   *
   * @param aTypedValue &lt;T&gt; - the underlyined value, never is <code>null</code>
   * @return {@link IAtomicValue} - corresponding atomic value, must not be <code>null</code>
   */
  protected abstract IAtomicValue tv2av( T aTypedValue );

  /**
   * Implementation must convert atomic value to underlying valed value.
   * <p>
   * Note that argument is always of {@link #atomicType}.
   *
   * @param aAtomicValue {@link IAtomicValue} - the atomic value, never is <code>null</code> or
   *          {@link IAtomicValue#NULL}
   * @return &lt;T&gt; - corresponding underlying value, must not be <code>null</code>
   */
  protected abstract T av2tv( IAtomicValue aAtomicValue );

}
