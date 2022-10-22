package org.toxsoft.core.tsgui.ved.api.impl;

import org.toxsoft.core.tsgui.ved.api.cfgdata.*;
import org.toxsoft.core.tsgui.ved.api.entity.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * {@link IVedEntityConfig} immutable implementation.
 *
 * @author hazard157
 */
public final class VedEntityConfig
    extends VedConfigBase
    implements IVedEntityConfig {

  /**
   * The keeper singleton.
   */
  public static final IEntityKeeper<IVedEntityConfig> KEEPER =
      new AbstractEntityKeeper<>( IVedEntityConfig.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        @Override
        protected void doWrite( IStrioWriter aSw, IVedEntityConfig aEntity ) {
          aSw.writeAsIs( aEntity.id() );
          aSw.writeSeparatorChar();
          EVedEntityKind.KEEPER.write( aSw, aEntity.entityKind() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.propValues() );
          aSw.writeSeparatorChar();
          OptionSetKeeper.KEEPER.write( aSw, aEntity.extdata() );
        }

        @Override
        protected IVedEntityConfig doRead( IStrioReader aSr ) {
          String id = aSr.readIdPath();
          aSr.ensureSeparatorChar();
          EVedEntityKind kind = EVedEntityKind.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet props = OptionSetKeeper.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          IOptionSet extdata = OptionSetKeeper.KEEPER.read( aSr );
          return new VedEntityConfig( id, kind, props, extdata );
        }
      };

  private final EVedEntityKind kind;

  /**
   * Constructor.
   *
   * @param aId String - entity ID
   * @param aKind {@link EVedEntityKind} - entity kind
   * @param aProps {@link IOptionSet} - properties values
   * @param aExtData {@link IOptionSet} - external data
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEntityConfig( String aId, EVedEntityKind aKind, IOptionSet aProps, IOptionSet aExtData ) {
    super( aId, aProps, aExtData );
    kind = TsNullArgumentRtException.checkNull( aKind );
  }

  // ------------------------------------------------------------------------------------
  // IVedEntityConfig
  //

  @Override
  public EVedEntityKind entityKind() {
    return kind;
  }

}
