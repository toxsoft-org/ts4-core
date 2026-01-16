package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.impl.ITsResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IValedControl} basic implementation.
 *
 * @author hazard157
 * @param <V> - the edited value type
 * @param <C> - the underlying widget type
 */
public abstract class AbstractValedControl<V, C extends Control>
    implements IValedControl<V>, ITsContextListener, ITsGuiContextable {

  /**
   * {@link IValedControl#eventer()} implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<IValedControlValueChangeListener> {

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
        l.onEditorValueChanged( AbstractValedControl.this, aEditFinished );
      }
    }

    // ------------------------------------------------------------------------------------
    // API

    /**
     * Fires an {@link IValedControlValueChangeListener#onEditorValueChanged(IValedControl, boolean)} event.
     *
     * @param aEditFinished boolean - the sign that editing was finished
     */
    public void fireEvent( boolean aEditFinished ) {
      if( isFiringPaused() ) {
        wasEvent = true;
        editFinished = aEditFinished;
      }
      else {
        internalFireEvent( aEditFinished );
      }
    }

  }

  // ------------------------------------------------------------------------------------
  // protected fields accessible for subclasses
  //

  /**
   * Listener for user-accessible child controls to fire this VALED change event.
   */
  protected SelectionListener notificationSelectionListener = new SelectionListener() {

    @Override
    public void widgetSelected( SelectionEvent e ) {
      fireModifyEvent( true );
    }

    @Override
    public void widgetDefaultSelected( SelectionEvent e ) {
      fireModifyEvent( true );
    }
  };

  /**
   * A control focus change listener that generates an edit-finished message when focus is lost.
   */
  protected FocusListener notifyEditFinishedOnFocusLostListener = new FocusListener() {

    @Override
    public void focusLost( FocusEvent e ) {
      fireModifyEvent( true );
    }

    @Override
    public void focusGained( FocusEvent e ) {
      // nop
    }
  };

  /**
   * Listener for user-accessible child controls to fire this VALED change event.
   */
  protected ISelectionChangedListener notificationSelectionChangedListener = event -> fireModifyEvent( true );

  /**
   * Listener for user-accessible child controls to fire this VALED change event.
   */
  protected ModifyListener notificationModifyListener = e -> fireModifyEvent( true );

  /**
   * Listener for user-accessible child widgets to fire this VALED change event.
   */
  protected IGenericChangeListener widgetValueChangeListener = aSource -> fireModifyEvent( true );

  /**
   * Listener for user-accessible child VALEDs to fire this VALED change event.
   */
  protected IValedControlValueChangeListener childValedsListener = ( src, finished ) -> fireModifyEvent( finished );

  // ------------------------------------------------------------------------------------
  // other fields
  //

  /**
   * SWT-control returned by {@link #getControl()} - the implementation of this VALED.
   * <p>
   * For complex VALEDs usually it is some {@link Composite} containing other widgets.
   */
  private C control = null;

  private final ITsGuiContext tsContext;

  /**
   * The value of the {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE} parameter when the constructor is called.
   */
  private final boolean createdUneditable;

  /**
   * A flag determining that a value is being changed in a programmatic way.
   *
   * @see AbstractValedControl#isSelfEditing()
   * @see AbstractValedControl#setSelfEditing(boolean)
   */
  private boolean isSelfEditing = false;

  // TODO TRANSLATE

  /**
   * A flag determining if editing is enabled in VALED.
   * <p>
   * Changed by the {@link #setEditable(boolean)} method, which causes the SWT control's state to be changed by the
   * {@link #doSetEditable(boolean)} method.
   * <p>
   * If the control is created with the {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE} flag set to
   * <code>true</code>, then it is set to <code>false</code> and the {@link #setEditable(boolean)} method can not change
   * it (and therefore, {@link #doSetEditable(boolean)} is not called).
   */
  private boolean editable;

  /**
   * Value set by {@link #setValue(Object)}.
   * <p>
   * <p>
   * Последнее заданное значение методом {@link #setValue(Object)} (или <code>null</code> после вызова
   * {@link #clearValue()}) или возвращенное методом {@link #doGetUnvalidatedValue()}.
   * <p>
   * Используется для того, чтобы корректно работал метод {@link #getValue()}, даже когда виджет редакттра не
   * существует.
   * <p>
   * Изначально имеет значение null (больше нечего присваивать). Поэтому, до первого вызова {@link #getValue()} должен
   * быть вызван метод {@link #setValue(Object)}.
   */
  private V lastValue = null;

  /**
   * Optional visualsProvider initialized from {@link IValedControlConstants#REFDEF_VALUE_VISUALS_PROVIDER}.
   */
  private ITsVisualsProvider<V> visualsProvider = ITsVisualsProvider.DEFAULT;

  /**
   * {@link #eventer()} implementation.
   */
  private final Eventer eventer = new Eventer();

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the VALED context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  @SuppressWarnings( "unchecked" )
  protected AbstractValedControl( ITsGuiContext aContext ) {
    tsContext = TsNullArgumentRtException.checkNull( aContext );
    tsContext.addContextListener( this );
    createdUneditable = OPDEF_CREATE_UNEDITABLE.getValue( params() ).asBool();
    editable = !createdUneditable;
    // visualsProvider
    Object rawVp = REFDEF_VALUE_VISUALS_PROVIDER.getRef( tsContext(), ITsVisualsProvider.DEFAULT );
    if( !(rawVp instanceof ITsVisualsProvider) ) {
      throw new TsInternalErrorRtException();
    }
    visualsProvider = (ITsVisualsProvider<V>)rawVp;
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  @Override
  final public Control createControl( Composite aParent ) {
    TsNullArgumentRtException.checkNull( aParent );
    TsIllegalStateRtException.checkNoNull( control );
    C c = doCreateControl( aParent );
    if( control != null && c != control ) {
      throw new TsInternalErrorRtException();
    }
    TsInternalErrorRtException.checkNull( c );
    control = c;
    control.addDisposeListener( e -> {
      tsContext().removeContextListener( AbstractValedControl.this );
      onDispose();
    } );
    String tooltipText = getTooltipText();
    control.setToolTipText( tooltipText );
    doSetTooltip( tooltipText );
    doSetupControl();
    // 2023-12-17 GOGA --- change: old code ignores setEdiable() called between constructor and createControl()
    // doSetEditable( !isCreatedUneditable() );
    doSetEditable( editable );
    // ---
    setValue( lastValue );
    return control;
  }

  @Override
  final public C getControl() {
    return control;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  final public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IValedControl
  //

  @Override
  final public boolean isEditable() {
    return editable;
  }

  @Override
  final public void setEditable( boolean aEditable ) {
    if( !createdUneditable && editable != aEditable ) {
      editable = aEditable;
      if( control != null ) {
        doSetEditable( aEditable );
      }
    }
  }

  @Override
  public ValidationResult canGetValue() {
    if( getControl() == null ) {
      if( lastValue == null ) {
        return ValidationResult.error( FMT_ERR_CANT_GET_VALUE_BEFORE_ITS_SET, this.getClass().getSimpleName() );
      }
      return ValidationResult.SUCCESS; // anyway, doCanGetValue() must NOT be called if widget does not exists
    }
    return doCanGetValue();
  }

  @Override
  final public V getValue() {
    if( getControl() == null ) {
      if( lastValue == null ) {
        throw new TsIllegalStateRtException( FMT_ERR_CANT_GET_VALUE_BEFORE_ITS_SET, this.getClass().getSimpleName() );
      }
      return lastValue;
    }
    TsValidationFailedRtException.checkError( canGetValue() );
    V v = doGetUnvalidatedValue();
    lastValue = v;
    return v;
  }

  @Override
  final public void setValue( V aValue ) {
    lastValue = aValue;
    if( getControl() == null ) {
      return;
    }
    boolean savedSelfEditingFlag = isSelfEditing;
    isSelfEditing = true;
    try {
      if( aValue != null ) {
        doSetUnvalidatedValue( aValue );
      }
      else {
        doClearValue();
      }
    }
    finally {
      isSelfEditing = savedSelfEditingFlag;
    }
    getControl().getParent().layout();
  }

  @Override
  final public void clearValue() {
    lastValue = null;
    if( getControl() == null ) {
      return;
    }
    boolean savedSelfEditingFlag = isSelfEditing;
    isSelfEditing = true;
    try {
      doClearValue();
    }
    finally {
      isSelfEditing = savedSelfEditingFlag;
    }
    getControl().getParent().layout();
  }

  @Override
  public ITsEventer<IValedControlValueChangeListener> eventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  final public IOptionSetEdit params() {
    return tsContext.params();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Sets value in {@link #params()} if option is not present or has value {@link IAtomicValue#NULL}.
   *
   * @param aParamId String - option ID
   * @param aValue {@link IAtomicValue} - the option value to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException the ID is not an IDpath
   */
  public final void setParamIfNull( String aParamId, IAtomicValue aValue ) {
    StridUtils.checkValidIdPath( aParamId );
    TsNullArgumentRtException.checkNull( aValue );
    if( !tsContext().isSelfOption( aParamId ) || tsContext.params().isNull( aParamId ) ) {
      params().setValue( aParamId, aValue );
    }
  }

  /**
   * Sets value in {@link #params()} if option is not present or has value {@link IAtomicValue#NULL}.
   *
   * @param aParamId {@link IStridable} - option ID
   * @param aValue {@link IAtomicValue} - the option value to set
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public final void setParamIfNull( IStridable aParamId, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNulls( aParamId, aValue );
    setParamIfNull( aParamId.id(), aValue );
  }

  /**
   * Sets the value visuals provider.
   *
   * @param aVisualsProvider {@link ITsVisualsProvider}&lt;V&gt; - the value visuals provider
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public void setVisualsProvider( ITsVisualsProvider<V> aVisualsProvider ) {
    TsNullArgumentRtException.checkNull( aVisualsProvider );
    visualsProvider = aVisualsProvider;

  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Задает значение, возвращаемое методом {@link #getControl()}.
   * <p>
   * Позволяет наследнику в {@link #doCreateControl(Composite)} задать возвращаемое методом {@link #getControl()}
   * значение. Это может понадобиться, чтобы из {@link #doCreateControl(Composite)} вызывать методы, рассчитывающие на
   * получение ссылки {@link #getControl()}.
   * <p>
   * Метод можно вызвать один раз, и задать только не-null значение. Если {@link #doCreateControl(Composite)} врнет
   * другую ссылку (а не ту, которая была тут задана), то будет выброшено исключние.
   *
   * @param aControl {@link Control} - контроль
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsIllegalArgumentRtException контроль {@link #getControl()} уже был задан
   */
  protected void setControl( C aControl ) {
    TsNullArgumentRtException.checkNull( aControl );
    if( aControl != control ) {
      TsIllegalStateRtException.checkNoNull( control );
    }
    control = aControl;
  }

  /**
   * Определяет, доступен ли виджет.
   * <p>
   * Виджет доступен, если {@link #getControl()} != <code>null</code> и виджет еще не уничтожен. Этот метод проверки
   * рекомендуется наследникам вместо простой праверки {@link #getControl()} != <code>null</code>.
   *
   * @return boolean - признак существования виджета
   */
  public boolean isWidget() {
    return control != null && !control.isDisposed();
  }

  /**
   * Возвращает значение параметра {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE} при создании редактора.
   *
   * @return boolean - значение параметра {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE}
   */
  public final boolean isCreatedUneditable() {
    return createdUneditable;
  }

  /**
   * Возвращает текст всплывающей подсказки из параметров {@link #params()}.
   *
   * @return String - текущий текст всплывающей подсказки
   */
  public final String getTooltipText() {
    return OPDEF_TOOLTIP_TEXT.getValue( params() ).asString();
  }

  /**
   * Возвращает признак изменения значения в контроле программным путем.
   * <p>
   * При таком изменении не должно генерироваться сообщение
   * {@link IValedControlValueChangeListener#onEditorValueChanged(IValedControl, boolean)}. Посколку некторые SWT
   * контроли генерируют сообщение при программной установке значений (например, {@link Text#setText(String)}), то перед
   * программным изменением следует установить, а после редактирования сбросить признак методом
   * {@link #setSelfEditing(boolean)}.
   * <p>
   * Когда этот прзнак <code>true</code>, также не вызывается {@link #onValueChanged()}.
   *
   * @return boolean - признак изменения значения в контроле программным путем
   */
  public boolean isSelfEditing() {
    return isSelfEditing;
  }

  /**
   * Задает значение признака изменения значения в контроле программным путем.
   * <p>
   * Следует задать <code>true</code> перед внесением изменения в значение в виджете и <code>false</code> после
   * окончания.
   *
   * @param aIsSelfEditing boolean - признак изменения значения в контроле программным путем
   */
  public void setSelfEditing( boolean aIsSelfEditing ) {
    isSelfEditing = aIsSelfEditing;
  }

  /**
   * Fires an {@link IValedControlValueChangeListener} event only if {@link #isSelfEditing()} = <code>false</code>.
   *
   * @param aEditFinished boolean - the sign that editing was finished
   */
  public void fireModifyEvent( boolean aEditFinished ) {
    if( isSelfEditing ) {
      return;
    }
    if( !onValueChanged() ) {
      return;
    }
    eventer.fireEvent( aEditFinished );
  }

  /**
   * Returns the visuals provider specified by {@link IValedControlConstants#REFDEF_VALUE_VISUALS_PROVIDER}.
   * <p>
   * If reference {@link IValedControlConstants#REFDEF_VALUE_VISUALS_PROVIDER} is not found in the VALED creation
   * context, default provider {@link ITsVisualsProvider#DEFAULT} is returned.
   *
   * @return {@link ITsVisualsProvider}&lt;V&gt; - edited value visuals provider
   */
  public ITsVisualsProvider<V> visualsProvider() {
    return visualsProvider;
  }

  // ------------------------------------------------------------------------------------
  // ITsContextListener
  //

  /**
   * {@inheritDoc}
   * <p>
   * In {@link AbstractValedControl} does nothing, there is no need to call superclass method when overriding.
   */
  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    // nop
  }

  /**
   * {@inheritDoc}
   * <p>
   * In {@link AbstractValedControl} does nothing, there is no need to call superclass method when overriding.
   */
  @Override
  public <X extends ITsContextRo> void onContextRefChanged( X aSource, String aName, Object aRef ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // To override
  //

  /**
   * Subclass may perform check if the widgets contain valid value to be returned by {@link #getValue()}.
   * <p>
   * Called from {@link #canGetValue()} only when SWT widget exits, that is {@link #getControl()} != <code>null</code>.
   * <p>
   * In {@link AbstractValedControl} returns {@link ValidationResult#SUCCESS}, there is no need to call superclass
   * method when overriding.
   *
   * @return {@link ValidationResult} - - the check result
   */
  protected ValidationResult doCanGetValue() {
    return ValidationResult.SUCCESS;
  }

  /**
   * Called when the operating system widget that implements this control is destroyed.
   * <p>
   * This method in the base class {@link AbstractValedControl} does nothing and the parent method does not need to be
   * called when overridden. However, if the inheritance does not come directly from {@link AbstractValedControl} but
   * its successor, you need to call the parent method.
   */
  protected void onDispose() {
    // nop
  }

  /**
   * Наследник может задать текст всплывающей подсказки.
   * <p>
   * Родительский класс {@link AbstractValedControl} задает текст подсказки методом
   * {@link Control#setToolTipText(String)} контролю, возвращаемоую методом {@link #doCreateControl(Composite)}. Однако,
   * для комплексных контролей понадобится задавать текст для всех виджетов, составляющих комплексный редактор.
   *
   * @param aTooltipText String - текст всплывающей подсказки
   */
  protected void doSetTooltip( String aTooltipText ) {
    // nop
  }

  /**
   * Наследник может осуществить дополнительные действия после создания контроля в методе
   * {@link #createControl(Composite)}.
   * <p>
   * В базовом классе {@link AbstractValedControl} ничего не делает, при переопределении вызывать родительский метод не
   * нужно.
   */
  protected void doSetupControl() {
    // nop
  }

  /**
   * Subclass may add processing after user changes value but before value change event is fired.
   * <p>
   * Subclass may disable event firing by returning <code>false</code>. Value will be changed anyway but without
   * notification. This may be usefull in case of linked editors. For example editing time interval START field event
   * must be fired after END field will be adjusted.
   * <p>
   * Возвращая false, наследник может игнорировать изменение. Особенно, это имеет смысл для комлексных (состоящих из
   * нескольких виджетов) редакторов. Напрмер, редактор интервала времени (виджеты "начало", "окончание",
   * "длительность") имеет возможность ограничить время окончания, чтобы не было раньше времени начала.
   * <p>
   * In base class simply returns <code>true</code>, no need to call superclass method in subclasses.
   *
   * @return boolean - if <code>false</code> no {@link IValedControlValueChangeListener} event will be fired
   */
  protected boolean onValueChanged() {
    return true;
  }

  // ------------------------------------------------------------------------------------
  // To implements
  //

  /**
   * Реализация должна создать панель, реализующий этот редактор.
   * <p>
   * Метод вызывается из {@link #createControl(Composite)}.
   *
   * @param aParent {@link Composite} - родительская панель
   * @return {@link Control} - созданный контроль
   */
  abstract protected C doCreateControl( Composite aParent );

  /**
   * Implementation must change SWT widgets so that editing becomes impossible.
   * <p>
   * This method is called when <b>all</b> conditions ae met:
   * <ul>
   * <li>the SWT widget exists {@link #isWidget()} = true;</li>
   * <li>VALED is not read-only, {@link #isCreatedUneditable()} = <code>false</code>;</li>
   * <li>{@link #isEditable()} state really has been changed.</li>
   * </ul>
   * When method is called, {@link #isEditable()} is already has changed value.
   *
   * @param aEditable boolean - editing permission sign
   */
  abstract protected void doSetEditable( boolean aEditable );

  /**
   * Реализация должна вернуть значение, находящейся в контроле.
   * <p>
   * Этот метод вызывается только когда существует виджет, то есть, когда {@link #getControl()} != null.
   * <p>
   * При существующем виджете этот метод вызывается, только если предварительный вызов {@link #canGetValue()} не верент
   * ошибку {@link EValidationResultType#ERROR}.
   *
   * @return &lt;V&gt; - значение, находящейся в редакторе, может быть <code>null</code>
   * @throws TsValidationFailedRtException {@link #canGetValue()} вернул ошибку
   */
  abstract protected V doGetUnvalidatedValue();

  /**
   * Subclass must the value to editor widget(s).
   * <p>
   * While <code>aValue</code> never is <code>null</code> it must be checked for some "special case" value like
   * {@link IAtomicValue#NULL}.
   * <p>
   * This method is called after the SWT widgets are created.
   *
   * @param aValue &lt;V&gt; - new value, never is <code>null</code>
   * @throws TsIllegalArgumentRtException value has incompatible type
   */
  abstract protected void doSetUnvalidatedValue( V aValue );

  /**
   * Implementation must clear the value in the widget.
   * <p>
   * This method is called from {@link #clearValue()} and {@link #setValue(Object) setValue( <b>null</b> )}.
   * <p>
   * This method is called only when widget is created, that is {@link #getControl()} != <code>null</code>.
   */
  abstract protected void doClearValue();

}
