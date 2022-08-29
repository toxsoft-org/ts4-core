package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор значений типа {@link RGBA} в виде нередактируемой строки текста с кнопкой вызова диалога выбора.
 * <p>
 * Редактор возвращает <code>null</code> если цвет не задан, рассматривая это как "цвет по умолчанию".
 *
 * @author goga
 * @author vs
 */
public class ValedSimpleRgba
    extends AbstractValedSimpleRgba<RGBA> {

  /**
   * The factory class.
   *
   * @author hazard157
   */
  public static class Factory
      extends AbstractValedControlFactory {

    /**
     * Constructor.
     */
    public Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<RGBA> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSimpleRgba( aContext );
    }

  }

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SimpleRgba"; //$NON-NLS-1$

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Конструкторe.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  ValedSimpleRgba( ITsGuiContext aContext ) {
    super( aContext );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected RGBA doGetUnvalidatedValue() {
    return getRgba();
  }

  @Override
  protected void doSetUnvalidatedValue( RGBA aValue ) {
    setRgba( aValue );
  }

}