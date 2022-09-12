package org.toxsoft.core.tsgui.ved.extra.ctrl;

import org.toxsoft.core.tslib.av.props.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * {@link IVedComponentController} base implementation.
 *
 * @author hazard157
 */
public non-sealed class VedAbstractComponentController
    extends StridableParameterized
    implements IVedComponentController {

  public VedAbstractComponentController() {
    // TODO Auto-generated constructor stub
  }

  // ------------------------------------------------------------------------------------
  // IPropertable
  //

  @Override
  public IPropertiesSet props() {
    // TODO Auto-generated method stub
    return null;
  }

  // ------------------------------------------------------------------------------------
  // IVedComponentController
  //

  @Override
  public IVedComponentControllerProvider provider() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IList<IVedCompLink> links() {
    // TODO Auto-generated method stub
    return null;
  }

}
