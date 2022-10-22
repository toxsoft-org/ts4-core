package org.toxsoft.core.tsgui.ved.api.impl;

import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.incub.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.coll.primtypes.*;

/**
 * {@link IVedDocumentData} implementation.
 *
 * @author hazard157
 */
class VedDocumentData
    implements IVedDocumentData {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IVedDocumentData> KEEPER =
      new AbstractEntityKeeper<>( IVedDocumentData.class, EEncloseMode.ENCLOSES_BASE_CLASS, new VedDocumentData() ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IVedDocumentData aEntity ) {
          // TODO Auto-generated method stub

        }

        @Override
        protected IVedDocumentData doRead( IStrioReader aSr ) {
          // TODO Auto-generated method stub
          return null;
        }
      };

  public VedDocumentData() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IVedDocumentData
  //

  @Override
  public IOptionSet documentPropValues() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStridablesList<IVedEntityConfig> componentConfigs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStridablesList<IVedEntityConfig> tailorConfigs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStringMap<IStridablesList<IVedBindingCfg>> tailorBindingConfigs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IStridablesList<IVedEntityConfig> actorConfigs() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public IKtorSectionsContainer secitonsData() {
    // TODO Auto-generated method stub
    return null;
  }

}
