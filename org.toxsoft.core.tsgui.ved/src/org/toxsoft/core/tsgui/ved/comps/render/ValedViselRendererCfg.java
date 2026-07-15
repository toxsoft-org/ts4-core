package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;

public class ValedViselRendererCfg
    extends AbstractValedLabelAndButton<ViselRendererCfg> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".ViselRendererCfg"; //$NON-NLS-1$

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
    protected IValedControl<ViselRendererCfg> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedViselRendererCfg( aContext );
    }

    @Override
    protected boolean isSuitableRawEditor( Class<?> aValueClass, ITsGuiContext aEditorContext ) {
      return aValueClass.equals( ViselRendererCfg.class );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  ViselRendererCfg rendererCfg = null;

  protected ValedViselRendererCfg( ITsGuiContext aTsContext ) {
    super( aTsContext );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  // ------------------------------------------------------------------------------------
  // AbstractValedLabelAndButton
  //

  @Override
  protected boolean doProcessButtonPress() {
    ViselRendererCfg cfg = PanelRendererProps.editConfig( rendererCfg, tsContext() );
    if( cfg == null ) {
      return false;
    }
    rendererCfg = cfg;
    return true;
  }

  @Override
  protected void doUpdateLabelControl() {
    String s = TsLibUtils.EMPTY_STRING;
    if( rendererCfg != null ) {
      s = rendererCfg.nmName();
    }
    getLabelControl().setText( s );
  }

  @Override
  protected ViselRendererCfg doGetUnvalidatedValue() {
    return rendererCfg;
  }

  @Override
  protected void doDoSetUnvalidatedValue( ViselRendererCfg aValue ) {
    rendererCfg = aValue;
  }

}
