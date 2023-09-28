package org.toxsoft.core.tsgui.ved.impl;

import static org.toxsoft.core.tsgui.ved.l10n.ITsguiVedSharedResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.ved.api.*;
import org.toxsoft.core.tsgui.ved.api.cfg.*;
import org.toxsoft.core.tsgui.ved.api.helpers.*;
import org.toxsoft.core.tsgui.ved.api.items.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.basis.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;

/**
 * {@link IVedEnvironment} implementation.
 *
 * @author hazard157
 */
public class VedEnvironment
    implements IVedEnvironment, ITsClearable {

  private final ITsGuiContext tsContext;

  private final IStridablesListEdit<VedAbstractVisel> viselsList = new StridablesList<>();
  private final IStridablesListEdit<VedAbstractActor> actorsList = new StridablesList<>();

  /**
   * Constructor.
   *
   * @param aTsContext {@link ITsGuiContext} - the context
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public VedEnvironment( ITsGuiContext aTsContext ) {
    tsContext = aTsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @Override
  public ITsGuiContext tsContext() {
    return tsContext;
  }

  // ------------------------------------------------------------------------------------
  // IVedEnvironment
  //

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IStridablesList<IVedVisel> visels() {
    return (IStridablesList)viselsList;
  }

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IStridablesList<IVedActor> actors() {
    return (IStridablesList)actorsList;
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Returns an editable list of the VISELs.
   *
   * @return {@link IStridablesListEdit}&lt;{@link VedAbstractVisel}&gt; - the ordered list of VISELs
   */
  public IStridablesListEdit<VedAbstractVisel> viselsList() {
    return viselsList;
  }

  /**
   * Returns an editable list of the actors.
   *
   * @return {@link IStridablesListEdit}&lt;{@link VedAbstractActor}&gt; - the ordered list of actors
   */
  public IStridablesListEdit<VedAbstractActor> actorsList() {
    return actorsList;
  }

  /**
   * Creates items and adds them to the internal lists.
   *
   * @param aScreenCfg {@link IVedScreenCfg} - configurationfor items creation
   */
  public void createItems( IVedScreenCfg aScreenCfg ) {
    // create the VISELs and add to the VED environment
    IVedViselFactoriesRegistry vfReg = tsContext().get( IVedViselFactoriesRegistry.class );
    for( IVedItemCfg cfg : aScreenCfg.viselCfgs() ) {
      IVedViselFactory factory = vfReg.find( cfg.factoryId() );
      if( factory != null ) {
        try {
          VedAbstractVisel visel = factory.create( cfg, this );
          viselsList.add( visel );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex, FMT_ERR_CAN_CREATE_VISEL, ex.getMessage() );
        }
      }
      else {
        LoggerUtils.errorLogger().warning( FMT_WARN_UNKNON_VISEL_FACTORY, cfg.factoryId() );
      }
    }
    // create the actors and add to the VED environment
    IVedActorFactoriesRegistry afReg = tsContext().get( IVedActorFactoriesRegistry.class );
    for( IVedItemCfg cfg : aScreenCfg.actorCfgs() ) {
      IVedActorFactory factory = afReg.find( cfg.factoryId() );
      if( factory != null ) {
        try {
          VedAbstractActor actor = factory.create( cfg, this );
          actorsList.add( actor );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex, FMT_ERR_CAN_CREATE_ACTOR, ex.getMessage() );
        }
      }
      else {
        LoggerUtils.errorLogger().warning( FMT_WARN_UNKNON_ACTOR_FACTORY, cfg.factoryId() );
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // ITsClearable
  //

  /**
   * Removes all items and calls {@link IDisposable#dispose() item.dispose()} on them.
   * <p>
   * {@inheritDoc}
   */

  @Override
  public void clear() {
    // remove and dispose actors
    while( !actorsList.isEmpty() ) {
      VedAbstractActor actor = actorsList.last();
      // actor.disable();
      actor.dispose();
      actorsList.remove( actor );
    }
    // remove and dispose VISELs
    while( !viselsList.isEmpty() ) {
      VedAbstractVisel visel = viselsList.last();
      // visel.hide();
      visel.dispose();
      viselsList.remove( visel );
    }
  }

}