package org.toxsoft.core.tsgui.ved.screen.impl;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;

import org.toxsoft.core.tsgui.ved.screen.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Utility and helper methods.
 *
 * @author hazard157
 * @author vs
 */
public class VedScreenUtils {

  /**
   * Creates current configuration of the VED screen model.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link VedScreenCfg} - created instance of the configuration
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static VedScreenCfg getVedScreenConfig( IVedScreen aVedScreen ) {
    TsNullArgumentRtException.checkNull( aVedScreen );
    VedScreenCfg scrCfg = new VedScreenCfg();
    IVedScreenModel sm = aVedScreen.model();
    for( VedAbstractVisel item : sm.visels().list() ) {
      VedItemCfg cfg = VedItemCfg.ofVisel( item.id(), item.factoryId(), item.params(), item.props() );
      scrCfg.viselCfgs().add( cfg );
    }
    for( VedAbstractActor item : sm.actors().list() ) {
      VedItemCfg cfg = VedItemCfg.ofActor( item.id(), item.factoryId(), item.params(), item.props() );
      scrCfg.actorCfgs().add( cfg );
    }
    scrCfg.canvasCfg().copyFrom( aVedScreen.view().canvasConfig() );
    return scrCfg;
  }

  /**
   * Sets configuration to the VED screen.
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @param aScreenCfg {@link IVedScreenCfg} - configuration to be applied to the screen
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public static void setVedScreenConfig( IVedScreen aVedScreen, IVedScreenCfg aScreenCfg ) {
    TsNullArgumentRtException.checkNulls( aVedScreen, aScreenCfg );
    IVedScreenModel sm = aVedScreen.model();
    try {
      sm.actors().eventer().pauseFiring();
      sm.visels().eventer().pauseFiring();
      sm.actors().clear();
      sm.visels().clear();
      for( IVedItemCfg cfg : aScreenCfg.viselCfgs() ) {
        sm.visels().create( cfg );
      }
      for( IVedItemCfg cfg : aScreenCfg.actorCfgs() ) {
        sm.actors().create( cfg );
      }
      aVedScreen.view().setCanvasConfig( aScreenCfg.canvasCfg() );
    }
    finally {
      sm.visels().eventer().resumeFiring( true );
      sm.actors().eventer().resumeFiring( true );
    }
  }

  /**
   * Returns list of visel ids that have no assigned actors.>br>
   *
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link IStringList} - list of visel ids that have no assigned actors
   */
  public static IStringList listOrphanViselIds( IVedScreen aVedScreen ) {
    IStringListEdit visels = new StringArrayList( aVedScreen.model().visels().list().ids() );
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      if( actor.params().hasKey( PROPID_VISEL_ID ) ) {
        String viselId = actor.params().getStr( PROPID_VISEL_ID );
        visels.remove( viselId );
      }
    }
    return visels;
  }

  /**
   * Returns actor ids, associated with this visel.<br>
   *
   * @param aViselId String - visel id
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link IStringList} - actor ids, associated with this visel
   */
  public static IStringList viselActorIds( String aViselId, IVedScreen aVedScreen ) {
    IStringListEdit result = new StringArrayList();
    for( IVedActor actor : aVedScreen.model().actors().list() ) {
      if( actor.props().hasKey( PROPID_VISEL_ID ) ) {
        if( actor.props().getStr( PROPID_VISEL_ID ).equals( aViselId ) ) {
          result.add( actor.id() );
        }
      }
    }
    return result;
  }

  /**
   * Returns actor ids, associated with this visel.<br>
   *
   * @param aViselId String - visel id
   * @param aActorsCfg IStridablesList&lt;VedItemCfg> - list of actors configurations
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return {@link IStringList} - actor ids, associated with this visel
   */
  public static IStridablesList<VedItemCfg> viselActorsConfigs( String aViselId, IStridablesList<VedItemCfg> aActorsCfg,
      IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( VedItemCfg actorCfg : aActorsCfg ) {
      if( actorCfg.propValues().hasKey( PROPID_VISEL_ID ) ) {
        if( actorCfg.propValues().getStr( PROPID_VISEL_ID ).equals( aViselId ) ) {
          result.add( actorCfg );
        }
      }
    }
    return result;
  }

  /**
   * Returns copy of visel configuration.<br>
   *
   * @param aViselId String - visel id
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return VedItemCfg - copy of visel configuration
   */
  public static VedItemCfg createCopyOfViselConfig( String aViselId, IVedScreen aVedScreen ) {
    IVedVisel visel = aVedScreen.model().visels().list().getByKey( aViselId );
    VedItemCfg cfg = VedItemCfg.ofVisel( visel.id(), visel.factoryId(), visel.params(), visel.props() );
    return aVedScreen.model().visels().prepareFromTemplate( cfg );
  }

  /**
   * Returns copy of actor configuration.<br>
   *
   * @param aActorId String - actor id
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return VedItemCfg - copy of visel configuration
   */
  public static VedItemCfg createCopyOfActorConfig( String aActorId, IVedScreen aVedScreen ) {
    IVedActor actor = aVedScreen.model().actors().list().getByKey( aActorId );
    VedItemCfg cfg = VedItemCfg.ofVisel( actor.id(), actor.factoryId(), actor.params(), actor.props() );
    return aVedScreen.model().actors().prepareFromTemplate( cfg );
  }

  /**
   * Returns visel configuration list.<br>
   *
   * @param aViselIds {@link IStringList} - list of visel ids
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;VedItemCfg> - visel configuration list
   */
  public static IStridablesList<VedItemCfg> listViselConfigs( IStringList aViselIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( String id : aViselIds ) {
      IVedVisel visel = aVedScreen.model().visels().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofVisel( visel.id(), visel.factoryId(), visel.params(), visel.props() );
      result.add( cfg );
    }
    return result;
  }

  /**
   * Returns actor configuration list.<br>
   *
   * @param aActorIds {@link IStringList} - list of actor ids
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;VedItemCfg> - actor configuration list
   */
  public static IStridablesList<VedItemCfg> listActorConfigs( IStringList aActorIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( String id : aActorIds ) {
      IVedActor actor = aVedScreen.model().actors().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofActor( actor.id(), actor.factoryId(), actor.params(), actor.props() );
      result.add( cfg );
    }
    return result;
  }

  /**
   * Returns copy of visel configuration list.<br>
   *
   * @param aViselIds {@link IStringList} - list of visel ids
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;VedItemCfg> - copy of visel configuration list
   */
  public static IStridablesList<VedItemCfg> listCopyOfViselConfigs( IStringList aViselIds, IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( String id : aViselIds ) {
      IVedVisel visel = aVedScreen.model().visels().list().getByKey( id );
      VedItemCfg cfg = VedItemCfg.ofVisel( visel.id(), visel.factoryId(), visel.params(), visel.props() );
      VedItemCfg newCfg = aVedScreen.model().visels().prepareFromTemplate( cfg );
      result.add( newCfg );
    }
    return result;
  }

  /**
   * Returns copy of visel configurations list.<br>
   *
   * @param aViselCfgs IStridablesList&lt;VedItemCfg> - list of visel configurations
   * @param aVedScreen {@link IVedScreen} - the VED screen
   * @return IStridablesList&lt;VedItemCfg> - copy of visel configuration list
   */
  public static IStridablesList<VedItemCfg> createCopyOfViselConfigs( IStridablesList<VedItemCfg> aViselCfgs,
      IVedScreen aVedScreen ) {
    IStridablesListEdit<VedItemCfg> result = new StridablesList<>();
    for( VedItemCfg cfg : aViselCfgs ) {
      VedItemCfg config = VedItemCfg.ofVisel( cfg.id(), cfg.factoryId(), cfg.params(), cfg.propValues() );
      VedItemCfg newCfg = aVedScreen.model().visels().prepareFromTemplate( config );
      result.add( newCfg );
    }
    return result;
  }

  /**
   * No subclasses.
   */
  private VedScreenUtils() {
    // nop
  }

}
