package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class ValedAvValobjSwtPattern
    extends AbstractValedLabelAndButton<IAtomicValue> {

  private static final int IMAGE_WIDTH  = 20;
  private static final int IMAGE_HEIGHT = 20;

  private Image patternImage = null;

  private ISwtPatternInfo value = null;

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
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjSwtPattern( aContext );
    }

  }

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".SwtPattern"; //$NON-NLS-1$

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor for subclasses.
   *
   * @param aTsContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  protected ValedAvValobjSwtPattern( ITsGuiContext aTsContext ) {
    super( aTsContext );
  }

  @Override
  protected void doProcessButtonPress() {
    ISwtPatternInfo pi = null;
    if( value != null ) {
      pi = value;
    }
    pi = PanelSwtPatternSelector.editPattern( pi, tsContext() );
    if( pi != null ) {
      value = pi;
      // doSetUnvalidatedValue( AvUtils.avValobj( value ) );
      // updateTextControl();
      fireModifyEvent( true );
    }
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    return AvUtils.avValobj( value );
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    value = null;
    if( aValue == null || !aValue.isAssigned() ) {
      value = null;
    }
    else {
      if( aValue.asValobj() instanceof ISwtPatternInfo ) {
        ISwtPatternInfo info = aValue.asValobj();
        value = info;
      }
    }
    setPattern( value );
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  /**
   * Узор для заливки областей переводим в человеческое название
   *
   * @param aPatternInfo ISwtPatternInfo - параметры узора для заливки областей переводим
   * @return String человеческое название
   */
  public static String pattern2text( ISwtPatternInfo aPatternInfo ) {
    return aPatternInfo.gradientType().name();
  }

  private void updateTextControl() {
    getLabelControl().setText( TsLibUtils.EMPTY_STRING );
    if( value != null ) {
      getLabelControl().setText( pattern2text( value ) );
      // TODO отрисовать прямоугольник
      if( patternImage != null ) {
        patternImage.dispose();
      }
      if( value != null ) {
        patternImage = new Image( Display.getCurrent(), IMAGE_WIDTH, IMAGE_HEIGHT );
        GC gc = new GC( patternImage );
        Pattern p = value.createSwtPattern( value, tsContext() ).pattern( gc, IMAGE_WIDTH, IMAGE_HEIGHT );
        gc.setBackgroundPattern( p );
        gc.fillRectangle( 0, 0, patternImage.getImageData().width, patternImage.getImageData().height );
        p.dispose();
        gc.dispose();
        getLabelControl().setImage( patternImage );
      }

    }
  }

  void setPattern( ISwtPatternInfo aPatternInfo ) {
    updateTextControl();
  }

}
