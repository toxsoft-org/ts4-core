package org.toxsoft.core.tsgui.valed.controls.graphics;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.eclipse.swt.graphics.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.graphics.path.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;

/**
 * Редактор атрибутов для рисования контура {@link Path}.
 * <p>
 *
 * @author vs
 */
public class ValedTsPathData
    extends AbstractValedLabelAndButton<TsPathData> {

  /**
   * Название фабрики, с которым она зарегистрирована в {@link ValedControlFactoriesRegistry}.
   * <p>
   * Напомним, что автоматическая регистрация с именем класса фабрики тоже работает.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".TsPathData"; //$NON-NLS-1$

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
    protected IValedControl<TsPathData> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedTsPathData( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( TsPathData.class );
    }

  }

  /**
   * Синглтон Фабрики.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  TsPathData pathData = TsPathData.NONE;

  protected ValedTsPathData( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  //
  //

  @Override
  protected boolean doProcessButtonPress() {
    TsPathData pd = PanelTsPathDataEditor.editPathData( pathData, tsContext() );
    if( pd == null ) {
      return false;
    }
    pathData = pd;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    // TODO what to write/display in label ?
  }

  @Override
  protected TsPathData doGetUnvalidatedValue() {
    return pathData;
  }

  @Override
  protected void doDoSetUnvalidatedValue( TsPathData aValue ) {
    pathData = aValue;
  }

}
