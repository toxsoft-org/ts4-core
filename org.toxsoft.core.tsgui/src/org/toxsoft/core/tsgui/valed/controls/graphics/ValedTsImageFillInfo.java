package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * Редактор параметров заливки.
 * <p>
 *
 * @author vs
 */
public class ValedTsImageFillInfo
    extends AbstractValedLabelAndButton<TsImageFillInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsImageFillInfo"; //$NON-NLS-1$

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
    protected IValedControl<TsImageFillInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsImageFillInfo( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( TsImageFillInfo.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsImageFillInfo value = TsImageFillInfo.DEFAULT;

  /**
   * Конструктор.
   *
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  protected ValedTsImageFillInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // {@link AbstractValedLabelAndButton}
  //

  @Override
  protected boolean doProcessButtonPress() {
    TsImageFillInfo fi = TsImageFillInfo.DEFAULT;
    if( value != TsImageFillInfo.DEFAULT ) {
      fi = value;
    }
    fi = PanelTsImageFillInfo.edit( fi, tsContext() );
    if( fi == null ) {
      return false;
    }
    value = fi;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    // TODO what to write/display in label ?
  }

  @Override
  protected TsImageFillInfo doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doDoSetUnvalidatedValue( TsImageFillInfo aValue ) {
    value = aValue;
  }

}
