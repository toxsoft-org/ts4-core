package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.d2.*;

/**
 * Редактор параметров заливки.
 * <p>
 *
 * @author vs
 */
public class ValedD2Conversion
    extends AbstractValedLabelAndButton<ID2Conversion> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ID2Conversion"; //$NON-NLS-1$

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
    protected IValedControl<ID2Conversion> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedD2Conversion( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( ID2Conversion.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  ID2Conversion value = ID2Conversion.NONE;

  /**
   * Конструктор.
   *
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  protected ValedD2Conversion( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // {@link AbstractValedLabelAndButton}
  //

  @Override
  protected boolean doProcessButtonPress() {
    ID2Conversion d2Conv = ID2Conversion.NONE;
    if( value != ID2Conversion.NONE ) {
      d2Conv = value;
    }
    d2Conv = PanelD2ConversionEditor.editD2Conversion( d2Conv, tsContext() );
    if( d2Conv == null ) {
      return false;
    }
    value = d2Conv;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    if( value == null ) {
      getLabelControl().setText( "-" ); //$NON-NLS-1$
      return;
    }
    getLabelControl().setText( value.toString() );
  }

  @Override
  protected ID2Conversion doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doDoSetUnvalidatedValue( ID2Conversion aValue ) {
    value = aValue;
  }

}
