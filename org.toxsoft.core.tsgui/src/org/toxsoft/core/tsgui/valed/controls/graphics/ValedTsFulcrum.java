package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * Редактор параметров опорной точки.
 * <p>
 *
 * @author vs
 */
public class ValedTsFulcrum
    extends AbstractValedLabelAndButton<TsFulcrum> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsFulcrum"; //$NON-NLS-1$

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
    protected IValedControl<TsFulcrum> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsFulcrum( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( TsFulcrum.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsFulcrum fulcrum = TsFulcrum.of( ETsFulcrum.LEFT_TOP );

  protected ValedTsFulcrum( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    TsFulcrum li = PanelTsFulcrumEditor.edit( fulcrum, tsContext() );
    if( li == null ) {
      return false;
    }
    fulcrum = li;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    getLabelControl().setText( fulcrum.toString() );
  }

  @Override
  protected TsFulcrum doGetUnvalidatedValue() {
    return fulcrum;
  }

  @Override
  protected void doDoSetUnvalidatedValue( TsFulcrum aValue ) {
    fulcrum = aValue;
  }

}
