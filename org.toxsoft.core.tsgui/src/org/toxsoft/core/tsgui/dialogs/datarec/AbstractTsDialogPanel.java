package org.toxsoft.core.tsgui.dialogs.datarec;

import static org.toxsoft.core.tsgui.dialogs.datarec.ITsDialogConstants.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.swt.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * Dialog content panel for data record view / edit.
 * <p>
 * Panel may be used either as {@link TsDialog} content or as usual panel anywhere.
 * <p>
 * TODO describe how to subclass, incl. CREATOR
 *
 * @author hazard157
 * @param <T> - data transfer object type passed to/from dialog
 * @param <E> - client specified optional environment type
 */
abstract public class AbstractTsDialogPanel<T, E>
    extends TsComposite
    implements IGenericChangeEventCapable, ITsGuiContextable {

  /**
   * Owner dialog or <code>null</code>.
   */
  private final TsDialog<T, E> ownerDialog;

  private final GenericChangeEventer eventer;

  /**
   * These fields are initialzed in constructor, either from owner {@link TsDialog} or from constructor arguments.
   */
  private final ITsGuiContext tsContext;
  private final E             environ;
  private final int           panelFlags;
  private T                   dataInput = null; // это поле менят также метод setDataRecord()

  // ------------------------------------------------------------------------------------
  // protected listeners helps to handle edit events in child controls
  //

  /**
   * SWT listener for controls - just calls {@link #fireContentChangeEvent()}.
   */
  protected SelectionListener notificationSelectionListener = new SelectionListenerAdapter() {

    @Override
    public void widgetSelected( SelectionEvent e ) {
      fireContentChangeEvent();
    }
  };

  /**
   * SWT listener for controls - just calls {@link #fireContentChangeEvent()}.
   */
  protected ISelectionChangedListener notificationSelectionChangedListener = event -> fireContentChangeEvent();

  /**
   * SWT listener for controls - just calls {@link #fireContentChangeEvent()}.
   */
  protected ModifyListener notificationModifyListener = e -> fireContentChangeEvent();

  /**
   * TS listener for controls - just calls {@link #fireContentChangeEvent()}.
   */
  protected IGenericChangeListener notificationGenericChangeListener = aSource -> fireContentChangeEvent();

  /**
   * TS listener for controls - just calls {@link #fireContentChangeEvent()}.
   */
  protected IValedControlValueChangeListener notificationValedControlChangeListener =
      ( src, fin ) -> fireContentChangeEvent();

  // ------------------------------------------------------------------------------------
  // Constructors
  //

  // TODO TRANSLATE

  /**
   * Constructs panel as {@link TsDialog} content.
   * <p>
   * After creating a panel with this constructor, before displaying it on the screen, the parent dialog calls the
   * {@link #setDataRecord(Object)} method. Therefore, in the constructor there is no need to initialize the contents of
   * the controls - it is guaranteed that the "fill" method in the data controls {@link #doSetDataRecord(Object)} will
   * be called before the panel is shown.
   *
   * @param aParent {@link Composite} - the parent composite
   * @param aOwnerDialog {@link TsDialog} - the owner dialog
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractTsDialogPanel( Composite aParent, TsDialog<T, E> aOwnerDialog ) {
    super( aParent, SWT.NONE );
    eventer = new GenericChangeEventer( this );
    ownerDialog = TsNullArgumentRtException.checkNull( aOwnerDialog );
    dataInput = ownerDialog.dialogData;
    panelFlags = aOwnerDialog.dialogInfo().flags();
    tsContext = ownerDialog.tsContext();
    environ = ownerDialog.environment();
  }

  /**
   * Constructor to be used as a generic panel.
   * <p>
   * Warning: when creating a panel with this constructor (unlike the
   * {@link #AbstractTsDialogPanel(Composite, TsDialog)} constructor), initialization of controls with
   * <code>aData</code> data must be done in the body of the constructor. The correct way is to have an implementation
   * of the {@link #doSetDataRecord(Object) method }, and at the end of the constructor body call it.
   *
   * @param aParent {@link Composite} - the parent composite
   * @param aContext {@link ITsGuiContext} - the context
   * @param aData &lt;T&gt; - initial data record value, may be <code>null</code>
   * @param aEnviron &lt;E&gt; - the environment, may be <code>null</code>
   * @param aFlags int - ORed dialog configuration flags <code>DF_XXX</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  protected AbstractTsDialogPanel( Composite aParent, ITsGuiContext aContext, T aData, E aEnviron, int aFlags ) {
    super( aParent, SWT.NONE );
    TsNullArgumentRtException.checkNulls( aEnviron, aContext );
    eventer = new GenericChangeEventer( this );
    dataInput = aData;
    ownerDialog = null;
    panelFlags = aFlags;
    environ = aEnviron;
    tsContext = aContext;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Извещает зарегистрированноых слушателей об изменениях в контролях.
   * <p>
   * Этот метод <b>должен</b> вызываться неследником каждый раз, когда пользователь делает изменения в контролях
   * диалога.
   * <p>
   * Метод сделан public, а не protected, во избежание предупреждений, при вызове из анонимных классов.
   */
  public void fireContentChangeEvent() {
    eventer.fireChangeEvent();
  }

  /**
   * Возвращает данные, заданные в конструкторе или {@link #setDataRecord(Object)}.
   * <p>
   * Метод может возвращаеть null.
   * <p>
   * Метод сделан public, а не protected, во избежание предупреждений, при вызове из анонимных классов.
   *
   * @return T - заданные данные диалога
   */
  public T dataRecordInput() {
    return dataInput;
  }

  /**
   * Возвращает контекст, заданный в конструкторе.
   * <p>
   * Метод сделан public, а не protected, во избежание предупреждений, при вызове из анонимных классов.
   *
   * @return E - контекст просмотра / правки данных, не бывает null
   */
  public E environ() {
    return environ;
  }

  /**
   * Возвращает флаги конфигурирования панели.
   * <p>
   * Флаги конфигурирования задаются в конструкторе либо из родительского диалога, либо в отдельном параметре
   * конструктора. В любом случае, флаги это биты {@link TsDialog}.DF_XXX, собранные по ИЛИ.
   * <p>
   * Для получения наиболее важного флага панели, {@link ITsDialogConstants#DF_READONLY}, существует отдельный метод
   * {@link #isPanelReadOnly()}.
   * <p>
   * Метод сделан public, а не protected, во избежание предупреждений, при вызове из анонимных классов.
   *
   * @return int - флаги {@link TsDialog}.DF_XXX, собранные по ИЛИ
   * @see ITsDialogConstants#DF_READONLY
   * @see #isPanelReadOnly()
   */
  public int panelFlags() {
    return panelFlags;
  }

  /**
   * Определяет, работает ли панель в режиме "только-для-чтения" или редактирования.
   * <p>
   * Значение получается проверкой бита {@link ITsDialogConstants#DF_READONLY} в слове флагов {@link #panelFlags()}.
   * <p>
   * Обратите внимание, что режим "только-для-чтения" запрет пользователю графического интерфейса менять данные, метод @
   * {@link #getDataRecord()} при этом работает корректно.
   * <p>
   * Метод сделан public, а не protected, во избежание предупреждений, при вызове из анонимных классов.
   *
   * @return boolean - gпризнак режима "только-для-чтения"
   * @see ITsDialogConstants#DF_READONLY
   * @see #panelFlags()
   */
  public boolean isPanelReadOnly() {
    return (panelFlags & DF_READONLY) != 0;
  }

  /**
   * Возвращает родительский диалог или null если панель создан не для диалога.
   * <p>
   * Метод сделан public, а не protected, во избежание предупреждений, при вызове из анонимных классов.
   *
   * @return {@link TsDialog} - родительский диалог или null
   */
  public TsDialog<T, E> ownerDialog() {
    return ownerDialog;
  }

  // ------------------------------------------------------------------------------------
  // Методы для реализации наследниками
  //

  /**
   * Реализация метода должна проверять корректность вводимых пользователем данных.
   * <p>
   * Этот метод будет вызываться <b>по мере ввода</b> данных пользователем, а также (на всякий случай:) перед обработкой
   * нажатия кнопок использующие данные (например, Ok или Apply). При ошибочных данных, метод должен возвращать описание
   * ошибки (или предупреждение), поясняющим человеку, в чем неверность ввода. В случае верных данных, метод
   * {@link ValidationResult#SUCCESS}.
   * <p>
   * Если ошибочен ввод в двух или более контролях, метод должен возвращаеть сообщение об ошибочном вводе только в одном
   * контроле. При этом, порядок проверки контролей на валидность ввода следующий: сначала проверется сфокусированный
   * контроль (т.е. контроль, с которым работает пользователь сейчас), а потом контроли слева направо и сверху вниз, в
   * соответствии с естественным для пользователя порядком ввода данных.
   * <p>
   * В базовом классе {@link AbstractTsDialogPanel} ничего не делает, толко возвращает {@link ValidationResult#SUCCESS}.
   * При переопределении вызывать метод родителя не нужно.
   * <p>
   * Метод предназначен для использования в панели, который вставлен в диалог {@link TsDialog}, при отдельном
   * использовании панели переопределять нет необходимости (конечно, если Вы сами не захотите использовать валидацию
   * данных).
   *
   * @return {@link ValidationResult} - результат проверки вводимых даных
   */
  protected ValidationResult validateData() {
    return doValidate();
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Задает данные для отображения в диалогах.
   * <p>
   * Данные диалога запоминаются, и доступны наследникам методом {@link #dataRecordInput()}.
   *
   * @param aData &lt;T&gt; - задаваемые данные, может быть null
   */
  final public void setDataRecord( T aData ) {
    dataInput = aData;
    try {
      doSetDataRecord( aData );
    }
    catch( Exception ex ) {
      LoggerUtils.errorLogger().error( ex );
    }
    if( ownerDialog != null ) {
      ownerDialog.validateDialogData();
    }
    else { // панель может использоваться ВНЕ диалога
      validateData();
    }
  }

  /**
   * Возвращает данные диалога, собранные из текущего содержимого контролей.
   * <p>
   * Не вызвается, если {@link #validateData()} возвращает {@link EValidationResultType#ERROR}.
   *
   * @return T - данные диалога, может быть null
   */
  final public T getDataRecord() {
    return doGetDataRecord();
  }

  // ------------------------------------------------------------------------------------
  // IGenericChangeEventCapable
  //

  @Override
  public IGenericChangeEventer genericChangeEventer() {
    return eventer;
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // To implement / override
  //

  /**
   * Реализация в наследниках должна заполнить контроли данными из aData.
   * <p>
   * При вызове этого метода аргумент aData уже запомнен и доступен методом {@link #dataRecordInput()}.
   * <p>
   * Допускается задавать значение null, например, в качестве признака, что отображать нечего.
   *
   * @param aData T - данные для отображения в контролях диалога
   */
  abstract protected void doSetDataRecord( T aData );

  /**
   * Реализация в наследниках должен создать новый объект из данных, которые находятся в дочерных контролях панели.
   * <p>
   * При вызове этого метода могут потребоваться данные, которые были заданы в конструкторе или
   * {@link #setDataRecord(Object)}, они доступны сметодом {@link #dataRecordInput()}.
   * <p>
   * Данный метод должен работать всегда, в том числе, в режиме "только-для-чтения", см. {@link #isPanelReadOnly()}.
   * <p>
   * Допускается возвращаеть <code>null</code> (например, для обозначения того, что данные не были введены).
   * <p>
   * Не вызвается, если {@link #validateData()} возвращает {@link EValidationResultType#ERROR}.
   *
   * @return T - данные диалога, находящейся в дочерных контролях панели или null
   */
  abstract protected T doGetDataRecord();

  /**
   * Subclass may perform data record validation.
   * <p>
   * Validation result will be displayed in dialog title area. {@link EValidationResultType#ERROR} disables "OK" button
   * if it presents in button bar.
   *
   * @return {@link ValidationResult} - validation result
   */
  protected ValidationResult doValidate() {
    return ValidationResult.SUCCESS;
  }

}
