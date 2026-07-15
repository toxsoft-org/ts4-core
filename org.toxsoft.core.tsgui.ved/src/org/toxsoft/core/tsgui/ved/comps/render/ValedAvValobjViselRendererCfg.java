package org.toxsoft.core.tsgui.ved.comps.render;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.av.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.errors.*;

public class ValedAvValobjViselRendererCfg
    extends AbstractAvValobjWrapperValedControl<ViselRendererCfg> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".AvValobjAvValobjViselRendererCfg"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<IAtomicValue> doCreateEditor( ITsGuiContext aContext ) {
      return new ValedAvValobjViselRendererCfg( aContext );
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the editor context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public ValedAvValobjViselRendererCfg( ITsGuiContext aTsContext ) {
    super( aTsContext, ValedViselRendererCfg.FACTORY );
  }

}
