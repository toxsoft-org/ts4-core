package org.toxsoft.core.tsgui.m5.gui.panels.impl;

import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.m5.IM5Model;
import org.toxsoft.core.tsgui.m5.gui.panels.IM5PanelBase;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5PanelBase} base implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public abstract class M5PanelBase<T>
    implements IM5PanelBase<T>, ITsGuiContextable {

  private final GenericChangeEventer eventer;

  private final boolean isViewer;
  private boolean       editable;
  private Control       control = null;

  private final ITsGuiContext tsContext;
  private final IM5Model<T>   model;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @param aViewer boolean - flags that viewer (not editor) will be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected M5PanelBase( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    TsNullArgumentRtException.checkNulls( aContext, aModel );
    tsContext = aContext;
    model = aModel;
    eventer = new GenericChangeEventer( this );
    isViewer = aViewer;
    editable = !isViewer;
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  final public Control createControl( Composite aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    TsIllegalStateRtException.checkNoNull( control );
    Control c = doCreateControl( aParent );
    TsInternalErrorRtException.checkNull( c );
    if( control != null ) {
      TsInternalErrorRtException.checkTrue( c != control );
    }
    control = c;
    control.addDisposeListener( e -> doDispose() );
    return control;
  }

  @Override
  final public Control getControl() {
    return control;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IM5PanelBase
  //

  @Override
  public boolean isViewer() {
    return isViewer;
  }

  @Override
  final public IM5Model<T> model() {
    return model;
  }

  @Override
  final public boolean isEditable() {
    return editable;
  }

  @Override
  public void setEditable( boolean aEditable ) {
    if( isViewer ) {
      return;
    }
    if( editable != aEditable ) {
      editable = aEditable;
      doEditableStateChanged();
    }
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // Методы для наследников
  //

  /**
   * Задает значение, возвращаемое методом {@link #getControl()}.
   * <p>
   * Позволяет наследнику в {@link #doCreateControl(Composite)} задатьвозвращаемое методом {@link #getControl()}
   * значение. Это может понадобиться, чтобы из {@link #doCreateControl(Composite)} вызывать методы, рассчитывающие на
   * получение ссылки {@link #getControl()}.
   * <p>
   * Метод можно вызвать один раз, и задать только не-null значение. Если {@link #doCreateControl(Composite)} врнет
   * другую ссылку (а не ту, которая была тут задана), то будет выброшено исключние.
   *
   * @param aControl {@link Composite} - контроль
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException контроль {@link #getControl()} уже был задан
   */
  protected void setControl( Composite aControl ) {
    TsNullArgumentRtException.checkNull( aControl );
    TsIllegalStateRtException.checkNoNull( control );
    control = aControl;
  }

  /**
   * Генерирует сообщения интерфейса {@link IGenericChangeListener#onGenericChangeEvent(Object)}.
   */
  public void fireChangeEvent() {
    eventer.fireChangeEvent();
  }

  /**
   * Возвращает ссылку на главное окно.
   *
   * @return {@link Shell} - главное окно
   */
  @Override
  public Shell getShell() {
    if( control != null ) {
      return control.getShell();
    }
    return tsContext().get( Shell.class );
  }

  // ------------------------------------------------------------------------------------
  // To implement
  //

  /**
   * Subclass must create panel implementing SWT control.
   *
   * @param aParent {@link Composite} - parent composite
   * @return {@link Control} - created control, must no be <code>null</code>
   */
  protected abstract Control doCreateControl( Composite aParent );

  /**
   * Subclass may reslease resources allocated in {@link #doCreateControl(Composite)}.
   * <p>
   * This method is called before SWT control disposal only if {@link #createControl(Composite)} was called. Note that
   * this method will <b>not</b> be invoked if SWT control was not created.
   * <p>
   * Does nothing in base class so direct subclass does not needs to call superclass method.
   */
  protected void doDispose() {
    // nop
  }

  /**
   * The subclass may change the appearance and behavior of the panel when editing is enabled / disabled.
   * <p>
   * This method is called when {@link #isEditable()} state is changed.
   * <p>
   * Does nothing in the base class, when overridden, there is no need to call the superclass method.
   */
  protected void doEditableStateChanged() {
    // nop
  }

}
