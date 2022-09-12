package org.toxsoft.core.tsgui.ved.extra.ctrl;

import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tsgui.ved.core.library.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;

/**
 * {@link IVedCompLinkInfo} base implementation.
 *
 * @author hazard157
 */
public non-sealed class VedAbstractCompLinkInfo
    extends StridableParameterized
    implements IVedCompLinkInfo {

  public VedAbstractCompLinkInfo() {
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // IVedCompLinkInfo
  //

  @Override
  public boolean acceptsComponentsOfProvider( IVedComponentProvider aComponentProvider ) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public IVedCompLink createLink( IVedComponent aComponent ) {
    // TODO Auto-generated method stub
    return null;
  }

}
