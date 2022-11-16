package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.fonts.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор значений типа {@link IFontInfo} в виде нередактируемой строки текста с кнопкой вызова диалога выбора.
 * <p>
 * Редактор не возвращает <code>null</code>, а {@link IFontInfo#NULL}, если шрифт не задан. Не заданный грифт имеет
 * смысл, например чтобы обозначить понятие "использовать шрифт по умолчанию". Задание значения <code>null</code> в
 * методе {@link IValedControl#setValue(Object)} равнозначно заданию значения {@link IFontInfo#NULL}.
 *
 * @author goga
 */
public class ValedSimpleFontInfo
    extends AbstractValedSimpleFontInfo<IFontInfo> {

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
    protected IValedControl<IFontInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedSimpleFontInfo( aContext );
    }

  }

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SimpleFontInfo"; //$NON-NLS-1$

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
  ValedSimpleFontInfo( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  // ------------------------------------------------------------------------------------
  // Реализация методов базового класса
  //

  @Override
  protected IFontInfo doGetUnvalidatedValue() {
    return getFontInfo();
  }

  @Override
  protected void doSetUnvalidatedValue( IFontInfo aValue ) {
    setFontInfo( aValue );
  }

}
