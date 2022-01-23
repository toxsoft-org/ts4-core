package org.toxsoft.tsgui.m5_3.gui.panels.impl;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.bricks.ctx.ITsGuiContextable;
import org.toxsoft.tsgui.m5_3.*;
import org.toxsoft.tsgui.m5_3.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.api.IValedControlFactory;
import org.toxsoft.tslib.bricks.events.change.IGenericChangeListener;
import org.toxsoft.tslib.bricks.validator.EValidationResultType;
import org.toxsoft.tslib.bricks.validator.ValidationResult;

// TODO TRANSLATE

/**
 * Контроллер панели, использующий редакторы {@link IValedControl} для правки полей сущности.
 * <p>
 * В этом интерфейсе перечислены методы, которые вызываются из панели за время жизненного цикла панели.
 * <p>
 * Реализация контроллера обязана наследоваться от {@link M5EntityPanelWithValedsController}.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public interface IM5EntityPanelWithValedsController<T>
    extends ITsGuiContextable {

  // FIXME doInitEditors() doInitLayout()

  /**
   * Implementation must create an editor for the specified field.
   *
   * @param aFactory {@link IValedControlFactory} - the factory found for the field
   * @param aFieldDef {@link IM5FieldDef} - the field
   * @param aEditorContext {@link ITsGuiContext} - context instance created for the editor
   * @return {@link IValedControl} - created editor must not be <code>null</code>
   */
  IValedControl<?> doCreateEditor( IValedControlFactory aFactory, IM5FieldDef<T, ?> aFieldDef,
      ITsGuiContext aEditorContext );

  /**
   * Реализация может осуществить дополнительную инициализацию.
   * <p>
   * Вызывается после создания редакторов (завершения создания панели и виджетов).
   */
  void afterEditorsCreated();

  /**
   * Реализация может определеить специальные действия при редактировании значений.
   * <p>
   * Вызывается, когда пользователь изменил значение в редакторе и соответственно, нет необходимости вручную ставить
   * слушателей редаторам индивидуальных полей.
   * <p>
   * Данный метод своим возвращаемым значением может определить, нужно ли вызывать внешний слушатель класса
   * {@link IGenericChangeListener}. Поскольку контроллер может игнорировать ввод пользователя например, вернуть
   * значение в поле к предыдущему значению.
   *
   * @param aEditor {@link IValedControl} - редактор, в котором пользователь изменил значение
   * @param aFieldDef {@link IM5FieldDef} - описание редактируемого поля модели
   * @param aEditFinished boolean - признак завершения редактирования (ввода значения)
   * @return boolean - нужно ли извещать вне панели об изменении в поле ввода<br>
   *         <b>true</b> - да, панель вызовет внешнего слушателя {@link IGenericChangeListener};<br>
   *         <b>false</b> - нет, информация о пользовательских действиях останется тайной панели.
   */
  boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<T, ?> aFieldDef, boolean aEditFinished );

  /**
   * Релизация может осуществить дополнительную проверку корректности введенных данных.
   * <p>
   * Вызывается из {@link IM5EntityPanel#canGetValues()} непосредсвенно перед возвращаением значения, после
   * осуществления встроенных проверок. Если встроенная проверка обнаруживает ошибку
   * {@link EValidationResultType#ERROR}, то этот метод не вызывается.
   * <p>
   * Аргумент aValues является редактируемым. Это используется при извлечении значений из панели. Вызов
   * {@link IM5EntityPanel#getValues()} начинается из вызова {@link IM5EntityPanel#canGetValues()} и, соответственно,
   * вызова этого метода. Если реализация изменит значения в наборе, то измененные значения будут возвращены из панели.
   *
   * @param aValues {@link IM5BunchEdit} - редактируемый набор значений
   * @param aPreResult {@link ValidationResult} - результат встроенных проверок, {@link EValidationResultType#OK} или
   *          {@link EValidationResultType#WARNING}
   * @return {@link ValidationResult} - результат проверки
   */
  ValidationResult canGetValues( IM5BunchEdit<T> aValues, ValidationResult aPreResult );

  /**
   * Наследник может осуществить дополнительные действия перед заданием извне значений полей сущности.
   * <p>
   * Вызывается в в начале метода {@link IM5EntityPanel#setValues(IM5Bunch)}, перед там, как контрлям в панели будут
   * заданы новые значения.
   *
   * @param aValues {@link IM5Bunch}&lt;T&gt; - задаваемые значения
   */
  void beforeSetValues( IM5Bunch<T> aValues );

  /**
   * Наследник может осуществить дополнительные действия сразу после задания извне значений полей сущности.
   * <p>
   * Вызывается в самом конце метода {@link IM5EntityPanel#setValues(IM5Bunch)}.
   *
   * @param aValues {@link IM5Bunch}&lt;T&gt; - задаваемые значения
   */
  void afterSetValues( IM5Bunch<T> aValues );

  /**
   * Наследник может предпринять дополнительные действия при изменении состояния редактируемости
   * {@link IM5EntityPanel#isEditable()}.
   * <p>
   * Обратите внимание, что метод может вызываться также для просмотрщиков {@link IM5EntityPanel#isViewer()} =
   * <code>true</code>.
   *
   * @param aEditable boolean - признак разрешения редактирования
   */
  void onEditableStateChanged( boolean aEditable );

}
