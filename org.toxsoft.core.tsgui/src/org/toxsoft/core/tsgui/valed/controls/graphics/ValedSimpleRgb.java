package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор значений типа {@link RGB} в виде нередактируемой строки текста с кнопкой вызова диалога выбора.
 * <p>
 * Редактор возвращает <code>null</code> если цвет не задан, рассматривая это как "цвет по умолчанию".
 *
 * @author hazard157
 */
public class ValedSimpleRgb
    extends AbstractValedSimpleRgb<RGB> {

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
    protected IValedControl<RGB> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSimpleRgb( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( RGB.class );
    }

  }

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SimpleRgb"; //$NON-NLS-1$

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
  ValedSimpleRgb( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected RGB doGetUnvalidatedValue() {
    return getRgb();
  }

  @Override
  protected void doDoSetUnvalidatedValue( RGB aValue ) {
    setRgb( aValue );
  }

}
