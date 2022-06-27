package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tsgui.ved.api.view.*;

/**
 * {@link IVedEnvironment} implementation.
 *
 * @author hazard157
 */
class VedEnvironment
    implements IVedEnvironment {

  private final IVedLibraryManager libraryManager = new VedLibraryManager();

  /**
   * Constructor.
   */
  public VedEnvironment() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @Override
  public IVedDataModel dataModel() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedScreenManager screenManager() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedLibraryManager libraryManager() {
    return libraryManager;
  }

}
