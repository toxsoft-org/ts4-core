package org.toxsoft.tsgui.m5_1.impl.gui;

import static org.toxsoft.tsgui.m5_1.IM5Constants.*;
import static org.toxsoft.tsgui.m5_1.impl.gui.ITsResources.*;
import static org.toxsoft.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.impl.TsGuiContext;
import org.toxsoft.tsgui.graphics.EHorAlignment;
import org.toxsoft.tsgui.graphics.EVerAlignment;
import org.toxsoft.tsgui.m5.model.IM5AttributeFieldDef;
import org.toxsoft.tsgui.m5_1.*;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.gui.IM5EntityPanelWithValedsController;
import org.toxsoft.tsgui.m5_1.impl.M5BunchEdit;
import org.toxsoft.tsgui.panels.vecboard.*;
import org.toxsoft.tsgui.panels.vecboard.impl.*;
import org.toxsoft.tsgui.valed.api.*;
import org.toxsoft.tsgui.valed.impl.ValedControlFactoriesRegistry;
import org.toxsoft.tsgui.valed.impl.ValedControlUtils;
import org.toxsoft.tslib.av.EAtomicType;
import org.toxsoft.tslib.av.IAtomicValue;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.bricks.validator.impl.TsValidationFailedRtException;
import org.toxsoft.tslib.coll.primtypes.IStringMap;
import org.toxsoft.tslib.coll.primtypes.IStringMapEdit;
import org.toxsoft.tslib.coll.primtypes.impl.StringMap;
import org.toxsoft.tslib.utils.TsLibUtils;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Базовая реализация панели сущности, использующий редакторы {@link IValedControl}.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public class M5EntityPanelWithValeds<T>
    extends M5AbstractEntityPanel<T> {

  /**
   * Слушатель правок в редакторах полей.
   */
  private final IValedControlValueChangeListener valueEditorsListener = new IValedControlValueChangeListener() {

    @Override
    public void onEditorValueChanged( IValedControl<?> aSource, boolean aEditFinished ) {
      // сначала пусть обработает наследник, и если он разрешил обработку по умолчанию...
      IM5FieldDef<T, ?> fDef = findEditorFieldDef( aSource );
      if( doProcessEditorValueChange( aSource, fDef, aEditFinished ) ) {
        fireChangeEvent();
      }
      // TODO этот метод вызывается для обновления состояния редакторов, но может это
      // не совсем подходящий метод? может нужно иметь другой (типа как updateActionsState() в панели коллекции?)
      doEditableStateChanged();
    }

  };

  /**
   * Карта редакторов "ИД поля" - "редактор поля".
   */
  private final IStringMapEdit<IValedControl<?>> editors = new StringMap<>();

  /**
   * Панель с визуальными компонентами.
   */
  private final IVecBoard board = new VecBoard();

  /**
   * Панель с визуальными компонентами, инициализаируется в конструкторе.
   */
  private final IM5BunchEdit<T> currentValues;

  private final M5EntityPanelWithValedsController<T> controller;

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aModel {@link IM5Model} - модель
   * @param aViewer boolean - признак просмотрщика (панели только для просмотра)
   * @param aController {@link M5EntityPanelWithValedsController} - контроллер панели или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5EntityPanelWithValeds( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer,
      M5EntityPanelWithValedsController<T> aController ) {
    super( aContext, aModel, aViewer );
    if( aController != null ) {
      controller = aController;
    }
    else {
      controller = new M5EntityPanelWithValedsController<>();
    }
    currentValues = new M5BunchEdit<>( aModel );
    controller.setPanel( this );
  }

  /**
   * Конструктор.
   *
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aModel {@link IM5Model} - модель
   * @param aViewer boolean - признак просмотрщика (панели только для просмотра)
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public M5EntityPanelWithValeds( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    this( aContext, aModel, aViewer, null );
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  IM5Bunch<T> currentValues() {
    return currentValues;
  }

  // ------------------------------------------------------------------------------------
  // Реализация
  //

  /**
   * Возвращает описание поля, редактиромое запрошенным редактором.
   *
   * @param aEditor {@link IValedControl} - запрошенный редактор
   * @return {@link IM5FieldDef} - описание поля моделированного объекта, редактируемое аргументом
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException редактор не от этой панели
   */
  public IM5FieldDef<T, ?> findEditorFieldDef( IValedControl<?> aEditor ) {
    int index = editors.values().indexOf( aEditor );
    TsItemNotFoundRtException.checkTrue( index < 0 );
    String fieldId = editors.keys().get( index );
    return model().fieldDefs().getByKey( fieldId );
  }

  /**
   * Добавляет виджет просмотра значения поля.
   * <p>
   * Если такое поле уже добавлено, метод ничего не делает.
   *
   * @param aFieldId String - идентификатор одного из полей модели {@link IM5Model#fieldDefs()}
   * @return {@link IValedControl} - созданный или ранее существующий редактор поля
   * @throws TsNullArgumentRtException аргумент = null
   * @throws TsItemNotFoundRtException нет такого поля в модели
   */
  public IValedControl<?> addField( String aFieldId ) {
    IM5FieldDef<T, ?> fDef = model().fieldDefs().getByKey( aFieldId );
    IValedControl<?> e = editors.findByKey( aFieldId );
    if( e == null ) {
      e = createEditor( fDef );
      TsInternalErrorRtException.checkNull( e );
      e.setEditable( isEditable() );
      editors.put( aFieldId, e );
    }
    return e;
  }

  /**
   * Создает редактор для указанного поля.
   * <p>
   * Имя фабрики создаваемого редактора должно содержаться в параметре
   * {@link IValedControlConstants#OPID_EDITOR_FACTORY_NAME} набора {@link IM5FieldDef#params()}.
   *
   * @param aFieldDef {@link IM5FieldDef} - описания поля, для правки когорого создается редактор
   * @return {@link IValedControl} - созданный редактор
   */
  @SuppressWarnings( { "rawtypes" } )
  private IValedControl<?> createEditor( IM5FieldDef<T, ?> aFieldDef ) {
    TsNullArgumentRtException.checkNull( aFieldDef );
    // prepare context instance for VALED creation
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    // copy options and references from field def
    ctx.params().extendSet( aFieldDef.params() );
    if( aFieldDef.hasHint( M5FF_READ_ONLY ) || isViewer() ) {
      ctx.params().setBool( OPID_CREATE_UNEDITABLE, true );
    }
    for( String refId : aFieldDef.valedRefs().keys() ) {
      ctx.put( refId, aFieldDef.valedRefs().getByKey( refId ) );
    }
    REFDEF_M5_FIELD_DEF.setRef( ctx, aFieldDef ); // some valed may use field def
    // если редактор задан явно, то используем его
    if( aFieldDef.params().getValue( OPID_EDITOR_FACTORY_NAME ).isAssigned() ) {
      // необходим реестр фабрик
      ValedControlFactoriesRegistry registry = tsContext().get( ValedControlFactoriesRegistry.class );
      TsInternalErrorRtException.checkNull( registry, MSG_ERR_NO_FACTORIES_REGISTRY_IN_CONTEXT );
      // найдем фабрику редактора
      IValedControlFactory factory = null;
      String edName = aFieldDef.params().getStr( OPID_EDITOR_FACTORY_NAME );
      factory = registry.findFactory( edName );
      IValedControl editor = doCreateEditor( factory, aFieldDef, ctx );
      editor.clearValue();
      return editor;
    }
    //
    // An editor not found, starting heuristic algorithm to find suitable editor.
    // In the above context there may be an option OPID_EDITOR_FACTORY_NAME, we need to reset it.
    //
    OPDEF_EDITOR_FACTORY_NAME.setValue( ctx.params(), IAtomicValue.NULL );
    // an editor always may be found for attribute, using heuristic from ValedControlUtils
    if( aFieldDef instanceof IM5AttributeFieldDef ) {
      IM5AttributeFieldDef<?> afd = (IM5AttributeFieldDef<?>)aFieldDef;
      EAtomicType atomicType = afd.atomicType();
      IValedControlFactory factory = ValedControlUtils.guessAvEditorFactory( atomicType, ctx );
      IValedControl editor = doCreateEditor( factory, aFieldDef, ctx );
      editor.clearValue();
      return editor;
    }
    // попытка подобрать редактор по Java-классу значения
    IValedControlFactory factory = ValedControlUtils.guessRawEditorFactory( aFieldDef.valueClass(), ctx );
    if( factory != null ) {
      IValedControl editor = doCreateEditor( factory, aFieldDef, ctx );
      editor.clearValue();
      return editor;
    }
    // HERE другие эвристические правила создания редактора моделированных полей
    // не смогли ничего подобрать, увы...
    throw new TsInternalErrorRtException( FMT_ERR_NO_FACTORY_IN_PARAMS, aFieldDef.id() );
  }

  /**
   * Заменяет текст сообщения, добавляя информацию насчет поля объекта, вызвавшего ошибку.
   *
   * @param aVr {@link ValidationResult} - исходное сообщение
   * @param aFieldDef {@link IM5FieldDef} - поле, редактор которого вызвал ошибку/предупреждение
   * @return {@link ValidationResult} - сообщение с замененным текстом
   */
  public static ValidationResult repackFieldVr( ValidationResult aVr, IM5FieldDef<?, ?> aFieldDef ) {
    switch( aVr.type() ) {
      case OK:
        return ValidationResult.SUCCESS;
      case WARNING:
        return ValidationResult.warn( FMT_ERR_FIELD_VALIDATION_FAIL, aFieldDef.nmName(), aVr.message() );
      case ERROR:
        return ValidationResult.error( FMT_ERR_FIELD_VALIDATION_FAIL, aFieldDef.nmName(), aVr.message() );
      default:
        throw new TsNotAllEnumsUsedRtException();
    }
  }

  /**
   * Возвращает оснву, содержащий виджеты панели.
   *
   * @return {@link IVecBoard} 0 основа для виджетов панели
   */
  public IVecBoard board() {
    return board;
  }

  /**
   * Возвращает все редакторы панели.
   *
   * @return IStringMap&lt;{@link IValedControl}&gt; - карта "ИД поля" - "Редактор поля"
   */
  public IStringMap<IValedControl<?>> editors() {
    return editors;
  }

  /**
   * Пытается собрать значения полей с виджетов панели.
   *
   * @param aValues {@link IM5BunchEdit} - редактируемый набор для сбора значений
   * @return {@link ValidationResult} - результат сбора и валидации значений полей
   */
  @SuppressWarnings( { "rawtypes", "unchecked" } )
  private ValidationResult internalCollectValues( IM5BunchEdit<T> aValues ) {
    ValidationResult valResult = ValidationResult.SUCCESS;
    // соберем значения с всех виджетов, пропустив виджеты с ошибками
    for( int i = 0, count = editors.size(); i < count; i++ ) {
      IM5FieldDef fieldDef = model().fieldDefs().getByKey( editors.keys().get( i ) );
      // сначала проверим что можно считать значение из редактора и считаем его
      IValedControl e = editors.values().get( i );
      ValidationResult vr = e.canGetValue();
      switch( vr.type() ) {
        case OK:
          break;
        case WARNING: {
          valResult = ValidationResult.firstNonOk( valResult, repackFieldVr( vr, fieldDef ) );
          break;
        }
        case ERROR: {
          valResult = ValidationResult.firstNonOk( valResult, repackFieldVr( vr, fieldDef ) );
          break;
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
      // виджет с ошибкой пропускаем, не пытаемся считать его значение
      if( !valResult.isError() ) {
        aValues.set( fieldDef.id(), e.getValue() );
      }
    }
    // не со всех полей были собраны данные, выходим
    if( valResult.isError() ) {
      return valResult;
    }
    // проверим значение каждого поля валидатором поля
    for( IM5FieldDef fieldDef : model().fieldDefs() ) {
      ValidationResult vr = fieldDef.validator().validate( aValues.get( fieldDef ) );
      switch( vr.type() ) {
        case OK:
          break;
        case WARNING: {
          valResult = ValidationResult.firstNonOk( valResult, repackFieldVr( vr, fieldDef ) );
          break;
        }
        case ERROR: {
          return repackFieldVr( vr, fieldDef );
        }
        default:
          throw new TsNotAllEnumsUsedRtException();
      }
    }
    return valResult;
  }

  // ------------------------------------------------------------------------------------
  // Переопределение методов
  //

  @Override
  protected Control doCreateControl( Composite aParent ) {
    ScrolledComposite background = new ScrolledComposite( aParent, SWT.V_SCROLL | SWT.H_SCROLL );
    background.setExpandHorizontal( true );
    background.setExpandVertical( true );
    doInitEditors();
    doInitLayout();
    background.setLayout( new FillLayout() );
    Composite c = board.createControl( background );
    background.setContent( c );
    background.setMinSize( c.computeSize( SWT.DEFAULT, SWT.DEFAULT ) );
    // слушаем изменения в редакторах
    for( IValedControl<?> e : editors ) {
      e.eventer().addListener( valueEditorsListener );
    }
    controller.afterEditorsCreated();
    doEditableStateChanged();
    return background;
  }

  @Override
  protected void doEditableStateChanged() {
    // установим состояние разрешения редактированияb всех редакторов полей
    for( IM5FieldDef<?, ?> fd : model().fieldDefs() ) {
      IValedControl<?> ve = editors.findByKey( fd.id() );
      if( ve != null ) {
        boolean fieldEditable = fd.hasHint( M5FF_READ_ONLY ) && !ve.params().getBool( OPID_CREATE_UNEDITABLE );
        // инвариантное поле в режиме редактирования надо запретить
        if( fieldEditable ) {
          if( fd.hasHint( M5FF_INVARIANT ) ) {
            if( currentValues.originalEntity() != null ) {
              fieldEditable = false;
            }
          }
        }
        ve.setEditable( fieldEditable & isEditable() );
      }
    }
    controller.onEditableStateChanged( isEditable() );
  }

  // ------------------------------------------------------------------------------------
  // Для переопределения
  //

  /**
   * Наследник может переопределить создание редакторов по умолчанию.
   * <p>
   * Предполагается, что наследники создают редакторы методам {@link #addField(String)} где-то между конструктором и
   * вызовом {@link #doCreateControl(Composite)}. Если к моменту создания виджета панели ни один редактор не был создан,
   * вызвается этот метод.
   * <p>
   * В базовом классе создает редакторы всех полей, у которы <b>не установлен</b> признак
   * {@link IM5Constants#M5FF_HIDDEN}.
   * <p>
   * При переопределении в этом методе можно создать как редакторы, так и задать раскладку.
   */
  protected void doInitEditors() {
    // создает редакторы всех полей без признака M5FF_HIDDEN
    for( IM5FieldDef<T, ?> fDef : model().fieldDefs() ) {
      if( (fDef.hints() & M5FF_HIDDEN) == 0 ) {
        addField( fDef.id() );
      }
    }
  }

  /**
   * Наследник может определить переопределить алгоритм создания раскладки по умолчанию.
   * <p>
   * Предполагается, что наследники создают одну из раскладок {@link IVecLayout}, и задают панели {@link #board} методом
   * {@link IVecBoard#setLayout(IVecLayout)} где-то между конструктором и вызовом {@link #doCreateControl(Composite)}.
   * Если к моменту создания виджета панели раскладлка не задана (то есть, {@link IVecBoard#getLayout()} ==
   * <code>null</code>), то вызвается этот метод.
   * <p>
   * В базовом классе создает раскладку типа {@link EVecLayoutKind#LADDER}.
   */
  protected void doInitLayout() {
    board.setLayout( makeLadderLayout( model(), editors ) );
  }

  @Override
  protected void doSetValues( IM5Bunch<T> aBunch ) {
    if( aBunch != null ) {
      currentValues.fillFrom( aBunch, true );
    }
    else {
      currentValues.clear();
    }
    controller.beforeSetValues( aBunch );
    // задаем значения в контроли
    for( String fieldId : editors.keys() ) {
      @SuppressWarnings( "unchecked" )
      IValedControl<Object> e = (IValedControl<Object>)editors.getByKey( fieldId );
      try {
        Object val = currentValues.get( fieldId );
        e.setValue( val );
      }
      catch( Exception ex ) {
        throw new TsInternalErrorRtException( ex, FMT_CANT_SET_FIELD_VALUE, fieldId, ex.getMessage() );
      }
    }
    controller.afterSetValues( aBunch );
  }

  @Override
  protected ValidationResult doCanGetValues() {
    ValidationResult vr = internalCollectValues( currentValues );
    if( vr.isError() ) {
      return vr;
    }
    return controller.canGetValues( currentValues, vr );
  }

  @Override
  protected IM5Bunch<T> doGetValues() {
    TsValidationFailedRtException.checkError( doCanGetValues() );
    // internalCollectValues( currentValues ); - уже собраны в doCanGetValues()
    return currentValues.getBunch();
  }

  /**
   * Наследник может определеить специальные действия при редактировании значений.
   * <p>
   * Вызывается, когда пользователь изменил значение в редакторе и соответственно, нет необходимости вручную ставить
   * слушателей редаторам индивидуальных полей.
   * <p>
   * Данный метод своим возвращаемым значением может определить, нужно ли вызывать внешний слушатель класса
   * {@link IGenericChangeListener}. Поскольку контроллер может игнорировать ввод пользователя например, вернуть
   * значение в поле к предыдущему значению.
   * <p>
   * В базовом классе возвращает результат вызова контроллера
   * {@link IM5EntityPanelWithValedsController#doProcessEditorValueChange(IValedControl, IM5FieldDef, boolean)}
   *
   * @param aEditor {@link IValedControl} - редактор, в котором пользователь изменил значение
   * @param aFieldDef {@link IM5FieldDef} - описание редактируемого поля модели
   * @param aEditFinished boolean - признак завершения редактирования (ввода значения)
   * @return boolean - нужно ли извещать вне панели об изменении в поле ввода<br>
   *         <b>true</b> - да, панель вызовет внешнего слушателя {@link IGenericChangeListener};<br>
   *         <b>false</b> - нет, информация о пользовательских действиях останется тайной панели.
   */
  public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<T, ?> aFieldDef,
      boolean aEditFinished ) {
    return controller.doProcessEditorValueChange( aEditor, aFieldDef, aEditFinished );
  }

  /**
   * Наследник может подправить параметры и вообще, процедуру создания редактора поля.
   * <p>
   * Этот метод вызывается из {@link #createEditor(IM5FieldDef)} непосредственно для создания редактора.
   * <p>
   * В базовом классе возвращает редактор, созданный вызовом метода контроллера
   * {@link IM5EntityPanelWithValedsController#doCreateEditor(IValedControlFactory, IM5FieldDef, ITsGuiContext)}.
   *
   * @param aFactory {@link IValedControlFactory} - фабрика для создания редактора
   * @param aFieldDef {@link IM5FieldDef} - описание поля, для которого создается редактор
   * @param aContext {@link ITsGuiContext} - индивидуальный экземпляр контекста редактора
   * @return {@link IValedControl} - созданный редактор, не может быть null
   */
  @SuppressWarnings( { "rawtypes" } )
  protected IValedControl doCreateEditor( IValedControlFactory aFactory, IM5FieldDef<T, ?> aFieldDef,
      ITsGuiContext aContext ) {
    return controller.doCreateEditor( aFactory, aFieldDef, aContext );
  }

  // ------------------------------------------------------------------------------------
  // API класса
  //

  /**
   * Создает лестничную раскладлку указанных редакторов.
   *
   * @param aModel {@link IM5Model} - модель
   * @param aEditors {@link IStringMap}&lt;{@link IValedControl}&gt; - карта "ИД поля" - "редактор" (части) полей моедли
   * @return {@link IVecLadderLayout} - созданная раскладка
   */
  public static IVecLadderLayout makeLadderLayout( IM5Model<?> aModel, IStringMap<IValedControl<?>> aEditors ) {
    IVecLadderLayout ll = new VecLadderLayout( true );
    for( String fieldId : aEditors.keys() ) {
      IValedControl<?> varEditor = aEditors.getByKey( fieldId );
      boolean isPrefWidthFixed = varEditor.params().getBool( IValedControlConstants.OPDEF_IS_WIDTH_FIXED );
      boolean isPrefHeighFixed = varEditor.params().getBool( IValedControlConstants.OPDEF_IS_HEIGHT_FIXED );
      int verSpan = varEditor.params().getInt( IValedControlConstants.OPDEF_VERTICAL_SPAN );
      boolean useLabel = !varEditor.params().getBool( IValedControlConstants.OPDEF_NO_FIELD_LABEL );
      EHorAlignment horAl = isPrefWidthFixed ? EHorAlignment.LEFT : EHorAlignment.FILL;
      EVerAlignment verAl = isPrefHeighFixed ? EVerAlignment.TOP : EVerAlignment.FILL;
      IM5FieldDef<?, ?> fd = aModel.fieldDefs().getByKey( fieldId );
      String label = TsLibUtils.EMPTY_STRING;
      if( !fd.nmName().isEmpty() ) {
        label = fd.nmName() + ": "; //$NON-NLS-1$
      }
      String tooltip = fd.description();
      IVecLadderLayoutData layoutData =
          new VecLadderLayoutData( useLabel, false, verSpan, label, tooltip, horAl, verAl );
      ll.addControl( varEditor, layoutData );
    }
    return ll;
  }

}
