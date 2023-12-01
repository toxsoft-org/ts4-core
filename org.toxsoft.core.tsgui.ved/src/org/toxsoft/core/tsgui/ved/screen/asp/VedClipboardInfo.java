package org.toxsoft.core.tsgui.ved.screen.asp;

import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tslib.bricks.keeper.*;
import org.toxsoft.core.tslib.bricks.keeper.AbstractEntityKeeper.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strio.*;
import org.toxsoft.core.tslib.bricks.strio.impl.*;
import org.toxsoft.core.tslib.coll.*;

/**
 * VED clipboard information for copy/paste operations.
 *
 * @author vs
 */
public class VedClipboardInfo {

  /**
   * The keeper singleton.
   * <p>
   * Read configuration may safely be casted to {@link VedClipboardInfo}.
   */
  public static final IEntityKeeper<VedClipboardInfo> KEEPER =
      new AbstractEntityKeeper<>( VedClipboardInfo.class, EEncloseMode.ENCLOSES_BASE_CLASS, null ) {

        private static final String KW_VISEL_CONFIGS = "ViselConfigs"; //$NON-NLS-1$
        private static final String KW_ACTOR_CONFIGS = "ActorConfigs"; //$NON-NLS-1$

        @Override
        protected void doWrite( IStrioWriter aSw, VedClipboardInfo aEntity ) {
          // visels2paste
          StrioUtils.writeCollection( aSw, KW_VISEL_CONFIGS, aEntity.viselCfgs(), VedItemCfg.KEEPER, true );
          aSw.writeSeparatorChar();
          aSw.writeEol();
          // actors2paste
          StrioUtils.writeCollection( aSw, KW_ACTOR_CONFIGS, aEntity.actorCfgs(), VedItemCfg.KEEPER, true );
          aSw.writeSeparatorChar();
          aSw.writeEol();
        }

        @Override
        protected VedClipboardInfo doRead( IStrioReader aSr ) {
          // viselCfgs
          IList<IVedItemCfg> viselCfgs = StrioUtils.readCollection( aSr, KW_VISEL_CONFIGS, VedItemCfg.KEEPER );
          aSr.ensureSeparatorChar();
          // actorCfgs
          IList<IVedItemCfg> actorCfgs = StrioUtils.readCollection( aSr, KW_ACTOR_CONFIGS, VedItemCfg.KEEPER );
          aSr.ensureSeparatorChar();
          return new VedClipboardInfo( viselCfgs, actorCfgs );
        }
      };

  private final IStridablesListEdit<IVedItemCfg> visels2paste = new StridablesList<>();

  private final IStridablesListEdit<IVedItemCfg> actors2paste = new StridablesList<>();

  // VedClipboardInfo() {
  // // nop
  // }

  VedClipboardInfo( IList<IVedItemCfg> aViselCfgs, IList<IVedItemCfg> aActorCfgs ) {
    visels2paste.addAll( aViselCfgs );
    actors2paste.addAll( aActorCfgs );
  }

  // // ------------------------------------------------------------------------------------
  // // API
  // //
  //
  // /**
  // * Добавляет конфигурацию визеля в список для вставки.
  // *
  // * @param aViselCfg {@link VedItemCfg} - конфигурация визеля
  // */
  // public void addViselCfg( VedItemCfg aViselCfg ) {
  // visels2paste.add( aViselCfg );
  // }
  //
  // /**
  // * Добавляет конфигурацию актора в список для вставки.
  // *
  // * @param aActorCfg {@link VedItemCfg} - конфигурация актора
  // */
  // public void addActorCfg( VedItemCfg aActorCfg ) {
  // actors2paste.add( aActorCfg );
  // }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  IStridablesList<IVedItemCfg> viselCfgs() {
    return visels2paste;
  }

  IStridablesList<IVedItemCfg> actorCfgs() {
    return actors2paste;
  }

}
