package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.shadow.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * Редактор атрибутов для рисования контура {@link Path}.
 * <p>
 *
 * @author vs
 */
public class ValedTsShadowInfo
    extends AbstractValedLabelAndButton<TsShadowInfo> {

  /**
   * ИД параметра, содержащего максимальный радиус размытия
   */
  public final static String PARAMID_MAX_BLUR = "maxBlur"; //$NON-NLS-1$

  /**
   * ИД параметра, содержащего максимальный сдвиг по оси X
   */
  public final static String PARAMID_MAX_XSHIFT = "maxXshift"; //$NON-NLS-1$

  /**
   * ИД параметра, содержащего максимальный сдвиг по оси Y
   */
  public final static String PARAMID_MAX_YSHIFT = "maxYshift"; //$NON-NLS-1$

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsShadowInfo"; //$NON-NLS-1$

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
    protected IValedControl<TsShadowInfo> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsShadowInfo( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( TsShadowInfo.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsShadowInfo shadowInfo = TsShadowInfo.NONE;

  protected ValedTsShadowInfo( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    TsShadowInfo pd = PanelTsShadowInfoEditor.editPathData( shadowInfo, tsContext() );
    if( pd == null ) {
      return false;
    }
    shadowInfo = pd;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    // TODO what to write/display in label ?
  }

  @Override
  protected TsShadowInfo doGetUnvalidatedValue() {
    return shadowInfo;
  }

  @Override
  protected void doDoSetUnvalidatedValue( TsShadowInfo aValue ) {
    shadowInfo = aValue;
  }

}
