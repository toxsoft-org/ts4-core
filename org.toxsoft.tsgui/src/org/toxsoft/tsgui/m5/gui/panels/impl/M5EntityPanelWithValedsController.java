package org.toxsoft.tsgui.m5.gui.panels.impl;

import org.toxsoft.tsgui.bricks.ctx.ITsGuiContext;
import org.toxsoft.tsgui.m5.*;
import org.toxsoft.tsgui.m5.gui.panels.IM5EntityPanel;
import org.toxsoft.tsgui.valed.api.IValedControl;
import org.toxsoft.tsgui.valed.api.IValedControlFactory;
import org.toxsoft.tslib.bricks.validator.ValidationResult;
import org.toxsoft.tslib.coll.primtypes.IStringMap;
import org.toxsoft.tslib.utils.errors.*;

/**
 * {@link IM5EntityPanelWithValedsController} base implementation.
 *
 * @author hazard157
 * @param <T> - modelled entity type
 */
public class M5EntityPanelWithValedsController<T>
    implements IM5EntityPanelWithValedsController<T> {

  // TODO TRANSLATE

  private M5EntityPanelWithValeds<T> panel = null;

  /**
   * Constructor.
   * <p>
   * Sublcass constructors must not call methods of this class, override {@link #doInit()} instead.
   */
  public M5EntityPanelWithValedsController() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ITsGuiContextable
  //

  @Override
  final public ITsGuiContext tsContext() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  final void papiSetPanel( M5EntityPanelWithValeds<T> aPanel ) {
    panel = aPanel;
  }

  // ------------------------------------------------------------------------------------
  // API for subclasses
  //

  /**
   * Returns the panel controlled by this class.
   *
   * @return {@link IM5EntityPanel} - controlled panel
   * @throws TsIllegalStateRtException method is called before instance was initialized with {@link #doInit()}
   */
  final public IM5EntityPanel<T> panel() {
    TsIllegalStateRtException.checkNull( panel );
    return panel;
  }

  /**
   * Returns all editors in panel.
   *
   * @return IStringMap&lt;{@link IValedControl}&gt; - the map "field ID" - "field editor"
   * @throws TsIllegalStateRtException method is called before instance was initialized with {@link #doInit()}
   */
  final public IStringMap<IValedControl<?>> editors() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.editors();
  }

  /**
   * Finds editor with type check.
   * <p>
   * Returns <code>null</code> if editor exsits but is not of expected type.
   *
   * @param <E> - expected class of the editor
   * @param aFieldId String - the field ID
   * @param aEditorClass {@link Class} - class of the editor
   * @return &lt;E&gt; - found editor or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
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
   * Returns editor with type check.
   * <p>
   * Throws an exception if editor exsits but is not of expected type.
   *
   * @param <E> - expected class of the editor
   * @param aFieldId String - the field ID
   * @param aEditorClass {@link Class} - class of the editor
   * @return &lt;E&gt; - found editor or <code>null</code>
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no editor for specified field
   * @throws ClassCastException editor is not of expected kind
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
  final public IM5Bunch<T> lastValues() {
    TsInternalErrorRtException.checkNull( panel );
    return panel.lastValues();
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

  protected void doInit() {
    // nop
  }

  @Override
  public boolean doProcessEditorValueChange( IValedControl<?> aEditor, IM5FieldDef<T, ?> aFieldDef,
      boolean aEditFinished ) {
    return true;
  }

  @Override
  public IValedControl<?> doCreateEditor( IValedControlFactory aFactory, IM5FieldDef<T, ?> aFieldDef,
      ITsGuiContext aEditorContext ) {
    return aFactory.createEditor( aEditorContext );
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
