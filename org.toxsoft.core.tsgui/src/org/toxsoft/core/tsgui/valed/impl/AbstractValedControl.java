package org.toxsoft.core.tsgui.valed.impl;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tsgui.valed.impl.ITsResources.*;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.core.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.core.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tslib.av.IAtomicValue;
import org.toxsoft.core.tslib.av.opset.IOptionSetEdit;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextListener;
import org.toxsoft.core.tslib.bricks.ctx.ITsContextRo;
import org.toxsoft.core.tslib.bricks.events.AbstractTsEventer;
import org.toxsoft.core.tslib.bricks.events.ITsEventer;
import org.toxsoft.core.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.core.tslib.bricks.strid.IStridable;
import org.toxsoft.core.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.core.tslib.bricks.validator.ValidationResult;
import org.toxsoft.core.tslib.bricks.validator.impl.TsValidationFailedRtException;
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

  // ------------------------------------------------------------------------------------
  // Поля, доступные для наследников
  //

  /**
   * Слушатель изменений в виджетах, извещающий диалог о правках пользовтеля.
   * <p>
   * Этот слушатель типа {@link SelectionListener} нужно ставить тем виджетам в панели, с которыми работает пользователь
   * для внесения изменений в отображаемой структуре данных T.
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
   * Слушатель изменения фокуса контролем, который генерирует сообщение об окончании редактирования при потере фокуса.
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
   * Слушатель изменений в просмотрщиках (XxxViewer), извещающий диалог о правках пользовтеля.
   * <p>
   * Этот слушатель типа {@link ISelectionChangedListener} нужно ставить тем просмотрщикам в панели, с которыми работает
   * пользователь для внесения изменений в отображаемой структуре данных T.
   */
  protected ISelectionChangedListener notificationSelectionChangedListener = new ISelectionChangedListener() {

    @Override
    public void selectionChanged( SelectionChangedEvent event ) {
      fireModifyEvent( true );
    }
  };

  /**
   * Слушатель изменений в виджетах, извещающий диалог о правках пользовтеля.
   * <p>
   * Этот слушатель типа {@link ModifyListener} нужно ставить тем виджетам в панели, с которыми работает пользователь
   * для внесения изменений в отображаемой структуре данных T.
   */
  protected ModifyListener notificationModifyListener = new ModifyListener() {

    @Override
    public void modifyText( ModifyEvent e ) {
      fireModifyEvent( false );
    }
  };

  /**
   * Слушатель изменений в виджетах, извещающий диалог о правках пользовтеля.
   * <p>
   * Этот слушатель типа {@link IGenericChangeListener} нужно ставить тем виджетам в панели, с которыми работает
   * пользователь для внесения изменений в отображаемой структуре данных T.
   */
  protected IGenericChangeListener widgetValueChangeListener = new IGenericChangeListener() {

    @Override
    public void onGenericChangeEvent( Object aSource ) {
      fireModifyEvent( true );
    }

  };

  // ------------------------------------------------------------------------------------
  // Другие поля экземпляра класса
  //

  /**
   * SWT-контроль реализующий {@link IValedControl}, создается на втором этапе инифиализации редактора, методом
   * {@link #doCreateControl(Composite)}.
   */
  private C control = null;

  /**
   * Контекст.
   */
  private final ITsGuiContext tsContext;

  /**
   * Значение параметра {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE} в момент вызова конструктора.
   */
  private final boolean createdUneditable;

  /**
   * Признак изменения значения в контроле программным путем.
   *
   * @see AbstractValedControl#isSelfEditing()
   * @see AbstractValedControl#setSelfEditing(boolean)
   */
  private boolean isSelfEditing = false;

  /**
   * Признак разрешения редакторования.
   * <p>
   * Изменяется методом {@link #setEditable(boolean)}, что приводит к изменению состояния SWT-контроля методом
   * {@link #doSetEditable(boolean)}.
   * <p>
   * Если контроль создан с признаком {@link IValedControlConstants#OPDEF_CREATE_UNEDITABLE}, то равен
   * <code>false</code> и метод {@link #setEditable(boolean)} не меняет его (соответственно, не вызывается
   * {@link #doSetEditable(boolean)}).
   */
  private boolean editable;

  /**
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
   * Реализация для {@link #eventer()}.
   */
  private final Eventer eventer = new Eventer();

  // ------------------------------------------------------------------------------------
  // Скрытый конструктор
  //

  /**
   * Constructor for subclasses.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected AbstractValedControl( ITsGuiContext aTsContext ) {
    TsNullArgumentRtException.checkNull( aTsContext );
    tsContext = new TsGuiContext( aTsContext );
    tsContext.addContextListener( this );
    createdUneditable = OPDEF_CREATE_UNEDITABLE.getValue( params() ).asBool();
    editable = !createdUneditable;
  }

  // ------------------------------------------------------------------------------------
  // ILazyControl
  //

  /**
   * Создает SWT-контроль, реализующий этот редактор.
   * <p>
   * Этот метод вызывается фабрикой сразу после конструктора.
   *
   * @param aParent {@link Composite} - родительская панель
   * @throws TsNullArgumentRtException аргумент = null
   */
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
    control.addDisposeListener( new DisposeListener() {

      @Override
      public void widgetDisposed( DisposeEvent e ) {
        tsContext().removeContextListener( AbstractValedControl.this );
        onDispose();
      }
    } );
    String tooltipText = getTooltipText();
    control.setToolTipText( tooltipText );
    doSetTooltip( tooltipText );
    doSetupControl();
    doSetEditable( !isCreatedUneditable() );
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
  // ITsGuiStdContextReferences
  //

  @Override
  public IEclipseContext eclipseContext() {
    return tsContext.eclipseContext();
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
  final public V getValue() {
    if( getControl() == null ) {
      TsIllegalStateRtException.checkNull( lastValue, FMT_ERR_CANT_GET_VALUE_BEFORE_ITS_SET,
          this.getClass().getSimpleName() );
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
    eventer.pauseFiring();
    try {
      if( aValue != null ) {
        doSetUnvalidatedValue( aValue );
      }
      else {
        doClearValue();
      }
    }
    finally {
      eventer.resumeFiring( false );
    }
    getControl().getParent().layout();
  }

  @Override
  final public void clearValue() {
    lastValue = null;
    if( getControl() == null ) {
      return;
    }
    eventer.pauseFiring();
    try {
      doClearValue();
    }
    finally {
      eventer.resumeFiring( false );
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
  // API класса
  //

  /**
   * Задает значение в {@link #params()}, если его там нет, или оно равно {@link IAtomicValue#NULL}.
   *
   * @param aParamId String - идентификатор параметра
   * @param aValue {@link IAtomicValue} - новое значнеие параметра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final void setParamIfNull( String aParamId, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNulls( aParamId, aValue );
    if( !params().getValue( aParamId, IAtomicValue.NULL ).isAssigned() ) {
      params().setValue( aParamId, aValue );
    }
  }

  /**
   * Задает значение в {@link #params()}, если его там нет, или оно равно {@link IAtomicValue#NULL}.
   *
   * @param aParamId {@link IStridable} - идентификатор параметра
   * @param aValue {@link IAtomicValue} - новое значнеие параметра
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public final void setParamIfNull( IStridable aParamId, IAtomicValue aValue ) {
    TsNullArgumentRtException.checkNulls( aParamId, aValue );
    setParamIfNull( aParamId.id(), aValue );
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
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
    TsIllegalStateRtException.checkNoNull( control );
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
   * Извещает слушателей {@link IValedControlValueChangeListener} об измненни
   *
   * @param aEditFinished boolean - признак завершения редактирования (ввода значения)
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

  // ------------------------------------------------------------------------------------
  // Методы, которые могут потребовать реализации наследниками
  //

  @Override
  public ValidationResult canGetValue() {
    return ValidationResult.SUCCESS;
  }

  /**
   * Вызывается при уничтожении виджета операционной системы, реализуюший данный контроль.
   * <p>
   * В базовом классе {@link AbstractValedControl} ничего не делает, и при переопределении можно не вызвать. Однако,
   * если наследуетесь не напрямую от {@link AbstractValedControl} а его наследника, нужно вызвать родительский метод.
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
  // ITsContextListener
  //

  @Override
  public <X extends ITsContextRo> void onContextOpChanged( X aSource, String aId, IAtomicValue aValue ) {
    // nop
  }

  @Override
  public <X extends ITsContextRo> void onContextRefChanged( X aSource, String aName, Object aRef ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // Методы, обязательные для реализации наследниками
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
   * Реализация должна изменить вид и работу SWT-контроля для отражения состояния разрешенности.
   * <p>
   * Вызывается только при изменении состояния {@link #isEditable()} из метода {@link #setEditable(boolean)}. При вызове
   * этого метода {@link #isEditable()} уже имеет новое значение признака.
   * <p>
   * Вызов метода осуществляется только при существующем контроле, то есть, когда {@link #getControl()} !=
   * <code>null</code>.
   *
   * @param aEditable booolean - признак разрешения редактирования
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
   * Вызывается из {@link #setValue(Object)} после проверки аргумента на <code>null</code>.
   * <p>
   * Этот метод вызывается только когда существует виджет, то есть, когда {@link #getControl()} != null.
   *
   * @param aValue V - новое значение, не бывает <code>null</code>
   * @throws TsIllegalArgumentRtException значение имеет несовместимый с редактором тип
   */
  abstract protected void doSetUnvalidatedValue( V aValue );

  /**
   * Implementation must clear the value in the widget.
   * <p>
   * This methos\d is called from {@link #clearValue()} and {@link #doSetUnvalidatedValue(Object)} with
   * <code>null</code> argument.
   * <p>
   * This meothod is called only when widget is created, that is {@link #getControl()} != <code>null</code>.
   */
  abstract protected void doClearValue();

}
