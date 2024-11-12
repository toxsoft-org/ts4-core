package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.graphics.patterns.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.bricks.events.change.*;

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

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( TsFillInfo.class );
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
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // {@link AbstractValedLabelAndButton}
  //

  @Override
  protected boolean doProcessButtonPress() {
    TsFillInfo fi = TsFillInfo.NONE;
    if( value != TsFillInfo.NONE ) {
      fi = value;
    }
    ITsGuiContext ctx = prepareContext();
    fi = PanelTsFillInfoSelector.editPattern( fi, ctx );
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
  protected TsFillInfo doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doDoSetUnvalidatedValue( TsFillInfo aValue ) {
    value = aValue;
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  ITsGuiContext prepareContext() {
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    if( tsContext().hasKey( IValedControlValueChangeListener.class ) ) {
      IValedControlValueChangeListener valedListener = tsContext().get( IValedControlValueChangeListener.class );

      IGenericChangeListener changeListener = aSource -> {
        value = ((PanelTsFillInfoSelector)aSource).getDataRecord();
        // if( aSource instanceof PanelColorFillInfo ) {
        // RGBA rgba = ((PanelColorFillInfo)aSource).rgbaSelector.rgba();
        // value = new TsFillInfo( rgba );
        // }
        valedListener.onEditorValueChanged( ValedTsFillInfo.this, true );
      };
      ctx.put( IGenericChangeListener.class, changeListener );
    }
    return ctx;
  }

}
