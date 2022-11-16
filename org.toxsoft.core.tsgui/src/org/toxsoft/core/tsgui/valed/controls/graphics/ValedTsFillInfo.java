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
public class ValedTsFillInfo
    extends AbstractValedLabelAndButton<TsFillInfo> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsFillInfo"; //$NON-NLS-1$

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
    protected IValedControl<TsFillInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsFillInfo( aContext );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsFillInfo value = TsFillInfo.NONE;

  /**
   * Конструктор.
   *
   * @param aTsContext ITsGuiContext - соответствующий контекст
   */
  protected ValedTsFillInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
  }

  // ------------------------------------------------------------------------------------
  // {@link AbstractValedLabelAndButton}
  //

  @Override
  protected void doProcessButtonPress() {
    TsFillInfo fi = TsFillInfo.NONE;
    if( value != TsFillInfo.NONE ) {
      fi = value;
    }
    fi = PanelTsFillInfoSelector.editPattern( fi, tsContext() );
    if( fi != null ) {
      value = fi;
      fireModifyEvent( true );
    }
  }

  @Override
  protected TsFillInfo doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doSetUnvalidatedValue( TsFillInfo aValue ) {
    value = aValue;
  }

}
