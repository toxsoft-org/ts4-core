package org.toxsoft.core.tsgui.ved.extra.tools;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.view.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * Базовый класс для "инструментов" редактора.
 *
 * @author vs
 */
public abstract class VedAbstractEditorTool
    extends StridableParameterized
    implements IVedEditorTool {

  private boolean disposed = false;

  private IVedScreen vedScreen;

  private final IVedEnvironment vedEnv;

  protected VedAbstractEditorTool( String aId, IOptionSet aParams, IVedScreen aVedScreen, IVedEnvironment aVedEnv ) {
    super( aId, aParams );
    vedScreen = aVedScreen;
    vedEnv = aVedEnv;
  }

  // ------------------------------------------------------------------------------------
  // IIconIdable
  //

  @Override
  public String iconId() {
    return params().getStr( TSID_ICON_ID );
  }

  // ------------------------------------------------------------------------------------
  // IVedContextable
  //

  @Override
  public ITsGuiContext tsContext() {
    return vedEnv.tsContext();
  }

  // ------------------------------------------------------------------------------------
  // IDisposable
  //

  @Override
  public final boolean isDisposed() {
    return disposed;
  }

  @Override
  public final void dispose() {
    if( !disposed ) {
      disposed = true;
      doDispose();
    }
  }

  // ------------------------------------------------------------------------------------
  // to use
  //

  @Override
  public IVedScreen vedScreen() {
    return vedScreen;
  }

  // ------------------------------------------------------------------------------------
  // toOverride
  //

  protected void doDispose() {
    // nop
  }
}
