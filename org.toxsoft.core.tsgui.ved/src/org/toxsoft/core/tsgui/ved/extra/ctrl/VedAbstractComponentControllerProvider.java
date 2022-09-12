package org.toxsoft.core.tsgui.ved.extra.ctrl;

import org.toxsoft.core.tsgui.ved.core.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * {@link IVedComponentControllerProvider} base implementation.
 *
 * @author hazard157
 */
public non-sealed class VedAbstractComponentControllerProvider
    extends StridableParameterized
    implements IVedComponentControllerProvider {

  // ------------------------------------------------------------------------------------
  // IVedComponentControllerProvider
  //

  @Override
  public IStridablesList<IDataDef> propDefs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStridablesList<IVedCompLinkInfo> linkDefs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IVedComponent createComponent( String aId, IVedEnvironment aVedEnv, IOptionSet aProps,
      IStringMap<String> aLinks ) {
    // TODO Auto-generated method stub
    return null;
  }

}
