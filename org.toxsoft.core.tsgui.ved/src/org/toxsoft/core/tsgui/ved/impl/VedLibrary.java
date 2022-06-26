package org.toxsoft.core.tsgui.ved.impl;

import org.toxsoft.core.tsgui.ved.api.library.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * {@link IVedLibrary} implementation.
 *
 * @author hazard157
 */
public class VedLibrary
    extends StridableParameterized
    implements IVedLibrary {

  private final IStridablesListEdit<IVedComponentProvider>  componentProviders = new StridablesList<>();
  private final IStridablesListEdit<IVedEditorToolProvider> toolProviders      = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aId String - the library ID (an IDpath)
   * @param aParams {@link IOptionSet} - {@link #params()} value
   */
  public VedLibrary( String aId, IOptionSet aParams ) {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IVedLibrary
  //

  @Override
  public IStridablesListEdit<IVedComponentProvider> componentProviders() {
    return componentProviders;
  }

  @Override
  public IStridablesListEdit<IVedEditorToolProvider> toolProviders() {
    return toolProviders;
  }

}
