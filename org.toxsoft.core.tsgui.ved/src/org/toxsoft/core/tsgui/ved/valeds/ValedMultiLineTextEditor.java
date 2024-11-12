package org.toxsoft.core.tsgui.ved.valeds;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.jface.window.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;

/**
 * Редактор произвольного многострочного текста.
 *
 * @author vs
 */
public class ValedMultiLineTextEditor
    extends AbstractValedLabelAndButton<String> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".MultiLineText"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author vs
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
    protected IValedControl<String> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedMultiLineTextEditor( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( String.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  String text = TsLibUtils.EMPTY_STRING;

  /**
   * Constructor.
   *
   * @param aContext {@link ITsGuiContext} - соответствующий контекст
   */
  protected ValedMultiLineTextEditor( ITsGuiContext aContext ) {
    super( aContext );
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedLabelAndButton
  //

  @Override
  protected void doUpdateLabelControl() {
    getLabelControl().setText( text );
  }

  @Override
  protected boolean doProcessButtonPress() {
    MultiLineInputDialog dlg = new MultiLineInputDialog( getShell(), "Ввод текста", "Тест: ", text, null );
    if( dlg.open() == Window.OK ) {
      text = dlg.getValue();
      return true;
    }
    return false;
  }

  @Override
  protected void doDoSetUnvalidatedValue( String aValue ) {
    text = aValue;
  }

  @Override
  protected String doGetUnvalidatedValue() {
    return text;
  }

}
