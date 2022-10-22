package org.toxsoft.core.tsgui.ved.api.impl;

import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedBindingCfg} immutable implementation.
 *
 * @author hazard157
 */
public class VedBindingCfg
    extends VedConfigBase
    implements IVedBindingCfg {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IVedBindingCfg> KEEPER =
      new AbstractEntityKeeper<>( IVedBindingCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IVedBindingCfg aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.bindingProviderId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.componentId() );
          aSw.writeSeparatorChar();
          aSw.writeAsIs( aEntity.propertyId() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.extdata() );
        }

        @Override
        protected IVedBindingCfg doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String provId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String compId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          String propId = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          IOptionSet props = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet extdata = OptionSetKeeper.KEEPER.read( aSr );
          return new VedBindingCfg( id, provId, compId, propId, props, extdata );
        }
      };

  private final String bindingProviderId;
  private final String componentId;
  private final String propertyId;

  /**
   * Constructor.
   *
   * @param aId String - the ID
   * @param aProviderId String - the binding provider ID
   * @param aCompId String - the bind component ID
   * @param aPropId String - the bind property ID
   * @param aProps {@link IOptionSet} - properties values
   * @param aExtData {@link IOptionSet} - external data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public VedBindingCfg( String aId, String aProviderId, String aCompId, String aPropId, IOptionSet aProps,
      IOptionSet aExtData ) {
    super( aId, aProps, aExtData );
    bindingProviderId = StridUtils.checkValidIdPath( aProviderId );
    componentId = StridUtils.checkValidIdPath( aCompId );
    propertyId = StridUtils.checkValidIdPath( aPropId );
  }

  // ------------------------------------------------------------------------------------
  // IVedBindingCfg
  //

  @Override
  public String bindingProviderId() {
    return bindingProviderId;
  }

  @Override
  public String componentId() {
    return componentId;
  }

  @Override
  public String propertyId() {
    return propertyId;
  }

}
