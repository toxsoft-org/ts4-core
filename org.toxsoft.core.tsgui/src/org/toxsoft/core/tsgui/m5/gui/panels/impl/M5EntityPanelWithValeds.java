package org.toxsoft.core.tsgui.m5.gui.panels.impl;

import static org.toxsoft.core.tsgui.m5.IM5Constants.*;
import static org.toxsoft.core.tsgui.m5.gui.panels.impl.ITsResources.*;
import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.valeds.*;
import org.toxsoft.core.tsgui.panels.vecboard.*;
import org.toxsoft.core.tsgui.panels.vecboard.impl.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.bricks.events.change.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IM5EntityPanel} implementation based on auto-created {@link IValedControl} editors.
 * <p>
 * Thois panle has a backplane {@link #board()} and field VALEDs on it. Optional decorating widgets may also be placed
 * on this panel. However, panel creates {@link IM5Bunch} result based only on values from {@link #editors()}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5EntityPanelWithValeds<T>
    extends M5AbstractEntityPanel<T> {

  /**
   * Listens to the edit events in the VALEDs of this panel.
   */
  private final IValedControlValueChangeListener valueEditorsListener = ( aSource, aEditFinished ) -> {
    IM5FieldDef<T, ?> fDef = findEditorFieldDef( aSource );
    if( doProcessEditorValueChange( aSource, fDef, aEditFinished ) ) {
      fireChangeEvent();
    }
    // calling this method allows to update panel widgets states
    doEditableStateChanged();
  };

  /**
   * Field value editors as map "field ID" - "field VALED"..
   */
  private final IStringMapEdit<IValedControl<?>> editors = new StringMap<>();

  /**
   * The backplane board.
   */
  private final IVecBoard board = new VecBoard();

  /**
   * Panel controller, if not set by client it will be initialized as {@link M5EntityPanelWithValedsController}.
   */
  private final M5EntityPanelWithValedsController<T> controller;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - the M5-model of entity
   * @param aViewer boolean - flags viewer (read-only) mode of panel to be created
   * @param aController {@link M5EntityPanelWithValedsController} - the controller or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
    controller.papiSetPanel( this );
  }

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - the context
   * @param aModel {@link IM5Model} - entity model
   * @param aViewer boolean - flags viewer (read-only) mode of panel to be created
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public M5EntityPanelWithValeds( ITsGuiContext aContext, IM5Model<T> aModel, boolean aViewer ) {
    this( aContext, aModel, aViewer, null );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Creates editor for specified field.
   * <p>
   * First finds the editor factory and then crates the editor. Editor factory may be specified in option
   * {@link IValedControlConstants#OPDEF_EDITOR_FACTORY_NAME} of field {@link IM5FieldDef#params()}. If not specified
   * method uses heuristic knowledge from {@link ValedControlUtils} and tries to puck up an editor factory. If factory
   * can not be found throws an exception {@link TsItemNotFoundRtException}.
   *
   * @param aFieldDef {@link IM5FieldDef} - the field
   * @return {@link IValedControl} - created editor
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException editor factory can not be found
   */
  @SuppressWarnings( { "rawtypes" } )
  private IValedControl<?> createEditor( IM5FieldDef<T, ?> aFieldDef ) {
    TsNullArgumentRtException.checkNull( aFieldDef );
    // each editor needs own instance of the context
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    ctx.params().addAll( aFieldDef.params() );
    for( String refId : aFieldDef.valedRefs().keys() ) {
      ctx.put( refId, aFieldDef.valedRefs().getByKey( refId ) );
    }
    if( ((aFieldDef.flags() & M5FF_READ_ONLY) != 0) || isViewer() ) {
      ctx.params().setBool( OPDEF_CREATE_UNEDITABLE, true );
    }
    IM5ValedConstants.M5_VALED_REFDEF_FIELD_DEF.setRef( ctx, aFieldDef );
    // use explicitly specified VALED
    IAtomicValue avEdName = aFieldDef.params().findValue( OPDEF_EDITOR_FACTORY_NAME );
    if( avEdName != null && avEdName != IAtomicValue.NULL ) {
      // необходим реестр фабрик
      IValedControlFactoriesRegistry registry = tsContext().get( IValedControlFactoriesRegistry.class );
      // найдем фабрику редактора
      IValedControlFactory factory = null;
      String edName = avEdName.asString();
      factory = registry.getFactory( edName );
      IValedControl editor = doCreateEditor( factory, aFieldDef, ctx );
      editor.clearValue();
      return editor;
    }
    //
    // GOGA 2020-11-24 в контексте уровнем выше может быть EDITOR_FACTORY_NAME, обнулим для себя
    // reset OPDEF_EDITOR_FACTORY_NAME option if it is defined

    /**
     * At this point #ctx does NOT contains OPDEF_EDITOR_FACTORY_NAME option from the field definition. However,
     * somewhere in the parent contexts there may be the same option defined. To avoid misunderstanding we'll "hide"
     * parent OPDEF_EDITOR_FACTORY_NAME option.
     */
    OPDEF_EDITOR_FACTORY_NAME.setValue( ctx.params(), IAtomicValue.NULL );
    // for the attributes an IAtmicValue editor always may be found
    if( aFieldDef instanceof IM5AttributeFieldDef afd ) {
      EAtomicType atomicType = afd.atomicType();
      IValedControlFactory factory = ValedControlUtils.guessAvEditorFactory( atomicType, ctx );
      IValedControl editor = doCreateEditor( factory, aFieldDef, ctx );
      editor.clearValue();
      return editor;
    }
    // use ValedControlUtils heuristics to find VALED for the field
    IValedControlFactory factory = ValedControlUtils.guessRawEditorFactory( aFieldDef.valueClass(), ctx );
    if( factory != null ) {
      IValedControl editor = doCreateEditor( factory, aFieldDef, ctx );
      editor.clearValue();
      return editor;
    }
    // heuritics faild, we can't create fiedl editor
    throw new TsItemNotFoundRtException( FMT_ERR_NO_FACTORY_IN_PARAMS, aFieldDef.id() );
  }

  // TODO TRANSLATE

  /**
   * Method tries to collect values from VALEDs in the argument bunch.
   * <p>
   * If error is returned the reason may be either VALED which fails {@link IValedControl#canGetValue()} or field that
   * fails {@link IM5FieldDef#validator()} validation.
   *
   * @param aValues {@link IM5BunchEdit} - the bunch to put values to
   * @return {@link ValidationResult} - the result of collecing and validating values
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
      Object fieldVal = aValues.get( fieldDef );
      ValidationResult vr = fieldDef.validator().validate( fieldVal );
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
   * Заменяет текст сообщения, добавляя информацию насчет поля объекта, вызвавшего ошибку.
   *
   * @param aVr {@link ValidationResult} - исходное сообщение
   * @param aFieldDef {@link IM5FieldDef} - поле, редактор которого вызвал ошибку/предупреждение
   * @return {@link ValidationResult} - сообщение с замененным текстом
   */
  public static ValidationResult repackFieldVr( ValidationResult aVr, IM5FieldDef<?, ?> aFieldDef ) {
    return switch( aVr.type() ) {
      case OK -> ValidationResult.SUCCESS;
      case WARNING -> ValidationResult.warn( FMT_ERR_FIELD_VALIDATION_FAIL, aFieldDef.nmName(), aVr.message() );
      case ERROR -> ValidationResult.error( FMT_ERR_FIELD_VALIDATION_FAIL, aFieldDef.nmName(), aVr.message() );
      default -> throw new TsNotAllEnumsUsedRtException();
    };
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
   * Returns all editors in panel.
   *
   * @return IStringMap&lt;{@link IValedControl}&gt; - the map "field ID" - "field editor"
   */
  public IStringMap<IValedControl<?>> editors() {
    return editors;
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
        boolean fieldEditable = ((fd.flags() & M5FF_READ_ONLY) == 0) && !ve.params().getBool( OPDEF_CREATE_UNEDITABLE );
        // инвариантное поле в режиме редактирования надо запретить
        if( fieldEditable ) {
          if( (fd.flags() & M5FF_INVARIANT) != 0 ) {
            if( lastValues().originalEntity() != null ) {
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
    // create editors for unhidden (not M5FF_HIDDEN) fields
    for( IM5FieldDef<T, ?> fDef : model().fieldDefs() ) {
      if( !fDef.hasFlag( M5FF_HIDDEN ) ) {
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
    controller.beforeSetValues( aBunch );
    // задаем значения в контроли
    for( String fieldId : editors.keys() ) {
      @SuppressWarnings( "unchecked" )
      IValedControl<Object> e = (IValedControl<Object>)editors.getByKey( fieldId );
      try {
        Object val = lastValues().get( fieldId );
        e.setValue( val );
      }
      catch( Exception ex ) {
        throw new TsInternalErrorRtException( ex, FMT_CANT_SET_FIELD_VALUE, fieldId, ex.getMessage() );
      }
    }
    controller.afterSetValues( aBunch );
  }

  @Override
  protected ValidationResult doCollectValues( IM5BunchEdit<T> aBunch ) {
    ValidationResult vr = internalCollectValues( aBunch );
    if( vr.isError() ) {
      return vr;
    }
    return controller.canGetValues( aBunch, vr );
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
   * @param aEditorContext {@link ITsGuiContext} - индивидуальный экземпляр контекста редактора
   * @return {@link IValedControl} - созданный редактор, не может быть null
   */
  @SuppressWarnings( { "rawtypes" } )
  protected IValedControl doCreateEditor( IValedControlFactory aFactory, IM5FieldDef<T, ?> aFieldDef,
      ITsGuiContext aEditorContext ) {
    return controller.doCreateEditor( aFactory, aFieldDef, aEditorContext );
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
      int verSpan = varEditor.params().getInt( OPDEF_VERTICAL_SPAN );
      boolean isPrefWidthFixed = varEditor.params().getBool( OPDEF_IS_WIDTH_FIXED );
      boolean isPrefHeighFixed = varEditor.params().getBool( OPDEF_IS_HEIGHT_FIXED );
      boolean useLabel = !varEditor.params().getBool( OPDEF_NO_FIELD_LABEL );
      EHorAlignment ha = isPrefWidthFixed ? EHorAlignment.LEFT : EHorAlignment.FILL;
      EVerAlignment va = isPrefHeighFixed ? EVerAlignment.TOP : EVerAlignment.FILL;
      IM5FieldDef<?, ?> fd = aModel.fieldDefs().getByKey( fieldId );
      String label = TsLibUtils.EMPTY_STRING;
      if( !fd.nmName().isEmpty() ) {
        label = fd.nmName() + ": "; //$NON-NLS-1$
      }
      String tooltip = fd.description();

      // TODO need option OPDEF_FULL_WIDTH_CONTROL ???

      IVecLadderLayoutData layoutData = new VecLadderLayoutData( useLabel, !useLabel, verSpan, label, tooltip, ha, va );
      ll.addControl( varEditor, layoutData );
    }
    return ll;
  }

}
