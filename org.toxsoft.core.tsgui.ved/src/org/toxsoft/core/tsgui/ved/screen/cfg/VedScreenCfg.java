package org.toxsoft.core.tsgui.ved.screen.cfg;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.txtproj.lib.storage.*;

/**
 * {@link IVedScreenCfg} implementation.
 *
 * @author hazard157
 */
public final class VedScreenCfg
    implements IVedScreenCfg, IParameterizedEdit {

  /**
   * The keeper singleton.
   * <p>
   * Read configuration may safely be casted to {@link VedScreenCfg}.
   */
  public static final IEntityKeeper<IVedScreenCfg> KEEPER =
      new AbstractEntityKeeper<>( IVedScreenCfg.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        private static final String KW_PARAMS    = "Params";       //$NON-NLS-1$
        private static final String KW_VISEL_CONFIGS = "ViselConfigs"; //$NON-NLS-1$
        private static final String KW_ACTOR_CONFIGS = "ActorConfigs"; //$NON-NLS-1$
        private static final String KW_CANVAS_CONFIG = "CanvasConfig"; //$NON-NLS-1$
        private static final String KW_EXTRA_DATA = "ExtraData";    //$NON-NLS-1$

        @Override
        protected void doWrite( IStrioWriter aSw, IVedScreenCfg aEntity ) {
          // Parameterized
          aSw.incNewLine();
          StrioUtils.writeKeywordHeader( aSw, KW_PARAMS, true );
          OptionSetKeeper.KEEPER_INDENTED.write( aSw, aEntity.params() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // viselCfgs
          StrioUtils.writeCollection( aSw, KW_VISEL_CONFIGS, aEntity.viselCfgs(), VedItemCfg.KEEPER, true );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // actorCfgs
          StrioUtils.writeCollection( aSw, KW_ACTOR_CONFIGS, aEntity.actorCfgs(), VedItemCfg.KEEPER, true );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // canvasCfg
          StrioUtils.writeKeywordHeader( aSw, KW_CANVAS_CONFIG, true );
          VedCanvasCfg.KEEPER.write( aSw, aEntity.canvasCfg() );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // extraData
          StrioUtils.writeKeywordHeader( aSw, KW_EXTRA_DATA, true );
          VedScreenCfg scrCfg = VedScreenCfg.class.cast( aEntity );
          scrCfg.extraData.write( aSw );
          aSw.decNewLine();
        }

        @Override
        protected IVedScreenCfg doRead( IStrioReader aSr ) {
          // Parameterized
          VedScreenCfg scrCfg = new VedScreenCfg();
          StrioUtils.ensureKeywordHeader( aSr, KW_PARAMS );
          IOptionSet params = OptionSetKeeper.KEEPER.read( aSr );
          scrCfg.params().setAll( params );
          aSr.ensureSeparatorChar();
          // viselCfgs
          IList<IVedItemCfg> viselCfgs = StrioUtils.readCollection( aSr, KW_VISEL_CONFIGS, VedItemCfg.KEEPER );
          aSr.ensureSeparatorChar();
          scrCfg.viselCfgs.setAll( viselCfgs );
          // actorCfgs
          IList<IVedItemCfg> actorCfgs = StrioUtils.readCollection( aSr, KW_ACTOR_CONFIGS, VedItemCfg.KEEPER );
          aSr.ensureSeparatorChar();
          scrCfg.actorCfgs.setAll( actorCfgs );
          // canvasCfg
          StrioUtils.ensureKeywordHeader( aSr, KW_CANVAS_CONFIG );
          IVedCanvasCfg canvasCfg = VedCanvasCfg.KEEPER.read( aSr );
          aSr.ensureSeparatorChar();
          scrCfg.canvasCfg.copyFrom( canvasCfg );
          // extraData
          StrioUtils.ensureKeywordHeader( aSr, KW_EXTRA_DATA );
          scrCfg.extraData.read( aSr );
          return scrCfg;
        }
      };

  private final IStridablesListEdit<IVedItemCfg> viselCfgs = new StridablesList<>();
  private final IStridablesListEdit<IVedItemCfg> actorCfgs = new StridablesList<>();

  private final IOptionSetEdit       params    = new OptionSet();
  private VedCanvasCfg               canvasCfg = new VedCanvasCfg();
  private KeepablesStorageAsKeepable extraData = new KeepablesStorageAsKeepable();

  /**
   * Constructor.
   */
  public VedScreenCfg() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // IParameterizedEdit
  //

  @Override
  public IOptionSetEdit params() {
    return params;
  }

  // ------------------------------------------------------------------------------------
  // IViScreenCfg
  //

  @Override
  public IStridablesListEdit<IVedItemCfg> viselCfgs() {
    return viselCfgs;
  }

  @Override
  public IStridablesListEdit<IVedItemCfg> actorCfgs() {
    return actorCfgs;
  }

  @Override
  public VedCanvasCfg canvasCfg() {
    return canvasCfg;
  }

  @Override
  public IKeepablesStorageRo extraData() {
    return extraData;
  }

}
