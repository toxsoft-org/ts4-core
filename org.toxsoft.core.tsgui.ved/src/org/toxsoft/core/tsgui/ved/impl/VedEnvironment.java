package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;

public class VedEnvironment
    implements IVedEnvironment {

  private final ITsGuiContext tsContext;

  public VedEnvironment( ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

}
