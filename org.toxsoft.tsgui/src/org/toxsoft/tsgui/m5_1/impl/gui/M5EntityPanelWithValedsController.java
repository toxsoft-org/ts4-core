package org.toxsoft.tsgui.m5_1.impl.gui;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5_1.api.*;
import org.toxsoft.tsgui.m5_1.gui.*;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.api.IValedControlFactory;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.primtypes.IStringMap;
import org.toxsoft.tslib.utils.errors.*;

/**
 * Контроллер панели, использующий редакторы {@link IValedControl} для правки полей сущности.
 *
 * @author goga
 * @param <T> - класс моделированых сущностей, отображаемых в панели
 */
public class M5EntityPanelWithValedsController<T>
    implements IM5EntityPanelWithValedsController<T> {

  private M5EntityPanelWithValeds<T> panel = null;

  /**
   * Пустой конструктор.
   * <p>
   * Внимание: в конструкторе не следует вызвать методы этого класса, поскольку внутренности инициализируются после
   * конструктора.
   */
  public M5EntityPanelWithValedsController() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // API пакета
  //

  final void setPanel( M5EntityPanelWithValeds<T> aPanel ) {
    panel = aPanel;
  }

  // ------------------------------------------------------------------------------------
  // API для наследников
  //

  /**
   * Возвращает панель, которым управляет контроллер.
   *
   * @return {@link IM5EntityEditPanel} - панель, которым управляет контроллер
   */
  final public IM5EntityEditPanel<T> panel() {
    TsIllegalStateRtException.checkNull( panel );
    return panel;
  }

  /**
   * Возвращает все редакторы панели.
   *
   * @return IStringMap&lt;{@link IValedControl}&gt; - карта "ИД поля" - "Редактор поля"
   */
  final public IStringMap<IValedControl<?>> editors() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.editors();
  }

  /**
   * Находит редактор указанного типа.
   *
   * @param <E> - тип редактора
   * @param aFieldId String - идентификатор поля
   * @param aEditorClass {@link Class} - тип (класс) редактора
   * @return &lt;E&gt; - найдленный редактор или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  final public <E extends IValedControl<?>> E findEditor( String aFieldId, Class<E> aEditorClass ) {
    TsNullArgumentRtException.checkNull( aEditorClass );
    IValedControl<?> editor = editors().findByKey( aFieldId );
    if( editor == null ) {
      return null;
    }
    if( !aEditorClass.isInstance( editor ) ) {
      return null;
    }
    return aEditorClass.cast( editor );
  }

  /**
   * Возвращает редактор указанного типа.
   *
   * @param <E> - тип редактора
   * @param aFieldId String - идентификатор поля
   * @param aEditorClass {@link Class} - тип (класс) редактора
   * @return &lt;E&gt; - найдленный редактор
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет редактора такого поля
   * @throws ClassCastException редактор имеет не запрошенный тип
   */
  @SuppressWarnings( "rawtypes" )
  final public <E extends IValedControl> E getEditor( String aFieldId, Class<E> aEditorClass ) {
    TsNullArgumentRtException.checkNull( aEditorClass );
    IValedControl<?> editor = editors().getByKey( aFieldId );
    return aEditorClass.cast( editor );
  }

  /**
   * Возвращает набор текущих значений, содержащейся в редакторах.
   * <p>
   * Значения обновляются во время и после каждой правки в редакторах.
   * <p>
   * Внимание: если значение из редактора не может быть извлечено (то есть {@link IValedControl#canGetValue()}
   * возвращает ошибку), то в наборе будет последнее значение такого поля.
   *
   * @return {@link IM5Bunch}&lt;T&gt; - набор текущих значений полей
   */
  final public IM5Bunch<T> currentValues() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.currentValues();
  }

  /**
   * Возвращает контекст панели.
   *
   * @return {@link ITsGuiContext} - контекст приложения
   */
  final public ITsGuiContext tsContext() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.tsContext();
  }

  /**
   * Возвращает модель объектов.
   *
   * @return {@link IM5Model} - модель объектов
   */
  final public IM5Model<T> model() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.model();
  }

  /**
   * Определяет, является ли панель только просмотрщиком, или позволяет также редактировать данные.
   * <p>
   * Возвращает то же, что и {@link IM5EntityPanel#isViewer()}.
   *
   * @return boolean - признак только просмотрщика
   */
  final public boolean isViewer() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.isViewer();
  }

  /**
   * Определяет, разрешено ли редактирование в панели.
   * <p>
   * Для панели просмотрщика {@link #isViewer()}=<code>true</code> всегда возвращает <code>false</code>.
   *
   * @return boolean - признак разрешения редактирования
   */
  final public boolean isEditable() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.isEditable();
  }

  // ------------------------------------------------------------------------------------
  // Метод IM5EntityPanelWithValedsController для переопределения наследниками
  //

  @Override
  public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<T, ?> aFieldDef,
      boolean aEditFinished ) {
    return true;
  }

  @Override
  public IValedControl<?> doCreateEditor( IValedControlFactory aFactory, IM5FieldDef<T, ?> aFieldDef,
      ITsGuiContext aContext ) {
    return aFactory.createEditor( aContext );
  }

  @Override
  public ValidationResult canGetValues( IM5BunchEdit<T> aValues, ValidationResult aPreResult ) {
    return aPreResult;
  }

  @Override
  public void afterEditorsCreated() {
    // nop
  }

  @Override
  public void beforeSetValues( IM5Bunch<T> aValues ) {
    // nop
  }

  @Override
  public void afterSetValues( IM5Bunch<T> aValues ) {
    // nop
  }

  @Override
  public void onEditableStateChanged( boolean aEditable ) {
    // nop
  }

}
